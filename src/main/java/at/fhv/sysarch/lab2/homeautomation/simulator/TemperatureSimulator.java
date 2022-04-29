package at.fhv.sysarch.lab2.homeautomation.simulator;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.*;
import at.fhv.sysarch.lab2.homeautomation.devices.TemperatureSensor;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

public class TemperatureSimulator extends AbstractBehavior<TemperatureSimulator.TempSimCommand> {
    public interface TempSimCommand {}

    public static final class SetTemperatureRandomly implements TempSimCommand {}

    public static final class SetTemperatureTo implements TempSimCommand {
        Optional<Double> value;

        public SetTemperatureTo(Optional<Double> value) {
            this.value = value;
        }
    }

    public static final class GetTemperature implements TempSimCommand {
        ActorRef<TemperatureSensor.ReadTemperature> recipient;

        public GetTemperature(ActorRef<TemperatureSensor.ReadTemperature> recipient) {
            this.recipient = recipient;
        }
    }

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
        
    }

    @Override
    public Receive<TempSimCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(SetTemperatureTo.class, this::onTemperatureSet)
                .onMessage(GetTemperature.class, this::onGetTemperature)
                .onMessage(SetTemperatureRandomly.class, this::onRandomTemperature)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<TempSimCommand> onRandomTemperature(SetTemperatureRandomly msg) {
        //TODO
        getContext().getLog().info("Setting temperature now randomly");
        return Behaviors.same();
    }

    private Behavior<TempSimCommand> onGetTemperature(GetTemperature msg) {
        msg.recipient.tell(new TemperatureSensor.ReadTemperature(Optional.of(this.temperature)));
        getContext().getLog().info("TemperatureSimulator responded with temperature {}", this.temperature);
        return Behaviors.same();
    }

    private Behavior<TempSimCommand> onTemperatureSet(SetTemperatureTo msg) {
        this.temperature = msg.value.get();
        getContext().getLog().info("TemperatureSimulator set temperature to {}", this.temperature);
        return Behaviors.same();
    }

    private TemperatureSimulator onPostStop() {
        getContext().getLog().info("TemperatureSimulator actor {}-{} stopped", groupId, deviceId);
        return this;
    }
}
