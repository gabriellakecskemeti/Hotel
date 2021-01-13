




public class UserMenu {

    public static Object showUserMenu() {
        printUserHeader();
        int selection; //= -1;

        do {
            printUserMenu();

            selection=Menu.enterSelection();

            switch (selection) {
                case 1:
                    Staff.listAllUser();  //ordered by last name, first name
                    return showUserMenu();
                case 2:
                    //System.out.println(("create"));
                    Staff staff=new Staff();
                    staff.addNewStaff();
                    return showUserMenu();
                case 3:
                    //System.out.println("delete");
                    Staff staff1=new Staff();
                    staff1.deleteUser();
                    return showUserMenu();
                case 4:
                    //System.out.println("change pw");
                    Staff staff2=new Staff();
                    staff2.changePassword();
                    return showUserMenu();
                case 5:
                    //System.out.println("change user name");
                    Staff staff3=new Staff();
                    staff3.changeUserName();
                    return showUserMenu();

                case 0:
                    return null;
                default:
                    System.out.println("The selection was invalid!");
            }
        } while (selection != 0);
        return Menu.mainMenu();

    }




    public static void printUserHeader() {
        System.out.println(" ----------------------------------------- ");
        System.out.println("|                 WELCOME                 |");
        System.out.println("|       to the Hotel of Your Dreams       |");
        System.out.println("|             User Management             |");
        System.out.println(" ----------------------------------------- ");
    }

    public static void printUserMenu() {

        System.out.println("1.Display all user");
        System.out.println("2.Create new user");
        System.out.println("3.Delete user");
        System.out.println("4.Change password");
        System.out.println("5.Change user name");

        System.out.println("0.Exit");
    }




}
