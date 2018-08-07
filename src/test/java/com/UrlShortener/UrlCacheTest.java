package com.UrlShortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

public class UrlCacheTest {
    @Test
    public final void whenUrlCacheHasSizeZeroThenItShouldRemainEmpty() {
        UrlCache urlCache = new UrlCache(0);
        urlCache.insert("toto", "myurl");

        assertAll("cache content",
                () -> assertEquals(null, urlCache.get("toto")),
                () -> assertEquals(0, urlCache.size())
                );
    }

    @Test
    public final void whenUrlCacheHasSizeOneThenItShouldKeepTheLastInsertedElement() {
        UrlCache urlCache = new UrlCache(1);
        urlCache.insert("toto", "myurl");
        urlCache.insert("titi", "mynewurl");
        assertAll("cache content",
                () -> assertEquals(1, urlCache.size()),
                () -> assertEquals("mynewurl", urlCache.get("titi"))
                );
    }

    @Test
    public final void whenUrlCacheIsCreateThenItShouldBeEmpty() {
        UrlCache urlCache = new UrlCache(3);
        assertAll("cache content",
                () -> assertEquals(0, urlCache.size()),
                () -> assertEquals(null, urlCache.get("1"))
                );
    }

    @Test
    public final void whenUrlCacheIsNotFullThenItShouldKeepEntries() {
        UrlCache urlCache = new UrlCache(3);
        urlCache.insert("1", "myurl1");
        urlCache.insert("2", "myurl2");
        urlCache.insert("3", "myurl3");

        assertAll("cache content",
                () -> assertEquals(3, urlCache.size()),
                () -> assertEquals("myurl1", urlCache.get("1")),
                () -> assertEquals("myurl2", urlCache.get("2")),
                () -> assertEquals("myurl3", urlCache.get("3"))
                );
    }

    @Test
    public final void whenUrlCacheIsFullThenItShouldEvictTheLeastRecentlyInsertedEntry() {
        UrlCache urlCache = new UrlCache(3);
        urlCache.insert("1", "myurl1");
        urlCache.insert("2", "myurl2");
        urlCache.insert("3", "myurl3");
        urlCache.insert("4", "myurl4");

        assertAll("cache content",
                () -> assertEquals(3, urlCache.size()),
                () -> assertEquals(null    , urlCache.get("1")),
                () -> assertEquals("myurl2", urlCache.get("2")),
                () -> assertEquals("myurl3", urlCache.get("3")),
                () -> assertEquals("myurl4", urlCache.get("4"))
                );
    }

    @Test
    public final void whenUrlCacheIsFullThenItShouldEvictTheLeastRecentlyUpdatedEntries() {
        UrlCache urlCache = new UrlCache(3);
        urlCache.insert("1", "myurl1");
        urlCache.insert("2", "myurl2");
        urlCache.insert("3", "myurl3");

        urlCache.insert("1", "myurl4");
        urlCache.insert("5", "myurl5");

        assertAll("cache content",
                () -> assertEquals(3, urlCache.size()),
                () -> assertEquals("myurl4", urlCache.get("1")),
                () -> assertEquals(null    , urlCache.get("2")),
                () -> assertEquals("myurl3", urlCache.get("3")),
                () -> assertEquals("myurl5", urlCache.get("5"))
                );
    }

    @Test
    public final void whenUrlCacheIsFullThenItShouldEvictTheLeastRecentlyAccessedEntries() {
        UrlCache urlCache = new UrlCache(3);
        urlCache.insert("1", "myurl1");
        urlCache.insert("2", "myurl2");
        urlCache.insert("3", "myurl3");
        urlCache.get("1");
        urlCache.insert("4", "myurl4");
        urlCache.insert("5", "myurl5");

        assertAll("cache content",
                () -> assertEquals(3, urlCache.size()),
                () -> assertEquals("myurl1", urlCache.get("1")),
                () -> assertEquals(null    , urlCache.get("2")),
                () -> assertEquals(null    , urlCache.get("3")),
                () -> assertEquals("myurl4", urlCache.get("4")),
                () -> assertEquals("myurl5", urlCache.get("5"))
                );
    }
}
