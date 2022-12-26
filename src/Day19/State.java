package Day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static Day19.Artefacts.*;

public class State {
    private int minute;
    private final Map<String, Integer> robot;
    private final Map<String, Integer> resources;
    private List<String> lastCouldBuys;
    //
    public int getMinute() {
        return minute;
    }
    public void updateMinute() {
        minute++;
    }
    public Map<String, Integer> getRobot() {
        return robot;
    }
    public Map<String, Integer> getResources() {
        return resources;
    }
    public List<String> getLastCouldBuys() {
        return lastCouldBuys;
    }
    public void setLastCouldBuys(List<String> lastCouldBuys) {
        this.lastCouldBuys = lastCouldBuys;
    }

    public State() {
        minute = 1;
        robot = new HashMap<>();
        robot.put(ORE, 1);
        robot.put(CLAY, 0);
        robot.put(OBSIDIAN, 0);
        robot.put(GEODE, 0);
        resources = new HashMap<>();
        resources.put(ORE, 1);
        resources.put(CLAY, 0);
        resources.put(OBSIDIAN, 0);
        resources.put(GEODE, 0);
        lastCouldBuys = new ArrayList<>();
    }

    public State(State state) {
        minute = state.minute;
        robot = new HashMap<>();
        for(String s : state.getRobot().keySet())
            robot.put(s, state.getRobot().get(s));
        resources = new HashMap<>();
        for(String s : state.getResources().keySet())
            resources.put(s, state.getResources().get(s));
        lastCouldBuys = new ArrayList<>();
        lastCouldBuys.addAll(state.getLastCouldBuys());
    }

    public boolean canBuild(Map<String, Integer> cost) {
        for (String c : cost.keySet()) {
            if(resources.get(c) < cost.get(c))
                return false;
        }
        return true;
    }

    public void build(String robotType, Map<String, Integer> cost) {
        for (String c : cost.keySet())
            resources.put(c, resources.get(c) - cost.get(c));
        robot.put(robotType, robot.get(robotType) + 1);
        lastCouldBuys.clear();
    }

    public void harvest() {
        for(String type : robot.keySet()) {
            resources.put(type, resources.get(type) + robot.get(type));
        }
    }
}
