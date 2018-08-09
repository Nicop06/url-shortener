package com.UrlShortener;

/**
 * The service to store shorten URL
 */
interface UrlStoreService {
    /**
     * Insert an url in the store
     *
     * @param url the URL to insert
     * @return the index of the URL in the store
     */
    public abstract long insert(String url);

    /**
     * Get an URL from the store
     *
     * @param index the index of the URL
     * @return the target URL
     */
    public abstract String get(int index);
}
