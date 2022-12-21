package Day16;

import Base.Puzzle;
import java.util.*;
import static Day16.Day16_constants.*;

public class Day16_ProboscideaVolcanium extends Puzzle {


    private Map<String, Valve> valve;
    Map<String, Integer> fromToLengths;
    private List<Path> ranklist, nextRanklist;
    Set<String> havingPressure;
    private int sumPressure;

    public static void main(String[] args) {
        Puzzle puzzle = new Day16_ProboscideaVolcanium();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        //Reading input
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


        havingPressure.add(FIRST);
        fromToLengths = createFromToLengths();
        calcSumPressure();
        havingPressure.remove(FIRST);

        ranklist = new ArrayList<>();
        nextRanklist = new ArrayList<>();
        ranklist.add(new Path(new ArrayList<>(), 0, 0));
        ranklist.get(0).getNodes().add("AA");
        while (ranklist.get(0).getNodes().size() < havingPressure.size()){
            for (Path path : ranklist) {
                pickElement(STEPS, path.getNodes(), havingPressure);
            }
            ranklist = nextRanklist;
            nextRanklist = new ArrayList<>();
        }

        int releasedPressure = TIME_PART_ONE * sumPressure + ranklist.get(0).getLostValue();
        setAnswerPartOne(String.valueOf(releasedPressure));
    }

    private Map<String, Integer> createFromToLengths() {
        Map<String, Integer> fromToLengths = new HashMap<>();
        for(String fr : havingPressure)
            for(String to : havingPressure) {
                if(fr.equals(to))
                    continue;;
                int d = calcFromToDistance(fr, to);
                fromToLengths.put(fr + " " + to, d);
                fromToLengths.put(to + " " + fr, d);
            }
        return  fromToLengths;
    }

    private void calcSumPressure() {
        for(String e : havingPressure)
            sumPressure += valve.get(e).getRate();
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

    private void pickElement(int stepsLeft,  List<String> sequence, Set<String> nodesLeft) {
        List<String> nextSequence;
        for(String hp : nodesLeft) {
            if(!sequence.contains(hp)){
                nextSequence = new ArrayList<>(sequence);
                nextSequence.add(hp);
                if(stepsLeft == 1 || nodesLeft.size() == 1) {
                    evaluateSequence(nextSequence);
                } else {
                    Set<String> nextNodesLeft = new HashSet<>(nodesLeft);
                    nextNodesLeft.remove(hp);
                    pickElement(stepsLeft - 1, nextSequence, nextNodesLeft);
                }
            }
        }
    }

    private void evaluateSequence(List<String> sequence) {
        int value = 0;
        int minutesLeft = TIME_PART_ONE;
        int ratesSum = 0;
        for(int i = 0; i < sequence.size() - 1 && minutesLeft > 0; i++) {
            String singleFromTo = sequence.get(i) + " " + sequence.get(i+1);
            int minutesPassed = fromToLengths.get(singleFromTo) + 1;
            minutesPassed = Integer.min(minutesPassed, minutesLeft);
            value -= minutesPassed * (sumPressure - ratesSum);
            minutesLeft -= minutesPassed;
            ratesSum += valve.get(sequence.get(i+1)).getRate();
        }

        if(sequence.size() < havingPressure.size()) {
            value += ratesSum - sumPressure;    //including in considerartion the rate of the last node added
        } else {
            value -= minutesLeft * (sumPressure - ratesSum);
        }

        putInNextRanklist(sequence, value, ratesSum);
    }

    private void putInNextRanklist(List<String> sequence, int value, int rateSum) {
        int startIndex = 0;
        int endIndex = nextRanklist.size();
        while(startIndex != endIndex) {
            int middle = (startIndex + endIndex) / 2;
            if(nextRanklist.get(middle).getLostValue() < value)
                endIndex = middle;
            else
                startIndex = middle + 1;
        }

        if(endIndex < BEST_OF) {
            nextRanklist.add(startIndex, new Path(sequence, value, rateSum));
            if(nextRanklist.size() == BEST_OF + 1)
                nextRanklist.remove(BEST_OF);
        }
    }


}
