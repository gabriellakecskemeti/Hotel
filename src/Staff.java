import java.security.NoSuchAlgorithmException;

import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Staff is the same like user.

public class Staff {

    private int id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;


    static int userIdOnline;
    static String userNameOnline;

    public Staff(int id, String userName, String firstName, String lastName, String password) {
        this.id = id;
        this.userName = userName;
        this.firstName =firstName;
        this.lastName=lastName;
        this.password = password;
    }

    public Staff() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static int getUserIdOnline() {
        return userIdOnline;
    }

    public static void setUserIdOnline(int userIdOnline) {
        Staff.userIdOnline = userIdOnline;
    }

    public static String getUserNameOnline() {
        return userNameOnline;
    }

    public static void setUserNameOnline(String userNameOnline) {
        Staff.userNameOnline = userNameOnline;
    }

    public void addNewStaff(){
        int userId=0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("First Name:");
        String firstName = scanner.nextLine();
        System.out.println("Last Name:");
        String lastName = scanner.nextLine();
        System.out.println();
        System.out.println("User name: ");
        String userName = scanner.nextLine();
        System.out.println("Password: ");
        String userPassword = scanner.nextLine();
        try {
            userPassword=DataAccess.generateStrongPasswordHash(userPassword);
            System.out.println("Password has been translated to secure code!");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("There is a problem with the security algorithm, please contact the system administrator!");
            e.printStackTrace();
        }

        try {
            DataAccess dataAccess=new DataAccess();
            Connection con= dataAccess.getConnection();
            int staffId=0;
            String query1 = "insert into staff (username,firstname,lastname,password) values (?,?,?,?)";
            PreparedStatement pst1 = con.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            pst1.setString(1, userName);
            pst1.setString(2, firstName);
            pst1.setString(3, lastName);
            pst1.setString(4, userPassword);
            pst1.executeUpdate();
            ResultSet generatedKeys = pst1.getGeneratedKeys();
            if (!generatedKeys.next()) {
                // Should never happen
                System.err.println("There is a problem with the Database, please contact the system Administrator!");
            } else {
                userId = generatedKeys.getInt(1);
                System.out.println("The system generated user ID :"+userId +"   for following user:"+userName);
            }
            pst1.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void listAllUser(){
        ArrayList<Staff> userList = getAllUser();
        System.out.println("*********************");
        System.out.println("*    User list     *");
        System.out.println("*********************");
        if (userList.isEmpty()){
            System.out.println();
            System.out.println("There is no User in the Database! Please contact the System Administrator!");
            System.out.println();
        }else{

            System.out.println("First Name                      Last Name             User Name         Id");
            for (int i = 0; i < userList.size(); i++) {

                System.out.println(MyMethods.formatString(30, userList.get(i).getFirstName().toUpperCase()) + "  " +
                        MyMethods.formatString(20, userList.get(i).getLastName().toUpperCase()) + "  " +
                        MyMethods.formatString(15, userList.get(i).getUserName()) + "   " +
                        userList.get(i).getId());
            }
            System.out.println();
        }
    }

    private static ArrayList<Staff> getAllUser(){
        ArrayList<Staff> userList = new ArrayList<>();

        try {
            DataAccess dataAccess = new DataAccess();
            Connection con=dataAccess.getConnection();
            String sql;
            sql = "SELECT * FROM staff ORDER BY lastname, firstname";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String userName = rs.getString("username");
                userList.add(new Staff(id, userName,firstName, lastName,null ));
            }
            rs.close();
            preparedStatement.close();
            con.close();

        }catch (SQLException | ClassNotFoundException throwables) {
            System.out.println("There is no connection to the database! ");
            throwables.printStackTrace();
        }
        return userList;
    }

    protected void deleteUser(){
        ArrayList<Staff> userList = getAllUser();

        if (userList.isEmpty()){
            System.out.println();
            System.out.println("There is no User in the database!");
            System.out.println();
        }else{
            System.out.println("First Name                      Last Name             User Name         Id");
            for (int i = 0; i < userList.size(); i++) {

                System.out.println(MyMethods.formatString(30, userList.get(i).getFirstName().toUpperCase()) + "  " +
                        MyMethods.formatString(20, userList.get(i).getLastName().toUpperCase()) + "  " +
                        MyMethods.formatString(15, userList.get(i).getUserName()) + "   " +
                        userList.get(i).getId());
            }
            System.out.println();
        }
       // System.out.println("Select User ID to delete:");
        Staff selectedStaff=selectUser();
        System.out.println("User first name: "+selectedStaff.firstName);
        System.out.println("User last name:  "+selectedStaff.lastName);
        System.out.println("Username:        "+selectedStaff.userName);

        String choice=MyMethods.askingYesOrNo("Do you really want to delete?  (Y/N)");
        System.out.println();
        if (choice.equals("N")){
            return;
        }else{
            try {
                DataAccess dataAccess = new DataAccess();
                Connection con=dataAccess.getConnection();
                String sql;
                sql = "Delete FROM staff where staff.id=?";

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1,selectedStaff.id);
               int result = preparedStatement.executeUpdate();
               if (result==1){
                   System.out.println("User have been deleted.");
               }else{
                   System.out.println("Please contact the system Administrator, delete is not successful.");
               }
               preparedStatement.close();
               con.close();

        }catch(SQLException | ClassNotFoundException e){
                System.out.println("The Data Base is not available!");
                e.printStackTrace();
            }
    }}


