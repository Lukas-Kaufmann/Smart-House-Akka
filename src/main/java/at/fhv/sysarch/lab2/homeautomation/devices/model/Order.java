package at.fhv.sysarch.lab2.homeautomation.devices.model;

public class Order {
    private String productName;
    private int amount;

    public Order(String productName, int amount) {
        this.productName = productName;
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public int getAmount() {
        return amount;
    }
}
