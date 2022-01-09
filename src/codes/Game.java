package codes;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.plaf.FontUIResource;
import javax.sound.sampled.*;


public class Game implements ActionListener {

//Code Elements
	String illegalChar = ".1,8!?2:;(3){4}9[5]@*~/\\6–#$7&^%0|`";

	String incorrectGuesses = "";
	String selectedWord;
	String progressDone;
	String progress;
	String command;
	String input;

	char [] wordToGuess;
	char [] guesses;

	int att = 1;
	int wins = 0;
	int losses = 0;
	int lives = 6;
	int gameState = 0;
	int gameInstance = 0;
	int keyboardState = 0;

//GUIs
	/*-----Frames-----*/
	JFrame mainWindow = new JFrame("Hangman");
	ImageIcon icon = new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/Others/iconHangman.png"));
	/*-----MenuBar-----*/
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("Menu");
	JMenuItem menuHome = new JMenuItem ("Home");
	JMenuItem menuStart = new JMenuItem ("Restart");
	JLabel scoreCount = new JLabel();
	/*-----Home-----*/
	JLabel title = new JLabel(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/Others/homeTitle.png")));
	JLabel hangmanAnimation = new JLabel(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/Others/homeAnimation.gif")));
	JButton buttonNewGame = new JButton(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonNewGame.png")));
	JButton buttonResume = new JButton(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonResume.png")));
	JButton buttonOptions = new JButton(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonOptions.png")));
	JButton buttonExit = new JButton(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonExit.png")));
	JLabel eye = new JLabel(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/Others/homeEye.png")));
	JLabel credits = new JLabel("Created By: Del Mundo & Mata");
	/*-----Game-----*/
	JLabel hint = new JLabel();
	JLabel lineTop = new JLabel();
	JLabel attemptCount = new JLabel("Attempt #" + att);
	JLabel lifeCount = new JLabel("Lives left: " + lives);
	JLabel progressVisual = new JLabel();
	JLabel progressWord = new JLabel();
	JLabel lineBottom = new JLabel();
	JLabel guide = new JLabel("Please enter a letter: ");
	JButton buttonKeyboard = new JButton(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonKeyboard.png")));
	JTextField entry = new JTextField();
	JButton submit = new JButton("Submit");
	JButton endPlayAgain = new JButton("Play Again");
	JButton endHome = new JButton("Leave");
	/*-----On-Screen Keyboard-----*/
	JPanel keyboardWindow = new JPanel();
	JPanel keyPanel = new JPanel();

//Window
	public Game() {				
		
	//Window
		mainWindow.setSize(365, 430);
		mainWindow.setResizable(false);
		mainWindow.setIconImage(icon.getImage());
		mainWindow.getContentPane().setBackground(Color.WHITE);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setLayout(null);
		
	//Menu Bar
		menuBar.setVisible(false);
		mainWindow.setJMenuBar(menuBar);
		/*-----Menu-----*/
		fileMenu.setFont(new Font("Century Gothic", Font.BOLD, 11));
		menuBar.add(fileMenu);
		/*-----Menu Elements-----*/
		menuHome.setFont(new Font("Century Gothic", Font.BOLD, 11));
		menuStart.setFont(new Font("Century Gothic", Font.BOLD, 11));
		fileMenu.add(menuHome);
		fileMenu.add(menuStart);
		/*-----W/L Tracker-----*/
		menuBar.add(Box.createHorizontalGlue());
		scoreCount.setFont(new Font("Century Gothic", Font.BOLD, 11));
		menuBar.add(scoreCount);

	//On-Screen Keyboard Window
		mainWindow.add(keyboardWindow);
		keyboardWindow.setBounds(-1, 270, 351, 100);
		keyboardWindow.setBackground(Color.decode("#f5f5f5"));
		keyboardWindow.setVisible(false);
		keyboardWindow.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(4, 5, 5, 5)));
		/*-----Key Container-----*/
		keyboardWindow.add(keyPanel);
		keyPanel.setBackground(Color.decode("#f5f5f5"));
		keyPanel.setPreferredSize(new Dimension(327, 79));
				createButtons(keyPanel);
		
	//Action Listeners
		menuHome.addActionListener(this);
		menuStart.addActionListener(this);
		buttonResume.addActionListener(this);
		buttonNewGame.addActionListener(this);
		buttonExit.addActionListener(this);
		entry.addActionListener(this);
		buttonKeyboard.addActionListener(this);
		submit.addActionListener(this);
		endPlayAgain.addActionListener(this);
		endHome.addActionListener(this);
		
		initializeSounds();
		initiateEffects();
		startHomeScreen();
	}

//First Home Screen
	public void startHomeScreen() {

		mainWindow.getContentPane().setBackground(Color.WHITE);

	//Title
		title.setBounds(20, 52, 250, 60);
		title.setEnabled(true);
		title.setVisible(true);
		mainWindow.add(title);

	//Hangman Animation
		hangmanAnimation.setBounds(270, 32, 60, 85);
		hangmanAnimation.setEnabled(true);
		hangmanAnimation.setVisible(true);
		mainWindow.add(hangmanAnimation);

	//Resume Button
		buttonResume.setVisible(false);

	//New Game Button
		buttonNewGame.setBounds(119, 170, 101, 22);
		buttonNewGame.setContentAreaFilled(false);
		buttonNewGame.setBorderPainted(false);
		buttonNewGame.setOpaque(false);
		buttonNewGame.setEnabled(true);
		buttonNewGame.setVisible(true);
		mainWindow.add(buttonNewGame);

	//Options Button
		buttonOptions.setBounds(127, 220, 84, 20);
		buttonOptions.setContentAreaFilled(false);
		buttonOptions.setBorderPainted(false);
		buttonOptions.setOpaque(false);
		buttonOptions.setEnabled(false);
		buttonOptions.setVisible(true);
		mainWindow.add(buttonOptions);

	//Exit Button
		buttonExit.setBounds(145, 270, 40, 20);
		buttonExit.setContentAreaFilled(false);
		buttonExit.setBorderPainted(false);
		buttonExit.setOpaque(false);
		buttonExit.setEnabled(true);
		buttonExit.setVisible(true);
		mainWindow.add(buttonExit);

	//Credits
		credits.setFont(new Font("Calibri", Font.PLAIN, 11));
		credits.setBounds(26, 369, 290, 20);
		credits.setEnabled(true);
		credits.setVisible(true);
		mainWindow.add(credits);

	//Eye
		eye.setBounds(5, 367, 20, 20);
		eye.setEnabled(true);
		eye.setVisible(true);
		mainWindow.add(eye);

	//Loads Main Window After Everything's Done
		mainWindow.setVisible(true);
		
	}

//Home Screen
	public void homeScreen() {

	mainWindow.getContentPane().setBackground(Color.WHITE);
	
	//Disables Unneeded Elements
		audioLose.stop();
		audioWin.stop();
	
		menuHome.setEnabled(false);

		hint.setVisible(false);
		lineTop.setVisible(false);
		attemptCount.setVisible(false);
		lifeCount.setVisible(false);
		progressVisual.setVisible(false);
		progressWord.setVisible(false);
		lineBottom.setVisible(false);
		guide.setVisible(false);
		entry.setVisible(false);
		buttonKeyboard.setVisible(false);
		submit.setVisible(false);
		endPlayAgain.setVisible(false);
		endHome.setVisible(false);

	//Title
		title.setBounds(20, 52, 250, 60);
		title.setEnabled(true);
		title.setVisible(true);

	//Hangman Animation
		hangmanAnimation.setBounds(270, 32, 60, 85);
		hangmanAnimation.setEnabled(true);
		hangmanAnimation.setVisible(true);

	//Resume Button
		buttonResume.setBounds(132, 160, 71, 20);
		buttonResume.setContentAreaFilled(false);
		buttonResume.setBorderPainted(false);
		buttonResume.setOpaque(false);
		buttonResume.setEnabled(true);
		buttonResume.setVisible(true);	
		mainWindow.add(buttonResume);

	//New Game Button
		buttonNewGame.setBounds(119, 205, 101, 22);
		buttonNewGame.setContentAreaFilled(false);
		buttonNewGame.setBorderPainted(false);
		buttonNewGame.setOpaque(false);
		buttonNewGame.setEnabled(true);
		buttonNewGame.setVisible(true);

	//Options Button
		buttonOptions.setBounds(127, 250, 84, 20);
		buttonOptions.setContentAreaFilled(false);
		buttonOptions.setBorderPainted(false);
		buttonOptions.setOpaque(false);
		buttonOptions.setEnabled(false);
		buttonOptions.setVisible(true);

	//Exit Button
		buttonExit.setBounds(145, 295, 40, 20);
		buttonExit.setContentAreaFilled(false);
		buttonExit.setBorderPainted(false);
		buttonExit.setOpaque(false);
		buttonExit.setEnabled(true);
		buttonExit.setVisible(true);

	//Credits
		credits.setFont(new Font("Calibri", Font.PLAIN, 11));
		credits.setBounds(26, 369, 290, 20);
		credits.setEnabled(true);
		credits.setVisible(true);

	//Eye
		eye.setBounds(5, 367, 20, 20);
		eye.setEnabled(true);
		eye.setVisible(true);
		
	}

//Game Initialization
	public void newGame() {
		
		gameState = 0;
		mainWindow.getContentPane().setBackground(Color.WHITE);
		
		menuHome.setEnabled(true);
		menuStart.setText("Restart");

	//Removes Unneeded Elements
		audioLose.stop();
		audioWin.stop();
		
		title.setVisible(false);
		hangmanAnimation.setVisible(false);
		buttonResume.setVisible(false);
		buttonNewGame.setVisible(false);
		buttonOptions.setVisible(false);
		buttonExit.setVisible(false);
		eye.setVisible(false);
		credits.setVisible(false);
		
		endPlayAgain.setVisible(false);
		endHome.setVisible(false);
	
	//Resets Game Information
		att = 1;
		lives = 6;
		incorrectGuesses = "";

	//Hint
		getRandomList();
		hint.setFont(new Font("Century Gothic", Font.BOLD, 13));
		hint.setBounds(28, 10, 290, 30);
		hint.setHorizontalAlignment(SwingConstants.CENTER); 

	//Top Line
		lineTop.setFont(new Font("Century Gothic", Font.BOLD, 13));
		lineTop.setText("-------------------------------------------------------------");
		lineTop.setBounds(21, 30, 320, 30);

	//Attempt Count
		attemptCount.setFont(new Font("Century Gothic", Font.BOLD, 13));
		attemptCount.setBounds(25, 50, 290, 30);

	//Life Count
		lifeCount.setFont(new Font("Century Gothic", Font.BOLD, 13));
		lifeCount.setBounds(251, 50, 290, 30);

	//Hangman
		progressVisual.setBounds(110, 82, 100, 150);
		printHangman();

	//Word Progression
		progressWord.setFont(new Font("Century Gothic", Font.BOLD, 13));
		progressWord.setText("Word to Guess: " + progress);
		progressWord.setBounds(14, 240, 320, 30);
		progressWord.setHorizontalAlignment(SwingConstants.CENTER);

	//Bottom Line
		lineBottom.setFont(new Font("Century Gothic", Font.BOLD, 13));
		lineBottom.setText("-------------------------------------------------------------");
		lineBottom.setBounds(21, 260, 320, 30);

	//Guide
		guide.setFont(new Font("Century Gothic", Font.BOLD, 13));
		guide.setBounds(14, 280, 320, 30);
		guide.setHorizontalAlignment(SwingConstants.CENTER);

	//Display Keyboard Button
		buttonKeyboard.setBounds(5, 349, 22, 14);
		buttonKeyboard.setContentAreaFilled(false);
		buttonKeyboard.setBorderPainted(false);
		buttonKeyboard.setFocusPainted(false);
		buttonKeyboard.setOpaque(true);

	//Input
		entry.setFont(new Font("Century Gothic", Font.PLAIN, 11));
		entry.setText("Enter your guess");
		entry.setMargin(new Insets(0, 5, 0, 5));
		entry.setBounds(68, 315, 110, 30);

	//Submission Button
		submit.setFont(new Font("Century Gothic", Font.BOLD, 11));
		submit.setBounds(193, 315, 90, 29);
		submit.setFocusPainted(false);
	
	//Play Again Button
		endPlayAgain.setFont(new Font("Century Gothic", Font.BOLD, 11));
		endPlayAgain.setBounds(75, 315, 110, 29);
		endPlayAgain.setFocusPainted(false);

	//Home Button
		endHome.setFont(new Font("Century Gothic", Font.BOLD, 11));
		endHome.setBounds(200, 315, 80, 29);
		endHome.setFocusPainted(false);

	//Adds Game GUI to Window
		mainWindow.add(hint);
		mainWindow.add(lineTop);
		mainWindow.add(attemptCount);
		mainWindow.add(lifeCount);
		mainWindow.add(progressVisual);
		mainWindow.add(progressWord);
		mainWindow.add(lineBottom);
		mainWindow.add(guide);
		mainWindow.add(entry);
		mainWindow.add(buttonKeyboard);
		mainWindow.add(submit);
		mainWindow.add(endPlayAgain);
		mainWindow.add(endHome);

	//Enables Game GUI
		hint.setVisible(true);
		lineTop.setVisible(true);
		attemptCount.setVisible(true);
		lifeCount.setVisible(true);
		progressVisual.setVisible(true);
		progressWord.setVisible(true);
		lineBottom.setVisible(true);
		guide.setVisible(true);
		entry.setVisible(true);
		buttonKeyboard.setVisible(true);
		submit.setVisible(true);

		guide.setText("Please enter a letter: ");
		guide.setForeground(Color.darkGray);
	
	//Initializes Words and Updates Variables
		printWord();
		updateVariables();

	}

//Resume Game GUI
	public void resumeGame() {

		mainWindow.getContentPane().setBackground(Color.WHITE);
		menuHome.setEnabled(true);
	
	//Removes Home GUI
		title.setVisible(false);
		hangmanAnimation.setVisible(false);
		buttonResume.setVisible(false);
		buttonNewGame.setVisible(false);
		buttonOptions.setVisible(false);
		buttonExit.setVisible(false);
		eye.setVisible(false);
		credits.setVisible(false);

	//Enables Game GUI
		hint.setVisible(true);
		lineTop.setVisible(true);
		attemptCount.setVisible(true);
		lifeCount.setVisible(true);
		progressVisual.setVisible(true);
		progressWord.setVisible(true);
		lineBottom.setVisible(true);
		buttonKeyboard.setVisible(true);
		guide.setVisible(true);
		buttonKeyboard.setBounds(5, 350, 22, 14);
		
		/*-----Checks if Game is Complete-----*/
		if (gameState == 1 || gameState == 2) {
			entry.setVisible(false);
			submit.setVisible(false);
			buttonKeyboard.setVisible(false);
			endPlayAgain.setVisible(true);
			endHome.setVisible(true);
		}
		else {
			entry.setVisible(true);
			submit.setVisible(true);
			buttonKeyboard.setVisible(true);
			endPlayAgain.setVisible(false);
			endHome.setVisible(false);
		}

	}

//GUI Effect Mouse Listeners 
	public void initiateEffects() {
		/*-----Resume-----*/
		buttonResume.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
       			buttonResume.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonHovers/hoverResume.png")));
       			audioHoverMenu.setFramePosition(0);
       			audioHoverMenu.start();
    		}
   			
   			public void mouseReleased(java.awt.event.MouseEvent evt) {
   				audioPressedMenu.setFramePosition(0);
   				audioPressedMenu.start();
    		}

			public void mouseExited(java.awt.event.MouseEvent evt) {
        		buttonResume.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonResume.png")));
    		}	
		});
		
		/*-----New Game-----*/
		buttonNewGame.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
       			buttonNewGame.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonHovers/hoverNewGame.png")));
       			audioHoverMenu.setFramePosition(0);
       			audioHoverMenu.start();
    		}
   			
   			public void mouseReleased(java.awt.event.MouseEvent evt) {
   				audioPressedMenu.setFramePosition(0);
   				audioPressedMenu.start();
    		}

			public void mouseExited(java.awt.event.MouseEvent evt) {
        		buttonNewGame.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonNewGame.png")));
    		}	
		});
		
		/*-----Options-----*/
		buttonOptions.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
   				buttonOptions.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonHovers/hoverOptions.png")));
       			//audioHoverMenu.setFramePosition(0);
       			//audioHoverMenu.start();
    		}
   			
