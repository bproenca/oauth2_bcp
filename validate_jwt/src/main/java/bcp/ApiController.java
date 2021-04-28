package bcp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/tenant/{tenant}/hello")
    public String publicApi(@PathVariable(required = true) String tenant) {
        return "Hello, this is a protected API (tenant=" + tenant + ")";
    }

}