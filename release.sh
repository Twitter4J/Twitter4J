LANG=C
cp pom.xml pom.$1.xml
sed -i '' "s/<version>.*SNAPSHOT<\/version>/<version>$1<\/version>/g" pom.$1.xml
sed -i '' "s/version=.*$/version=twitter4j-$1/g" build.properties

mvn -f pom.$1.xml install -Dmaven.test.skip=true
DIR=twitter4j-$1
mkdir $DIR
cp -r src $DIR/
cp pom.$1.xml $DIR/pom.xml
cp feedmonitor.properties $DIR/
cp build.properties $DIR/
cp build.xml $DIR/
cp LICENSE.txt $DIR/
cp -r bin $DIR/
cp -r lib $DIR/
cp target/twitter4j-$1.jar $DIR/
cp target/twitter4j-$1-sources.jar $DIR/
cp target/twitter4j-$1-javadoc.jar $DIR/
javadoc -windowtitle Twitter4J-$1 -sourcepath src/main/java twitter4j twitter4j.http -d ../twitter4j-site/site/javadoc
mkdir twitter4j-$1/doc/
cp -r ../twitter4j-site/site/javadoc/* twitter4j-$1/doc/
cd $DIR/
find . -name ".svn" -print -exec rm -rf {} ";"
find . -name ".DS_Store" -print -exec rm -r {} ";"
find . -name ._* -exec rm -r {} ";"
zip -r ../twitter4j-$1.zip .
#cd bin
#sed -i '' "s/VERSION/$1/g" setEnv.sh
#sed -i '' "s/VERSION/$1/g" setEnv.cmd
cd ..
cp twitter4j-$1.zip ../twitter4j-site/site/
#mvn deploy -Dmaven.test.skip=true
#cd target
rm twitter4j-$1.zip
rm -Rf twitter4j-$1
rm pom.$1.xml

#mvn deploy:deploy-file -DgroupId=net.homeip.yusuke -DartifactId=twitter4j -Dversion=$1 -Dpackaging=jar -Dfile=twitter4j-$1.jar -DgeneratePom=true -Durl=file:/Users/yusukey/maven2 -DrepositoryId=yusuke.homeip.net
#mvn deploy:deploy-file -DgroupId=net.homeip.yusuke -DartifactId=twitter4j -Dversion=$1 -Dpackaging=jar -Dfile=twitter4j-$1.jar -DgeneratePom=true -Durl=file:/Users/yusukey/maven2 -DrepositoryId=yusuke.homeip.net   -Dclassifier=source
#mvn deploy:deploy-file -DgroupId=net.homeip.yusuke -DartifactId=twitter4j -Dversion=$1 -Dpackaging=jar -Dfile=twitter4j-$1.jar -DgeneratePom=true -Durl=file:/Users/yusukey/maven2 -DrepositoryId=yusuke.homeip.net   -Dclassifier=javadoc