   			public void mouseReleased(java.awt.event.MouseEvent evt) {
   				//audioPressedMenu.setFramePosition(0);
   				//audioPressedMenu.start();
    		}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				buttonOptions.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonOptions.png")));
    		}	
		});
		
		/*-----Exit-----*/
		buttonExit.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
       			buttonExit.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonHovers/hoverExit.png")));
       			audioHoverMenu.setFramePosition(0);
       			audioHoverMenu.start();
    		}
   			
   			public void mouseReleased(java.awt.event.MouseEvent evt) {
   				audioPressedMenu.setFramePosition(0);
   				audioPressedMenu.start();
    		}

			public void mouseExited(java.awt.event.MouseEvent evt) {
        		buttonExit.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonExit.png")));
    		}	
		});
		
		/*-----Play Again-----*/
		endPlayAgain.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
   				audioHoverMenu.setFramePosition(0);
       			audioHoverMenu.start();
    		}
   			
   			public void mouseReleased(java.awt.event.MouseEvent evt) {
   				audioPressedMenu.setFramePosition(0);
   				audioPressedMenu.start();
    		}
		});
		
		/*-----Home-----*/
		endHome.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
   				audioHoverMenu.setFramePosition(0);
       			audioHoverMenu.start();
    		}
   			
   			public void mouseReleased(java.awt.event.MouseEvent evt) {
   				audioPressedMenu.setFramePosition(0);
   				audioPressedMenu.start();
    		}
		});
		
		/*-----On-Screen Keyboard-----*/
		buttonKeyboard.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
       			buttonKeyboard.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonHovers/hoverKeyboard.png")));
    		}

			public void mouseExited(java.awt.event.MouseEvent evt) {
        		buttonKeyboard.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/GUI/ButtonSprites/buttonKeyboard.png")));
    		}	
		});
	}

	
