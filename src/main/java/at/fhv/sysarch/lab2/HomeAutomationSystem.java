package at.fhv.sysarch.lab2;

import akka.actor.typed.ActorSystem;
import at.fhv.sysarch.lab2.homeautomation.HomeAutomationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeAutomationSystem {

    public static void main(String[] args) {
        SpringApplication.run(HomeAutomationSystem.class);
        ActorSystem<Void> home = ActorSystem.create(HomeAutomationController.create(), "HomeAutomation");
    }


}
