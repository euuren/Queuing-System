import java.util.function.Supplier;

class Arrive extends Event {
    private final Supplier<Double> serviceTime;
    private final Supplier<Double> restTime;
    private final int numOfServers;
    private static final int NO_AVAILABLE_SERVERS = -1;

    Arrive(int customerId, double time, Supplier<Double> serviceTime,
        Supplier<Double> restTime, int numOfServers) {
        super(customerId, time);
        this.serviceTime = serviceTime;
        this.restTime = restTime;
        this.numOfServers = numOfServers;
    }

    public String output() {
        return String.format("%.3f %s arrives\n", 
            super.time, super.customerId);
    }

    private int findAvailableServer(double time, ImList<Server> serverList) {
        for (Server server : serverList) {
            if (server.canServe(time)) {
                return server.getServerId();
            }
        }
        return NO_AVAILABLE_SERVERS;
    }

    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> servers) {
        int serverId = findAvailableServer(super.time, servers);
        if (serverId != NO_AVAILABLE_SERVERS) {
            return new Pair<>(new Serve(super.customerId, super.time, 
                serverId, this.serviceTime, this.restTime, this.numOfServers), servers);
        } else {
            for (Server server : servers) {
                int queueNumber = server.canQueue();
                if (queueNumber != NO_AVAILABLE_SERVERS) {
                    Server updatedServer = new Server(server.getServerId(),
                        server.getNextAvailableTime(), server.getNumInQueue() + 1, 
                        server.getQMax());
                    servers = servers.set(server.getServerId() - 1, updatedServer);
                    return new Pair<>(new Wait(super.customerId, super.time,
                    server.getServerId(), this.serviceTime, queueNumber, true,
                        this.restTime, this.numOfServers), servers);
                }
            }
            return new Pair<>(new Leave(super.customerId, super.time), servers);
        }
    }
}