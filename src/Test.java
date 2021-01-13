import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Console;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import static java.util.Objects.hash;

public class Test {
    public static void main(String[] args) {
        System.out.print("old line");
        Scanner scanner=new Scanner(System.in);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("\r");

        String text1= scanner.next();

        System.out.println(text1);
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
            System.out.println(DataAccess.generatePwHash(pwObj1));
            System.out.println(DataAccess.generatePwHash(pwObj2));
            System.out.println(DataAccess.generatePwHash(pwObj3));
            System.out.println("*****************from hier will be stronger***********************");
            String a=DataAccess.generateStrongPasswordHash(pwObj1);

            System.out.println(a+" \n  Length:"+a.length());
            System.out.println(DataAccess.generateStrongPasswordHash(pwObj2));
            System.out.println(DataAccess.generateStrongPasswordHash(pwObj3));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }



}

