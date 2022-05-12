package at.fhv.sysarch.lab2.homeautomation.devices;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import at.fhv.sysarch.lab2.homeautomation.devices.model.Order;
import at.fhv.sysarch.lab2.homeautomation.devices.model.Product;
import at.fhv.sysarch.lab2.homeautomation.devices.model.ProductAmount;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Fridge extends AbstractBehavior<Fridge.Command> {
    public static ActorRef<Command> instanceRef;
    public interface Command {}

    public static class GetProducts implements Command {}
    public static class GetOrders implements Command { }
    public static class OrderProduct implements Command {
        String name;
        double weight;
        int amount;

        public OrderProduct(String name, double weight, int amount) {
            this.name = name;
            this.weight = weight;
            this.amount = amount;
        }
    }

    private double maxWeight = 200;
    private double maxAmount = 50;
    private List<ProductAmount> storage;
    private List<Order> pastOrders;

    public static Behavior<Command> create() {
        return Behaviors.setup(Fridge::new);
    }
    public Fridge(ActorContext<Command> context) {
        super(context);
        this.storage = new LinkedList<>();
        //TODO remove
        this.storage.add(new ProductAmount(new Product("Cheese", 14), 3));
        instanceRef = getContext().getSelf();
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetProducts.class, this::onGetProducts)
                .build();
    }

    private Behavior<Command> onPlaceOrder(OrderProduct msg) {
        //TODO spawn child process
        return this;
    }

    private Behavior<Command> onGetOrders(GetOrders msg) {
        //TODO
        return this;
    }

    public Behavior<Command> onGetProducts(GetProducts msg) {
        String logsString = "Stored products:\n" + storage.stream().map(item -> item.getAmount() + " pieces of " + item.getProduct().getName()).collect(Collectors.joining("\n"));
        getContext().getLog().info(logsString);
        return this;
    }
}
