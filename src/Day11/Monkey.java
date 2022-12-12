package Day11;

import java.util.ArrayList;
import java.util.List;

public class Monkey {
    List<Long> items;
    String[] operation;
    int divisibleTest;
    int indxTrue, indxFalse, numOfInspects;

    public Monkey(List<Long> items, String[] operation, int divisibleTest, int indxTrue, int indxFalse) {
        this.items = items;
        this.operation = operation;
        this.divisibleTest = divisibleTest;
        this.indxTrue = indxTrue;
        this.indxFalse = indxFalse;
        this.numOfInspects = 0;
    }

    public Monkey(Monkey m) {
        items = new ArrayList<>();
        items.addAll(m.items);
        operation = m.operation.clone();
        divisibleTest = m.divisibleTest;
        indxTrue = m.indxTrue;
        indxFalse = m.indxFalse;
        numOfInspects = m.numOfInspects;
    }

    public long operation(long old) {
        long a = (operation[0].equals("old")) ? old : Long.parseLong(operation[0]);
        long b = (operation[2].equals("old")) ? old : Long.parseLong(operation[2]);
        switch (operation[1]) {
            case "*" : return a * b;
            case "+" : return a + b;
            default: return -1;
        }
    }
}
