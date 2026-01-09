package com.gavbath.mathlegends;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MathLegends extends JFrame { //creates the class for MathLegends (like the old assignments KidsGame), but I made it extend JFrame as we're making a gui for it now.

    private CardLayout cardLayout; //below, I created a ton of buttons, labels, instance variables, etc we'll need to use later.
                                    //I made sure to use private to abide by encapsulation rules especially since this project is more formal.
    private JPanel mainPanel;
    private JPanel menuPanel, setupPanel, gamePanel, summaryPanel; 
    private int mode;
    private int playerCount;
    private int currentPlayerIndex = 0;
    private Player[] players;
    private Game game;
    private long startTime;
    private int nQuestions = 0;
    private int timeLimit = 0;
    private int questionsAnswered = 0;
    private int lives = 3;
    private Timer gameClock;
    private JLabel playerNamesLabel;
    private JLabel questionLabel;
    private JLabel statusLabel;
    private JTextField textAns;
    private JButton submitButton;
    private boolean isMultiplayer = false; 
    private JTextField textPlayerCount; 
    private JPanel playersPanel; 
    
    public MathLegends() { //the constructor for our class. sets the title, size, and all the panels we need.
        setTitle("Math Legends!"); //sets the title of the game!
        setSize(700, 550); //sets the intial size for the window.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        makeMenuPanel();
        makeSetupPanel();
        makeGamePanel();
        makeSummaryPanel();
        
        mainPanel.add(menuPanel, "Menu"); //adds the main 4 panels to our gui.
        mainPanel.add(setupPanel, "Setup");
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(summaryPanel, "Summary");
        
        add(mainPanel);
        setVisible(true);
    }

    private void makeMenuPanel() { //method responsible for making the main menu screen. it makes the singleplayer and multiplayer buttons useable and decorates the edges.
        menuPanel = new JPanel(new GridBagLayout()) {
            
            @Override //we use override here since we "override" the old, built in paint method.
            protected void paintComponent(Graphics g) { //here, we make a new paint method for adding the designs to the side of the screen.
                super.paintComponent(g);
                
                Graphics2D g2d = (Graphics2D) g;
                
                g2d.setFont(new Font("Verdana", Font.BOLD, 40));
                g2d.setColor(new Color(173, 216, 230));
                
                String[] symbols = {"+", "-", "x", "÷"}; //these are the 4 symbols we add to an array, to wrap around the sides of the screen, which i think looks pretty sweet.
                int gap = 60; //we set a gap of 60 for spacing.
                int index = 0;
                
                for (int x = 20; x < getWidth(); x += gap) { //loops through the width of the screen, priting the symbol.
                    g2d.drawString(symbols[index % 4], x, 45); //we use index % 4 to cycle through the symbols without going out of bounds.
                    index++;
                }

                for (int x = 20; x < getWidth(); x += gap) {
                    g2d.drawString(symbols[index % 4], x, getHeight() - 20);  //loops through the width of the screen, priting the symbol.
                    index++;
                }
                
                for (int y = 100; y < getHeight() - 50; y += gap) { //loops through the height of the screen, priting the symbol.
                    g2d.drawString(symbols[index % 4], 20, y);
                    index++;
                }
                
                for (int y = 100; y < getHeight() - 50; y += gap) { //loops through the height of the screen, priting the symbol.
                    g2d.drawString(symbols[index % 4], getWidth() - 50, y);
                    index++;
                }
            }
        };
        
        menuPanel.setBackground(new Color(255, 250, 205)); //here, we set the background of our game to a nice colour.
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 15, 25, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("MATH LEGENDS!", SwingConstants.CENTER); //this is responsible for setting the title of our game, centred in the middle.
        title.setFont(new Font("Verdana", Font.BOLD, 48)); //we use a bold font for contrast.
        title.setOpaque(true);
        title.setBackground(new Color(100, 149, 237)); 
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createCompoundBorder( //here, we set a border around the title (lighter blue).
            BorderFactory.createLineBorder(new Color(173, 216, 230), 8), 
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        
        menuPanel.add(title, gbc);

        gbc.gridy++;
        JLabel subtitle = new JLabel("How do you want to play? Alone, or with friends?", SwingConstants.CENTER); //tell the user they're able to select singleplayer or multiplayer.
        subtitle.setFont(new Font("Verdana", Font.ITALIC, 16));
        menuPanel.add(subtitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 60, 10, 60); 
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.ipady = 15; 
        JButton singlePlayerButton = new JButton("Single Player"); //makes the single player button.
        JButton multiplayerButton = new JButton("Multiplayer");//makes the multi player button.
        Font buttonFont = new Font("Verdana", Font.BOLD, 22);
        singlePlayerButton.setFont(buttonFont); //sets the font for single player.
        multiplayerButton.setFont(buttonFont); //sets the font for multi player.
        singlePlayerButton.setBackground(Color.WHITE);
        multiplayerButton.setBackground(Color.WHITE);
        
        singlePlayerButton.setFocusPainted(false); 
        multiplayerButton.setFocusPainted(false);

        singlePlayerButton.addActionListener(e -> { //here we make an action listener for what happens if the user presses singleplayer.
            playerCount = 1; //hardcode the playercount to 1 for single player.
            isMultiplayer = false; //false, of course.
            showSetup();
        });
        
        multiplayerButton.addActionListener(e -> { //here we make an action listener for what happens if the user presses multiplayer.
            isMultiplayer = true; //set multiplayer to true.
            showSetup();
        });
        
        menuPanel.add(singlePlayerButton, gbc);
        
        gbc.gridy++; 
        menuPanel.add(multiplayerButton, gbc);
    }
    
    private void makeSetupPanel() { //this method builds the setup screen (where the user selects the game mode). it also handles the specific inputs for time limit and question count, and number of players.
        setupPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        setupPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setupPanel.setBackground(new Color(255, 255, 224));
        JLabel headerLabel = new JLabel("SELECT YOUR MODE!", SwingConstants.CENTER); //text telling the user to select a mode.
        headerLabel.setFont(new Font("Verdana", Font.BOLD, 22));
        headerLabel.setForeground(new Color(70, 130, 180));
       
        playersPanel = new JPanel(new FlowLayout());
        playersPanel.setBackground(new Color(255, 255, 224));
        
        JLabel playerCountLabel = new JLabel("How many players?");  //asks the user for the amt of players
        playerCountLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        
        textPlayerCount = new JTextField(5);
        textPlayerCount.setFont(new Font("Verdana", Font.PLAIN, 14));
        
        playersPanel.add(playerCountLabel);
        playersPanel.add(textPlayerCount);
        
        String[] modes = {"1) Make a wish", "2) No Mistakes", "3) Take Chances (3 lives)", "4) Time Trial"};
        JComboBox<String> modeComboBox = new JComboBox<>(modes); //creates the box to select the 1-4 game modes!
        modeComboBox.setBackground(Color.WHITE);
        modeComboBox.setFont(new Font("Verdana", Font.PLAIN, 14));
        
        JLabel descriptionLabel = new JLabel("Pick a number of questions to test yourself on, and see your score!", SwingConstants.CENTER); //description for gamemode 1 (default).
        descriptionLabel.setFont(new Font("Verdana", Font.ITALIC, 14));
        descriptionLabel.setForeground(Color.DARK_GRAY);

        JPanel settingsPanel = new JPanel(new FlowLayout());
        settingsPanel.setBackground(new Color(255, 255, 224));
        
        JLabel settingLabel = new JLabel("Number of Questions:"); //makes the label telling the user to enter a number of questions.
        settingLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        JTextField settingField = new JTextField(10);
        settingField.setFont(new Font("Verdana", Font.PLAIN, 14));
        settingsPanel.add(settingLabel);
        settingsPanel.add(settingField);
     
        JPanel combinedSettingsPanel = new JPanel();
        combinedSettingsPanel.setLayout(new BoxLayout(combinedSettingsPanel, BoxLayout.Y_AXIS)); //stacks the items vertically.
        combinedSettingsPanel.setBackground(new Color(255, 255, 224));
        
        combinedSettingsPanel.add(playersPanel);
        combinedSettingsPanel.add(Box.createRigidArea(new Dimension(0, 5))); //this adds an invisible 5 pixel spacing between the 2 boxes so they dont collide.
        combinedSettingsPanel.add(settingsPanel);
        
        modeComboBox.addActionListener(e -> { //action listener for what happens if you select any of the 1-4 game modes.
            
            
            int selectedIndex = modeComboBox.getSelectedIndex();
            if (selectedIndex == 0) { //if you select the first mode:
                settingsPanel.setVisible(true); //set to true, as we need to grab settings from user for questions.
                settingLabel.setText("Number of Questions:");
                descriptionLabel.setText("Pick a number of questions to test yourself on, and see your score!");
            } else if (selectedIndex == 1) {
                settingsPanel.setVisible(false);
                descriptionLabel.setText("This game mode ends if you get a single question wrong!");
            } else if (selectedIndex == 2) {
                settingsPanel.setVisible(false);
                    descriptionLabel.setText("Be careful, in this gamemode you get 3 lives, then it's game over!");
            } else if (selectedIndex == 3) {
                settingsPanel.setVisible(true); //set to true, as we need to grab settings from user for time.
                settingLabel.setText("Time Limit (seconds):");
                descriptionLabel.setText("Pick a time limit, and answer as many as you can before it runs out!");
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0)); //creates a panel for buttons. 1 row, 2 columns.
        buttonPanel.setBackground(new Color(255, 255, 224));
        
        JButton backButton = new JButton("Back"); //creates the back button.
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setFont(new Font("Verdana", Font.BOLD, 14));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu")); //when the back button is clciked, send them back to the menu.

        JButton startButton = new JButton("Start Game"); //creates a start game button.
        startButton.setFont(new Font("Verdana", Font.BOLD, 14));
        startButton.setBackground(new Color(100, 149, 237));
        startButton.setForeground(Color.WHITE);
        
        startButton.addActionListener(e -> { //action listener for start button.
            
            
            try {
                if (isMultiplayer) { //if the user selected multiplayer
                    String countStr = textPlayerCount.getText();
                    if (countStr == null || countStr.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please enter number of players.");
                        return;
                    }
                    playerCount = Integer.parseInt(countStr);
                    
                    if (playerCount < 2) { //if the player count is under 2 (singleplayer)
                        JOptionPane.showMessageDialog(this, "Multiplayer requires at least 2 players!"); //error message, telling user they cant select 1 for multiplayer.
                        return;
                    }
                } else {
                    playerCount = 1; //if none of these are correct, fallback set to 1.
                }
                
                mode = modeComboBox.getSelectedIndex() + 1;
                String settingText = settingField.getText();
                int settingVal = 0;
                
                if (mode == 1 || mode == 4) { //if the mode is 1 or 4.
                      if (settingText == null || settingText.trim().isEmpty()) {
                          String msg = (mode == 1) ? "Please enter number of questions." : "Please enter a time limit.";
                          JOptionPane.showMessageDialog(this, msg); //tells the user to select questions if game mode 1, or time if 4.
                          return;
                      }
                      settingVal = Integer.parseInt(settingText);
                }

                if (mode == 1) nQuestions = settingVal;
                else if (mode == 4) timeLimit = settingVal;
                else nQuestions = 999;
                
                players = new Player[playerCount];
                makePlayersRecursive(0);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number."); //error message for when a user enters an invalid input.
            }
        });
        
        buttonPanel.add(backButton); //now we add both buttons to the panel.
        buttonPanel.add(startButton);
        
        setupPanel.add(headerLabel); //here and below, we add the labels we made to the setup panel.
        setupPanel.add(modeComboBox);
        setupPanel.add(descriptionLabel);
        setupPanel.add(combinedSettingsPanel);
        setupPanel.add(buttonPanel);
    }

    private void showSetup() { //this switches the view over to the setup screen. It will also hide the number of players button depending on if the user chose single/multiplayer.
        
        if (isMultiplayer) {
            playersPanel.setVisible(true); //if they're on multiplayer, set the player panel to visible.
            textPlayerCount.setText(""); 
            textPlayerCount.requestFocus();
        } else {
            playersPanel.setVisible(false); //set it to false if in singleplayer!
        }
        cardLayout.show(mainPanel, "Setup");
    }

    private void makePlayersRecursive(int index) { //this is an important method. it handles grabbing the names from the user, makes that player object, and starts the game!
        if (index < playerCount) {
            String name = JOptionPane.showInputDialog(this, "Enter player " + (index + 1) + " name:");
            
            if (name == null) {
                return;
            }
            
            if (name.trim().isEmpty()) { //if the name, after trimming the empty space, is empty, then:
                name = "Player " + (index + 1); //we give them a default name as a fallback!!
            }
            
            players[index] = new Player(name); 
            makePlayersRecursive(index + 1); //calls the method back recursively to make the player.
        } else {
            currentPlayerIndex = 0;
            beginPlayersTurn();
        }
    }

    private void makeGamePanel() { //creates the main gameplay screen. it makes the layout for the questions, answer field, and submit button, as well as the extra labels like lives and time.
        gamePanel = new JPanel(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout()); //creates the header panel.
        headerPanel.setBackground(new Color(100, 149, 237));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JButton quitButton = new JButton("Exit to Menu"); //makes the quit button, with text "exit to menu".
        quitButton.setBackground(new Color(255, 99, 71));
        quitButton.setForeground(Color.WHITE);
        quitButton.setFocusPainted(false);
        quitButton.addActionListener(e -> { //makes an action listener for the quit button.
            if (gameClock != null && gameClock.isRunning()) gameClock.stop(); //if the game clock isnt empty and its still running, end the clock.
            cardLayout.show(mainPanel, "Menu");
        });
        
        playerNamesLabel = new JLabel("Player 1", SwingConstants.CENTER); //creates the names label in the center.
        playerNamesLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        playerNamesLabel.setForeground(Color.WHITE);
        
        statusLabel = new JLabel("Stats", SwingConstants.RIGHT); //creates the status label to the right.
        statusLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);
        
        headerPanel.add(quitButton, BorderLayout.WEST); //adds the quit button to the header panel.
        headerPanel.add(playerNamesLabel, BorderLayout.CENTER);
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        JPanel centerPanel = new JPanel(new GridBagLayout()); //use gridbaylayout becuase its the best for centring a single large item verically and horizontally!
        centerPanel.setBackground(new Color(255, 250, 205));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        questionLabel = new JLabel("Question?", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Verdana", Font.BOLD, 48));
        questionLabel.setForeground(new Color(70, 70, 70));
        
        centerPanel.add(questionLabel, gbc); //adds the label using the constraints defined above to the center panel.
        
        gbc.gridy++;
        textAns = new JTextField(8); //this creates an input box about 8 characters wide
        textAns.setFont(new Font("Verdana", Font.PLAIN, 32));
        textAns.setHorizontalAlignment(JTextField.CENTER);
        centerPanel.add(textAns, gbc); //adds the input field to the center panel
        
        gbc.gridy++;
        submitButton = new JButton("SUBMIT ANSWER"); //creates a submit button, with text "submit answer".
        submitButton.setFont(new Font("Verdana", Font.BOLD, 16));
        submitButton.setBackground(new Color(60, 179, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setPreferredSize(new Dimension(200, 50));
        
        centerPanel.add(submitButton, gbc); //adds the submit button to the center panel too.
        
        gamePanel.add(headerPanel, BorderLayout.NORTH); //adds both header and center panel to the main game panel.
        gamePanel.add(centerPanel, BorderLayout.CENTER);
        
        submitButton.addActionListener(e -> submitAnswer()); //this runs when the submit answer button is clicekd.
        textAns.addActionListener(e -> submitAnswer()); //runs when the user hits the enter key on the box.
    }
    
        private void nextQuestion() { //this method is responsible for randomly selecting a math question.
        
        int randomNum = (int)(Math.random() * 4);
        switch (randomNum) { //switch statement, depending on what the selection was itll generate a random question.
            case 0: game.generateAddQuestion(); break;
            case 1: game.generateSubQuestion(); break;
            case 2: game.generateMulQuestion(); break;
            case 3: game.generateDivQuestion(); break;
        }
        
        questionLabel.setText(game.thisQuestionMsg);
        textAns.setText("");
        textAns.requestFocus();
    }

    private void beginPlayersTurn() { //gets the game ready for the current player. it generates the the first question, and resets all fields like lives, score, etc.
        game = new Game(); //create a new game object.
        questionsAnswered = 0; //set the questions answered to 0 since obviously the user hasn't gone yet.
        lives = 3;
        startTime = System.currentTimeMillis(); 
        
        playerNamesLabel.setText(players[currentPlayerIndex].getName());
        textAns.setText("");
        textAns.requestFocus();
        
        if (mode == 4) { //if you're in the 4th game mode
             final long limitInMS = timeLimit * 1000L;
             statusLabel.setText("Time: " + timeLimit);
             
             gameClock = new Timer(100, new ActionListener() { //creates the timer.
                 public void actionPerformed(ActionEvent e) {
                     long elapsed = System.currentTimeMillis() - startTime; //calculates the elapsed time.
                     long remaining = limitInMS - elapsed; //calcalates the time remaining.
                     
                     statusLabel.setText("Time: " + (int)Math.ceil(remaining / 1000.0));  //this displays the time, ensuring it starts at the seconds you enter.
                     
                     if (remaining <= 0) {
                         gameClock.stop(); //ends the clock if theres no time left.
                         stopTurn();
                     }
                 }
             });
             gameClock.start();
        } else if (mode == 3) {
            statusLabel.setText("Lives: " + lives); 
        } else {
            statusLabel.setText("You got this!"); //if no special gamemode is selected, display some motivational text in the top right.
        }
        
        nextQuestion();
        cardLayout.show(mainPanel, "Game");
    }


    private void submitAnswer() { //this method reads the users input, checks if its correct,and if so updates their score and checks if the game should end.
        try {
            double ans = Double.parseDouble(textAns.getText()); 
            boolean isCorrect = game.ansCheck(ans); //saves a boolean true or false value depending on if the users answer is correct.
            questionsAnswered++;
            
            if (mode == 1) { //if game mode 1, if the user has completed >= amount of questions picked, end the game.
                if (questionsAnswered >= nQuestions) {
                    stopTurn();
                    return;
                }
            } else if (mode == 2) { //if game mode 2, if they're not correct, end the game.
                if (!isCorrect) {
                    JOptionPane.showMessageDialog(this, "Wrong! Game Over.");
                    stopTurn();
                    return;
                }
            } else if (mode == 3) { //if game mode 3, if they're not correct, substract a life from them, and end the game if its 0.
                if (!isCorrect) {
                    lives--;
                    statusLabel.setText("Lives: " + lives);
                    if (lives <= 0) {
                        JOptionPane.showMessageDialog(this, "Lives exhausted!");
                        stopTurn();
                        return;
                    }
                }
            }
            
            nextQuestion(); //call nextQuestion to spawn another question to ask.
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }
    
    private void showSummary() { //this is responsible for making the actual leaderboard and summary similar to assignment 1. it sorts through each user, and outputs the results.
        JTextPane area = (JTextPane)((JScrollPane)summaryPanel.getComponent(0)).getViewport().getView();
        StringBuilder sb = new StringBuilder();
        
        if (playerCount > 1) { //if the playercount is more than 1 (multiplayer), then it shows the winner text.
            sb.append("-- Leaderboard (Score only) --\n"); //bubble sort, looping through each player and comparing scores.
            for (int i = 0; i < playerCount - 1; i++) {
                
                for (int j = i + 1; j < playerCount; j++) {
                    if (players[j].getScore() > players[i].getScore()) {
                        Player tempSwap = players[i];
                        players[i] = players[j];
                        players[j] = tempSwap;
                    }
                }
            }
            
            //if the playercount is more than 1 (multiplayer), then it shows the winner text.
            if (players[0].getScore() > players[1].getScore()) {
                sb.append("\n******************\n");
                sb.append("   WINNER: ").append(players[0].getName());
                sb.append("\n******************\n\n");
            } else {
                sb.append("\nIT'S A TIE!\n\n");
            }
            
            for (Player p : players) { //for each player in players:
                sb.append(p.getName()).append(" : ").append(p.getScore()).append("\n");
            }
            sb.append("\n");
        }
        
        sb.append("\n-- Results --\n");
        for (Player p : players) { //loops through each player, and displays their summary.
            sb.append("\nPlayer: ").append(p.getName()).append("\n");
            sb.append("Time taken: ").append(p.getTimeUsed()).append(" seconds\n");
            sb.append("Score: ").append(p.getScore()).append("\n");
            sb.append("Summary:").append(p.getSummary()).append("\n");
            sb.append("----------------------------\n");
        }
        
        
        
        area.setText(sb.toString());
        
        StyledDocument doc = area.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        cardLayout.show(mainPanel, "Summary"); //using the settings above, displays the summary now centered.
    }
    
    private void stopTurn() { //this method ends the players turn, stopping the timer and saving the score. it also checks if another player should go after.
        
        if (gameClock != null && gameClock.isRunning()) gameClock.stop();
        
        long endTime = System.currentTimeMillis();
        long totalTime = (endTime - startTime) / 1000;
        
        players[currentPlayerIndex].setScore(game.score);
        players[currentPlayerIndex].setSummary(game.summary);
        players[currentPlayerIndex].setTimeUsed(totalTime);
        
        JOptionPane.showMessageDialog(this, players[currentPlayerIndex].getName() + " finished! Score: " + game.score);
        
        currentPlayerIndex++;
        if (currentPlayerIndex < playerCount) { //checks if theres more players in the array
            beginPlayersTurn(); //if so, reset the board and start that players game.
        } else { //if theres no more players left in the array (game ends).
            if (playerCount > 1) {
                findAndShowWinner(); //showcase winner or tie if multiplayer.
            }
            showSummary();
        }
    }

    private void findAndShowWinner() { //this is used in only multiplayer mode. it compares scores, and sees who wins, or if there is a tie.
        Player winner = players[0];
        boolean tie = false;
        
        for (int i = 1; i < playerCount; i++) { //cycle through the player count.
            if (players[i].getScore() > winner.getScore()) { //if the player at index i has a better score than the current winner.
                winner = players[i]; //set the winner to that player.
                tie = false;
            } else if (players[i].getScore() == winner.getScore()) {
                tie = true;
            }
        }
        
        if (tie) { //if the game is a tie, tell the users.
            JOptionPane.showMessageDialog(this, "It is a TIE game! \nGreat job to all players.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else { //if it ISNT a tie, then tell which user won.
            JOptionPane.showMessageDialog(this, "Congrats!\n\n" + winner.getName() + " WON!\nScore: " + winner.getScore(), "Winner Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void makeSummaryPanel() { //this method makes the panel for the final summary screen.
        
        summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(Color.WHITE);
        JTextPane textSummary = new JTextPane(); //creates the text summary using JTextPane for multiple lines of text.
        textSummary.setEditable(false);
        textSummary.setFont(new Font("Verdana", Font.PLAIN, 14));
        
        StyledDocument doc = textSummary.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        textSummary.setParagraphAttributes(center, true);
        
        summaryPanel.add(new JScrollPane(textSummary), BorderLayout.CENTER); //centers the summary output, so its not to the left.
        
        JButton homeButton = new JButton("Back to Menu"); //creates the home button with text saying "back to menu".
        homeButton.setFont(new Font("Verdana", Font.BOLD, 14));
        homeButton.setBackground(new Color(100, 149, 238));
        homeButton.setForeground(Color.WHITE);
        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu")); //when the home button is clicked, show the main menu again.
        summaryPanel.add(homeButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) { //just the main method.
        SwingUtilities.invokeLater(() -> new MathLegends()); //here, it laubches the MathLegends GUI using Swing.
    }
}