//Action Listeners
	public void actionPerformed (ActionEvent e) {
	
	//Menu Home Action
		if (e.getSource() == menuHome || e.getSource() == endHome) {
		//Removes Menu Bar
			menuBar.setVisible(false);
			keyboardWindow.setVisible(false);
			keyboardState = 0;
			System.out.println("\nGoing to Home Screen...");
			homeScreen();
		}

	//Menu Resume Action
		if (e.getSource() == buttonResume) {
		//Adds Menu Bar
			menuBar.setVisible(true);
			System.out.println("\nResuming Game...");
			resumeGame();
		}

	//Menu New Game Action
		else if (e.getSource() == menuStart || e.getSource() == buttonNewGame || e.getSource() == endPlayAgain) {
		//Adds Menu Bar
			menuBar.setVisible(true);

			gameInstance++;
			keyboardState = 0;
			System.out.println("\nStarting New Game...");
			newGame();
		}

	//Menu Exit Action
		else if (e.getSource() == buttonExit) {
			System.out.println("\nExiting Game...");
			JOptionPane.showMessageDialog(mainWindow, "      Thanks for playing! Stay safe!", "Message", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}

	//Show On-screen Keyboard Button Action
		else if (e.getSource() == buttonKeyboard) {
			if (keyboardState == 0) {
				audioKeyboardOpen.setFramePosition(0);
				audioKeyboardOpen.start();
				keyboardWindow.setVisible(true);
				entry.setVisible(false);
				submit.setVisible(false);
				buttonKeyboard.setBounds(5, 250, 22, 14);
				keyboardState = 1;
			}
			else if (keyboardState == 1) {
				audioKeyboardClose.setFramePosition(0);
				audioKeyboardClose.start();
				keyboardWindow.setVisible(false);
				entry.setVisible(true);
				submit.setVisible(true);
				buttonKeyboard.setBounds(5, 349, 22, 14);
				keyboardState = 0;
			}
		}

	//Submit Button Actions
		else if (e.getSource() == submit || e.getSource() == entry) {
			
		//Gets Input From TextField
			input = entry.getText();

		//Runs if Selected Word is Incomplete
			if (!Arrays.equals(guesses, wordToGuess)) {

			//Error Handler
				if (input.length() != 1 || illegalChar.contains(input) || incorrectGuesses.contains(input.toUpperCase()) || progress.toUpperCase().contains(input.toUpperCase())) {
					/*-----Secret Phrases-----*/
					if (input.equalsIgnoreCase("answer")) {
						guide.setForeground(Color.darkGray);
						guide.setText("The word is: \"" + selectedWord + "\". You cheater ha ;)");
						updateVariables();
					}
					else if (input.equalsIgnoreCase("heal")) {
						lives = 6;
						guide.setText("Lives restored.");
						updateVariables();
					}
					else if (input.equalsIgnoreCase("incorrect")) {
						updateVariables();
						displayIncorrect();
					}
					else if (input.equalsIgnoreCase("end")) {
						lives = 0;
						updateVariables();
					}
					/*-----Errors-----*/
					else if (input.trim().isEmpty()) {
						
						guide.setForeground(Color.GRAY);
						guide.setText("Blank input detected! Try again. ");
						updateVariables();
					}
					else if (illegalChar.contains(input.trim())) {
						
						guide.setForeground(Color.GRAY);
						guide.setText("Illegal character detected! Try again. ");
						updateVariables();
					}
					else if (input.length() > 1) {
						
						guide.setForeground(Color.GRAY);
						guide.setText("Multiple inputs detected! Try again. ");
						updateVariables();
					}
					else if (incorrectGuesses.contains(input.toUpperCase())) {
						
						guide.setForeground(Color.GRAY);
						guide.setText("Letter is already guessed! Try another one.");
						updateVariables();
						displayIncorrect();
					}
					else if (progress.toUpperCase().contains(input.toUpperCase())) {
						
						guide.setForeground(Color.GRAY);
						guide.setText("Letter is already guessed! Try another one.");
						updateVariables();
					}
					else {
						System.out.println("\nUnexpected Input Error Found.");
						System.exit(-1);
					}
				}

			//Guess Checker (Correct)
				else if (selectedWord.toLowerCase().contains(input.toLowerCase())) {
					audioCorrect.stop();
					audioCorrect.setFramePosition(0);
					audioCorrect.start();
					att++;
					guide.setForeground(Color.darkGray);
					for (int i = 0; i < wordToGuess.length; i++) {
						if (input.toLowerCase().charAt(0) == wordToGuess[i]) {
							guesses[i] = input.toLowerCase().charAt(0);
							guide.setText("Letter \"" + input.toUpperCase() + "\" is correct. Good job!");
						}
					}
					updateVariables();
				}

			//Guess Checker (Incorrect)
				else if (!selectedWord.toLowerCase().contains(input.toLowerCase())) {
					audioIncorrect.stop();
					audioIncorrect.setFramePosition(0);
					audioIncorrect.start();
					
					att++;
					lives--;
					guide.setForeground(Color.darkGray);
					guide.setText("Letter \"" + input.toUpperCase() + "\" does not exist. Try again!"); 
					
					vibrate(mainWindow);

				//Updates Incorrect Guesses
					if (!selectedWord.toUpperCase().contains(input.toUpperCase())) {
						incorrectGuesses += input.toUpperCase();
						if (lives != 0 && !Arrays.equals(guesses, wordToGuess)) {
							incorrectGuesses += ", ";
						}
					}

					updateVariables();
				}

			}
			
			winCheck();

		}

	//Keyboard Input Handler
		else if (e.getActionCommand().length() == 1) {

			keyboardWindow.setVisible(false);
			entry.setVisible(true);
			submit.setVisible(true);
			buttonKeyboard.setBounds(5, 349, 22, 14);
			keyboardState = 0;

			command = e.getActionCommand();

			if (!Arrays.equals(guesses, wordToGuess)) {
			
			//Error Handler
				if (incorrectGuesses.contains(command.toUpperCase()) || progress.contains(command.toLowerCase())) {
					
					if (incorrectGuesses.contains(command.toUpperCase())) {
						guide.setForeground(Color.GRAY);
						guide.setText("Letter is already guessed! Try another one.");
						updateVariables();
						displayIncorrect();
					}
					else if (progress.contains(command.toLowerCase())) {
						guide.setForeground(Color.GRAY);
						guide.setText("Letter is already guessed! Try another one.");
						updateVariables();
					}
				}

			//Guess Checker (Correct)
				else if (selectedWord.toLowerCase().contains(command.toLowerCase())) {
					audioCorrect.stop();
					audioCorrect.setFramePosition(0);
					audioCorrect.start();
					att++;
					guide.setForeground(Color.darkGray);
					for (int i = 0; i < wordToGuess.length; i++) {
						if (command.toLowerCase().charAt(0) == wordToGuess[i]) {
							guesses[i] = command.toLowerCase().charAt(0);
							guide.setText("Letter \"" + command.toUpperCase() + "\" is correct. Good job!");
						}
					}
					updateVariables();
				}

			//Guess Checker (Incorrect)
				else if (!selectedWord.toLowerCase().contains(command.toLowerCase())) {
					audioIncorrect.stop();
					audioIncorrect.setFramePosition(0);
					audioIncorrect.start();
					att++;
					lives--;
					guide.setForeground(Color.darkGray);
					guide.setText("Letter \"" + command.toUpperCase() + "\" does not exist. Try again!");
					
					vibrate(mainWindow);

				//Updates Incorrect Guesses
					if (!selectedWord.toUpperCase().contains(command.toUpperCase())) {
						incorrectGuesses += command.toUpperCase();
						if (lives != 0 && !Arrays.equals(guesses, wordToGuess)) {
							incorrectGuesses += ", ";
						}
					}

					updateVariables();
				}
				
			//Game Over
				if (lives == 0) {
					audioLose.setFramePosition(0);
					audioLose.start();
					
					entry.setVisible(false);
					submit.setVisible(false);
					buttonKeyboard.setVisible(false);
					keyboardWindow.setVisible(false);
					endPlayAgain.setVisible(true);
					endHome.setVisible(true);

					losses++;
					gameState = 2;
					updateVariables();
					progressWord.setText("Word to Guess: " + progressDone);
					guide.setForeground(Color.decode("#d10000"));
					guide.setText("Game over! Would you like to try again?");
					JOptionPane.showMessageDialog(mainWindow, "You died! You've used up all your lives. The word was \"" + selectedWord + "\".", "Message", JOptionPane.PLAIN_MESSAGE);
					menuStart.setText("New Game");
				}

			}
			
		//Runs if Selected Word is Complete
			if (Arrays.equals(guesses, wordToGuess)) {
				audioWin.setFramePosition(0);
				audioWin.start();
				
				entry.setVisible(false);
				submit.setVisible(false);
				buttonKeyboard.setVisible(false);
				keyboardWindow.setVisible(false);
				endPlayAgain.setVisible(true);
				endHome.setVisible(true);

				wins++;
				gameState = 1;
				updateVariables();
				progressWord.setText("Word to Guess: " + progressDone);
				guide.setForeground(Color.decode("#00c900"));
				guide.setText("Well done, you get to live another day!");
				JOptionPane.showMessageDialog(mainWindow, "You got the word in " + att + " attempts with " + lives + " lives left. Congratulations!", "Message", JOptionPane.PLAIN_MESSAGE);
				menuStart.setText("New Game");
			}

		}

	}
	
//Main
	public static void main (String [] args) {
		
		ClassLoader cl = Game.class.getClassLoader();
        String f = cl.getResource("").getFile();
        String dir = System.getProperty("user.dir");

        File cwd = new File(f);

        if (cwd.toString().endsWith("!"))
            cwd = cwd.getParentFile();

        System.out.println(cwd);
        System.out.println(dir);
		
		/*-----Changes Default Key to Enter-----*/
		UIManager.put("Button.focusInputMap", new UIDefaults.LazyInputMap(new
			Object[] {
			"ENTER", "pressed",
			"released ENTER", "released"
			}));
		
		/*-----Changes Default Font of OptionPane to Century Gothic-----*/
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(  
		          "Century Gothic", Font.BOLD, 13))); 
		
		/*-----Initializes Game-----*/
		new Game();
	}
	
