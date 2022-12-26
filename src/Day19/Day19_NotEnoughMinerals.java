package Day19;

import Base.Puzzle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import static Day19.Artefacts.*;

public class Day19_NotEnoughMinerals extends Puzzle {
    final static int MINUTES_PART_ONE = 24;
    final static int MINUTES_PART_TWO = 32;
    final int ORE_CLAY_ROBOTS_TRESHOLD = 25;
    List<Blueprint> blueprint;
    List<State> unusedOptions;

    public static void main(String[] args) {
        Puzzle puzzle = new Day19_NotEnoughMinerals();
        puzzle.execute(args);
    }

    @Override
    protected void solution(Scanner inputReader) {
        blueprint = new ArrayList<>();
        int index = -1;
        while (inputReader.hasNextLine()) {
            String[] line = inputReader.nextLine().split("[:.]");
            for (String s : line) {
                if (s.startsWith("Blueprint")) {
                    index++;
                    blueprint.add(new Blueprint());
                } else if (s.trim().startsWith("Each " + ORE + " robot costs ")) {
                    blueprint.get(index).price = new HashMap<>();
                    blueprint.get(index).price.put(ORE, new HashMap<>());
                    int price = Integer.parseInt(s.trim().split(" ")[4]);
                    blueprint.get(index).price.get(ORE).put(ORE, price);
                } else if (s.trim().startsWith("Each " + CLAY + " robot costs ")) {
                    blueprint.get(index).price.put(CLAY, new HashMap<>());
                    int price = Integer.parseInt(s.trim().split(" ")[4]);
                    blueprint.get(index).price.get(CLAY).put(ORE, price);
                } else if (s.trim().startsWith("Each " + OBSIDIAN + " robot costs")) {
                    blueprint.get(index).price.put(OBSIDIAN, new HashMap<>());
                    int price = Integer.parseInt(s.trim().split(" ")[4]);
                    blueprint.get(index).price.get(OBSIDIAN).put(ORE, price);
                    price = Integer.parseInt(s.trim().split(" ")[7]);
                    blueprint.get(index).price.get(OBSIDIAN).put(CLAY, price);
                } else if (s.trim().startsWith("Each " + GEODE + " robot costs ")) {
                    blueprint.get(index).price.put(GEODE, new HashMap<>());
                    int price = Integer.parseInt(s.trim().split(" ")[4]);
                    blueprint.get(index).price.get(GEODE).put(ORE, price);
                    price = Integer.parseInt(s.trim().split(" ")[7]);
                    blueprint.get(index).price.get(GEODE).put(OBSIDIAN, price);
                }
            }
        }

        int score = 0;
        for (int i = 0; i < blueprint.size(); i++)
            score += findMaximumGeodes(i, MINUTES_PART_ONE) * (i + 1);
        setAnswerPartOne(String.valueOf(score));

        score = 1;
        for (int i = 0; i < blueprint.size() && i < 3; i++)
            score *= findMaximumGeodes(i, MINUTES_PART_TWO);
        setAnswerPartTwo(String.valueOf(score));
    }

    private int findMaximumGeodes(int blueprint, int minutes) {
        int maxGeodes = 0;
        unusedOptions = new ArrayList<>();
        unusedOptions.add(new State());
        while (unusedOptions.size() > 0) {
            State currState = unusedOptions.get(unusedOptions.size() - 1);
            unusedOptions.remove(unusedOptions.size() - 1);
            while (currState.getMinute() < minutes) {
                currState.updateMinute();
                currState = updateState(currState, blueprint);
            }
            if(currState.getResources().get(GEODE) > maxGeodes)
                maxGeodes = currState.getResources().get(GEODE);
        }
        return maxGeodes;
    }

    //!!!!!!NOT OPTIMUM AS PERFORMANCE AND EXPLORATION!!!!!!\\
    private State updateState(State currentState, int bp) {
        //Admitting only a single robot can be build in a certain minute
        List<String> possibleBuildings = new ArrayList<>();
        if (currentState.canBuild(blueprint.get(bp).getPrice().get(GEODE))) {
            possibleBuildings.add(GEODE);
        } else if(currentState.canBuild(blueprint.get(bp).getPrice().get(OBSIDIAN))) {
            possibleBuildings.add(OBSIDIAN);
        } else if(currentState.getMinute() < ORE_CLAY_ROBOTS_TRESHOLD) {
            if (currentState.canBuild(blueprint.get(bp).getPrice().get(CLAY)))
                possibleBuildings.add(CLAY);
            if (currentState.canBuild(blueprint.get(bp).getPrice().get(ORE)))
                possibleBuildings.add(ORE);
        }

        currentState.harvest();
        int stateCount = unusedOptions.size();
        for(String pb : possibleBuildings) {
            if(!currentState.getLastCouldBuys().contains(pb)) {
                State newState = new State(currentState);
                newState.build(pb, blueprint.get(bp).getPrice().get(pb));
                unusedOptions.add(newState);
            }
        }

        State newState = new State(currentState);
        newState.setLastCouldBuys(new ArrayList<>(possibleBuildings));  //eliminating minor options not taken in the previous minute
        unusedOptions.add(newState);
        currentState = unusedOptions.get(stateCount);                   //get the option with the optimum robot building option
        unusedOptions.remove(stateCount);
        return currentState;
    }
}
