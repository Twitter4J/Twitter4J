#!/bin/sh
LANG=C

#export HASH=`git log|head -n 1|sed "s/^commit //g"`
sed -i '' "s/<url>http:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusukey\/maven2\//g" pom.xml
sed -i '' "s/<url>http:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusukey\/maven2\//g" twitter4j-core/pom.xml
sed -i '' "s/<url>http:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusukey\/maven2\//g" twitter4j-appengine/pom.xml
sed -i '' "s/<url>http:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusukey\/maven2\//g" twitter4j-examples/pom.xml
sed -i '' "s/<url>http:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusukey\/maven2\//g" twitter4j-httpclient-support/pom.xml
sed -i '' "s/<url>http:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusukey\/maven2\//g" twitter4j-media-support/pom.xml
sed -i '' "s/<url>http:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusukey\/maven2\//g" twitter4j-async/pom.xml
sed -i '' "s/<url>http:\/\/oss.sonatype.org\/service\/local\/staging\/deploy\/maven2\//<url>file:\/Users\/yusukey\/maven2\//g" twitter4j-stream/pom.xml

sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" twitter4j-core/src/main/java/twitter4j/Version.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" twitter4j-httpclient-support/src/main/java/twitter4j/internal/http/alternative/Version.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" twitter4j-media-support/src/main/java/twitter4j/media/Version.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" twitter4j-async/src/main/java/twitter4j/VersionAsync.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" twitter4j-appengine/src/main/java/twitter4j/VersionAppEngine.java
sed -i '' "s/-SNAPSHOT\";/-SNAPSHOT\(build: $HASH\)\";/g" twitter4j-stream/src/main/java/twitter4j/VersionStream.java

rm -Rf .git
rm -Rf target

if [ -n "$2" ];
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
  cd ..
fi

DIR=twitter4j-$1
mkdir $DIR
mkdir $DIR/twitter4j-core
cp -r twitter4j-core/src $DIR/twitter4j-core/
cp twitter4j-core/pom.xml $DIR/twitter4j-core/

mkdir $DIR/twitter4j-examples
cp -r twitter4j-examples/src $DIR/twitter4j-examples/
cp twitter4j-examples/pom.xml $DIR/twitter4j-examples/
cp -r twitter4j-examples/bin $DIR/

mkdir $DIR/twitter4j-media-support
cp -r twitter4j-media-support/src $DIR/twitter4j-media-support/
cp twitter4j-media-support/pom.xml $DIR/twitter4j-media-support/

mkdir $DIR/twitter4j-async
cp -r twitter4j-async/src $DIR/twitter4j-async/
cp twitter4j-async/pom.xml $DIR/twitter4j-async/

mkdir $DIR/twitter4j-stream
cp -r twitter4j-stream/src $DIR/twitter4j-stream/
cp twitter4j-stream/pom.xml $DIR/twitter4j-stream/

mkdir $DIR/twitter4j-appengine
cp -r twitter4j-appengine/src $DIR/twitter4j-appengine/
cp twitter4j-appengine/pom.xml $DIR/twitter4j-appengine/

cp pom.xml $DIR/pom.xml
cp LICENSE.txt $DIR/
cp readme.txt $DIR/
cp -r powered-by-badge $DIR/

mkdir $DIR/twitter4j-core/javadoc/
unzip twitter4j-core/target/twitter4j-core-$1-javadoc.jar -d $DIR/twitter4j-core/javadoc/
cp twitter4j-core/target/twitter4j-core-$1-javadoc.jar -d $DIR/twitter4j-core/
cp twitter4j-core/target/twitter4j-core-$1-sources.jar -d $DIR/twitter4j-core/

mkdir $DIR/twitter4j-media-support/javadoc/
unzip twitter4j-media-support/target/twitter4j-media-support-$1-javadoc.jar -d $DIR/twitter4j-media-support/javadoc/
cp twitter4j-media-support/target/twitter4j-media-support-$1-javadoc.jar -d $DIR/twitter4j-media-support/
cp twitter4j-media-support/target/twitter4j-media-support-$1-sources.jar -d $DIR/twitter4j-media-support/

mkdir $DIR/twitter4j-examples/javadoc/
unzip twitter4j-examples/target/twitter4j-examples-$1-javadoc.jar -d $DIR/twitter4j-examples/javadoc/
cp twitter4j-examples/target/twitter4j-examples-$1-javadoc.jar -d $DIR/twitter4j-examples/
cp twitter4j-examples/target/twitter4j-examples-$1-sources.jar -d $DIR/twitter4j-examples/

mkdir $DIR/twitter4j-async/javadoc/
unzip twitter4j-async/target/twitter4j-async-$1-javadoc.jar -d $DIR/twitter4j-async/javadoc/
cp twitter4j-async/target/twitter4j-async-$1-javadoc.jar -d $DIR/twitter4j-async/
cp twitter4j-async/target/twitter4j-async-$1-sources.jar -d $DIR/twitter4j-async/

