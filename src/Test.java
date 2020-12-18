import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static java.util.Objects.hash;

public class Test {
    public static void main(String[] args) {

        System.out.println(hash("test123"));

        String pwObj1="testthehash123";
        String pwObj2="test123";
        String pwObj3="test123";

        System.out.println(pwObj1.hashCode());
        System.out.println(pwObj2.hashCode());
        System.out.println(pwObj3.hashCode());

        try {
            System.out.println(DataAccess.generatePwHash(pwObj1));
            System.out.println(DataAccess.generatePwHash(pwObj2));
            System.out.println(DataAccess.generatePwHash(pwObj3));
            String a=DataAccess.generateStrongPasswordHash(pwObj1);

            System.out.println(a+" \n  Length:"+a.length());
            System.out.println(DataAccess.generateStrongPasswordHash(pwObj2));
            System.out.println(DataAccess.generateStrongPasswordHash(pwObj3));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }
}
