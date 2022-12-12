package Day10;

import Base.Puzzle;
import java.util.Scanner;

public class Day10_CathodeRayTube extends Puzzle{
    int cycle, sum, registerX;
    String image;

    public static void main(String[] args) {
        Puzzle puzzle = new Day10_CathodeRayTube();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        cycle = 0;
        sum = 0;
        registerX = 1;
        image = "";

        String word;
        while (inputReader.hasNext()) {
            word = inputReader.next();
            updateCycle();
            if(word.equals("addx")) {
                word = inputReader.next();
                updateCycle();
                registerX += Integer.parseInt(word);
            }
        }

        setAnswerPartOne(String.valueOf(sum));
        setAnswerPartTwo(image);
    }

    private void updateCycle() {
        //PartTwo
        if(cycle % 40 == 0)
            image += System.lineSeparator();
        if(Math.abs(cycle % 40 - registerX) < 2)
            image += "#";
        else
            image += ".";
        cycle++;
        if(cycle % 40 == 20 && cycle < 230) {
            sum += cycle * registerX;
        }
    }
}
