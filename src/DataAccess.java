import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;


public class DataAccess {
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/hotel" + "?useTimezone=true&serverTimezone=UTC";
    private static String user = "root";
    private static String password = "";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";


    public DataAccess() throws SQLException, ClassNotFoundException {
        java.lang.Class.forName("com.mysql.cj.jdbc.Driver");

        connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(true);
        connection.setReadOnly(false);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
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
        String algorithm = ALGORITHM;  //PBKDF2WithHmacSHA1  does not work in my computer why?
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


    //****another hash algorithm************it creates 166 char long hashcode***********************************************************

    public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = new byte[0];
        try {
            hash = skf.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

}
