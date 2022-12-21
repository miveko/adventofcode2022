package Day16;

import java.util.List;

public class Path {
    private List<String> nodes;
    private int lostValue;
    private int ratesSum;
    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public int getLostValue() {
        return lostValue;
    }

    public void setLostValue(int lostValue) {
        this.lostValue = lostValue;
    }

    public int getRatesSum() {
        return ratesSum;
    }

    public void setRatesSum(int ratesSum) {
        this.ratesSum = ratesSum;
    }

    public Path(List<String> pathOfNodes, int value,  int sumOfNodesRate) {
        nodes = pathOfNodes;
        lostValue = value;
        ratesSum = sumOfNodesRate;
    }
}
