package Day08;

import Base.Puzzle;
import java.util.Scanner;

public class Day08_TreetopTreeHouse extends Puzzle {

    static byte[][] forest;
    static int i, j, n, visibleNum, maxViewingArea;

    public static void main(String[] args) {
        Puzzle puzzle = new Day08_TreetopTreeHouse();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        visibleNum = 0;
        maxViewingArea = 1;
        String line = inputReader.nextLine();
        n = line.length();
        forest = new byte[n][n];
        //initialize forest with the input
        int lineNum = 0;
        while(lineNum < n) {
            for(j = 0; j < n; j++) {
                forest[lineNum][j] = Byte.parseByte(String.valueOf(line.charAt(j)));
            }

            if(inputReader.hasNextLine())
                line = inputReader.nextLine();
            lineNum++;
        }

        //calculate visibility and viewingDistance for each tree
        for(i = 0; i < n; i++)
            for(j = 0; j < n; j++) {
                calcViewingDistanceAndVisibility(i, j);
            }

        setAnswerPartOne(String.valueOf(visibleNum));
        setAnswerPartTwo(String.valueOf(maxViewingArea));
    }

    private static void calcViewingDistanceAndVisibility(int i, int j ) {
        int k, distance;
        int visible = 4;
        int viewArea = 1;
        //calculating distance and visibility to left
        distance = 0;
        for(k = j - 1; k >= 0; k--) {
            distance++;
            if(forest[i][j] <= forest[i][k]) {
                visible--;
                break;
            }
        }
        viewArea  *= distance;

        //calculating distance and visibility to right
        distance = 0;
        for(k = j + 1; k < n; k++) {
            distance++;
            if(forest[i][j] <= forest[i][k]) {
                visible--;
                break;
            }
        }
        viewArea  *= distance;

        //calculating distance and visibility to top
        distance = 0;
        for(k = i - 1; k >= 0; k--) {
            distance++;
            if(forest[i][j] <= forest[k][j]) {
                visible--;
                break;
            }
        }
        viewArea  *= distance;

        //calculating distance and visibility to bottom
        distance = 0;
        for(k = i + 1; k < n; k++) {
            distance++;
            if(forest[i][j] <= forest[k][j]) {
                visible--;
                break;
            }
        }
        viewArea  *= distance;

        if(viewArea > maxViewingArea)
            maxViewingArea = viewArea;
        if(visible > 0)
            visibleNum++;
    }
}
