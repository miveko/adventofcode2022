package Day11;

import Base.Puzzle;

import java.util.*;

public class Day11_MonkeyInTheMiddle extends Puzzle {

    final int ROUNDS_PART_ONE = 20;
    final int ROUNDS_PART_TWO = 10000;
    private List<Monkey> monkeys = new ArrayList<>();
    private static int divisiblesProduction = 1;

    public static void main(String[] args) {
        Puzzle puzzle = new Day11_MonkeyInTheMiddle();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        monkeys.clear();
        Day11_MonkeyInTheMiddle.divisiblesProduction = 1;
        while (inputReader.hasNextLine()) {
            if(inputReader.nextLine().contains("Monkey")) {
                monkeys.add(addMonkey(inputReader));
            } else {
                System.err.println("Inconsistent input stream!");
            }
        }

        List<Monkey> monkeys_copy = new ArrayList<>();
        for(Monkey m : monkeys)
            monkeys_copy.add(new Monkey(m));
        simulatePiggyInTheMiddle(true);
        setAnswerPartOne(String.valueOf(calcMonkeyBusinessLeve()));
        monkeys = monkeys_copy;
        simulatePiggyInTheMiddle(false);
        setAnswerPartTwo(String.valueOf(calcMonkeyBusinessLeve()));
    }

    private Monkey addMonkey(Scanner reader) {
        List<Long> items = new ArrayList<>();
        for(String s : reader.nextLine().replace("  Starting items: ", "").split(", ")) {
            items.add(Long.parseLong(s));
        }

        String[] operation =
                reader.nextLine().replace("  Operation: new = ", "").split(" ");
        int divisibleBy =
                Integer.parseInt(reader.nextLine().replace("  Test: divisible by ", ""));
        int receiverIfTrue =
                Integer.parseInt(reader.nextLine().replace("    If true: throw to monkey ", ""));
        int receiverIfFalse =
                Integer.parseInt(reader.nextLine().replace("    If false: throw to monkey ", ""));

        if(reader.hasNextLine())
            reader.nextLine();
        divisiblesProduction *= divisibleBy;
        return new Monkey(items, operation, divisibleBy, receiverIfTrue, receiverIfFalse);
    }

    private void simulatePiggyInTheMiddle(boolean partOne) {
        int rounds = partOne ? ROUNDS_PART_ONE : ROUNDS_PART_TWO;

        for(int i = 0; i < rounds; i++)
            for(Monkey m : monkeys) {
                for(long item : m.items) {
                    item %= divisiblesProduction;
                    long worryLevel = m.operation(item);
                    if(partOne) worryLevel /= 3;
                    if(worryLevel % m.divisibleTest == 0) {
                        monkeys.get(m.indxTrue).items.add(worryLevel);
                    } else {
                        monkeys.get(m.indxFalse).items.add(worryLevel);
                    }
                }

                m.numOfInspects += m.items.size();
                m.items.clear();
            }
    }

    private long calcMonkeyBusinessLeve() {
        int[] maxInspects = new int[] {0, -1};
        for(Monkey m : monkeys) {
            if(m.numOfInspects > maxInspects[0]) {
                maxInspects[1] = maxInspects[0];
                maxInspects[0] = m.numOfInspects;
            } else if(m.numOfInspects > maxInspects[1]) {
                maxInspects[1] = m.numOfInspects;
            }
        }

        return (long) maxInspects[0] * maxInspects[1];
    }
}
