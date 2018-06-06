package srh_heidelberg;


import srh_heidelberg.model.DateCalculations;
import srh_heidelberg.model.Member;
import srh_heidelberg.model.PoolDetails;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MemberHomePortal {

    private static Scanner scanner = new Scanner(System.in);
    private static Member memberObject = new Member();
    private static PoolDetails tempPool = new PoolDetails();
    private static PreparedStatement preparedStatement = null;
    private static PoolEnrollment poolEnrollment = new PoolEnrollment();
    private static Reporting memberReport = new Reporting();


    public void welcomeMember(Member member) {
        System.out.println("Hallo " + member.getMemberFirstName() + " " + member.getMemberLastName() + "\n ");
        System.out.println("Welcome to MoneyPool Portal");
        System.out.println("Please find your Details Below \n");
        System.out.println("Email : " + member.getMemberEmail());
        System.out.println("Address : " + member.getMemberAddress());
        System.out.println("IBAN: " + member.getMemberIban());
        System.out.println("Swift Code: " + member.getMemberSwiftCode());
        System.out.println("Nominee: " + member.getMemberNominee());
        System.out.println("Phone Number: " + member.getMemberPhoneNumber());
        memberObject = member;
        int memberId = member.getMemberID();
        askForOperation();

    }

    private static void askForOperation() {
        System.out.println("Please Select An option to Proceed: ");
        System.out.println("\n 1. Create Pool \n 2. Join Pool \n 3. Update Member Details \n 4. Work As Pool Admin \n 5. Pool Member \n 6. View Transaction History");
        int option = scanner.nextInt();
        performAction(option);
    }

    private static void performAction(int option) {
        switch (option) {
            case 1:
                createPool();
                break;
            case 2:
                joinPool();
                break;
            case 3:
                updateMemberDetails();
                break;
            case 4:
                workAsPoolAdmin();
                break;
            case 5:
                MyPoolDetails myPoolDetails = new MyPoolDetails();
                myPoolDetails.fetchMyPoolDetails(memberObject);
                break;
            case 6:


        }
    }

    private static void askForReports(){
        System.out.println("\n1.Transaction reports \n2.Investment VS Returns \n3.Back to Member Menu");
        int option = scanner.nextInt();
        performReports(option);
    }

    private static void performReports(int option) {
        switch (option){

            case 1:
                    Reporting.memberTransactions(memberObject.getMemberID());
                    askForReports();
            case 2:
                    Reporting.investmentReturnsForPools(memberObject.getMemberID());
                    askForReports();
            case 3: askForOperation();
        }
    }


    private static void updateMemberDetails() {
        System.out.println("Selection the field to update :");
        System.out.println("1. Name \n 2. Email \n 3. Password \n 4. Address");
        System.out.println("5.IBAN \n 6. Swift Code \n 7. Nominee \n 8. Phone Number \n 9. Exit ");
        int option = scanner.nextInt();
        updateOperation(option);
    }

    private static void workAsPoolAdmin() {
        PoolAdminPage pageAdmin = new PoolAdminPage();
        pageAdmin.AdminOperationSelect(memberObject.getMemberID());

    }


    private static void joinPool() {

        System.out.println("Please Enter the Pool ID you want to join for : ");
        int poolid = scanner.nextInt();
        poolEnrollment.getPoolDetails(poolid, memberObject);

    }

    private static void createPool() {

        System.out.println("Enter Pool Name : ");
        String poolName = scanner.next();
        tempPool.setPoolName(poolName);

        System.out.println("Enter Pool Duration : ");
        tempPool.setDuration(scanner.nextInt());
        tempPool.setStrength(tempPool.getDuration());

        System.out.println("Enter Individual Contribution Amount : ");
        tempPool.setIndividualShare(scanner.nextDouble());
        tempPool.setMonthlyTakeaway(tempPool.getDuration() * tempPool.getIndividualShare());

        System.out.println("Enter on which day of every month Meet up will be planned : ");
        tempPool.setMeetupDate(scanner.nextInt());

        System.out.println("Enter before which day of every month Contribution has to made : ");
        tempPool.setDepositDate(scanner.nextInt());

        System.out.println("Enter Late Payment percent charge : ");
        tempPool.setLateFeeCharge(scanner.nextFloat());

        System.out.println("Enter Start date in dd/MM/yyyy");
        String StrDate = scanner.next();

        java.util.Date javaStartdate = DateCalculations.stringToDateParse(StrDate);
        java.sql.Date sqlStartDate = new java.sql.Date(javaStartdate.getTime());


        java.util.Date EndDate = DateCalculations.addMonth(javaStartdate, tempPool.getDuration());
        java.sql.Date sqlEndDate = new java.sql.Date(EndDate.getTime());

        tempPool.setStartDate(sqlStartDate);
        tempPool.setEndDate(sqlEndDate);
        tempPool.setPoolAdminMemberID(memberObject.getMemberID());

        loadPoolToDB(tempPool);

        int poolID = getPoolID(tempPool);
        poolEnrollment.getPoolDetails(poolID, memberObject);

    }

    private static void updateOperation(int option) {

        UpdateMemberDetailsInDB updateMemberDetailsInDB = new UpdateMemberDetailsInDB();

        switch (option) {
            case 1:
                updateMemberDetailsInDB.updateName(memberObject);
                updateMemberDetails();
                break;
            case 2:
                updateMemberDetailsInDB.updateEmail(memberObject);
                updateMemberDetails();
                break;
            case 3:
                updateMemberDetailsInDB.updatePassword(memberObject);
                updateMemberDetails();
                break;
            case 4:
                updateMemberDetailsInDB.updateAddress(memberObject);
                updateMemberDetails();
                break;
            case 5:
                updateMemberDetailsInDB.updateIban(memberObject);
                updateMemberDetails();
                break;
            case 6:
                updateMemberDetailsInDB.updateSwiftCode(memberObject);
                updateMemberDetails();
                break;
            case 7:
                updateMemberDetailsInDB.updateNominee(memberObject);
                updateMemberDetails();
                break;
            case 8:
                updateMemberDetailsInDB.updatePhoneNumber(memberObject);
                updateMemberDetails();
                break;
            case 9:
                break;
            default:
                break;

        }
    }

    private static int getPoolID(PoolDetails pool){
        int poolID = 0;
        try {
            preparedStatement = DatabaseConnection.singletonConnectionToDb.prepareCall("select PoolID from pooldetails " +
                    "where PoolName = ? and Duration = ? and Strength= ? and IndividualShare = ? and MonthlyTakeaway = ? and MeetupDate =? " +
                    "and DepositDate = ? and LateFeeCharge = ? and StartDate = ? and EndDate = ? and PoolAdminMemberID = ?");
            preparedStatement.setString(1, pool.getPoolName());
            preparedStatement.setInt(2, pool.getDuration());
            preparedStatement.setInt(3, pool.getStrength());
            preparedStatement.setDouble(4, pool.getIndividualShare());
            preparedStatement.setDouble(5, pool.getMonthlyTakeaway());
            preparedStatement.setInt(6, pool.getMeetupDate());
            preparedStatement.setInt(7, pool.getDepositDate());
            preparedStatement.setFloat(8, pool.getLateFeeCharge());
            preparedStatement.setDate(9, pool.getStartDate());
            preparedStatement.setDate(10, pool.getEndDate());
            preparedStatement.setInt(11, pool.getPoolAdminMemberID());
            preparedStatement.execute();
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            poolID = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poolID;
    }

    private static void loadPoolToDB(PoolDetails Pool) {

        try {
            preparedStatement = DatabaseConnection.singletonConnectionToDb.prepareStatement("INSERT INTO pooldetails(PoolName,Duration,Strength," +
                    "IndividualShare,MonthlyTakeaway,MeetupDate,DepositDate,LateFeeCharge,StartDate,EndDate,PoolAdminMemberID,CurrentCounter)" +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, Pool.getPoolName());
            preparedStatement.setInt(2, Pool.getDuration());
            preparedStatement.setInt(3, Pool.getStrength());
            preparedStatement.setDouble(4, Pool.getIndividualShare());
            preparedStatement.setDouble(5, Pool.getMonthlyTakeaway());
            preparedStatement.setInt(6, Pool.getMeetupDate());
            preparedStatement.setInt(7, Pool.getDepositDate());
            preparedStatement.setFloat(8, Pool.getLateFeeCharge());
            preparedStatement.setDate(9, Pool.getStartDate());
            preparedStatement.setDate(10, Pool.getEndDate());
            preparedStatement.setInt(11, Pool.getPoolAdminMemberID());
            preparedStatement.setInt(12,1);

            preparedStatement.executeUpdate();
            System.out.println("Registration Successful");


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}