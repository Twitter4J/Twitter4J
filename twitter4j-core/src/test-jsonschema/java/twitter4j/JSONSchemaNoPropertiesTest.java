package twitter4j;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

class JSONSchemaNoPropertiesTest {
    @Test
    void noProp() {
        Map<String, JSONSchema> extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "errors": {
                        "type": "array",
                        "items": {
                          "type": "object",
                          "properties": {
                            "parameters": {
                              "type": "object",
                              "additionalProperties": {
                                "type": "array",
                                "items": {
                                  "type": "string"
                                }
                              }
                            },
                            "message": {
                              "type": "string"
                            }
                          }
                        }
                      }
                    }
                  }
                }
                """);
        JSONSchema jsonSchema = extract.get("#/components/schemas/errors/items/properties/parameters");
        assertFalse(jsonSchema.hasAnyProperties());

    }
}
