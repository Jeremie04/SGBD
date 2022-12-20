package socket;

import java.net.Socket;
import java.util.*;
import java.net.*;
import java.io.*;

import sgbd.DatabaseManager;
import sgbd.Table;
import sgbd.*;

public class Serveur {
/// Attributs
    Socket socket;
    String database=null;
    DataInputStream dis;
    ObjectOutputStream oos;
    DataOutputStream dos;
    

/// Setters & Getters
    public void setSocket(Socket value)throws Exception{
        if(value==null) throw new Exception("Socket null");
        this.socket=value;
    }

    public Socket getSocket(){
        return this.socket;
    }

    public void setDatabase(String value)throws Exception{
        DatabaseManager ddbManager = new DatabaseManager();
        if(ddbManager.DatabaseExist(value)){
            this.database=value;
        }else{
            throw new Exception("Database doesn't exist");
        }
    }

    public String getDatabase(){
        return this.database;
    }

/// Constructor
    public Serveur(Socket sk)throws Exception{
        this.setSocket(sk);
    }


/// Functions 
    public void SendResponse (ObjectOutputStream oos, String str)throws Exception{
        if(str.startsWith("table")||str.startsWith("data")){
            System.out.println("Table ou database");
            DataRequest d = new DataRequest(this.getDatabase(),str );
            Object result = d.ExcecuteDataRequest();
            oos.writeObject(result);
        }else{
            System.out.println("requete");
            Request r = new Request(str, this.getDatabase());
            Table result = r.ExcecuteRequest();
            oos.writeObject(result);
        }
    }

    public void ChangeDatabase (String str, ObjectOutputStream oos)throws Exception{
        String[] req = str.split("use ");
        this.setDatabase(req[1]);
        Object msg = "Database changed";
        oos.writeObject(msg);
        oos.flush();
    }


    public void run()throws Exception{
        dis=new DataInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        dos = new DataOutputStream(socket.getOutputStream());

        String	str="";
        while(true){
            System.out.println("Enter message");
            str=(String)dis.readUTF();
            if(str.equalsIgnoreCase("exit")){
                oos.close();
                dis.close();
                socket.close();
                break;
            }
            System.out.println("message= "+str);
            try{
                if(str.startsWith("use")){
                    ChangeDatabase(str, oos);
                }else if(this.getDatabase()!=null){
                    SendResponse(oos, str);
                }else{
                    throw new Exception("Choose a database");   
                }
            }catch(Exception e){
                oos.writeObject(e);    
            }
            oos.flush();
        }
    }
}
