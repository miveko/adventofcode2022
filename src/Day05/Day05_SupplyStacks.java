package Day05;

import Base.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day05_SupplyStacks extends Puzzle {

    public static void main(String[] args) {
        Puzzle puzzle = new Day05_SupplyStacks();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        String line = inputReader.nextLine();
        int stacksNum = (line.length() + 2) / 4;
        List<Character>[] stackA = new ArrayList[stacksNum];
        List<Character>[] stackB = new ArrayList[stacksNum];
        while (line.charAt(1) != '1') {
            for (int i = 1; i < line.length(); i += 4) {
                if (line.charAt(i) != ' ') {
                    if (stackA[i / 4] == null) {
                        stackA[i / 4] = new ArrayList<>();
                        stackB[i / 4] = new ArrayList<>();
                    }
                    stackA[i / 4].add(line.charAt(i));
                    stackB[i / 4].add(line.charAt(i));
                }
            }

            line = inputReader.nextLine();
        }
        //reading the empty (separating) line
        inputReader.nextLine();
        int cratesNum, posFrom, posTo;
        while (inputReader.hasNextLine()) {
            line = inputReader.nextLine();
            String[] parts =  line.split(" ");
            cratesNum = Integer.parseInt(parts[1]);
            posFrom = Integer.parseInt(parts[3]) - 1;
            posTo = Integer.parseInt(parts[5]) - 1;
            //CrateMover 9000
            for(int i = 0; i < cratesNum; i++) {
                char ch = stackA[posFrom].get(0);
                stackA[posFrom].remove(0);
                stackA[posTo].add(0, ch);
            }
            //CrateMover 9001
            for(int i = cratesNum - 1;i >=0;  i--) {
                char ch = stackB[posFrom].get(i);
                stackB[posFrom].remove(i);
                stackB[posTo].add(0, ch);
            }
        }

        StringBuilder answerA = new StringBuilder();
        for (List<Character> characters : stackA) answerA.append(characters.get(0));
        setAnswerPartOne(answerA.toString());
        StringBuilder answerB = new StringBuilder();
        for (List<Character> characters : stackB) answerB.append(characters.get(0));
        setAnswerPartTwo(answerB.toString());
    }
}
