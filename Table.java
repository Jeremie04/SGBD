package sgbd;

import java.io.FileOutputStream;
import java.util.Vector;
import java.io.*;

public class Table implements Serializable{
    String nom;
    Vector<String> attribut;
    Vector<Vector<String>> data;

    public String getNom(){
        return this.nom;
    }

    public Vector<String> getAttribut(){
        return this.attribut;
    }

    public int getIdColumn(String column)throws Exception{
        if(this.doesExist(column)){
            return this.getAttribut().indexOf(column);
        }else{
            throw new Exception (this.getNom()+" does not contain column "+column);
        }
    }
    
    public Vector<Vector<String>> getData(){
        return this.data;
    }

    public Table getRelation(){
        return this.relation;
    }

    public void setNom(String n){
        this.nom=n;
    }
    public void setAttribut(Vector<String> lc){
        this.attribut=lc;
    }
    public void setData(Vector<Vector<String>> d){
        this.data=d;
    }
    public void setRelation(Table r){
        this.relation=r;
    }
    
    /*Constructeur */
    public Table(){}
    public Table(String n, Vector<String> Columns, Vector<Vector<String>> data){
        this.setNom(n);
        this.setAttribut(Columns);
        this.setData(data);
    }

    /* Operation pour les colonnes */
    public boolean checkColumn (Table r){
        if(this.getAttribut().size()==r.getAttribut().size()){
            for(int i=0; i<this.getAttribut().size(); i++){
                if(r.getAttribut().contains(this.getAttribut().get(i))==false){
                    break;
                }
                return true;   
            }
        }
        return false;
    }

    public Vector<String> differenceColonne (Vector<String> col1, Vector<String> col2){
        Vector<String> liste = new Vector<>();
        for(int i=0; i<col1.size(); i++){
            if(col2.contains(col1.get(i))==false){
                liste.add(col1.get(i));
            }
        }
        return liste;
    }
    
    Boolean doesExist(String colonne){
        if(this.getAttribut().contains(colonne)==false){
            return false;
        }return true;
    }

    public void afficher(Vector<String> liste){
        for(int i=0; i<liste.size(); i++){
            System.out.print(liste.get(i)+"  ");
        }
    }

    // Combine two Vector<String> into one
    public Vector<String> combine (Vector<String> v1,Vector<String>v2){
        Vector<String> sum= new Vector<>();
        for(int i=0; i<v1.size(); i++){
            sum.add(v1.get(i));
        }
        for(int i=0; i<v2.size(); i++){
            if(sum.contains(v2.get(i))==false){
                sum.add(v2.get(i));
            }
        }
        return sum;
    }

    public Table Join (Table r, String colonne)throws Exception{
        Vector<Vector<String>> newData= new Vector<>();
        int idCol1= this.getIdColumn(colonne);
        int idCol2= r.getIdColumn(colonne);

        for(int i=0; i<this.getData().size(); i++){
            for(int j=0; j<r.getData().size(); j++){
                if(this.getData().get(i).get(idCol1).equalsIgnoreCase(r.getData().get(j).get(idCol2))){
                    newData.add(this.combine(getData().get(i), r.getData().get(j)));
                }
            }
        }
        return new Table("Join result", this.combine(this.getAttribut(), r.getAttribut()), newData);
    }

    public Table Projection (Vector<String> attribut)throws Exception{
        // Cree les nouvelles data du resultat
        Vector<Vector<String>> newData= new Vector<>();
        Vector<String> newColumn= new Vector<>();
        // Pour les colonnes a projeter
        Vector<Integer> idCol = new Vector<>();
        for(int i=0; i<attribut.size(); i++){
            newColumn.add(attribut.get(i));
            idCol.add(this.getIdColumn(attribut.get(i)));
        }
        for(int i=0; i<this.getData().size(); i++){
            Vector<String> liste = new Vector<>();
            // insérer pour chaque colonne
            for(int c=0; c<idCol.size(); c++ ){
                liste.add(this.getData().get(i).get(idCol.get(c)));
            }
            // insere dans la nouvel data s'il n'y en a pas encore
            if(newData.contains(liste)==false){
                newData.add(liste);
            }
        }
        return new Table("Projection result", newColumn, newData);
    }

    
    public Table Union (Table r)throws Exception{
        Vector<Vector<String>> newData= new Vector<>();
        if(this.checkColumn(r)){
            for(int i=0; i<this.getData().size(); i++){
                newData.add(this.getData().get(i));
            }
            for(int i=0; i<r.getData().size(); i++){
                if(newData.contains(r.getData().get(i))==false){
                    newData.add(r.getData().get(i));
                }
            }
            return new Table("Union result", this.getAttribut(), newData);
        }
        throw new Exception("the columns are not equal");
    }

    public Table Intersection (Table r)throws Exception{
        Vector<Vector<String>> newData= new Vector<>();

        for(int i=0; i<this.getData().size(); i++){
            if(r.getData().contains(this.getData().get(i))==true){
                newData.add(this.getData().get(i));
            }
        }

        return new Table("Intersection result", this.getAttribut(), newData);
    }

    public Table Difference (Table r)throws Exception{
        Vector<Vector<String>> newData= new Vector<>();

        for(int i=0; i<this.getData().size(); i++){
            if(r.getData().contains(this.getData().get(i))!=true){
                newData.add(this.getData().get(i));
            }
        }
    
        return new Table("Difference result", this.getAttribut(), newData);
    } 

    public Table ProduitQuartesien (Table r)throws Exception{
        Vector<Vector<String>> newData= new Vector<>();
        for (int i=0; i<this.getData().size(); i++){
            for (int j=0; j<r.getData().size(); j++){
                newData.add(this.combine(getData().get(i), r.getData().get(j)));
            }
        }
        return new Table("Produit result", this.combine(this.getAttribut(), r.getAttribut()), newData);
    }

    // Division d'un ensemble this(X,Y) et r(Y)
    public Table Division (Table r)throws Exception{
        Vector<String> attributX = this.differenceColonne(this.getAttribut(), r.getAttribut());
        afficher(attributX);
        Table r1 = this.Projection(attributX);
        Table r2 = r1.ProduitQuartesien(r);
        Table r3 = r2.Difference(this);
        Table r4 = r3.Projection(attributX);
        Table result = r1.Difference(r4);
        result.display();
        return result;
    }

}