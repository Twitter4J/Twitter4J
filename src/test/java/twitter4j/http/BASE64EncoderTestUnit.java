package twitter4j.http;

import junit.framework.TestCase;

public class BASE64EncoderTestUnit extends TestCase {
    public BASE64EncoderTestUnit(String name) {
        super(name);
    }


    public void testEncode() {
        BASE64Encoder encoder = new BASE64Encoder();
        assertEquals("QUJDREVGRw==", encoder.encode("ABCDEFG".getBytes()));
        assertEquals("VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2cu", encoder.encode("The quick brown fox jumped over the lazy dog.".getBytes()));
        assertEquals("bGVhc3VyZS4=", encoder.encode("leasure.".getBytes()));
        assertEquals("ZWFzdXJlLg==", encoder.encode("easure.".getBytes()));
        assertEquals("YXN1cmUu", encoder.encode("asure.".getBytes()));
        assertEquals("c3VyZS4=", encoder.encode("sure.".getBytes()));
//        assertEquals("", encoder.encode("".getBytes()));
//        assertEquals("", encoder.encode("".getBytes()));
//        assertEquals("", encoder.encode("".getBytes()));
//        assertEquals("", encoder.encode("".getBytes()));
    }
}