    private Staff selectUser() {
        Scanner scanner = new Scanner(System.in);
        int selection;
        boolean exit = false;

        int foundID;
        Staff selectedStaff = new Staff();
        try {
            DataAccess dataAccess = new DataAccess();
            Connection con = dataAccess.getConnection();
            while (!exit) {
                System.out.println("Insert User ID: ");    //User Id=Staff Id
                try {
                    selection = Integer.parseInt(scanner.nextLine().trim());
                    String sql = "SELECT * FROM staff where id=?";

                    foundID = 0;
                    try {
                        PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setInt(1, selection);
                        ResultSet rs = preparedStatement.executeQuery();

                        while (rs.next()) {
                            int id = rs.getInt("id");
                            selectedStaff.setId(selection);
                            selectedStaff.setFirstName(rs.getString("firstname"));
                            selectedStaff.setLastName(rs.getString("lastname"));
                            selectedStaff.setUserName(rs.getString("username"));
                            selectedStaff.setPassword("*****************************");
                            foundID++;
                        }
                        //con.commit();
                        preparedStatement.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                    if ((foundID != 0) || (selection == 0)) {
                        if (selection==0){
                            selectedStaff = null;
                        }
                        exit = true;
                    } else {
                        System.out.println("Not existing User Id, try to enter again, or add 0 to exit");
                        exit = false;
                    }
                } catch (Exception e) {
                    System.out.println("Please Enter one of the user Ids, only numbers are allowed!");
                }
            }
            con.close();
        }catch(SQLException | ClassNotFoundException throwables) {
            System.out.println("There is no connection to the database! ");
            throwables.printStackTrace();
        }

        return selectedStaff;
    }


    protected void changePassword(){
        ArrayList<Staff> userList = getAllUser();

        if (userList.isEmpty()){
            System.out.println();
            System.out.println("There is no User in the database!");
            System.out.println();
        }else{
            System.out.println("First Name                      Last Name             User Name         Id");
            for (int i = 0; i < userList.size(); i++) {

                System.out.println(MyMethods.formatString(30, userList.get(i).getFirstName().toUpperCase()) + "  " +
                        MyMethods.formatString(20, userList.get(i).getLastName().toUpperCase()) + "  " +
                        MyMethods.formatString(15, userList.get(i).getUserName()) + "   " +
                        userList.get(i).getId());
            }
            System.out.println();
        }
        // System.out.println("Select User ID:");
        Staff selectedStaff=selectUser();
        System.out.println("User first name: "+selectedStaff.firstName);
        System.out.println("User last name:  "+selectedStaff.lastName);
        System.out.println("Username:        "+selectedStaff.userName);

        Scanner scanner=new Scanner(System.in);
        String newPassWord1;
        String newPassWord2;
        boolean first=true;
        do{
            if(!first){
                System.out.println("Password entry false, try it again!");
            }
            System.out.println("Please enter new password!");
            newPassWord1 = scanner.nextLine().trim();
            System.out.println("Please enter new PW. second time for checking your entry.");
            newPassWord2 = scanner.nextLine().trim();
            first=false;
        }while(!newPassWord1.equals(newPassWord2));
        String choice=MyMethods.askingYesOrNo("Do you really want to change?  (Y/N)");
        System.out.println();


        if (choice.equals("N")){
            return;
        }else{
            try {
                newPassWord1=DataAccess.generateStrongPasswordHash(newPassWord1);
                System.out.println("Password has been translated to secure code!");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                System.out.println("There is a problem with the security algorithm, please contact the system administrator!");
                e.printStackTrace();
            }
            try {
                DataAccess dataAccess = new DataAccess();
                Connection con=dataAccess.getConnection();
                String sql;
                sql = "Update staff set password=? where staff.id=?";

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1,newPassWord1);
                preparedStatement.setInt(2,selectedStaff.id);
                int result = preparedStatement.executeUpdate();
                if (result==1){
                    System.out.println("New password has been saved.");
                }else{
                    System.out.println("Please contact the system Administrator, update is not successful.");
                }
                preparedStatement.close();
                con.close();

            }catch(SQLException | ClassNotFoundException e){
                System.out.println("The Data Base is not available!");
                e.printStackTrace();
            }
        }
    }

