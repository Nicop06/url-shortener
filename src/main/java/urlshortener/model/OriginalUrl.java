package urlshortener.model;

import urlshortener.validator.UrlConstraint;

public class OriginalUrl {
    @UrlConstraint
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
