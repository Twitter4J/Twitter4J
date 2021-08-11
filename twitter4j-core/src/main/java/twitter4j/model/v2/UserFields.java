package twitter4j.model.v2;

public enum UserFields {

  created_at,
  description,
  entities,
  id,
  location,
  name,
  pinned_tweet_id,
  profile_image_url,
  is_protected("protected"),
  public_metrics,
  url, username,
  verified,
  withheld;

  private String realName;

  UserFields() {
    this.realName = this.name();
  }

  UserFields(String realName) {
    this.realName = realName;
  }


}
