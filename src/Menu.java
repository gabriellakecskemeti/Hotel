

import java.util.Scanner;

public class Menu {


    public static Menu mainMenu() {
        printHeader();

        int selection= -1;

        do {
            printMenu();

            selection=enterSelection();

            switch (selection) {
                case 1:
                    //method to show all rooms
                    RoomMenu.showRoomMenu();
                    return mainMenu();
                case 2:
                    GuestMenu.showGuestMenu();
                    return mainMenu();
                case 3:
                    BookingMenu.showBookingMenu();
                    return mainMenu();
                case 4:
                    Room availableRoom=new Room();
                    availableRoom.showAllAvailableRoom();
                    return mainMenu();
                case 5:
                    UserMenu.showUserMenu();
                    return mainMenu();
                case 0:
                    return null;
                default:
                    System.out.println("The selection was invalid!");
            }
         } while (selection != 0);
        return null;
    }





    public static int enterSelection(){
        Scanner scanner= new Scanner(System.in);
        int selection = -1;
        boolean exit=false;

        while(!exit ) {
            System.out.println("Insert selection: ");
            try {
                selection = Integer.parseInt(scanner.nextLine().trim());
                exit=true;
            } catch (Exception e) {
                System.out.println("Please Enter one of the menu options, only numbers are allowed!");
            }
        }
        return selection;
    }


    public static void printHeader() {
        System.out.println(" ----------------------------------------- ");
        System.out.println("|                 WELCOME                 |");
        System.out.println("|       to the Hotel of Your Dreams       |");
        System.out.println(" ----------------------------------------- ");
    }

    public static void printMenu() {

        System.out.println("1.All about our rooms");
        System.out.println("2.Guest management");
        System.out.println("3.All about bookings");
        System.out.println("4.Show all available Room");
        System.out.println("5.User management");

        System.out.println("0.Exit");
    }



}
