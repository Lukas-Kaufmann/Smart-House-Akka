package at.fhv.sysarch.lab2.homeautomation.simulator;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.*;
import at.fhv.sysarch.lab2.homeautomation.devices.AirCondition;

import java.time.Duration;
import java.util.Random;

public class TemperatureSimulator extends AbstractBehavior<TemperatureSimulator.TempSimCommand> {

    public static ActorRef<TempSimCommand> instanceRef;

    public interface TempSimCommand {}

    public static final class SetTemperatureRandomly implements TempSimCommand {}

    //TODO intermediate tempSensor

    public static final class SetTemperatureTo implements TempSimCommand {
        Double value;

        public SetTemperatureTo(Double value) {
            this.value = value;
        }
    }

    public static final class GetTemperature implements TempSimCommand {
        ActorRef<AirCondition.EnrichedTemperature> recipient;

        public GetTemperature(ActorRef<AirCondition.EnrichedTemperature> recipient) {
            this.recipient = recipient;
        }
    }

    public static final class RandomTemperatureStep implements TempSimCommand {}

    public static Behavior<TempSimCommand> create(String groupId, String deviceId) {
        return Behaviors.setup(context -> Behaviors.withTimers(timer -> new TemperatureSimulator(context, groupId, deviceId, timer)));
    }

    private static final float MAX_TEMP = 30f;
    private static final float MIN_TEMP = -10f;

    private final String groupId;
    private final String deviceId;
    private double temperature;
    private String unit;
    private TimerScheduler<TempSimCommand> timer;

    public TemperatureSimulator(ActorContext<TempSimCommand> context, String groupId, String deviceId, TimerScheduler<TempSimCommand> timer) {
        super(context);
        this.groupId = groupId;
        this.deviceId = deviceId;
        this.temperature = 15f;
        this.unit = "Celsius";
        this.timer = timer;
        this.timer.startTimerWithFixedDelay(new RandomTemperatureStep(), Duration.ofSeconds(3));
        instanceRef = getContext().getSelf();
    }

    @Override
    public Receive<TempSimCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(SetTemperatureTo.class, this::onTemperatureSet)
                .onMessage(GetTemperature.class, this::onGetTemperature)
                .onMessage(SetTemperatureRandomly.class, this::onRandomTemperature)
                .onMessage(RandomTemperatureStep.class, this::onRandomStep)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<TempSimCommand> onRandomStep(RandomTemperatureStep msg) {
        double randomStep = new Random().nextDouble() * 6 - 3;
        double newTemp = this.temperature + randomStep;
        newTemp = Math.max(newTemp, MIN_TEMP);
        newTemp = Math.min(newTemp, MAX_TEMP);
        this.temperature = newTemp;
        getContext().getLog().info("Randomly set temperature to {}", this.temperature);
        return Behaviors.same();
    }

    private Behavior<TempSimCommand> onRandomTemperature(SetTemperatureRandomly msg) {
        this.timer.startTimerWithFixedDelay(new RandomTemperatureStep(), Duration.ofSeconds(5));
        getContext().getLog().info("Setting temperature now randomly");
        return Behaviors.same();
    }

    private Behavior<TempSimCommand> onGetTemperature(GetTemperature msg) {
        msg.recipient.tell(new AirCondition.EnrichedTemperature(this.temperature, this.unit));
        getContext().getLog().info("TemperatureSimulator responded with temperature {}", this.temperature);
        return Behaviors.same();
    }

    private Behavior<TempSimCommand> onTemperatureSet(SetTemperatureTo msg) {
        this.timer.cancelAll();
        this.temperature = msg.value;
        getContext().getLog().info("TemperatureSimulator set temperature to {}", this.temperature);
        return Behaviors.same();
    }

    private TemperatureSimulator onPostStop() {
        getContext().getLog().info("TemperatureSimulator actor {}-{} stopped", groupId, deviceId);
        return this;
    }
}
