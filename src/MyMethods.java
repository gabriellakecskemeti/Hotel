import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MyMethods {

    /**
     * method formatString makes the string for given length for your lists
     * if the string is shorter than the length in the argument, than some empty char will be added
     * if it is shorter, the end of string will be cut away
     * @param length  = how long place I give for this string in the list
     * @param myString - the string to be formatted
     * @return formatted string
     */
    public static String formatString(int length, String myString) {
        StringBuilder x = new StringBuilder();
        for (int n = 0; n < length; n++) {
            x.append(" ");
        }
        StringBuilder formattedString = new StringBuilder();
        int lengthOfMyString = myString.length();
        String part2OfMyString;
        if (length < lengthOfMyString) {
            part2OfMyString = myString.substring(length, lengthOfMyString - 1);
            if (part2OfMyString.isEmpty()) {
                formattedString.append(myString);
            }
        } else {
            formattedString.append(myString);
            formattedString.append(x);
            myString=formattedString.toString().substring(0, length); //x=field to help formatting name  20 char long in the list
        }

        return myString;
    }

    /**
     * formatMyDouble method makes the number right aligned and gives back a String for the list
     * in the given length, if the number is longer than the given length, a longer string will be
     * returned, because this method, do not gives back wrong values.
     *
     * @param length
     * @param number
     * @return
     */
    public static String formatMyInt(int length,int number){
        String x="";
        for (int n=0;n<length;n++){
            x=x+" ";
        }
        String niceInt = String.valueOf(number); //field to format and right aline the given number

        if (niceInt.length()<length) {
            int y=niceInt.length()+length;
            niceInt = (x+niceInt).substring(y-length,y);  //right align the number
        }

        return niceInt;
    }
    /**
     * formatMyDouble method makes the number right aligned and gives back a String for the list
     * in the given length, if the number is longer than the given length, a longer string will be
     * returned, because this method, do not gives back wrong values.
     *
     * @param length
     * @param number
     * @return
     */
    public static String formatMyDouble(int length,Double number){
        String x="";
        for (int n=0;n<length;n++){
            x=x+" ";
        }
        String niceDouble = String.valueOf(number); //field to format and right aline the given number

        if (niceDouble.length()<length) {
            int y=niceDouble.length()+length;
            niceDouble = (x+niceDouble).substring(y-length,y);  //right align the number
        }

        return niceDouble;
    }

    /**
     *it takes a question as parameter and it asks if the answer is yes or no.
     * @param question this is the text,the question to answer with yes or no
     * @return it gives back Y or N in form of a String object
     */
    public static String askingYesOrNo(String question){
        String choice="";
        Scanner scanner = new Scanner(System.in);
        boolean exit=false;
        while (!exit){
            System.out.println(question);
            choice=scanner.nextLine().trim();
            choice=choice.toUpperCase();

            if (choice.equals("N") || choice.equals("Y")  ){
                exit=true;
            }else{
                System.out.println("Wrong input, can only be Y/N.");
            }
        }
        return choice.toUpperCase();
    }

    static public void init(){
        try{
            DataAccess dataAccess = new DataAccess();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    static public void stop(DataAccess dataAccess){
        try{
            dataAccess.getConnection().close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * it reads the information_schema and gives back the specified enum values (max 100 items)
     * if the column is not en enum and it has no enum values, the array list will be empty!
     * @param tname = table name, where is the enum to read
     * @param cname = the column, it has to be an enum
     * @return arraylist contains the enum values of specified table column.
     */
    static ArrayList<String> enumReaderV2(String tname, String cname){
        ArrayList<String> enumValues= new ArrayList<>();
        String tnameapo= "\'"+tname+"\'";
        String cnameapo= "\'"+cname+"\'";
        try {
            String url = "jdbc:mysql://localhost:3306/information_schema" + "?useTimezone=true&serverTimezone=UTC";
            Connection connection;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, "root", "");
            connection.setAutoCommit(true);
            connection.setReadOnly(false);

            String x="\"','\"";

            String a= "SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING(COLUMN_TYPE, 7, LENGTH(COLUMN_TYPE) - 8), "+x+", 1 + units.i + tens.i * 10) ,"+x+" , -1) FROM INFORMATION_SCHEMA.COLUMNS CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) units CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) tens WHERE TABLE_NAME = "+"\'"+tname+"\'"+" AND COLUMN_NAME = "+"\'"+cname+"\'";

            PreparedStatement psEnum= connection.prepareStatement(a);
            ResultSet rsEnum = null;
            rsEnum = psEnum.executeQuery();
            while (rsEnum.next()){
                enumValues.add(rsEnum.getString(1));
            }
            rsEnum.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return enumValues;
    }

      

}
