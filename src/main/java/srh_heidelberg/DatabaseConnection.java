package srh_heidelberg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance = null;
    public static Connection singletonConnectionToDb;

    private DatabaseConnection(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            singletonConnectionToDb = DriverManager.getConnection("jdbc:mysql://localhost:3306/money_pool", "root", "qwe098123");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static DatabaseConnection DatabaseConnection(){

        if(instance == null){
            instance = new DatabaseConnection();
        }

        return instance;
    }


}
