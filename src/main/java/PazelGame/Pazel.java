/* Main paige
Mohammad Sargazi 12/14/2020
This game saves the user attempts and writ them into
SQlite and a text file. by pressing the Results button,
the program will open a text file and display it to the user.

//TODO (in the future) when user starts a new game, he/she should be able to see the previous results in the text file
//TODO (in the future) modift the button sizes.
// TODO (in the future) results must be displayed using a better GUI
//TODO(in the future) Display something when user finishes the game


Sources used in this program
www.javatpoint.com - https://www.javatpoint.com/Puzzle-Game
Anjali Prasad - https://justtsorandoms.blogspot.com/2018/12/the-game-of-fifteen-or-15-puzzle-using.html
https://stackoverflow.com/
https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html

 */


package PazelGame;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Pazel extends JFrame {
    private static final String DB_PATH = "jdbc:sqlite:results.sqlite";
    private static GameDB database;
    // TODO Setup the buttons
    //get all the buttons
    int counter = 0;
    JButton[][] possition = new JButton[4][4]; //creat button in i and j mode (means creat 4 columns and 4 rows)
    JButton result = new JButton("Results");
    JLabel diplayResult = new JLabel(String.valueOf(counter));
    String name;
    JLabel displayname = new JLabel(name);
    int m;
    int n;
    public Pazel() {
        Container c = getContentPane();// method retrieves the content pane layer so that you can add an object to it.
        c.setLayout(new GridLayout(5, 4));
        ButtonListener cl = new ButtonListener();  //method to call button action
        ButtonListener button = new Resultbutton(); // calling for the second buttonListener method that does different thing.
        name = (String) JOptionPane.showInputDialog(   //method for displaying a dialog box this one gets user name and stores it in String name
                this,
                "Enter your name",  //user prompt
                "Input Name",  //title text
                JOptionPane.PLAIN_MESSAGE);
        while (name.isBlank()) {
            JOptionPane.showMessageDialog(this,  //This method display an error with only a "OK" button to dismiss.
                    "Name cannot be blank. try again.");
            name = JOptionPane.showInputDialog(
                    this,
                    "Enter your name",  //user prompt
                    "Input Name",  //title text
                    JOptionPane.PLAIN_MESSAGE);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JButton n = new JButton();
                possition[i][j] = n;
                possition[i][j].addActionListener(cl);
                possition[i][j].setBackground(Color.pink);
                c.add(possition[i][j]);

            }//c.add(start);
            result.setBackground(Color.pink);
            result.setSize(300, 100);
            c.add(result);

        }
        displayname.setFont(new Font("Serif", Font.PLAIN,20));
        displayname.setBackground(Color.cyan);
        displayname.setText(name+":");
        c.add(displayname);

        diplayResult.setFont(new Font("Serif", Font.PLAIN, 60));
        c.add(diplayResult);
        result.addActionListener(button);
        addNewData();
    }

    public static void main(String args[]) {
        database = new GameDB(DB_PATH);
        Pazel game = new Pazel(); //calls the game to start
        game.setTitle("Puzzle");
        game.setVisible(true);
        game.setSize(400, 400);
        game.shuffle(); //method to shuffle the numbers
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void shuffle() {
        boolean[] shuffeled = new boolean[16];;
        for (int i = 0; i < 4; i++) { //4 culoms
            for (int j = 0; j < 4; j++) { //4 rows
                int val = (int) ( 16 *Math.random()); //Default random method only generates between 0 and 1
//                System.out.println(val);               // so we need to multiply that by 16 to get 16 numbers.
                while (shuffeled[val]) {               //In line 104; 16 represents the maximum of the number we need (0-16)
                    val = (int) (16 * Math.random());   // and in line 107, 16 represents the number of random numbers we need.
                }
                shuffeled[val] = true;
                if (val != 0)  //setup the liable on each button
                    possition[i][j].setText("" + val); // number 0 is our blank spot . so if the number is not 0 display the number
                else {
                    possition[i][j].setBackground(Color.gray); // and if it's 0 don't display anything and only give it a gray background color

                    m = i;
                    n = j;
                }
            }
        }
    }
    class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object square = e.getSource(); // a class that listens to a mouse click in a square
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (possition[i][j] == square) {
                        moves(i, j);
//                                System.out.println(counter);
                        break;
                    }
                }
            }
//                    System.out.println(counter);
        }
        public void moves(int i, int j) {
            if ((i + 1 == m && j == n) || (i - 1 == m && j == n) || (i == m && j + 1 == n) || (i == m && j - 1 == n)) {
                possition[m][n].setText(possition[i][j].getText()); //This method (160) defines the moves and checks which buttons are able to move and have the blank button next to them
                possition[m][n].setBackground(Color.pink);
                possition[i][j].setText("");
                possition[i][j].setBackground(Color.gray);
                m = i;
                n = j;
                counter++;
                diplayResult.setText(String.valueOf(counter));
                UpdateData();
                writedatatoFile(name, counter);
            }
        }
    }
    public void addNewData() {
        String playerName = this.name;
        int score = this.counter;   // This method writes name and score into the database

        GameData gamedata = new GameData(playerName, score);   //
        database.addDate(gamedata);
    }
    public void UpdateData() {
        String playerName = this.name;
        int score = this.counter;  //this method pretty much updates the score in DB

        GameData gamedata = new GameData(playerName, score);
        database.update(gamedata);
    }
    public static void writedatatoFile(String name, int counter) {
        String filename = "Results.txt";        //This method writes a sentece that displays user name and number of attempts in a text file
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter((filename)))) {
            bufferedWriter.write("Player " + name+ " has done " + counter + " attempts but not completed the puzzle yet!");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    private class Resultbutton extends ButtonListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Desktop.getDesktop().open(new File("Results.txt"));//this method is a simple way to open a file by clicking on a button
            } catch (IOException d) {
                System.out.println(d);
            }
        }
    }
}

