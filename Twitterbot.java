package twitterbot;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Twitterbot {

    public static void main(String[] args) 
    {
         NewTweet();
    }
       
        public static void NewTweet()
        {
            Twitter twitter = TwitterFactory.getSingleton();
            String mytweet="Hello, this UPES DevOps Bot";
            try
            {
            Status status = twitter.updateStatus(mytweet);
                System.out.println("successful"+status.getText());
            }
            catch(TwitterException e){
                e.printStackTrace();
            }
    }
}
