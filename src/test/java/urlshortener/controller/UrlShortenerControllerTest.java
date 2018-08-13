package urlshortener.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import urlshortener.service.UrlShortenerService;

@RunWith(SpringRunner.class)
@WebMvcTest(UrlShortenerController.class)
public class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UrlShortenerService service;

    @Test
    public void whenTheUrlIsInvalidThenStatusShouldBeBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/shorten_url")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"file:///root\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void whenTheUrlIsLocalThenSlugShouldBeReturned() throws Exception {
        Mockito.when(service.shortenUrl("localhost")).thenReturn("ABCDEF5");
        mvc.perform(MockMvcRequestBuilders.post("/shorten_url")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"localhost\"}"))
            .andExpect(status().isCreated())
            .andExpect(content().string("{\"shortened_url\":\"http://localhost/ABCDEF5\"}"));
    }

    @Test
    public void whenTheUrlHasNoSchemeThenSlugShouldBeReturned() throws Exception {
        String originalUrl = "www.example.com";
        Mockito.when(service.shortenUrl(Mockito.anyString())).thenReturn("ABCDEF5");
        mvc.perform(MockMvcRequestBuilders.post("/shorten_url")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"" + originalUrl + "\"}"))
            .andExpect(status().isCreated())
            .andExpect(content().string("{\"shortened_url\":\"http://localhost/ABCDEF5\"}"));
    }

    @Test
    public void whenTheUrlIsHttpsThenSlugShouldBeReturned() throws Exception {
        String originalUrl = "https://www.example.com";
        Mockito.when(service.shortenUrl(originalUrl)).thenReturn("ABCDEF5");
        mvc.perform(MockMvcRequestBuilders.post("/shorten_url")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"" + originalUrl + "\"}"))
            .andExpect(status().isCreated())
            .andExpect(content().string("{\"shortened_url\":\"http://localhost/ABCDEF5\"}"));
    }

    @Test
    public void whenWeGetAnUrlWeShouldBeRedirected() throws Exception {
        String originalUrl = "https://www.example.com";
        Mockito.when(service.getOriginalUrl("ABCDEF5")).thenReturn(originalUrl);
        mvc.perform(MockMvcRequestBuilders.get("/ABCDEF5"))
            .andExpect(status().isMovedPermanently())
            .andExpect(header().string("Location", originalUrl));
    }
}
