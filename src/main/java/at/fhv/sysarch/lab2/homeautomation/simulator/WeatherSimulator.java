package at.fhv.sysarch.lab2.homeautomation.simulator;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import at.fhv.sysarch.lab2.homeautomation.devices.WeatherSensor;

import java.time.Duration;
import java.util.Random;

public class WeatherSimulator extends AbstractBehavior<WeatherSimulator.WeatherSimCommand> {
    public static ActorRef<WeatherSimCommand> instanceRef;

    public enum WEATHER {
        SUNNY, CLOUDY, RAIN;

        public static WEATHER random() {
            Random r = new Random();
            WEATHER[] values = values();
            return values[r.nextInt(values.length)];
        }
    }

    public interface WeatherSimCommand {}

    public static class RandomWeather implements WeatherSimCommand {}

    public static class RandomWeatherStep implements WeatherSimCommand {}

    public static class SetWeather implements WeatherSimCommand {
        WEATHER value;

        public SetWeather(WEATHER value) {
            this.value = value;
        }
    }

    public static class GetWeather implements WeatherSimCommand {
        final ActorRef<WeatherSensor.Command> sender;
        public GetWeather(ActorRef<WeatherSensor.Command> sender) {
            this.sender = sender;
        }
    }

    private WEATHER currentWeather;
    private TimerScheduler<WeatherSimCommand> timer;

    public static Behavior<WeatherSimCommand> create() {
        return Behaviors.setup(context -> Behaviors.withTimers(timer -> new WeatherSimulator(context, timer)));
    }

    public WeatherSimulator(ActorContext context, TimerScheduler<WeatherSimCommand> timer) {
        super(context);
        instanceRef = getContext().getSelf();
        this.currentWeather = WEATHER.CLOUDY;
        this.timer = timer;
        this.timer.startTimerWithFixedDelay(new RandomWeatherStep(), Duration.ofMinutes(1));
    }

    @Override
    public Receive<WeatherSimCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetWeather.class, this::onGetWeather)
                .onMessage(RandomWeather.class, (RandomWeather msg) -> setRandomWeather())
                .onMessage(SetWeather.class, this::onSetWeather)
                .onMessage(RandomWeatherStep.class, (RandomWeatherStep m) -> onRandomStep())
                .build();
    }

    private Behavior<WeatherSimCommand> onGetWeather(GetWeather msg) {
        getContext().getLog().info("WeatherSimulator sending {}", this.currentWeather);
        msg.sender.tell(new WeatherSensor.ReceiveWeather(this.currentWeather));
        return this;
    }

    private Behavior<WeatherSimCommand> onSetWeather(SetWeather msg) {
        this.timer.cancelAll();
        this.currentWeather = msg.value;
        getContext().getLog().info("Setting weather to " + this.currentWeather.toString());

        return this;
    }

    private Behavior<WeatherSimCommand> setRandomWeather() {
        this.timer.startTimerWithFixedDelay(new RandomWeatherStep(), Duration.ofMinutes(1));
        getContext().getLog().info("Now setting Weather randomly");
        return this;
    }

    private Behavior<WeatherSimCommand> onRandomStep() {
        WEATHER w = WEATHER.random();
        getContext().getLog().info("Randomly setting temperature to " + w.toString());
        this.currentWeather = w;
        return this;
    }

}
