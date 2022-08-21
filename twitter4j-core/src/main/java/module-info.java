module twitter4j {
    requires java.management;
    requires commons.logging.api;
    requires java.logging;
    requires slf4j.api;
    requires org.apache.logging.log4j;
    opens twitter4j;
    opens twitter4j.api;
    opens twitter4j.auth;
    opens twitter4j.conf;
    opens twitter4j.util;
}