package com.UrlShortener.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Serice used to shorten the URL
 */
public class UrlShortenerService {

    /**
     * The Maximum number of redirect allowed before URL is rejected by the service
     */
    private static final int MAXIMUM_REDIRECT = 3;

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
    UrlStoreService store;

    /**
     * Construct a URL service class
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
    public String shortenUrl(String urlToShorten) throws IOException {
        checkHttpUrl(urlToShorten);
        long urlIndex = store.insert(urlToShorten);
        return indexToSlug(urlIndex);
    }

    /**
     * Get the original URL corresponding to a given slug
     *
     * @param slug the slug of the short URL
     * @return the original URL or an empty String if it is invalid
     */
    public String getOriginalUrl(String slug) throws IOException {
        final long index = slugToIndex(slug);
        return this.store.get(index);
    }

    /**
     * Shorten a given URL
     *
     * @param slug the slug of the shorten URL
     * @return the index of the URL in the store
     */
    private long slugToIndex(String slug) throws IllegalArgumentException {
        if (slug == null || slug.length() < SLUG_MIN_SIZE) {
            throw new IllegalArgumentException("URL size should be at least " + SLUG_MIN_SIZE);
        }

        long index = 0;
        long power = 1;
        final int slugSize = slug.length();

        for (int i = 0; i < slugSize; i++) {
            char c = slug.charAt(i);
            int currentIndex = SLUG_ALPHABET.indexOf(c);
            if (currentIndex == -1) {
                throw new IllegalArgumentException("Invalid character found in URL: " + c);
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

    /**
     * Check a given URL
     *
     * @param urlToCheck the URL to shorten
     * @throws IOException if the URL is invalid
     */
    private void checkHttpUrl(String urlToCheck) throws IOException {
        String realUrl = urlToCheck;
        if (realUrl != null && !realUrl.isEmpty() && !realUrl.startsWith("http://")
                && !realUrl.startsWith("https://")) {
            realUrl = "http://" + realUrl;
        }

        HttpURLConnection connection;

        // Follow the redirections
        for (int nbRedirect = 0; nbRedirect < MAXIMUM_REDIRECT; nbRedirect++) {
            URL url = new URL(realUrl);
            connection = (HttpURLConnection) url.openConnection();
            int status = connection.getResponseCode();
            nbRedirect++;

            if (status == HttpURLConnection.HTTP_OK) {
                break;
            } else if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER)
            {
                realUrl = connection.getHeaderField("Location");
            } else {
                throw new IOException();
            }
        }
    }
}
