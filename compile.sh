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
native-image \
  --no-server \
  --no-fallback \
  --enable-all-security-services \
  --verbose \
  -H:Name=$ARTIFACT \
  -H:+RemoveSaturatedTypeFlows \
  -H:+ReportExceptionStackTraces \
  -H:+PrintClassInitialization \
  --rerun-class-initialization-at-runtime=org.bouncycastle.jcajce.provider.drbg.DRBG\$Default,org.bouncycastle.jcajce.provider.drbg.DRBG\$NonceAndIV \
  -Dspring.native.remove-yaml-support=true \
  -cp $CP $MAINCLASS

#   --rerun-class-initialization-at-runtime=org.bouncycastle.jcajce.provider.drbg.DRBG$Default,org.bouncycastle.jcajce.provider.drbg.DRBG$NonceAndIV \
#  --trace-class-initialization=org.bouncycastle.jcajce.provider.drbg.DRBG \
#  --initialize-at-run-time=org.bouncycastle.jcajce.provider.drbg.DRBG \
