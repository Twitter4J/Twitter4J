package twitter4j;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unused")
class JSONSchemaEnumTest {
    /**
     * reason
     */
    public enum Reason {
        OFFICIAL_CLIENT_FORBIDDEN("official-client-forbidden"),
        CLIENT_NOT_ENROLLED("client-not-enrolled");
        public final String value;

        Reason(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Reason of(String str) {
            for (Reason value : Reason.values()) {
                if (value.value.equals(str)) {
                    return value;
                }
            }
            return null;
        }
    }

    @Test
    void enumType() {
        var extract = JSONSchema.extract("#/", """
                {
                  "reason" : {
                     "type" : "string",
                     "pattern": "^[A-Za-z]{1,12}$",
                     "enum" : [ "official-client-forbidden", "client-not-enrolled" ]
                  }
                }""");
        assertEquals(1, extract.size());
        JSONSchema reason = extract.get("#/reason");
        assertEquals("""
                        @Nullable
                        private final Reason reason;""",
                reason.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("@NotNull\nprivate final Reason reason;",
                reason.asFieldDeclaration(true, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        this.reason = Reason.of(json.getString("reason"));""",
                reason.asConstructorAssignment("twitter4j", false, null).codeFragment());
        assertEquals("""
                        this.reason = Reason.of(json.getString("reason"));""",
                reason.asConstructorAssignment("twitter4j", true, null).codeFragment());
        assertEquals("""
                        @Nullable
                        @Override
                        public Reason getReason() {
                            return reason;
                        }
                        """,
                reason.asGetterImplementation(false, "twitter4j.v2", null, false).codeFragment());
        assertEquals("""
                        /**
                         * reason
                         */
                        enum Reason {
                            /**
                             * official-client-forbidden
                             */
                            OFFICIAL_CLIENT_FORBIDDEN("official-client-forbidden"),
                            /**
                             * client-not-enrolled
                             */
                            CLIENT_NOT_ENROLLED("client-not-enrolled");
                            /**
                             * value
                             */
                            public final String value;
                                                
                            Reason(String value) {
                                this.value = value;
                            }
                                                
                            @Override
                            public String toString() {
                                return value;
                            }
                            /**
                             * Returns the enum constant of the specified enum class with the specified name.
                             *
                             * @param name the name of the constant to return
                             * @return the enum constant of the specified enum class with the specified name,
                             * or null if the enum constant is not found.
                             */
                            public static Reason of(String name) {
                                for (Reason value : Reason.values()) {
                                    if (value.value.equals(name)) {
                                        return value;
                                    }
                                }
                                return null;
                            }
                        }
                                                
                        /**
                         * @return reason
                         */
                        @Nullable
                        Reason getReason();
                        """,
                reason.asGetterDeclaration(false, "twitter4j.v2", null, false).codeFragment());


        assertThrows(UnsupportedOperationException.class, () -> reason.asJavaImpl("twitter4j", "twitter4j.v2", false));
        assertThrows(UnsupportedOperationException.class, () -> reason.asInterface("twitter4j.v2", false));
    }

    @Test
    void enumTypeImport() {
        var extract = JSONSchema.extract("#/", """
                {
                  "reason" : {
                     "type" : "string",
                     "enum" : [ "official-client-forbidden", "client-not-enrolled" ],
                     "example" : "client-not-enrolled"
                  }
                  ,
                  "MyObject": {
                    "type": "object",
                    "properties":{
                      "MyReason": {
                        "$ref": "#/reason"}
                      }
                  }
                }""");
        assertEquals(2, extract.size());
        JSONSchema reason = extract.get("#/MyObject");

        assertEquals("""
                        package twitter4j;
                                                
                        import org.jetbrains.annotations.Nullable;
                                                
                        import javax.annotation.processing.Generated;
                                                
                        /**
                         * MyObject
                         */
                        @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/MyObject")
                        class MyObjectImpl implements twitter4j.v2.MyObject {
                            @Nullable
                            private final Reason myReason;
                                                
                            MyObjectImpl(JSONObject json) {
                                this.myReason = Reason.of(json.getString("MyReason"));
                            }
                                                
                            @Nullable
                            @Override
                            public Reason getMyReason() {
                                return myReason;
                            }
                        }
                        """,
                reason.asJavaImpl("twitter4j", "twitter4j.v2", false).content()
                        .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));

        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.Nullable;
                                                
                        import javax.annotation.processing.Generated;
                                                
                        /**
                         * MyObject
                         */
                        @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/MyObject")
                        public interface MyObject {
                            /**
                             * MyReason
                             */
                            enum Reason {
                                /**
                                 * official-client-forbidden
                                 */
                                OFFICIAL_CLIENT_FORBIDDEN("official-client-forbidden"),
                                /**
                                 * client-not-enrolled
                                 */
                                CLIENT_NOT_ENROLLED("client-not-enrolled");
                                /**
                                 * value
                                 */
                                public final String value;
                                                
                                Reason(String value) {
                                    this.value = value;
                                }
                                                
                                @Override
                                public String toString() {
                                    return value;
                                }
                                /**
                                 * Returns the enum constant of the specified enum class with the specified name.
                                 *
                                 * @param name the name of the constant to return
                                 * @return the enum constant of the specified enum class with the specified name,
                                 * or null if the enum constant is not found.
                                 */
                                public static Reason of(String name) {
                                    for (Reason value : Reason.values()) {
                                        if (value.value.equals(name)) {
                                            return value;
                                        }
                                    }
                                    return null;
                                }
                            }
                                                
                            /**
                             * @return MyReason
                             */
                            @Nullable
                            Reason getMyReason();
                        }
                        """,
                reason.asInterface("twitter4j.v2", false).content()
                        .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
    }

    @Test
    void enumSingle() {
        // single enum element will be just StringSchema
        var extract = JSONSchema.extract("#/", """
                {
                  "UsageCapExceededProblem": {
                    "description": "A problem that indicates that a usage cap has been exceeded.",
                    "type": "object",
                    "properties": {
                      "type": {
                        "type": "string",
                        "enum": [
                          "https://api.twitter.com/labs/2/problems/usage-capped"
                        ]
                      },
                      "period": {
                        "type": "string",
                        "enum": [
                          "Daily",
                          "Monthly"
                        ]
                      }
                    }
                  }
                }""");
        assertEquals(3, extract.size());
        JSONSchema usageCapExceededProblem = extract.get("#/UsageCapExceededProblem");


        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.Nullable;
                                                
                        import javax.annotation.processing.Generated;
                                                
                        /**
                         * A problem that indicates that a usage cap has been exceeded.
                         */
                        @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/UsageCapExceededProblem")
                        public interface UsageCapExceededProblem {
                            /**
                             * @return type
                             */
                            @Nullable
                            String getType();
                                                
                            /**
                             * period
                             */
                            enum Period {
                                /**
                                 * Daily
                                 */
                                DAILY("Daily"),
                                /**
                                 * Monthly
                                 */
                                MONTHLY("Monthly");
                                /**
                                 * value
                                 */
                                public final String value;
                                                
                                Period(String value) {
                                    this.value = value;
                                }
                                                
                                @Override
                                public String toString() {
                                    return value;
                                }
                                /**
                                 * Returns the enum constant of the specified enum class with the specified name.
                                 *
                                 * @param name the name of the constant to return
                                 * @return the enum constant of the specified enum class with the specified name,
                                 * or null if the enum constant is not found.
                                 */
                                public static Period of(String name) {
                                    for (Period value : Period.values()) {
                                        if (value.value.equals(name)) {
                                            return value;
                                        }
                                    }
                                    return null;
                                }
                            }
                                                
                            /**
                             * @return period
                             */
                            @Nullable
                            Period getPeriod();
                        }
                        """,
                usageCapExceededProblem.asInterface("twitter4j.v2", false)
                        .content().replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));

        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.Nullable;
                                                
                        import javax.annotation.processing.Generated;
                                                
                        /**
                         * A problem that indicates that a usage cap has been exceeded.
                         */
                        @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/UsageCapExceededProblem")
                        public interface UsageCapExceededProblem {
                            /**
                             * @return type
                             */
                            @Nullable
                            String type();
                                                
                            /**
                             * period
                             */
                            enum Period {
                                /**
                                 * Daily
                                 */
                                DAILY("Daily"),
                                /**
                                 * Monthly
                                 */
                                MONTHLY("Monthly");
                                /**
                                 * value
                                 */
                                public final String value;
                                                
                                Period(String value) {
                                    this.value = value;
                                }
                                                
                                @Override
                                public String toString() {
                                    return value;
                                }
                                /**
                                 * Returns the enum constant of the specified enum class with the specified name.
                                 *
                                 * @param name the name of the constant to return
                                 * @return the enum constant of the specified enum class with the specified name,
                                 * or null if the enum constant is not found.
                                 */
                                public static Period of(String name) {
                                    for (Period value : Period.values()) {
                                        if (value.value.equals(name)) {
                                            return value;
                                        }
                                    }
                                    return null;
                                }
                            }
                                                
                            /**
                             * @return period
                             */
                            @Nullable
                            Period period();
                        }
                        """,
                usageCapExceededProblem.asInterface("twitter4j.v2", true)
                        .content().replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));

    }


    @Test
    void format() throws IOException, URISyntaxException {
        Path file = Path.of(Objects.requireNonNull(SchemaLoader.class.getResource("/openapi.json")).toURI());

        @Language("JSON") String apiJsonLatest = new String(Files.readAllBytes(file));

        Map<String, JSONSchema> extract = JSONSchema.extract("#/components/schemas/", apiJsonLatest);
        JSONSchema schema = extract.get("#/components/schemas/UsageCapExceededProblem");
        assertEquals("""
                package twitter4j.v2;
                                
                import org.jetbrains.annotations.NotNull;
                import org.jetbrains.annotations.Nullable;
                                
                import javax.annotation.processing.Generated;
                                
                /**
                 * A problem that indicates that a usage cap has been exceeded.
                 */
                @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/components/schemas/UsageCapExceededProblem")
                public interface UsageCapExceededProblem {
                    /**
                     * @return An HTTP Problem Details object, as defined in IETF RFC 7807 (<a href="https://tools.ietf.org/html/rfc7807">https://tools.ietf.org/html/rfc7807</a>).
                     */
                    @NotNull
                    Problem problem();
                                
                    /**
                     * period
                     */
                    enum Period {
                        /**
                         * Daily
                         */
                        DAILY("Daily"),
                        /**
                         * Monthly
                         */
                        MONTHLY("Monthly");
                        /**
                         * value
                         */
                        public final String value;
                                
                        Period(String value) {
                            this.value = value;
                        }
                                
                        @Override
                        public String toString() {
                            return value;
                        }
                        /**
                         * Returns the enum constant of the specified enum class with the specified name.
                         *
                         * @param name the name of the constant to return
                         * @return the enum constant of the specified enum class with the specified name,
                         * or null if the enum constant is not found.
                         */
                        public static Period of(String name) {
                            for (Period value : Period.values()) {
                                if (value.value.equals(name)) {
                                    return value;
                                }
                            }
                            return null;
                        }
                    }
                                
                    /**
                     * @return period
                     */
                    @Nullable
                    Period period();
                                
                    /**
                     * scope
                     */
                    enum Scope {
                        /**
                         * Account
                         */
                        ACCOUNT("Account"),
                        /**
                         * Product
                         */
                        PRODUCT("Product");
                        /**
                         * value
                         */
                        public final String value;
                                
                        Scope(String value) {
                            this.value = value;
                        }
                                
                        @Override
                        public String toString() {
                            return value;
                        }
                        /**
                         * Returns the enum constant of the specified enum class with the specified name.
                         *
                         * @param name the name of the constant to return
                         * @return the enum constant of the specified enum class with the specified name,
                         * or null if the enum constant is not found.
                         */
                        public static Scope of(String name) {
                            for (Scope value : Scope.values()) {
                                if (value.value.equals(name)) {
                                    return value;
                                }
                            }
                            return null;
                        }
                    }
                                
                    /**
                     * @return scope
                     */
                    @Nullable
                    Scope scope();
                }
                """, schema.asInterface("twitter4j.v2", true).content()
                .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));

    }

