#!/bin/sh
LANG=C
export LATEST_VERSION=$1
export DEPLOY=$2

#export HASH=`git log|head -n 1|sed "s/^commit //g"`
packageZip(){

DIR=twitter4j-$LATEST_VERSION
mkdir $DIR
mkdir $DIR/twitter4j-core
cp -r twitter4j-core/src $DIR/twitter4j-core/
cp twitter4j-core/pom.xml $DIR/twitter4j-core/
rm $DIR/twitter4j-core/src/test/resources/twitter4j.properties
rm $DIR/twitter4j-core/src/test/resources/test.properties
rm $DIR/twitter4j-core/src/test/resources/xauth-test.properties

mkdir $DIR/twitter4j-examples
cp -r twitter4j-examples/src $DIR/twitter4j-examples/
cp twitter4j-examples/pom.xml $DIR/twitter4j-examples/
cp -r twitter4j-examples/bin $DIR/
rm $DIR/twitter4j-examples/src/main/resources/twitter4j.properties
rm $DIR/twitter4j-examples/src/test/resources/twitter4j.properties

mkdir $DIR/twitter4j-media-support
cp -r twitter4j-media-support/src $DIR/twitter4j-media-support/
cp twitter4j-media-support/pom.xml $DIR/twitter4j-media-support/
rm $DIR/twitter4j-media-support/src/test/resources/twitter4j.properties

mkdir $DIR/twitter4j-async
cp -r twitter4j-async/src $DIR/twitter4j-async/
cp twitter4j-async/pom.xml $DIR/twitter4j-async/
rm $DIR/twitter4j-async/src/test/resources/test.properties

mkdir $DIR/twitter4j-stream
cp -r twitter4j-stream/src $DIR/twitter4j-stream/
cp twitter4j-stream/pom.xml $DIR/twitter4j-stream/

mkdir $DIR/twitter4j-appengine
cp -r twitter4j-appengine/src $DIR/twitter4j-appengine/
cp twitter4j-appengine/pom.xml $DIR/twitter4j-appengine/

mkdir $DIR/twitter4j-http2-support
cp -r twitter4j-http2-support/src $DIR/twitter4j-http2-support/
cp twitter4j-http2-support/pom.xml $DIR/twitter4j-http2-support/
rm $DIR/twitter4j-http2-support/src/test/resources/test.properties
rm $DIR/twitter4j-http2-support/src/test/resources/twitter4j.properties

cp pom.xml $DIR/pom.xml
cp LICENSE.txt $DIR/
cp readme.txt $DIR/
cp -r powered-by-badge $DIR/

echo hash=$HASH
#embedding commit hash into version string
sed -i '' "s/<url>https:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusuke\/maven2\//g" $DIR/pom.xml
sed -i '' "s/<url>https:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusuke\/maven2\//g" $DIR/twitter4j-core/pom.xml
sed -i '' "s/<url>https:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusuke\/maven2\//g" $DIR/twitter4j-appengine/pom.xml
sed -i '' "s/<url>https:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusuke\/maven2\//g" $DIR/twitter4j-examples/pom.xml
sed -i '' "s/<url>https:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusuke\/maven2\//g" $DIR/twitter4j-media-support/pom.xml
sed -i '' "s/<url>https:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusuke\/maven2\//g" $DIR/twitter4j-async/pom.xml
sed -i '' "s/<url>https:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusuke\/maven2\//g" $DIR/twitter4j-stream/pom.xml
sed -i '' "s/<url>https:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusuke\/maven2\//g" $DIR/twitter4j-http2-support/pom.xml

sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" $DIR/twitter4j-core/src/main/java/twitter4j/Version.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" $DIR/twitter4j-media-support/src/main/java/twitter4j/media/Version.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" $DIR/twitter4j-async/src/main/java/twitter4j/VersionAsync.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" $DIR/twitter4j-appengine/src/main/java/twitter4j/VersionAppEngine.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" $DIR/twitter4j-stream/src/main/java/twitter4j/VersionStream.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" $DIR/twitter4j-http2-support/src/main/java/twitter4j/VersionHTTP2.java

cd $DIR
if [ -n "$DEPLOY" ];
 then
# deploy
  mvn clean deploy -Dmaven.test.skip=true
 else
  # no deploy
  cd twitter4j-core
  mvn clean package -Dmaven.test.skip=true
  cd ../twitter4j-examples
  mvn clean package -Dmaven.test.skip=true
  cd ../twitter4j-media-support
  mvn clean package -Dmaven.test.skip=true
  cd ../twitter4j-async
  mvn clean package -Dmaven.test.skip=true
  cd ../twitter4j-stream
  mvn clean package -Dmaven.test.skip=true
  cd ../twitter4j-appengine
  mvn clean package -Dmaven.test.skip=true
  cd ../twitter4j-http2-support
  mvn clean package -Dmaven.test.skip=true
  cd ..
fi

mkdir twitter4j-core/javadoc/
unzip twitter4j-core/target/twitter4j-core-$LATEST_VERSION-javadoc.jar -d twitter4j-core/javadoc/
mv twitter4j-core/target/twitter4j-core-$LATEST_VERSION-javadoc.jar twitter4j-core/
# mv twitter4j-core/target/twitter4j-core-$LATEST_VERSION-sources.jar twitter4j-core/

mkdir twitter4j-media-support/javadoc/
unzip twitter4j-media-support/target/twitter4j-media-support-$LATEST_VERSION-javadoc.jar -d twitter4j-media-support/javadoc/
mv twitter4j-media-support/target/twitter4j-media-support-$LATEST_VERSION-javadoc.jar twitter4j-media-support/
# mv twitter4j-media-support/target/twitter4j-media-support-$LATEST_VERSION-sources.jar twitter4j-media-support/

mkdir twitter4j-examples/javadoc/
unzip twitter4j-examples/target/twitter4j-examples-$LATEST_VERSION-javadoc.jar -d twitter4j-examples/javadoc/
mv twitter4j-examples/target/twitter4j-examples-$LATEST_VERSION-javadoc.jar twitter4j-examples/
# mv twitter4j-examples/target/twitter4j-examples-$LATEST_VERSION-sources.jar twitter4j-examples/

mkdir twitter4j-async/javadoc/
unzip twitter4j-async/target/twitter4j-async-$LATEST_VERSION-javadoc.jar -d twitter4j-async/javadoc/
mv twitter4j-async/target/twitter4j-async-$LATEST_VERSION-javadoc.jar twitter4j-async/
# mv twitter4j-async/target/twitter4j-async-$LATEST_VERSION-sources.jar twitter4j-async/

mkdir twitter4j-stream/javadoc/
unzip twitter4j-stream/target/twitter4j-stream-$LATEST_VERSION-javadoc.jar -d twitter4j-stream/javadoc/
mv twitter4j-stream/target/twitter4j-stream-$LATEST_VERSION-javadoc.jar twitter4j-stream/
# mv twitter4j-stream/target/twitter4j-stream-$LATEST_VERSION-sources.jar twitter4j-stream/
rm twitter4j-stream/src/test/resources/test.properties
rm twitter4j-stream/src/test/resources/sitestream-test.properties

mkdir twitter4j-appengine/javadoc/
unzip twitter4j-appengine/target/twitter4j-appengine-$LATEST_VERSION-javadoc.jar -d twitter4j-appengine/javadoc/
mv twitter4j-appengine/target/twitter4j-appengine-$LATEST_VERSION-javadoc.jar twitter4j-appengine/
# mv twitter4j-appengine/target/twitter4j-appengine-$LATEST_VERSION-sources.jar twitter4j-appengine/
mv twitter4j-appengine/target/twitter4j-appengine-$LATEST_VERSION.jar twitter4j-appengine/
rm twitter4j-appengine/src/test/resources/twitter4j.properties

mkdir twitter4j-http2-support/javadoc/
unzip twitter4j-http2-support/target/twitter4j-http2-support-$LATEST_VERSION-javadoc.jar -d twitter4j-http2-support/javadoc/
mv twitter4j-http2-support/target/twitter4j-http2-support-$LATEST_VERSION-javadoc.jar twitter4j-http2-support/
# mv twitter4j-http2-support/target/twitter4j-http2-support-$LATEST_VERSION-sources.jar twitter4j-http2-support/
mv twitter4j-http2-support/target/twitter4j-http2-support-$LATEST_VERSION.jar twitter4j-http2-support/
rm twitter4j-http2-support/src/test/resources/twitter4j.properties

mkdir lib
cp ../readme-libs.txt lib
mv twitter4j-core/target/twitter4j-core-$LATEST_VERSION.jar lib
mv twitter4j-media-support/target/twitter4j-media-support-$LATEST_VERSION.jar lib
mv twitter4j-examples/target/twitter4j-examples-$LATEST_VERSION.jar lib
mv twitter4j-async/target/twitter4j-async-$LATEST_VERSION.jar lib
mv twitter4j-stream/target/twitter4j-stream-$LATEST_VERSION.jar lib

rm -Rf twitter4j-core/target
rm -Rf twitter4j-media-support/target
rm -Rf twitter4j-async/target
rm -Rf twitter4j-examples/target
rm -Rf twitter4j-stream/target
rm -Rf twitter4j-appengine/target
rm -Rf twitter4j-http2-support/target

find . -name ".svn" -print -exec rm -rf {} ";"
find . -name ".DS_Store" -print -exec rm -r {} ";"
find . -name ._* -exec rm -r {} ";"
rm ../twitter4j-$LATEST_VERSION.zip
zip -r ../twitter4j-$LATEST_VERSION.zip .
cd ..
}

packageZip
rm -Rf twitter4j-$LATEST_VERSION
