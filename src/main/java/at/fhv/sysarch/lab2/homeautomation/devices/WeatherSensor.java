package at.fhv.sysarch.lab2.homeautomation.devices;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import at.fhv.sysarch.lab2.homeautomation.simulator.WeatherSimulator;

import java.time.Duration;

public class WeatherSensor extends AbstractBehavior<WeatherSensor.Command> {
    public interface Command {}

    public static class TimedPing implements Command {}

    public static class ReceiveWeather implements Command {
        WeatherSimulator.WEATHER value;
        public ReceiveWeather(WeatherSimulator.WEATHER value) {
            this.value = value;
        }
    }

    public static Behavior<Command> create(ActorRef<WeatherSimulator.WeatherSimCommand> weatherSim, ActorRef<Blinds.BlindsCommand> blinds) {
        return Behaviors.setup(context -> Behaviors.withTimers(timer -> new WeatherSensor(context, timer, weatherSim, blinds)));
    }

    private TimerScheduler<Command> timer;
    private ActorRef<WeatherSimulator.WeatherSimCommand> weatherSim;
    private ActorRef<Blinds.BlindsCommand> blinds;
    private WeatherSimulator.WEATHER lastWeatherValue;

    public WeatherSensor(ActorContext<Command> context, TimerScheduler<Command> timer, ActorRef<WeatherSimulator.WeatherSimCommand> weatherSim, ActorRef<Blinds.BlindsCommand> blinds) {
        super(context);
        this.weatherSim = weatherSim;
        this.blinds = blinds;
        this.timer = timer;
        timer.startTimerWithFixedDelay(new TimedPing(), Duration.ofSeconds(10));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ReceiveWeather.class, this::receiveWeather)
                .onMessage(TimedPing.class, this::onPing)
                .build();
    }

    private Behavior<Command> receiveWeather(ReceiveWeather msg) {
        getContext().getLog().info("WeatherSensor reading {}", msg.value);

        if (this.lastWeatherValue != msg.value) {
            this.lastWeatherValue = msg.value;

            if (this.lastWeatherValue == WeatherSimulator.WEATHER.SUNNY) {
                blinds.tell(new Blinds.WeatherTurnedSunny());
            } else {
                blinds.tell(new Blinds.SunIsGone());
            }
        }
        return this;
    }

    private Behavior<Command> onPing(TimedPing msg) {
        this.weatherSim.tell(new WeatherSimulator.GetWeather(getContext().getSelf()));
        return this;
    }

}
