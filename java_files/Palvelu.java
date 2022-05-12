public class Palvelu {
    int palvelu_id;
    Alue alue;
    String nimi;
    String tyyppi;
    String kuvaus;
    double hinta;
    double alv;
    int osto_lkm;

    public Palvelu(int pid, Alue alue, String nimi, String tyyppi, String kuvaus, double hinta, double alv){
        this.palvelu_id = pid;
        this.alue = alue;
        this.nimi = nimi;
        this.tyyppi = tyyppi;
        this.kuvaus = kuvaus;
        this.hinta = hinta;
        this.alv = alv;
        this. osto_lkm = 0;
    }

    public Palvelu(){

    }

    public int getPalveluId(){
        return this.palvelu_id;
    }
    public Alue getAlue(){
        return this.alue;
    }
    public String getNimi(){
        return this.nimi;
    }
    public String getTyyppi(){
        return this.tyyppi;
    }
    public String getKuvaus(){
        return this.kuvaus;
    }
    public double getHinta(){
        return this.hinta;
    }
    public double getAlv(){
        return this.alv;
    }
    
    public void setPalveluId(int pid){
        this.palvelu_id = pid;
    }
    public void setAlue(Alue alue){
        this.alue = alue;
    }
    public void setNimi(String nimi){
        this.nimi = nimi;
    }
    public void setTyyppi(String tyyppi){
        this.tyyppi = tyyppi;
    }
    public void setKuvaus(String kuvaus){
        this.kuvaus = kuvaus;
    }
    public void setHinta(double hinta){
        this.hinta = hinta;
    }
    public void setAlv(double alv){
        this.alv = alv;
    }
    

    public String printPalvelu(){
        String pr = "Palvelun tiedot:";
        pr += "\nPalvelu Id: " + this.palvelu_id;
        pr += "\nAlue: " + this.alue;
        pr += "\nNimi: " + this.nimi;
        pr += "\nTyyppi: " + this.tyyppi;
        pr += "\nKuvaus: " + this.kuvaus;
        pr += "\nHinta: " + this.hinta;
        pr += "\nAlv: " + this.alv;
        return pr;

    }

    public void lisaaOsto(){
        this.osto_lkm += 1;
    }

    public void nollaaOstot(){
        this.osto_lkm = 0;
    }

    public int getOsto_lkm(){
        return this.osto_lkm;
    }
}