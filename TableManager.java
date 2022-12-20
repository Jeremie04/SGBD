package sgbd;

import java.util.*;
import object.*;
import java.io.*;

/* Pour les creations, insertion de table  */

public class TableManager{
    String database;

    public String getDatabase(){
        return this.database;
    }
    public void setDatabase(String value){
        this.database=value;
    }

    public TableManager(String dtb){
        this.setDatabase(dtb);
    }

    public Vector<String> split (String text, String spliter){
        Vector<String> retour = new Vector<>();
        String[] list = text.split(spliter);
        for(int i=0; i<list.length; i++){
            retour.add(list[i]);
        }
        return retour;
    }

    public int countLine (String path)throws IOException{
        int c=0;
        File file= new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);
        while(bf.readLine() != null){
            c++;
        }
        bf.close();
        return c;
    }

    public void writeToFile(String text, String path, String fileName)throws Exception{
        File file = new File(path+"/"+fileName);
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bf = new BufferedWriter(fw);
        fw.write(text);
        bf.newLine();
        
        bf.close();
        fw.close();
    }
    
    public Vector<String> getDataFromFile (String filePath){
        Vector<String> liste=new Vector<>();
        try{
            BufferedReader buff = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = buff.readLine()) != null) {
                liste.add(line);
            } 
            buff.close(); 
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return liste;
    }

    public Vector<Vector<String>> splitData (String data){
        Vector<Vector<String>> splitData = new Vector<>();
        String[] ligne = data.split("/");
        for(int a=1; a<ligne.length; a++){
            Vector<String> d = split(ligne[a], ",");
            splitData.add(d);
        }
        System.out.println(splitData);
        return splitData;
    }

/// Prendre les Tables dans une database
    public Vector<Table> TakeRelation ()throws Exception{
        Vector<Table> listeTable = new Vector<>();
        String directory = "databases/"+getDatabase();  // the actual directory
        File f = new File(directory);
        
        // liste ny anaran'ilay table
        String[] listFile = f.list(); 
        for( int i=0; i<listFile.length; i++){
            String tableDirectory =  directory+"/"+listFile[i]; // das ist fur die table
            //pour les colonnes
            Vector<String> col = getDataFromFile(tableDirectory+"/colonne.txt");

            //pour les data
            Vector<Vector<String>> AllData = new Vector<>();
            Vector<String> dataligne = getDataFromFile(tableDirectory+"/data.txt");
            for(int a=0; a<dataligne.size(); a++){
                AllData.add(split(dataligne.get(a),","));
            }

            Table tab = new Table(listFile[i], col, AllData);
            listeTable.add(tab);
        }
        return listeTable;
    }

/// Verifications des syntaxes
    //verifie que le nb colonne de colonne est egal aux data 
    public void DataChecking (String tableName, String data) throws Exception{
        String tableDirectory =  "databases/"+getDatabase()+"/"+tableName;
        int nbColonne = countLine(tableDirectory+"/colonne.txt");
        int nbData = data.split(",").length;
        if(nbColonne!=0){
            if(nbColonne>nbData){
                throw new Exception("Colonne manquante");
            }else if(nbColonne<nbData){
                throw new Exception("Colonne en exes nbCOlonne="+nbColonne+" nb Data="+nbData);
            }
        }else{
            throw new Exception("Colonne vide");
        }
    }

    public void CheckExistence (String tableName)throws Exception{
        File f = new File("databases/"+getDatabase());
        String[] list = f.list();
        Vector<String> table = new Vector<>();
        for(int i=0; i<list.length; i++){
            table.add(list[i]);
            System.out.println(list[i]);
        }
        if(!table.contains(tableName)){
            throw new Exception("La table "+tableName+" n'existe pas");
        }
    }

/// Creer une Table dans une database
    public void Creer (String tableName, String column)throws Exception{
        String directory = "databases/"+getDatabase()+"/"+tableName;
        File f = new File(directory);
        f.mkdir();
        String[] cols = column.split(",");
        for(int i=0; i<cols.length; i++){
            writeToFile(cols[i], directory, "colonne.txt");
        }
    }

/// Insert donnee dans une table
    public void Insert(String tableName, String data)throws Exception{
        String directory = "databases/"+getDatabase()+"/"+tableName;
        CheckExistence(tableName);
        DataChecking(tableName, data);
        writeToFile(data, directory, "data.txt");
    }

/// Delete table
    public void Delete(String tableName)throws Exception{
        String directory = "databases/"+getDatabase()+"/"+tableName+"/data.txt";
        File f = new File(directory);
        CheckExistence(tableName);
        f.delete();
    }
/// Drop table
    public void Drop(String tableName)throws Exception{
        String directory = "databases/"+getDatabase()+"/"+tableName;
        File f = new File(directory);
        CheckExistence(tableName);
        f.delete();
    }
}