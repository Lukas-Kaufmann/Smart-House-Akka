package at.fhv.sysarch.lab2.homeautomation.devices;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import at.fhv.sysarch.lab2.homeautomation.simulator.WeatherSimulator;

public class Blinds extends AbstractBehavior<Blinds.BlindsCommand> {
    public interface BlindsCommand {}
    public static class MovieStartedPlaying implements BlindsCommand {
        public MovieStartedPlaying() {}
    }
    public static class MovieStoppedPlaying implements BlindsCommand {
        public MovieStoppedPlaying() {}
    }
    public static class WeatherTurnedSunny implements BlindsCommand {}

    public static class SunIsGone implements BlindsCommand {}

    private boolean open;
    private boolean isSunny;
    private boolean moviePlaying;

    public Blinds(ActorContext<BlindsCommand> context) {
        super(context);
    }
    public static Behavior<BlindsCommand> create() {
        return Behaviors.setup(context -> new Blinds(context));
    }

    public void updateState() {
        if (moviePlaying || isSunny) {
            //means blinds should be closed
            if (open) {
                getContext().getLog().info("Blinds closing");
                this.open = false;
            }
        } else {
            if (!open) {
                getContext().getLog().info("Blinds opening");
                this.open = true;
            }
        }
    }

    private Behavior<BlindsCommand> onWeatherSunny(WeatherTurnedSunny msg) {
        this.isSunny = true;
        updateState();
        return this;
    }
    private Behavior<BlindsCommand> onWeatherNotSunny(SunIsGone msg) {
        this.isSunny = false;
        updateState();
        return this;
    }
    private Behavior<BlindsCommand> onMoviePlaying(MovieStartedPlaying msg) {
        this.moviePlaying = true;
        updateState();
        return this;
    }
    private Behavior<BlindsCommand> onMovieStopped(MovieStoppedPlaying msg) {
        this.moviePlaying = false;
        updateState();
        return this;
    }

    @Override
    public Receive<BlindsCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(MovieStoppedPlaying.class, this::onMovieStopped)
                .onMessage(MovieStartedPlaying.class, this::onMoviePlaying)
                .onMessage(WeatherTurnedSunny.class, this::onWeatherSunny)
                .onMessage(SunIsGone.class, this::onWeatherNotSunny)
                .build();
    }
}
