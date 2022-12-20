package sgbd;

import java.util.*;
import object.*;
import java.io.*;

public class Request{
/// Attributs
    Vector<Table> relation; 
    String request;
    String database;

/// Setters & Getters
    public void setRelation (Vector<Table> r)throws Exception{
        if(r!=null){
            this.relation=r;
        }else{
            throw new Exception("Relation invalid");
        }
    }

    public Vector<Table> getRelation()throws Exception{
        if(this.relation==null){
            TableManager tm = new TableManager(this.getDatabase());
            this.setRelation(tm.TakeRelation());
        }
        return this.relation;
    }

    public Table getRelation(String nom)throws Exception{
        for(int i=0; i<this.getRelation().size(); i++){
            if(this.getRelation().get(i).getNom().equalsIgnoreCase(nom)){
                return this.getRelation().get(i);
            }
        }
        throw new Exception(nom+" not found");
    }

    public String getDatabase(){
        return this.database;
    }
    public void setDatabase(String value){
        this.database=value;
    }

/// Constructor
    public Request(String dtb){this.database=dtb;}
    public Request(String req, String dtb){
        this.database=dtb;
        this.request = req;
    }

/// Fonctions
    public Vector<Table> getRelationfromRequest( String action)throws Exception{
        Vector<Table> rel = new Vector<>();
        String[] element = request.split(action+" ");
        String[] tableName = element[1].split(" with ");
        rel.add (this.getRelation(tableName[0]));
        rel.add (this.getRelation(tableName[1]));
        return rel;
    }


    public Table selectProcedure (String requete)throws Exception{
        /* select Nom,Gender from <select * from personne> */
        if(requete.contains("<")){
            int firstBrak = requete.lastIndexOf("<");
            int lastBrak = requete.indexOf(">");
            String req = requete.substring(firstBrak+1, lastBrak);

            return selectProcedure(req);
        }else{
            /* SELECT col1,col2,... FROM table */
            String[] req = requete.split("select "); 
            String[] element = req[1].split(" from ");
            String columns = element[0];
            System.out.println("column"+columns);
            String t = element[1];
            Table table = getRelation(t);
            if(columns.equals("*")){
                return table;
            }else{
                String[] col = columns.split(",");
                Vector<String> cols = new Vector<>();
                for(int i=0; i<col.length; i++){
                    cols.add(col[i]);
                }
                
                return table.Projection(cols); 
            }
        }
    }



    public Table ExcecuteRequest ()throws Exception{
        String FirstElement = request.split(" ")[0];
        switch (FirstElement) {
            case "select":
                return this.selectProcedure(request);
            
        
            case "substract":
                /* SUBSTRACT table1 with table2 */
                Vector<Table> rel = getRelationfromRequest("substract");
                return rel.get(0).Difference(rel.get(1));
            

            case "divide":
                /*Divide tab1 with tab2 */
                Vector<Table> tab = getRelationfromRequest("divide");
                return tab.get(0).Division(tab.get(1));
            

            case "unify":
                /*Unify table with table */
                Vector<Table> table = getRelationfromRequest("unify");
                return table.get(0).Union(table.get(1));        
            

            case "join":
                /*Join table1 with table2 on colonne */
                String[] element = request.split("join ");
                String[] table_colonne = element[1].split(" on ");
                String[] tables = table_colonne[0].split(" with ");
                Table r1 = this.getRelation(tables[0]);
                Table r2 = this.getRelation(tables[1]);
                return r1.Join(r2, table_colonne[1]);
            

            case "intersect":
                /*intersect table1 with table2*/
                Vector<Table> relation = getRelationfromRequest("intersect");
                return relation.get(0).Intersection(relation.get(1));
            
        }
        throw new Exception("Syntax Error !");
    }

}