package Day20;

import Base.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day20_GrovePositioningSystem extends Puzzle {

    final int DECRYPT_KEY_PART_TWO = 811589153;
    List<Long> nums, numsP2;
    int[] position;

    public static void main(String[] args) {
        Puzzle puzzle = new Day20_GrovePositioningSystem();
        puzzle.execute(args);
    }

    @Override
    protected void solution(Scanner inputReader) {
        nums = new ArrayList<>();
        numsP2 = new ArrayList<>();
        while(inputReader.hasNextLine()) {
            long number = Long.parseLong(inputReader.nextLine());
            nums.add(number);
            numsP2.add(number * DECRYPT_KEY_PART_TWO);
        }

        int size = nums.size();
        //start PartOne
        position = initializePositionArray(size);
        mixList(nums, size);
        setAnswerPartOne(String.valueOf(getResult(nums, size)));

        //start PartTwo
        position = initializePositionArray(size);
        for(int iteration=0; iteration < 10; iteration++)
            mixList(numsP2, size);
        setAnswerPartTwo(String.valueOf(getResult(numsP2, size)));
    }

    private void mixList(List<Long> array, int size) {
        for (int i = 0; i < size; i++) {
            long temp = array.get(position[i]);         //getting the next number which is currently at index position[i]
            int movement = (int) (temp % (size - 1));   //(size - 1) movements are equal to no movement
            int newPosition;
            if (movement < 0) {                         //moving backwards
                newPosition = position[i] + movement;
                if (newPosition <= 0)                   //if the number overtakes the first element when moved
                    newPosition += size - 1;
            } else {                                    //moving forward
                newPosition = position[i] + movement;
                if (newPosition >= size)                //if the number overtakes the last element when moved
                    newPosition -= size - 1;
            }
            //placing the number at its newer position
            array.remove(position[i]);
            array.add(newPosition, temp);
            //adjusting the new position of the elements affected by the movement of number
            if (newPosition > position[i]) {                // the number moved forward
//                for(int j = position[i]+1; j <= newPosition; j++) !!checkWhyThisIsEquivelent for basiclinput!!
                for (int j = 0; j < size; j++) {
                    if (position[j] >= position[i] + 1 && position[j] <= newPosition)
                        position[j]--;
                }
            } else {                                        // the number moved backwards
//                for(int j = newPosition; j < position[i] ; j++) !!checkWhyThisIsEquivelent for basiclinput!!
                for (int j = 0; j < size; j++) {
                    if (position[j] >= newPosition && position[j] < position[i])
                        position[j]++;
                }
            }
            //adjusting the position of the number
            position[i] = newPosition;
        }
//       printNums();
    }

    private int[] initializePositionArray(int size) {
        int[] array = new int[size];
        for(int i=0; i<size; i++)
            array[i] = i;
        return array;
    }

    private long getResult(List<Long> array, int size) {
        int iOf0 = array.indexOf(0L);
        if(iOf0 >= 0) {
            return array.get((iOf0 + 1000) % size) + array.get((iOf0 + 2000) % size) + array.get((iOf0 + 3000) % size);
        } else {
            System.err.println("0 is not located in the array");
            return -1234567890;
        }
    }

    private  void printNums() {
        for(long i : nums) {
            System.out.print(i + "\t");
        }
        System.out.println();
    }
}
