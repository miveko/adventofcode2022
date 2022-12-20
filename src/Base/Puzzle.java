package Base;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;
import java.util.Scanner;

abstract public class Puzzle {
    private final String className;
    private long time;
    protected void setAnswerPartOne(String answerPartOne) {
        long interval = System.currentTimeMillis() - time;
        if(interval > 10000)
            System.out.printf("PartOne: %s\tFound in %d seconds%n", answerPartOne, (interval /1000));
        else
            System.out.printf("PartOne: %s\tFound in %d miliseconds%n", answerPartOne, interval);
        time = System.currentTimeMillis();
    }

    protected void setAnswerPartTwo(String answerPartTwo) {
        long interval = System.currentTimeMillis() - time;
        if(interval > 10000)
            System.out.printf("PartTwo: %s\tFound in %d seconds%n", answerPartTwo, (interval /1000));
        else
            System.out.printf("PartTwo: %s\tFound in %d miliseconds%n", answerPartTwo, interval);
        time = System.currentTimeMillis();
    }
    public Puzzle() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        className = stackTrace[stackTrace.length - 1].getClassName();
    }
    public void execute(String[] args) {
        if(args.length == 0) {
            args = new String[] {"basic.input", "main.input"};
        }

        for(String inputFile : args) {
            System.out.println("Input: " + inputFile);
            Scanner inputFileReader = getInputFileReader(inputFile);
            time = System.currentTimeMillis();
            solution(inputFileReader);
        }
    }

    private Scanner getInputFileReader(String inputFile) {

        String classFileName = className.substring(className.lastIndexOf(".") + 1) + ".class";
        FileInputStream fileInputStream = null;
        try {
            Class<?> callerClass = Class.forName(className);
            String fullPath = Objects.requireNonNull(callerClass.getResource(classFileName)).getPath();
            fullPath = fullPath.replace(classFileName, "");
            fileInputStream = new FileInputStream(fullPath + inputFile);
        } catch (Exception e) {
            System.err.println("File '" + inputFile + "' not found!");
            System.out.println("Creating an empty input file");
        }

        if(fileInputStream == null) {
            try {
                File emptyFile = new File("empty.input");
                emptyFile.createNewFile();
                fileInputStream = new FileInputStream(emptyFile);
            } catch (Exception e) {
                System.err.println("Unable to create 'empty.input' file!" + System.lineSeparator()
                        + "Please check permissions!");
                System.out.println("Returning null as fileInputStream!");
            }
        }
        return new Scanner(fileInputStream);
    }

    protected abstract void solution(Scanner inputReader);

}
