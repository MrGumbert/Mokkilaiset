public class VarauksetPalvelut {
    int varaus_id;
    int palvelu_id;
    java.sql.Timestamp ajankohta;
    int lkm;

    public VarauksetPalvelut(int vid, int pid, java.sql.Timestamp ajankohta, int lkm){
        this.varaus_id = vid;
        this.palvelu_id = pid;
        this.ajankohta = ajankohta;
        this.lkm = lkm;
    }
    public VarauksetPalvelut(){

    }

    public int getVarausId(){
        return this.varaus_id;
    }
    public int getPalveluId(){
        return this.palvelu_id;
    }
    public java.sql.Timestamp getAjankohta(){
        return this.ajankohta;
    }
    public int getLkm(){
        return this.lkm;
    }

    public void setVarausId(int vid){
        this.varaus_id = vid;
    }
    public void setPalveluId(int pid){
        this.palvelu_id = pid;
    }
    public void setAjankohta(java.sql.Timestamp ajankohta){
        this.ajankohta = ajankohta;
    }
    public void setLkm(int lkm){
        this.lkm = lkm;
    }
}
