package twitter4j;

import org.junit.jupiter.api.Test;

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
                     "enum" : [ "official-client-forbidden", "client-not-enrolled" ]
                  }
                }""");
        assertEquals(1, extract.size());
        JSONSchema reason = extract.get("reason");
        assertEquals("""
                        @Nullable
                        private final Reason reason;""",
                reason.asFieldDeclaration(false));
        assertEquals("@NotNull\nprivate final Reason reason;",
                reason.asFieldDeclaration(true));
        assertEquals("""
                        this.reason = Reason.of(json.getString("reason"));""",
                reason.asConstructorAssignment(false, null));
        assertEquals("""
                        this.reason = Reason.of(json.getString("reason"));""",
                reason.asConstructorAssignment(true, null));
        assertEquals("""
                        @Nullable
                        @Override
                        public Reason getReason() {
                            return reason;
                        }
                        """,
                reason.asGetterImplementation(false));
        assertEquals("""
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
                        
                        /**
                         * @return reason
                         */
                        @Nullable
                        Reason getReason();
                        """,
                reason.asGetterDeclaration(false));


        assertThrows(UnsupportedOperationException.class, () -> reason.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> reason.asInterface("twitter4j.v2"));
    }

}
