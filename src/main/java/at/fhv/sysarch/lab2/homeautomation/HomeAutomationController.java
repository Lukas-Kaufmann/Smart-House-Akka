package at.fhv.sysarch.lab2.homeautomation;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import at.fhv.sysarch.lab2.homeautomation.devices.*;
import at.fhv.sysarch.lab2.homeautomation.simulator.TemperatureSimulator;
import at.fhv.sysarch.lab2.homeautomation.simulator.WeatherSimulator;

public class HomeAutomationController extends AbstractBehavior<Void>{
    private  ActorRef<AirCondition.AirConditionCommand> airCondition;
    private ActorRef<TemperatureSimulator.TempSimCommand> tempSimulator;

    public static Behavior<Void> create() {
        return Behaviors.setup(HomeAutomationController::new);
    }

    private  HomeAutomationController(ActorContext<Void> context) {
        super(context);
        // TODO: consider guardians and hierarchies. Who should create and communicate with which Actors?
        this.tempSimulator = getContext().spawn(TemperatureSimulator.create("2", "3"), "temperatureSimulator");
        this.airCondition = getContext().spawn(AirCondition.create("2", "1", this.tempSimulator), "AirCondition");
        ActorRef<Blinds.BlindsCommand> blinds = getContext().spawn(Blinds.create(), "Blinds");
        ActorRef<MediaStation.MediaCommand> mediaStation = getContext().spawn(MediaStation.create(blinds), "MediaStation");
        ActorRef<WeatherSimulator.WeatherSimCommand> weatherSim = getContext().spawn(WeatherSimulator.create(), "WeatherSimulator");
        ActorRef<WeatherSensor.Command> weatherSensor = getContext().spawn(WeatherSensor.create(weatherSim, blinds), "WeatherSensor");

        ActorRef<Fridge.Command> fridge = getContext().spawn(Fridge.create(), "Fridge");

        getContext().getLog().info("HomeAutomation Application started");
    }

    @Override
    public Receive<Void> createReceive() {
        return newReceiveBuilder().onSignal(PostStop.class, signal -> onPostStop()).build();
    }

    private HomeAutomationController onPostStop() {
        getContext().getLog().info("HomeAutomation Application stopped");
        return this;
    }
}