    protected void changeUserName(){
        ArrayList<Staff> userList = getAllUser();

        if (userList.isEmpty()){
            System.out.println();
            System.out.println("There is no User in the database!");
            System.out.println();
        }else{
            System.out.println("First Name                      Last Name             User Name         Id");
            for (int i = 0; i < userList.size(); i++) {

                System.out.println(MyMethods.formatString(30, userList.get(i).getFirstName().toUpperCase()) + "  " +
                        MyMethods.formatString(20, userList.get(i).getLastName().toUpperCase()) + "  " +
                        MyMethods.formatString(15, userList.get(i).getUserName()) + "   " +
                        userList.get(i).getId());
            }
            System.out.println();
        }
        // System.out.println("Select User ID:");
        Staff selectedStaff=selectUser();
        System.out.println("User first name: "+selectedStaff.firstName);
        System.out.println("User last name:  "+selectedStaff.lastName);
        System.out.println("Username:        "+selectedStaff.userName);

        Scanner scanner=new Scanner(System.in);

        System.out.println("Please enter new user Name:");
        String newUserName= scanner.nextLine().trim();

        String choice=MyMethods.askingYesOrNo("Do you really want to change?  (Y/N)");
        System.out.println();


        if (choice.equals("N")){
            return;
        }else{

            try {
                DataAccess dataAccess = new DataAccess();
                Connection con=dataAccess.getConnection();
                String sql;
                sql = "Update staff set username=? where staff.id=?";

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1,newUserName);
                preparedStatement.setInt(2,selectedStaff.id);
                int result = preparedStatement.executeUpdate();
                if (result==1){
                    System.out.println("New user name has been saved.");
                }else{
                    System.out.println("Please contact the system Administrator, update is not successful.");
                }
                preparedStatement.close();
                con.close();

            }catch(SQLException | ClassNotFoundException e){
                System.out.println("The Data Base is not available!");
                e.printStackTrace();
            }
        }
    }
}
