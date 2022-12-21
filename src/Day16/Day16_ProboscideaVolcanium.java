package Day16;

import Base.Puzzle;
import java.util.*;
import static Day16.Day16_constants.*;

public class Day16_ProboscideaVolcanium extends Puzzle {
    private Map<String, Valve> valve;
    Map<String, Integer> fromToLengths;
    private List<Path> nextRanklist;
    Set<String> havingPressure;
    private int minutesLeft;

    public static void main(String[] args) {
        Puzzle puzzle = new Day16_ProboscideaVolcanium();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        //reading input
        valve = new LinkedHashMap<>();
        havingPressure = new HashSet<>();
        while (inputReader.hasNextLine()) {
            String[] line = inputReader.nextLine()
                    .replace(" tunnels lead to valves ", "")
                    .replace(" tunnel leads to valve ", "")
                    .replace(" has flow rate=", ";")
                    .replace("Valve ", "")
                    .split(";");
            List<String> leadsToValves = Arrays.asList(line[2].split(", "));
            valve.put(line[0], new Valve(Integer.parseInt(line[1]), leadsToValves));
            if(Integer.parseInt(line[1]) > 0) {
                havingPressure.add(line[0]);
            }
        }

        //create fromTo lengths
        havingPressure.add(FIRST);
        fromToLengths = createFromToLengths();

        //start Part_One
        minutesLeft = MINUTES_PART_ONE;
        nextRanklist = new ArrayList<>();
        List<Path> ranklist = new ArrayList<>();
        ranklist.add(new Path(new ArrayList[2], 0));
        ranklist.get(0).getPathsOfNodes()[0] = new ArrayList<>();
        ranklist.get(0).getPathsOfNodes()[0].add("AA");
        while (ranklist.get(0).getPathsOfNodes()[0].size() < havingPressure.size()) {
            for (Path path : ranklist) {
                pickElement(STEPS_PART_ONE, path.getPathsOfNodes(), havingPressure);
            }
            ranklist = nextRanklist;
            nextRanklist = new ArrayList<>();
        }

        setAnswerPartOne(String.valueOf(ranklist.get(0).getValue()));

        //start Part_Two
        minutesLeft = MINUTES_PART_TWO;
        nextRanklist = new ArrayList<>();
        ranklist = new ArrayList<>();
        ranklist.add(new Path(new ArrayList[2], 0));
        ranklist.get(0).getPathsOfNodes()[0] = new ArrayList<>();
        ranklist.get(0).getPathsOfNodes()[0].add("AA");
        ranklist.get(0).getPathsOfNodes()[1] = new ArrayList<>();
        ranklist.get(0).getPathsOfNodes()[1].add("AA");
        while (ranklist.get(0).getPathsOfNodes()[0].size() + ranklist.get(0).getPathsOfNodes()[1].size()  < havingPressure.size() + 1) {
            for (Path path : ranklist) {
                pickElement_partTwo(STEPS_PART_TWO, path.getPathsOfNodes(), havingPressure);
            }
            ranklist = nextRanklist;
            nextRanklist = new ArrayList<>();
        }

        setAnswerPartTwo(String.valueOf(ranklist.get(0).getValue()));
    }

    private Map<String, Integer> createFromToLengths() {
        Map<String, Integer> fromToLengths = new HashMap<>();
        for(String fr : havingPressure)
            for(String to : havingPressure) {
                if(fr.equals(to))
                    continue;
                int d = calcFromToDistance(fr, to);
                fromToLengths.put(fr + " " + to, d);
                fromToLengths.put(to + " " + fr, d);
            }
        return  fromToLengths;
    }

    private int calcFromToDistance(String from, String to) {
        int distance = 0;
        Set<String> current;
        Set<String> next = new HashSet<>();
        next.add(from);
        while (!next.contains(to)) {
            distance++;
            current = next;
            next = new HashSet<>();
            for(String c : current) {
                Valve temp = valve.get(c);
                next.addAll(temp.getLeadsTo());
            }
        }

        return distance;
    }

    private void pickElement(int stepsLeft,  List<String>[] sequence, Set<String> left) {
        Set<String> nodesLeft = findTheLeftNodes(sequence, left);
        for(String node : nodesLeft) {
            List<String>[] nodesPath = new ArrayList[2];
            List<String> nextSequence = new ArrayList<>(sequence[0]);
            nextSequence.add(node);
            nodesPath[0] = nextSequence;
            if(stepsLeft == 1 || nodesLeft.size() == 1) {
                int value = evaluateSequence(nodesPath);
                putInNextRanklist(new Path(nodesPath, value));
            } else {
                Set<String> nextNodesLeft = new HashSet<>(nodesLeft);
                nextNodesLeft.remove(node);
                pickElement(stepsLeft - 1, nodesPath, nextNodesLeft);
            }
        }
    }

