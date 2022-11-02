package twitter4j;

import org.intellij.lang.annotations.Language;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

class SchemaLoader {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Path file = Path.of(Objects.requireNonNull(SchemaLoader.class.getResource("/openapi.json")).toURI());

        @Language("JSON") String apiJsonLatest = new String(Files.readAllBytes(file));

        var jsonSchemas = JSONSchema.extract("#/components/schemas", apiJsonLatest);
        for (JSONSchema jsonSchema : jsonSchemas.values()) {
            System.out.println(jsonSchema.typeName() + " , " + jsonSchema.getClass().getName() + ", " + jsonSchema.jsonPointer());
        }
    }

}

class GenerateCodes {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Path file = Path.of(Objects.requireNonNull(SchemaLoader.class.getResource("/openapi.json")).toURI());

        @Language("JSON") String apiJsonLatest = new String(Files.readAllBytes(file));

        JSONSchemaExtractor extractor = JSONSchemaExtractor.from(apiJsonLatest, "#/components/schemas/");
        for (JavaFile javaFile : extractor.interfaceFiles("twitter4j.v2")) {
            String javaInterface = javaFile.content();
            Files.write(Path.of("twitter4j-core", "src", "test-jsonschema", "java", "twitter4j", "v2", javaFile.fileName()), javaInterface.getBytes());
        }
        for (JavaFile javaFile : extractor.javaImplFiles("twitter4j", "twitter4j.v2")) {
            String javaInterface = javaFile.content();
            Files.write(Path.of("twitter4j-core", "src", "test-jsonschema", "java", "twitter4j", javaFile.fileName()), javaInterface.getBytes());
        }
    }
}

@SuppressWarnings("ResultOfMethodCallIgnored")
class DeleteGeneratedCodes {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Path file = Path.of(Objects.requireNonNull(SchemaLoader.class.getResource("/openapi.json")).toURI());

        @Language("JSON") String apiJsonLatest = new String(Files.readAllBytes(file));

        JSONSchemaExtractor extractor = JSONSchemaExtractor.from(apiJsonLatest, "#/components/schemas/");
        for (JavaFile javaFile : extractor.interfaceFiles("twitter4j.v2")) {
            Path.of("twitter4j-core", "src", "test-jsonschema", "java", "twitter4j", "v2", javaFile.fileName()).toFile().delete();
        }
        for (JavaFile javaFile : extractor.javaImplFiles("twitter4j", "twitter4j.v2")) {
            Path.of("twitter4j-core", "src", "test-jsonschema", "java", "twitter4j", javaFile.fileName()).toFile().delete();
        }
    }
}

class LoadLatest {
    public static void main(String[] args) throws TwitterException, IOException {
        HttpClient http = Twitter.newBuilder().buildConfiguration().http;
        HttpResponse httpResponse = http.get("https://api.twitter.com/labs/2/openapi.json");
        String apiJsonLatest = httpResponse.asString();
        String existingApiJson = new String(Files.readAllBytes(Path.of("twitter4j-core", "src", "test", "resources", "openapi.json")));
        if (!existingApiJson.equals(apiJsonLatest)) {
            Files.write(Path.of("twitter4j-core", "src", "test", "resources", "openapi.json"), apiJsonLatest.getBytes());
        }
    }
}
