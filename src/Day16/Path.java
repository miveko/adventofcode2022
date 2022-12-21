package Day16;

import java.util.List;

public class Path {
    private final List<String>[] pathsOfNodes;
    private final int value;
    public List<String>[] getPathsOfNodes() {
        return pathsOfNodes;
    }
    public int getValue() {
        return value;
    }

    public Path(List<String>[] pathOfNodes, int value) {
        pathsOfNodes = pathOfNodes;
        this.value = value;
    }
}
