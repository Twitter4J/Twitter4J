export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.4.2/Home
$JAVA_HOME/bin/java -version
$JAVA_HOME/bin/java -Dtwitter4j.debug=true -cp lib/junit.jar:target/classes:target/test-classes/ junit.textui.TestRunner twitter4j.TwitterTestUnit
$JAVA_HOME/bin/java -Dtwitter4j.debug=true -cp lib/junit.jar:target/classes:target/test-classes/ junit.textui.TestRunner twitter4j.AsyncTwitterTest

