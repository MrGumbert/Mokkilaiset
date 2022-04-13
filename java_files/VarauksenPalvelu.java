public class VarauksenPalvelu {
    Palvelu palvelu;
    int lkm;

    public VarauksenPalvelu(Palvelu palvelu, int lkm){
        this.palvelu = palvelu;
        this.lkm = lkm;
    }

    public VarauksenPalvelu(){

    }

    public Palvelu getPalvelu(){
        return this.palvelu;
    }
    public int getLkm(){
        return this.lkm;
    }

    public void setPalvelu(Palvelu palvelu){
        this.palvelu = palvelu;
    }
    public void setLkm(int lkm){
        this.lkm = lkm;
    }
}
