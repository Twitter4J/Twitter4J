LANG=C

cd twitter4j-core
mvn clean package -Dmaven.test.skip=true
cd ../twitter4j-examples
mvn clean package -Dmaven.test.skip=true
#cd ../twitter4j-httpclient-support
#mvn clean package -Dmaven.test.skip=true
cd ../twitter4j-media-support
mvn clean package -Dmaven.test.skip=true
cd ..

DIR=twitter4j-$1
mkdir $DIR
mkdir $DIR/twitter4j-core
cp -r twitter4j-core/src $DIR/twitter4j-core/
cp twitter4j-core/build.properties $DIR/twitter4j-core/
cp twitter4j-core/build.xml $DIR/twitter4j-core/
cp twitter4j-core/pom.xml $DIR/twitter4j-core/

mkdir $DIR/twitter4j-examples
cp -r twitter4j-examples/src $DIR/twitter4j-examples/
cp twitter4j-examples/pom.xml $DIR/twitter4j-examples/
cp -r twitter4j-examples/bin $DIR/

#mkdir $DIR/twitter4j-httpclient-support
#cp -r twitter4j-httpclient-support/src $DIR/twitter4j-httpclient-support/
#cp twitter4j-httpclient-support/pom.xml $DIR/twitter4j-httpclient-support/

mkdir $DIR/twitter4j-media-support
cp -r twitter4j-media-support/src $DIR/twitter4j-media-support/
cp twitter4j-media-support/pom.xml $DIR/twitter4j-media-support/

cp pom.xml $DIR/pom.xml
cp LICENSE.txt $DIR/
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

#cp twitter4j-httpclient-support/target/twitter4j-httpclient-support-$1.jar $DIR/twitter4j-httpclient-support/
#mkdir $DIR/twitter4j-httpclient-support/javadoc/
#unzip twitter4j-httpclient-support/target/twitter4j-httpclient-support-$1-javadoc.jar -d $DIR/twitter4j-httpclient-support/javadoc/

mkdir $DIR/lib
cp twitter4j-core/target/twitter4j-core-$1.jar $DIR/lib
cp twitter4j-media-support/target/twitter4j-media-support-$1.jar $DIR/lib
cp twitter4j-examples/target/twitter4j-examples-$1.jar $DIR/lib

cd $DIR/
find . -name ".svn" -print -exec rm -rf {} ";"
find . -name ".DS_Store" -print -exec rm -r {} ";"
find . -name ._* -exec rm -r {} ";"
rm ../twitter4j-$1.zip
zip -r ../twitter4j-$1.zip .
cd ..
rm -Rf $DIR