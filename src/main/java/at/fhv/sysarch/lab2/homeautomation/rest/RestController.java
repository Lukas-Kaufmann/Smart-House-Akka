package at.fhv.sysarch.lab2.homeautomation.rest;

import akka.actor.typed.ActorSystem;
import at.fhv.sysarch.lab2.homeautomation.HomeAutomationController;
import at.fhv.sysarch.lab2.homeautomation.simulator.TemperatureSimulator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.PostConstruct;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @PostConstruct
    public void setup() {
        ActorSystem<Void> home = ActorSystem.create(HomeAutomationController.create(), "HomeAutomation");
    }


    @GetMapping(path = "/randomTemp")
    public String setRandomTemp() {
        TemperatureSimulator.instanceRef.tell(new TemperatureSimulator.SetTemperatureRandomly());
        return "Ok";
    }

    @GetMapping(path = "/setTemp/{value}")
    public String setTemp(@PathVariable double value) {
        TemperatureSimulator.instanceRef.tell(new TemperatureSimulator.SetTemperatureTo(value));
        return "Ok";
    }



    @GetMapping(path = "/test")
    public String helloWorld() {
        return "Hello World";
    }
}
