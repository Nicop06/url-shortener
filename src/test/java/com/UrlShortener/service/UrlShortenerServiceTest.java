package com.UrlShortener.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UrlShortenerServiceTest {
    
    private UrlStoreService urlStore;
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    public void initEach() {
        urlStore = Mockito.mock(UrlStoreService.class);
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
        Mockito.when(urlStore.insert(Mockito.anyString())).thenReturn(12345L);
        String slug = urlShortenerService.shortenUrl("example.com");
        Assertions.assertEquals("HNDAAAA", slug);
    }

    @Test
    public final void whenTheUrlStartingWithHttpIsFoundThenItShouldReturnTheSlug() throws IOException {
        Mockito.when(urlStore.insert(Mockito.anyString())).thenReturn(5L);
        String slug = urlShortenerService.shortenUrl("http://example.com");
        Assertions.assertEquals("FAAAAAA", slug);
    }

    @Test
    public final void whenTheUrlStartingWithHttpsIsFoundThenItShouldReturnTheSlug() throws IOException {
        Mockito.when(urlStore.insert(Mockito.anyString())).thenReturn(3521614606213L);
        String slug = urlShortenerService.shortenUrl("https://example.com");
        Assertions.assertEquals("FAAAAAAB", slug);
    }
}
