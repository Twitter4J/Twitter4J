module twitter4j {
    requires java.management;
    requires java.logging;
    requires org.slf4j;
    requires org.apache.logging.log4j;
    opens twitter4j;
    opens twitter4j.api;
    opens twitter4j.auth;
    opens twitter4j.conf;
    opens twitter4j.util;
}