mkdir $DIR/twitter4j-stream/javadoc/
unzip twitter4j-stream/target/twitter4j-stream-$1-javadoc.jar -d $DIR/twitter4j-stream/javadoc/
cp twitter4j-stream/target/twitter4j-stream-$1-javadoc.jar -d $DIR/twitter4j-stream/
cp twitter4j-stream/target/twitter4j-stream-$1-sources.jar -d $DIR/twitter4j-stream/

mkdir $DIR/twitter4j-appengine/javadoc/
unzip twitter4j-appengine/target/twitter4j-appengine-$1-javadoc.jar -d $DIR/twitter4j-appengine/javadoc/
cp twitter4j-appengine/target/twitter4j-appengine-$1-javadoc.jar -d $DIR/twitter4j-appengine/
cp twitter4j-appengine/target/twitter4j-appengine-$1-sources.jar -d $DIR/twitter4j-appengine/
cp twitter4j-appengine/target/twitter4j-appengine-$1.jar -d $DIR/twitter4j-appengine/

mkdir $DIR/lib
cp readme-libs.txt $DIR/lib
cp twitter4j-core/target/twitter4j-core-$1.jar $DIR/lib
cp twitter4j-media-support/target/twitter4j-media-support-$1.jar $DIR/lib
cp twitter4j-examples/target/twitter4j-examples-$1.jar $DIR/lib
cp twitter4j-async/target/twitter4j-async-$1.jar $DIR/lib
cp twitter4j-stream/target/twitter4j-stream-$1.jar $DIR/lib

cd $DIR/
find . -name ".svn" -print -exec rm -rf {} ";"
find . -name ".DS_Store" -print -exec rm -r {} ";"
find . -name ._* -exec rm -r {} ";"
rm ../twitter4j-$1.zip
zip -r ../../twitter4j-$1.zip .

echo building android-jar remove org.json
pwd

cd ../
rm -Rf 	twitter4j-core/src/main/java/twitter4j/internal/org
find . -type f |while read file; do sed -e 's/import twitter4j.internal.org.json/import org.json/' $file > $file.tmp && mv $file.tmp $file; done
sed -i "" -e 's/<dependencies>/<dependencies><dependency><groupId>org.json<\/groupId><artifactId>json<\/artifactId><version>20090211<\/version><scope>provided<\/scope><\/dependency>/' twitter4j-core/pom.xml
sed -i "" -e 's/<dependencies>/<dependencies><dependency><groupId>org.json<\/groupId><artifactId>json<\/artifactId><version>20090211<\/version><scope>provided<\/scope><\/dependency>/' twitter4j-media-support/pom.xml
sed -i "" -e 's/<dependencies>/<dependencies><dependency><groupId>org.json<\/groupId><artifactId>json<\/artifactId><version>20090211<\/version><scope>provided<\/scope><\/dependency>/' twitter4j-async/pom.xml
sed -i "" -e 's/<dependencies>/<dependencies><dependency><groupId>org.json<\/groupId><artifactId>json<\/artifactId><version>20090211<\/version><scope>provided<\/scope><\/dependency>/' twitter4j-stream/pom.xml

sed -i "" -e 's/reader = asReader();/\/\/reader = asReader();/' twitter4j-core/src/main/java/twitter4j/internal/http/HttpResponse.java
sed -i "" -e 's/new JSONTokener(reader)/asString()/' twitter4j-core/src/main/java/twitter4j/internal/http/HttpResponse.java
sed -i "" -e 's/import twitter4j.internal.org.json.JSONTokener;/\/\/import twitter4j.internal.org.json.JSONTokener;/' twitter4j-core/src/main/java/twitter4j/internal/http/HttpResponse.java


echo building android-jar
pwd
export HOME=/tmp
cd twitter4j-core
mvn clean compile jar:jar -Dmaven.test.skip=true
mvn install:install-file -Dfile=target/twitter4j-core-$1.jar -DgroupId=org.twitter4j -DartifactId=twitter4j-core -Dversion=$1 -Dpackaging=jar -DgeneratePom=true
cd ../twitter4j-media-support
mvn clean compile jar:jar -Dmaven.test.skip=true
cd ../twitter4j-async
mvn clean compile jar:jar -Dmaven.test.skip=true
mvn install:install-file -Dfile=target/twitter4j-async-$1.jar -DgroupId=org.twitter4j -DartifactId=twitter4j-async -Dversion=$1 -Dpackaging=jar -DgeneratePom=true
cd ../twitter4j-stream
mvn clean compile jar:jar -Dmaven.test.skip=true
cd ..

echo packaging android-zip
pwd
rm $DIR/lib/*.jar

cp twitter4j-core/target/twitter4j-core-$1.jar $DIR/lib/twitter4j-core-android-$1.jar
cp twitter4j-media-support/target/twitter4j-media-support-$1.jar $DIR/lib/twitter4j-media-support-android-$1.jar
cp twitter4j-examples/target/twitter4j-examples-$1.jar $DIR/lib/
cp twitter4j-async/target/twitter4j-async-$1.jar $DIR/lib/twitter4j-async-android-$1.jar
cp twitter4j-stream/target/twitter4j-stream-$1.jar $DIR/lib/twitter4j-stream-android-$1.jar

cd $DIR
zip -r ../../twitter4j-android-$1.zip .
cd ../..
rm -Rf t4jbuild/
