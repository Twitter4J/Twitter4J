package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JSONSchemaBooleanTest {
    @Test
    void bool() {

        var extract = JSONSchema.extract("#/", """
                  {
                    "possibly_sensitive" : {
                      "type" : "boolean",
                      "description" : "Indicates if this Tweet contains URLs marked as sensitive, for example content suitable for mature audiences.",
                      "example": true
                    }
                }""");
        assertEquals(1, extract.size());
        JSONSchema possiblySensitive = extract.get("#/possibly_sensitive");
        assertEquals("@Nullable\nprivate final Boolean possiblySensitive;",
                possiblySensitive.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("this.possiblySensitive = json.getBooleanValue(\"possibly_sensitive\");",
                possiblySensitive.asConstructorAssignment("twitter4j", false, null).codeFragment());
        assertEquals("this.possiblySensitive = json.getBoolean(\"possibly_sensitive\");",
                possiblySensitive.asConstructorAssignment("twitter4j", true, null).codeFragment());
        assertEquals("""
                        @Nullable
                        @Override
                        public Boolean getPossiblySensitive() {
                            return possiblySensitive;
                        }
                        """,
                possiblySensitive.asGetterImplementation(false, "twitter4j.v2", null, false).codeFragment());

        // nonnull boolean getter starts with "is"
        assertEquals("""
                        @Override
                        public boolean isPossiblySensitive() {
                            return possiblySensitive;
                        }
                        """,
                possiblySensitive.asGetterImplementation(true, "twitter4j.v2", null, false).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> possiblySensitive.asJavaImpl("twitter4j", "twitter4j.v2", false));
        assertThrows(UnsupportedOperationException.class, () -> possiblySensitive.asInterface("twitter4j.v2", false));
    }
    @Test
    void boolDefault() {

        var extract = JSONSchema.extract("#/", """
                  {
                    "possibly_sensitive" : {
                      "type" : "boolean",
                      "default" : false,
                      "description" : "Indicates if this Tweet contains URLs marked as sensitive, for example content suitable for mature audiences.",
                      "example" : "false"
                    }
                }""");
        assertEquals(1, extract.size());
        JSONSchema possiblySensitive = extract.get("#/possibly_sensitive");

        // property is nonnull because default value is specified
        assertEquals("private final boolean possiblySensitive;",
                possiblySensitive.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("private final boolean possiblySensitive;",
                possiblySensitive.asFieldDeclaration(true, "twitter4j.v2", null).codeFragment());

        assertEquals("""
                        this.possiblySensitive = json.getBoolean("possibly_sensitive", false);""",
                possiblySensitive.asConstructorAssignment("twitter4j", false,  null).codeFragment());

        assertEquals("""
                        this.possiblySensitive = json.getBoolean("possibly_sensitive", false);""",
                possiblySensitive.asConstructorAssignment("twitter4j", true,  null).codeFragment());

        assertEquals("""
                        @Override
                        public boolean getPossiblySensitive() {
                            return possiblySensitive;
                        }
                        """,
                possiblySensitive.asGetterImplementation(false, "twitter4j.v2", null, false).codeFragment());


        // nonnull boolean getter starts with "is"
        assertEquals("""
                        @Override
                        public boolean isPossiblySensitive() {
                            return possiblySensitive;
                        }
                        """,
                possiblySensitive.asGetterImplementation(true, "twitter4j.v2", null, false).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> possiblySensitive.asJavaImpl("twitter4j", "twitter4j.v2", false));
        assertThrows(UnsupportedOperationException.class, () -> possiblySensitive.asInterface("twitter4j.v2", false));
    }

}
