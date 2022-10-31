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
        JSONSchema position = extract.get("Position");
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
                position.asGetterDeclaration(false, "twitter4j.v2",null).codeFragment());

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
        JSONSchema position = extract.get("Position");
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
                position.asGetterDeclaration(false, "twitter4j.v2",null).codeFragment());

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
        JSONSchema position = extract.get("Position");
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
                position.asGetterDeclaration(false, "twitter4j.v2",null).codeFragment());

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
        JSONSchema position = extract.get("Position");
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
                position.asGetterDeclaration(false, "twitter4j.v2",null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> position.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> position.asInterface("twitter4j.v2"));
    }

    @Test
    void objectArray() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Position" : {
                        "type" : "array",
                        "description" : "A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.",
                        "items" : {
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
                           "type" : "string"
                         },
                         "HTTPStatusCode" : {
                           "type" : "integer",
                           "minimum" : 100,
                           "maximum" : 599,
                           "description" : "HTTP Status Code."
                         }
                       }
                     },
                        "minItems" : 2,
                        "maxItems" : 2
                      }
                }""");
        assertEquals(6, extract.size());
        JSONSchema position = extract.get("Position");
        assertEquals("private final List<Position> position;",
                position.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("private final List<Position> position;",
                position.asFieldDeclaration(true, "twitter4j.v2", null).codeFragment());

        assertEquals("""
                this.position = json.getJSONArrayAsStream("Position").map(PositionImpl::new).collect(Collectors.toList());""", position.asConstructorAssignment(true, null));
        assertEquals("""
                this.position = json.getJSONArrayAsStream("Position").map(PositionImpl::new).collect(Collectors.toList());""", position.asConstructorAssignment(false, null));
        assertEquals("""
                        @Override
                        public List<Position> getPosition() {
                            return position;
                        }
                        """,
                position.asGetterImplementation(false, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        /**
                         * @return A [GeoJson Position](https://tools.ietf.org/html/rfc7946#section-3.1.1) in the format `[longitude,latitude]`.
                         */
                        List<Position> getPosition();
                        """,
                position.asGetterDeclaration(false, "twitter4j.v2",null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> position.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> position.asInterface("twitter4j.v2"));
    }

}
