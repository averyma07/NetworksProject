import java.io.*;
import java.net.*;
import java.lang.Process;
import java.lang.Runtime;
 
public class OsThread extends Thread {
    private Socket socket;
 
    public OsThread(Socket socket) {
        this.socket = socket;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
	    String command = new String();
	    while(!(command=reader.readLine()).isBlank()){
		    Character com = command.charAt(0);
		    switch (com) {
			case 'd': command = "date";
				break;
			case 'u': command = "uptime | cut -d \" \" -f 3-5 | cut -d \",\" -f 1";
				break;
			case 'c': command = "uptime | cut -d \" \" -f 8-10 | cut -d \",\" -f 1";
				break;
			case 'm': command = "free -mh";
				break;
			case 'p': command = "ps -a";
				break;
			case 'n': command = "netstat";
				break;
			
		    }
		    String[] script = {
			"/bin/sh",
			"-c",
			""
		    };
		    script[2] = command;
		    System.out.println("Command run: " + command);
	    	Process process = Runtime.getRuntime().exec(script); 
	    	BufferedReader procReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String outputLine = new String();
		while((outputLine = procReader.readLine())!=null){
			writer.println(outputLine);
		}
	    }
 
            socket.close();
        } catch (IOException e) {
		e.printStackTrace();
        }
    }
}
