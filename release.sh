LANG=C
cp pom.xml pom.$1.xml
sed -i '' "s/<version>.*SNAPSHOT<\/version>/<version>$1<\/version>/g" pom.$1.xml
sed -i '' "s/version=.*$/version=twitter4j-$1/g" build.properties
mvn clean
mvn -f pom.$1.xml install deploy -Dmaven.test.skip=true
DIR=twitter4j-$1
mkdir $DIR
cp -r src $DIR/
cp pom.$1.xml $DIR/pom.xml
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
mkdir $DIR/javadoc/
#javadoc -windowtitle Twitter4J-$1 -sourcepath src/main/java twitter4j twitter4j.examples twitter4j.api -d /Users/yusukey/Dropbox/twitter4j.war/javadoc
cp -r target/apidoc/* $DIR/javadoc
cp -r target/apidoc/* /Users/yusukey/Dropbox/twitter4j.war/javadoc/

cd $DIR/
find . -name ".svn" -print -exec rm -rf {} ";"
find . -name ".DS_Store" -print -exec rm -r {} ";"
find . -name ._* -exec rm -r {} ";"
zip -r ../twitter4j-$1.zip .
#cd bin
#sed -i '' "s/VERSION/$1/g" setEnv.sh
#sed -i '' "s/VERSION/$1/g" setEnv.cmd
cd ..
cp twitter4j-$1.zip ../twitter4j-site/
#mvn deploy -Dmaven.test.skip=true
#cd target
#rm twitter4j-$1.zip
mv twitter4j-$1.zip /Users/yusukey/Dropbox/twitter4j.war/

rm -Rf twitter4j-$1
rm -Rf twitter4j
rm pom.$1.xml
