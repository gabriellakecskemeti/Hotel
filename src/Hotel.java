


public class Hotel {

    public static void main(String[] args) {
        InputPassword in=new  InputPassword();
        boolean valid=in.checkPassword();
        if (valid) {
            //Menu menu = new Menu();
            Menu.mainMenu();
            System.out.println("Thank you for running the Application");
        }else{
            System.out.println("Password entry is unsuccessful!");
        }
    }
}
