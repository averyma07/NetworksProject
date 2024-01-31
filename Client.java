import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.time.*;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException{
		Scanner input = new Scanner(System.in);
		String ipAddress = args[0];
		int port = Integer.parseInt(args[1]);
		String command;
		int numQueries;
		double totalTurnAround;
		Socket clientSocket = new Socket(ipAddress, port);
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		while(true) {
			System.out.println("Choose a command, or press Q to quit.");
			System.out.println("\tD - Date and Time");
			System.out.println("\tU - Uptime");
			System.out.println("\tC - Current Users");
			System.out.println("\tM - Memory Use");
			System.out.println("\tP - Running Processes");
			System.out.println("\tN - Netstat");
			command = input.next();
			if(command.equalsIgnoreCase("Q")) {
				input.close();
				in.close();
				out.close();
				clientSocket.close();
				System.exit(0);
			}
			command = command.toLowerCase();
			System.out.println("How many queries would you like to send? Please choose 1, 5, 10, 15, 20, 25, or 100.");
			numQueries = input.nextInt();
			totalTurnAround = 0;
			for(int i = 0; i < numQueries; i++) {
				CommandThread newThread = new CommandThread(command, out, in);
				Instant startTime = Instant.now();
				newThread.start();
				Instant stopTime = Instant.now();
				double duration = Duration.between(startTime, stopTime).getNano() / 1000;
				Thread.sleep(20);
				System.out.println("***Turn-Around Time: " + duration + " microseconds***");
				totalTurnAround += duration;
			}
			Thread.sleep(20);
			System.out.println("============================================");
			System.out.println("Total Turn-Around Time: " + totalTurnAround + " microseconds");
			System.out.println("Average Turn-Around Time: " + totalTurnAround / numQueries + " microseconds");
			System.out.println("============================================");
		}
	}

}

class CommandThread extends Thread{
	private String command;
	private PrintWriter out;
	private BufferedReader in;
	
	public CommandThread(String command, PrintWriter out, BufferedReader in) {
		this.command = command;
		this.in = in;
		this.out = out;
	}
	
	public void run() {
		String commandResults;
		out.println(command);
		try {
			while((commandResults = in.readLine()) != null) {
				System.out.println(commandResults);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}