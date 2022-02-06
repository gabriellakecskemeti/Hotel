import java.io.IOException;

public class TryStream {

    public static void main(String[] args) {

        byte buffer[] = new byte[80];
        String str = "";
        do {
            System.out.print("Eingabe>");
            System.out.flush();
            try{
                int read = System.in.read(buffer, 0, 80);

                str = new String(buffer, 0, read);

                System.out.write(buffer, 0, read);

                System.out.flush();

            } catch (IOException e){
                e.printStackTrace();
            }
        } while (!str.equals("quit\n"));
    }

}
