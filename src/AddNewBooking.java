import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddNewBooking {
    static int userId;
    static String userName;

    public AddNewBooking() {
    }

    public void addNewBooking(){
        System.out.println("Please check the guest data, hier you can enter booking only for existing guest!");
        String choice=WorkInvoice.askingYesOrNo("Do you want to continue? (Y/N) ");
        if (choice.equals("N")){
            return;
        }
        Connection con = WorkInvoice.init();
        if (con==null){
            System.out.println("There is no connection to the database!");
            return;
        }
        Guest guest;
        readmyGuests(con);
        guest=selectGuest(con);   //hier is insert guest ID
        Date arrival;
        Date departure;
        int period;
        do {
            arrival=askDate("Please enter the Date of arrival YYYY.MM.DD: ");
            departure = askDate("Please enter the Date of departure YYYY.MM.DD: ");
            period=differenceInDays(arrival.toLocalDate(),departure.toLocalDate());
            if(period<0){
                System.out.println("Arrival can not be later, than departure! Try it again, please!");
            }
        }while(period>=0);

        System.out.println("Booked nights: "+period);

        ArrayList<Room> freeRooms= availableRoom(con,arrival,departure);
        int fk_room_id=selectRoom(freeRooms);
        if (fk_room_id==0){
            System.out.println("there is something wrong with the Room number, we must exit!");
            return;
        }
        int total=0;
        int price=0;
        for (Room element:freeRooms) {
            if(fk_room_id== element.roomId){
                price=(int)element.price;
                total = period*price;   //askTotal();
            }
        }
        if(total<=0){
            System.out.println("There a problem with the price data, please add the final amount to pay!");
            total=askTotal();
        }else{
            System.out.println("Price of room: " +price+"   Final amount to pay : "+total);
        }
        String payment_type=enterPaymentType();  //{CREDIT_CARD, PAYPAL,BANK_TRANSFER,CASH}
        String status=enterStatus();            // {Booked,CANCELLED,PAYED,OPEN}
        String notes = enterNotes();

        int fk_staff_id=AddNewBooking.userId;
        if (fk_staff_id==0){
            fk_staff_id=1;
        }
        MyBooking newBooking= new MyBooking(0,arrival,departure,total,payment_type,status,notes,
                guest.getId(),fk_room_id,fk_staff_id,guest.getFirstName(),guest.getLastName(),0,userName);
        int newBookingId=writeNewBooking(newBooking,con);
        try {
            con.close();
            System.out.println("Booking is successful! The booking Id is: "+newBookingId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static int differenceInDays(LocalDate start, LocalDate end) {
       if (start.isEqual(end)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(start, end);
    }

    public Guest selectGuest(Connection con) {
        Scanner scanner = new Scanner(System.in);
        int selection;
        boolean exit = false;
        Guest guest = null;
        while (!exit) {
            System.out.println("Insert Guest ID: ");
            try {
                selection = Integer.parseInt(scanner.nextLine());
                guest = checkGuestId(selection, con);
                if (guest != null || (selection == 0)) {
                    if (selection == 0) {
                        guest = null;
                    }
                    exit = true;
                } else {
                    System.out.println("Not existing Guest Id, try to enter again, or add 0 to exit");
                    exit = false;
                }
            } catch (Exception e) {
                System.out.println("Please Enter one of the user Ids, only numbers are allowed!");
            }
        }
        return guest;
    }
    public Guest checkGuestId(int selection, Connection con) {
        Guest guest = null;
        try {
            List<Guest> guests = getAllRowsGuests(con);
            if (guests != null) {
                for (Guest item : guests) {
                    if (item.getId() == selection) {
                        guest = item;
                        con.commit();
                        return guest;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.commit();
            } catch (SQLException throwables) {
                System.out.println("There is a Problem with the connection!");
                throwables.printStackTrace();
            }
        }
        return guest;
    }


    public void readmyGuests(Connection con) {
        try {
            List<Guest> guests=getAllRowsGuests(con);
            if(guests== null){
                System.out.println("Database can not be connected!");
                System.out.println();
            }else{
                if (guests.size()==0){
                    System.out.println("There are no guests in the database!");
                    System.out.println();
                }else {
                    displayRowsGuests(guests);
                }}
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static List<Guest> getAllRowsGuests(Connection connection) {
        String sql = "SELECT * FROM guests order by last_name, first_name";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            List<Guest> list = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String address = rs.getString("address");
                String zip = rs.getString("zip");
                String country = rs.getString("country");
                String email = rs.getString("email");
                java.sql.Date birth = rs.getDate("birth");
                String phoneNumber = rs.getString("phone_number");
                String document = rs.getString("document");
                //java.util.Date arrival = rs.getDate("arrival_date");
                //java.util.Date depature = rs.getDate("departure_date");

                list.add(new Guest(id, firstName, lastName,
                        address, zip, country, email, birth, phoneNumber, document,null,null));

            }
            connection.commit();
            preparedStatement.close();
            return list;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public void displayRowsGuests(List<Guest> list) {
        System.out.println("*****************************************************************************");
        System.out.println("*                         List of all Guests                              *");
        System.out.println("*****************************************************************************");
        //String formattedId="";
        for(Guest item : list) {


            System.out.println("Guest ID: "+item.getId()+"   "+formatMyString(15,item.getFirstName().toUpperCase())+
                    "  "+formatMyString(20,item.getLastName().toUpperCase()));


        }
        System.out.println();
        System.out.println();

    }


    /**
     * method formatMyString makes the string for given length for your lists
     * if the string is shorter than the length in the argument, than some empty char will be added
     * if it is shorter, the end of string will be cut away
     * @param length  = how long place I give for this string in the list
     * @param myString - the string to be formatted
     * @return formatted string
     */
    public String formatMyString(int length,String myString){
        StringBuilder x= new StringBuilder(length);
        for (int n=0;n<length;n++){
            x.append(" ");
        }
        String formattedString=myString+x; //field to help formatting name  20 char long in the list
        formattedString=formattedString.substring(0,length-1);

        return formattedString;
    }

    /**
     *
     * @param text  text to ask which date you need
     *              for example:"Please enter the Date of arrival yyyy.mm.dd: "
     * @return entered date
     */
    public Date askDate(String text){

        boolean exit=false;
        String myDate= "";
        System.out.println();
        Scanner sc = new Scanner(System.in);
        while(!exit) {
            System.out.print(text);
            myDate = sc.nextLine();
            try {
                if (myDate.length()==10){

                    exit = isValid(myDate.substring(0, 4) + "-" +
                            myDate.substring(5, 7) + "-" + myDate.substring(8, 10));
                }else {
                    System.out.println("Date is too short or too long");
                    exit = false;
                }
            }catch (Exception e){
                System.out.println("Not valid Date, try it again!");
                exit = false;
            }
        }
        return Date.valueOf(myDate.substring(0, 4) + "-" +
                myDate.substring(5, 7) + "-" + myDate.substring(8, 10));
    }



    public boolean isValid(String date) {
        boolean valid; //= false;
        try {
            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu-M-d")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            valid = true;
        } catch (DateTimeParseException e) {
            System.out.println("Not valid Date, try again!");
            //e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    public int askTotal(){
        boolean exit=false;
        int total=0;
        Scanner scanner = new Scanner(System.in);
        while(!exit) {
            try {
                System.out.println("Total Amount to pay in EUR: ");
                total = scanner.nextInt();
                exit=true;
            } catch (Exception e) {
                System.out.println("Not valid Input, try again!");
                //e.printStackTrace();
                scanner.next();
                total=0;
                exit = false;
            }
        }
        return total;
    }

    public String enterPaymentType() {

        boolean exit = false;
        int selection;
        System.out.println("Please select payment type! 1.Credit Card  2. Paypal 3.Bank transfer 4.Cash");
        while (!exit) {
            selection = enterSelection();
            switch (selection) {
                case 1:
                    return "credit card";
                case 2:
                    return "paypal";
                case 3:
                    return "bank transfer";
                case 4:
                    return "cash";
                default:
                    System.out.println("The selection was invalid!");
            }
        }
        return "cash";
    }

    public int enterSelection(){
        Scanner scanner= new Scanner(System.in);
        int selection = -1;
        boolean exit=false;
        while(!exit ) {
            try {
                selection = Integer.parseInt(scanner.nextLine());
                exit=true;
            } catch (Exception e) {
                System.out.println("Please Enter one of the options, only numbers are allowed!");
            }
        }
        return selection;
    }


    public String enterStatus() {
        String paymentType = "";
        boolean exit = false;
        int selection;
        System.out.println("Please select status! 1.Booked  2. Cancelled 3.Payed 4.Open");
        while (!exit) {
            selection = enterSelection();
            switch (selection) {
                case 1:
                    return "booked";
                case 2:
                    return "cancelled";
                case 3:
                    return "payed";
                case 4:
                    return "";
                default:
                    System.out.println("The selection was invalid!");
            }
        }
        return "";
    }

    public String enterNotes(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Notes: ");
        String notes = scanner.nextLine();
        return notes;
    }

    public static ArrayList<Room> availableRoom(Connection con, Date arrival, Date departure) {
        ArrayList<Room> freeRooms=new ArrayList<>();
        try {

            Date sqlDateFrom = arrival;
            Date sqlDateTo = departure;
            int count=0;
            PreparedStatement ps = con.prepareStatement("select rr.id, cc.name, cc.capacity, cc.price, cc.roomsize from rooms as rr \n" +
                    "inner join category as cc on cc.id=rr.fk_category_id\n" +
                    "where rr.id not in(SELECT bb.fk_room_id from bookings as bb where bb.departure_date>=? and bb.arrival_date<=?)");
            ps.setDate(1, sqlDateFrom);
            ps.setDate(2, sqlDateTo);
            ResultSet rs = ps.executeQuery();
            System.out.println("-----available rooms -----");
            while (rs.next()) {
                count++;
                freeRooms.add(new Room(rs.getInt("id"),rs.getFloat("price")));
                System.out.println("ID: " + rs.getInt("id") + " " + rs.getString("name") + " price: " + rs.getFloat("price"));
            }
            if(count==0){
                freeRooms=null;
                System.out.println("We do not have free room in this time period");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return freeRooms;
    }

    public int selectRoom(ArrayList<Room> freeRooms){
        boolean exit = false;
        int selection;
        System.out.println("Please select a Room!");
        while (!exit) {
            selection = enterSelection();
            for (Room element: freeRooms) {
                if(selection==element.roomId){
                    return selection;
                }
            }
        }
        return 0;
    }
    public int writeNewBooking(MyBooking newBooking, Connection con) {
        int bookingId=0;
        try {
            String query1 = "insert into bookings (arrival_date,departure_date,total_price,payment_type,status," +
                    "notes,fk_guest_id,fk_room_id,fk_staff_id) values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setDate(1, (Date) newBooking.arrival);
            pst1.setDate(2, (Date) newBooking.departure);
            pst1.setInt(3, newBooking.total);
            pst1.setString(4, newBooking.payment_type);
            pst1.setString(5, newBooking.status);
            pst1.setString(6,newBooking.notes);
            pst1.setInt(7,newBooking.fk_guest_id);
            pst1.setInt(8,newBooking.fk_room_id);
            pst1.setInt(9,newBooking.fk_staff_id);
            pst1.executeUpdate();
            con.commit();
            pst1.close();
            String query2="SELECT  * from bookings ORDER BY ID DESC LIMIT 1";
            PreparedStatement pst2=con.prepareStatement(query2);
            ResultSet rs = pst2.executeQuery();
            if(rs.next()){
                bookingId=rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingId;
    }

}
