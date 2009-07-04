package twitter4j;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Version {
    private final static String VERSION = "2.0.9-SNAPSHOT";
    private final static String TITLE = "Twitter4J";

    public static String getVersion(){
        return VERSION;
    }
    public static void main(String[] args) {
        System.out.println(TITLE +" " + VERSION);
    }
}
