package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaNoGetPrefixTest {
    @Test
    void nogetter() {
        var extract = JSONSchema.extract("#/", """
                {
                   "Tweet": {
                     "type": "object",
                     "properties": {
                         "TweetID": {
                           "type": "string",
                           "description": "Unique identifier of this Tweet. This is returned as a string in order to avoid complications with languages and tools that cannot handle large integers.",
                           "pattern": "^[0-9]{1,19}$",
                           "example": "120897978112909812"
                         },
                         "created_at": {
                           "type": "string",
                           "format": "date-time"
                         },
                         "numdouble": {
                           "type": "number"
                         },
                         "numint": {
                           "type": "number",
                           "format": "int32"
                         },
                         "bool": {
                           "type": "boolean"
                         },
                         "stringList": {
                           "type": "array",
                           "items": {
                             "type": "string"
                           }
                         },
                         "double_list": {
                           "type": "array",
                           "items": {
                             "type": "number"
                           }
                         },
                         "int_list": {
                           "type": "array",
                           "items": {
                             "type": "number",
                             "format": "int32"
                           }
                       }
                     }
                   }
                 }""");
        JSONSchema tweetId = extract.get("#/Tweet");
        assertEquals("""
                package twitter4j;
                                
                import org.jetbrains.annotations.Nullable;
                                
                import javax.annotation.processing.Generated;
                import java.time.LocalDateTime;
                import java.util.List;
                                
                /**
                 * Tweet
                 */
                @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/Tweet")
                class TweetImpl implements twitter4j.v2.Tweet {
                    @Nullable
                    private final String tweetID;
                                
                    @Nullable
                    private final LocalDateTime createdAt;
                                
                    @Nullable
                    private final Double numdouble;
                                
                    @Nullable
                    private final Double numint;
                                
                    @Nullable
                    private final Boolean bool;
                                
                    private final List<String> stringList;
                                
                    private final double[] doubleList;
                                
                    private final double[] intList;
                                
                    TweetImpl(JSONObject json) {
                        this.tweetID = json.getString("TweetID");
                        this.createdAt = json.getLocalDateTime("created_at");
                        this.numdouble = json.getDoubleValue("numdouble");
                        this.numint = json.getDoubleValue("numint");
                        this.bool = json.getBooleanValue("bool");
                        this.stringList = json.getStringList("stringList");
                        this.doubleList = json.getDoubleArray("double_list");
                        this.intList = json.getDoubleArray("int_list");
                    }
                                
                    @Nullable
                    @Override
                    public String tweetID() {
                        return tweetID;
                    }
                                
                    @Nullable
                    @Override
                    public LocalDateTime createdAt() {
                        return createdAt;
                    }
                                
                    @Nullable
                    @Override
                    public Double numdouble() {
                        return numdouble;
                    }
                                
                    @Nullable
                    @Override
                    public Double numint() {
                        return numint;
                    }
                                
                    @Nullable
                    @Override
                    public Boolean bool() {
                        return bool;
                    }
                                
                    @Override
                    public List<String> stringList() {
                        return stringList;
                    }
                                
                    @Override
                    public double[] doubleList() {
                        return doubleList;
                    }
                                
                    @Override
                    public double[] intList() {
                        return intList;
                    }
                }
                """, tweetId.asJavaImpl("twitter4j", "twitter4j.v2", true).content().replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
        assertEquals("""
                package twitter4j.v2;
                                
                import org.jetbrains.annotations.Nullable;
                                
                import javax.annotation.processing.Generated;
                import java.time.LocalDateTime;
                import java.util.List;
                                
                /**
                 * Tweet
                 */
                @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/Tweet")
                public interface Tweet {
                    /**
                     * @return Unique identifier of this Tweet. This is returned as a string in order to avoid complications with languages and tools that cannot handle large integers.
                     */
                    @Nullable
                    String tweetID();
                                
                    /**
                     * @return created_at
                     */
                    @Nullable
                    LocalDateTime createdAt();
                                
                    /**
                     * @return null
                     */
                    @Nullable
                    Double numdouble();
                                
                    /**
                     * @return null
                     */
                    @Nullable
                    Double numint();
                                
                    /**
                     * @return null
                     */
                    @Nullable
                    Boolean bool();
                                
                    /**
                     * @return stringList
                     */
                    List<String> stringList();
                                
                    /**
                     * @return double_list
                     */
                    double[] doubleList();
                                
                    /**
                     * @return int_list
                     */
                    double[] intList();
                }
                """, tweetId.asInterface("twitter4j.v2", true).content().replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
    }
}
