package twitter4j;

import org.junit.jupiter.api.Test;

class JSONSchemaRefResolveTest {
    @Test
    void allOf() {
        var extract = JSONSchema.extract("#/components/schemas/", """
                {
                       "components": {
                         "schemas": {
                                                    
                           "ContextAnnotation": {
                             "type": "object",
                             "description": "Annotation inferred from the tweet text.",
                             "required": [
                               "domain",
                               "entity"
                             ],
                             "properties": {
                               "domain": {
                                 "type": "string"
                               }
                             }
                           },
                           "Tweet": {
                             "required": [
                               "text"
                             ],
                             "properties": {
                               "text": {
                                 "type": "string",
                                 "description": "The content of the Tweet."
                               },
                               "context_annotations": {
                                 "type": "array",
                                 "items": {
                                   "$ref": "#/components/schemas/ContextAnnotation"
                                 },
                                 "minItems": 1
                               }
                             }
                           }
                         }
                       }
                     }""");

        for (JSONSchema value : extract.values()) {
            if (value instanceof ObjectSchema) {
                value.asInterface("twitter4j.v2");
            }

        }
    }
}
