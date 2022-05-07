package at.fhv.sysarch.lab2.homeautomation.devices;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Blinds extends AbstractBehavior<Blinds.BlindsCommand> {
    public interface BlindsCommand {}
    public static class MovieStartedPlaying implements BlindsCommand {
        public MovieStartedPlaying() {}
    }
    public static class MovieStoppedPlaying implements BlindsCommand {
        public MovieStoppedPlaying() {}
    }

    public Blinds(ActorContext<BlindsCommand> context) {
        super(context);
    }

    public static Behavior<BlindsCommand> create() {
        return Behaviors.setup(context -> new Blinds(context));
    }

    @Override
    public Receive<BlindsCommand> createReceive() {
        return newReceiveBuilder().build();
    }
}
