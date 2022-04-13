public class Posti {
    String postinro;
    String toimipaikka;

    public Posti(String postinro, String toimipaikka){
        this.postinro = postinro;
        this.toimipaikka = toimipaikka;
    }
    public Posti(){

    }

    public String getPostinro(){
        return this.postinro;
    }
    public String getToimipaikka(){
        return this.toimipaikka;
    }

    public void setPostinro(String postinro){
        this.postinro = postinro;
    }
    public void setToimipaikka(String toimipaikka){
        this.toimipaikka = toimipaikka;
    }
}
