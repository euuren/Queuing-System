abstract class Event {
    protected final double time;
    protected final int customerId;

    Event(int customerId, double time) {
        this.time = time;
        this.customerId = customerId;
    }

    public abstract String output();

    public abstract Pair<Event, ImList<Server>> nextEvent(ImList<Server> servers);

    public double timeDifference(Event anotherEvent) {
        return this.time - anotherEvent.time;
    }

    public int customerIdDifference(Event anotherEvent) {
        return this.customerId - anotherEvent.customerId;
    }
}