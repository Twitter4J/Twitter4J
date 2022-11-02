package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaNoneTest {
    @Test
    void none() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error" : {
                    "required" : [ "code", "message" ],
                    "properties" : {
                      "code" : {
                        "type" : "integer",
                        "format" : "int32"
                      },
                      "message" : {
                        "type" : "string"
                      }
                    }
                  }
                      
                }
                """);
        assertEquals(3, extract.size());
        JSONSchema error = extract.get("#/Error");
        assertEquals("""
                        private final int code;
                                                
                        @NotNull
                        private final String message;""",
                error.asFieldDeclarations("twitter4j.v2", null).codeFragment());
        assertEquals("""
                        this.code = json.getInt("code");
                        this.message = json.getString("message");""",
                error.asConstructorAssignments("twitter4j.v2").codeFragment());
        assertEquals("""
                        @Override
                        public int getCode() {
                            return code;
                        }
                                                
                        @NotNull
                        @Override
                        public String getMessage() {
                            return message;
                        }
                        """,
                error.asGetterImplementations("twitter4j.v2",null).codeFragment());

        assertEquals("""
                package twitter4j;
                                
                import org.jetbrains.annotations.NotNull;
                                
                /**
                 * Error
                 */
                class ErrorImpl implements twitter4j.v2.Error {
                    private final int code;
                                
                    @NotNull
                    private final String message;
                                
                    ErrorImpl(JSONObject json) {
                        this.code = json.getInt("code");
                        this.message = json.getString("message");
                    }
                                
                    @Override
                    public int getCode() {
                        return code;
                    }
                                
                    @NotNull
                    @Override
                    public String getMessage() {
                        return message;
                    }
                }
                """, error.asJavaImpl("twitter4j", "twitter4j.v2").content());
        assertEquals("""
                package twitter4j.v2;
                                
                import org.jetbrains.annotations.NotNull;
                                
                /**
                 * Error
                 */
                public interface Error {
                    /**
                     * @return null
                     */
                    int getCode();
                                
                    /**
                     * @return message
                     */
                    @NotNull
                    String getMessage();
                }
                """, error.asInterface("twitter4j.v2").content());
    }

}
