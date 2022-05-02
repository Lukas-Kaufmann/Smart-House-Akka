package at.fhv.sysarch.lab2.homeautomation.ui;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import at.fhv.sysarch.lab2.homeautomation.devices.AirCondition;
import at.fhv.sysarch.lab2.homeautomation.simulator.TemperatureSimulator;

import java.util.Scanner;

public class UI extends AbstractBehavior<UI.UICommand> {
    public interface UICommand {}

    public static final class SetTemparatureTo implements UICommand {
        double value;

        public SetTemparatureTo(double value) {
            this.value = value;
        }
    }

    private ActorRef<AirCondition.AirConditionCommand> airCondition;
    private ActorRef<TemperatureSimulator.TempSimCommand> tempSimulator;

    public static Behavior<UICommand> create(ActorRef<AirCondition.AirConditionCommand> airCondition, ActorRef<TemperatureSimulator.TempSimCommand> tempSimulator) {
        return Behaviors.setup(context -> new UI(context, airCondition, tempSimulator));
    }

    private UI(ActorContext<UICommand> context, ActorRef<AirCondition.AirConditionCommand> airCondition, ActorRef<TemperatureSimulator.TempSimCommand> tempSimulator) {
        super(context);
        // TODO: implement actor and behavior as needed
        // TODO: move UI initialization to appropriate place
        this.airCondition = airCondition;
        this.tempSimulator = tempSimulator;
        new Thread(() -> this.runCommandLine() ).start();

        getContext().getLog().info("UI started");
    }

    @Override
    public Receive<UICommand> createReceive() {
        return newReceiveBuilder().onSignal(PostStop.class, signal -> onPostStop()).build();
    }

    private UI onPostStop() {
        getContext().getLog().info("UI stopped");
        return this;
    }


    //TODO remove
    public void runCommandLine() {
        // TODO: Create Actor for UI Input-Handling
        Scanner scanner = new Scanner(System.in);
        String[] input = null;
        String reader = "";


        while (!reader.equalsIgnoreCase("quit") && scanner.hasNextLine()) {
            reader = scanner.nextLine();
            // TODO: change input handling
            String[] command = reader.split(" ");
            if(command[0].equals("t")) {
                this.tempSimulator.tell(new TemperatureSimulator.SetTemperatureTo(Double.valueOf(command[1])));
//                this.tempSensor.tell(new TemperatureSensor.ReadTemperature(Optional.of(Double.valueOf(command[1]))));
            }
            if (command[0].equals("r")) {
                this.tempSimulator.tell(new TemperatureSimulator.SetTemperatureRandomly());
            }
            if(command[0].equals("a")) {
                this.airCondition.tell(new AirCondition.PowerAirCondition(Boolean.valueOf(command[1])));
            }
            // TODO: process Input
        }
        getContext().getLog().info("UI done");
    }
}
