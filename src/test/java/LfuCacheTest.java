import com.example.mytask.cache.LfuCache;
import com.example.mytask.cache.LruCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LfuCacheTest {
    private static final String A = "A";

    private static final String B = "B";

    private static final String C = "C";

    private static final String D = "D";

    private static final String E = "E";

    private LfuCache<String, String> cache;

    private static void assertMiss(LfuCache<String, String> cache, String key) {
        assertNull(cache.get(key));
    }

    private static void assertHit(LfuCache<String, String> cache, String key, String value) {
        assertEquals(cache.get(key), value);
    }


    @BeforeEach
    public void setUp() {
        cache = new LfuCache<>(3);
    }

    @AfterEach
    public void tearDown() {
        cache.clear();
        cache = null;
    }

    @Test
    public void constructorDoesNotAllowZeroCacheSize() {
        try {
            new LruCache(0);
            fail();
        } catch (IllegalArgumentException expected) {
            //nothing
        }
    }

    @Test
    public void cannotPutNullKey() {
        try {
            cache.put(null, "a");
            fail();
        } catch (NullPointerException expected) {
            // nothing
        }
    }

    @Test
    public void cannotPutNullValue() {
        try {
            cache.put("a", null);
            fail();
        } catch (NullPointerException expected) {
            // nothing
        }
    }

    @Test
    public void cannotRemoveNullKey() {
        try {
            cache.remove(null);
            fail();
        } catch (NullPointerException expected) {
            // nothing
        }
    }

    /**
     * Replacing the value for a key doesn't cause an eviction but it does bring the replaced entry to
     * the front of the queue.
     */

    @Test
    public void throwsWithNullKey() {
        try {
            cache.get(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // nothing
        }
    }
}
