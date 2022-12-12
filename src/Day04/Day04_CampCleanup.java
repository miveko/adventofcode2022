package Day04;

import Base.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day04_CampCleanup extends Puzzle {

    public static void main(String[] args) {
        Puzzle puzzle = new Day04_CampCleanup();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        List<String> input = new ArrayList<>();
        while (inputReader.hasNextLine())
            input.add(inputReader.nextLine());

        int counterWholeOverlap = 0;
        int counterPartialOverlap = 0;
        int startA, endA, startB, endB;
        for (String s : input) {
            int firstHyphenIndx = s.indexOf('-');
            int secondHyphenIndx = s.lastIndexOf('-');
            int semicolonIndx = s.indexOf(',');
            startA = Integer.parseInt(s.substring(0, firstHyphenIndx));
            endA = Integer.parseInt(s.substring(firstHyphenIndx + 1, semicolonIndx));
            startB = Integer.parseInt(s.substring(semicolonIndx + 1, secondHyphenIndx));
            endB = Integer.parseInt(s.substring(secondHyphenIndx + 1));
            if (isThereFullyIncluded(startA, endA, startB, endB))
                counterWholeOverlap++;
            if (isTherePartialOverlap(startA, endA, startB, endB))
                counterPartialOverlap++;
        }

        setAnswerPartOne(String.valueOf(counterWholeOverlap));
        setAnswerPartTwo(String.valueOf(counterPartialOverlap));
    }

    private static boolean isThereFullyIncluded(int sa, int ea, int sb, int eb) {
        return (sa <= sb && ea >= eb) || (sb <= sa && eb >= ea);
    }

    private static boolean isTherePartialOverlap(int sa, int ea, int sb, int eb) {
        return ((ea >= sb && sa <= sb) || (eb >= sa && sb <= sa));
    }
}