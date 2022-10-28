package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JSONSchemaTest {
    @Test
    void hoge() {
        assertEquals("    hoge\n    foo", JSONSchema.indent("""
                hoge
                foo
                """, 4));
        assertEquals("    hoge\n    foo", JSONSchema.indent("""
                hoge
                foo""", 4));
    }


    @Test
    void integer() {
        var extract = JSONSchema.extract("#/", """
                {
                        "MediaHeight" : {
                         "type" : "integer",
                         "minimum" : 0,
                         "description" : "The height of the media in pixels"
                       }
                 
                }""");
        assertEquals(1, extract.size());
        JSONSchema mediaHeight = extract.get("MediaHeight");
        assertEquals("""
                        @Nullable
                        @Range(from = 0, to = Long.MAX_VALUE)
                        private final Long mediaHeight;""",
                mediaHeight.asFieldDeclaration(false));
        assertEquals("""
                        this.mediaHeight = json.has("MediaHeight") ? json.getLong("MediaHeight") : null;""",
                mediaHeight.asConstructorAssignment(false));
        assertEquals("this.mediaHeight = json.getLong(\"MediaHeight\");",
                mediaHeight.asConstructorAssignment(true));
        assertEquals("""
                        @Nullable
                        @Range(from = 0, to = Long.MAX_VALUE)
                        @Override
                        public Long getMediaHeight() {
                            return mediaHeight;
                        }
                        """,
                mediaHeight.asGetterImplementation(false));

        assertThrows(UnsupportedOperationException.class, () -> mediaHeight.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> mediaHeight.asInterface("twitter4j.v2"));
    }

    @Test
    void number() {
        var extract = JSONSchema.extract("#/", """
                {
                    "items" : {
                           "type" : "number",
                           "format" : "double",
                           "minimum" : -180,
                           "maximum" : 180
                    }
                }""");
        assertEquals(1, extract.size());
        JSONSchema items = extract.get("items");
        assertEquals("""
                        @Nullable
                        @Range(from = -180, to = 180)
                        private final Double items;""",
                items.asFieldDeclaration(false));
        assertEquals("""
                        this.items = json.has("items") ? json.getDouble("items") : null;""",
                items.asConstructorAssignment(false));
        assertEquals("this.items = json.getDouble(\"items\");",
                items.asConstructorAssignment(true));
        assertEquals("""
                        @Nullable
                        @Range(from = -180, to = 180)
                        @Override
                        public Double getItems() {
                            return items;
                        }
                        """,
                items.asGetterImplementation(false));

        assertThrows(UnsupportedOperationException.class, () -> items.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> items.asInterface("twitter4j.v2"));
    }
    @Test
    void integerMinMax() {

        var extract = JSONSchema.extract("#/", """
                {       "HTTPStatusCode" : {
                          "type" : "integer",
                          "minimum" : 100,
                          "maximum" : 599,
                          "description" : "HTTP Status Code."
                        }
                  
                }""");
        assertEquals(1, extract.size());
        JSONSchema hTTPStatusCode = extract.get("HTTPStatusCode");
        assertEquals("@Nullable\n@Range(from = 100, to = 599)\nprivate final Integer hTTPStatusCode;",
                hTTPStatusCode.asFieldDeclaration(false));
        assertEquals("""
                        this.hTTPStatusCode = json.has("HTTPStatusCode") ? json.getInt("HTTPStatusCode") : null;""",
                hTTPStatusCode.asConstructorAssignment(false));
        assertEquals("this.hTTPStatusCode = json.getInt(\"HTTPStatusCode\");",
                hTTPStatusCode.asConstructorAssignment(true));
        assertEquals("""
                        @Nullable
                        @Range(from = 100, to = 599)
                        @Override
                        public Integer getHTTPStatusCode() {
                            return hTTPStatusCode;
                        }
                        """,
                hTTPStatusCode.asGetterImplementation(false));

        assertThrows(UnsupportedOperationException.class, () -> hTTPStatusCode.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> hTTPStatusCode.asInterface("twitter4j.v2"));
    }

    @Test
    void int32() {

        var extract = JSONSchema.extract("#/", """
                {               "code" : {
                                     "type" : "integer",
                                             "format" : "int32"
                                 },
                                                           "HTTPStatusCode" : {
                            "type" : "integer",
                            "minimum" : 100,
                            "maximum" : 599,
                            "description" : "HTTP Status Code."
                          }

                  
                }""");
        assertEquals(2, extract.size());
        JSONSchema code = extract.get("code");
        assertEquals("@Nullable\nprivate final Integer code;",
                code.asFieldDeclaration(false));
        assertEquals("private final int code;",
                code.asFieldDeclaration(true));
        assertEquals("""
                        this.code = json.has("code") ? json.getInt("code") : null;""",
                code.asConstructorAssignment(false));
        assertEquals("this.code = json.getInt(\"code\");",
                code.asConstructorAssignment(true));
        assertEquals("""
                        @Nullable
                        @Override
                        public Integer getCode() {
                            return code;
                        }
                        """,
                code.asGetterImplementation(false));
        assertEquals("""
                        @Override
                        public int getCode() {
                            return code;
                        }
                        """,
                code.asGetterImplementation(true));


        JSONSchema hTTPStatusCode = extract.get("HTTPStatusCode");
        assertEquals("""
                /**
                 * @return HTTP Status Code.
                 */
                @Nullable
                @Range(from = 100, to = 599)
                Integer getHTTPStatusCode();
                """, hTTPStatusCode.asGetterDeclaration(false));
        assertEquals("""
                /**
                 * @return HTTP Status Code.
                 */
                @Range(from = 100, to = 599)
                int getHTTPStatusCode();
                """, hTTPStatusCode.asGetterDeclaration(true));

    }

    @Test
    void bool() {

        var extract = JSONSchema.extract("#/", """
                  {
                    "possibly_sensitive" : {
                      "type" : "boolean",
                      "description" : "Indicates if this Tweet contains URLs marked as sensitive, for example content suitable for mature audiences."
                    }
                }""");
        assertEquals(1, extract.size());
        JSONSchema possiblySensitive = extract.get("possibly_sensitive");
        assertEquals("@Nullable\nprivate final Boolean possiblySensitive;",
                possiblySensitive.asFieldDeclaration(false));
        assertEquals("this.possiblySensitive = json.getBoolean(\"possibly_sensitive\");",
                possiblySensitive.asConstructorAssignment(false));
        assertEquals("""
                        @Nullable
                        @Override
                        public Boolean getPossiblySensitive() {
                            return possiblySensitive;
                        }
                        """,
                possiblySensitive.asGetterImplementation(false));

        assertThrows(UnsupportedOperationException.class, () -> possiblySensitive.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> possiblySensitive.asInterface("twitter4j.v2"));
    }


    @Test
    void object() {
        var extract = JSONSchema.extract("#/", """
                {
                      "ProblemFields" : {
                        "type" : "object",
                        "required" : [ "type", "title"],
                        "properties" : {
                          "type" : {
                            "type" : "string",
                            "format" : "uri"
                          },
                          "title" : {
                            "type" : "string"
                          },
                          "detail" : {
                            "type" : "string",
                            "format" : "date-time"
                          },
                          "HTTPStatusCode" : {
                            "type" : "integer",
                            "minimum" : 100,
                            "maximum" : 599,
                            "description" : "HTTP Status Code."
                          }
                        }
                      }
                }""");
        assertEquals(1, extract.size());
        JSONSchema problemFields = extract.get("ProblemFields");
        assertEquals("""
                this.problemFields = new ProblemFields(json.getJSONObject("ProblemFields"));""", problemFields.asConstructorAssignment(true));
        assertEquals("""
                this.problemFields = json.has("ProblemFields") ? new ProblemFields(json.getJSONObject("ProblemFields")) : null;
                """, problemFields.asConstructorAssignment(false));
        assertEquals("""
                        @NotNull
                        private final String type;

                        @NotNull
                        private final String title;

                        @Nullable
                        private final LocalDateTime detail;
                                                
                        @Nullable
                        @Range(from = 100, to = 599)
                        private final Integer hTTPStatusCode;
                        """,
                problemFields.asFieldDeclarations());
        assertEquals("""
                        this.type = json.getString("type");
                        this.title = json.getString("title");
                        this.detail = json.has("detail") ? json.getLocalDateTime("detail") : null;
                        this.hTTPStatusCode = json.has("HTTPStatusCode") ? json.getInt("HTTPStatusCode") : null;""",
                problemFields.asConstructorAssignments());
        assertEquals("""
                        @NotNull
                        @Override
                        public String getType() {
                            return type;
                        }

                        @NotNull
                        @Override
                        public String getTitle() {
                            return title;
                        }

                        @Nullable
                        @Override
                        public LocalDateTime getDetail() {
                            return detail;
                        }
                        
                        @Nullable
                        @Range(from = 100, to = 599)
                        @Override
                        public Integer getHTTPStatusCode() {
                            return hTTPStatusCode;
                        }
                        """,
                problemFields.asGetterImplementations());

        String javaImpl = problemFields.asJavaImpl("twitter4j", "twitter4j.v2");
        assertEquals("""
                        package twitter4j;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                        import org.jetbrains.annotations.Range;
                        
                        import java.time.LocalDateTime;
                                                
                        /**
                         * ProblemFields
                         */
                        class ProblemFieldsImpl implements twitter4j.v2.ProblemFields {
                            @NotNull
                            private final String type;
                                                
                            @NotNull
                            private final String title;
                                                
                            @Nullable
                            private final LocalDateTime detail;
                                                
                            @Nullable
                            @Range(from = 100, to = 599)
                            private final Integer hTTPStatusCode;
                                                
                            ProblemFieldsImpl(JSONObject json) {
                                this.type = json.getString("type");
                                this.title = json.getString("title");
                                this.detail = json.has("detail") ? json.getLocalDateTime("detail") : null;
                                this.hTTPStatusCode = json.has("HTTPStatusCode") ? json.getInt("HTTPStatusCode") : null;
                            }
                                                
                            @NotNull
                            @Override
                            public String getType() {
                                return type;
                            }
                                                
                            @NotNull
                            @Override
                            public String getTitle() {
                                return title;
                            }
                                                
                            @Nullable
                            @Override
                            public LocalDateTime getDetail() {
                                return detail;
                            }
                                                
                            @Nullable
                            @Range(from = 100, to = 599)
                            @Override
                            public Integer getHTTPStatusCode() {
                                return hTTPStatusCode;
                            }
                        }
                        """,
                javaImpl);

        String interfaceDeclaration = problemFields.asInterface("twitter4j.v2");
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                        import org.jetbrains.annotations.Range;
                        
                        import java.time.LocalDateTime;
                        
                        /**
                         * ProblemFields
                         */
                        public interface ProblemFields {
                            /**
                             * @return type
                             */
                            @NotNull
                            String getType();
                            
                            /**
                             * @return title
                             */
                            @NotNull
                            String getTitle();
                            
                            /**
                             * @return detail
                             */
                            @Nullable
                            LocalDateTime getDetail();
                                                
                            /**
                             * @return HTTP Status Code.
                             */
                            @Nullable
                            @Range(from = 100, to = 599)
                            Integer getHTTPStatusCode();
                        }
                        """,
                interfaceDeclaration);


    }
}