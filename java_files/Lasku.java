public class Lasku {
    int lasku_id;
    Varaus varaus;
    double summa;
    double alv;
    boolean maksettu;
    java.sql.Date erapaiva;
    java.sql.Date laskupaiva;
    String laskutyyppi;

    public Lasku(int lid, Varaus varaus, double summa, double alv, boolean maksettu, java.sql.Date erapaiva, java.sql.Date laskupaiva, String laskutyyppi){
        this.lasku_id = lid;
        this.varaus = varaus;
        this.summa = summa;
        this.alv = alv;
        this.maksettu = maksettu;
        this.erapaiva = erapaiva;
        this.laskupaiva = laskupaiva;
        this.laskutyyppi = laskutyyppi;
    }

    public Lasku(){

    }

    public int getLaskuId(){
        return this.lasku_id;
    }
    public Varaus getVaraus(){
        return this.varaus;
    }
    public double getSumma(){
        return this.summa;
    }
    public double getAlv(){
        return this.alv;
    }
    public boolean getMaksettu(){
        return this.maksettu;
    }
    public java.sql.Date getErapaiva(){
        return this.erapaiva;
    }
    public java.sql.Date getLaskupaiva(){
        return this.laskupaiva;
    }
    public String getLaskutyyppi(){
        return this.laskutyyppi;
    }

    public void setLaskuId(int lid){
        this.lasku_id = lid;
    }
    public void setVaraus(Varaus varaus){
        this.varaus = varaus;
    }
    public void setSumma(double summa){
        this.summa = summa;
    }
    public void setAlv(double alv){
        this.alv = alv;
    }
    public void setMaksettu(int maksettu){
        if(maksettu == 0){
            this.maksettu = false;
        }else{
            this.maksettu = true;
        }
    }
    public void setErapaiva(java.sql.Date erapaiva){
        this.erapaiva = erapaiva;
    }
    public void setLaskupaiva(java.sql.Date laskupaiva){
        this.laskupaiva = laskupaiva;
    }
    public void setLaskutyyppi(String laskutyyppi){
        this.laskutyyppi = laskutyyppi;
    }

    public String printLasku(){
        String pr = "Laskun tiedot:";
        pr += "\nLasku Id: " + this.lasku_id;
        pr += "\nVaraus: " + this.varaus;
        pr += "\nSumma: " + this.summa;
        pr += "\nAlv: " + this.alv;
        pr += "\nMaksettu: " + this.maksettu;
        pr += "\nErapäivä: " + this.erapaiva;
        pr += "\nLaskupäivä: " + this.laskupaiva;
        pr += "\nLaskutyyppi: " + this.laskutyyppi;
        return pr;

    }

}
