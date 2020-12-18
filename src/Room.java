import java.sql.*;
import java.util.Scanner;

public class Room {

    int roomId;
    //int roomNr;
    float price;

    public Room() {
    }

    public Room(int roomId, float price) {
        this.roomId = roomId;

        this.price = price;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void showAll_Rooms() {

        try {
            DataAccess dataAccess=new DataAccess();

            Connection connection = dataAccess.getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM rooms left Join category on fk_category_id = category.id;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("ID: "+rs.getInt("id") + "| "+"room name : "+rs.getString("name")+" | "+"room number : "+rs.getString("room_number")+"| "+"facilities : "+rs.getString("facilities")+"| "+"roomsize : "+rs.getString("roomsize")+"| "+" price: "+rs.getFloat("price"));
                System.out.println("__________________________________________________________________________________________________________________________________________");
            }
            rs.close();
            ps.close();
            connection.close();


        } catch (SQLException sql) {
            System.out.println("Data base connection or definition problem!");
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


            PreparedStatement ps = connection.prepareStatement("select rr.id, cc.name, cc.capacity, cc.price, cc.roomsize from rooms as rr \n" +
                    "inner join category as cc on cc.id=rr.fk_category_id\n" +
                    "where rr.id not in(SELECT bb.fk_room_id from bookings as bb where bb.departure_date>=? and bb.arrival_date<=?)");
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            ResultSet rs = ps.executeQuery();
            ps.close();
            System.out.println("-----available rooms -----");
            int count=0;
            while (rs.next()) {
                count++;
                System.out.println("ID: " + rs.getInt("id") + " " + rs.getString("name") + " price: " + rs.getFloat("price"));
            }
            if(count==0){
                System.out.println("Sorry, there are no available rooms.");
            }
            rs.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
