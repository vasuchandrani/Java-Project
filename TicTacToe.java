import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends Frame implements ActionListener {
    
    Button[] buttons = new Button[9]; 

    // true for Player 1 (X)
    // false for Player 2 (O)
    boolean playerTurn = true;  
    String[] board = new String[9]; 
    
    // To show status of the game
    Label statusLabel; 

    public TicTacToe() {

        // Set up the frame
        setTitle("Tic-Tac-Toe Game");
        setSize(500, 500);
        setLayout(new GridLayout(4, 3)); // 3x3 grid + status bar

        // Initialize the buttons and add them to the frame
        for (int i = 0; i < 9; i++) {
            buttons[i] = new Button("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }


        statusLabel = new Label("Player 1 (X) Turn");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(statusLabel);

        // Set default close operation and show the frame
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Button clickedButton = (Button)e.getSource();

        // Find the button index
        int index = -1;
        for (int i = 0; i < 9; i++) {
            if (buttons[i] == clickedButton) {
                index = i;
                break;
            }
        }

        // If the cell is not already taken
        if (buttons[index].getLabel().equals("")) {
            // true for "X"
            if (playerTurn) {
                buttons[index].setLabel("X");
                board[index] = "X";
                statusLabel.setText("Player 2 (O) Turn");
            } 
            // false for "O"
            else {
                buttons[index].setLabel("O");
                board[index] = "O";
                statusLabel.setText("Player 1 (X) Turn");
            }

            // Check for a win or a draw
            if (checkWin()) {
                statusLabel.setText("Player " + (playerTurn ? "1 (X)" : "2 (O)") + " Wins!");
                disableButtons();
            } 
            else if (isDraw()) {
                statusLabel.setText("It's a Draw!");
            }

            // Switch turn
            playerTurn = !playerTurn;
        }
    }


    // check for win
    private boolean checkWin() {

        // Winning combinations
        int[][] winCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] combo : winCombinations) {
            if (board[combo[0]] != null && board[combo[0]].equals(board[combo[1]]) && board[combo[0]].equals(board[combo[2]])) {
                return true;
            }
        }
        return false;

    }

    // check for draw
    private boolean isDraw() {

        for (int i = 0; i < 9; i++) {
            if (board[i] == null) {
                return false;
            }
        }
        return true;

    }

    // Disable all buttons once the game is over
    private void disableButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i].setEnabled(false);
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        new TicTacToe();
    }
}   