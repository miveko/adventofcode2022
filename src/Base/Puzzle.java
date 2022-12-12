package Base;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;
import java.util.Scanner;

abstract public class Puzzle {
    private final String className;
    private String answerPartOne;
    private String answerPartTwo;
    protected void setAnswerPartOne(String answerPartOne) {
        this.answerPartOne = answerPartOne;
    }

    protected void setAnswerPartTwo(String answerPartTwo) {
        this.answerPartTwo = answerPartTwo;
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
            solution(inputFileReader);
            showResults();
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

    protected void showResults() {
        System.out.println("PartOne: " + answerPartOne);
        System.out.println("PartTwo: " + answerPartTwo);
    }
}
