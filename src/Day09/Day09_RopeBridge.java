package Day09;

import Base.Puzzle;
import java.awt.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day09_RopeBridge extends Puzzle {
    static final byte KNOTS_NUM_PART_ONE = 2;
    static final byte KNOTS_NUM_PART_TWO = 10;
    static Point[] knots;
    static Set<String> coveredPart1;
    static Set<String> coveredPart2 = new HashSet<>();

    public static void main(String[] args) {
        Puzzle puzzle = new Day09_RopeBridge();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        knots = new Point[Integer.max(KNOTS_NUM_PART_ONE, KNOTS_NUM_PART_TWO)];
        for(int i = 0; i < knots.length;i++)
            knots[i] = new Point(0,0);
        coveredPart1 = new HashSet<>();
        coveredPart2 = new HashSet<>();
        while (inputReader.hasNextLine()) {
            char d = inputReader.next().toCharArray()[0];
            int distance = Integer.parseInt(inputReader.next());
            for(int i = 0; i < distance; i++) {
                moveHead(d);
                moveTail();
            }
        }

        setAnswerPartOne(String.valueOf(coveredPart1.size()));
        setAnswerPartTwo(String.valueOf(coveredPart2.size()));
    }

    private static void moveHead(char direction) {
        switch (direction) {
            case 'U' : knots[0].y++; break;
            case 'D' : knots[0].y--; break;
            case 'L' : knots[0].x--; break;
            case 'R' : knots[0].x++; break;
        }
    }

    private static void moveTail() {
        for(int i = 0; i < knots.length - 1; i++) {
            if (Math.abs(knots[i].x - knots[i+1].x) < 2 && Math.abs(knots[i].y - knots[i+1].y) < 2)
                break;

            if (knots[i].x - knots[i+1].x > 0) {
                knots[i+1].x++;
            } else if (knots[i].x - knots[i+1].x < 0) {
                knots[i+1].x--;
            }

            if (knots[i].y - knots[i+1].y > 0) {
                knots[i+1].y++;
            } else if (knots[i].y - knots[i+1].y < 0) {
                knots[i+1].y--;
            }
        }

        coveredPart1.add(knots[KNOTS_NUM_PART_ONE - 1].x + " " + knots[KNOTS_NUM_PART_ONE - 1].y);
        coveredPart2.add(knots[KNOTS_NUM_PART_TWO - 1].x + " " + knots[KNOTS_NUM_PART_TWO - 1].y);
    }
}
