import java.sql.Connection;
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
     *
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



}
