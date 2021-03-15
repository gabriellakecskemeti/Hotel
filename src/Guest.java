import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Guest {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String zip;
    private String country;
    private String email;
    private Date birth;
    private String phoneNumber;
    private String document;
    private Date arrival;
    private Date departure;

    public Guest() {
    }

    public Guest(int id, String firstName, String lastName, String address, String zip, String country, String email, Date birth, String phoneNumber, String document, Date arrival, Date departure) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.zip = zip;
        this.country = country;
        this.email = email;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.document = document;
        this.arrival = arrival;
        this.departure = departure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    @Override
    public String toString() {
        return "DisplayGuests{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                ", phoneNumber='" + phoneNumber + '\t' +
                ", document='" + document + '\t' +
                ", arrival=" + arrival +
                ", departure=" + departure +
                '}';
    }

    /**
     *
     * @param option  1= guests with booking today or later   2= guests today 3= all guests
     * @return
     */
    public ArrayList<Guest> getAllGuests(int option) {
        ArrayList<Guest> guestList = new ArrayList<>();

        try {
            DataAccess dataAccess = new DataAccess();
            Connection con=dataAccess.getConnection();
            String sql;
            if(option==2) {
                sql = "SELECT * FROM `bookings` LEFT JOIN guests on fk_guest_id = guests.id " +
                        "WHERE CURDATE() >= arrival_date AND CURDATE() <= departure_date;";
            }else if (option==1){
                sql = "SELECT * FROM `bookings` LEFT JOIN guests on fk_guest_id = guests.id " +
                        "WHERE CURDATE() <= arrival_date ORDER BY guests.last_name, guests.first_name";
            }else{
                sql = "SELECT * FROM `guests`  ORDER BY guests.last_name, guests.first_name";
            }

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();
                Date arrival=null; //only for option 1 and 2 will be listed
                Date departure=null;
                while (rs.next()) {
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
                    if (option!=3){
                        arrival = rs.getDate("arrival_date");
                        departure = rs.getDate("departure_date");
                    }
                    guestList.add(new Guest(id, firstName, lastName, address, zip, country, email, birth, phoneNumber, document, arrival, departure));
                }
                rs.close();
                preparedStatement.close();
                con.close();

        }catch (SQLException | ClassNotFoundException throwables) {
            System.out.println("There is no connection to the database! ");
            throwables.printStackTrace();
        }
        return guestList;
    }

    /**
     *
     * @param option 1= guests with booking today or later   2= guests today 3= all guests
     */
    public void displayAllGuests(int option) {

        ArrayList<Guest> guestList = getAllGuests(option);


        System.out.println("*********************");
        System.out.println("*    Guest list     *");
        System.out.println("*********************");

        if (guestList.isEmpty()){
            System.out.println();
            System.out.println("I could not find GUESTS for this list.");
            System.out.println();
        }else {

            if (option==3) {
                System.out.println("First Name    Last Name        Address                     Zip     " +
                        "    Country          Email                                  " +
                        "   Birth Date  Phone Number     Document       ");
                for (int i = 0; i < guestList.size(); i++) {

                    System.out.println(MyMethods.formatString(12, guestList.get(i).getFirstName().toUpperCase()) + "  " +
                            MyMethods.formatString(15, guestList.get(i).getLastName().toUpperCase()) + "  " +
                            MyMethods.formatString(25, guestList.get(i).getAddress()) + "   " +
                            MyMethods.formatString(10, guestList.get(i).getZip()) + "  " +
                            MyMethods.formatString(15, guestList.get(i).getCountry()) + "  " +
                            MyMethods.formatString(40, guestList.get(i).getEmail()) + "  " + guestList.get(i).getBirth() + "  " +
                            MyMethods.formatString(15, guestList.get(i).getPhoneNumber()) + "  " +guestList.get(i).getDocument());

                }
            }else{
                System.out.println("First Name    Last Name        Address                     Zip     " +
                        "    Country          Email                                  " +
                        "   Birth Date  Phone Number     Document              Arrival    / Departure ");
                for (int i = 0; i < guestList.size(); i++) {

                    System.out.println(MyMethods.formatString(12, guestList.get(i).getFirstName().toUpperCase()) + "  " +
                            MyMethods.formatString(15, guestList.get(i).getLastName().toUpperCase()) + "  " +
                            MyMethods.formatString(25, guestList.get(i).getAddress()) + "   " +
                            MyMethods.formatString(10, guestList.get(i).getZip()) + "  " +
                            MyMethods.formatString(15, guestList.get(i).getCountry()) + "  " +
                            MyMethods.formatString(40, guestList.get(i).getEmail()) + "  " + guestList.get(i).getBirth() + "  " +
                            MyMethods.formatString(15, guestList.get(i).getPhoneNumber()) + "  " +
                            MyMethods.formatString(20, guestList.get(i).getDocument()) + "  " +
                            guestList.get(i).getArrival() + " / " + guestList.get(i).getDeparture());

                }
            }
            System.out.println();
        }
    }



    public void addNewGuest()  {
        Scanner scanner=new Scanner(System.in);
        try {
            DataAccess dataAccess=new DataAccess();
            Connection connection = dataAccess.getConnection();

            System.out.println("Enter new Guest");

            System.out.println("Enter FirstName");
            String firstName = scanner.nextLine();

            System.out.println("Enter lastname");
            String lastName = scanner.nextLine();

            System.out.println("Enter address");
            String address = scanner.nextLine();

            System.out.println("Enter Zip");
            String ZIP = scanner.nextLine();

            System.out.println("Enter country code");
            String country = scanner.nextLine();

            System.out.println("Enter email");
            String email = scanner.nextLine();

            AddNewBooking anbObj=new AddNewBooking();
            java.sql.Date birthDate = anbObj.askDate("Enter birthdate in format yyyy-MM-dd: ");   //scanner.next();

            System.out.println("Enter telephone");
            String telephone = scanner.next();

            System.out.println("Enter document");
            String document = scanner.next();


            PreparedStatement ps = connection.prepareStatement("insert into guests (`first_name`,`last_name`," +
                    "`address`,`zip`,`country`,`email`,`birth`,`phone_number`,`document`)\n" +
                    "SELECT ?,?,?,?,?,?,?,?,? \n", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, address);
            ps.setString(4, ZIP);
            ps.setString(5, country);
            ps.setString(6, email);
            ps.setDate(7, birthDate);
            ps.setString(8, telephone);
            ps.setString(9, document);


            int success = ps.executeUpdate();

            if (success >0) {
                System.out.println("Inserted in database !!!");

            } else {
                System.out.println("Data could not be entered to the Database. Please call the system Administrator!");
            }
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (!generatedKeys.next()) {
                // Should never happen
                System.err.println("Database did not return generated guest ID, please contact the system administrator!");
            } else {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("Following guest ID has been generated: " + generatedId+ "  for "+firstName+"  "+lastName);
            }

    }catch (Exception e){
        e.printStackTrace();
    }
}
    protected void deleteGuest(){
        ArrayList<Guest> guestList = getAllGuests(3);

        if (guestList.isEmpty()){
            System.out.println();
            System.out.println("The Table guests is empty!");
            System.out.println();
        }else{

            System.out.println("*********************");
            System.out.println("*    Guest list     *");
            System.out.println("*********************");
            System.out.println(" ID   First Name    Last Name        Address                     Zip     " +
                    "    Country          Email                             " +
                    "   Birth Date  Phone Number     Document  ");
            for (int i = 0; i < guestList.size(); i++) {

                System.out.println(MyMethods.formatMyInt(3,guestList.get(i).id)+"   "+
                        MyMethods.formatString(12, guestList.get(i).getFirstName().toUpperCase()) + "  " +
                        MyMethods.formatString(15, guestList.get(i).getLastName().toUpperCase()) + "  " +
                        MyMethods.formatString(25, guestList.get(i).getAddress()) + "   " +
                        MyMethods.formatString(10, guestList.get(i).getZip()) + "  " +
                        MyMethods.formatString(15, guestList.get(i).getCountry()) + "  " +
                        MyMethods.formatString(35, guestList.get(i).getEmail()) + "  " + guestList.get(i).getBirth() + "  " +
                        MyMethods.formatString(15, guestList.get(i).getPhoneNumber()) + "  " +
                        MyMethods.formatString(20, guestList.get(i).getDocument()));
            }
            System.out.println();
        }
        // System.out.println("Select User ID to delete:");
        boolean selected=false;
        Guest selectedGuest= null;
        do{
            selectedGuest=selectGuest();
            if (selectedGuest!=null){
                selected=true;
                if (selectedGuest.id==0){
                    return;
                }
            }
        }while(!selected);
        System.out.println("Guest ID:   "+selectedGuest.id);
        System.out.println("First name: "+selectedGuest.firstName);
        System.out.println("Last name:  "+selectedGuest.lastName);
        System.out.println("Document:   "+selectedGuest.document);
        String choice=MyMethods.askingYesOrNo(" Do you really want to delete?  (Y/N)");
        System.out.println();
        if (choice.equals("N")){
            return;
        }
        try {
            DataAccess dataAccess = new DataAccess();
            Connection con=dataAccess.getConnection();
            String sql;
            sql = "Delete FROM guests where guests.id=?";
            String sql1= "select * FROM bookings WHERE bookings.fk_guest_id=?";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            PreparedStatement preparedStatement1 = con.prepareStatement(sql1);
            preparedStatement.setInt(1, selectedGuest.id);
            preparedStatement1.setInt(1,selectedGuest.id);

            ResultSet rs = preparedStatement1.executeQuery();
            int counter=0;
            while (rs.next()){
                System.out.println("Booking id: "+rs.getInt("id")+
                        "  Arrival: "+rs.getDate("arrival_date")+
                        "  Departure: "+rs.getDate("departure_date")+
                        "  Status: "+rs.getString("status"));
                counter++;
            }
            if (counter>0){
                choice=MyMethods.askingYesOrNo(" There are bookings saved with this Guest Id, do you really want to delete?  (Y/N)");
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


    private Guest selectGuest() {
        Scanner scanner = new Scanner(System.in);
        int selection;
        boolean exit = false;

        int foundID;
        Guest selectedGuest = new Guest();
        try {
            DataAccess dataAccess = new DataAccess();
            Connection con = dataAccess.getConnection();
            while (!exit) {
                System.out.println("Insert Guest ID: ");
                try {
                    selection = Integer.parseInt(scanner.nextLine().trim());
                    if (selection==0){
                        selectedGuest.setId(0);
                        return selectedGuest;
                    }
                    String sql = "SELECT * FROM guests where id=?";

                    foundID = 0;
                    try {
                        PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setInt(1, selection);
                        ResultSet rs = preparedStatement.executeQuery();

                        while (rs.next()) {

                            selectedGuest.setId(selection);
                            selectedGuest.setFirstName(rs.getString("first_name"));
                            selectedGuest.setLastName(rs.getString("last_name"));
                            selectedGuest.setAddress(rs.getString("address"));
                            selectedGuest.setZip(rs.getString("zip"));
                            selectedGuest.setCountry(rs.getString("country"));
                            selectedGuest.setEmail(rs.getString("email"));
                            selectedGuest.setBirth(rs.getDate("birth"));
                            selectedGuest.setPhoneNumber(rs.getString("phone_number"));
                            selectedGuest.setDocument(rs.getString("document"));

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
                            selectedGuest = null;
                        }
                        exit = true;
                    } else {
                        System.out.println("Not existing Id, try to enter again, or add 0 to exit");
                        exit = false;
                    }
                } catch (Exception e) {
                    System.out.println("Please Enter one of the guest Ids, only numbers are allowed!");
                }
            }
            con.close();
        }catch(SQLException | ClassNotFoundException throwables) {
            System.out.println("There is no connection to the database! ");
            throwables.printStackTrace();
        }

        return selectedGuest;
    }

}
