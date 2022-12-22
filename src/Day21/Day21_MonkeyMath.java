package Day21;

import Base.Puzzle;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day21_MonkeyMath extends Puzzle {

    final String ME = "humn";
    Map<String, String> monkeyYells;

    public static void main(String[] args) {
        Puzzle puzzle = new Day21_MonkeyMath();
        puzzle.execute(args);
    }

    @Override
    protected void solution(Scanner inputReader) {
        monkeyYells = new HashMap<>();
        while(inputReader.hasNextLine()){
            String[] line = inputReader.nextLine().split(": ");
            monkeyYells.put(line[0], line[1]);
        }
        //Start PartOne
        setAnswerPartOne(getYell("root"));

        //Start PartTwo
        String num1 = monkeyYells.get("root").split(" ")[0];
        String num2 = monkeyYells.get("root").split(" ")[2];
        if(leadsToME(num1)) {
            reverseYell(num1, getYell(num2));
        } else  {
            reverseYell(num2, getYell(num1));
        }
    }

    private String getYell(String monkeyName) {
        String[] yell = monkeyYells.get(monkeyName).split(" ");
        if(yell.length == 1) {
            return yell[0];
        } else {
            String a = getYell(yell[0]);
            String operation = yell[1];
            String b = getYell(yell[2]);
            switch (operation) {
                case "+" : return String.valueOf(Long.parseLong(a) + Long.parseLong(b));
                case "-" : return String.valueOf(Long.parseLong(a) - Long.parseLong(b));
                case "*" : return String.valueOf(Long.parseLong(a) * Long.parseLong(b));
                case "/" : return String.valueOf(Long.parseLong(a) / Long.parseLong(b));
                default  : System.err.println("Invalid data! Operation: " + operation);
                return "";
            }
        }
    }

    private void reverseYell(String monkey, String number) {
        if(monkey.equals(ME)) {
            setAnswerPartTwo(number);
            return;
        }

        String[] yell = monkeyYells.get(monkey).split(" ");
        if(leadsToME(yell[0])) {
            reverseYell(yell[0], yell[1], Long.parseLong(getYell(yell[2])), Long.parseLong(number));
        } else {
            reverseYell(Long.parseLong(getYell(yell[0])), yell[1], yell[2], Long.parseLong(number));
        }
    }

    private void reverseYell(long a, String operation, String b, long result) {
        switch(operation) {
            case "+" : reverseYell(b, String.valueOf(result - a)); break;
            case "-" : reverseYell(b, String.valueOf(a - result)); break;
            case "*" : reverseYell(b, String.valueOf(result / a)); break;
            case "/" : reverseYell(b, String.valueOf(a / result)); break;
            default  : System.err.println("Invalid data! Operation: " + operation);
        }
    }

    private void reverseYell(String a, String operation, long b, long result) {
        switch(operation) {
            case "+" : reverseYell(a, String.valueOf(result - b)); break;
            case "-" : reverseYell(a, String.valueOf(b + result)); break;
            case "*" : reverseYell(a, String.valueOf(result / b)); break;
            case "/" : reverseYell(a, String.valueOf(b * result)); break;
            default  : System.err.println("Invalid data! Operation: " + operation);
        }
    }

    private boolean leadsToME(String monkey) {
        if(monkey.equals(ME))
            return true;
        String[] yell = monkeyYells.get(monkey).split(" ");
        if(yell.length == 1)
            return false;
        else if (yell[0].equals(ME) || yell[2].equals(ME))
            return true;
        else return leadsToME(yell[0]) || leadsToME(yell[2]);
    }
}
