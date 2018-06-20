package net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	static Socket socket = null;
	static BufferedReader reader = null;
	static BufferedWriter writer = null;
	
	public static void main(String[] args) throws IOException {

		System.out.println("请输入您在聊天室的昵称:");
		Scanner input = new Scanner(System.in);
		String name = input.nextLine();
		
		try {
			socket = new Socket("localhost",8888);
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		    pw.println(name);
			
			ClientThread clientThread = new ClientThread();
			clientThread.start();
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message;
			//持续监听服务器，有信息传来就显示
			while ((message = reader.readLine()) != null) {
                System.out.println(message);
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static class ClientThread extends Thread {
		
		public void run() {
			try { 
				Scanner input = new Scanner(System.in);
			    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			    //使客户端保持持续接收用户输入的状态
			    while(true) {
			    	String message = input.nextLine();
			    	//writer.write(message);
			    	//writer.flush();
			    	pw.println(message);
			    	
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
