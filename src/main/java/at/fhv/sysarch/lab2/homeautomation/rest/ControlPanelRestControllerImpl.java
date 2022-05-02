package at.fhv.sysarch.lab2.homeautomation.rest;

import akka.actor.typed.ActorRef;
import at.fhv.sysarch.lab2.homeautomation.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ControlPanelRestControllerImpl implements ControlPanelRestController {

    private ActorRef<UI.UICommand> actor;

    public void setActor(ActorRef<UI.UICommand> actor) {
        this.actor = actor;
    }

    @Override
    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @Override
    @GetMapping("/temperature/{temp}")
    public String setTemperature(@PathVariable String temp) {
        double temperature = Double.parseDouble(temp);
        actor.tell(new UI.SetTemparatureTo(temperature));

        return "Ok";
    }
}
