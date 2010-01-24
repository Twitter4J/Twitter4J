sed -i "" -E "s/^version=twitter4j-core-$1/version=twitterj-core-$2/g" build.properties
sed -i "" "s/<version>$1<\/version>/<version>$2<\/version>/g" pom.xml
sed -i "" "s/VERSION = \"$1\"/VERSION = \"$2\"/g" src/main/java/twitter4j/Version.java
cp /Users/yusukey/Dropbox/twitter4j.war/twitter4j-2.1.0.xml /Users/yusukey/Dropbox/twitter4j.war/twitter4j-$2.xml
sed -i "" "s/<version>2.1.0<\/version>/<version>$2<\/version>/g" /Users/yusukey/Dropbox/twitter4j.war/twitter4j-$2.xml
echo $2 > /Users/yusukey/Dropbox/twitter4j.war/latest-SNAPSHOT-version.fragment