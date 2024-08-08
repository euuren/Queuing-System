import java.util.Iterator;
import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    private final int qmax;
    private final int numOfSelfChecks;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTimes;
    private final Supplier<Double> restTimes;

    Simulator(int numOfServers, int numOfSelfChecks, int qmax, 
        ImList<Double> arrivalTimes, Supplier<Double> serviceTimes,
        Supplier<Double> restTimes) {
        this.numOfServers = numOfServers;
        this.qmax = qmax;
        this.numOfSelfChecks = numOfSelfChecks;
        this.arrivalTimes = arrivalTimes;
        this.serviceTimes = serviceTimes;
        this.restTimes = restTimes;
    }
    
    public String simulate() {
        ImList<Server> serverList = createServerList(this.numOfServers, this.numOfSelfChecks);
        String output = processCustomers(serverList);
        return output;
    }

    private ImList<Server> createServerList(int numOfServers, int numOfSelfChecks) {
        ImList<Server> serverList = new ImList<Server>();
        if (numOfSelfChecks > 0) {
            for (int i = 1; i <= numOfServers + 1; i++) {
                serverList = serverList.add(new Server(i, 0.0, 0, qmax));
            }
            for (int i = numOfServers + 2; i <= numOfSelfChecks + numOfServers; i++) {
                serverList = serverList.add(new Server(i, 0.0, 0, 0));
            }
        } else {
            for (int i = 1; i <= numOfServers; i++) {
                serverList = serverList.add(new Server(i, 0.0, 0, qmax));
            }
        }
        return serverList;
    }

    String processCustomers(ImList<Server> serverList) {
        int numServed = 0;
        int numLeft = 0;
        double totalTimeWaited = 0;
        double avgTimeWaited = 0;
        String output = ""; 
        EventComparator comparator = new EventComparator();
        PQ<Event> priorityQueue = new PQ<Event>(comparator);

        for (int i = 0; i < arrivalTimes.size(); i++) {
            double arrivalTime = arrivalTimes.get(i);
            Arrive arrive = new Arrive(i + 1, arrivalTime, serviceTimes, 
                                        restTimes, this.numOfServers);
            priorityQueue = priorityQueue.add(arrive);
        }

        while (!priorityQueue.isEmpty()) {
            Pair<Event, PQ<Event>> pair = priorityQueue.poll();
            Event currentEvent = pair.first();
            if (!currentEvent.output().contains("rewait")
                && !currentEvent.output().contains("rest")) {
                output = output + currentEvent.output();
            }
            priorityQueue = pair.second();
            if (currentEvent.output().contains("serves")) {
                numServed++;
            }
            if (currentEvent.output().contains("leaves")) {
                numLeft++;
            }
            Pair<Event, ImList<Server>> nextEvent = currentEvent.nextEvent(serverList);
            if (currentEvent.output().contains("waits") ||
                currentEvent.output().contains("rewait")) {
                totalTimeWaited += (nextEvent.first()).timeDifference(currentEvent);
            }
            serverList = nextEvent.second();
            if (nextEvent.first() != currentEvent) {
                priorityQueue = priorityQueue.add(nextEvent.first());
            }
        }
        if (totalTimeWaited == 0) {
            avgTimeWaited = 0;
        } else {
            avgTimeWaited = totalTimeWaited / numServed;
        }
        output = output + "[" + 
            String.format("%.3f", avgTimeWaited) + 
                " " + numServed + " " + numLeft + "]";
        return output;
    }
}
