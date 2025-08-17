package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServer {

    public  static void main(String [] args){

        ExecutorService executorService= Executors.newCachedThreadPool();
        try(ServerSocket serverSocket=new ServerSocket(5000)){

            while (true) {

                    Socket socket = serverSocket.accept();
                    System.out.println("Server accepts client connection");
                    socket.setSoTimeout(900_0000);

                    executorService.execute(()->handleClientRequest(socket));

            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    private static void handleClientRequest(Socket socket){

        try(socket;  BufferedReader input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output=new PrintWriter(socket.getOutputStream(),true);
        )
        {
            while(true){
                String echo=input.readLine();
                System.out.println("server got request data "+ echo);

                if(echo.equals("exit")){
                    break;
                }
                output.println("Echo from server "+echo);
            }

        }
        catch (Exception ex){
            System.out.println("Client socket shut down");
        }

    }
}
