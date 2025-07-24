import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Sudoku extends Frame implements ActionListener {
    int[][] board = new int[9][9];
    TextField t[][] = new TextField[9][9];
    Panel p = new Panel();
    Button solveButton = new Button("Solve");
    Button clearButton = new Button("Clear");
    Button submitButton = new Button("Submit");
    Button newPuzzleButton = new Button("New Puzzle");
    Label messageLabel = new Label("Welcome to Sudoku Solver!");

    Sudoku() {
        super("Sudoku Solver");
        setSize(500, 550);
        setLayout(new BorderLayout());

        p.setLayout(new GridLayout(9, 9));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                t[i][j] = new TextField();
                p.add(t[i][j]);
            }
        }
        add(p, BorderLayout.CENTER);

        Panel buttonsPanel = new Panel();
        buttonsPanel.add(solveButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(submitButton);
        buttonsPanel.add(newPuzzleButton);
        add(buttonsPanel, BorderLayout.SOUTH);

        add(messageLabel, BorderLayout.NORTH);

        solveButton.addActionListener(this);
        clearButton.addActionListener(e -> clearBoard());
        submitButton.addActionListener(e -> checkAndSubmit());
        newPuzzleButton.addActionListener(e -> generateNewPuzzle());

        setVisible(true);
        generateNewPuzzle(); 

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                dispose();
            }
        });
    }

    public void takeInput() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String s = t[i][j].getText();
                if (s.length() > 0) {
                    try {
                        board[i][j] = Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        board[i][j] = 0;
                    }
                } else {
                    board[i][j] = 0;
                }
            }
        }
    }

    public void print() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    t[i][j].setText(board[i][j] + "");
                    t[i][j].setEditable(false);
                    t[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    t[i][j].setText("");
                    t[i][j].setEditable(true);
                    t[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    public boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSafe(int[][] board, int row, int col, int num) {
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num || board[x][col] == num) {
                return false;
            }
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clearBoard() {
           for (int i = 0; i < 9; i++) {
               for (int j = 0; j < 9; j++) {        
                   if (t[i][j].isEditable()) {      
                       t[i][j].setText("");
                       board[i][j] =0;
                   }
               }
           }
           messageLabel.setText("Inputs cleared. Predefined numbers remain.");
       }

    public boolean isValidBoard() {
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10];
            for (int col = 0; col < 9; col++) {
                if (board[row][col] != 0) {
                    if (seen[board[row][col]]) return false;
                    seen[board[row][col]] = true;
                }
            }
        }

        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];
            for (int row = 0; row < 9; row++) {
                if (board[row][col] != 0) {
                    if (seen[board[row][col]]) return false;
                    seen[board[row][col]] = true;
                }
            }
        }

        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                boolean[] seen = new boolean[10];
                for (int r = row; r < row + 3; r++) {
                    for (int c = col; c < col + 3; c++) {
                        if (board[r][c] != 0) {
                            if (seen[board[r][c]]) return false;
                            seen[board[r][c]] = true;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void checkAndSubmit() {
        takeInput();
        if (isValidBoard()) {
            showDialog("The solution is valid!");
        } else {
            showDialog("The solution is invalid!");
        }
    }

    public void showDialog(String message) {
        Dialog dialog = new Dialog(this, "Validation Result", true);
        dialog.setLayout(new FlowLayout());
        Label label = new Label(message);
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.setVisible(false));
        dialog.add(label);
        dialog.add(okButton);
        dialog.setSize(250, 100);
        dialog.setVisible(true);
    }

    public void generateNewPuzzle() {
        Random random = new Random();

        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0;
            }
        }
        for (int i = 0; i < 9; i++) {
            int numbersToPlace = random.nextInt(4) + 1; 
            for (int j = 0; j < numbersToPlace; j++) {
                int col = random.nextInt(9);
                int num = random.nextInt(9) + 1;
                if (isSafe(board, i, col, num)) {
                    board[i][col] = num;
                }
            }
        }

        print();
        messageLabel.setText("New Sudoku puzzle generated!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        takeInput();
        if (solveSudoku(board)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    t[i][j].setText(board[i][j] + "");
                }
            }
            messageLabel.setText("Sudoku solved successfully!");
        } else {
            messageLabel.setText("No solution exists.");
        }
    }

    public static void main(String[] args) {
        new Sudoku();
    }
}