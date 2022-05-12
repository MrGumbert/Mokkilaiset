public class Mokki {
    int mokki_id;
    Alue alue;
    Posti posti;
    String mokkinimi;
    String katuosoite;
    double hinta;
    String kuvaus;
    int henkilomaara;
    String varustelu;

    /**
    * Raportti näkymää varten
    * Tätä ei tallenneta tietokantaan
    */
    int varausten_lkm;
    

    public Mokki(int mid, Alue alue, Posti posti, String mokkinimi, String katuosoite, double hinta, String kuvaus, int henkilomaara, String varustelu){
        this.mokki_id = mid;
        this.alue = alue;
        this.posti = posti;
        this.mokkinimi = mokkinimi;
        this.katuosoite = katuosoite;
        this.hinta = hinta;
        this.kuvaus = kuvaus;
        this.henkilomaara = henkilomaara;
        this.varustelu = varustelu;
        this.varausten_lkm = 0;
    }

    public Mokki(){

    }

    public int getMokkiId(){
        return this.mokki_id;
    }
    public Alue getAlue(){
        return this.alue;
    }
    public Posti getPosti(){
        return this.posti;
    }
    public String getMokkinimi(){
        return this.mokkinimi;
    }
    public String getKatuosoite(){
        return this.katuosoite;
    }
    public double getHinta(){
        return this.hinta;
    }
    public String getKuvaus(){
        return this.kuvaus;
    }
    public int getHenkilomaara(){
        return this.henkilomaara;
    }
    public String getVarustelu(){
        return this.varustelu;
    }

    public void setMokkiId(int mid){
        this.mokki_id = mid;
    }
    public void setAlue(Alue alue){
        this.alue = alue;
    }
    public void setPosti(Posti posti){
        this.posti = posti;
    }
    public void setMokkinimi(String mokkinimi){
        this.mokkinimi = mokkinimi;
    }
    public void setKatuosoite(String katuosoite){
        this.katuosoite = katuosoite;
    }
    public void setHinta(double hinta){
        this.hinta = hinta;
    }
    public void setKuvaus(String kuvaus){
        this.kuvaus = kuvaus;
    }
    public void setHenkilomaara(int henkilomaara){
        this.henkilomaara = henkilomaara;
    }
    public void setVarustelu(String varustelu){
        this.varustelu = varustelu;
    }

    public String printAsiakas(){
        String pr = "Mökin tiedot:";
        pr += "\nMokki Id: " + this.mokki_id;
        pr += "\nAlue: " + this.alue;
        pr += "\nPosti: " + this.posti;
        pr += "\nMökkinimi: " + this.mokkinimi;
        pr += "\nKatuosoite: " + this.katuosoite;
        pr += "\nKuvaus: " + this.kuvaus;
        pr += "\nHenkilömäärä: " + this.henkilomaara;
        pr += "\nVarustelu: " + this.varustelu;
        return pr;

    }

    public boolean checkCopy(String mokkiMuokattuAlue, String mokkiMuokattuPostinro, String mokkiMuokattuNimi, String mokkiMuokattuOsosite){
        int calc = 0;
        if(this.alue.getNimi().equals(mokkiMuokattuAlue)){
            calc += 1;
        }
        if(this.posti.getPostinro().equals(mokkiMuokattuPostinro)){
            calc += 1;
        }
        if(this.mokkinimi.equals(mokkiMuokattuNimi)){
            calc += 1;
        }
        if(this.katuosoite.equals(mokkiMuokattuOsosite)){
            calc += 1;
        }
        if(calc == 4){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Raportti näkymää varten
     */
    public void lisaa_varaus(){
        this.varausten_lkm += 1;
    }

    public int getVarausten_lkm(){
        return this.varausten_lkm;
    }

    public void nollaaVaraukset(){
        this.varausten_lkm = 0;
    }
}