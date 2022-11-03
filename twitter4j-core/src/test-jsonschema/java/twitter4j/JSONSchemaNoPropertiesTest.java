package twitter4j;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;

class JSONSchemaNoPropertiesTest {
    @Test
    void noProp() throws IOException, URISyntaxException {
        // #/components/schemas/InvalidRequestProblem/properties/errors/items/properties/parameters
        Path file = Path.of(Objects.requireNonNull(SchemaLoader.class.getResource("/openapi.json")).toURI());

        @Language("JSON") String apiJsonLatest = new String(Files.readAllBytes(file));

        Map<String, JSONSchema> extract = JSONSchema.extract("#/components/schemas/", apiJsonLatest);
        JSONSchema jsonSchema = extract.get("#/components/schemas/InvalidRequestProblem/properties/errors/items/properties/parameters");
        assertFalse(jsonSchema.hasAnyProperties());

    }
}
