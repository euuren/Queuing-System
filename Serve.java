import java.util.function.Supplier;

class Serve extends Event {
    private final int serverId;
    private final Supplier<Double> serviceTime;
    private final Supplier<Double> restTime;
    private final int numOfServers;

    Serve(int customerId, double time, int serverId, Supplier<Double> serviceTime,
        Supplier<Double> restTime, int numOfServers) {
        super(customerId, time);
        this.serverId = serverId;
        this.serviceTime = serviceTime;
        this.restTime = restTime;
        this.numOfServers = numOfServers;
    }

    public String output() {
        if (serverId <= numOfServers) {
            return String.format("%.3f %s serves by %s\n", 
                super.time, super.customerId, this.serverId);
        } else {
            return String.format("%.3f %s serves by self-check %s\n", 
                super.time, super.customerId, this.serverId);
        }
    }

    public Double getServiceTime() {
        return this.serviceTime.get();
    }

    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> servers) {
        double currentServiceTime = this.getServiceTime();
        Server server = servers.get(serverId - 1);
        if (serverId <= numOfServers + 1 || numOfServers == servers.size()) {
            if (server.getNumInQueue() == 0) {
                Server updatedServer = new Server(serverId, 
                    super.time + currentServiceTime, 0, server.getQMax());
                servers = servers.set(serverId - 1, updatedServer);
            } else {
                Server updatedServer = new Server(serverId, 
                    super.time + currentServiceTime, server.getNumInQueue() - 1, server.getQMax());
                servers = servers.set(serverId - 1, updatedServer);
            }
        } else {
            if (servers.get(numOfServers).getNumInQueue() != 0) {
                Server updatedQueue = new Server(numOfServers + 1, 
                    servers.get(numOfServers).getNextAvailableTime(),
                    servers.get(numOfServers).getNumInQueue() - 1,
                    servers.get(numOfServers).getQMax());
                servers = servers.set(numOfServers, updatedQueue);
            }
            Server updatedServer = new Server(serverId, 
                super.time + currentServiceTime, 0, 0);
            servers = servers.set(serverId - 1, updatedServer);
        }
        return new Pair<>(new Done(super.customerId, super.time + 
            currentServiceTime, this.serverId, this.restTime, this.numOfServers), servers);
    }
}