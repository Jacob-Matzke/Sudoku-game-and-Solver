import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTextField;

public class BackEnd {

    public int[][] startBoard() throws FileNotFoundException {
        Scanner read = new Scanner(new File("Puzzle3.txt"));
        int[][] board = new int[9][9];

        for (int i = 0; i < 9; i++) {
            String line = read.nextLine();
            for (int j = 0; j < 9; j++)
                board[i][j] = line.charAt(j) == 88 ? 0 : line.charAt(j) - 48;
        }

        read.close();
        return board;
    }

    public static int[][] genBoard() {
        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 5;
            }
        }
        return board;
    }

    public static int[][] scanBoard(JTextField[][] textFields) {
        String numbs = "123456789";
        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = textFields[i][j].getText();
                if (!numbs.contains("" + text.charAt(0)))
                    board[i][j] = 0;
                else
                    board[i][j] = Integer.parseInt("" + text.charAt(0));
            }
        }
        return board;
    }

    public static int[][] solve(int[][] board) {
        ArrayList<Integer>[][] valids = validNumbs(board);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (valids[i][j].size() == 1)
                    board[i][j] = valids[i][j].get(0);
            }
        }
        return board;
    }

    public static ArrayList<Pair> checkBoard(int[][] board) {
        ArrayList<Pair> wrongTiles = new ArrayList<>();

        // Linear Scan
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int numb = board[i][j];
                if (numb == 0)
                    continue;

                for (int k = 1; k < j && j + k < 9; k++) {
                    if (board[i][j + k] == numb) {
                        wrongTiles.add(new Pair(j + k, i));
                        wrongTiles.add(new Pair(j, i));
                    }
                }
                for (int k = j + 1; k + j < 9; k++) {
                    if (board[i][j + k] == numb) {
                        wrongTiles.add(new Pair(j + k, i));
                        wrongTiles.add(new Pair(j, i));
                    }
                }
                for (int k = 1; k < i && i + k < 9; k++) {
                    if (board[i + k][j] == numb) {
                        wrongTiles.add(new Pair(j, i + k));
                        wrongTiles.add(new Pair(j, i));
                    }
                }
                for (int k = i + 1; k + i < 9; k++) {
                    if (board[i + k][j] == numb) {
                        wrongTiles.add(new Pair(j, i + k));
                        wrongTiles.add(new Pair(j, i));
                    }
                }
            }
        }

        // Orbital Scan
        // Will capture all 9 clusters
        for (

                int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Inner 9 cells
                // Will capture all members of cluster with x and y
                int[] count = new int[9];
                for (int y = i * 3; y < (i + 1) * 3; y++) {
                    for (int x = j * 3; x < (j + 1) * 3; x++) {
                        int numb = board[y][x];
                        if (numb == 0)
                            continue;

                        if (++count[board[y][x] - 1] == 2)
                            wrongTiles.add(new Pair(x, y));
                    }
                }
            }
        }

        return wrongTiles;
    }

    public static ArrayList<Integer>[][] validNumbs(int[][] board) {
        System.out.println("Board");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j]);
                if (j != 8)
                    System.out.print(" | ");
            }
            System.out.println();
        }
        System.out.println();

        // Setting up return
        ArrayList<Integer>[][] valids = new ArrayList[9][9];

        // Single Number Check
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                ArrayList<Integer> numbs = new ArrayList<>();
                for (int k = 1; k <= 9; k++)
                    numbs.add(k);
                valids[i][j] = numbs;
            }
        }

        System.out.println("Single");
        printValids(valids);
        System.out.println();

        // Linear Scan
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int numb = board[i][j];
                if (numb == 0)
                    continue;
                valids[i][j] = removeExcept(valids[i][j], numb);
                for (int k = 0; k < i; k++) {
                    int index = valids[k][j].indexOf(numb);
                    if (index != -1)
                        valids[k][j].remove(index);
                }
                for (int k = i + 1; k < 9; k++) {
                    int index = valids[k][j].indexOf(numb);
                    if (index != -1)
                        valids[k][j].remove(index);
                }
                for (int k = 0; k < j; k++) {
                    int index = valids[i][k].indexOf(numb);
                    if (index != -1)
                        valids[i][k].remove(index);
                }
                for (int k = j + 1; k < 9; k++) {
                    int index = valids[i][k].indexOf(numb);
                    if (index != -1)
                        valids[i][k].remove(index);
                }
            }
        }

        System.out.println("Linear");
        printValids(valids);
        System.out.println();

        // Orbital Scan
        ArrayList<Integer> clusterNumbs = new ArrayList<>();
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                clusterNumbs.clear();
                for (int y = 0; y < 3; y++)
                    for (int x = 0; x < 3; x++)
                        if (board[i + y][j + x] != 0)
                            clusterNumbs.add(board[i + y][j + x]);

                System.out.println(clusterNumbs);
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        int newY = i + y;
                        int newX = j + x;
                        if (valids[newY][newX].size() == 1)
                            continue;
                        valids[newY][newX].removeAll(clusterNumbs);
                    }
                }
            }
        }

        System.out.println("Orbital");
        printValids(valids);
        System.out.println();

        // Unique in Cluster
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        int newY = i + y;
                        int newX = j + x;
                        if (valids[newY][newX].size() == 1)
                            continue;
                        for (int numb : valids[newY][newX]) {
                            boolean isUnique = true;
                            for (int iY = 0; iY < 3; iY++) {
                                for (int iX = 0; iX < 3; iX++) {
                                    if (iY == y && iX == x)
                                        continue;
                                    if (valids[iY][iX].contains(numb))
                                        isUnique = false;
                                }
                            }
                            if (isUnique) {
                                valids[y][x] = removeExcept(valids[y][x], numb);
                                break;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Cluster");
        printValids(valids);
        System.out.println();

        return valids;

    }

    public static void printValids(ArrayList<Integer>[][] valids) {
        for (ArrayList<Integer>[] y : valids) {
            for (int i = 0; i < y.length; i++) {
                System.out.print(y[i]);
                if (i != y.length - 1)
                    System.out.print(" | ");
            }
            System.out.println();
        }
    }

    public static boolean hasWon(int[][] board) {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0)
                    return false;
        return true;
    }

    public static ArrayList<Integer> removeExcept(ArrayList<Integer> list, int except) {
        for (int i = 0; i < list.size();) {
            if (list.get(i) == except)
                i++;
            else
                list.remove(i);
        }
        return list;
    }

    public static void main(String[] args) {
    }
}
