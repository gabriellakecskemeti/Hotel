public class RoomMenu {

    public static Object showRoomMenu() {
        printUserHeader();
        int selection; //= -1;

        do {
            printUserMenu();

            selection=Menu.enterSelection();

            switch (selection) {
                case 1:
                    Room room=new Room();
                    room.showAll_Rooms();
                    return showRoomMenu();
                case 2:
                    Room room2=new Room();
                    room2.addRoom();
                    return showRoomMenu();
                case 3:
                    Room room3=new Room();
                    room3.deleteRoom();
                    return showRoomMenu();
                case 4:
                    Category category=new Category();
                    category.updateRoomPrice();
                    return showRoomMenu();

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
        System.out.println("|           All about our Rooms           |");
        System.out.println(" ----------------------------------------- ");
    }

    public static void printUserMenu() {

        System.out.println("1.Display all rooms");
        System.out.println("2.Create new room");
        System.out.println("3.Delete room");
        System.out.println("4.Update room price");

        System.out.println("0.Exit");
    }


}
