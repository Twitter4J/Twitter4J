LANG=C

mvn clean install -Dmaven.test.skip=true
DIR=twitter4j-$1
mkdir $DIR
cp -r src $DIR/
cp pom.xml $DIR/pom.xml
cp feedmonitor.properties $DIR/
cp build.properties $DIR/
cp build.xml $DIR/
cp LICENSE.txt $DIR/
cp -r bin $DIR/
cp -r powered-by-badge $DIR/
cp -r lib $DIR/
cp target/twitter4j-core-$1.jar $DIR/
cp target/twitter4j-core-$1-sources.jar $DIR/
cp target/twitter4j-core-$1-javadoc.jar $DIR/
cd $DIR/
find . -name ".svn" -print -exec rm -rf {} ";"
find . -name ".DS_Store" -print -exec rm -r {} ";"
find . -name ._* -exec rm -r {} ";"
zip -r /Users/yusukey/deploy/twitter4j.war/twitter4j-$1.zip .
cd ..
rm -Rf twitter4j-$1
