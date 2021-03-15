public class GuestMenu {


    public static Object showGuestMenu() {
        printGuestHeader();
        int selection; //= -1;

        do {
            printGuestMenu();

            selection=Menu.enterSelection();

            switch (selection) {
                case 1:
                    Guest guest=new Guest();
                    guest.displayAllGuests(3);
                    return showGuestMenu();
                case 2:
                    Guest newGuest=new Guest();
                    newGuest.addNewGuest();
                    return showGuestMenu();
                case 3:
                    Guest newGuest1=new Guest();
                    newGuest1.deleteGuest();
                    return showGuestMenu();
                case 4:
                    Guest guestNow=new Guest();
                    guestNow.displayAllGuests(2);
                    return showGuestMenu();

                case 0:
                    return null;
                default:
                    System.out.println("The selection was invalid!");
            }
        } while (selection != 0);
        return Menu.mainMenu();

    }




    public static void printGuestHeader() {
        System.out.println(" ----------------------------------------- ");
        System.out.println("|                 WELCOME                 |");
        System.out.println("|       to the Hotel of Your Dreams       |");
        System.out.println("|             Guest Management            |");
        System.out.println(" ----------------------------------------- ");
    }

    public static void printGuestMenu() {

        System.out.println("1.Display all guest");
        System.out.println("2.Create new guest");
        System.out.println("3.Delete guest");
        System.out.println("4.Display guests in Hotel today");

        System.out.println("0.Exit");
    }


}
