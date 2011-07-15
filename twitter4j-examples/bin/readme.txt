Twitter4J example scripts

Follow the below steps to run the examples:
1. configure consumer key / secret
Go to https://dev.twitter.com/, register an app and get your app's consumer key/secret pair at https://dev.twitter.com/apps/[appid]/show.
Save those values to twitter4j.properties

2. run getAccessToken.sh/cmd
run getAccessToken.sh/cmd and follow the instruction and get your access token.
Access token will be stored in twitter4j.properties

3. run examples you want.
Now OAuth credentials are ready.
You can run any examples.

- Files in this directory
getAccessToken.sh/cmd - script to acquire OAuth access token. need to run before executing examples that requires authorization.
logback.xml - logging configuration. see http://logback.qos.ch/manual/configuration.html for the detail.
readme.txt - this file
setEnv.sh/cmd - script configures JAVA_HOME end classpath system variables
twitter4j.properties - Twitter4J configuration

- Directory structure
account/ - Account resources examples
async/ - Async API examples
block/ - Block resources examples
directmessage/ - Direct Message resources examples
favorite/ - Favorite resources examples
friendsandfollowers/ - Friends and Followers resources examples
friendship/ - Friendship resources examples
geo/ - Geo resources examples
help/ - Help resources examples
json/ - Raw JSON handling examples
legal/ - Legal resources examples
list/ -  List resources examples
listmembers/ - List Members resources examples
listsubscribers/ - List Subscribers resources examples
misc/ -  resources examples
notification/ - Miscellaneous resources examples
savedsearches/ - Saved Searches resources examples
search/ -  resources examples
spamreporting/ - Spam Reporting resources examples
stream/ - Streamed Tweets resources examples
timeline/ - Timeline resources examples
trends/ - Trends resources examples
tweets/ - Tweets resources examples
user/ - User resources examples

