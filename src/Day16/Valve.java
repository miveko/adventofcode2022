package Day16;

import java.util.List;

public class Valve {
    private int rate;
    private List<String> leadsTo;
    boolean released;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public List<String> getLeadsTo() {
        return leadsTo;
    }

    public void setLeadsTo(List<String> leadsTo) {
        this.leadsTo = leadsTo;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public Valve(int pressureRate, List<String> leadsToValves) {
        rate = pressureRate;
        leadsTo = leadsToValves;
        released = false;
    }
}
