package net;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

public class Server {
	static List<Socket> clients = null;
	static BufferedReader reader = null;
	static PrintWriter writer = null;
	
	public static void main(String[] args) {
		try {
			clients = new ArrayList<Socket>();
			ServerSocket serverSocket = new ServerSocket(8888);
			System.out.println("wait for connecting...");
			//一有新进程就启动一个新的服务器线程
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("got a connection");
				
				clients.add(socket);
				ServerThread serverThread = new ServerThread(socket);
			    serverThread.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	static class ServerThread extends Thread {
		Socket socket = null;
		String message = null;
		
		public ServerThread(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    String name = reader.readLine();
				message = "欢迎" + socket.getInetAddress() + name + "进入聊天室当前聊天室有"
                        + clients.size() + "人";;    
			
				System.out.println(message);
				//向每个客户端输出新进程和人数
				for(int n = 0; n < clients.size(); n++) {
					writer = new PrintWriter(clients.get(n).getOutputStream(), true);
					writer.println(message);
			        writer.flush();
				}
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//向每个客户端输出这个客户端的发言
				while((message = reader.readLine()) != null) {
					message = name + " : " +  message;
					System.out.println(message);
					for(int n = 0; n < clients.size(); n++) {
						writer = new PrintWriter(clients.get(n).getOutputStream(), true);
				        writer.println(message);
				        writer.flush();
					}
				}
					
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
