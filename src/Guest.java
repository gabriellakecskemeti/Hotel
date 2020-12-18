import java.sql.*;
import java.util.ArrayList;
import java.util.Date;


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
    private Date depature;

    public Guest() {
    }

    public Guest(int id, String firstName, String lastName, String address, String zip, String country, String email, Date birth, String phoneNumber, String document, Date arrival, Date depature) {
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
        this.depature = depature;
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

    public Date getDepature() {
        return depature;
    }

    public void setDepature(Date depature) {
        this.depature = depature;
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
                ", depature=" + depature +
                '}';
    }

    /**
     *
     * @param option  1= all guests    2= guests today
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
            }else{
                sql = "SELECT * FROM `bookings` LEFT JOIN guests on fk_guest_id = guests.id ";
            }

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();

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
                    Date arrival = rs.getDate("arrival_date");
                    Date depature = rs.getDate("departure_date");

                    guestList.add(new Guest(id, firstName, lastName, address, zip, country, email, birth, phoneNumber, document, arrival, depature));
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
     * @param option 1= all guests    2= guests today
     */
    public void displayAllGuests(int option) {

        ArrayList<Guest> guestList = getAllGuests(option);

        System.out.println("Guest list:");
        System.out.println();
        System.out.println("First Name    Last Name      Address                    Zip     " +
                "   Country         Email          " +
                " Birth Date  Phone Number    Document             Arrival      Departure ");
        for (int i = 0; i < guestList.size(); i++) {

            System.out.println(formatString(12, guestList.get(i).getFirstName().toUpperCase()) + "  " +
                    formatString(15, guestList.get(i).getLastName().toUpperCase()) + "  " +
                    formatString(25, guestList.get(i).getAddress()) + "   " +
                    formatString(10, guestList.get(i).getZip()) + "  " +
                    formatString(15, guestList.get(i).getCountry()) + "  " +
                    formatString(15, guestList.get(i).getEmail()) + "  " + guestList.get(i).getBirth() + "  " +
                    formatString(15, guestList.get(i).getPhoneNumber()) + "  " +
                    formatString(20, guestList.get(i).getDocument()) + "  " +
                    guestList.get(i).getArrival() + " / " + guestList.get(i).getDepature());
        }
        System.out.println();
    }

    public static String formatString(int length, String myString) {
        StringBuilder x = new StringBuilder(length);
        for (int n = 0; n < length; n++) {
            x.append(" ");
        }
        String formattedString = myString;
        int lengthOfMyString = myString.length();
        String part1OfMyString;
        String part2OfMyString;
        if (length < lengthOfMyString) {
            part1OfMyString = myString.substring(0, length - 1);
            part2OfMyString = myString.substring(length, lengthOfMyString - 1);
            if (part2OfMyString.isEmpty()) {
                formattedString = formattedString.substring(0, length - 1);
            }
        } else {
            formattedString = myString + x; //field to help formatting name  20 char long in the list
            formattedString = formattedString.substring(0, length - 1);
        }
        return formattedString;
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
                    "SELECT ?,?,?,?,?,?,?,?,? \n" +
                    "where ? not in (SELECT document from guests)");

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, address);
            ps.setString(4, ZIP);
            ps.setString(5, country);
            ps.setString(6, email);
            ps.setDate(7, birthDate);
            ps.setString(8, telephone);
            ps.setString(9, document);
            ps.setString(10, document);

            int success = ps.executeUpdate();

            if (success >0) {
                System.out.println("Inserted in database !!!");

            } else {
                System.out.println("Guest with that document exists in database");
            }
    }catch (Exception e){
        e.printStackTrace();
    }
}


}
