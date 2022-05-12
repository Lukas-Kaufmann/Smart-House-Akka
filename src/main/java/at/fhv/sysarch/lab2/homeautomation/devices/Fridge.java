package at.fhv.sysarch.lab2.homeautomation.devices;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import at.fhv.sysarch.lab2.homeautomation.devices.fridge.OrderProcessor;
import at.fhv.sysarch.lab2.homeautomation.devices.model.Order;
import at.fhv.sysarch.lab2.homeautomation.devices.model.Product;
import at.fhv.sysarch.lab2.homeautomation.devices.model.ProductAmount;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Fridge extends AbstractBehavior<Fridge.Command> {
    public static ActorRef<Command> instanceRef;
    public interface Command {}

    public static class GetProducts implements Command {}
    public static class GetOrders implements Command { }
    public static class AddProduct implements Command {
        ProductAmount productAmount;
        public AddProduct(ProductAmount productAmount) {
            this.productAmount = productAmount;
        }
    }
    public static class ConsumeProduct implements Command {
        int amount;
        String name;
        public ConsumeProduct(String name, int amount) {
            this.amount = amount;
            this.name = name;
        }
    }
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
    private int maxAmount = 50;
    private List<ProductAmount> storage;
    private List<Order> pastOrders;
    private Optional<Behavior<OrderProcessor.Command>>  processorOpt = Optional.empty();

    public static Behavior<Command> create() {
        return Behaviors.setup(Fridge::new);
    }
    public Fridge(ActorContext<Command> context) {
        super(context);
        this.storage = new LinkedList<>();
        this.pastOrders = new LinkedList<>();
        //TODO remove
        this.storage.add(new ProductAmount(new Product("Cheese", 14), 3));
        instanceRef = getContext().getSelf();
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetProducts.class, this::onGetProducts)
                .onMessage(OrderProduct.class, this::onPlaceOrder)
                .onMessage(GetOrders.class, this::onGetOrders)
                .onMessage(AddProduct.class, this::onAddProduct)
                .onMessage(ConsumeProduct.class, this::onConsume)
                .build();
    }

    private Behavior<Command> onAddProduct(AddProduct msg) {
        this.storage.removeIf(item -> item.getAmount()==0);
        getContext().getLog().info("Fridge adding Product " + msg.productAmount.getProduct().getName());
        this.storage.add(msg.productAmount);
        return this;
    }

    private Behavior<Command> onConsume(ConsumeProduct msg) {
        Optional<ProductAmount> itemOpt = this.storage.stream().filter(item -> item.getProduct().getName().equals(msg.name)).findFirst();
        if (itemOpt.isPresent()) {
            if (itemOpt.get().consume(msg.amount)) {
                getContext().getLog().info("Consuming " + msg.amount + " of " + msg.name);
                if (itemOpt.get().getAmount()==0) {
                    getContext().getSelf().tell(new OrderProduct(msg.name, 10f, 2));
                    getContext().getLog().info("Reordering " + msg.name);
                }
            } else {
                getContext().getLog().info("Unable to consume " + msg.amount + " only " + itemOpt.get().getAmount() + " are in storage") ;
            }

        } else {
            getContext().getLog().info("Unable to consume " + msg.name + " since it's not in storage");
        }
        return this;
    }

    private Behavior<Command> onPlaceOrder(OrderProduct msg) {
        //TODO spawn child process
        ActorRef<OrderProcessor.Command> processor = getContext().spawn(OrderProcessor.create(
                getContext().getSelf(),
                this.maxWeight,
                this.maxAmount,
                Collections.unmodifiableList(this.storage)
        ), "processor");
        Product product = new Product(msg.name, msg.weight);
        Order order = new Order(product, msg.amount);
        processor.tell(new OrderProcessor.ProcessOrder(order));
        this.pastOrders.add(order);
        return this;
    }

    private Behavior<Command> onGetOrders(GetOrders msg) {
        String logString = "Orders:\n" + pastOrders.stream().map(order -> order.getAmount() + " of " + order.getProduct().getName()).collect(Collectors.joining("\n"));
        getContext().getLog().info(logString);
        return this;
    }

    public Behavior<Command> onGetProducts(GetProducts msg) {
        String logsString = "Stored products:\n" + storage.stream().map(item -> item.getAmount() + " pieces of " + item.getProduct().getName()).collect(Collectors.joining("\n"));
        getContext().getLog().info(logsString);
        return this;
    }
}
