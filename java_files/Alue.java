import java.util.ArrayList;
public class Alue {
    int alue_id;
    String nimi;
    ArrayList<Palvelu> palvelut = new ArrayList<Palvelu>();

    public Alue(int alue_id, String nimi, ArrayList<Palvelu> palvelut){
        this.alue_id = alue_id;
        this.nimi = nimi;
        this.palvelut = palvelut;
    }
    public Alue(){

    }

    public int getAlueId(){
        return this.alue_id;
    }
    public String getNimi(){
        return this.nimi;
    }
    public ArrayList<Palvelu> getPalvelut(){
        return this.palvelut;
    }

    public void setAlueId(int alue_id){
        this.alue_id = alue_id;
    }
    public void setNimi(String nimi){
        this.nimi = nimi;
    }
    public void setPalvelut(ArrayList<Palvelu> palvelut){
        this.palvelut = palvelut;
    }
    public void addPalvelu(Palvelu palvelu){
        this.palvelut.add(palvelu);
    }
}
