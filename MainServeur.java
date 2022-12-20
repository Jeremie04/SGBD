package socket;

import java.io.*;
import java.net.*;

import sgbd.Request;
import sgbd.Table;

public class MainServeur {
    ServerSocket serveurSocket;

    public static void main(String[] args){
        try{
            ServerSocket ss=new ServerSocket(777);
            System.out.println("Waiting for client Request");

            while(Thread.currentThread().isInterrupted()==false){
                Socket s=ss.accept();//establishes connection 
                
                Serveur server = new Serveur(s);
                server.run();
            }
            ss.close();
            System.out.println("Shutting down the server");
        }catch(Exception e){
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }
}
