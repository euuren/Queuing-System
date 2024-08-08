class Leave extends Event {
    Leave(int customerId, double time) {
        super(customerId, time);
    }

    public String output() {
        return String.format("%.3f %s leaves\n", 
            super.time, this.customerId);
    }

    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> servers) {
        return new Pair<>(this, servers);
    }
}