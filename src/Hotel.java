import java.sql.Date;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Hotel {
    static DataAccess dataAccess;

    public static void main(String[] args) {
        InputPassword in=new  InputPassword();
        boolean valid=in.checkPassword();
        if (valid) {
            Menu menu = new Menu();
            menu.mainMenu(menu);
            System.out.println("Thank you for running the Application");
        }else{
            System.out.println("Password entry unsuccessfully, please connect" +
                    " the application manager if you need a new password!!");
        }

    }

    static public void init(){
        try{
            dataAccess = new DataAccess();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    static public void stop(){
        try{
            dataAccess.getConnection().close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}






  /*  public static void main(String[] args){

        Menu userInterface = new Menu();
        userInterface.mainMenu(userInterface);
        System.out.println("Thank you for running the Application");

    }*/