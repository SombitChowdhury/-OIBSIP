import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberGuessingGame extends JFrame {
    private int randomNumber;
    private int attemptsLeft;
    private int score;
    private int round;
    private final int MAX_ATTEMPTS = 5;
    private final int MAX_ROUNDS = 3;
    
    // UI Components
    private JLabel titleLabel;
    private JLabel promptLabel;
    private JTextField guessField;
    private JButton submitButton;
    private JButton newGameButton;
    private JLabel attemptsLabel;
    private JLabel roundLabel;
    private JLabel scoreLabel;
    private JTextArea historyArea;
    
    public NumberGuessingGame() {
    
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
       
        initGame();
        initUI();
        
        // Start the first round
        startNewRound();
    }
    
    private void initGame() {
        score = 0;
        round = 0;
        attemptsLeft = MAX_ATTEMPTS;
    }
    
    private void initUI() {
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        
        titleLabel = new JLabel("Number Guessing Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
       
        promptLabel = new JLabel("I'm thinking of a number between 1 and 100.", JLabel.CENTER);
        promptLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(promptLabel, gbc);
        
        
        attemptsLabel = new JLabel("Attempts left: " + attemptsLeft, JLabel.CENTER);
        gbc.gridy = 1;
        centerPanel.add(attemptsLabel, gbc);
        
       
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        infoPanel.setBackground(new Color(240, 240, 240));
        roundLabel = new JLabel("Round: " + (round + 1) + "/" + MAX_ROUNDS);
        scoreLabel = new JLabel("Score: " + score);
        infoPanel.add(roundLabel);
        infoPanel.add(scoreLabel);
        gbc.gridy = 2;
        centerPanel.add(infoPanel, gbc);
        
        
        guessField = new JTextField(10);
        guessField.setFont(new Font("Arial", Font.PLAIN, 16));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.addActionListener(new SubmitActionListener());
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        centerPanel.add(guessField, gbc);
        
        
        submitButton = new JButton("Submit Guess");
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(new SubmitActionListener());
        gbc.gridx = 1;
        centerPanel.add(submitButton, gbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
       
        historyArea = new JTextArea(8, 30);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setText("Game History:\n");
        JScrollPane scrollPane = new JScrollPane(historyArea);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        
        newGameButton = new JButton("New Game");
        newGameButton.setBackground(new Color(70, 130, 180));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.addActionListener(e -> startNewGame());
        mainPanel.add(newGameButton, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void startNewGame() {
        score = 0;
        round = 0;
        historyArea.setText("Game History:\n");
        startNewRound();
    }
    
    private void startNewRound() {
        if (round >= MAX_ROUNDS) {
            endGame();
            return;
        }
        
        round++;
        attemptsLeft = MAX_ATTEMPTS;
        Random rand = new Random();
        randomNumber = rand.nextInt(100) + 1;
        
        updateUI();
        guessField.setText("");
        guessField.requestFocus();
        
        historyArea.append("Round " + round + " started. Good luck!\n");
    }
    
    private void processGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            
            if (guess < 1 || guess > 100) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a number between 1 and 100.", 
                    "Invalid Input", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            attemptsLeft--;
            historyArea.append("Guess: " + guess + " - ");
            
            if (guess == randomNumber) {
                
                int pointsEarned = attemptsLeft + 1; 
                score += pointsEarned;
                
                historyArea.append("Correct! +" + pointsEarned + " points\n");
                JOptionPane.showMessageDialog(this, 
                    "Congratulations! You guessed the number " + randomNumber + 
                    " and earned " + pointsEarned + " points.", 
                    "You Win!", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                startNewRound();
            } else if (attemptsLeft <= 0) {
               
                historyArea.append("Out of attempts. The number was " + randomNumber + "\n");
                JOptionPane.showMessageDialog(this, 
                    "Sorry, you're out of attempts. The number was " + randomNumber + ".", 
                    "Round Over", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                startNewRound();
            } else {
               
                String hint = guess < randomNumber ? "higher" : "lower";
                historyArea.append("Try " + hint + ". Attempts left: " + attemptsLeft + "\n");
                
                JOptionPane.showMessageDialog(this, 
                    "Try " + hint + "! Attempts left: " + attemptsLeft, 
                    "Hint", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                updateUI();
                guessField.setText("");
                guessField.requestFocus();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number.", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateUI() {
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        roundLabel.setText("Round: " + round + "/" + MAX_ROUNDS);
        scoreLabel.setText("Score: " + score);
    }
    
    private void endGame() {
        String message;
        if (score >= 10) {
            message = "Excellent playing! Your final score is: " + score;
        } else if (score >= 5) {
            message = "Good job! Your final score is: " + score;
        } else {
            message = "Nice try! Your final score is: " + score;
        }
        
        historyArea.append("Game over! " + message + "\n");
        JOptionPane.showMessageDialog(this, 
            message, 
            "Game Over", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private class SubmitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            processGuess();
        }
    }
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            try {
                
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            NumberGuessingGame game = new NumberGuessingGame();
            game.setVisible(true);
        });
    }
}