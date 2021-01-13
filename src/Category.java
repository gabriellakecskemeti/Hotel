import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Category {
    private  int id;
    private String categoryName;
    private int capacity;
    private double price;
    private int roomSize;
    private String facilities;

    public Category() {
    }

    public Category(int id, String categoryName, int capacity, double price, int roomSize, String facilities) {
        this.id = id;
        this.categoryName = categoryName;
        this.capacity = capacity;
        this.price = price;
        this.roomSize = roomSize;
        this.facilities = facilities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }


    public static ArrayList<Category> getAllCategory(){
        ArrayList<Category> allCategory= new ArrayList<>();
        try {
            DataAccess dataAccess = new DataAccess();
            Connection con=dataAccess.getConnection();
            String sql;
            sql = "SELECT * FROM category ORDER BY name";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            int id;
            String categoryName;
            int capacity;
            double price;
            int roomSize;
            String facilities;
            while (rs.next()) {
                id = rs.getInt("id");
                categoryName=rs.getString("name");
                capacity=rs.getInt("capacity");
                price=rs.getDouble("price");
                roomSize=rs.getInt("roomsize");
                facilities=rs.getString("facilities");
                allCategory.add(new Category(id,categoryName,capacity,price,roomSize,facilities ));
            }
            rs.close();
            preparedStatement.close();
            con.close();
        }catch (SQLException | ClassNotFoundException throwables) {
            System.out.println("There is no connection to the database! ");
            throwables.printStackTrace();
        }
        return allCategory;
    }

    public static void listAllCategory(ArrayList<Category> allCategory){
        System.out.println("*****************Room categories*****************");
        if (allCategory.isEmpty()){
            System.out.println("The table of categories is empty!");
            System.out.println();
        }else {
            System.out.println("  ID   Name                  Capacity      Price    m2   Facilities");
            for (Category element : allCategory) {
                System.out.println(" "+MyMethods.formatMyInt(3, element.id)+" | "+
                        MyMethods.formatString(20,element.categoryName)+"  |  "+
                        MyMethods.formatMyInt(3, element.capacity)+" | "+
                        MyMethods.formatMyDouble(10, element.price)+" | "+
                        MyMethods.formatMyInt(3, element.roomSize)+" | "+element.facilities);

            }
            System.out.println();
        }
    }

    public void updateRoomPrice(){
        ArrayList<Category> allCategory= getAllCategory();
        listAllCategory(allCategory);
        Scanner scanner= new Scanner(System.in);

        Category selectedCategory=selectCategory();
        if (selectedCategory==null){
            System.out.println("You choose 0. Exit!");
            return;
        }
        System.out.println("Category ID: "+selectedCategory.id);
        System.out.println("Name:        "+selectedCategory.categoryName);
        System.out.println("Price:       "+selectedCategory.price);
        boolean finished=false;
        double newPrice = 0;
        do {
            System.out.println("Please enter new Price: ");
            try {
                newPrice = scanner.nextDouble();
                finished = true;
            } catch (Exception e) {
                System.out.println("Please enter numbers, dots are also not allowed. Allowed: 999,99!");
                //e.printStackTrace();
                scanner.nextLine();
            }
        }while(!finished);

        try {
            DataAccess dataAccess = new DataAccess();
            Connection con=dataAccess.getConnection();
            String sql;
            sql = "Update category set price =? where category.id=?";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setInt(2, selectedCategory.id);

            int result = preparedStatement.executeUpdate();
            if (result==1){
                System.out.println("Price has been changed.");
            }else{
                System.out.println("Please contact the system Administrator, change was not successful.");
            }
            preparedStatement.close();
            con.close();

        }catch(SQLException | ClassNotFoundException e){
            System.out.println("The Data Base is not available!");
            //e.printStackTrace();
        }
    }


    private Category selectCategory() {
        Scanner scanner = new Scanner(System.in);
        int selection;
        boolean exit = false;

        int foundID;
        Category selectedCategory = new Category();
        try {
            DataAccess dataAccess = new DataAccess();
            Connection con = dataAccess.getConnection();
            while (!exit) {
                System.out.println("Insert Category ID: ");    //User Id=Staff Id
                try {
                    selection = Integer.parseInt(scanner.nextLine().trim());
                    String sql = "SELECT * FROM category where id=?";

                    foundID = 0;
                    try {
                        PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setInt(1, selection);
                        ResultSet rs = preparedStatement.executeQuery();

                        while (rs.next()) {
                            selectedCategory.setId(selection);
                            selectedCategory.setCategoryName(rs.getString("name"));
                            selectedCategory.setCapacity(rs.getInt("capacity"));
                            selectedCategory.setPrice(rs.getInt("price"));
                            selectedCategory.setRoomSize(rs.getInt("roomsize"));
                            selectedCategory.setFacilities(rs.getString("facilities"));

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
                            selectedCategory = null;
                        }
                        exit = true;
                    } else {
                        System.out.println("Not existing Id, try to enter again, or add 0 to exit");
                        exit = false;
                    }
                } catch (Exception e) {
                    System.out.println("Please Enter one of the Ids, only numbers are allowed!");
                }
            }
            con.close();
        }catch(SQLException | ClassNotFoundException throwables) {
            System.out.println("There is no connection to the database! ");
            throwables.printStackTrace();
        }

        return selectedCategory;
    }




}
