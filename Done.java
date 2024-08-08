import java.util.function.Supplier;

class Done extends Event {
    private final int serverId;
    private final Supplier<Double> restTime;
    private final int numOfServers;

    Done(int customerId, double time, int serverId, Supplier<Double> restTime, int numOfServers) {
        super(customerId, time);
        this.serverId = serverId;
        this.restTime = restTime;
        this.numOfServers = numOfServers;
    }

    public Double getRestTime() {
        return this.restTime.get();
    }

    public String output() {
        if (serverId <= numOfServers) { 
            return String.format("%.3f %s done serving by %s\n", 
                super.time, this.customerId, this.serverId);
        } else {
            return String.format("%.3f %s done serving by self-check %s\n", 
                super.time, this.customerId, this.serverId);
        }
    }

    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> servers) {
        Server server = servers.get(this.serverId - 1);
        if (serverId <= numOfServers) {
            Server updatedServer = new Server(serverId, 
                super.time + this.getRestTime(), server.getNumInQueue(), server.getQMax());
            servers = servers.set(serverId - 1, updatedServer);
            return new Pair<>(this, servers);
        } else {
            return new Pair<>(this, servers);
        }
    }
}