module org.twitter4j {
    requires java.management;
    requires java.logging;
    requires static org.slf4j;
    requires static org.apache.logging.log4j;
    requires static org.jetbrains.annotations;
    exports twitter4j;
    exports twitter4j.api;
    exports twitter4j.auth;
    exports twitter4j.conf;
    exports twitter4j.util;
    exports twitter4j.management;
}