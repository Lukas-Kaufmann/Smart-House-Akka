package at.fhv.sysarch.lab2.homeautomation.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ControlPanelRestController {
    @GetMapping("/test")
    String test();

    @GetMapping("/temperature/{temp}")
    String setTemperature(@PathVariable String temp);
}
