import java.io.*;
import java.net.*;
 
public class Server {
 
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
	System.out.println("Server running on port: " + port);
 
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            while (true) {
                Socket socket = serverSocket.accept();
 
                new OsThread(socket).start();
            }
 
        } catch (IOException e) {
		e.printStackTrace();
        }
    }
}
