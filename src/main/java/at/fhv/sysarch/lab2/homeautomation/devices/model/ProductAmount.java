package at.fhv.sysarch.lab2.homeautomation.devices.model;

public class ProductAmount {
    private Product product;
    private int amount;

    public ProductAmount(Product product, int amount) {
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
