import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MyBooking {

    int id;
    Date arrival;
    Date departure;
    int total;
    String payment_type;         //{CREDIT_CARD, PAYPAL,BANK_TRANSFER,CASH}
    String status;            // {Booked,CANCELLED,PAYED,OPEN}
    String notes ;
    int fk_guest_id;
    int fk_room_id;
    int fk_staff_id;
    String surName;
    String lastName;
    int roomNumber;
    String staffName;


    public MyBooking(int id, Date arrival, Date departure, int total, String payment_type, String status,
                     String notes, int fk_guest_id, int fk_room_id, int fk_staff_id, String surName,
                     String lastName, int roomNumber, String staffName) {
        this.id = id;
        this.arrival = arrival;
        this.departure = departure;
        this.total = total;
        this.payment_type = payment_type;
        this.status = status;
        this.notes = notes;
        this.fk_guest_id = fk_guest_id;
        this.fk_room_id = fk_room_id;
        this.fk_staff_id = fk_staff_id;
        this.surName = surName;
        this.lastName = lastName;
        this.roomNumber = roomNumber;
        this.staffName = staffName;
    }


    public static MyBooking checkBookingId(int bookingId){
        MyBooking selectedBooking=null;

        Connection con = WorkInvoice.init();
        if (con==null){
            System.out.println("There is no connection to the database!");
            return null;
        }
        try {
            String query;

            query = "SELECT  bookings.id, bookings.arrival_date, bookings.departure_date, bookings.total_price" +
                    ", bookings.payment_type, bookings.status, bookings.notes, bookings.fk_guest_id," +
                    " bookings.fk_room_id, bookings.fk_staff_id, guests.first_name, guests.last_name, " +
                    "rooms.room_number, staff.name FROM bookings " +
                    "join guests on bookings.fk_guest_id=guests.id  " +
                    "join rooms on bookings.fk_room_id= rooms.id " +
                    " join staff on bookings.fk_staff_id=staff.id " +
                    " where bookings.id=" + bookingId;


            PreparedStatement pst1=con.prepareStatement(query);
            ResultSet rs = pst1.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("bookings.id");
                java.sql.Date arrival = rs.getDate("bookings.arrival_date");
                java.sql.Date departure = rs.getDate("bookings.departure_date");
                int total = rs.getInt("bookings.total_price");
                String payment_type = rs.getString("bookings.payment_type");
                String status = rs.getString("bookings.status");
                String notes = rs.getString("bookings.notes");
                int fk_guest_id= rs.getInt("bookings.fk_guest_id");
                int fk_room_id = rs.getInt("bookings.fk_room_id");
                int fk_staff_id = rs.getInt("bookings.fk_staff_id");
                String surname=rs.getString("guests.first_name");
                String lastname=rs.getString("guests.last_name");
                int roomNumber= rs.getInt("rooms.room_number");
                String staffName=rs.getString("staff.name");

                selectedBooking=new MyBooking(id,
                        arrival,
                        departure,
                        total,
                        payment_type,
                        status,
                        notes,
                        fk_guest_id,
                        fk_room_id,
                        fk_staff_id,
                        surname, lastname, roomNumber, staffName);
            }else{
                selectedBooking=new MyBooking(0,null,null,
                        0,"","","",0,
                        0,0,"","",0,"");
            }

            con.commit();
            pst1.close();

        } catch(SQLException e){
            e.printStackTrace();
        }
        WorkInvoice.stop(con);
        return selectedBooking;
    }


    public static int checkInvoice(int idkey){

        int option;
        Connection con = WorkInvoice.init();
        if (con==null){
            System.out.println("There is no connection to the database!");
            option= 9;
        }
        try {
            String query;
            query = "SELECT  * from invoice where fk_bookings_id=" + idkey;
            PreparedStatement pst1=con.prepareStatement(query);
            ResultSet rs = pst1.executeQuery();
            boolean invoiceFound=rs.next();
            if(invoiceFound){
                int id = rs.getInt("id");
                java.sql.Date invoiceDate = rs.getDate("date");
                System.out.println("Invoice Id: "+id+"  Invoice Date: "+invoiceDate);
                option=1;
            }else{
                option=0;
            }
            con.commit();
            pst1.close();

        } catch(SQLException e){
            e.printStackTrace();
            option=9;
        }

        WorkInvoice.stop(con);
        return option;
    }



}
