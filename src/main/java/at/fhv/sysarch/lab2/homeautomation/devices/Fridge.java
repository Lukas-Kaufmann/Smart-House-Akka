package at.fhv.sysarch.lab2.homeautomation.devices;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;

public class Fridge extends AbstractBehavior<Fridge.Command> {
    public interface Command {}

    public static class GetProducts implements Command {

    }
    public static class GetOrders implements Command {

    }
    public static class OrderProduct implements Command {

    }



    public static Behavior<Command> create() {
        return Behaviors.setup(Fridge::new);
    }
    public Fridge(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .build();
    }
}
