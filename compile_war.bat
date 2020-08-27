@echo off

SET ARTIFACT=boot-2.3.1
SET MAINCLASS=com.demo.DemoApplication
SET VERSION=0.0.1-SNAPSHOT

RMDIR /Q/S build
mkdir build\native-image
mkdir build\graal-agent\META-INF\native-image
SET rootPath=%CD%\%1
echo "root" %rootPath%
SET GRAAL_AGENT_PATH=%rootPath%build\graal-agent
echo "GRAAL_AGENT_PATH" %GRAAL_AGENT_PATH%

echo "Packaging %ARTIFACT% with Gradle"
call gradlew build

echo "***************************** Setting WAR ************************************************"
SET WAR="%ARTIFACT%-%VERSION%.war"
RMDIR /Q/S %ARTIFACT%
echo "Unpacking %WAR%"
cd build/native-image
jar -xvf ../libs/%WAR%

echo "***************************** Copy jars & classes *****************************************"
xcopy "META-INF" "WEB-INF/classes" /s /e

SET rootPath=%CD%\%1

SET CP=WEB-INF\classes;WEB-INF\lib\*;WEB-INF\lib-provided\*

echo "***************************** CLASSPATH ***********************************************"
echo %CP%
echo "**************************************************************************************"

echo "============== RUNNING THE APPLICATION WITH THE AGENT TO POPULATE CONFIGURATION FILES ========="
echo "(for debug see agent-output.txt)"
echo "Running for 50000 seconds"
echo "Once tomcat starts successfully in new window hit ctrl+c to terminate tomcat gracefully. This is very important step!!!!!"

start "java-agent"  java -cp %CP% ^
  -agentlib:native-image-agent=config-merge-dir=%GRAAL_AGENT_PATH%\META-INF\native-image ^
  %MAINCLASS% > agent-output.txt

call gradlew test
TIMEOUT /T 50000

echo "Killing..."

taskkill /FI "WindowTitle eq java-agent*" /T
 echo "*****************************GRAALVM_VERSION ******************************************"
 echo native-image --version
 echo "**************************************************************************************"

echo "***************************** CLASSPATH ***********************************************"
 set CP=%CP%;%GRAAL_AGENT_PATH%;
 echo %CP%
 echo "**************************************************************************************"

echo "MAINCLASS" %MAINCLASS%

call native-image ^
        --no-server ^
        --no-fallback ^
        --initialize-at-build-time=com.sun.jmx.remote ^
        -H:Name=%ARTIFACT%-agent ^
        -H:+TraceClassInitialization ^
        -H:+RemoveSaturatedTypeFlows ^
        -Dspring.native.remove-yaml-support=true ^
        -Dspring.xml.ignore=true ^
        -Dspring.spel.ignore=true ^
        -Dspring.native.remove-jmx-support=true ^
        -Dspring.native.verify=true ^
        -cp %CP% %MAINCLASS%
