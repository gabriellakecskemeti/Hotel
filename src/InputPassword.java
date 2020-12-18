import java.sql.*;
import java.util.Scanner;


public class  InputPassword{


    public boolean checkPassword() {
        Connection connection = null;
        boolean valid=false;
        try {
            DataAccess dataAccess=new DataAccess();
            connection = dataAccess.getConnection();
            try {
                do {
                    System.out.println("Enter username");
                    Scanner scanner = new Scanner(System.in);
                    String inputUser= scanner.nextLine();
                    System.out.println("Enter password");
                    String inputPassword= scanner.nextLine();
                    PreparedStatement passWordDatabase = connection.prepareStatement("SELECT staff.id,staff.username, staff.password FROM staff");
                    ResultSet rs = passWordDatabase.executeQuery();
                    String usernameDatabase1="";
                    String passWordDatabase1="";
                    int count=0;
                    while (rs.next()) {
                        count++;
                        usernameDatabase1= rs.getString("username");
                        passWordDatabase1= rs.getString("password");
                        int userId=rs.getInt("id");
                        if (inputUser.equals(usernameDatabase1) && inputPassword.equals(passWordDatabase1)) {
                            AddNewBooking.userName=usernameDatabase1;
                            AddNewBooking.userId=userId;
                            return true;
                        }
                }
                System.out.println("\n Please enter Valid username and password");
                valid=false;
            }while (!valid);

            }catch (Exception e) {
                System.out.println("The data base is not available");
                e.printStackTrace();
            }
            connection.close();
        } catch (SQLException sql) {
            System.out.println("The Data Base is not available!");
            sql.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException throwables) {
                //throwables.printStackTrace();
            }
        }
        return false;
    }

}


