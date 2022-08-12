package edu.uci.ics.localization;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class messager {
    private static final String template = "Hello, %s!";

    @GetMapping("/message")
    public message greeting(String time, @RequestParam(value = "name", defaultValue = "World") String name) {
        return new message(time, String.format(template, name));
    }
}
