package twitter4j;

public class TwitterBasicAuthTestUnit extends TwitterTestUnit {
    public TwitterBasicAuthTestUnit(String name) {
        super(name);
    }
    protected void setUp() throws Exception {
        super.setUp();
        twitterAPI1 = new Twitter(id1,pass1);
        twitterAPI1.setRetryCount(3);
        twitterAPI1.setRetryIntervalSecs(10);
        twitterAPI2 = new Twitter(id2,pass2);
         twitterAPI2.setRetryCount(3);
        twitterAPI2.setRetryIntervalSecs(10);
        unauthenticated = new Twitter();
    }

}
