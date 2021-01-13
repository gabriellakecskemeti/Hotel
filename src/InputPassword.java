import java.io.Console;
import java.sql.*;
import java.util.Scanner;


public class  InputPassword{


    public boolean checkPassword() {
        Connection connection = null;
        boolean valid=false;
        try {
            DataAccess dataAccess=new DataAccess();
            connection = dataAccess.getConnection();
            String inputUser;
            String inputPassword;
            try {
                do {
                    System.out.println("Enter username");
                    Scanner scanner = new Scanner(System.in);
                    inputUser = scanner.nextLine().trim();
                    System.out.println("Enter password");
                    inputPassword = scanner.nextLine().trim();

                    PreparedStatement passWordDatabase = connection.prepareStatement("SELECT staff.id,staff.username, staff.password FROM staff");
                    ResultSet rs = passWordDatabase.executeQuery();
                    String usernameDatabase1="";
                    String passWordDatabase1="";
                    int count=0;
                    while (rs.next()) {
                        count++;
                        usernameDatabase1= rs.getString("username");
                        passWordDatabase1= rs.getString("password");
                        boolean matched = DataAccess.validatePassword(inputPassword, passWordDatabase1);
                        if (matched){
                            return true;
                        }

                }
                System.out.println("\nPlease enter Valid username and password");
                valid=false;
            }while (!valid);

            }catch (Exception e) {
                System.out.println("The data base is not available1");
                e.printStackTrace();
            }
            connection.close();
        } catch (SQLException sql) {
            System.out.println("The Data Base is not available!");
            //sql.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (connection!=null){
                connection.close();}
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

}


