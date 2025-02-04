package starter;

public class Calculator {
    private int total;

    public Calculator(int amount) {
        this.total = amount;
    }

    public void add(int amount) {
        total = total + amount;
    }

    public int getTotal() {
        return total;
    }
}
