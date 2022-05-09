package at.fhv.sysarch.lab2.homeautomation.devices;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.Signal;
import akka.actor.typed.javadsl.*;

public class MediaStation extends AbstractBehavior<MediaStation.MediaCommand> {
    public static ActorRef<MediaCommand> instanceRef;

    public interface MediaCommand {}

    public static class PlayMovie implements MediaCommand {
        public PlayMovie() {}
    }

    public static class StopMovie implements MediaCommand {
        public StopMovie() {}
    }

    private ActorRef<Blinds.BlindsCommand> blinds;
    private boolean playing = false;

    public MediaStation(ActorContext<MediaCommand> context, ActorRef<Blinds.BlindsCommand> blinds) {
        super(context);
        this.blinds = blinds;
        instanceRef = getContext().getSelf();
    }

    public static Behavior<MediaCommand> create(ActorRef<Blinds.BlindsCommand> blinds) {
        return Behaviors.setup(context -> new MediaStation(context, blinds));
    }

    @Override
    public Receive<MediaCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(PlayMovie.class, this::onPlayMovie)
                .onMessage(StopMovie.class, this::onStopMovie)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    public Behavior<MediaCommand> onPlayMovie(PlayMovie msg) {
        if (!this.playing) {
            getContext().getLog().info("Media Station started playing a movie");
            blinds.tell(new Blinds.MovieStartedPlaying());
            this.playing = true;
        } else {
            getContext().getLog().info("A Movie is already playing, unable to start a new one");
        }
        return this;
    }

    public Behavior<MediaCommand> onStopMovie(StopMovie msg) {
        if (this.playing) {
            getContext().getLog().info("Media Station stopped playing a movie");
            blinds.tell(new Blinds.MovieStoppedPlaying());
            this.playing = false;
        }
        return this;
    }

    public Behavior<MediaCommand> onPostStop() {
        getContext().getLog().info("Media station stopped");
        return this;
    }
}