//Vibrate Window
	
	static int vibrationLength = 3;
    static int vibrationSpeed = 1;
	
    public static void vibrate(JFrame mainWindow) {
        try {
            final int originalX = mainWindow.getLocationOnScreen().x;
            final int originalY = mainWindow.getLocationOnScreen().y;
            for (int i = 0; i < vibrationLength; i++) {
                Thread.sleep(10);
                mainWindow.setLocation(originalX, originalY + vibrationSpeed);
                Thread.sleep(10);
                mainWindow.setLocation(originalX, originalY - vibrationSpeed);
                Thread.sleep(10);
                mainWindow.setLocation(originalX + vibrationSpeed, originalY);
                Thread.sleep(10);
                mainWindow.setLocation(originalX, originalY);
            }
        } 
        
        catch (Exception err) {
            err.printStackTrace();
        }
   }

//Keyboard Buttons Initialization
  	public void createButtons(JPanel keyPanel) {
  		JButton[] buttons = new JButton[26];
  		String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
  				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
  				"W", "X", "Y", "Z" };

  		for (int i = 0; i < buttons.length; i++) {
  			buttons[i] = new JButton(letters[i]);
  			buttons[i].setPreferredSize(new Dimension(27, 20));
  			buttons[i].setFocusPainted(false);
  			buttons[i].setBorder(BorderFactory.createRaisedBevelBorder());
  			buttons[i].setBackground(Color.decode("#fafafa"));
  			buttons[i].setMargin(new Insets(0, 0, 0, 0));
  			buttons[i].setFont(new Font("Century Gothic", Font.BOLD, 9));
  			buttons[i].setActionCommand(letters[i]);
  			buttons[i].addActionListener(this);
  			
  			buttons[i].addMouseListener(new java.awt.event.MouseAdapter() {
  				
  				public void mouseEntered (java.awt.event.MouseEvent me) {
  					JButton event = (JButton) me.getSource();
  					event.setBorder(BorderFactory.createLineBorder(Color.decode("#fafafa"), 2));
  					audioHoverKeyboard.stop();
  					audioHoverKeyboard.setFramePosition(0);
  					audioHoverKeyboard.start();
  				}
  				
  				public void mousePressed (java.awt.event.MouseEvent me) {
  					JButton event = (JButton) me.getSource();
  					event.setBorder(BorderFactory.createLoweredBevelBorder());
  					event.setContentAreaFilled(false);
  				}
  				
  				public void mouseExited (java.awt.event.MouseEvent me) {
  					JButton event = (JButton) me.getSource();
  					event.setBorder(BorderFactory.createRaisedBevelBorder());
  					event.setContentAreaFilled(true);
  				}
  				
  			});

  			keyPanel.add(buttons[i]);
  		}
  	}
  
  //Information Updater
  	public void updateVariables() {
  		printHangman();
  		reprintWord();
  		entry.setText(null);
  		scoreCount.setText("W: " + wins + " L: " + losses + "  ");
  		progressWord.setText("Word to Guess: " + progress);
  		attemptCount.setText("Attempt #" + att);
  		lifeCount.setText("Lives left: " + lives);
  	}

  //Replaces Selected Word With Question Marks
  	public void printWord() {
  		progress = "";
  		selectedWord = getWord();
  		progressDone = selectedWord.replaceAll(".(?!$)", "$0 ");
  		wordToGuess = selectedWord.toLowerCase().toCharArray();
  		guesses = new char[wordToGuess.length];
  		for (int i = 0; i < guesses.length; i++) {
  			guesses[i] = '?';
  			progress += guesses[i] + " ";
  		}
  	}

