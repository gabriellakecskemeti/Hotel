


public class BookingMenu {

    public static Object showBookingMenu() {
        printBookingHeader();
        int selection; //= -1;

        do {
            printBookingMenu();

            selection=Menu.enterSelection();

            switch (selection) {
                case 1:
                    MyBooking.listBookings();
                    return showBookingMenu();
                case 2:
                    AddNewBooking anbObj=new AddNewBooking();
                    anbObj.addNewBooking();
                    return showBookingMenu();
                case 3:
                    System.out.println("cancel booking");
                    return showBookingMenu();
                case 4:
                    WorkInvoice.createInvoice();
                    return showBookingMenu();
                case 0:
                    return null;
                default:
                    System.out.println("The selection was invalid!");
            }
        } while (selection != 0);
        return Menu.mainMenu();

    }




    public static void printBookingHeader() {
        System.out.println(" ----------------------------------------- ");
        System.out.println("|                 WELCOME                 |");
        System.out.println("|       to the Hotel of Your Dreams       |");
        System.out.println("|                 Bookings                |");
        System.out.println(" ----------------------------------------- ");
    }

    public static void printBookingMenu() {

        System.out.println("1.Display all Bookings in given Period");
        System.out.println("2.Create new booking");
        System.out.println("3.Cancel booking");
        System.out.println("4.Create Invoice");

        System.out.println("0.Exit");
    }

}
