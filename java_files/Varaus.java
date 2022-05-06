import java.util.ArrayList;
public class Varaus {
    int varaus_id;
    Asiakas asiakas;
    Mokki mokki;
    java.sql.Timestamp varattu_pvm;
    java.sql.Timestamp vahvistus_pvm;
    java.sql.Timestamp varattu_alkupvm;
    java.sql.Timestamp varattu_loppupvm;
    ArrayList<VarauksenPalvelu> varauksenPalvelut = new ArrayList<VarauksenPalvelu>();

    public Varaus(int vid, Asiakas asiakas, Mokki mokki, java.sql.Timestamp varattu_pvm, java.sql.Timestamp vahvistus_pvm, java.sql.Timestamp varattu_alkupvm, java.sql.Timestamp varattu_loppupvm, ArrayList<VarauksenPalvelu> palvelut){
        this.varaus_id = vid;
        this.asiakas = asiakas;
        this.mokki = mokki;
        this.varattu_pvm = varattu_pvm;
        this.vahvistus_pvm = vahvistus_pvm;
        this.varattu_alkupvm = varattu_alkupvm;
        this.varattu_loppupvm = varattu_loppupvm;
        this.varauksenPalvelut = palvelut;
    }

    public Varaus(){

    }

    public int getVarausId(){
        return this.varaus_id;
    }
    public Asiakas getAsiakas(){
        return this.asiakas;
    }
    public Mokki getMokki(){
        return this.mokki;
    }
    public java.sql.Timestamp getVarattuPvm(){
        return this.varattu_pvm;
    }
    public java.sql.Timestamp getVahvistusPvm(){
        return this.vahvistus_pvm;
    }
    public java.sql.Timestamp getVarattuAlkuPvm(){
        return this.varattu_alkupvm;
    }
    public java.sql.Timestamp getVarattuLoppuPvm(){
        return this.varattu_loppupvm;
    }
    public ArrayList<VarauksenPalvelu> getVarauksenPalvelut(){
        return this.varauksenPalvelut;
    }

    public void setVarausId(int vid){
        this.varaus_id = vid;
    }
    public void setAsiakas(Asiakas asiakas){
        this.asiakas = asiakas;
    }
    public void setMokki(Mokki mokki){
        this.mokki = mokki;
    }
    public void setVarattuPvm(java.sql.Timestamp VarattuPvm){
        this.varattu_pvm = VarattuPvm;
    }
    public void setVahvistusPvm(java.sql.Timestamp VahvistusPvm){
        this.vahvistus_pvm = VahvistusPvm;
    }
    public void setVarattuAlkuPvm(java.sql.Timestamp VarattuAlkuPvm){
        this.varattu_alkupvm = VarattuAlkuPvm;
    }
    public void setVarattuLoppuPvm(java.sql.Timestamp VarattuLoppuPvm){
        this.varattu_loppupvm = VarattuLoppuPvm;
    }
    public void setVarauksenPalvelut(ArrayList<VarauksenPalvelu> palvelut){
        this.varauksenPalvelut = palvelut;
    }

    public void addVarauksenPalvelu(VarauksenPalvelu palvelu){
        this.varauksenPalvelut.add(palvelu);
    }

    public String printVaraus(){
        String pr = "Varauksen tiedot:";
        pr += "\nVaraus Id: " + this.varaus_id;
        pr += "\nAsiakas: " + this.asiakas;
        pr += "\nMökki: " + this.mokki;
        pr += "\nVarattu pvm: " + this.varattu_pvm;
        pr += "\nVahvistus pvm: " + this.vahvistus_pvm;
        pr += "\nVarattu alkupvm: " + this.varattu_alkupvm;
        pr += "\nVarattu loppupvm: " + this.varattu_loppupvm;
        return pr;

    }
}