//Updates Progress
  	public void reprintWord() {
  		progress = "";
  		for (int i = 0; i < guesses.length; i++) {
  			progress += guesses[i] + " ";
  		}
  	}

//Displays Guessed Word
  	public void displayIncorrect() {
  		progressWord.setText("Incorrect Guesses: " + incorrectGuesses.toLowerCase());
  		javax.swing.Timer timer = new javax.swing.Timer (3000, e -> {
  			progressWord.setText("Word to Guess: " + progress);
  		});
  		timer.setRepeats(false);
  		timer.start();
  	}

//Checks if Game is Won or Lost
  	public void winCheck() {
  	//Game Over
  		if (lives == 0) {
  			audioLose.setFramePosition(0);
  			audioLose.start();
  			
  			entry.setVisible(false);
  			submit.setVisible(false);
  			buttonKeyboard.setVisible(false);
  			keyboardWindow.setVisible(false);
  			endPlayAgain.setVisible(true);
  			endHome.setVisible(true);

  			losses++;
  			gameState = 2;
  			updateVariables();
  			progressWord.setText("Word to Guess: " + progressDone);
  			guide.setForeground(Color.decode("#d10000"));
  			guide.setText("Game over! Would you like to try again?");
			JOptionPane.showMessageDialog(mainWindow, "You died! You've used up all your lives. The word was \"" + selectedWord + "\".", "Message", JOptionPane.PLAIN_MESSAGE);
  			menuStart.setText("New Game");
  		}

    //Runs if Selected Word is Complete
  		else if (Arrays.equals(guesses, wordToGuess) || input.trim().equalsIgnoreCase(selectedWord)) {
  			audioWin.setFramePosition(0);
  			audioWin.start();
  			
  			entry.setVisible(false);
  			submit.setVisible(false);
  			buttonKeyboard.setVisible(false);
  			keyboardWindow.setVisible(false);
  			endPlayAgain.setVisible(true);
  			endHome.setVisible(true);

  			wins++;
  			gameState = 1;
  			updateVariables();
  			progressWord.setText("Word to Guess: " + progressDone);
  			guide.setForeground(Color.decode("#00c900"));
  			guide.setText("Well done, you get to live another day!");
  			JOptionPane.showMessageDialog(mainWindow, "You got the word in " + att + " attempts with " + lives + " lives left. Congratulations!", "Message", JOptionPane.PLAIN_MESSAGE);
  			menuStart.setText("New Game");
  		}
  	}
  	
