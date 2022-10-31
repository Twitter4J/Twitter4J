package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaAllOfTest {
    @Test
    void allOf() {
        var extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "CashtagEntity": {
                        "allOf": [
                          {
                            "$ref": "#/components/schemas/EntityIndices"
                          },
                          {
                            "$ref": "#/components/schemas/CashtagFields"
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
                      },
                      "CashtagFields": {
                        "type": "object",
                        "description": "Represent the portion of text recognized as a Cashtag, and its start and end position within the text.",
                        "required": [
                          "tag"
                        ],
                        "properties": {
                          "tag": {
                            "type": "string"
                          }
                        }
                      }
                    }
                  }
                }""");

        JSONSchema attachments = extract.get("CashtagEntity");
        assertEquals("#/components/schemas/CashtagEntity", attachments.jsonPointer());


        JavaFile javaFile = attachments.asInterface("twitter4j.v2");
        assertEquals("CashtagEntity.java", javaFile.fileName());
        String interfaceDeclaration = javaFile.content();
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.NotNull;
                                                
                        /**
                         * CashtagEntity
                         */
                        public interface CashtagEntity {
                            /**
                             * @return Represent a boundary range (start and end index) for a recognized entity (for example a hashtag or a mention). `start` must be smaller than `end`.
                             */
                            @NotNull
                            EntityIndices getEntityIndices();
                                                
                            /**
                             * @return Represent the portion of text recognized as a Cashtag, and its start and end position within the text.
                             */
                            @NotNull
                            CashtagFields getCashtagFields();
                        }
                        """,
                interfaceDeclaration);
    }
}
