package Day18;

import Base.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day18_BoilingBoulders extends Puzzle {

    boolean[][][] cubes;
    int minX, maxX, minY, maxY, minZ, maxZ;


    public static void main(String[] args) {
        Puzzle puzzle = new Day18_BoilingBoulders();
        puzzle.execute(args);
    }

    @Override
    protected void solution(Scanner inputReader) {
        List<Integer[]> coordinates = new ArrayList<>();
        minX = minY = minZ = Integer.MAX_VALUE;
        maxX = maxY = maxZ = Integer.MIN_VALUE;
        while(inputReader.hasNextLine()) {  //
            String[] line = inputReader.nextLine().split(",");
            int x = Integer.parseInt(line[0]);
            int y = Integer.parseInt(line[1]);
            int z = Integer.parseInt(line[2]);
            coordinates.add(new Integer[]{x, y, z});
            if(x > maxX) maxX = x;
            if(x < minX) minX = x;
            if(y > maxY) maxY = y;
            if(y < minY) minY = y;
            if(z > maxZ) maxZ = z;
            if(z < minZ) minZ = z;
        }
        //Adding 3 to each coordinate: 1 for getting the number and 2 for including the outer of each side
        cubes = new boolean[maxX - minX + 3][maxY - minY + 3][maxZ - minZ + 3];
        //setting the cubes in the space
        for(Integer[] crdt : coordinates) {
            cubes[crdt[0] - minX + 1][crdt[1] - minY + 1][crdt[2] - minZ + 1] = true;
        }

        setAnswerPartOne(String.valueOf(calcSurface()));

        //converting inner empty space (bubbles) into cubes of lava
        for (int x = 1; x <=cubes.length-2; x++)
            for (int y = 1; y <=cubes[x].length-2; y++)
                for (int z = 1; z <=cubes[x][y].length-2; z++) {
                    boolean[][][] checked = new boolean[maxX - minX + 3][maxY - minY + 3][maxZ - minZ + 3];
                    if(!cubes[x][y][z] && isPartOfBuble(x, y, z, checked))
                        cubes[x][y][z] = true;
                }

        setAnswerPartTwo(String.valueOf(calcSurface()));
    }

    //calculating the surface checking if there is not adjacent cubes around each one
    private int calcSurface() {
        int surface = 0;
        for (int x = 1; x <=cubes.length-2; x++)
            for (int y = 1; y <=cubes[x].length-2; y++)
                for (int z = 1; z <=cubes[x][y].length-2; z++)
                    if(cubes[x][y][z])  {
                        if(!cubes[x - 1][y][z]) surface++;
                        if(!cubes[x + 1][y][z]) surface++;
                        if(!cubes[x][y - 1][z]) surface++;
                        if(!cubes[x][y + 1][z]) surface++;
                        if(!cubes[x][y][z - 1]) surface++;
                        if(!cubes[x][y][z + 1]) surface++;
                    }
        return surface;
    }

    private boolean isPartOfBuble(int x, int y, int z, boolean[][][] checked) {
        if(x == 0 || x == checked.length - 1 || y == 0 || y == checked[x].length - 1 || z == 0 || z == checked[x][y].length - 1)
            return false;
        checked[x][y][z] = true;
        cubes[x][y][z] = (cubes[x - 1][y][z] || checked[x - 1][y][z] || isPartOfBuble(x - 1, y, z, checked))
                && (cubes[x + 1][y][z] || checked[x + 1][y][z] || isPartOfBuble(x + 1, y, z, checked))
                && (cubes[x][y - 1][z] || checked[x][y - 1][z] || isPartOfBuble(x, y - 1, z, checked))
                && (cubes[x][y + 1][z] || checked[x][y + 1][z] || isPartOfBuble(x, y + 1, z, checked))
                && (cubes[x][y][z - 1] || checked[x][y][z - 1] || isPartOfBuble(x, y, z - 1, checked))
                && (cubes[x][y][z + 1] || checked[x][y][z + 1] || isPartOfBuble(x, y, z + 1, checked));
        return cubes[x][y][z];
    }
}
