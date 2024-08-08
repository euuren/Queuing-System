class Customer {
    private final int customerId;
    private final double arrivalTime;

    Customer(int customerId, double arrivalTime) {
        this.customerId = customerId;
        this.arrivalTime = arrivalTime;
    }
 
    public int getCustomerId() {
        return this.customerId;
    }
 
    public double getArrivalTime() {
        return this.arrivalTime;
    }
}