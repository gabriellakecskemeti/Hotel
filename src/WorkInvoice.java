
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class   WorkInvoice {
        int id;
        LocalDateTime iDate;
        int fk_bookings_id;
        int fk_stuff_id;

    public WorkInvoice(int id, LocalDateTime iDate, int fk_bookings_id, int fk_stuff_id) {
        this.id = id;
        this.iDate = iDate;
        this.fk_bookings_id = fk_bookings_id;
        this.fk_stuff_id = fk_stuff_id;
    }

   /* public static void listAllInvoices(){

    }*/



    public static void createInvoice(){
        MyBooking bookingObj=askBookingId();
        if (bookingObj == null) {
            return;
        }
         // next method controls if an invoice to this booking exist
        int invoiceFoundOption= MyBooking.checkInvoice(bookingObj.id);  //0=not found 1=found 9=missing connection
        if (invoiceFoundOption==1) {
            System.out.println("There is already an Invoice for this Booking!");
        } else {
            if (invoiceFoundOption == 9) {  //we could not connect the database
                return;
            }else{
                System.out.println("Invoice will be created for following booking:");
                System.out.println("Booking: "+bookingObj.id+"   Guest: "+bookingObj.surName.toUpperCase()+
                        " "+bookingObj.lastName.toUpperCase()+"   Room number: "+bookingObj.roomNumber+ "   Amount: "+bookingObj.total+ " EUR");
                if(!invoiceGenerator(bookingObj)){
                    System.out.println("I could not generate the Invoice, check your Data Base connection!");
                }
            }
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
                if (selection==0){   //user want to get out
                    return null;
                }
                bookingObj = MyBooking.checkBookingId(selection);
                if (bookingObj != null ) {
                    //There was a connection!
                    if(bookingObj.id==0) {
                        System.out.println("Not existing Booking Id, try to enter again, or add 0 to exit");
                        exit=false;
                    }else{
                        exit = true;
                    }

                } else {//no connection
                       // System.out.println("There is no connection to the DataBase! ");
                        return null;
                }

            } catch (Exception e) {
                System.out.println("Please Enter one of the booking Ids, only numbers are allowed!");
            }
        }
        return bookingObj;
    }



    /**
     * generates WorkInvoice   the name of the file is: invoice_X.txt, where X=bookingId
     *
     */
    public static boolean invoiceGenerator(MyBooking bookingObj) {

       String path="C:\\Test";
       String pathAndFileName = path+"\\invoiceErr.txt";
       String filename;
       String pathNew="C:\\Test";

        Scanner scanner = new Scanner(System.in);

        System.out.println("the report will be generated: "+path);

        String choice = MyMethods.askingYesOrNo("Do you want to change this? Y/N");
        boolean exit;
        exit= !choice.equals("Y");
        File file1;

        while(!exit) {
            System.out.println("Old Path: " + path + "   Please enter new Path: ");
            pathNew = scanner.nextLine();

            file1 = new File(pathNew);
            if (file1.exists()) {
                exit = true;
                pathAndFileName=pathNew+"\\invoiceErr.txt";
                path=pathNew;
                System.out.println("Filepath is accepted!" + pathNew);
            } else {
                if(pathNew.equals("")){
                    System.out.println("Empty file path, process finished!");
                    System.out.println();
                    return false;
                }
                exit = false;
                System.out.println("NOT existing filepath!" + pathNew);
            }

        }
        Connection con;
       try {

           Class.forName("com.mysql.cj.jdbc.Driver");
           String url = "jdbc:mysql://localhost:3306/hotel?useTimezone=true&serverTimezone=UTC";
           con = DriverManager.getConnection(
                   url, "root", "");
           con.setAutoCommit(false);
           con.setReadOnly(false);

           String query1 = "insert into invoice (fk_bookings_id,fk_staff_id) values (?,?)";
           PreparedStatement pst1=con.prepareStatement(query1,Statement.RETURN_GENERATED_KEYS);
           pst1.setInt(1,bookingObj.id);
           pst1.setInt(2,bookingObj.fk_staff_id);
           pst1.executeUpdate();
           con.commit();

           //after writing new invoice to the invoice table, we need to get the generated ID of the invoice
           int invoiceId=0;
           Date invoiceDate=null;
           ResultSet generatedKeys = pst1.getGeneratedKeys();
           if (!generatedKeys.next()) {
               // Should never happen
               System.err.println("There is a problem with the Database, please contact the system administrator!");
           } else {
               invoiceId = generatedKeys.getInt(1);
               filename="invoice"+bookingObj.id;
               pathAndFileName=path+"\\"+filename+".txt";
           }
           con.commit();
           pst1.close();
           //we want to get the generated date of the invoice
           String query2="SELECT  * from invoice where id=" + invoiceId;
           PreparedStatement pst2=con.prepareStatement(query2);
           ResultSet rs = pst2.executeQuery();
           con.commit();
           boolean invoiceFound=rs.next();
           if(invoiceFound){
               invoiceDate = rs.getDate("date");
               System.out.println("Invoice Id: "+invoiceId+"  Invoice Date: "+invoiceDate);
           }else{
               System.out.println("There is a big big technical problem, connect please the system Administrator");
           }
           pst2.close();
           con.close();
           FileWriter file = new FileWriter(pathAndFileName);
           file.write("\n**************************************************************************" + System.lineSeparator());
           file.write("\n                   Like in Your Dream Hotel "+System.lineSeparator());
           file.write( "\n***************************************************************************" + System.lineSeparator());
           file.write( "\n\n Invoice number: " + invoiceId + " Date: " + invoiceDate + System.lineSeparator());
           file.write( "\n\n Guest: " + bookingObj.surName.toUpperCase() + "  "+bookingObj.lastName.toUpperCase() + System.lineSeparator());
           file.write( "\n Room:       " + bookingObj.roomNumber  + System.lineSeparator());
           file.write( " Booking Id: " + bookingObj.id  + System.lineSeparator());
           file.write( " Arrival:    " + bookingObj.arrival  + System.lineSeparator());
           file.write( " Departure:  " + bookingObj.departure  + System.lineSeparator());

           file.write( "\n\n Total amount to pay: " + bookingObj.total  + System.lineSeparator());
           file.write("\n\n Above amount is inclusive 10% value added tax");
           file.write("\n\n\n\n\n----------------------------------------------------------------------------");
           file.write("\nLike in Your Dream Hotel, 2100, Nassfeld, Wald Straße 22. Id:984398494974339");
           file.close();
           System.out.println("\nSuccessfully wrote report to: "+pathAndFileName);
           System.out.println();

       } catch(IOException e){
               System.out.println("An error occurred during writing into the file.");
               e.printStackTrace();
           }
       catch (Exception e){
           System.out.println("The database is not available! Check your environment and start again!");
           e.printStackTrace();
       }
    return true;
   }

    /**
     * generates Reversed WorkInvoice   the name of the file is: invoiceReversal_X.txt, where X=bookingId
     *
     */
    public static boolean invoiceReversalGenerator(MyBooking bookingObj) {

        String path="C:\\Test";
        String pathAndFileName = path+"\\invoiceReversalErr.txt";
        String filename;
        String pathNew="C:\\Test";

        Scanner scanner = new Scanner(System.in);

        System.out.println("the report will be generated: "+path);

        String choice = MyMethods.askingYesOrNo("Do you want to change this? Y/N");
        boolean exit;
        exit= !choice.equals("Y");
        File file1;

        while(!exit) {
            System.out.println("Old Path: " + path + "   Please enter new Path: ");
            pathNew = scanner.nextLine();

            file1 = new File(pathNew);
            if (file1.exists()) {
                exit = true;
                pathAndFileName=pathNew+"\\invoiceReversalErr.txt";
                path=pathNew;
                System.out.println("Filepath is accepted!" + pathNew);
            } else {
                if(pathNew.equals("")){
                    System.out.println("Empty file path, process finished!");
                    System.out.println();
                    return false;
                }
                exit = false;
                System.out.println("NOT existing filepath!" + pathNew);
            }

        }
        Connection con;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/hotel?useTimezone=true&serverTimezone=UTC";
            con = DriverManager.getConnection(
                    url, "root", "");
            con.setAutoCommit(false);
            con.setReadOnly(false);

            String query1 = "insert into invoice (fk_bookings_id,fk_staff_id) values (?,?)";
            PreparedStatement pst1=con.prepareStatement(query1,Statement.RETURN_GENERATED_KEYS);
            pst1.setInt(1,bookingObj.id);
            pst1.setInt(2,bookingObj.fk_staff_id);
            pst1.executeUpdate();
            con.commit();

            //after writing new invoice to the invoice table, we need to get the generated ID of the invoice
            int invoiceId=0;
            Date invoiceDate=null;
            ResultSet generatedKeys = pst1.getGeneratedKeys();
            if (!generatedKeys.next()) {
                // Should never happen
                System.err.println("There is a problem with the Database, please contact the system admintrator!");
            } else {
                invoiceId = generatedKeys.getInt(1);
                filename="invoiceReversal"+bookingObj.id;
                pathAndFileName=path+"\\"+filename+".txt";
            }
            con.commit();
            pst1.close();
            //we want to get the generated date of the invoice
            String query2="SELECT  * from invoice where id=" + invoiceId;
            PreparedStatement pst2=con.prepareStatement(query2);
            ResultSet rs = pst2.executeQuery();
            con.commit();
            boolean invoiceFound=rs.next();
            if(invoiceFound){
                invoiceDate = rs.getDate("date");
                System.out.println("Invoice Id: "+invoiceId+"  Invoice Date: "+invoiceDate);
            }else{
                System.out.println("There is a big big technical problem, connect please the system Administrator");
            }
            pst2.close();
            con.close();
            FileWriter file = new FileWriter(pathAndFileName);
            file.write("\n**************************************************************************" + System.lineSeparator());
            file.write("\n                   Like in Your Dream Hotel "+System.lineSeparator());
            file.write("\n                        Invoice Reversal "+System.lineSeparator());
            file.write( "\n***************************************************************************" + System.lineSeparator());
            file.write( "\n\n Invoice number: " + invoiceId + " Date: " + invoiceDate + System.lineSeparator());
            file.write( "\n\n Guest: " + bookingObj.surName.toUpperCase() + "  "+bookingObj.lastName.toUpperCase() + System.lineSeparator());
            file.write( "\n Room:       " + bookingObj.roomNumber  + System.lineSeparator());
            file.write( " Booking Id: " + bookingObj.id  + System.lineSeparator());
            file.write( " Arrival:    " + bookingObj.arrival  + System.lineSeparator());
            file.write( " Departure:  " + bookingObj.departure  + System.lineSeparator());

            file.write( "\n\n Total amount to pay back: -" + bookingObj.total  + System.lineSeparator());
            file.write("\n\n Above amount is inclusive 10% value added tax");
            file.write("\n\n\n\n\n----------------------------------------------------------------------------");
            file.write("\nLike in Your Dream Hotel, 2100, Nassfeld, Wald Straße 22. Id:984398494974339");
            file.close();
            System.out.println("\nSuccessfully wrote report to: "+pathAndFileName);
            System.out.println();

        } catch(IOException e){
            System.out.println("An error occurred during writing into the file.");
            e.printStackTrace();
        }
        catch (Exception e){
            System.out.println("The database is not available! Check your environment and start again!");
            e.printStackTrace();
        }
        return true;
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
        return "WorkInvoice{" +
                "id=" + id +
                ", iDate=" + iDate +
                ", fk_bookings_id=" + fk_bookings_id +
                ", fk_stuff_id=" + fk_stuff_id +
                '}';
    }
}


