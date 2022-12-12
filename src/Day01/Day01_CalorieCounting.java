package Day01;

import Base.Puzzle;
import java.util.*;

public class Day01_CalorieCounting extends Puzzle {

    public static void main(String[] args) {
        Puzzle puzzle = new Day01_CalorieCounting();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        List<Integer> maxCalories = new ArrayList<>(Arrays.asList(0, 0, 0));
        int currentCalories = 0;
        int min = 0;
        String line;
        while (inputReader.hasNextLine()) {
            line = inputReader.nextLine();
            if(line.length() > 2) {
                currentCalories += Integer.parseInt(line);
            }

            if(line.length() < 2 || !inputReader.hasNextLine()) {
                if(currentCalories > min) {
                    maxCalories.set(maxCalories.indexOf(min), currentCalories);
                    min = Collections.min(maxCalories);
                }
                currentCalories = 0;
            }
        }

        this.setAnswerPartOne(Collections.max(maxCalories).toString());
        this.setAnswerPartTwo(String.valueOf(maxCalories.stream().mapToInt(i -> i).sum()));
    }
}
