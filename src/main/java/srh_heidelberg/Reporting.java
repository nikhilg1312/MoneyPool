package srh_heidelberg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;


public class Reporting {
    private static DatabaseConnection databaseConnection = DatabaseConnection.DatabaseConnection();
    private static PreparedStatement preparedStatement = null;


    public static void memberTransactions(int memberID){
        try {

            preparedStatement = DatabaseConnection.singletonConnectionToDb.prepareCall("SELECT  PoolID,MemberID,IndividualMonthlyShare,PaymentDate FROM money_pool.pooltransactions where MemberID = ? and PaymentDate IS NOT null ");
            preparedStatement.setInt(1,memberID);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            FastReportBuilder drb = new FastReportBuilder();
            DynamicReport dr = drb.addColumn("Pool ID", "PoolID", Integer.class.getName(), 30)
                    .addColumn("Member ID", "MemberID", Integer.class.getName(), 30)
                    .addColumn("Amount Paid", "IndividualMonthlyShare", String.class.getName(), 30)
                    .addColumn("Payment Date", "PaymentDate", Date.class.getName(), 50)

                    .setTitle("List Of Member Transactions").setSubtitle("This report was generated at " + new Date())
                    .setPrintBackgroundOnOddRows(true).setUseFullPageWidth(true).build();
            JRResultSetDataSource resultsetdatasource = new JRResultSetDataSource(resultSet); // Convert Resultset to Jasper Resultset
            JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), resultsetdatasource);
            JasperViewer.viewReport(jp); // finally display the report report
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }


    public static void investmentReturnsForPools(int memberID){
        try {

            preparedStatement = DatabaseConnection.singletonConnectionToDb.prepareCall("select  MemberID,PoolID,sum(IndividualMonthlyShare),sum(TakeawayAmount) from pooltransactions\n" +
                    "where MemberID = ?\n" +
                    "group by PoolID");
            preparedStatement.setInt(1,memberID);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            FastReportBuilder drb = new FastReportBuilder();
            DynamicReport dr = drb.addColumn("Member ID", "MemberID", Integer.class.getName(), 30)
                    .addColumn("Pool ID", "PoolID", Integer.class.getName(), 30)
                    .addColumn("Total Monthly Investment", "sum(IndividualMonthlyShare)", Double.class.getName(), 50)
                    .addColumn("Total Returns", "sum(TakeawayAmount)", Double.class.getName(), 50)

                    .setTitle("Investment VS Returns").setSubtitle("This report was generated at " + new Date())
                    .setPrintBackgroundOnOddRows(true).setUseFullPageWidth(true).build();
            JRResultSetDataSource resultsetdatasource = new JRResultSetDataSource(resultSet); // Convert Resultset to Jasper Resultset
            JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), resultsetdatasource);
            JasperViewer.viewReport(jp); // finally display the report report
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    public static void winnerPickerReport(int poolID){
        try {

            preparedStatement = DatabaseConnection.singletonConnectionToDb.prepareCall("select ptp.PoolID,ptp.CurrentCounter as Iteration,ptw.MemberID as Winner,ptp.MemberID as Picker,ptp.TakeawayDate,\n" +
                    "ptw.TakeawayAmount as \"Winner Takeaway\",ptp.TakeawayAmount as \"Picker Takeaway\"\n" +
                    "from pooltransactions  as ptw\n" +
                    "JOIN\n" +
                    "pooltransactions as ptp\n" +
                    "ON ptp.PoolID = ptw.PoolID and ptw.CurrentCounter = ptp.CurrentCounter\n" +
                    "where ptw.WinnerFlag = 1 and ptp.PickerFlag = 1\n" +
                    "  and ptp.PoolID = ?\n" +
                    "ORDER by ptp.PoolID,ptp.CurrentCounter");
            preparedStatement.setInt(1,poolID);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            FastReportBuilder drb = new FastReportBuilder();
            DynamicReport dr = drb.addColumn("Pool ID", "PoolID", Integer.class.getName(), 30)
                    .addColumn("Iteration", "CurrentCounter", Integer.class.getName(), 30)
                    .addColumn("Winner MemberID", "MemberID", Integer.class.getName(), 30)
                    .addColumn("Picker MemberID", "MemberID", Integer.class.getName(), 30)
                    .addColumn("Winner Takeaway Amount", "TakeawayAmount", Double.class.getName(), 50)
                    .addColumn("Picker Takeaway Amount", "TakeawayAmount", Double.class.getName(), 50)
                    .setTitle("Auction Report for Pool").setSubtitle("This report was generated at " + new Date())
                    .setPrintBackgroundOnOddRows(true).setUseFullPageWidth(true).build();
            JRResultSetDataSource resultsetdatasource = new JRResultSetDataSource(resultSet); // Convert Resultset to Jasper Resultset
            JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), resultsetdatasource);
            JasperViewer.viewReport(jp); // finally display the report report
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }




}



