package PazelGame;

import java.sql.*;

public class GameDB {
    private String databasePath;
    GameDB(String databasePath) {
        this.databasePath= databasePath;
        try (Connection connection = DriverManager.getConnection(databasePath);
            Statement statement = connection.createStatement()){
           String sql = "CREATE TABLE IF NOT EXISTS gameresult (name text NOT NULL, attempts integer )";
            statement.execute(sql);

        } catch (SQLException e) {
            System.out.println("Cannot create the table because "+ e);
        }
    }
    public void addDate(GameData gamedata) {
        String insertSql = "INSERT INTO gameesult values (?,?)";
        try (Connection connection = DriverManager.getConnection(databasePath);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSql)){

            preparedStatement.setString(1,gamedata.getName());
            preparedStatement.setInt(2,gamedata.getCounter());
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Cannot add the results to the table because "+ e);
        }


    }
    public void update(GameData gamedata) {
        String Insertsql = "UPDATE gameresult SET attempts= ? WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(databasePath);
             PreparedStatement preparedStatement = connection.prepareStatement(Insertsql)) {

            preparedStatement.setInt(1, gamedata.getCounter());
            preparedStatement.setString(2, gamedata.getName());


            preparedStatement.execute();


        } catch (SQLException e) {
            System.out.println("Cannot add the results to the table because " + e);
        }
    }

    }
