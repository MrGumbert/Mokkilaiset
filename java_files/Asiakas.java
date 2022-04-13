public class Asiakas {
    int asiakas_id;
    Posti posti;
    String etunimi;
    String sukunimi;
    String lahiosoite;
    String email;
    String puhelinnro;
    boolean postituslista;

    public Asiakas(int aid, Posti posti, String etunimi, String sukunimi, String lahiosoite, String email, String puhelinnro, boolean postituslista){
        this.asiakas_id = aid;
        this.posti = posti;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.lahiosoite = lahiosoite;
        this.email = email;
        this.puhelinnro = puhelinnro;
        this.postituslista = postituslista;
    }

    public Asiakas(){

    }

    public int getAsiakasId(){
        return this.asiakas_id;
    }
    public Posti getPosti(){
        return this.posti;
    }
    public String getEtunimi(){
        return this.etunimi;
    }
    public String getSukunimi(){
        return this.sukunimi;
    }
    public String getLahiOsoite(){
        return this.lahiosoite;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPuhelinnro(){
        return this.puhelinnro;
    }
    public boolean getPostituslista(){
        return this.postituslista;
    }

    public void setAsiakasId(int aid){
        this.asiakas_id = aid;
    }
    public void setPosti(Posti posti){
        this.posti = posti;
    }
    public void setEtunimi(String etunimi){
        this.etunimi = etunimi;
    }
    public void setSukunimi(String sukunimi){
        this.sukunimi = sukunimi;
    }
    public void setLahiOsoite(String lahiosoite){
        this.lahiosoite = lahiosoite;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPuhelinnro(String puh){
        this.puhelinnro = puh;
    }
    public void setPostituslista(int postituslista){
        if(postituslista == 0){
            this.postituslista = false;
        }else{
            this.postituslista = true;
        }
    }

    public String printAsiakas(){
        String pr = "Asiakkaan tiedot:";
        pr += "\nId: " + this.asiakas_id;
        pr += "\nPosti: " + this.posti;
        pr += "\nEtunimi: " + this.etunimi;
        pr += "\nSukunimi: " + this.sukunimi;
        pr += "\nLähiosoite: " + this.lahiosoite;
        pr += "\nEmail: " + this.email;
        pr += "\nPuhelinnumero: " + this.puhelinnro;
        return pr;

    }

    public boolean checkCopy(String et, String su, String po, String la, String em, String pu, boolean postilista){
        int calc = 0;
        if(this.etunimi.equals(et)){
            calc++;
        }
        if(this.sukunimi.equals(su)){
            calc++;
        }
        if(this.posti.getPostinro().equals(po)){
            calc++;
        }
        if(this.lahiosoite.equals(la)){
            calc++;
        }
        if(this.email.equals(em)){
            calc++;
        }
        if(this.puhelinnro.equals(pu)){
            calc++;
        }
        if(this.postituslista == postilista){
            calc++;
        }
        if(calc == 7){
            return true;
        }else{
            return false;
        }
    }

}
