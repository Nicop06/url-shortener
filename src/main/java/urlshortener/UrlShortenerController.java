package urlshortener;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlShortenerController {

    @RequestMapping("/")
    public String url() {
        return "www.example.com";
    }

}
