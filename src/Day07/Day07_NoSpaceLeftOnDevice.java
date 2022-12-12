package Day07;

import Base.Puzzle;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day07_NoSpaceLeftOnDevice  extends Puzzle {

    protected static String currentDir;
    static String line;
    static Scanner inputReader;
    static Map<String, Integer> files;
    final static long FOLDER_SIZE_TRESHOLD = 100000;
    final static long TOTAL_SIZE = 70000000;
    final static long REQUIRED_SIZE = 30000000;

    public static void main(String[] args) {
        Puzzle puzzle = new Day07_NoSpaceLeftOnDevice();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        currentDir = "";
        files = new HashMap<>();
        Day07_NoSpaceLeftOnDevice.inputReader = inputReader;
        line = inputReader.nextLine();
        do {
            String[] command = line.split(" ");
            switch (command[1]) {
                case "cd" : commandChangeDir(command);
                    break;
                case "ls" : commandList();
                    break;
            }
        } while (inputReader.hasNextLine());

        long foldersUnderThreshTotalSize = 0;
        long requiredSpaceToFree = Math.abs(TOTAL_SIZE - REQUIRED_SIZE - getFolderSize("/"));
        long directoryToDeleteSize = Long.MAX_VALUE;
        for(Map.Entry<String, Integer> entry : files.entrySet()) {
            if (entry.getValue() == 0) {
                long size = getFolderSize(entry.getKey());
                if(size < FOLDER_SIZE_TRESHOLD)
                    foldersUnderThreshTotalSize += getFolderSize(entry.getKey());
                if(size > requiredSpaceToFree && size < directoryToDeleteSize)
                    directoryToDeleteSize = size;
            }
        }

        setAnswerPartOne(String.valueOf(foldersUnderThreshTotalSize));
        setAnswerPartTwo(String.valueOf(directoryToDeleteSize));
    }

    private static void commandChangeDir(String[] expression) {
        String arg0 = expression[2];
        switch (arg0) {
            case ".." : changeDirToUpper();
                break;
            case "/"  :
                break;
            default:
                changeDirToAnother(expression[2]);
                break;
        }

        line = inputReader.nextLine();
    }

    private static void changeDirToUpper() {
        if(!currentDir.equals("/")) {
            currentDir = currentDir.substring(0, currentDir.lastIndexOf("/"));
        }
    }

    private static void changeDirToAnother(String arg) {
        currentDir = currentDir.endsWith("/") ? currentDir + arg : currentDir + "/" + arg;
    }


    private static void commandList() {
        line = inputReader.nextLine();
        while(line.charAt(0) != '$') {
            addFile(line.split(" "));
            if(inputReader.hasNextLine())
                line = inputReader.nextLine();
            else break;
        }
    }

    private static void addFile(String[] args) {
        String file = currentDir.endsWith("/") ? currentDir : currentDir + "/";
        file += args[1];
        //if directory, set size of 0; else put the size specified
        files.put(file, (args[0].equals("dir") ? 0 : Integer.parseInt(args[0])));
    }

    private static long getFolderSize(String path) {
        long size = 0;
        for (String filepath : files.keySet() ) {
            if(filepath.startsWith(path)) {
                size += files.get(filepath);
            }
        }

        return size;
    }
}
