package socket;

import java.io.*;
import java.net.*;
import java.util.*;

import sgbd.Table;
import socket.Client;


public class MainClient {
    public static void main(String[] args){
        try{
            Client client = new Client();
            client.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }
}
