import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Room {

    int roomId;
    int roomNumber;
    String description;
    int category;
    double price;

    public Room() {
    }

    public Room(int roomId, double price) {
        this.roomId = roomId;

        this.price = price;
    }

    public Room(int roomId, int roomNumber, String description, int category, double price) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void showAll_Rooms() {

        try {
            DataAccess dataAccess=new DataAccess();

            Connection connection = dataAccess.getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM rooms left Join category on fk_category_id = category.id order by rooms.room_number;");
            ResultSet rs = ps.executeQuery();
            System.out.println(" ID    Room name                  Number    Size         Price     Description/Facilities     ");
            while (rs.next()) {
                System.out.println(" "+MyMethods.formatMyInt(3,rs.getInt("id")) +
                        " | "+MyMethods.formatString(25,rs.getString("name"))+" | "+
                        MyMethods.formatString(6,rs.getString("room_number"))+" |  "
                        +rs.getString("roomsize")
                        +"   | "+MyMethods.formatMyDouble(10,rs.getDouble("price"))+"  |  "
                        +rs.getString("description").trim()+"  /  "+rs.getString("facilities"));
                System.out.println("__________________________________________________________________________________________________________________________________________");
            }
            rs.close();
            ps.close();
            connection.close();


        } catch (SQLException sql) {
            System.out.println("Database connection or definition problem! Please call the system administrator!");
            sql.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void showAllAvailableRoom() {
        Scanner scanner = new Scanner(System.in);

        AddNewBooking anbObj= new AddNewBooking();
        try {
            DataAccess dataAccess=new DataAccess();
            Connection connection = dataAccess.getConnection();

            Date fromDate = anbObj.askDate("Please enter the Date of arrival YYYY.MM.DD: ");
            Date toDate = anbObj.askDate("Please enter the Date of departure YYYY.MM.DD: ");


            PreparedStatement ps = connection.prepareStatement("select rr.id, rr.room_number, cc.name, cc.capacity, cc.price, cc.roomsize from rooms as rr \n" +
                    "inner join category as cc on cc.id=rr.fk_category_id\n" +
                    "where rr.id not in(SELECT bb.fk_room_id from bookings as bb where bb.departure_date>=? and bb.arrival_date<=?)");
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            ResultSet rs = ps.executeQuery();

            System.out.println("-----available rooms -----");
            int count=0;
            while (rs.next()) {
                count++;
                System.out.println("ID: " + MyMethods.formatMyInt(3,rs.getInt("id"))+"  Room number: "
                        +MyMethods.formatMyInt(5,rs.getInt("room_number"))+ " " +
                        MyMethods.formatString(20,rs.getString("name"))
                         +" price: " + MyMethods.formatMyDouble(8,rs.getDouble("price")));
            }
            if(count==0){
                System.out.println("Sorry, there are no available rooms.");
            }
            ps.close();
            rs.close();
            connection.close();
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addRoom(){

        int roomId=0;
        int roomNumber=0;
        Scanner scanner = new Scanner(System.in);
        boolean ok=false;
        do {
            System.out.println("Room number:");
            try {
                roomNumber = Integer.parseInt(scanner.nextLine().trim());
                ok=true;
            } catch (Exception e) {
                System.out.println("Only numbers are allowed!");
            }
        }while(!ok);

        ArrayList<Category> allCategory= Category.getAllCategory();
        if(allCategory.isEmpty()){
            System.out.println("Category table is empty, please contact the system administrator!");
            return;
        }
        Category.listAllCategory(allCategory);
        boolean found=false;
        String roomCategory;

        do{
            System.out.println("Choose category:");
            roomCategory = scanner.nextLine().trim();
            found=false;

            for (int i=0;i<allCategory.size();i++){

                if(roomCategory.equals(String.valueOf(allCategory.get(i).getId()))) {
                  i= allCategory.size();
                  found=true;
                }
             }
            if(!found){
                System.out.println("Not existing category! Please try again!");
            }

        }while(!found);

        System.out.println("Description : ");
        String description = scanner.nextLine();

        try {
                DataAccess dataAccess=new DataAccess();
                Connection con= dataAccess.getConnection();
                int staffId=0;
                String query1 = "insert into rooms (room_number,description,fk_category_id) values (?,?,?)";
                PreparedStatement pst1 = con.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
                pst1.setInt(1, roomNumber);
                pst1.setString(2, description);
                pst1.setString(3, roomCategory);

                pst1.executeUpdate();
                ResultSet generatedKeys = pst1.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    // Should never happen
                    System.err.println("There is a problem with the Database, please contact the system Administrator!");
                } else {
                    roomId = generatedKeys.getInt(1);
                    System.out.println("The system generated room ID :"+roomId +"   for following room:"+roomNumber);
                }
                pst1.close();
                con.close();
        } catch (SQLException throwables) {
                throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
        }

    }

    protected void deleteRoom(){
        ArrayList<Room> roomList = getAllRoom();

        if (roomList.isEmpty()){
            System.out.println();
            System.out.println("The Table rooms is empty!");
            System.out.println();
        }else{
            System.out.println(" ID  Room number   Description                                          Category ID");
            for (int i = 0; i < roomList.size(); i++) {

                System.out.println(MyMethods.formatMyInt(3, roomList.get(i).getRoomId()) + "  " +
                        MyMethods.formatMyInt(5, roomList.get(i).getRoomNumber()) + "         " +
                        MyMethods.formatString(50, roomList.get(i).getDescription()) + "   " +
                        roomList.get(i).getCategory());
            }
            System.out.println();
        }
        // System.out.println("Select User ID to delete:");
        boolean selected=false;
        Room selectedRoom= new Room();
        do{
            selectedRoom=selectRoom();
            if (selectedRoom!=null){
                selected=true;
                if (selectedRoom.roomId==0){
                    return;
                }
            }
        }while(!selected);
        System.out.println("Room ID:     "+selectedRoom.roomId);
        System.out.println("Room number: "+selectedRoom.roomNumber);
        System.out.println("Description: "+selectedRoom.description);
        String choice=MyMethods.askingYesOrNo(" Do you really want to delete?  (Y/N)");
        System.out.println();
        if (choice.equals("N")){
            return;
        }
            try {
                DataAccess dataAccess = new DataAccess();
                Connection con=dataAccess.getConnection();
                String sql;
                sql = "Delete FROM rooms where rooms.id=?";
                String sql1= "select * FROM bookings WHERE bookings.fk_room_id=?";

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
                preparedStatement.setInt(1, selectedRoom.roomId);
                preparedStatement1.setInt(1, selectedRoom.roomId);

                ResultSet rs = preparedStatement1.executeQuery();
                int counter=0;
                while (rs.next()){
                    counter++;
                }
                if (counter>0){
                    choice=MyMethods.askingYesOrNo(" There are bookings saved with this Room Id, do you really want to delete?  (Y/N)");
                    System.out.println();
                    if (choice.equals("N")){
                        preparedStatement.close();
                        preparedStatement1.close();
                        con.close();
                        return;
                    }
                }
                int result = preparedStatement.executeUpdate();
                if (result==1){
                    System.out.println("Item has been deleted.");
                }else{
                    System.out.println("Please contact the system Administrator, delete is not successful.");
                }
                preparedStatement.close();
                preparedStatement1.close();
                con.close();

            }catch(SQLException | ClassNotFoundException e){
                System.out.println("The Data Base is not available!");
                e.printStackTrace();
            }
        }


    private Room selectRoom() {
        Scanner scanner = new Scanner(System.in);
        int selection;
        boolean exit = false;

        int foundID;
        Room selectedRoom = new Room();
        try {
            DataAccess dataAccess = new DataAccess();
            Connection con = dataAccess.getConnection();
            while (!exit) {
                System.out.println("Insert Room ID: ");    //User Id=Staff Id
                try {
                    selection = Integer.parseInt(scanner.nextLine().trim());
                    if (selection==0){
                        selectedRoom.setRoomId(0);
                        return selectedRoom;
                    }
                    String sql = "SELECT * FROM rooms where id=?";

                    foundID = 0;
                    try {
                        PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setInt(1, selection);
                        ResultSet rs = preparedStatement.executeQuery();

                        while (rs.next()) {

                            selectedRoom.setRoomId(selection);
                            selectedRoom.setRoomNumber(rs.getInt("room_number"));
                            selectedRoom.setDescription(rs.getString("description"));
                            selectedRoom.setCategory(rs.getInt("fk_category_id"));

                            foundID++;
                        }
                        //con.commit();
                        preparedStatement.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                    if ((foundID != 0) || (selection == 0)) {
                        if (selection==0){
                            selectedRoom = null;
                        }
                        exit = true;
                    } else {
                        System.out.println("Not existing Id, try to enter again, or add 0 to exit");
                        exit = false;
                    }
                } catch (Exception e) {
                    System.out.println("Please Enter one of the room Ids, only numbers are allowed!");
                }
            }
            con.close();
        }catch(SQLException | ClassNotFoundException throwables) {
            System.out.println("There is no connection to the database! ");
            throwables.printStackTrace();
        }

        return selectedRoom;
    }

    public static ArrayList<Room> getAllRoom(){
        ArrayList<Room> allRoom= new ArrayList<>();
        try {
            DataAccess dataAccess=new DataAccess();

            Connection connection = dataAccess.getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM rooms left Join category on fk_category_id = category.id;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allRoom.add(new Room(rs.getInt("rooms.id"),
                        rs.getInt("rooms.room_number"),
                        rs.getString("rooms.description"),
                        rs.getInt("rooms.fk_category_id"),rs.getDouble("category.price")));
            }
            rs.close();
            ps.close();
            connection.close();

        } catch (SQLException sql) {
            System.out.println("Database connection or definition problem! Please call the system administrator!");
            sql.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return allRoom;
    }
}
