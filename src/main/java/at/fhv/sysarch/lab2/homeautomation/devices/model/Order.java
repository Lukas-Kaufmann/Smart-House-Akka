package at.fhv.sysarch.lab2.homeautomation.devices.model;

public class Order {
    private Product product;
    private int amount;

    public Order(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}
