package urlshortener.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortenedUrl {
    private String shortened_url;

    public ShortenedUrl(String shortened_url) {
        this.shortened_url = shortened_url;
    }

    @JsonProperty("shortened_url")
    public String getshortenedUrl() {
        return this.shortened_url;
    }
}
