package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JSONSchemaLinkTest {
    @Test
    void link() {
        assertEquals("A [GeoJson Point](<a href=\"https://tools.ietf.org/html/rfc7946#section-3.1.2\">https://tools.ietf.org/html/rfc7946#section-3.1.2</a>) geometry object.",
                JSONSchema.link("A [GeoJson Point](https://tools.ietf.org/html/rfc7946#section-3.1.2) geometry object."));
        assertEquals("A [GeoJson Point](<a href=\"https://tools.ietf.org/html/rfc7946#section-3.1.2\">https://tools.ietf.org/html/rfc7946#section-3.1.2</a>) <a href=\"http://twitter4j.org/?foo=bar&bar=baz\">http://twitter4j.org/?foo=bar&bar=baz</a> geometry object.",
                JSONSchema.link("A [GeoJson Point](https://tools.ietf.org/html/rfc7946#section-3.1.2) http://twitter4j.org/?foo=bar&bar=baz geometry object."));
        assertEquals("no link", JSONSchema.link("no link"));
        assertNull(JSONSchema.link(null));
    }

}