package com.UrlShortener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlShortenerServiceTest {

    private static class UrlStoreServiceMock implements UrlStoreService {
        public long currentIndex = 0;

        public long insert(String url) {
            return currentIndex++;
        }

        public String get(int index) {
            return "";
        }
    }
    
    private UrlStoreServiceMock urlStore;
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    public void initEach() {
        urlStore = new UrlStoreServiceMock();
        urlShortenerService = new UrlShortenerService(urlStore);
    }

    @Test
    public final void whenTheUrlIsEmptyThenExceptionIsThrown() {
        Assertions.assertThrows(MalformedURLException.class, () -> {
            urlShortenerService.shortenUrl("");
        });
    }

    @Test
    public final void whenTheUrlIsNullThenExceptionIsThrown() {
        Assertions.assertThrows(MalformedURLException.class, () -> {
            urlShortenerService.shortenUrl(null);
        });
    }

    @Test
    public final void whenTheUrlIsNotHttpThenExceptionIsThrown() {
        Assertions.assertThrows(UnknownHostException.class, () -> {
            urlShortenerService.shortenUrl("ftp://example.com");
        });
    }

    @Test
    public final void whenTheUrlIsNotFoundThenExceptionIsThrown() {
        Assertions.assertThrows(IOException.class, () -> {
            urlShortenerService.shortenUrl("http://example.com/badaddr");
        });
    }

    @Test
    public final void whenTheUrlIsFoundThenItShouldReturnTheSlug() throws IOException {
        urlStore.currentIndex = 12345L;
        String slug = urlShortenerService.shortenUrl("example.com");
        Assertions.assertEquals("HNDAAAA", slug);
    }

    @Test
    public final void whenTheUrlStartingWithHttpIsFoundThenItShouldReturnTheSlug() throws IOException {
        urlStore.currentIndex = 5L;
        String slug = urlShortenerService.shortenUrl("http://example.com");
        Assertions.assertEquals("FAAAAAA", slug);
    }

    @Test
    public final void whenTheUrlStartingWithHttpsIsFoundThenItShouldReturnTheSlug() throws IOException {
        urlStore.currentIndex = 3521614606213L;
        String slug = urlShortenerService.shortenUrl("https://example.com");
        Assertions.assertEquals("FAAAAAAB", slug);
    }
}
