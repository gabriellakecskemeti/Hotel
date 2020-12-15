
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Invoice {
        int id;
        LocalDateTime iDate;
        int fk_bookings_id;
        int fk_stuff_id;

    public Invoice(int id, LocalDateTime iDate, int fk_bookings_id, int fk_stuff_id) {
        this.id = id;
        this.iDate = iDate;
        this.fk_bookings_id = fk_bookings_id;
        this.fk_stuff_id = fk_stuff_id;
    }



    public static void createInvoice(){
        MyBooking bookingObj=askBookingId();
        if (bookingObj == null) {
            return;
        }
         // next method controls if an invoice to this booking exist
        ArrayList<MyBooking> bookingArr= MyBooking.getBookingsInvoice(1,bookingObj.id);
        if (bookingArr == null) {
            return;
        }

        if (bookingArr.isEmpty()){
            System.out.println("There is already an Invoice for this Booking!");
            return;
        }else{

            createInvoice();
        }

    }


    public static MyBooking askBookingId(){

        Scanner scanner = new Scanner(System.in);
        int selection;
        boolean exit = false;
        MyBooking bookingObj = null;
        while (!exit) {
            System.out.println("Enter booking ID: ");
            try {
                selection = Integer.parseInt(scanner.nextLine());
                bookingObj = MyBooking.checkBookingId(selection);
                if (bookingObj != null || (selection == 0)) {
                    if (selection == 0) {
                        bookingObj = null;
                    }
                    exit = true;
                } else {
                    System.out.println("Not existing Booking Id, try to enter again, or add 0 to exit");
                    exit = false;
                }

            } catch (Exception e) {
                System.out.println("Please Enter one of the booking Ids, only numbers are allowed!");
            }
        }
        return bookingObj;
    }



    public static int selectBooking(){
        int bookingId=0;
        System.out.println("Please enter an existing booking ID");

        return bookingId;
    }

    /**
     * generates Invoice   the name of the file is: invoice_X.txt, where X=bookingId
     *
     */
    public static void invoiceGenerator(ArrayList<MyBooking> bookings) {


       String pathAndFileName = "C:\\Test\\invoice.txt";
       Connection con;

       try {

           FileWriter file = new FileWriter(pathAndFileName);

           Class.forName("com.mysql.cj.jdbc.Driver");
           String url = "jdbc:mysql://localhost:3306/hotel?useTimezone=true&serverTimezone=UTC";
           con = DriverManager.getConnection(
                   url, "root", "");
           con.setAutoCommit(false);
           con.setReadOnly(false);
           Statement stmt = con.createStatement();

           String query = "SELECT table_name, table_rows FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = 'cr7_gabriella'";
           ResultSet rs = stmt.executeQuery(query);
       } catch(IOException e){
               System.out.println("An error occurred during writing into the file.");
               e.printStackTrace();
           }
       catch (Exception e){
           System.out.println("The database is not available! Check your environment and start again!");
           e.printStackTrace();
       }

   }

    public static Connection init(){
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver is not available!");
            e.printStackTrace();
        }
        String url = "jdbc:mysql://localhost:3306/hotel?useTimezone=true&serverTimezone=UTC";
        try {
            con = DriverManager.getConnection(url,"root", "");
            con.setAutoCommit(false);
            con.setReadOnly(false);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }

    public static void stop(Connection con) {
        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getiDate() {
        return iDate;
    }

    public void setiDate(LocalDateTime iDate) {
        this.iDate = iDate;
    }

    public int getFk_bookings_id() {
        return fk_bookings_id;
    }

    public void setFk_bookings_id(int fk_bookings_id) {
        this.fk_bookings_id = fk_bookings_id;
    }

    public int getFk_stuff_id() {
        return fk_stuff_id;
    }

    public void setFk_stuff_id(int fk_stuff_id) {
        this.fk_stuff_id = fk_stuff_id;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", iDate=" + iDate +
                ", fk_bookings_id=" + fk_bookings_id +
                ", fk_stuff_id=" + fk_stuff_id +
                '}';
    }
}


