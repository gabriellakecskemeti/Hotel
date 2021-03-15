import com.mysql.cj.jdbc.DatabaseMetaData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class MyBooking {

    int id;
    Date arrival;
    Date departure;
    int total;
    String payment_type;         //{CREDIT_CARD, PAYPAL,BANK_TRANSFER,CASH}
    String status;               // {Booked,CANCELLED,PAID,OPEN}
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

    /**
     * Search given id in the sql database/booking table and give back item if it found
     * @param bookingId  id to search
     * @return the booking item with the given id
     */
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
                    "rooms.room_number, staff.username FROM bookings " +
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
                String staffName=rs.getString("staff.username");

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

    /**
     * Schaut nach if an invoice to the booking exists
     * @param idkey  -booking id to search
     * @return  1=found, 0=not found
     */
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

    public static void listBookings(){
        Scanner scanner = new Scanner(System.in);

        AddNewBooking anbObj= new AddNewBooking();
        try {
            DataAccess dataAccess=new DataAccess();
            Connection connection = dataAccess.getConnection();

            java.sql.Date fromDate = anbObj.askDate("Date from - YYYY.MM.DD: ");
            java.sql.Date toDate = anbObj.askDate("Date to   - YYYY.MM.DD: ");


            PreparedStatement ps = connection.prepareStatement("select bookings.id, bookings.arrival_date, " +
                    "bookings.departure_date, bookings.total_price, guests.first_name, guests.Last_name from bookings join guests on bookings.fk_guest_id=guests.id  " +
                    "where departure_date>=? or arrival_date<=?");
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            ResultSet rs = ps.executeQuery();

            System.out.println("-----Bookings -----");
            int count=0;
            double periodTotal=0;
            while (rs.next()) {
                count++;
                System.out.println("Booking ID: " + MyMethods.formatMyInt(3,rs.getInt("bookings.id"))+"    name: "
                        +MyMethods.formatString(20, rs.getString("guests.first_name"))
                        +MyMethods.formatString(20, rs.getString("guests.last_name"))+"  Arrival: "
                        +rs.getDate("bookings.arrival_date")+ "  Departure: " + rs.getDate("bookings.departure_date")
                        +"    price: " + MyMethods.formatMyDouble(8,rs.getDouble("bookings.total_price")));
                periodTotal=periodTotal+rs.getDouble("bookings.total_price");
            }
            if(count==0){
                System.out.println("Sorry, there are no bookings in this period.");
            }else{
                System.out.println("Total booked in EUR:                                                                                                    "+MyMethods.formatMyDouble(8,periodTotal));
            }
            ps.close();
            rs.close();
            connection.close();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void cancelBooking(){
        MyBooking bookingObj= WorkInvoice.askBookingId();
        if (bookingObj == null) {
            return;
        }
        // next method controls if an invoice to this booking exist
        //Missing: we should check the status of booking, if it is already cancelled, than the program should say this
        // and do not let to cancel again
        int invoiceFoundOption= MyBooking.checkInvoice(bookingObj.id);  //0=not found 1=found 9=missing connection
        if (invoiceFoundOption==1) {
            System.out.println("Invoice reversal will be created about cancellation for following booking:");
            System.out.println("Booking: "+bookingObj.id+"   Guest: "+bookingObj.surName.toUpperCase()+
                    " "+bookingObj.lastName.toUpperCase()+"   Room number: "+bookingObj.roomNumber+ "   Amount: "+bookingObj.total+ " EUR");
            if(!WorkInvoice.invoiceReversalGenerator(bookingObj)){
                System.out.println("I could not generate the reversed Invoice, check your Data Base connection!");
            }
        } else {
            if (invoiceFoundOption == 9) {  //we could not connect the database
                return;
            }else{
                System.out.println("There is no payment relating this Booking! Cancellation will be registered without Payment.");
                return;

            }
        }
        return;






    }

}
