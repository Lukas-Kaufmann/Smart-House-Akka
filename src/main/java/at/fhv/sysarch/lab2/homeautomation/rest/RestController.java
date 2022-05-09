package at.fhv.sysarch.lab2.homeautomation.rest;

import akka.actor.typed.ActorSystem;
import at.fhv.sysarch.lab2.homeautomation.HomeAutomationController;
import at.fhv.sysarch.lab2.homeautomation.devices.AirCondition;
import at.fhv.sysarch.lab2.homeautomation.simulator.TemperatureSimulator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.PostConstruct;

@org.springframework.web.bind.annotation.RestController
@CrossOrigin
public class RestController {

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

    @GetMapping(path = "/aircondition/on")
    public String airConditionOn() {
        AirCondition.instanceRef.tell(new AirCondition.PowerAirCondition(true));
        return "Ok";
    }
    @GetMapping(path = "/aircondition/off")
    public String airConditionOff() {
        AirCondition.instanceRef.tell(new AirCondition.PowerAirCondition(false));
        return "Ok";
    }
}
