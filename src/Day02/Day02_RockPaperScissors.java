package Day02;

import Base.Puzzle;
import java.util.Scanner;

public class Day02_RockPaperScissors extends Puzzle {


    public static void main(String[] args) {
        Puzzle puzzle = new Day02_RockPaperScissors();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        int scoreA = 0;
        int scoreB = 0;
        String line;
        while (inputReader.hasNextLine()) {
            line = inputReader.nextLine();
            scoreA += calculateScoreType1(line.charAt(0), line.charAt(2));
            scoreB += calculateScoreType2(line.charAt(0), line.charAt(2));
        }

        setAnswerPartOne(String.valueOf(scoreA));
        setAnswerPartTwo(String.valueOf(scoreB));
    }

    private static int calculateScoreType1(char opp, char me) {
        if(opp == 'A' && me == 'X')
            return 4;
        else if(opp == 'B' && me == 'X')
            return 1;
        else if(opp == 'C' && me == 'X')
            return 7;
        else if(opp == 'A' && me == 'Y')
            return 8;
        else if(opp == 'B' && me == 'Y')
            return 5;
        else if(opp == 'C' && me == 'Y')
            return 2;
        else if(opp == 'A' && me == 'Z')
            return 3;
        else if(opp == 'B' && me == 'Z')
            return 9;
        else if(opp == 'C' && me == 'Z')
            return 6;
        return 0;
    }

    private static int calculateScoreType2(char opp, char me) {
        if(opp == 'A' && me == 'X')
            return 3;
        else if(opp == 'B' && me == 'X')
            return 1;
        else if(opp == 'C' && me == 'X')
            return 2;
        else if(opp == 'A' && me == 'Y')
            return 4;
        else if(opp == 'B' && me == 'Y')
            return 5;
        else if(opp == 'C' && me == 'Y')
            return 6;
        else if(opp == 'A' && me == 'Z')
            return 8;
        else if(opp == 'B' && me == 'Z')
            return 9;
        else if(opp == 'C' && me == 'Z')
            return 7;
        return 0;
    }
}
