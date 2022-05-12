package at.fhv.sysarch.lab2.homeautomation.devices.fridge;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import at.fhv.sysarch.lab2.homeautomation.devices.Fridge;
import at.fhv.sysarch.lab2.homeautomation.devices.model.Order;
import at.fhv.sysarch.lab2.homeautomation.devices.model.Product;
import at.fhv.sysarch.lab2.homeautomation.devices.model.ProductAmount;

import java.util.List;

public class OrderProcessor extends AbstractBehavior<OrderProcessor.Command> {
    public interface Command {}

    public static class ProcessOrder implements Command {
        Order order;
        public ProcessOrder(Order order) {
            this.order = order;
        }
    }

    public static Behavior<Command> create(ActorRef<Fridge.Command> fridge, double maxWeight, int maxAmount, List<ProductAmount> storage) {
        return Behaviors.setup(context -> new OrderProcessor(context, fridge, maxWeight, maxAmount, storage));
    }
    private double maxWeight;
    private int maxAmount;
    private List<ProductAmount> storage;
    private ActorRef<Fridge.Command> fridge;

    public OrderProcessor(ActorContext<Command> context, ActorRef<Fridge.Command> fridge, double maxWeight, int maxAmount, List<ProductAmount> storage) {
        super(context);
        getContext().getLog().info("OrderProcessor created");
        this.maxAmount = maxAmount;
        this.maxWeight = maxWeight;
        this.storage = storage;
        this.fridge = fridge;
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ProcessOrder.class, this::onProcess)
                .onSignal(PostStop.class, this::onPostStop)
                .build();
    }

    private Behavior<Command> onProcess(ProcessOrder msg) {
        getContext().getLog().info("Processor handling order for " + msg.order.getProduct().getName());
        //TODO
        int numberOfItemsInStorage = this.storage.stream().mapToInt(item -> item.getAmount()).sum();
        double totalWeight = this.storage.stream().mapToDouble(item -> item.getAmount() * item.getProduct().getWeight()).sum();

        if (numberOfItemsInStorage + msg.order.getAmount() < this.maxAmount && totalWeight + (msg.order.getAmount() * msg.order.getProduct().getWeight()) < this.maxWeight) {
            getContext().getLog().info("Order is Ok, can be added to the fridge");
            this.fridge.tell(new Fridge.AddProduct(new ProductAmount(msg.order.getProduct(), msg.order.getAmount())));
        } else {
            getContext().getLog().info("Cant Process order Fridge too full");
        }

        return Behaviors.stopped();
    }

    private Behavior<Command> onPostStop(PostStop signal) {
        getContext().getLog().info("OrderProcessor stopped");
        return this;
    }
}
