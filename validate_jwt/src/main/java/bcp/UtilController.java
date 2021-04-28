package bcp;

import java.time.LocalTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/util")
public class UtilController {

    @GetMapping("/time")
    public String publicApi() {
        return "Hello, now it's: " + LocalTime.now();
    }

}
