package urlshortener.service;

import org.springframework.stereotype.Service;

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
    private final UrlStoreService store;

    /**
     * Construct a URL service class
     * @param store The service used to store URLs
     */
    public UrlShortenerService(UrlStoreService store) {
        this.store = store;
    }

    /**
     * Shorten a given URL
     *
     * @param urltoshorten the URL to shorten
     * @return the slug of the shorten URL
     */
    public String shortenUrl(String urlToShorten) {
        long urlIndex = store.insert(urlToShorten);
        return indexToSlug(urlIndex);
    }

    /**
     * Get the original URL corresponding to a given slug
     *
     * @param slug the slug of the short URL
     * @return the original URL or an empty String if it is invalid
     */
    public String getOriginalUrl(String slug) {
        final long index = slugToIndex(slug);
        if (index >= 0) {
            return this.store.get(index);
        } else {
            return null;
        }
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