//Random Word Selector from WordList
  	Random shuffle = new Random();
  	String [] wordFile;
  	
  	public String getWord() {
  		wordFile = textFile();

  		int n = wordFile.length;
  		int r = shuffle.nextInt(n);
  		String word = wordFile[r];
  		
  		return word;
  	}
  
//File Reader
  	public String[] textFile() {
		BufferedReader reader = null;
		String wordBank = getRandomList();
		System.out.println(wordBank);
		ArrayList<String> wordList = new ArrayList<String>();
		
		try {			
			URL resource = getClass().getClassLoader().getResource(wordBank);
			reader = new BufferedReader(new InputStreamReader(resource.openStream()));
			
			String s = null;
			while ((s = reader.readLine()) != null) {
				wordList.add(s);
			}
		} 
		
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} 
		
		finally {
			try {
				reader.close();
			} 
			catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(-1);
			}
		} 
		
		return wordList.toArray(new String[wordList.size()]);
	}
  	
//Selects Random Word List
  	public String getRandomList() {
  		
  		String chosenList = "";		

        int i = shuffle.nextInt(5);
			switch (i) {
			
				case 0:
					String filePath0 = "WordLists/ourPlanets.txt";
					chosenList = filePath0;
					hint.setText("Hint: A Planet in our Solar System (Easy)");
					break;
				
				case 1:
					String filePath1 = "WordLists/basicWords.txt";
			  		chosenList = filePath1;
				    hint.setText("Hint: An English Word (Random)");
				    break;
				
				case 2:
					String filePath2 = "WordLists/codingJargons.txt";
					chosenList = filePath2;
				    hint.setText("Hint: A Programming Term (Hard)");
				    break;
				
				case 3:
					String filePath3 = "WordLists/dataTypes.txt";
					chosenList = filePath3;
				    hint.setText("Hint: A Java Data Type (Medium)");
				    break;
				
				case 4:
					String filePath4 = "WordLists/fruitList.txt";
					chosenList = filePath4;					
				    hint.setText("Hint: A Popular Fruit (Easy)");
				    break;
				    
			}
			
  		return chosenList;
  	}

