package Day03;

import Base.Puzzle;
import java.util.Scanner;

public class Day03_RucksackReorganization extends Puzzle {

    public static void main(String[] args) {
        Puzzle puzzle = new Day03_RucksackReorganization();
        puzzle.execute(args);
    }

    public void solution(Scanner inputReader) {
        int prioritiesSum = 0;
        int prioritiesSum_B = 0;
        String[] rucksacks = new String[3];
        int indx = 0;
        String line;
        while(inputReader.hasNextLine()) {
            line = inputReader.nextLine();
            prioritiesSum += findItemPriority(line);
            rucksacks[indx] = line;
            if(indx == 2) {
                prioritiesSum_B += findItemPriority_B(rucksacks[0], rucksacks[1], rucksacks[2]);
                indx = 0;
            } else {
                indx++;
            }
        }

        setAnswerPartOne(String.valueOf(prioritiesSum));
        setAnswerPartTwo(String.valueOf(prioritiesSum_B));
    }

    private int findItemPriority(String elements) {
        for(int i = 0; i < elements.length() / 2; i++)
            for(int j = elements.length() / 2; j < elements.length(); j++)
                if(elements.charAt(i) == elements.charAt(j))
                    return getLetterPriority(elements.charAt(i));

        return -1;
    }

    private int findItemPriority_B(String rs1, String rs2, String rs3) {
        for(int i = 0; i < rs1.length(); i++)
            for(int j = 0; j < rs2.length(); j++)
                if(rs1.charAt(i) == rs2.charAt(j))
                    for(int k = 0; k < rs3.length(); k++)
                        if(rs1.charAt(i) == rs3.charAt(k))
                            return getLetterPriority(rs1.charAt(i));

        return - 1;
    }

    private int getLetterPriority(char letter) {
        if (letter < 91 && letter > 64) //Is capital letter
            return letter - 38;   //capital letter priority = ASCII code - 38
        else if (letter > 96)
            return letter - 96;   //small letter priority = ASCII code - 96
        else
            return -1;
    }
}