    @Test
    void format2() {

        Map<String, JSONSchema> extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "ClientForbiddenProblem": {
                        "description": "A problem that indicates your client is forbidden from making this request.",
                        "allOf": [
                          {
                            "$ref": "#/components/schemas/Problem"
                          },
                          {
                            "type": "object",
                            "properties": {
                              "reason": {
                                "type": "string",
                                "enum": [
                                  "official-client-forbidden",
                                  "client-not-enrolled"
                                ]
                              },
                              "registration_url": {
                                "type": "string",
                                "format": "uri"
                              }
                            }
                          }
                        ]
                      },
                      "Problem" : {
                        "type" : "object",
                        "description" : "An HTTP Problem Details object, as defined in IETF RFC 7807 (https://tools.ietf.org/html/rfc7807).",
                        "required" : [
                          "type",
                          "title"
                        ],
                        "properties" : {
                          "detail" : {
                            "type" : "string"
                          },
                          "status" : {
                            "type" : "integer"
                          },
                          "title" : {
                            "type" : "string"
                          },
                          "type" : {
                            "type" : "string"
                          }
                        },
                        "discriminator" : {
                          "propertyName" : "type",
                          "mapping" : {
                            "about:blank" : "#/components/schemas/GenericProblem",
                            "https://api.twitter.com/2/problems/client-disconnected" : "#/components/schemas/ClientDisconnectedProblem",
                            "https://api.twitter.com/2/problems/client-forbidden" : "#/components/schemas/ClientForbiddenProblem",
                            "https://api.twitter.com/2/problems/conflict" : "#/components/schemas/ConflictProblem",
                            "https://api.twitter.com/2/problems/disallowed-resource" : "#/components/schemas/DisallowedResourceProblem",
                            "https://api.twitter.com/2/problems/duplicate-rules" : "#/components/schemas/DuplicateRuleProblem",
                            "https://api.twitter.com/2/problems/invalid-request" : "#/components/schemas/InvalidRequestProblem",
                            "https://api.twitter.com/2/problems/invalid-rules" : "#/components/schemas/InvalidRuleProblem",
                            "https://api.twitter.com/2/problems/noncompliant-rules" : "#/components/schemas/NonCompliantRulesProblem",
                            "https://api.twitter.com/2/problems/not-authorized-for-field" : "#/components/schemas/FieldUnauthorizedProblem",
                            "https://api.twitter.com/2/problems/not-authorized-for-resource" : "#/components/schemas/ResourceUnauthorizedProblem",
                            "https://api.twitter.com/2/problems/operational-disconnect" : "#/components/schemas/OperationalDisconnectProblem",
                            "https://api.twitter.com/2/problems/resource-not-found" : "#/components/schemas/ResourceNotFoundProblem",
                            "https://api.twitter.com/2/problems/resource-unavailable" : "#/components/schemas/ResourceUnavailableProblem",
                            "https://api.twitter.com/2/problems/rule-cap" : "#/components/schemas/RulesCapProblem",
                            "https://api.twitter.com/2/problems/streaming-connection" : "#/components/schemas/ConnectionExceptionProblem",
                            "https://api.twitter.com/2/problems/unsupported-authentication" : "#/components/schemas/UnsupportedAuthenticationProblem",
                            "https://api.twitter.com/2/problems/usage-capped" : "#/components/schemas/UsageCapExceededProblem"
                          }
                        }
                      },
                      "GenericProblem" : {
                        "description" : "A generic problem with no additional information beyond that provided by the HTTP status code.",
                        "allOf" : [
                          {
                            "$ref" : "#/components/schemas/Problem"
                          }
                        ]
                      }
                    }
                  }
                }
                """
        );
        JSONSchema schema = extract.get("#/components/schemas/ClientForbiddenProblem");

        assertEquals("""
                package twitter4j.v2;
                                
                import org.jetbrains.annotations.NotNull;
                import org.jetbrains.annotations.Nullable;
                                
                import javax.annotation.processing.Generated;
                                
                /**
                 * A problem that indicates your client is forbidden from making this request.
                 */
                @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/components/schemas/ClientForbiddenProblem")
                public interface ClientForbiddenProblem {
                    /**
                     * @return An HTTP Problem Details object, as defined in IETF RFC 7807 (<a href="https://tools.ietf.org/html/rfc7807">https://tools.ietf.org/html/rfc7807</a>).
                     */
                    @NotNull
                    Problem problem();
                                
                    /**
                     * reason
                     */
                    enum Reason {
                        /**
                         * official-client-forbidden
                         */
                        OFFICIAL_CLIENT_FORBIDDEN("official-client-forbidden"),
                        /**
                         * client-not-enrolled
                         */
                        CLIENT_NOT_ENROLLED("client-not-enrolled");
                        /**
                         * value
                         */
                        public final String value;
                                
                        Reason(String value) {
                            this.value = value;
                        }
                                
                        @Override
                        public String toString() {
                            return value;
                        }
                        /**
                         * Returns the enum constant of the specified enum class with the specified name.
                         *
                         * @param name the name of the constant to return
                         * @return the enum constant of the specified enum class with the specified name,
                         * or null if the enum constant is not found.
                         */
                        public static Reason of(String name) {
                            for (Reason value : Reason.values()) {
                                if (value.value.equals(name)) {
                                    return value;
                                }
                            }
                            return null;
                        }
                    }
                                
                    /**
                     * @return reason
                     */
                    @Nullable
                    Reason reason();
                                
                    /**
                     * @return registration_url
                     */
                    @Nullable
                    String registrationUrl();
                }
                """, schema.asInterface("twitter4j.v2", true).content()
                .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));

        assertEquals("""
                package twitter4j;
                                
                import org.jetbrains.annotations.NotNull;
                import org.jetbrains.annotations.Nullable;
                import twitter4j.v2.Problem;
                                
                import javax.annotation.processing.Generated;
                                
                /**
                 * A problem that indicates your client is forbidden from making this request.
                 */
                @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/components/schemas/ClientForbiddenProblem")
                class ClientForbiddenProblemImpl implements twitter4j.v2.ClientForbiddenProblem {
                    @NotNull
                    private final Problem problem;
                                
                    @Nullable
                    private final Reason reason;
                                
                    @Nullable
                    private final String registrationUrl;
                                
                    @SuppressWarnings("ConstantConditions")
                    ClientForbiddenProblemImpl(JSONObject json) {
                        this.problem = json.has("Problem") ? new ProblemImpl(json.getJSONObject("Problem")) : null;
                        this.reason = Reason.of(json.getString("reason"));
                        this.registrationUrl = json.getString("registration_url");
                    }
                                
                    @NotNull
                    @Override
                    public Problem problem() {
                        return problem;
                    }
                                
                    @Nullable
                    @Override
                    public Reason reason() {
                        return reason;
                    }
                                
                    @Nullable
                    @Override
                    public String registrationUrl() {
                        return registrationUrl;
                    }
                }
                """, schema.asJavaImpl("twitter4j","twitter4j.v2", true).content()
                .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));

    }
}
