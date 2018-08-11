package urlshortener.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleUrlStoreService implements UrlStoreService {
    private List<String> urls = new ArrayList<>();

    public synchronized long insert(String url) {
        urls.add(url);
        return urls.size() - 1;
    }

    public synchronized String get(long index) {
        if (index < urls.size()) {
            return urls.get((int) index);
        } else {
            return null;
        }
    }
}


