
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {


    public Menu mainMenu(Menu menu) {
        printHeader();

        int selection; //= -1;

        do {
            printMenu();

            selection=enterSelection();

            switch (selection) {
                case 1:
                    //method to show all rooms
                    Room room=new Room();
                    room.showAll_Rooms();
                    return menu.mainMenu(menu);
                case 2:
                    Guest guest=new Guest();
                    guest.displayAllGuests(1);
                    return menu.mainMenu(menu);
                case 3:
                    Room availableRoom=new Room();
                    availableRoom.showAllAvailableRoom();
                    return menu.mainMenu(menu);
                case 4:
                    Guest guestNow=new Guest();
                    guestNow.displayAllGuests(2);
                    return menu.mainMenu(menu);
                case 5:
                    Guest newGuest=new Guest();
                    newGuest.addNewGuest();
                    return menu.mainMenu(menu);
                case 6:
                    AddNewBooking anbObj=new AddNewBooking();
                    anbObj.addNewBooking();
                    return menu.mainMenu(menu);
                case 7:
                    System.out.println("Place holder");
                    WorkInvoice.createInvoice();
                    return menu.mainMenu(menu);
                case 0:
                    return menu;    //it would be also good to  give back a null.
                default:
                    System.out.println("The selection was invalid!");
            }
        } while (selection != 0);
        return menu;
    }





    public int enterSelection(){
        Scanner scanner= new Scanner(System.in);
        int selection = -1;
        boolean exit=false;

        while(!exit ) {
            System.out.println("Insert selection: ");
            try {
                selection = Integer.parseInt(scanner.nextLine());
                exit=true;
            } catch (Exception e) {
                System.out.println("Please Enter one of the menu options, only numbers are allowed!");
            }
        }
        return selection;
    }


    public void printHeader() {
        System.out.println(" ----------------------------------------- ");
        System.out.println("|                 WELCOME                 |");
        System.out.println("|       to the Hotel of Your Dreams       |");
        System.out.println(" ----------------------------------------- ");
    }

    public void printMenu() {

        System.out.println("1.Display all rooms");
        System.out.println("2.Display all guests");
        System.out.println("3.Display all available room");
        System.out.println("4.Display guests in hotel now");
        System.out.println("5.Create new guest");
        System.out.println("6.Create booking");
        System.out.println("7.Create invoice");
        System.out.println("0.Exit");
    }



}
