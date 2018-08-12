package urlshortener.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import urlshortener.model.OriginalUrl;
import urlshortener.model.ShortenedUrl;

@RestController
public class UrlShortenerController {

    @PostMapping(value = "/shorten_url",
        consumes = "application/json",
        produces = "application/json")
    @ResponseBody
    public ShortenedUrl shortenUrl(@Valid @RequestBody OriginalUrl originalUrl) {
        return new ShortenedUrl(originalUrl.getUrl());
    }

    @GetMapping(path="/{slug}")
    public void getUrl(@PathVariable(value = "slug", required = true) String slug, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", "http://www.example.com/slug");
    }

}
