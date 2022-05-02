package at.fhv.sysarch.lab2.homeautomation.simulator;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;

public class WeatherSimulator extends AbstractBehavior<WeatherSimulator.WeatherSimCommand> {
    public interface WeatherSimCommand {}


    public WeatherSimulator(ActorContext context) {
        super(context);
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
