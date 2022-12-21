package Day16;

import java.util.List;

public class Valve {
    private final int rate;
    private final List<String> leadsTo;

    public int getRate() {
        return rate;
    }

    public List<String> getLeadsTo() {
        return leadsTo;
    }

    public Valve(int pressureRate, List<String> leadsToValves) {
        rate = pressureRate;
        leadsTo = leadsToValves;
    }
}
