package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JSONSchemaSuppressWarningsTest {
    @Test
    void suppressWarningsInt() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error" : {
                    "required" : [ "code"],
                    "properties" : {
                      "code" : {
                        "type" : "integer",
                        "format" : "int32"
                      }
                    }
                  }
                      
                }
                """);
        JSONSchema error = extract.get("#/Error");
        // primitive type doesn't require suppressWarnings
        assertEquals(0, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
    }

    @Test
    void noSuppressWarningsInt() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error" : {
                    "properties" : {
                      "code" : {
                        "type" : "integer"
                      }
                    }
                  }
                      
                }
                """);
        JSONSchema error = extract.get("#/Error");
        assertEquals(0, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
    }

    @Test
    void suppressWarningsBoolean() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error" : {
                    "required" : [ "code"],
                    "properties" : {
                      "code" : {
                        "type" : "boolean"
                      }
                    }
                  }
                      
                }
                """);
        JSONSchema error = extract.get("#/Error");
        // primitive type doesn't require suppressWarnings
        assertEquals(0, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
    }

    @Test
    void noSuppressWarningsBoolean() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error" : {
                    "properties" : {
                      "code" : {
                        "type" : "boolean"
                      }
                    }
                  }
                      
                }
                """);
        JSONSchema error = extract.get("#/Error");
        assertEquals(0, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
    }

    @Test
    void suppressWarningsNumber() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error" : {
                    "required" : [ "code"],
                    "properties" : {
                      "code" : {
                        "type" : "integer"
                      }
                    }
                  }
                      
                }
                """);
        JSONSchema error = extract.get("#/Error");
        assertEquals(0, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
    }

    @Test
    void noSuppressWarningsNumber() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error" : {
                    "properties" : {
                      "code" : {
                        "type" : "integer",
                        "format" : "int32"
                      }
                    }
                  }
                      
                }
                """);
        JSONSchema error = extract.get("#/Error");
        assertEquals(0, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
    }

    @Test
    void suppressWarningsString() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error" : {
                    "required" : [ "code" ],
                    "properties" : {
                      "code" : {
                        "type" : "string"
                      }
                    }
                  }
                      
                }
                """);
        JSONSchema error = extract.get("#/Error");
        assertEquals(1, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
        assertTrue(error.asConstructorAssignments("twitter4j").methodLevelAnnotations().contains("@SuppressWarnings(\"ConstantConditions\")"));
    }

    @Test
    void noSuppressWarningsString() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error" : {
                    "properties" : {
                      "code" : {
                        "type" : "string"
                      }
                    }
                  }
                      
                }
                """);
        JSONSchema error = extract.get("#/Error");
        assertEquals(0, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
    }


    @Test
    void suppressWarningsObject() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error": {
                    "required": [
                      "code"
                    ],
                    "properties": {
                      "code": {
                        "type": "object",
                        "properties": {
                          "prop1": {
                            "type": "string"
                          }
                        }
                      }
                    }
                  }
                }
                """);
        JSONSchema error = extract.get("#/Error");
        assertEquals(1, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
        assertTrue(error.asConstructorAssignments("twitter4j").methodLevelAnnotations().contains("@SuppressWarnings(\"ConstantConditions\")"));
    }

    @Test
    void noSuppressWarningsObject() {
        var extract = JSONSchema.extract("#/", """
                {
                  "Error": {
                    "properties": {
                      "code": {
                        "type": "object",
                        "properties": {
                          "prop1": {
                            "type": "string"
                          }
                        }
                      }
                    }
                  }
                }
                               
                """);
        JSONSchema error = extract.get("#/Error");
        assertEquals(0, error.asConstructorAssignments("twitter4j").methodLevelAnnotations().size());
    }
}

