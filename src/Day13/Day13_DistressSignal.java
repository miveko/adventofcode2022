package Day13;

import Base.Puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13_DistressSignal extends Puzzle {

    final String DIVIDER_PACKET_ONE =  "[[2]]";
    final String DIVIDER_PACKET_TWO =  "[[6]]";
    List<String> listToOrder;

    public static void main(String[] args) {
        Puzzle puzzle = new Day13_DistressSignal();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        listToOrder = new ArrayList<>();
        int indicesSum = 0;
        int index = 0;
        while (true) {
            index++;
            String left = inputReader.nextLine();
            String right = inputReader.nextLine();
            if(compare(transformToList(left), transformToList(right)) < 0) {
                indicesSum += index;
                listToOrder.add(left);
                listToOrder.add(right);
            } else {
                listToOrder.add(right);
                listToOrder.add(left);
            }

            if(inputReader.hasNextLine()) {
                inputReader.nextLine();
            } else break;
        }
        setAnswerPartOne(String.valueOf(indicesSum));

        //start Part Two
        listToOrder.add(DIVIDER_PACKET_ONE);
        listToOrder.add(DIVIDER_PACKET_TWO);
        boolean swapped;
        do {
            swapped = false;
            for(int i = 0; i < listToOrder.size() - 1; i++)
                if(compare(transformToList(listToOrder.get(i)), transformToList(listToOrder.get(i + 1))) > 0) {
                    String temp = listToOrder.get(i + 1);
                    listToOrder.set(i + 1, listToOrder.get(i));
                    listToOrder.set(i, temp);
                    swapped = true;
                }
        } while (swapped);

        int decoderKey =
                (listToOrder.indexOf(DIVIDER_PACKET_ONE) + 1) * (listToOrder.indexOf(DIVIDER_PACKET_TWO) + 1);
        setAnswerPartTwo(String.valueOf(decoderKey));
    }

    private List<String> transformToList(String packet) {
        List<String> elements = new ArrayList<>();
        packet = packet.substring(1, packet.length() - 1);
        while (packet.length() > 0) {
            String e = getNext(packet);
            elements.add(e);
            packet = packet.substring(e.length());
            if(packet.startsWith(",")) {
                packet = packet.substring(1);
            }
        }

        return elements;
    }

    private int compare(List<String> left, List<String> right) {
        int num = Math.max(left.size(), right.size());
        int result;
        for(int i = 0; i <= num; i++) {
            //check if any of the list has run out of elements
            if(left.size() == i && right.size() == i) {
                return 0;
            } else if(left.size() == i) {
                return -1;
            } else if(right.size() == i) {
                return 1;
            }

            if(!left.get(i).startsWith("[")) {
                if(!right.get(i).startsWith("[")) { //left element is a digit and right element is a digit
                    int lInt = Integer.parseInt(left.get(i));
                    int rInt = Integer.parseInt(right.get(i));
                    result = lInt - rInt;
                } else {     //left element is a digit and right element is a list
                    result = compare(transformToList("[" + left.get(i) + "]"), transformToList(right.get(i)));
                }
            } else {
                if(!right.get(i).startsWith("[")) {     //left element is a list and right is a digit
                    result = compare(transformToList(left.get(i)), transformToList("[" + right.get(i) + "]"));
                } else {     //left element is a list and right element is a list
                    result = compare(transformToList(left.get(i)), transformToList(right.get(i)));
                }
            }

            if(result != 0) {
                return result;
            } //else continue with the next element: i + 1
        }

        return 0;
    }

    private String getNext(String line) {
        if(line.charAt(0) == '[') { //next element is a list
            int k = 1;
            for(int i = 1; i < line.length(); i++) {
                if(line.charAt(i) == '[') {
                    k++;
                } else if(line.charAt(i) == ']') {
                    k--;
                    if(k == 0) {
                        return line.substring(0, i + 1);
                    }
                }
            }
        } else {    //next element is a number
            if(line.contains(","))
                return line.substring(0,line.indexOf(","));
            else if(line.contains("]"))
                return line.substring(0,line.indexOf("]"));
            else if(line.contains("["))
                return line.substring(0, line.indexOf("["));
            else
                return line;
        }

        System.err.println("Wrong input format!");
        return "";
    }
}
