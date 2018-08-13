package urlshortener.controller;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import urlshortener.model.OriginalUrl;
import urlshortener.model.ShortenedUrl;
import urlshortener.service.UrlShortenerService;

@RestController
public class UrlShortenerController {

    private final UrlShortenerService service;

    UrlShortenerController(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping(value = "/shorten_url",
        consumes = "application/json",
        produces = "application/json")
    @ResponseBody
    public ShortenedUrl shortenUrl(@Valid @RequestBody OriginalUrl originalUrl, HttpServletResponse response, HttpServletRequest request) {
        String slug = this.service.shortenUrl(originalUrl.getUrl());
        if (slug != null) {
            StringBuffer shortenedUrl = request.getRequestURL();
            int URLLength = shortenedUrl.length();
            int URILength = request.getRequestURI().length();
            shortenedUrl.replace(URLLength - URILength, URLLength, "/" + slug);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return new ShortenedUrl(shortenedUrl.toString());
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ShortenedUrl("");
        }
    }

    @GetMapping(path="/{slug}")
    public void getUrl(@PathVariable(value = "slug", required = true) String slug, HttpServletResponse response) {
        String originalUrl = service.getOriginalUrl(slug);
        if (originalUrl != null) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", originalUrl);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
