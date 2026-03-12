package com.olivmaher.urlshortener;

import com.olivmaher.urlshortener.util.Base62Encoder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Base62EncoderTest {

    @Test
    void encode_shouldReturnNonEmptyString() {
        String result = Base62Encoder.encode(1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void encode_shouldReturnDifferentCodesForDifferentIds() {
        String first = Base62Encoder.encode(1);
        String second = Base62Encoder.encode(2);
        assertNotEquals(first, second);
    }

    @Test
    void encode_shouldReturnLongerStringForLargerNumbers() {
        String small = Base62Encoder.encode(1);
        String large = Base62Encoder.encode(10000000);
        assertTrue(large.length() > small.length());
    }

    @Test
    void encode_shouldReturnConsistentResults() {
        String first = Base62Encoder.encode(12345);
        String second = Base62Encoder.encode(12345);
        assertEquals(first, second);
    }
}