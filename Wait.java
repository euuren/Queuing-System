import java.util.function.Supplier;

class Wait extends Event {
    private final int serverId;
    private final Supplier<Double> serviceTime;
    private final Supplier<Double> restTime;
    private final int queueNumber;
    private final boolean isOriginalWait;
    private final int numOfServers;

    Wait(int customerId, double time, int serverId, 
        Supplier<Double> serviceTime, int queueNumber, 
        boolean isOriginalWait, Supplier<Double> restTime,
        int numOfServers) {
        super(customerId, time);
        this.serverId = serverId;
        this.serviceTime = serviceTime;
        this.queueNumber = queueNumber;
        this.isOriginalWait = isOriginalWait;
        this.restTime = restTime;
        this.numOfServers = numOfServers;
    }

    public String output() {
        if (this.isOriginalWait) {
            if (serverId <= numOfServers) {
                return String.format("%.3f %s waits at %s\n", 
                    super.time, super.customerId, this.serverId);
            } else {
                return String.format("%.3f %s waits at self-check " + (numOfServers + 1) + "\n", 
                    super.time, super.customerId);
            }
        }
        return "rewait";
    }

    public Server getFirstAvailableSelfCheck(ImList<Server> servers) {
        Server firstAvailableSelfCheck = servers.get(0);
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).getNextAvailableTime() < 
                firstAvailableSelfCheck.getNextAvailableTime()) {
                firstAvailableSelfCheck = servers.get(i);
            }
        }
        return firstAvailableSelfCheck;
    }

    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> servers) {
        // if human server
        if (serverId <= numOfServers || numOfServers == servers.size()) {
            Server server = servers.get(this.serverId - 1);
            if (super.time >= server.getNextAvailableTime()) {
                if (this.queueNumber == 1) {
                    return new Pair<>(new Serve(super.customerId, server.getNextAvailableTime(), 
                    serverId, this.serviceTime, this.restTime, this.numOfServers), servers);
                }
                return new Pair<>(new Wait(super.customerId, server.getNextAvailableTime(),
                this.serverId, this.serviceTime, queueNumber - 1, false,
                this.restTime, this.numOfServers), servers);
            } else {
                return new Pair<>(new Wait(super.customerId, server.getNextAvailableTime(),
                this.serverId, this.serviceTime, queueNumber, false,
                this.restTime, this.numOfServers), servers);
            }
        } else { // if self checkout
            ImList<Server> selfCheckList = new ImList<Server>();
            for (int i = numOfServers; i < servers.size(); i++) {
                selfCheckList = selfCheckList.add(servers.get(i));
            }
            Server selfCheck = getFirstAvailableSelfCheck(selfCheckList);
            if (this.queueNumber == 1) {
                return new Pair<>(new Serve(super.customerId, 
                selfCheck.getNextAvailableTime(), selfCheck.getServerId(), this.serviceTime, 
                this.restTime, this.numOfServers), servers);
            } else {
                return new Pair<>(new Wait(super.customerId, selfCheck.getNextAvailableTime(),
                this.serverId, this.serviceTime, queueNumber - 1, false,
                this.restTime, this.numOfServers), servers);
            }
        }
    }
}