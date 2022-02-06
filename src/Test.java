import com.mysql.cj.jdbc.DatabaseMetaData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Objects.hash;

public class Test {
    public static void main(String[] args) {



        String test= "abc*kkkk";
        String testa= test.substring(1,2);

        System.out.println(testa);
        System.out.println(test.substring(0,3));
        System.out.println(test.substring(1,3));

/*
        SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING(COLUMN_TYPE, 7, LENGTH(COLUMN_TYPE) - 8), "','", 1 + units.i + tens.i * 10) , "','", -1)
        FROM INFORMATION_SCHEMA.COLUMNS
        CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) units
        CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) tens
        WHERE TABLE_NAME = 'mytable'
        AND COLUMN_NAME = 'mycolumn'


        SHOW COLUMNS FROM `bookings` WHERE field = 'status'

SELECT column_type FROM information_schema.columns
WHERE table_schema = 'Hotel' AND table_name = 'bookings' AND column_name = 'status'


        try {
            String url = "jdbc:mysql://localhost:3306/information_schema" + "?useTimezone=true&serverTimezone=UTC";
            Connection connection;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, "root", "");
            connection.setAutoCommit(true);
            connection.setReadOnly(false);


            PreparedStatement psEnum = connection.prepareStatement("SELECT column_type " +
                    "FROM information_schema.columns " +
                    "WHERE table_schema = 'Hotel' AND table_name = 'bookings' AND column_name = 'status'");

            //String x="\"','\"";
            //System.out.println(x+"********"+x);
            //String a= "SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING(COLUMN_TYPE, 7, LENGTH(COLUMN_TYPE) - 8), "+x+", 1 + units.i + tens.i * 10)
            // ,"+x+" , -1) FROM INFORMATION_SCHEMA.COLUMNS CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) units CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) tens WHERE TABLE_NAME = 'bookings' AND COLUMN_NAME = 'status'";
            //PreparedStatement psEnum= connection.prepareStatement(a);

            ResultSet rsEnum = null;

            rsEnum = psEnum.executeQuery();

        System.out.println("Enum values of status: ");

        String enumval=null;


        while (rsEnum.next()){
            System.out.println("First version");
            enumval=rsEnum.getString(1);
            System.out.println(enumval);
        }
        String eval=enumval.substring(5,enumval.length()-1);
            System.out.println(eval);
        String[] ee=eval.split(",");

        ArrayList<String> enumReader=new ArrayList<>();
            for (String element:eval.split(",")) {
                enumReader.add(element.substring(1,element.length()-1));
            }

            System.out.println("--------arraylist--------");
            for (String element:enumReader) {
                System.out.println(element);
            }

            System.out.println("The array");
        for(int i=0;i<ee.length;i++){
            System.out.println(ee[i]);
        }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }


        /*ArrayList<String> aaa= enumReader("bookings","payment_type");
        System.out.println("Enum values of status: ");
        for (String element:aaa) {
            System.out.println(element);
        }
*/
        ArrayList<String> aa=MyMethods.enumReaderV2("bookings","payment_type");
        System.out.println("payment types");
        for (String element:aa) {
            System.out.println(element);
        }


        System.out.println();
        System.out.println();
         try {
            String originalPassword = "789";
            String generatedSecuredPasswordHash = DataAccess.generateStrongPasswordHash(originalPassword);
            System.out.println("very very secure hash   "+generatedSecuredPasswordHash);

            boolean matched = DataAccess.validatePassword("789", generatedSecuredPasswordHash);
            System.out.println(matched);

            matched = DataAccess.validatePassword("124", generatedSecuredPasswordHash);
            System.out.println(matched);
        }catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(hash("test123"));

        String pwObj1="789";
        String pwObj2="789";
        String pwObj3="789";

        System.out.println(pwObj1.hashCode());
        System.out.println(pwObj2.hashCode());
        System.out.println(pwObj3.hashCode());

        try {
            System.out.println(generatePwHash(pwObj1));
            System.out.println(generatePwHash(pwObj2));
            System.out.println(generatePwHash(pwObj3));
            System.out.println("*****************from hier will be stronger***********************");
            String a=DataAccess.generateStrongPasswordHash(pwObj1);

            System.out.println(a+" \n  Length:"+a.length());
            System.out.println(DataAccess.generateStrongPasswordHash(pwObj2));
            System.out.println(DataAccess.generateStrongPasswordHash(pwObj3));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }

    public static byte[] passWordHashing(String pww) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        //Then, we'll use the MessageDigest class to configure the SHA-512 hash function with our salt:

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(salt);

        //And with that added, we can now use the digest method to generate our hashed password:

        byte[] hashedPassword = md.digest(pww.getBytes(StandardCharsets.UTF_8));
        for (byte e : hashedPassword) {
            System.out.println(e);
        }

        return hashedPassword;
    }

    /**
     * @param data this is the password
     * @throws NoSuchAlgorithmException
     * @returnit will give back a 64 char long string of numbers that you can store in the Database
     */
    public static String generatePwHash(String data) throws NoSuchAlgorithmException {
      //  String algorithm = ALGORITHM;  //PBKDF2WithHmacSHA1  does not work in my computer why?
        MessageDigest digest = MessageDigest.getInstance("SHA-256");  //also possible SHA-384, SHA-512
        digest.reset();
        byte[] hash = digest.digest(data.getBytes());


        return bytesToHex(hash);
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }


    /**
     * it reads the information_schema and gives back the specified enum values (max 100 items)
     * if the column is not en enum and it has no enum values, the array list will be empty!
     * @param tname = table name, where is the enum to read
     * @param cname = the column, it has to be an enum
     * @return arraylist contains the enum values of specified table column.
     */
    static ArrayList<String> enumReader(String tname, String cname){
        ArrayList<String> enumValues= new ArrayList<>();
        String tnameapo= "\'"+tname+"\'";
        String cnameapo= "\'"+cname+"\'";

        System.out.println(tnameapo+"  "+cnameapo);

        try {
            String url = "jdbc:mysql://localhost:3306/information_schema" + "?useTimezone=true&serverTimezone=UTC";
            Connection connection;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, "root", "");
            connection.setAutoCommit(true);
            connection.setReadOnly(false);

            String x="\"','\"";
            System.out.println(x+"********"+x);
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

