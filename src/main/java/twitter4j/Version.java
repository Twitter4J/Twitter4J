package twitter4j;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Version {
    private final static String VERSION;
    private final static String TITLE;
    static {
        String version = "undefined";
        Package p = twitter4j.Version.class.getPackage();
        if(null != p.getImplementationVersion()){
            version = p.getImplementationVersion();
        }
        VERSION = version;
        version = "undefined";
        if(null != p.getImplementationTitle()){
            version = p.getImplementationTitle();
        }
        TITLE = version;
    }
    public static String getVersion(){
        return VERSION;
    }
    public static void main(String[] args) {
        System.out.println(TITLE +" " + VERSION);
    }
}
