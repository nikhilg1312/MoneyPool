package srh_heidelberg;


import srh_heidelberg.model.Member;
import srh_heidelberg.model.PoolDetails;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyPoolDetails {

    private static PreparedStatement preparedStatement = null;
    private static DatabaseConnection databaseConnection = DatabaseConnection.DatabaseConnection();

    public void fetchMyPoolDetails(Member member){


        try {
            preparedStatement = databaseConnection.singletonConnectionToDb.prepareStatement("SELECT pooldetails.* FROM pooltransactions JOIN pooldetails  on pooltransactions.PoolID = pooldetails.PoolID WHERE pooltransactions.MemberID = ?");
            preparedStatement.setInt(1,member.getMemberID());
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 1;
            while (resultSet.next()){
                System.out.println("Pool "+count);
                System.out.println("Pool ID: "+resultSet.getInt(1));
                System.out.println("Admin ID"+resultSet.getInt(2));
                System.out.println("Pool Name: " +resultSet.getString(3));
                System.out.println("Duration: "+resultSet.getInt(4));
                System.out.println("Pool Strength: "+resultSet.getInt(5));
                System.out.println("Pool Individual Share "+resultSet.getDouble(7));
                System.out.println("Pool Monthly TakeAway: "+resultSet.getDouble(8));
                System.out.println("Pool Start Date: "+resultSet.getDate(9));
                System.out.println("Pool End Date: "+resultSet.getDate(10));
                System.out.println("Pool MeetUp Date: "+resultSet.getInt(11));
                System.out.println("Pool Deposit Date: "+resultSet.getInt(12));

                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayMyPoolDetails(ResultSet resultSet) {
        ArrayList poolDetailsArrayList = new ArrayList();
        PoolDetails poolDetails = new PoolDetails();
        try {
            while (resultSet.next()) {

            }
        }catch (SQLException e){
        e.printStackTrace();
        }
    }


}

