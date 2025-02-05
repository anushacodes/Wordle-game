
import java.awt.*;

import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.io.FileWriter;

import javax.swing.*;

import java.nio.file.Paths;
import java.util.Scanner;


public class WordleGame extends JFrame {	

	private JButton result;            // guess button 
    private JLabel scoreLabel;         // keep track of score (chances)
    private JTextField wordField;    // wordle word/textbox
	private JTextField[][] slots;        // each letter of the word is a slot, there are 5 word slots
    private int currentRow = 0;       // to keep track of word row

    private String guess_word;     // stores the word to be guessed
    
	private int chances = 6;         // score tracker
	
    private JButton hint;          // button to show the word

	ArrayList<String> wordList = new ArrayList<String>();    // arraylist to hold words with len = 5

    // arraylist to store player names and their respective scores in scores.txt
    ArrayList<String> playerNames = new ArrayList<>();
    ArrayList<Integer> playerScores = new ArrayList<>();


    // game class
	public WordleGame() {
		
		ListofWords();     // creates an arraylist of all 5 letter words in dictionary.txt
	    
        createComponents();		
		setTitle("Wordle Game");		
		setSize(600, 500);
        setLocationRelativeTo(null);  // Center the window on the screen
	}
		


	private void ListofWords() {
			
	    try (Scanner fileScanner = new Scanner (Paths.get("/Users/anusha/D2L.ai/WordleGame/src/dictionary.txt"))) {
	    	while (fileScanner.hasNextLine()) {
	    		
	    		String word = fileScanner.nextLine();
	    		
                if (word.length() == 5 && !word.contains("-")) // there may be some words with '-' we don't want those
                    wordList.add(word);                    
	    	}	
	    }
	        
	    catch (Exception e) {
	    	System.out.println("Error while reading dictionary.txt: " + e.toString());
	    }
	    

        // to ensure wordList is not empty
        if (wordList.isEmpty()) {
            System.out.println("Error: Word list is empty!!");
            return;
        }

        // to randomly select word from the dictionary
	    Random random = new Random();
	    int n = random.nextInt(wordList.size());    // random index 
	    
	    guess_word = wordList.get(n).toUpperCase();     // 1 - aback (replace with n later)
	}
	
	
   private void createComponents() {	   
	   
       // to keep track of lives left (score)
       scoreLabel = new JLabel("Lives left: " + chances);
       scoreLabel.setHorizontalAlignment(JLabel.CENTER);   // aligning to center 
       scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));


	   // button to check word inputted
	   result = new JButton("Guess");	   
	   result.setPreferredSize(new Dimension(100, 30)); // to make size of the button smaller

       result.addActionListener(new wordChecker());    
       

       // main game board panel - boxLayout
	    JPanel wordPanel = new JPanel();
	    wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.Y_AXIS));   // horizontal layout
        

        // a grid to hold the word slots 
        slots = new JTextField[6][5];

        // creating 6 slots for each word
        for (int i = 0; i < 6; i++) {
            // to hold each letter of the word - rowPanel
            JPanel rowPanel = new JPanel(new FlowLayout());
            // each letter of a word is a slot
            for (int j = 0; j < 5; j++) {
                slots[i][j] = new JTextField(2);     // text field for each letter, width of 2 chars   
                slots[i][j].setEditable(false);            // slots are non-editable    
                slots[i][j].setHorizontalAlignment(JTextField.CENTER);    
                rowPanel.add(slots[i][j]);                  // adding each slot to rowPanel
            }

            wordPanel.add(rowPanel);                // adding each row to wordPanel
        }
        
        // sets the layout to add the components to the frame
	        // north - score
	        // centre - slots
	        // south - i/p textbox + guess button + hint button  
        setLayout(new BorderLayout(10, 10)); 
        
        
        // adds score (chances) label to the NORTH frame
        add(scoreLabel, BorderLayout.NORTH);

        
	    // adds the wordPanel to the CENTER frame
	    add(wordPanel, BorderLayout.CENTER);       		 

	    
        // creating a panel to hold the i/p field and the guess button
        JPanel inputPanel = new JPanel();
        
        // flowLayout to pad the inputPanel from the bottom
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50)); 
        
        
        // creating the word i/p field
        wordField = new JTextField(7);  // size = 7 but only takes 5 letter word as i/p
        wordField.setEditable(true);
        
        //wordField.setFont(new Font("Arial", Font.PLAIN, 18));
        
        inputPanel.add(wordField);  
        

        // a button to show the guess word
        hint = new JButton("Show word");

        //hint.setPreferredSize(new Dimension(90, 30));

        hint.addActionListener(new giveHint());  

        // adds the guess and hint button next to wordField
        inputPanel.add(result); 
        inputPanel.add(hint);

        // adding the i/p panel to the SOUTH frame
        add(inputPanel, BorderLayout.SOUTH);  

   }

    //  to check if a word is valid ( if it exists in the word list)
    private boolean isValidWord(String word) {
        return wordList.contains(word.toLowerCase()); 
    }
    
    class giveHint implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(WordleGame.this, "the word is: " + guess_word);
        }
    }

  // to check if word inputted is valid (guess button actionListener)
   class wordChecker implements ActionListener {

       @Override
       public void actionPerformed(ActionEvent e) {
    	   
    	   // fetches the word from text field and convert it to uppercase
           String entered_word = wordField.getText().toUpperCase();  

           
           // checks if the entered word is valid (length and if it's an actual word)
           if (guess_word.length() != 5 || !isValidWord(entered_word)) {
        	   
        	   // error message
               JOptionPane.showMessageDialog(WordleGame.this, "Please enter a valid word.", 
            		   "Invalid input", JOptionPane.ERROR_MESSAGE);
                       
                       
                wordField.setText("");   // resets i/p field               
               return;
           }

        // if the word is completed, display win message
          if (entered_word.equals(guess_word)) {

            JOptionPane.showMessageDialog(WordleGame.this, "You Win :D", "Congratulations!", 
            		  JOptionPane.INFORMATION_MESSAGE);
                      
            saveScore(chances);  // save score when winning
            displayTopScores();  // show the top score

            resetGame();
            return;
          }

           // iterate through word and check if equal to letter + update color
           for (int i = 0; i < 5; i++) {
            slots[currentRow][i].setText(String.valueOf(entered_word.charAt(i)));  // displays guessed word in the grid

            // if the letter is correct and in the correct position, set bg to green
            if (entered_word.charAt(i) == guess_word.charAt(i)) {
                slots[currentRow][i].setBackground(Color.GREEN);
            }

            // if the letter is in the word but in the wrong position, set bg to yellow
            else if (guess_word.contains(String.valueOf(entered_word.charAt(i)))) {
                slots[currentRow][i].setBackground(Color.YELLOW);
            }

            // if the letter is not in the word, set bg to gray
            else {
                slots[currentRow][i].setBackground(Color.GRAY);
            }
        }
           
	        // if the guessed word wasn't correct, decrement chances and update score
	        chances--;
	        scoreLabel.setText("Lives left: " + chances);

            // clears the i/p field after each guess
            wordField.setText("");  
	     	      
            currentRow++;  // move to the next row for the next guess

	      
	      // if chances = 0 then the player loses
	      if (chances == 0) {
              JOptionPane.showMessageDialog(WordleGame.this, "You lost. The word was: " + guess_word, 
                      "Game Over :(", JOptionPane.ERROR_MESSAGE);
                
                // we don't need to save score when losing 
                // but we need to save it otherwise it doesn't display any scores :/
                saveScore(0); 
                displayTopScores();
                
                resetGame();
                return;
	      }
	      
       }
       
       // to reset the game
       private void resetGame() {
           chances = 6;
           scoreLabel.setText("Lives left: " + chances);

           // clearing all the text slots and reset colors to white
            for (int i = 0; i < 6; i++) {

                for (int j = 0; j < 5; j++) {             
                    slots[i][j].setText("");  
                    slots[i][j].setBackground(Color.WHITE);  
                }
            }

            wordField.setText("");   // resets i/p field 
            currentRow = 0;     // resets row number
            ListofWords();    // new word for a new game
       }


       // to save scores in scores.txt
       // i only added 2 dummy players for now so the current player is always displayed
       private void saveScore(int chances) {
        playerNames.add("you");
        playerScores.add(chances);

        // dummy people with dummy scores 
        playerNames.add("tim");
        playerScores.add(5);

        playerNames.add("rammy");
        playerScores.add(3);

        // bubble sorting the player scores in descending order 
        for (int i = 0; i < playerScores.size() - 1; i++) {

            for (int j = 0; j < playerScores.size() - i - 1; j++) {

                if (playerScores.get(j) < playerScores.get(j + 1)) {

                //swap scores
                    int tempScore = playerScores.get(j);

                    playerScores.set(j, playerScores.get(j + 1));
                    playerScores.set(j + 1, tempScore);

                // swapping player names accordingly
                    String tempName = playerNames.get(j);

                    playerNames.set(j, playerNames.get(j + 1));
                    playerNames.set(j + 1, tempName);
                }
            }
        }

        // writing scores to scores.txt file in the format (name, scores)
        try (FileWriter scoreWriter = new FileWriter("/Users/anusha/D2L.ai/WordleGame/src/scores.txt")) {
            for (int i = 0; i < playerNames.size(); i++) {
                scoreWriter.write(playerNames.get(i) + "," + playerScores.get(i));
                scoreWriter.write("\n");
            }
        } 
        
        catch (Exception e) {
            System.out.println("scores.txt threw an error: " + e.toString());
        }
    }

  // method to display top 3 scores in a dialog box
    private void displayTopScores() {
        String topScorers = "Top Scores:\n";
        for (int i = 0; i < 3; i++) {
            topScorers += playerNames.get(i) + ": " + playerScores.get(i) + "\n";
        }
        JOptionPane.showMessageDialog(WordleGame.this, topScorers);
    }
}

    // main
    public static void main(String[] args) {
            JFrame frame = new WordleGame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            
        }
}