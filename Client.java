import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter server address:");
            String address = sc.nextLine();

            // Replace "address" with the actual IP if hardcoding:
            // Socket s = new Socket("192.168.1.10", 5000);
            Socket s = new Socket(192.168.1.11, 5000); // Use user input for IP address

            try (DataInputStream din = new DataInputStream(s.getInputStream());
                 DataOutputStream dout = new DataOutputStream(s.getOutputStream())) {

                System.out.println("Enter 'get' to request a file:");
                String command = sc.nextLine();
                dout.writeUTF(command); // Send the command to the server
                dout.flush();

                if (command.equalsIgnoreCase("get")) {
                    String filename = din.readUTF();
                    System.out.println("Receiving file: " + filename);

                    long fileSize = Long.parseLong(din.readUTF());
                    System.out.println("File size: " + fileSize / 1024 + " KB");

                    try (FileOutputStream fos = new FileOutputStream("client_" + filename)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = din.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }

                    System.out.println("File received and saved successfully.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
