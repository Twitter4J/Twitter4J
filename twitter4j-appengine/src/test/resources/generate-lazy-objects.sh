./generate-lazy-object.sh AccountSettings
./generate-lazy-object.sh AccountTotals
#./generate-lazy-object.sh Category
./generate-lazy-object.sh DirectMessage
#./generate-lazy-object.sh HashtagEntity
./generate-lazy-object.sh IDs
#./generate-lazy-object.sh MediaEntity
#./generate-lazy-object.sh Paging
./generate-lazy-object.sh Place
./generate-lazy-object.sh RateLimitStatus
./generate-lazy-object.sh RelatedResults
./generate-lazy-object.sh Relationship
./generate-lazy-object.sh SavedSearch
#./generate-lazy-object.sh SimilarPlaces
./generate-lazy-object.sh Status
#./generate-lazy-object.sh TimeZone
#./generate-lazy-object.sh Trend
./generate-lazy-object.sh Trends
#./generate-lazy-object.sh Tweet
./generate-lazy-object.sh TwitterAPIConfiguration
#./generate-lazy-object.sh URLEntity
./generate-lazy-object.sh User
./generate-lazy-object.sh UserList
sed -i "" "s/target = factory.createUserList(res);/target = factory.createAUserList(res);/g" ../../main/java/twitter4j/internal/json/LazyUserList.java

#./generate-lazy-object.sh UserMentionEntity

