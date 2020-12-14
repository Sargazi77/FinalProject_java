package PazelGame;

public class GameData {
    private String name;
    private int counter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    GameData(String name, int counter) {
        this.name = name;
        this.counter = counter;
    }
}
