package Day24;

import Base.Puzzle;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Day24_BlizzardBasin extends Puzzle {

    List<Blizzard> blizzard;
    Blizzard me;
    List<String> unUsedOptions;
    Set<Point> stormed;
    Set<Point>  reached;
    int minute, width, height;
    Map<Integer, List<Blizzard>> blizzardsState;
    Map<Integer, Point> myState;
    String path;

    public static void main(String[] args) {
        Puzzle puzzle = new Day24_BlizzardBasin();
        puzzle.execute(args);
    }

    @Override
    protected void solution(Scanner inputReader) {
        blizzard = new ArrayList<>();
        width = inputReader.nextLine().length();
        height = 1;
        while (inputReader.hasNextLine()) {
            String line = inputReader.nextLine();
            for(int j = 0; j < line.length(); j++)
                if(line.charAt(j) != '#' && line.charAt(j) != '.')
                    blizzard.add(new Blizzard(new Point(height, j), line.charAt(j)));
            height++;
        }

        Point start = new Point(0, 1);
        Point finish = new Point(height - 1, width - 2);
        int minutes = findShortestTime(start, finish);
        setAnswerPartOne(String.valueOf(minutes));

        minutes += findShortestTime(finish, start);
        minutes += findShortestTime(start, finish);
        setAnswerPartTwo(String.valueOf(minutes));
    }

    private int findShortestTime(Point start, Point finish) {
        reached = new HashSet<>();
        reached.add(new Point(start));
        minute = 0;
        while (!reached.contains(finish)) {
            minute++;
            updateBlizzardsPositions();
            Set<Point> lastReached = reached;
            reached = new HashSet<>();
            reached.add(new Point(start));
            for(Point p : lastReached)
                updateReachedList(p, finish);
        }
        return minute;
    }

    private void updateBlizzardsPositions() {
        stormed = new HashSet<>();
        for(Blizzard bl : blizzard) {
            stormed.add(bl.update(width, height));
        }
    }

    private void updateReachedList(Point pos, Point destination) {
        Point nextPoint = new Point(pos.x + 1, pos.y);
        if(nextPoint.x == destination.x && nextPoint.y == destination.y) {
            reached.add(new Point(destination.x, destination.y)); //getting to the Final destination
            return;
        }

        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1)    //check if downwards move is possible;
            reached.add(new Point(nextPoint));

        nextPoint.x--; nextPoint.y++;
        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1)
            reached.add(new Point(nextPoint));

        nextPoint.y--;
        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1)
            reached.add(new Point(nextPoint));

        nextPoint.x--;
        if(nextPoint.x == destination.x && nextPoint.y == destination.y) {
            reached.add(new Point(destination.x, destination.y)); //getting to the Final destination
            return;
        }
        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1) {
            reached.add(new Point(nextPoint));
        }

        nextPoint.x++;nextPoint.y--;
        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1)
            reached.add(new Point(nextPoint));
    }

    private char updatePossibleMove(char nextMove, char possibleMove) {
        if(nextMove == ' ')
            nextMove = possibleMove;
        else {
            String lastOptions = "";
            if(unUsedOptions.size() > 0)
                lastOptions = unUsedOptions.get(unUsedOptions.size() - 1);
            if(lastOptions.startsWith(String.valueOf(minute))) {
                lastOptions += possibleMove;
                unUsedOptions.set(unUsedOptions.size() - 1, lastOptions);
            } else {
                unUsedOptions.add(String.valueOf(minute) + ':' + possibleMove);
                blizzardsState.put(minute, new ArrayList<>());
                myState.put(minute, new Point(me.location));
                for(Blizzard bl : blizzard)
                    blizzardsState.get(minute).add(new Blizzard(bl));
            }
        }

        return nextMove;
    }

    //Next three methods compose the brute force solution (high complexity)
    private int findBestPath_bruteForce() {
        blizzardsState = new HashMap<>();
        myState = new HashMap<>();
        minute = 0;
        me = new Blizzard(new Point(0, 1), ' ');
        unUsedOptions = new ArrayList<>();
        int bestCompletion = Integer.MAX_VALUE;
        while (me.getX() != height - 1 || me.getY() != width - 2 || unUsedOptions.size() > 0) {
            minute++;
            updateBlizzardsPositions();
            char nxt = processPossibleMoves();
            if(nxt != ' ') {
                me.update(nxt);
                path += nxt;
            } else if(unUsedOptions.size() > 0) {
                revertToAnotherOption();
            } else
                break;
            //check if not get to the Final
            if(me.getX() == height - 1 && me.getY() == width - 2) {
                System.out.println(minute + ": " + me.getX() + " " + me.getY());
                System.out.println(path);
                if(minute < bestCompletion)
                    bestCompletion = minute;
                if(unUsedOptions.size() > 0)
                    revertToAnotherOption();
            }
            //clear unnecessary/useless options
            if(minute >= bestCompletion - 1 - (height - 1 - me.getX()) - (width - 2 - me.getY())) {
                for(int i = unUsedOptions.size() - 1; i >= 0; i--) {
                    int uselessMinute = Integer.parseInt(unUsedOptions.get(i).split(":")[0]);
                    if(uselessMinute > bestCompletion - 1 - (height - 1 - myState.get(uselessMinute).x) - (width - 2 - myState.get(uselessMinute).y)) {
                        myState.remove(uselessMinute);
                        blizzardsState.remove(uselessMinute);
                        unUsedOptions.remove(i);
                    } else break;
                }
                if(unUsedOptions.size() > 0)
                    revertToAnotherOption();
                else break;
            }
        }
        return bestCompletion;
    }

    private char processPossibleMoves() {
        char nextMove = ' ';
        Point nextPoint = new Point(me.getX() + 1, me.getY());
        if(nextPoint.x == height - 1 && nextPoint.y == width - 2)
            return 'v'; //getting to the Final destination
        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1)    //check if downwards move is possible;
            nextMove = 'v';
        if(me.getX() == 0 && me.getY() == 1)
            nextMove = updatePossibleMove(nextMove, 'x');

        nextPoint.x--; nextPoint.y++;
        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1)
            nextMove = updatePossibleMove(nextMove, '>');

        nextPoint.y--;
        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1)
            nextMove = updatePossibleMove(nextMove, 'x');

        nextPoint.x--;
        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1)
            nextMove = updatePossibleMove(nextMove, '^');

        nextPoint.x++;nextPoint.y--;
        if(!stormed.contains(nextPoint) && nextPoint.x > 0 && nextPoint.y > 0 && nextPoint.x < height - 1 && nextPoint.y < width - 1)
            nextMove = updatePossibleMove(nextMove, '<');

        return nextMove;
    }

    private void revertToAnotherOption() {
        String lastOptions = unUsedOptions.get(unUsedOptions.size() - 1);
        minute = Integer.parseInt(lastOptions.split(":")[0]);
        path = path.substring(0, minute - 1);
        char option = lastOptions.charAt(lastOptions.length() - 1);
        me.location.setLocation(myState.get(minute));
        me.update(option);
        path += option;
        blizzard = blizzardsState.get(minute);
        lastOptions = lastOptions.substring(0, lastOptions.length() - 1);
        if(lastOptions.split(":", -1)[1].length() == 0) {   //no more options for this minute
            unUsedOptions.remove(unUsedOptions.size() - 1);
            blizzardsState.remove(minute);
            myState.remove(minute);
        } else {
            unUsedOptions.set(unUsedOptions.size() - 1, lastOptions);
        }
    }

    //TODO: find the exact shortest path using the updateReachedList() method
    private int findBestPath_iterativeNextToTheLast() {
        return 0;
    }
}