//Hangman Art
	public void printHangman() {
		switch (lives) {
			case 6:
				progressVisual.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/HangmanArt/start.png")));
			break;

			case 5:
				progressVisual.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/HangmanArt/1.png")));
			break;

			case 4:
				progressVisual.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/HangmanArt/2.png")));
			break;

			case 3:
				progressVisual.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/HangmanArt/3.png")));
			break;

			case 2:
				progressVisual.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/HangmanArt/4.png")));
			break;

			case 1:
				progressVisual.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/HangmanArt/5.png")));
			break;

			default:
				progressVisual.setIcon(new ImageIcon(Game.class.getClassLoader().getResource("Images/HangmanArt/game_over.png")));
			break;
		}
	}
	
//For Audio
	
	Clip audioHoverKeyboard;
	Clip audioHoverMenu;
	Clip audioPressedMenu;
	Clip audioKeyboardOpen;
	Clip audioKeyboardClose;
	Clip audioCorrect;
	Clip audioIncorrect;
	Clip audioWin;
	Clip audioLose;
	
	URL urlMenuHover = Game.class.getClassLoader().getResource("Sounds/Hovers/audioHoverMenu.wav");
	URL urlMenuPressed = Game.class.getClassLoader().getResource("Sounds/Pressed/audioPressedMenu.wav");
	URL urlKeyHover = Game.class.getClassLoader().getResource("Sounds/Hovers/audioHoverKeyboard.wav");
	URL urlKeyboardOpen = Game.class.getClassLoader().getResource("Sounds/Pressed/audioKeyboardOpen.wav");
	URL urlKeyboardClose = Game.class.getClassLoader().getResource("Sounds/Pressed/audioKeyboardClose.wav");
	URL urlCorrect = Game.class.getClassLoader().getResource("Sounds/Game/audioCorrect.wav");
	URL urlIncorrect = Game.class.getClassLoader().getResource("Sounds/Game/audioIncorrect.wav");
	URL urlWin = Game.class.getClassLoader().getResource("Sounds/Game/audioWin.wav");
	URL urlLose = Game.class.getClassLoader().getResource("Sounds/Game/audioLose.wav");
	
  	public void initializeSounds() {
  		try {  	
  			
  	        AudioInputStream hoverMenu = AudioSystem.getAudioInputStream(urlMenuHover);
  	        audioHoverMenu = AudioSystem.getClip();
  	        audioHoverMenu.open(hoverMenu);
  	        
  	        AudioInputStream pressedMenu = AudioSystem.getAudioInputStream(urlMenuPressed);
  	        audioPressedMenu = AudioSystem.getClip();
  	        audioPressedMenu.open(pressedMenu);
  	        
  	        AudioInputStream hoverKeys = AudioSystem.getAudioInputStream(urlKeyHover);
  	        audioHoverKeyboard = AudioSystem.getClip();
  	        audioHoverKeyboard.open(hoverKeys);
  	        
  	        AudioInputStream openKeyboard = AudioSystem.getAudioInputStream(urlKeyboardOpen);
	        audioKeyboardOpen = AudioSystem.getClip();
	        audioKeyboardOpen.open(openKeyboard);
	        
	        AudioInputStream closeKeyboard = AudioSystem.getAudioInputStream(urlKeyboardClose);
	        audioKeyboardClose = AudioSystem.getClip();
	        audioKeyboardClose.open(closeKeyboard);
  	        
  	        AudioInputStream correct = AudioSystem.getAudioInputStream(urlCorrect);
  	        audioCorrect = AudioSystem.getClip();
  	        audioCorrect.open(correct);
  	        
  	        AudioInputStream incorrect = AudioSystem.getAudioInputStream(urlIncorrect);
  	        audioIncorrect = AudioSystem.getClip();
  	        audioIncorrect.open(incorrect);  	         	
  	        
  	        AudioInputStream win = AudioSystem.getAudioInputStream(urlWin);
  	        audioWin = AudioSystem.getClip();
  	        audioWin.open(win);
  	        
  	        AudioInputStream lose = AudioSystem.getAudioInputStream(urlLose);
  	        audioLose = AudioSystem.getClip();
  	        audioLose.open(lose);
  	    }
  		
  	    catch(Exception e) {
  	        e.getMessage();
  	    }
  	}

}



