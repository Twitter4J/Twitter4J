package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JSONSchemaArrayTest {
    @Test
    void numberArray() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Position" : {
                        "type" : "array",
                        "description" : "A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.",
                        "items" : {
                          "type" : "number"
                        },
                        "minItems" : 2,
                        "maxItems" : 2
                      }
                }""");
        assertEquals(2, extract.size());
        JSONSchema position = extract.get("#/Position");
        assertEquals("private final double[] position;",
                position.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("private final double[] position;",
                position.asFieldDeclaration(true, "twitter4j.v2", null).codeFragment());
        assertEquals("this.position = json.getDoubleArray(\"Position\");",
                position.asConstructorAssignment(false, null));
        assertEquals("this.position = json.getDoubleArray(\"Position\");",
                position.asConstructorAssignment(true, null));
        assertEquals("""
                        @Override
                        public double[] getPosition() {
                            return position;
                        }
                        """,
                position.asGetterImplementation(false, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        /**
                         * @return A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.
                         */
                        double[] getPosition();
                        """,
                position.asGetterDeclaration(false, "twitter4j.v2", null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> position.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> position.asInterface("twitter4j.v2"));
    }

    @Test
    void integerArray() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Position" : {
                        "type" : "array",
                        "description" : "A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.",
                        "items" : {
                          "type" : "integer"
                        },
                        "minItems" : 2,
                        "maxItems" : 2
                      }
                }""");
        assertEquals(2, extract.size());
        JSONSchema position = extract.get("#/Position");
        assertEquals("private final long[] position;",
                position.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("private final long[] position;",
                position.asFieldDeclaration(true, "twitter4j.v2", null).codeFragment());
        assertEquals("this.position = json.getIntArray(\"Position\");",
                position.asConstructorAssignment(false, null));
        assertEquals("this.position = json.getIntArray(\"Position\");",
                position.asConstructorAssignment(true, null));
        assertEquals("""
                        @Override
                        public long[] getPosition() {
                            return position;
                        }
                        """,
                position.asGetterImplementation(false, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        /**
                         * @return A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.
                         */
                        long[] getPosition();
                        """,
                position.asGetterDeclaration(false, "twitter4j.v2", null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> position.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> position.asInterface("twitter4j.v2"));
    }

    @Test
    void stringArray() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Position" : {
                        "type" : "array",
                        "description" : "A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.",
                        "items" : {
                          "type" : "string"
                        },
                        "minItems" : 2,
                        "maxItems" : 2
                      }
                }""");
        assertEquals(2, extract.size());
        JSONSchema position = extract.get("#/Position");
        assertEquals("private final List<String> position;",
                position.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("private final List<String> position;",
                position.asFieldDeclaration(true, "twitter4j.v2", null).codeFragment());
        assertEquals("this.position = json.getStringList(\"Position\");",
                position.asConstructorAssignment(false, null));
        assertEquals("this.position = json.getStringList(\"Position\");",
                position.asConstructorAssignment(true, null));
        assertEquals("""
                        @Override
                        public List<String> getPosition() {
                            return position;
                        }
                        """,
                position.asGetterImplementation(false, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        /**
                         * @return A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.
                         */
                        List<String> getPosition();
                        """,
                position.asGetterDeclaration(false, "twitter4j.v2", null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> position.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> position.asInterface("twitter4j.v2"));
    }

    @Test
    void booleanArray() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Position" : {
                        "type" : "array",
                        "description" : "A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.",
                        "items" : {
                          "type" : "boolean"
                        },
                        "minItems" : 2,
                        "maxItems" : 2
                      }
                }""");
        assertEquals(2, extract.size());
        JSONSchema position = extract.get("#/Position");
        assertEquals("private final boolean[] position;",
                position.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("private final boolean[] position;",
                position.asFieldDeclaration(true, "twitter4j.v2", null).codeFragment());
        assertEquals("this.position = json.getBooleanArray(\"Position\");",
                position.asConstructorAssignment(false, null));
        assertEquals("this.position = json.getBooleanArray(\"Position\");",
                position.asConstructorAssignment(true, null));
        assertEquals("""
                        @Override
                        public boolean[] getPosition() {
                            return position;
                        }
                        """,
                position.asGetterImplementation(false, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        /**
                         * @return A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.
                         */
                        boolean[] getPosition();
                        """,
                position.asGetterDeclaration(false, "twitter4j.v2", null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> position.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> position.asInterface("twitter4j.v2"));
    }

    @Test
    void objectArray() {
        var extract = JSONSchema.extract("#/", """
                {
                  "FullTextEntities": {
                    "type": "object",
                    "properties": {
                      "urls": {
                        "type": "array",
                        "items": {
                          "$ref": "#/UrlEntity"
                        },
                        "minItems": 1
                      }
                    }
                  },
                  "UrlEntity": {
                    "description": "Represent the portion of text recognized as a URL, and its start and end position within the text.",
                    "allOf": [
                      {
                        "$ref": "#/EntityIndices"
                      }
                    ]
                  },
                  "EntityIndices": {
                    "type": "object",
                    "description": "Represent a boundary range (start and end index) for a recognized entity (for example a hashtag or a mention). `start` must be smaller than `end`.",
                    "required": [
                      "start",
                      "end"
                    ],
                    "properties": {
                      "start": {
                        "type": "integer",
                        "minimum": 0,
                        "description": "Index (zero-based) at which position this entity starts."
                      },
                      "end": {
                        "type": "integer",
                        "minimum": 0,
                        "description": "Index (zero-based) at which position this entity ends."
                      }
                    }
                  }
                }""");
        JSONSchema fullTextEntities = extract.get("#/FullTextEntities");


        assertEquals("""
                package twitter4j.v2;
                                
                import java.util.List;
                                
                /**
                 * FullTextEntities
                 */
                public interface FullTextEntities {
                    /**
                     * @return urls
                     */
                    List<UrlEntity> getUrls();
                }
                """, fullTextEntities.asInterface("twitter4j.v2").content());
        assertEquals("""
                package twitter4j;
                                
                import java.util.List;
                import twitter4j.v2.UrlEntity;
                                
                /**
                 * FullTextEntities
                 */
                class FullTextEntitiesImpl implements twitter4j.v2.FullTextEntities {
                    private final List<UrlEntity> urls;
                                
                    FullTextEntitiesImpl(JSONObject json) {
                        this.urls = json.getJSONArrayAsStream("urls").map(urlsImpl::new).collect(Collectors.toList());
                    }
                                
                    @Override
                    public List<UrlEntity> getUrls() {
                        return urls;
                    }
                }
                """, fullTextEntities.asJavaImpl("twitter4j", "twitter4j.v2").content());
    }
}
