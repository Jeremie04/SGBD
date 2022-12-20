package sgbd;

import java.util.Vector;

public class Show {
    Table table;

    public Table getTable(){
        return this.table;
    }

    public void setTable(Table value)throws Exception{
        if(value==null)
            throw new Exception("TAble null");
        this.table=value;
    }

    public Show(Table t)throws Exception{
        this.setTable(t);
    }

    public Vector<Integer> getMaximum ()throws Exception{
        Vector<Integer> list = new Vector<>();
        for(int a=0; a<getTable().getAttribut().size(); a++){
            list.add(getTable().getAttribut().get(a).length());
        }

        for(int i=0; i<getTable().getData().size(); i++){
            for(int j=0; j<getTable().getData().get(i).size(); j++){
                if(list.get(j)<getTable().getData().get(i).get(j).length()){
                    list.set(j,getTable().getData().get(i).get(j).length());
                    break;
                }
            }
        }
        return list;
    }

    public String cmp (String element, int nbfois){
        String mot="";
        for(int i=0; i<nbfois; i++){
            mot+=element;
        }
        return mot;
    }

    public String init (String mot, int max){
        int length = (max+4)-mot.length();
        mot = cmp(" ", length) + mot;
        return mot;
    }

    public Vector<String> StringVersion()throws Exception{
        Vector<String> list = new Vector<>();
        // nom table
        list.add(getTable().getNom());
        
        Vector<Integer> listMax = getMaximum();

        // column
        String colulmLigne = "";
        colulmLigne += "|";
        for(int j=0; j<getTable().getAttribut().size(); j++){
            colulmLigne += init(getTable().getAttribut().get(j), listMax.get(j));
            colulmLigne += " |";
        }
        list.add(colulmLigne);
        list.add(1,"+"+cmp("-",colulmLigne.length()-2)+"+");
        list.add(3,"+"+cmp("-",colulmLigne.length()-2)+"+");

        // data
        if(getTable().getData().size()==0||getTable().getData()==null){
            String ligne = " Empty set ";
            list.add(ligne);
            list.add("+"+cmp("-",colulmLigne.length()-2)+"+");
        }else{
            for(int i=0; i<getTable().getData().size(); i++){
                String ligne = "";
                ligne += "|";
                for(int j=0; j<getTable().getData().get(i).size(); j++){
                    try {
                        ligne += init(getTable().getData().get(i).get(j), listMax.get(j));
                    } catch (Exception e) {
                        throw new Exception(e.getMessage()+"// data at i="+i+" j="+j);
                        // TODO: handle exception
                    }
                    ligne += " |";
                }
                list.add(ligne);
            }
            list.add("+"+cmp("-",colulmLigne.length()-2)+"+");
        }
        return list;
    }
}