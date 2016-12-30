package com.niki.assignment.client;

import java.io.*;
import java.net.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RestClient {
	public static void main(String[] args)
	{
		try {
			Socket sock, sock2;
			URL url = new URL("http://localhost:9080/niki-assignment-mms/rest/connection/create");
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			String input;
			OutputStream os;
			BufferedReader br;
			String output;
			String jsonOutput="";
			int port;
			JSONObject jsonObject;
			JSONParser parser = new JSONParser();

			input = "{\"source\":\"9967796319\"}";

			os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

			br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				jsonOutput = output;
				System.out.println(output);
			}
			
			conn.disconnect();
			jsonObject = (JSONObject) parser.parse(jsonOutput);
			port = ((Long)jsonObject.get("port")).intValue();
			sock = new Socket("localhost",port);
			
			
			
			//Repeat
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			input = "{\"source\":\"9967796320\"}";

			os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				sock.close();
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

			br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				jsonOutput = output;
				System.out.println(output);
			}

			conn.disconnect();
			jsonObject = (JSONObject) parser.parse(jsonOutput);
			port = ((Long)jsonObject.get("port")).intValue();
			sock2 = new Socket("localhost",port);
			
			SendThread sendThread = new SendThread(sock);
			Thread thread = new Thread(sendThread);thread.start();
			
			RecieveThread recieveThread = new RecieveThread(sock);
			Thread thread3 =new Thread(recieveThread);thread3.start();
			
			SendThread sendThread2 = new SendThread(sock2);
			Thread thread2 = new Thread(sendThread2);thread2.start();
			
			RecieveThread recieveThread2 = new RecieveThread(sock2);
			Thread thread4 =new Thread(recieveThread2);thread4.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} 
	}
}

class RecieveThread implements Runnable
{
	Socket sock=null;
	BufferedReader recieve=null;
	
	public RecieveThread(Socket sock) {
		this.sock = sock;
	}//end constructor
	public void run() {
		try{
		recieve = new BufferedReader(new InputStreamReader(sock.getInputStream()));//get inputstream
		String msgRecieved = null;
		if(sock.isConnected()){
			System.out.println("Client connected for recieve to "+sock.getInetAddress() + " on port "+sock.getPort());
		}
		while((msgRecieved = recieve.readLine())!= null)
		{
			System.out.println("From Server: " + msgRecieved);
		}
		}catch(Exception e){System.out.println(e.getMessage());}
	}//end run
}//end class recievethread

class SendThread implements Runnable
{
	Socket sock=null;
	PrintWriter print=null;
	BufferedReader brinput=null;
	BufferedReader recieve=null;
	
	public SendThread(Socket sock)
	{
		this.sock = sock;
	}
	public void run(){
		try{
			/*recieve = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));//get inputstream
			String msgRecieved = null;
			while((msgRecieved = recieve.readLine())!= null)
			{
				System.out.println("From Server: " + msgRecieved);
			}*/
			if(sock.isConnected())
			{
				System.out.println("Client connected to "+sock.getInetAddress() + " on port "+sock.getPort());
				this.print = new PrintWriter(sock.getOutputStream(), true);
				String destination=null, message=null;
				/*while(true){
					System.out.println("Type your message to send to server..type 'EXIT' to exit");
					brinput = new BufferedReader(new InputStreamReader(System.in));
					destination = brinput.readLine();
					if(destination.equals("EXIT"))
						break;
					this.print.println(destination);
					message = brinput.readLine();
					this.print.println(message);
					this.print.flush();
				}//end while
				*/
				this.print.println("9967796319");
				this.print.println("message1");
				this.print.println("9967796319");
				this.print.println("message2");
				this.print.println("9967796320");
				this.print.println("message3");
				this.print.println("9967796320");
				this.print.println("message4");
				this.print.println("EXIT");
				this.print.flush();
				/*this.print.println("EXIT");*/
				/*while(true){
					brinput = new BufferedReader(new InputStreamReader(System.in));
					destination = brinput.readLine();
					if(destination.equals("EXIT"))
						break;
				}*/
				/*sock.close();*/
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}//end run method
}//end class