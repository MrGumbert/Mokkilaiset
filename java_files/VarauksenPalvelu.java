public class VarauksenPalvelu {
    Palvelu palvelu;
    java.sql.Timestamp ajankohta;
    int lkm;

    public VarauksenPalvelu(Palvelu palvelu, java.sql.Timestamp ajankohta, int lkm){
        this.palvelu = palvelu;
        this.ajankohta = ajankohta;
        this.lkm = lkm;
    }

    public VarauksenPalvelu(){

    }

    public Palvelu getPalvelu(){
        return this.palvelu;
    }
    public java.sql.Timestamp getAjankohta(){
        return this.ajankohta;
    }
    public int getLkm(){
        return this.lkm;
    }

    public void setPalvelu(Palvelu palvelu){
        this.palvelu = palvelu;
    }
    public void setAjankohta(java.sql.Timestamp ajankohta){
        this.ajankohta = ajankohta;
    }
    public void setLkm(int lkm){
        this.lkm = lkm;
    }
}
