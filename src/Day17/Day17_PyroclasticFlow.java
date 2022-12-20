package Day17;

import Base.Puzzle;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day17_PyroclasticFlow extends Puzzle {
    int SHAPES_TO_FALL_PART_ONE = 2022;
    long SHAPES_TO_FALL_PART_TWO = 1000000000000L;
    long numShapesFallen, height;
    Point[][] shapes;
    List<boolean[]> map;
    Point currPosition;
    int shapeIndx;
    boolean boosted, partTwo;


    public static void main(String[] args) {
        Puzzle puzzle = new Day17_PyroclasticFlow();
        puzzle.execute(args);
    }

    private void initializeData() {
        //initializeShapes
        shapes = new Point[5][];
        shapes[0] = new Point[] {new Point(0, 0), new Point(0, 1), new Point(0,2), new Point(0,3)};
        shapes[1] = new Point[] {new Point(0, 1), new Point(1, 0), new Point(1,1), new Point(1,2), new Point(2, 1)};
        shapes[2] = new Point[] {new Point(0, 0), new Point(0, 1), new Point(0,2), new Point(1,2), new Point(2, 2)};
        shapes[3] = new Point[] {new Point(0, 0), new Point(1, 0), new Point(2,0), new Point(3,0)};
        shapes[4] = new Point[] {new Point(0, 0), new Point(0, 1), new Point(1,0), new Point(1,1)};
        //initializing map
        map = new ArrayList<>();
        map.add(new boolean[7]);
        Arrays.fill(map.get(0), true);
        //set current position and initial shape
        resetCurrentPosition();
        numShapesFallen = height = 0;
        shapeIndx = 0;
        //needed for optimizing
        boosted = partTwo = false;
    }

    protected void solution(Scanner inputReader) {
        initializeData();
        String line = inputReader.nextLine();
        int lineLength = line.length();
        int inputIndx = 0;
        //next three needed for identifying cyclic recurrence
        long lastNumShapesFallen = 0;
        long lastHeight = 0;
        List<String> checkPoint = new ArrayList<>();
        while (numShapesFallen < SHAPES_TO_FALL_PART_TWO) {
            char direction = line.charAt(inputIndx);
            if (direction == '<') {
                moveLeft();
            } else if(direction == '>') {
                moveRight();
            } else {
                System.err.println("Invalid input!");
            }

            if (!moveDown()) {
                setOnMap();
//                showMap();
                numShapesFallen++;
                shapeIndx = (int) (numShapesFallen % shapes.length);
                if(!boosted && partTwo) {
                    String controlStr = (height - lastHeight) + "\t" + (numShapesFallen - lastNumShapesFallen) + "\t"
                            + shapeIndx + "\t" + inputIndx + "\t" + boolArrayToByte(map.get(map.size() - 1));
                    if(checkPoint.contains(controlStr))
                        jumpAheadAkaBoost(checkPoint, checkPoint.indexOf(controlStr), checkPoint.size());
                    checkPoint.add(controlStr);
                    lastHeight = map.size() - 1;
                    lastNumShapesFallen = numShapesFallen;
                }
                if (numShapesFallen == SHAPES_TO_FALL_PART_ONE) {
                    setAnswerPartOne(String.valueOf(height));
                    partTwo = true;
                }

                resetCurrentPosition();
            }

            inputIndx = ++inputIndx % lineLength;
        }

        setAnswerPartTwo(String.valueOf(height));
    }
    private void resetCurrentPosition() {
        currPosition = new Point((map.size() + 3), 2); //OFFSETS GIVEN BY THE ASSIGNMENT
    }

    private void moveLeft() {
        for(Point part : shapes[shapeIndx]) {
            int y = currPosition.y + part.y - 1;
            if(y < 0) return;
            int i = (currPosition.x + part.x);
            if(i < map.size() && map.get(i)[y]) return;
        }
        currPosition.y--;
    }

    private void moveRight() {
        for(Point part : shapes[shapeIndx]) {
            int y = currPosition.y + part.y + 1;
            if(y > 6)   return;
            int i = (currPosition.x + part.x);
            if(i < map.size() && map.get(i)[y]) return;
        }
        currPosition.y++;
    }

    private boolean moveDown() {
        for(Point part : shapes[shapeIndx]) {
            int i = (currPosition.x + part.x - 1);
            int y = currPosition.y + part.y;
            if(i < map.size() && map.get(i)[y]) return false;
        }

        currPosition.x--;
        return true;
    }

    private void setOnMap() {
        int shapePartInd = 0;
        while (shapePartInd < shapes[shapeIndx].length) {
            int i = (int) (currPosition.x + shapes[shapeIndx][shapePartInd].getX());
            int j = (int) (currPosition.y + shapes[shapeIndx][shapePartInd].getY());
            if(map.size() == i) {
                map.add(new boolean[7]);
                height++;
            }
            map.get(i)[j] = true;
            shapePartInd++;
        }
    }

    private int boolArrayToByte(boolean[] arr) {
        int n = 0;
        for (boolean b : arr)
            n = (n << 1) + (b ? 1 : 0);
        return n;
    }

    private void jumpAheadAkaBoost(List<String> checkpoint, int startIndx, int endIndx) {
        int heightDiff = 0;
        int shapesFallenDiff = 0;
        for(int i = startIndx; i < endIndx; i++) {
            String[] checkP = checkpoint.get(i).split("\t");
            heightDiff += Integer.parseInt(checkP[0]);
            shapesFallenDiff += Integer.parseInt((checkP[1]));
        }
        long multiplier = (SHAPES_TO_FALL_PART_TWO - numShapesFallen) / shapesFallenDiff;
        height += multiplier * heightDiff;
        numShapesFallen += multiplier * shapesFallenDiff;
        boosted = true;
    }

    private void showMap() {
        System.out.println("       ");
        for(long i = map.size() - 1; i >= 0 && map.size() - i < 50; i--) {
            for(boolean b : map.get((int) i))
                if(b)
                    System.out.print("#");
                else
                    System.out.print(".");
            System.out.println();
        }
    }
}
