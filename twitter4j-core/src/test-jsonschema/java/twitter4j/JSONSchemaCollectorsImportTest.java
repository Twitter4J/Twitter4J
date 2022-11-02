package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaCollectorsImportTest {
    @Test
    void collectors() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Poll": {
                    "type": "object",
                    "description": "Represent a Poll attached to a Tweet",
                    "required": [
                      "id",
                      "options"
                    ],
                    "properties": {
                      "options": {
                        "type": "array",
                        "items": {
                          "$ref": "#/PollOption"
                        },
                        "minItems": 2,
                        "maxItems": 4
                      },
                      "voting_status": {
                        "type": "string",
                        "enum": [
                          "open",
                          "closed"
                        ]
                      },
                      "end_datetime": {
                        "type": "string",
                        "format": "date-time"
                      },
                      "duration_minutes": {
                        "type": "integer"
                      }
                    }
                  },
                  "PollOption": {
                    "type": "object",
                    "description": "Describes a choice in a Poll object.",
                    "required": [
                      "position",
                      "label",
                      "votes"
                    ],
                    "properties": {
                      "position": {
                        "type": "integer",
                        "description": "Position of this choice in the poll."
                      },
                      "label": {
                        "type": "string",
                        "description": "The text of a poll choice."
                      },
                      "votes": {
                        "type": "integer",
                        "description": "Number of users who voted for this choice."
                      }
                    }
                  }
                }""");
        assertEquals(9, extract.size());
        JSONSchema poll = extract.get("#/Poll");
        assertEquals("""
                        @Nullable
                        @Override
                        public Poll getPoll() {
                            return poll;
                        }
                        """,
                poll.asGetterImplementation(false, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        /**
                         * @return Represent a Poll attached to a Tweet
                         */
                        @Nullable
                        Poll getPoll();
                        """,
                poll.asGetterDeclaration(false, "twitter4j.v2", null).codeFragment());


        assertEquals("""
                package twitter4j;
                                
                import org.jetbrains.annotations.Nullable;
                                
                import java.time.LocalDateTime;
                import java.util.List;
                import java.util.stream.Collectors;
                import javax.annotation.processing.Generated;
                import twitter4j.v2.PollOption;
                                
                /**
                 * Represent a Poll attached to a Tweet
                 */
                @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/Poll")
                class PollImpl implements twitter4j.v2.Poll {
                    private final List<PollOption> options;
                                
                    @Nullable
                    private final VotingStatus votingStatus;
                                
                    @Nullable
                    private final LocalDateTime endDatetime;
                                
                    @Nullable
                    private final Long durationMinutes;
                                
                    PollImpl(JSONObject json) {
                        this.options = json.getJSONArrayAsStream("options").map(PollOptionImpl::new).collect(Collectors.toList());
                        this.votingStatus = VotingStatus.of(json.getString("voting_status"));
                        this.endDatetime = json.getLocalDateTime("end_datetime");
                        this.durationMinutes = json.getLongValue("duration_minutes");
                    }
                                
                    @Override
                    public List<PollOption> getOptions() {
                        return options;
                    }
                                
                    @Nullable
                    @Override
                    public VotingStatus getVotingStatus() {
                        return votingStatus;
                    }
                                
                    @Nullable
                    @Override
                    public LocalDateTime getEndDatetime() {
                        return endDatetime;
                    }
                                
                    @Nullable
                    @Override
                    public Long getDurationMinutes() {
                        return durationMinutes;
                    }
                }
                """, poll.asJavaImpl("twitter4j", "twitter4j.v2").content()
                .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
        assertEquals("""
                package twitter4j.v2;
                                
                import org.jetbrains.annotations.Nullable;
                                
                import java.time.LocalDateTime;
                import java.util.List;
                                
                /**
                 * Represent a Poll attached to a Tweet
                 */
                public interface Poll {
                    /**
                     * @return options
                     */
                    List<PollOption> getOptions();
                                
                    /**
                     * voting_status
                     */
                    enum VotingStatus {
                        /**
                         * open
                         */
                        OPEN("open"),
                        /**
                         * closed
                         */
                        CLOSED("closed");
                        /**
                         * value
                         */
                        public final String value;
                                
                        VotingStatus(String value) {
                            this.value = value;
                        }
                                
                        @Override
                        public String toString() {
                            return value;
                        }
                        /**
                         * Returns the enum constant of the specified enum class with the specified name.
                         * @param name the name of the constant to return
                         * @return the enum constant of the specified enum class with the specified name,
                         * or null if the enum constant is not found.\s
                         */
                        public static VotingStatus of(String name) {
                            for (VotingStatus value : VotingStatus.values()) {
                                if (value.value.equals(name)) {
                                    return value;
                                }
                            }
                            return null;
                        }
                    }
                                
                    /**
                     * @return voting_status
                     */
                    @Nullable
                    VotingStatus getVotingStatus();
                                
                    /**
                     * @return end_datetime
                     */
                    @Nullable
                    LocalDateTime getEndDatetime();
                                
                    /**
                     * @return null
                     */
                    @Nullable
                    Long getDurationMinutes();
                }
                """, poll.asInterface("twitter4j.v2").content()
                .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
    }
}