    private Set<String> findTheLeftNodes(List<String>[] sequence, Set<String> left) {
        Set<String> nodesLeft = new HashSet<>(left);
        for(String node : sequence[0])
            nodesLeft.remove(node);
        if(sequence[1] != null && sequence[1].size() > 1)
            for(String node : sequence[1])
                nodesLeft.remove(node);
        return nodesLeft;
    }

    private void pickElement_partTwo(int stepsLeft,  List<String>[] sequence, Set<String> left) {
        int totalValue;
        Set<String> nodesLeft = findTheLeftNodes(sequence, left);
        for(String node1 : nodesLeft) {
            Set<String> nextNodesLeft = new HashSet<>(nodesLeft);
            nextNodesLeft.remove(node1);
            if(nextNodesLeft.size() > 0) {
                for (String node2 : nextNodesLeft) {
                    List<String>[] nextSequence = new ArrayList[2];
                    nextSequence[0] = new ArrayList<>(sequence[0]);
                    nextSequence[0].add(node1);
                    //adding next node to nextSequence[0]
                    nextSequence[1] = new ArrayList<>(sequence[1]);
                    nextSequence[1].add(node2);
                    totalValue = evaluateSequence(nextSequence);
                    if(nextNodesLeft.size() == 1 || stepsLeft == 1) {
                        putInNextRanklist(new Path(nextSequence, totalValue));
                    } else {
                        Set<String> nextNextNodesLeft = new HashSet<>(nextNodesLeft);
                        pickElement_partTwo(--stepsLeft, nextSequence, nextNextNodesLeft);
                    }
                    //logic to include the possibility in which it is better to add next node(node2) to nextSequence[0]
                    List<String>[] nextSequence1 = new ArrayList[2];
                    nextSequence1[0] = new ArrayList<>(nextSequence[0]);
                    nextSequence1[0].add(node2);
                    nextSequence1[1] = new ArrayList<>(nextSequence[1]);
                    nextSequence1[1].remove(node2);
                    totalValue = evaluateSequence(nextSequence);
                    if(nextNodesLeft.size() == 1 || stepsLeft == 1) {
                        putInNextRanklist(new Path(nextSequence1, totalValue));
                    } else {
                        Set<String> nextNextNodesLeft = new HashSet<>(nextNodesLeft);
                        pickElement_partTwo(--stepsLeft, nextSequence1, nextNextNodesLeft);
                    }
                }
            } else {
                List<String>[] nextSequence = new ArrayList[2];
                nextSequence[0] = new ArrayList<>(sequence[0]);
                nextSequence[0].add(node1);
                nextSequence[1] = new ArrayList<>(sequence[1]);
                totalValue = evaluateSequence(nextSequence);
                putInNextRanklist(new Path(nextSequence, totalValue));
            }
        }
    }

    private int evaluateSequence(List<String>[] sequence) {
        int value = 0;
        int minutesLeft = this.minutesLeft;
        for(int i = 0; i < sequence[0].size() - 1 && minutesLeft > 0; i++) {
            String singleFromTo = sequence[0].get(i) + " " + sequence[0].get(i+1);
            int minutesPassed = fromToLengths.get(singleFromTo) + 1;
            minutesLeft = minutesLeft - minutesPassed;
            if(minutesLeft > 0)
                value += minutesLeft * valve.get(sequence[0].get(i+1)).getRate();
        }

        if(sequence[1] != null &&sequence[1].size() > 0) {
            int value1 = 0;
            minutesLeft = this.minutesLeft;
            for(int i = 0; i < sequence[1].size() - 1 && minutesLeft > 0; i++) {
                String singleFromTo = sequence[1].get(i) + " " + sequence[1].get(i+1);
                int minutesPassed = fromToLengths.get(singleFromTo) + 1;
                minutesLeft -= minutesPassed;
                if(minutesLeft > 0)
                    value1 += minutesLeft * valve.get(sequence[1].get(i+1)).getRate();
            }
            value += value1;
        }

        return value;
    }

    private void putInNextRanklist(Path path) {
        int startIndex = 0;
        int endIndex = nextRanklist.size();
        while(startIndex != endIndex) {
            int middle = (startIndex + endIndex) / 2;
            if(nextRanklist.get(middle).getValue() < path.getValue())
                endIndex = middle;
            else
                startIndex = middle + 1;
        }

        if(endIndex < BEST_OF) {
            nextRanklist.add(startIndex, path);
            if(nextRanklist.size() == BEST_OF + 1)
                nextRanklist.remove(BEST_OF);
        }
    }
}
