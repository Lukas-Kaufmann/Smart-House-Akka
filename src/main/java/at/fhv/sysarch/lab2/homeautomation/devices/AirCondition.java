package at.fhv.sysarch.lab2.homeautomation.devices;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.*;
import at.fhv.sysarch.lab2.homeautomation.simulator.TemperatureSimulator;

/**
 * This class shows ONE way to switch behaviors in object-oriented style. Another approach is the use of static
 * methods for each behavior.
 *
 * The switching of behaviors is not strictly necessary for this example, but is rather used for demonstration
 * purpose only.
 *
 * For an example with functional-style please refer to: {@link https://doc.akka.io/docs/akka/current/typed/style-guide.html#functional-versus-object-oriented-style}
 *
 */
import java.time.Duration;
import java.util.Optional;

public class AirCondition extends AbstractBehavior<AirCondition.AirConditionCommand> {
    public static ActorRef<AirConditionCommand> instanceRef;

    public interface AirConditionCommand {}

    public static final class PowerAirCondition implements AirConditionCommand {
        final Boolean value;

        public PowerAirCondition(Boolean value) {
            this.value = value;
        }
    }

    public static final class CheckTemperature implements AirConditionCommand {}

    public static final class EnrichedTemperature implements AirConditionCommand {
        Double value;
        String unit;

        public EnrichedTemperature(Double value, String unit) {
            this.value = value;
            this.unit = unit;
        }
    }

    private final String groupId;
    private final String deviceId;
    private boolean active = false;
    private boolean poweredOn = true;
    private ActorRef<TemperatureSimulator.TempSimCommand> sensor;
    private TimerScheduler<AirConditionCommand> timer;

    public AirCondition(ActorContext<AirConditionCommand> context, String groupId, String deviceId, ActorRef<TemperatureSimulator.TempSimCommand> tempSensor, TimerScheduler<AirConditionCommand> timer) {
        super(context);
        this.groupId = groupId;
        this.deviceId = deviceId;
        this.sensor = tempSensor;
        this.timer = timer;
        timer.startTimerWithFixedDelay(new CheckTemperature(), Duration.ofSeconds(15));
        getContext().getLog().info("AirCondition started");
        instanceRef = getContext().getSelf();
    }

    public static Behavior<AirConditionCommand> create(String groupId, String deviceId, ActorRef<TemperatureSimulator.TempSimCommand> tempSensor) {
        return Behaviors.setup(context -> Behaviors.withTimers(timer -> new AirCondition(context, groupId, deviceId, tempSensor, timer)));
    }

    @Override
    public Receive<AirConditionCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(EnrichedTemperature.class, this::onReceiveTemperature)
                .onMessage(PowerAirCondition.class, this::onPowerAirConditionOff)
                .onMessage(CheckTemperature.class, this::checkTemperature)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<AirConditionCommand> checkTemperature(CheckTemperature msg) {
        this.sensor.tell(new TemperatureSimulator.GetTemperature(getContext().getSelf().narrow()));

        return Behaviors.same();
    }

    private Behavior<AirConditionCommand> onReceiveTemperature(EnrichedTemperature r) {
        getContext().getLog().info("Aircondition reading {}", r.value);
        if(r.value >= 20 && !this.active) {
            getContext().getLog().info("Aircondition actived");
            this.active = true;
        }
        else if (!this.active) {
            getContext().getLog().info("Aircondition deactived");
            this.active =  false;
        }

        return Behaviors.same();
    }

    private Behavior<AirConditionCommand> onPowerAirConditionOff(PowerAirCondition r) {
        this.timer.cancelAll();
        if(r.value == false) {
            getContext().getLog().info("Turning Aircondition off");
            return this.powerOff();
        }
        return this;
    }

    private Behavior<AirConditionCommand> onPowerAirConditionOn(PowerAirCondition r) {
        if(r.value == true) {
            this.timer.startTimerWithFixedDelay(new CheckTemperature(), Duration.ofSeconds(15));
            getContext().getLog().info("Turning Aircondition on");
            return Behaviors.receive(AirConditionCommand.class)
                    .onMessage(EnrichedTemperature.class, this::onReceiveTemperature)
                    .onMessage(PowerAirCondition.class, this::onPowerAirConditionOff)
                    .onMessage(CheckTemperature.class, this::checkTemperature)
                    .onSignal(PostStop.class, signal -> onPostStop())
                    .build();
        }
        return this;
    }

    private Behavior<AirConditionCommand> powerOff() {
        this.poweredOn = false;
        return Behaviors.receive(AirConditionCommand.class)
                .onMessage(PowerAirCondition.class, this::onPowerAirConditionOn)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private AirCondition onPostStop() {
        getContext().getLog().info("TemperatureSensor actor {}-{} stopped", groupId, deviceId);
        return this;
    }
}
