package at.fhv.sysarch.lab2.homeautomation.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ControllPanelRestController {


    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }
}
