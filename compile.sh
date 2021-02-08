#!/usr/bin/env bash

ARTIFACT=boot-2.3.1
MAINCLASS=com.demo.DemoApplication
VERSION=0.0.1-SNAPSHOT

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

rm -rf build
mkdir -p build/native-image

echo "Packaging $ARTIFACT with Gradle"
./gradlew build

JAR="$ARTIFACT-$VERSION.war"
rm -f $ARTIFACT
echo "Unpacking $JAR"
cd build/native-image
jar -xvf ../libs/$JAR >/dev/null 2>&1
cp -R META-INF WEB-INF/classes

# To run tracing agent
# java -cp WEB-INF/classes:WEB-INF/lib/*:WEB-INF/lib-provided/* -agentlib:native-image-agent=config-output-dir=../graal-agent com.vue.labs.LocalAbeServiceApplication

LIBPATH=`find WEB-INF/lib | tr '\n' ':'`
CP=WEB-INF/classes:$LIBPATH

GRAALVM_VERSION=`native-image --version`
echo "Compiling $ARTIFACT with $GRAALVM_VERSION"
{ time native-image \
  --enable-all-security-services \
  --no-server \
  --no-fallback \
  --verbose \
  -H:Name=$ARTIFACT \
  -H:+RemoveSaturatedTypeFlows \
  -H:+ReportExceptionStackTraces \
  -H:+PrintClassInitialization \
  -Dspring.native.remove-yaml-support=true \
  --rerun-class-initialization-at-runtime=org.bouncycastle.jcajce.provider.drbg.DRBG$Default,org.bouncycastle.jcajce.provider.drbg.DRBG$NonceAndIV \
  -cp $CP $MAINCLASS >> output.txt ; } 2>> output.txt

if [[ -f $ARTIFACT ]]
then
  printf "${GREEN}SUCCESS${NC}\n"
  mv ./$ARTIFACT ..
  exit 0
else
  cat output.txt
  printf "${RED}FAILURE${NC}: an error occurred when compiling the native-image.\n"
  exit 1
fi

