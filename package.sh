LANG=C

cd twitter4j-core
mvn clean package -Dmaven.test.skip=true
cd ../twitter4j-examples
mvn clean package -Dmaven.test.skip=true
cd ..

DIR=twitter4j-$1
mkdir $DIR
mkdir $DIR/twitter4j-core
cp -r twitter4j-core/src $DIR/twitter4j-core/
cp twitter4j-core/build.properties $DIR/twitter4j-core/
cp twitter4j-core/build.xml $DIR/twitter4j-core/
cp twitter4j-core/pom.xml $DIR/twitter4j-core/
cp twitter4j-core/test.properties $DIR/twitter4j-core/

mkdir $DIR/twitter4j-examples
cp -r twitter4j-examples/src $DIR/twitter4j-examples/
cp twitter4j-examples/pom.xml $DIR/twitter4j-examples/
cp -r twitter4j-examples/bin $DIR/
cp twitter4j-examples/feedmonitor.properties $DIR/twitter4j-examples/


cp pom.xml $DIR/pom.xml
cp LICENSE.txt $DIR/
cp -r powered-by-badge $DIR/

mkdir $DIR/bin/lib
cp ~/.m2/repository/jdom/jdom/1.0/jdom-1.0.jar $DIR/bin/lib
cp ~/.m2/repository/junit/junit/3.8.2/junit-3.8.2.jar $DIR/bin/lib
cp ~/.m2/repository/ch/qos/logback/logback-classic/0.9.8/logback-classic-0.9.8.jar $DIR/bin/lib
cp ~/.m2/repository/ch/qos/logback/logback-core/0.9.8/logback-core-0.9.8.jar $DIR/bin/lib
cp ~/.m2/repository/rome/rome/0.9/rome-0.9.jar $DIR/bin/lib
cp ~/.m2/repository/org/slf4j/slf4j-api/1.4.3/slf4j-api-1.4.3.jar $DIR/bin/lib

cp twitter4j-core/target/twitter4j-core-$1.jar $DIR/
cp twitter4j-core/target/twitter4j-core-$1-sources.jar $DIR/twitter4j-core/
cp twitter4j-core/target/twitter4j-core-$1-javadoc.jar $DIR/twitter4j-core/

cp twitter4j-examples/target/twitter4j-examples-$1.jar $DIR/
cp twitter4j-examples/target/twitter4j-examples-$1-sources.jar $DIR/twitter4j-examples/
cp twitter4j-examples/target/twitter4j-examples-$1-javadoc.jar $DIR/twitter4j-examples/


cd $DIR/
find . -name ".svn" -print -exec rm -rf {} ";"
find . -name ".DS_Store" -print -exec rm -r {} ";"
find . -name ._* -exec rm -r {} ";"
rm ../twitter4j-$1.zip
zip -r ../twitter4j-$1.zip .
rm -Rf $DIR

