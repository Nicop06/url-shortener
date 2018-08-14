package urlshortener.service;

import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import urlshortener.store.UrlStore;
import urlshortener.utils.UrlCache;

/**
 * Serice used to shorten the URL
 */
@Service
public class UrlShortenerService {

    /**
     * The alphabet used to generate the slug
     */
    private static final String SLUG_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * The size of the slug alphabet
     */
    private static final int SLUG_ALPHABET_SIZE = SLUG_ALPHABET.length();

    /**
     * The minimum number of characters in the slug
     */
    private static final int SLUG_MIN_SIZE = 7;

    /**
     * The URL store
     */
    private final UrlStore store;

    /**
     * The URL cache
     */
    private Map<String, String> cache = null;;

    /**
     * The size of the cache
     */
    @Value("${urlshortener.cache.size:1000}")
    private int cacheSize;

    /**
     * Construct a URL service class
     * @param store The service used to store URLs
     */
    public UrlShortenerService(UrlStore store) {
        this.store = store;
        this.cache = Collections.synchronizedMap(new UrlCache(0));
    }

    @PostConstruct
    private void init(){
        // Only called when real application is up
        this.cache = Collections.synchronizedMap(new UrlCache(this.cacheSize));
    }

    /**
     * Shorten a given URL
     *
     * @param urltoshorten the URL to shorten
     * @return the slug of the shorten URL
     */
    public String shortenUrl(String urlToShorten) {
        long urlIndex = this.store.insert(urlToShorten);
        String slug = this.indexToSlug(urlIndex);
        this.cache.put(slug, urlToShorten);
        return slug;
    }

    /**
     * Get the original URL corresponding to a given slug
     *
     * @param slug the slug of the short URL
     * @return the original URL or an empty String if it is invalid
     */
    public String getOriginalUrl(String slug) {
        if (this.cache.containsKey(slug)) {
            return this.cache.get(slug);
        }

        final long index = this.slugToIndex(slug);
        String originalUrl = index >= 0 ? this.store.get(index) : null;
        this.cache.put(slug, originalUrl);
        return originalUrl;
    }

    /**
     * Shorten a given URL
     *
     * @param slug the slug of the shorten URL
     * @return the index of the URL in the store or -1 if the slug if not valid
     */
    private long slugToIndex(String slug) {
        if (slug == null || slug.length() < SLUG_MIN_SIZE) {
            return -1;
        }

        long index = 0;
        long power = 1;
        final int slugSize = slug.length();

        for (int i = 0; i < slugSize; i++) {
            char c = slug.charAt(i);
            int currentIndex = SLUG_ALPHABET.indexOf(c);
            if (currentIndex == -1) {
                return -1;
            }
            index += currentIndex * power;
            power *= SLUG_ALPHABET_SIZE;

        }
        return index;
    }

    /**
     * Shorten a given URL
     *
     * @param urlIndex the index of the URL to slugigy
     * @return the slug of the shorten URL
     */
    private String indexToSlug(long urlIndex) {
        StringBuilder slugBuilder = new StringBuilder();
        int size = 0;
        while (urlIndex > 0 || size < SLUG_MIN_SIZE) {
            slugBuilder.append(SLUG_ALPHABET.charAt((int)(urlIndex % SLUG_ALPHABET_SIZE)));
            urlIndex /= SLUG_ALPHABET_SIZE;
            size++;
        }
        return slugBuilder.toString();
    }
}
