import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GUI {

    // Back End (solver)
    static BackEnd solver = new BackEnd();

    // Gui Components
    private JPanel pnlMain;
    private JLabel checkTag;
    private JTextField[][] textFields = new JTextField[9][9];

    // Game Components
    static int[][] board;
    private ArrayList<Pair> wrongTiles = new ArrayList<>();

    public GUI() {
        pnlMain = new JPanel();
        pnlMain.setLayout(new GridBagLayout());

        createPanel();
    }

    public void createPanel() {
        pnlMain.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();

        // if(solver.hasWon(board)){

        // }

        // Check Tag
        // GBC
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 9;
        // Component
        checkTag = new JLabel(" ");
        checkTag.setFont(new Font("Courier", Font.PLAIN, 40));
        checkTag.setForeground(Color.white);
        // Composing Element
        pnlMain.add(checkTag, gbc);

        // Creating Board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // GBC
                gbc = new GridBagConstraints();
                gbc.gridx = j;
                gbc.gridy = i + 1;
                // Component
                JTextField square = new JTextField(board[i][j] == -1 ? " " : "" + board[i][j]);

                if (pairListContains(wrongTiles, new Pair(j, i)))
                    square.setBackground(Color.RED);
                else if ((i / 3 == 1 && j / 3 == 1) || (i / 3 % 2 == 0 && j / 3 % 2 == 0))
                    square.setBackground(Color.GRAY);
                else
                    square.setBackground(Color.LIGHT_GRAY);
                square.setOpaque(true);
                square.setFont(new Font("Courier", Font.PLAIN, 40));
                square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                textFields[i][j] = square;
                // Composing element
                pnlMain.add(textFields[i][j], gbc);
            }
        }

        // Check Button
        // GBC
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        // Component
        JButton checkBtn = new JButton("Check");
        checkBtn.setOpaque(true);
        checkBtn.setBackground(Color.GREEN);
        checkBtn.setFont(new Font("Courier", Font.PLAIN, 30));
        checkBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Event Listener
        checkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wrongTiles.clear();
                wrongTiles = solver.checkBoard(board);
                trimWrongs();
                board = solver.scanBoard(textFields);
                createPanel();
                checkTag.setText(wrongTiles.size() == 0 ? "Correct!" : "Wrong!");
            }
        });
        // Composing Element
        pnlMain.add(checkBtn, gbc);

        // Solve Button
        // GBC
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 10;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        // Component
        JButton solveBtn = new JButton("Solve");
        solveBtn.setOpaque(true);
        solveBtn.setBackground(Color.RED);
        solveBtn.setFont(new Font("Courier", Font.PLAIN, 30));
        solveBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Event Listener
        solveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 10; i++)
                    board = solver.solve(board);
                createPanel();
            }
        });
        // Composing Element
        pnlMain.add(solveBtn, gbc);

        pnlMain.revalidate();
    }

    public boolean pairListContains(ArrayList<Pair> list, Pair p) {
        for (Pair e : list)
            if (e.x == p.x && e.y == p.y)
                return true;
        return false;
    }

    public void trimWrongs() {
        ArrayList<Pair> newList = new ArrayList<>();
        for (Pair e : wrongTiles)
            if (!pairListContains(newList, e))
                newList.add(e);
        wrongTiles = newList;
    }

    public JPanel getUI() {
        return pnlMain;
    }

    public static void main(String[] args) throws FileNotFoundException {
        board = solver.startBoard();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Sudoku");
                frame.getContentPane().add(new GUI().getUI()).setBackground(Color.DARK_GRAY);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            }
        });
    }

}