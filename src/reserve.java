import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class reserve {


    /**
     *
     * @param option defines which kind of search we need 1= booking id  2 = guest id
     * @param idkey   the according to the option
     * @return  arraylist with all necessary info for the invoice
     */
    public static ArrayList<MyBooking> getBookingsInvoice(int option, int idkey){

        ArrayList<MyBooking> booking= new ArrayList<>();
        Connection con = WorkInvoice.init();
        if (con==null){
            System.out.println("There is no connection to the database!");
            return null;
        }
        try {
            String query;
            if (option == 1) {
                query = "SELECT  bookings.id, bookings.arrival_date, bookings.departure_date, bookings.total_price" +
                        ", bookings.payment_type, bookings.status, bookings.notes, bookings.fk_guest_id," +
                        " bookings.fk_room_id, bookings.fk_staff_id, guests.first_name, guests.last_name, " +
                        "rooms.room_number, staff.name, invoice.id, invoice.date FROM bookings " +
                        "join guests on bookings.fk_guest_id=guests.id  " +
                        "join rooms on bookings.fk_room_id= rooms.id " +
                        " join staff on bookings.fk_staff_id=staff.id " +
                        "left join invoice on bookings.id=invoice.fk_bookings_id where bookings.id=" + idkey;
            }else {
                query = "SELECT  bookings.id, bookings.arrival_date, bookings.departure_date, bookings.total_price" +
                        ", bookings.payment_type, bookings.status, bookings.notes, bookings.fk_guest_id," +
                        " bookings.fk_room_id, bookings.fk_staff_id, guests.first_name, guests.last_name, " +
                        "rooms.room_number, staff.name FROM bookings " +
                        "join guests on bookings.fk_guest_id=guests.id  " +
                        "join rooms on bookings.fk_room_id= rooms.id " +
                        " join staff on bookings.fk_staff_id=staff.id " +
                        "left join invoice on bookings.id=invoice.fk_bookings_id where bookings.fk_guest_id=" + idkey;
            }

            PreparedStatement pst1=con.prepareStatement(query);
            ResultSet rs = pst1.executeQuery();

            while (rs.next()) {
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
                int invoiceId=rs.getInt("invoice.id");

                if(invoiceId<1) {
                    booking.add(new MyBooking(id,
                            arrival,
                            departure,
                            total,
                            payment_type,
                            status,
                            notes,
                            fk_guest_id,
                            fk_room_id,
                            fk_staff_id,
                            surname, lastname, roomNumber, staffName));
                }
            }
            con.commit();
            pst1.close();
            if (booking.size()>1){
                for (MyBooking  element:booking) {

                    System.out.println(element.id+"  "+element.total);
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }

        WorkInvoice.stop(con);
        return booking;
    }


    private static boolean isDateValid(int year, int month, int day) {
        boolean dateIsValid = true;
        try {
            LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            dateIsValid = false;
        }
        return dateIsValid;
    }


    /**
     *
     * @return arrylist with null: not succeed, succeed:arraylist wit two items from and to date
     */
    public static ArrayList<String> askPeriod(){
        ArrayList<String> period=null;
        boolean exit=false;
        String fromDate = null;
        String toDate = null;
        System.out.println("Enter date of arrival and date of departure in format YYYY-MM-dd");
        Scanner sc = new Scanner(System.in);
        while(!exit) {
            System.out.println("From date:\n");
            fromDate = sc.next();
            System.out.println("To date:\n");
            toDate = sc.next();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date DateFrom = format.parse(fromDate);
                java.util.Date DateTo = format.parse(toDate);
                period.add(fromDate);
                period.add(toDate);
                exit=true;
            } catch (ParseException e) {
                System.out.println("Wrong date format! Try it again");
                // e.printStackTrace();
            }
        }
        return period;
    }
}
