package Day06;

import Base.Puzzle;
import java.util.Scanner;

public class Day06_TuningTrouble extends Puzzle {

    final int MARKER_LENGTH_ONE = 4;
    final int MARKER_LENGTH_TWO = 14;

    public static void main(String[] args) {
        Puzzle puzzle = new Day06_TuningTrouble();
        if(args.length == 0)
            args = new String[] {"basic.input", "basic1.input", "basic2.input", "basic3.input", "basic4.input", "main.input"};
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        String line = inputReader.nextLine();
        //adding 3 characters in front of line which are the same as the first character of line;
        line = new String(new char[3]).replace("\0", String.valueOf(line.charAt(0))) + line;
        int pointer = 3;//potential earliest start of a marker
        boolean marketOneFound = false;
        for(int i = 4; i < line.length(); i++) {
            for(int j = i - 1; j >= pointer; j--) {
                if(line.charAt(j) == line.charAt(i)) {
                    pointer = j + 1;
                    break;
                }
            }

            if(i - pointer == MARKER_LENGTH_TWO - 1) {
                setAnswerPartTwo(String.valueOf(i-2)); // - 3 added chars at the beggining + the zero index;
                break;
            } else if(!marketOneFound && i - pointer == MARKER_LENGTH_ONE - 1) {
                setAnswerPartOne(String.valueOf(i - 2));
                marketOneFound = true;
            }
        }
    }
}
