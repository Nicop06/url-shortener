package urlshortener.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UrlShortenerServiceTest {
    
    private UrlStoreService urlStore;
    private UrlShortenerService urlShortenerService;

    @Before
    public void initEach() {
        urlStore = Mockito.mock(UrlStoreService.class);
        urlShortenerService = new UrlShortenerService(urlStore);
    }

    @Test
    public final void testShortenUrlWithOneCharacterSlug() {
        Mockito.when(urlStore.insert(Mockito.anyString())).thenReturn(5L);
        String slug = urlShortenerService.shortenUrl("http://example.com");
        assertEquals("FAAAAAA", slug);
    }

    @Test
    public final void testShortenUrlWithThreeCharacterSlug() {
        Mockito.when(urlStore.insert(Mockito.anyString())).thenReturn(12345L);
        String slug = urlShortenerService.shortenUrl("http://example.com");
        assertEquals("HNDAAAA", slug);
    }

    @Test
    public final void testShortenUrlWithSevenCharacterSlug() {
        Mockito.when(urlStore.insert(Mockito.anyString())).thenReturn(3521614606205L);
        String slug = urlShortenerService.shortenUrl("http://example.com");
        assertEquals("7999999", slug);
    }

    @Test
    public final void testShortenUrlWithEightCharacterSlug() {
        Mockito.when(urlStore.insert(Mockito.anyString())).thenReturn(3521614606213L);
        String slug = urlShortenerService.shortenUrl("http://example.com");
        assertEquals("FAAAAAAB", slug);
    }

    @Test
    public final void testGetUrlWithOneCharacterSlug() {
        Mockito.when(urlStore.get(5L)).thenReturn("http://www.example.com");
        String originalUrl = urlShortenerService.getOriginalUrl("FAAAAAA");
        assertEquals("http://www.example.com", originalUrl);
    }

    @Test
    public final void testGetUrlWithThreeCharacterSlug() {
        Mockito.when(urlStore.get(12345L)).thenReturn("http://www.example.com");
        String originalUrl = urlShortenerService.getOriginalUrl("HNDAAAA");
        assertEquals("http://www.example.com", originalUrl);
    }

    @Test
    public final void testGetUrlWithSevenCharacterSlug() {
        Mockito.when(urlStore.get(3521614606205L)).thenReturn("http://www.example.com");
        String originalUrl = urlShortenerService.getOriginalUrl("7999999");
        assertEquals("http://www.example.com", originalUrl);
    }

    @Test
    public final void testGetUrlWithEightCharacterSlug() {
        Mockito.when(urlStore.get(3521614606213L)).thenReturn("http://www.example.com");
        String originalUrl = urlShortenerService.getOriginalUrl("FAAAAAAB");
        assertEquals("http://www.example.com", originalUrl);
    }

    @Test
    public final void whenWeGetAnInexistentUrlThenItShouldReturnAnEmtpyURL() {
        Mockito.when(urlStore.get(5L)).thenReturn("https://www.example.com");
        String originalUrl = urlShortenerService.getOriginalUrl("EAAAAAA");
        assertEquals(null, originalUrl);
    }

    @Test
    public final void whenWeGetAnUrlTooShortThenItShouldReturnAnEmptyURL() {
        Mockito.when(urlStore.get(5L)).thenReturn("https://www.example.com");
        String originalUrl = urlShortenerService.getOriginalUrl("FAAAAA");
        assertEquals(null, originalUrl);
    }

    @Test
    public final void whenWeGetAnInvalidUrlThenExceptionIsThrown() {
        Mockito.when(urlStore.get(5L)).thenReturn("https://www.example.com");
        String originalUrl = urlShortenerService.getOriginalUrl("AAAAAA#");
        assertEquals(null, originalUrl);
    }
}
