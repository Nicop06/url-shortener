package com.UrlShortener;

import java.util.HashMap;
import java.util.Map;

/**
 * A class caching the shorten url
 */
public class UrlCache {

    /**
     * An entry of the URL cache
     */
    private static class CacheEntry {
        String _slug;
        String _target;
        CacheEntry _prev = null;
        CacheEntry _next = null;

        /**
         * Construct a Cache entry
         * @param slug The slug of the shorten URL
         * @param target The target URL
         */
        public CacheEntry(String slug, String target) {
            this._slug = slug;
            this._target = target;
        }
    }

    /** The maximum number of elements the cache can hold */
    private final int _capacity;

    /** The cache store */
    private Map<String, CacheEntry> _cache = new HashMap<>();

    /** The most recent entry */
    private CacheEntry _mostRecentEntry = null;

    /** The least recent entry */
    private CacheEntry _leastRecent = null;

    /**
     * Construct an URL cache with a given capacity
     * @param capacity The number number of elements the cache can hold
     */
    public UrlCache(int capacity) {
        this._capacity = capacity;
    }

    /**
     * Get an entry from the cache
     * @param slug The slug of the shorten URL
     * @return The target URL, or null in case of cache miss
     */
    public synchronized String get(String slug) {
        CacheEntry foundEntry = this._cache.get(slug);
        if (foundEntry != null) {
            this.removeEntry(foundEntry);
            this.setMostRecentEntry(foundEntry);
            return foundEntry._target;
        } else {
            return null;
        }
    }

    /**
     * Insert an entry in the cache
     * @param slug The slug of the shorten URL
     * @param target The target of the URL
     */
    public synchronized void insert(String slug, String target) {
        if (this._capacity == 0)
        {
            return;
        }

        CacheEntry foundEntry = this._cache.get(slug);
        if (foundEntry == null) {
            CacheEntry newEntry = new CacheEntry(slug, target);
            if (this._cache.size() >= this._capacity)
            {
                this._cache.remove(_leastRecent._slug);
                this.removeEntry(_leastRecent);
            }
            this._cache.put(slug, newEntry);
            this.setMostRecentEntry(newEntry);
        } else {
            foundEntry._target = target;
            this.removeEntry(foundEntry);
            this.setMostRecentEntry(foundEntry);
        }
    }

    /**
     * Get the size of the cache
     * @return The number of entries in the cache
     */
    public synchronized int size() {
        return _cache.size();
    }

    /**
     * Set an entry as the most recentn entry
     * @param mostRecentEntry The new most recent entry
     */
    private void setMostRecentEntry(CacheEntry mostRecentEntry) {
        mostRecentEntry._next = this._mostRecentEntry;
        mostRecentEntry._prev = null;

        if (this._mostRecentEntry != null) {
            this._mostRecentEntry._prev = mostRecentEntry;
        }

        this._mostRecentEntry = mostRecentEntry;

        if (this._leastRecent == null) {
            this._leastRecent = mostRecentEntry;
        }
    }

    /**
     * Remove an entry from the most recent list
     * @param entry The entry to remove
     */
    private void removeEntry(CacheEntry entry) {
        if (entry._prev != null) {
            entry._prev._next = entry._next;
        } else {
            this._mostRecentEntry = entry._next;
        }

        if (entry._next != null) {
            entry._next._prev = entry._prev;
        } else {
            this._leastRecent = entry._prev;
        }
    }

}
