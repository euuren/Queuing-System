import java.util.Iterator;

class Server {
    private final double nextAvailableTime;
    private final int serverId;
    private final int numInQueue;
    private final int qmax;

    Server(int serverId, double nextAvailableTime, int numInQueue, int qmax) {
        this.serverId = serverId;
        this.nextAvailableTime = nextAvailableTime;
        this.numInQueue = numInQueue;
        this.qmax = qmax;
    }

    public boolean canServe(double customerArrivalTime) {
        return customerArrivalTime >= this.nextAvailableTime;
    }
    
    public int canQueue() {
        if (numInQueue < qmax) {
            return numInQueue + 1;
        } else {
            return -1;
        }
    }

    public int getServerId() {
        return this.serverId;
    }

    public int getQMax() {
        return this.qmax;
    }

    public int getNumInQueue() {
        return this.numInQueue;
    }

    public double getNextAvailableTime() {
        return this.nextAvailableTime;
    }
}
