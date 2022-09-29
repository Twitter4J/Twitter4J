package twitter4j;

public class TwitterBuilder extends Configuration<Twitter, TwitterBuilder> {
    private static final long serialVersionUID = -7194823238000676626L;

    TwitterBuilder() {
        super(TwitterImpl::new);
    }
}
