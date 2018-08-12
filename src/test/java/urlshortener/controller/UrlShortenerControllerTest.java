package urlshortener.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenTheUrlIsInvalidThenStatusShouldBeBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/shorten_url")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"file:///root\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void whenTheUrlHasNoSchemeThenSlugShouldBeReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/shorten_url")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"www.example.com\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("{\"shortened_url\":\"www.example.com\"}"));
    }

    @Test
    public void whenTheUrlIsHttpsThenSlugShouldBeReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/shorten_url")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"https://www.example.com\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("{\"shortened_url\":\"https://www.example.com\"}"));
    }

    @Test
    public void whenWeGetAnUrlWeShouldBeRedirected() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/ABCDEF5"))
            .andExpect(status().isMovedPermanently())
            .andExpect(header().string("Location", "https://www.example.com"));
    }
}
