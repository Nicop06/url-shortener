package urlshortener.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A LRU cache shorten URL
 */
public class UrlCache extends LinkedHashMap<String, String> {

    /** The serial version id */
    private static final long serialVersionUID = 8089957330899776767L;

    /** The maximum number of elements the cache can hold */
    private final int capacity;

    /**
     * Construct an URL cache with a given capacity
     * @param capacity The number number of elements the cache can hold
     */
    public UrlCache(int capacity) {
        super(capacity + 1, 1.0f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<String, String> eldest) {
        return super.size() > capacity;
    }
}
