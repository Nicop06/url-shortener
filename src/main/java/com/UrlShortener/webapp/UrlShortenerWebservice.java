package com.UrlShortener.webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.UrlShortener.service.UrlShortenerService;
import com.UrlShortener.service.UrlStoreService;
 
@Path("/")
public class UrlShortenerWebservice {

    private static class UrlStoreServiceMock implements UrlStoreService {
        public long currentIndex = 0;

        public long insert(String url) {
            return currentIndex++;
        }

        public String get(long index) {
            return "www.google.fr";
        }
    }

    private UrlStoreService store = new UrlStoreServiceMock();
    private UrlShortenerService service = new UrlShortenerService(store);

    @POST
    @Path("/shorten_url")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response shortenUrl(InputStream incomingData) {
        String urlToShorten = "";

        // Read input
        try {
            StringBuilder inputMessage = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                inputMessage.append(line);
            }
        } catch (Exception e) {
            return Response.status(500).entity("Invalid POST data.").build();
        }

        // Shorten URL
        try {
            service.shortenUrl(urlToShorten);
        } catch (MalformedURLException e) {
            return Response.status(500).entity("The URL is malformed.").build();
        } catch (IOException e) {
            return Response.status(500).entity("Unable to connect to the URL.").build();
        }

        return Response.status(201).entity("").build();
    }

    @GET
    @Path("/{shortUrl}")
    public Response getOriginalUrl(InputStream incomingData) {
        return Response.status(200).entity("").build();
    }

}
