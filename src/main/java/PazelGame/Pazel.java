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



    int m;
    int n;

    public Pazel() {

        Container c = getContentPane();
        c.setLayout(new GridLayout(5, 4));

        ButtonListener cl = new ButtonListener();
        ButtonListener button = new Resultbutton();


        name = (String) JOptionPane.showInputDialog(
                this,
                "Enter your name",  //user prompt
                "Input Name",  //title text
                JOptionPane.PLAIN_MESSAGE);
        while (name.isBlank()) {
            JOptionPane.showMessageDialog(this,
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
        diplayResult.setBackground(Color.red);
        diplayResult.setFont(new Font("Serif", Font.PLAIN, 60));
        c.add(diplayResult);
        result.addActionListener(button);
        addnewData();


    }
    
    public static void main(String args[]) {
        database = new GameDB(DB_PATH);
        Pazel game = new Pazel();
        game.setTitle("Puzzle");
        game.setVisible(true);
        game.setSize(400, 400);
        game.shuffle();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



    }

    public void shuffle() {
        boolean[] shuffeled = new boolean[16];;
        for (int i = 0; i < 4; i++) { //4 culoms
            for (int j = 0; j < 4; j++) { //4 rows
                int val = (int) (16 * Math.random());
                while (shuffeled[val]) {
                    val = (int) (16 * Math.random());
                }
                shuffeled[val] = true;
                if (val != 0)  //setup the lable on each button
                    possition[i][j].setText("" + val);
                else {
                    possition[i][j].setBackground(Color.gray);
                    m = i;
                    n = j;
                }
            }
        }
    }


    class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object square = e.getSource();
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
                possition[m][n].setText(possition[i][j].getText());
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
    public void addnewData() {
        String playername = this.name;
        int score = this.counter;

        GameData gamedata = new GameData(playername, score);
        database.addDate(gamedata);

    }
    public void UpdateData() {
        String playername = this.name;
        int score = this.counter;

        GameData gamedata = new GameData(playername, score);
        database.update(gamedata);
    }
    public static void writedatatoFile(String name, int counter) {
        String filename = "Results.txt";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter((filename)))) {
            bufferedWriter.write("Player " + name+ " has done " + counter + " attempts but not completed the puzzle yet!");
        } catch (IOException e) {
            System.out.println(e);


        }
    }

    private class Resultbutton extends ButtonListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Desktop.getDesktop().open(new File("Results.txt"));
            } catch (IOException d) {
                System.out.println(d);
            }
        }

    }
}

