package Day15;

import Base.Puzzle;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Day15_BeaconExclusionZone extends Puzzle {

    List<Point> sensor, beacon;
    List<Integer> distance;
    int axisMin, axisMax;

    public static void main(String[] args) {
        Puzzle puzzle = new Day15_BeaconExclusionZone();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        sensor = new ArrayList<>();
        beacon = new ArrayList<>();
        while (inputReader.hasNextLine()) {
            String[] line = inputReader.nextLine()
                    .replace("Sensor at x=", "")
                    .replace(" y=", "")
                    .replace(": closest beacon is at x=", ",")
                    .split(",");

            Point s = new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
            Point b = new Point(Integer.parseInt(line[2]), Integer.parseInt(line[3]));
            sensor.add(s);
            beacon.add(b);
        }

        //Part One
        int lineObserved = (sensor.get(0).x < 1000005) ? 10 : 2000000;
        Set<Integer> markedAtLine = new HashSet<>();
        distance = new ArrayList<>();
        for(Point s : sensor) {
            Point b = beacon.get(sensor.indexOf(s));
            int d = Math.abs(s.x - b.x) + Math.abs(s.y - b.y);
            distance.add(d);     //Used in Part Two
            int ovlap = d - Math.abs(s.y - lineObserved);
            for (int m = s.x - ovlap; m <= s.x + ovlap; m++)
                markedAtLine.add(m);
        }

        for(Point b : beacon)               //Eliminating beacons
            if(b.y == lineObserved)         //  lying on the lineObserved
                markedAtLine.remove(b.x);   //      from the set of marked(covered) points
        setAnswerPartOne(String.valueOf(markedAtLine.size()));

        //Part Two
        axisMin = 0;
        axisMax = lineObserved * 2;

        for(Point s : sensor) {
            int d = distance.get(sensor.indexOf(s)) + 1;
            for(int k = -d; k<= d + d; k++) {
                int i = s.x + k;
                int j = s.y - (d - Math.abs(k));
                if(isTheSearchedOne(i, j)) {
                    setAnswerPartTwo(String.valueOf((long)i * 4000000 + j));
                    return;
                }

                j = s.y + (d - Math.abs(k));
                if(isTheSearchedOne(i, j)) {
                    setAnswerPartTwo(String.valueOf((long)i * 4000000 + j));
                    return;
                }
            }
        }
    }

    private boolean isTheSearchedOne(int i, int j) {
        boolean isInScopoe = false;
        for(Point p : sensor) {
            int dist = distance.get(sensor.indexOf(p));
            if ((Math.abs(p.x - i) + Math.abs(p.y - j)) <= dist)
                isInScopoe = true;
        }

        return !isInScopoe && i >= axisMin && i <= axisMax && j >= axisMin && j <= axisMax;
    }
}
