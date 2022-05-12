package at.fhv.sysarch.lab2.homeautomation.devices.model;

public class ProductAmount {
    private Product product;
    private int amount;

    public ProductAmount(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public boolean consume(int consumeAmount) {
        if (consumeAmount > amount) {
            return false;
        }

        this.amount -= consumeAmount;
        return true;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}
