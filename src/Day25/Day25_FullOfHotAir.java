package Day25;

import Base.Puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day25_FullOfHotAir extends Puzzle {
    List<String> snafuNums = new ArrayList<>();
    long num;

    public static void main(String[] args) {
        Puzzle puzzle = new Day25_FullOfHotAir();
        puzzle.execute(args);
    }

    @Override
    protected void solution(Scanner inputReader) {
        snafuNums = new ArrayList<>();
        while (inputReader.hasNextLine()) {
            snafuNums.add(inputReader.nextLine());
        }

        num = 0;
        for(String snf : snafuNums)
            num += snafuToTenBase(snf);
        String fiveBased = tenBaseTo5Base(num);
        String snafu = fiveBaseToSnafu(fiveBased);

        setAnswerPartOne(snafu);
    }

    private long snafuToTenBase(String snafu) {
        long num = 0L;
        int l = snafu.length();
        for(int i = 0; i < l; i++) {
            num += snafuBit(snafu.charAt(l - i - 1)) * Math.pow(5, i);
        }
        return num;
    }

    private int snafuBit(char d) {
        switch (d) {
            case '=' : return -2;
            case '-' : return -1;
            case '0' : return 0;
            case '1' : return 1;
            case '2' : return 2;
            default: return -434232423;
        }
    }

    private String tenBaseTo5Base(long tenBased) {
        StringBuilder fiveBased = new StringBuilder();
        while (tenBased > 0) {
            fiveBased.insert(0, tenBased % 5);
            tenBased /= 5;
        }
        return String.valueOf(fiveBased);
    }

    private String fiveBaseToSnafu(String fiveBased) {
        int transToNext = 0;
        StringBuilder snafuNum = new StringBuilder();
        int l = fiveBased.length();
        for(int i = l-1; i >= 0; i--) {
            int d = Integer.parseInt(fiveBased.substring(i, i+1));
            d += transToNext;
            transToNext = 0;
            switch (d) {
                case 0:
                case 1:
                case 2: snafuNum.insert(0, d);
                break;
                case 3: snafuNum.insert(0, "=");
                        transToNext++;
                        break;
                case 4: snafuNum.insert(0, "-");
                        transToNext++;
                        break;
                case 5: snafuNum.insert(0,"0");
                        transToNext++;
                        break;
            }

        }
        return String.valueOf(snafuNum);
    }
}
