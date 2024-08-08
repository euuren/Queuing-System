import java.util.Comparator;
import java.lang.Math;

class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event event1, Event event2) {
        double timeDifference = event1.timeDifference(event2);
        int customerIdDifference = event1.customerIdDifference(event2);
        if (timeDifference > 0) {
            return 1;
        } else if (timeDifference < 0) {
            return -1;
        } else if (timeDifference == 0) {
            if (customerIdDifference > 0) {
                return 1;
            } else if (customerIdDifference < 0) {
                return -1;
            } else {
                if (event1.output().contains("arrives")) {
                    return -1;
                }
            }
        }
        return 1;
    }
}
