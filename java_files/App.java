import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application{
    String HOST_USER = "root";
    String HOST_PSWD = "";
    ArrayList<Posti> postit = getPostit();
    ArrayList<Alue> alueet = getAlueet();
    ArrayList<Asiakas> asiakkaat = getAsiakkaat();
    ArrayList<Mokki> mokit = getMokit();
    ArrayList<Varaus> varaukset = getVaraukset();
    ArrayList<Lasku> laskut = getLaskut();
    ArrayList<Palvelu> palvelut = getPalvelut();
    ArrayList<VarauksetPalvelut> varauksetPalvelut = getVarauksetPalvelut();
    String currPage = "Etusivu";
    String initPostiTarkistus = checkPosti();

    public void setPostit(ArrayList<Posti> postit){
        this.postit = postit;
    }
    public void setAlueet(ArrayList<Alue> alueet){
        this.alueet = alueet;
    }
    public void setAsiakkaat(ArrayList<Asiakas> asiakkaat){
        this.asiakkaat = asiakkaat;
    }
    public void setMokit(ArrayList<Mokki> mokit){
        this.mokit = mokit;
    }
    public void setVaraukset(ArrayList<Varaus> varaukset){
        this.varaukset = varaukset;
    }
    public void setLaskut(ArrayList<Lasku> laskut){
        this.laskut = laskut;
    }
    public void setPalvelut(ArrayList<Palvelu> palvelut){
        this.palvelut = palvelut;
    }
    public void setVarauksetPalvelut(ArrayList<VarauksetPalvelut> varauksetPalvelut){
        this.varauksetPalvelut = varauksetPalvelut;
    }

    public void addAlue(Alue alue){
        this.alueet.add(alue);
    }
    public void addPosti(Posti posti){
        this.postit.add(posti);
    }
    public void addMokki(Mokki mokki){
        this.mokit.add(mokki);
    }
    public void addVaraus(Varaus varaus){
        this.varaukset.add(varaus);
    }
    public void addLasku(Lasku lasku){
        this.laskut.add(lasku);
    }
    public void addPalvelu(Palvelu palvelu){
        this.palvelut.add(palvelu);
    }
    public void addVarauksetPalvelut(VarauksetPalvelut vp){
        this.varauksetPalvelut.add(vp);
    }


    public ArrayList<Posti> getPostit(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM posti");
            ArrayList<Posti> temp = new ArrayList<Posti>();
            while(rs.next()){
                Posti posti = new Posti();
                posti.setPostinro(rs.getString(1));
                posti.setToimipaikka(rs.getString(2));
                temp.add(posti);
            }
            con.close();
            return temp;
        }catch(Exception e){
            System.out.println(e);
            return new ArrayList<Posti>();
        }
    }
    public ArrayList<Alue> getAlueet(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM alue");
            ArrayList<Alue> temp = new ArrayList<Alue>();
            while(rs.next()){
                Alue alue = new Alue();
                alue.setAlueId(rs.getInt(1));
                alue.setNimi(rs.getString(2));
                temp.add(alue);
            }
            con.close();
            return temp; 
        }catch(Exception e){
            System.out.println(e);
            return new ArrayList<Alue>();
        }
        
    }

    public ArrayList<Asiakas> getAsiakkaat(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM asiakas");
            ArrayList<Asiakas> temp = new ArrayList<Asiakas>();
            while(rs.next()){
                boolean postiError = true;
                Asiakas asiakas = new Asiakas();
                asiakas.setAsiakasId(rs.getInt(1));
                for(Posti p : postit){
                    if(p.getPostinro().equals(rs.getString(2))){
                        asiakas.setPosti(p);
                        postiError = false;
                        break;
                    }
                }
                if(postiError){
                    asiakas.setPosti(new Posti("00000", "Rikki"));
                }
                asiakas.setEtunimi(rs.getString(3));
                asiakas.setSukunimi(rs.getString(4));
                asiakas.setLahiOsoite(rs.getString(5));
                asiakas.setEmail(rs.getString(6));
                asiakas.setPuhelinnro(rs.getString(7));
                asiakas.setPostituslista(rs.getInt(8));
                temp.add(asiakas);
            }
            con.close();
            return temp;
        }catch(Exception e){
            System.out.println(e);
            return new ArrayList<Asiakas>();
        }
    }

    public ArrayList<Mokki> getMokit(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mokki");
            ArrayList<Mokki> temp = new ArrayList<Mokki>();
            while(rs.next()){
                boolean postiError = true;
                Mokki mokki = new Mokki();
                mokki.setMokkiId(rs.getInt(1));
            
                for(Alue a : alueet){
                    if(a.getAlueId() == rs.getInt(2)){
                        mokki.setAlue(a);
                        break;
                    }
                }
                for(Posti p : postit){
                    if(p.getPostinro().equals(rs.getString(3))){
                        mokki.setPosti(p);
                        postiError = false;
                        break;
                    }
                }
                if(postiError){
                    mokki.setPosti(new Posti("00000", "Rikki"));
                }
                mokki.setMokkinimi(rs.getString(4));
                mokki.setKatuosoite(rs.getString(5));
                mokki.setHinta(rs.getDouble(6));
                mokki.setKuvaus(rs.getString(7));
                mokki.setHenkilomaara(rs.getInt(8));
                mokki.setVarustelu(rs.getString(9));
                temp.add(mokki);
            }
            con.close();
            return temp;
        }catch(Exception e){
            System.out.println(e);
            return new ArrayList<Mokki>();
        }
    }

    public ArrayList<Varaus> getVaraukset(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM varaus");
            ArrayList<Varaus> temp = new ArrayList<Varaus>();
            while(rs.next()){
                Varaus varaus = new Varaus();
                varaus.setVarausId(rs.getInt(1));
                for(Asiakas a : asiakkaat){
                    if(a.getAsiakasId() == rs.getInt(2)){
                        varaus.setAsiakas(a);
                        break;
                    }
                }
                for(Mokki m : mokit){
                    if(m.getMokkiId() == rs.getInt(3)){
                        varaus.setMokki(m);
                        break;
                    }
                }
                varaus.setVarattuPvm(rs.getTimestamp(4));
                varaus.setVahvistusPvm(rs.getTimestamp(5));
                varaus.setVarattuAlkuPvm(rs.getTimestamp(6));
                varaus.setVarattuLoppuPvm(rs.getTimestamp(7));
                temp.add(varaus);
            }
            con.close();
            return temp;
        }catch(Exception e){
            System.out.println(e);
            return new ArrayList<Varaus>();
        }
    }

    public ArrayList<Lasku> getLaskut(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM lasku");
            ArrayList<Lasku> temp = new ArrayList<Lasku>();
            while(rs.next()){
                Lasku lasku = new Lasku();
                lasku.setLaskuId(rs.getInt(1));
                for(Varaus v : varaukset){
                    if(v.getVarausId() == rs.getInt(2)){
                        lasku.setVaraus(v);
                        break;
                    }
                }
                lasku.setSumma(rs.getDouble(3));
                lasku.setAlv(rs.getDouble(4));
                lasku.setMaksettu(rs.getInt(5));
                lasku.setErapaiva(rs.getDate(6));
                lasku.setLaskupaiva(rs.getDate(7));
                lasku.setLaskutyyppi(rs.getString(8));
                temp.add(lasku);
            }
            con.close();
            return temp;
        }catch(Exception e){
            System.out.println(e);
            return new ArrayList<Lasku>();
        }
    }

    public ArrayList<Palvelu> getPalvelut(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM palvelu");
            ArrayList<Palvelu> temp = new ArrayList<Palvelu>();
            while(rs.next()){
                Palvelu palvelu = new Palvelu();
                palvelu.setPalveluId(rs.getInt(1));
                boolean flag = true;
                for(Alue a : alueet){
                    if(a.getAlueId() == rs.getInt(2)){
                        palvelu.setAlue(a);
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    palvelu.setAlue(null);
                }

                palvelu.setNimi(rs.getString(3));
                palvelu.setTyyppi(rs.getInt(4));
                palvelu.setKuvaus(rs.getString(5));
                palvelu.setHinta(rs.getDouble(6));
                palvelu.setAlv(rs.getDouble(7));
                temp.add(palvelu);
            }
            con.close();
            for(Alue a : alueet){
                for(Palvelu p : temp){
                    if(a.getAlueId() == p.getAlue().getAlueId()){
                        a.addPalvelu(p);
                    }
                }
            }
            return temp;
        }catch(Exception e){
            System.out.println(e);
            return new ArrayList<Palvelu>();
        }
    }

    public ArrayList<VarauksetPalvelut> getVarauksetPalvelut(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM varauksen_palvelut");
            ArrayList<VarauksetPalvelut> temp = new ArrayList<VarauksetPalvelut>();
            while(rs.next()){
                VarauksetPalvelut palvelu = new VarauksetPalvelut();
                palvelu.setVarausId(rs.getInt(1));
                palvelu.setPalveluId(rs.getInt(2));
                palvelu.setAjankohta(rs.getTimestamp(3));
                palvelu.setLkm(rs.getInt(4));
                temp.add(palvelu);
            }
            con.close();
            for(VarauksetPalvelut v : temp){
                for(Varaus v2 : varaukset){
                    if(v.getVarausId() == v2.getVarausId()){
                        for(Palvelu p : palvelut){
                            if(v.getPalveluId() == p.getPalveluId()){
                                VarauksenPalvelu temp2 = new VarauksenPalvelu();
                                temp2.setPalvelu(p);
                                temp2.setAjankohta(v.getAjankohta());
                                temp2.setLkm(v.getLkm());
                                v2.addVarauksenPalvelu(temp2);
                            }
                        }
                    }
                }
            }
            return temp;
        }catch(Exception e){
            System.out.println(e);
            return new ArrayList<VarauksetPalvelut>();
        }
    }

    public String checkPosti(){
        for(Asiakas a : asiakkaat){
            boolean flag = true;
            for(Posti p : postit){
                if(a.getPosti().getPostinro().equals(p.getPostinro())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                try{
                    String query = "UPDATE asiakas SET ";
                    query += "postinro='00000'";
                    query += " WHERE asiakas_id=" + a.getAsiakasId();

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    con.close();
                    for(Posti p : postit){
                        if(p.getPostinro().equals("00000")){
                            a.setPosti(p);
                            break;
                        }
                    }
                }catch(Exception err){
                    System.out.println(err);
                }
            }
        }
        for(Mokki m : mokit){
            boolean flag = true;
            for(Posti p : postit){
                if(m.getPosti().getPostinro().equals(p.getPostinro())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                try{
                    String query = "UPDATE mokki SET ";
                    query += "postinro='00000'";
                    query += " WHERE mokki_id=" + m.getMokkiId();

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    con.close();
                    for(Posti p : postit){
                        if(p.getPostinro().equals("00000")){
                            m.setPosti(p);
                            break;
                        }
                    }
                }catch(Exception err){
                    System.out.println(err);
                }
            }
        }
        return "Asiakkaiden ja mokkien postit tarkistettu";
    }

    Scene mainScene;
    @Override
    public void start(Stage alkuIkkuna) {
        VBox mainPanel = new VBox();
        HBox topPane = new HBox(10);
        VBox midPane = new VBox();
        midPane.setPadding(new Insets(40,40,40,40));

        mainPanel.setStyle("-fx-background-color:#ADD8E6;");
        topPane.setStyle("-fx-border-style: hidden hidden solid hidden; -fx-border-width: 2; -fx-border-color: red;");
        topPane.setAlignment(Pos.CENTER);
        topPane.setMinHeight(75);
        Button etusivuNappi = new Button("Etusivu");
        Button asiakasNappi = new Button("Asiakkaat");
        Button laskuNappi = new Button("Laskut");
        Button varausNappi = new Button("Varaukset");
        Button palveluNappi = new Button("Palvelut");
        Button mokkiNappi = new Button("Mökit");
        /**
         * Niko
         * Nappi alueiden hallinnointiin
         * Nappi raporteille
         * Nappi ohjelman sulkemiseen
         */
        Button alueetNappi = new Button("Alueet");
        Button raportitNappi = new Button("Raportit");
        Button lopetaNappi = new Button("Lopeta");

        topPane.getChildren().add(etusivuNappi);
        topPane.getChildren().add(asiakasNappi);
        topPane.getChildren().add(laskuNappi);
        topPane.getChildren().add(varausNappi);
        topPane.getChildren().add(palveluNappi);
        topPane.getChildren().add(mokkiNappi);
    
        topPane.getChildren().add(alueetNappi);
        topPane.getChildren().add(raportitNappi);
        topPane.getChildren().add(lopetaNappi);

        mainPanel.getChildren().add(topPane);
        mainPanel.getChildren().add(midPane);

        midPane.getChildren().add(createEtusivu());

        etusivuNappi.setOnAction(e -> {
            if(currPage.equals("Etusivu")){

            }else{
                currPage = "Etusivu";
                midPane.getChildren().clear();
                midPane.getChildren().add(createEtusivu());
            }
        });
        asiakasNappi.setOnAction(e -> {
            if(currPage.equals("Asiakkaat")){

            }else{
                currPage = "Asiakkaat";
                midPane.getChildren().clear();
                midPane.getChildren().add(createAsiakkaatSivu());
            }
        });
        varausNappi.setOnAction(e -> {
            if(currPage.equals("Varaukset")){

            }else{
                currPage = "Varaukset";
                midPane.getChildren().clear();
                midPane.getChildren().add(createVarauksetSivu());
            }
        });
        laskuNappi.setOnAction(e -> {
            if(currPage.equals("Laskut")){

            }else{
                currPage = "Laskut";
                midPane.getChildren().clear();
                midPane.getChildren().add(createLaskutSivu());
            }
        });
        palveluNappi.setOnAction(e -> {
            if(currPage.equals("Palvelut")){

            }else{
                currPage = "Palvelut";
                midPane.getChildren().clear();
                midPane.getChildren().add(createPalvelutSivu());
            }
        });
        mokkiNappi.setOnAction(e -> {
            if(currPage.equals("Mökit")){

            }else{
                currPage = "Mökit";
                midPane.getChildren().clear();
                midPane.getChildren().add(createMokitSivu());
            }
        });
        
        /**
         * Toiminnallisuus alueet nappiin
         */
        alueetNappi.setOnAction(e ->{
            if(currPage.equals("Alueet")){
            
            }else{
                currPage = "Alueet";
                midPane.getChildren().clear();
                midPane.getChildren().add(createAlueetSivu());
            }
        });

        raportitNappi.setOnAction(e ->{
            if(currPage.equals("Raportit")){

            }else{
                currPage = "Raportit";
                midPane.getChildren().clear();
                midPane.getChildren().add(createRaportitSivu());
            }
        });

        lopetaNappi.setOnAction(e ->{
            System.exit(0);
        });

        mainScene = new Scene(mainPanel);

        alkuIkkuna.setTitle("Mökkiläiset");
        //alkuIkkuna.setMaximized(true);
        alkuIkkuna.setScene(mainScene);
        alkuIkkuna.setHeight(900);
        alkuIkkuna.setWidth(800);
        alkuIkkuna.show();

    }
    
    public static void main(String[] args) throws Exception {

        Application.launch(args);

    }

    private ScrollPane createEtusivu(){
        ScrollPane sp = new ScrollPane();
        VBox etusivu = new VBox();
        sp.setFitToWidth(true);
        //sp.setFitToHeight(true);
        sp.setContent(etusivu);
        etusivu.setStyle("-fx-background-color:#fff;");
        etusivu.setAlignment(Pos.CENTER);
        Text etusivuTitle = new Text("Etusivu");
        etusivuTitle.setStyle("-fx-font: 24 arial;");
        etusivu.getChildren().add(etusivuTitle);
        Button consoleButton = new Button("Console log data");
        etusivu.getChildren().add(consoleButton);
        consoleButton.setOnAction(e -> {
            System.out.println("\nUUSI LOG\n");
            System.out.println("ASIAKKAAT: ");
            System.out.println(asiakkaat);
            System.out.println("\n\nVARAUKSET: ");
            System.out.println(varaukset);
            System.out.println("\n\nLASKUT: ");
            System.out.println(laskut);
            System.out.println("\n\nMOKIT: ");
            System.out.println(mokit);
            System.out.println("\n\nPALVELUT: ");
            System.out.println(palvelut);
            System.out.println("\n\nPOSTI: ");
            System.out.println(postit);
            System.out.println("\n\nALUEET: ");
            System.out.println(alueet);

            System.out.println(alueet.get(0).getPalvelut());
            System.out.println(alueet.get(0).getPalvelut().get(0).getAlue());
            System.out.println("\n\n\n");
            System.out.println(varaukset.get(0).getVarattuLoppuPvm().getYear());
            System.out.println(varaukset.get(4).getVahvistusPvm() == null);
        });

        return sp;
    }

    private ScrollPane createAsiakkaatSivu(){
        ScrollPane sp = new ScrollPane();
        VBox asiakkaatSivu = new VBox();
        sp.setFitToWidth(true);
        //sp.setFitToHeight(true);
        sp.setContent(asiakkaatSivu);
        asiakkaatSivu.setStyle("-fx-background-color:#fff;");
        asiakkaatSivu.setAlignment(Pos.CENTER);
        asiakkaatSivu.setPadding(new Insets(10,0,0,0));
        Text asiakasTitle = new Text("Asiakkaat");
        asiakasTitle.setStyle("-fx-font: 24 arial;");
        asiakkaatSivu.getChildren().add(asiakasTitle);

        HBox asiakkaatMenu = new HBox(10);
        asiakkaatMenu.setAlignment(Pos.CENTER);
        asiakkaatMenu.setPadding(new Insets(10, 0, 10, 0));
        Button listAsiakkaat = new Button("Asiakaslista");
        Button uusiAsiakas = new Button("Luo uusi asiakas");
        asiakkaatMenu.getChildren().add(listAsiakkaat);
        asiakkaatMenu.getChildren().add(uusiAsiakas);
        asiakkaatSivu.getChildren().add(asiakkaatMenu);

        GridPane asiakkaatHaeLomake = new GridPane();
        asiakkaatHaeLomake.setHgap(8);
        asiakkaatHaeLomake.setVgap(4);
        asiakkaatHaeLomake.setPadding(new Insets(15, 0, 10, 0));
        asiakkaatHaeLomake.setAlignment(Pos.CENTER);
        Label etunimiHaku = new Label("Etunimi: ");
        Label sukunimiHaku = new Label("Sukunimi: ");
        TextField etunimiTF = new TextField();
        TextField sukunimiTF = new TextField();
        asiakkaatHaeLomake.add(etunimiHaku, 0,0);
        asiakkaatHaeLomake.add(etunimiTF, 0,1);
        asiakkaatHaeLomake.add(sukunimiHaku, 1,0);
        asiakkaatHaeLomake.add(sukunimiTF, 1,1);
        Button hakuBtn = new Button("Hae asiakasta");
        asiakkaatHaeLomake.add(hakuBtn, 2,1);
        asiakkaatSivu.getChildren().add(asiakkaatHaeLomake);
        
        asiakkaatSivu.getChildren().add(createAsiakkaatBox(asiakkaat));

        GridPane asiakkaatLuo = new GridPane();
        asiakkaatLuo.setAlignment(Pos.CENTER);
        asiakkaatLuo.setPadding(new Insets(15,0,15,0));
        asiakkaatLuo.setHgap(5);
        asiakkaatLuo.setVgap(10);
        Label errMsgU = new Label("");
        errMsgU.setTextFill(Color.RED);
        Label etunimiUusi = new Label("Etunimi: ");
        Label sukunimiUusi = new Label("Sukunimi: ");
        Label postinroUusi = new Label("Postinro: ");
        Label postiTUusi = new Label("Toimipaikka: ");
        Label lahiosoiteUusi = new Label("Lähiosoite: ");
        Label emailUusi = new Label("Email: ");
        Label puhelinnroUusi = new Label("Puhelinnro: ");
        TextField etunimiTFU = new TextField();
        TextField sukunimiTFU = new TextField();
        TextField postinroTFU = new TextField();
        TextField postiTTFU = new TextField();
        TextField lahiosoiteTFU = new TextField();
        TextField emailTFU = new TextField();
        TextField puhelinnroTFU = new TextField();
        CheckBox postituslistaCBU = new CheckBox("Suostumus postituslistalle");
        asiakkaatLuo.add(errMsgU, 0,0);
        asiakkaatLuo.add(etunimiUusi, 0,1);
        asiakkaatLuo.add(etunimiTFU, 1,1);
        asiakkaatLuo.add(sukunimiUusi, 0,2);
        asiakkaatLuo.add(sukunimiTFU, 1,2);
        asiakkaatLuo.add(lahiosoiteUusi, 0,3);
        asiakkaatLuo.add(lahiosoiteTFU, 1,3);
        asiakkaatLuo.add(postinroUusi, 0,4);
        asiakkaatLuo.add(postinroTFU, 1,4);
        asiakkaatLuo.add(postiTUusi, 0,5);
        asiakkaatLuo.add(postiTTFU, 1,5);
        asiakkaatLuo.add(emailUusi, 0,6);
        asiakkaatLuo.add(emailTFU, 1,6);
        asiakkaatLuo.add(puhelinnroUusi, 0,7);
        asiakkaatLuo.add(puhelinnroTFU, 1,7);
        asiakkaatLuo.add(postituslistaCBU, 0,8);
        Button uusiBtn = new Button("Tallenna");
        asiakkaatLuo.add(uusiBtn, 1,9);
        GridPane.setColumnSpan(uusiBtn, 2);
        GridPane.setColumnSpan(errMsgU, 2);

        listAsiakkaat.setOnAction(e -> {
            asiakkaatSivu.getChildren().clear();
            asiakkaatSivu.getChildren().add(asiakasTitle);
            asiakkaatSivu.getChildren().add(asiakkaatMenu);
            asiakkaatSivu.getChildren().add(asiakkaatHaeLomake);
            asiakkaatSivu.getChildren().add(createAsiakkaatBox(asiakkaat));

            etunimiTFU.setText("");
            sukunimiTFU.setText("");
            lahiosoiteTFU.setText("");
            postinroTFU.setText("");
            postiTTFU.setText("");
            emailTFU.setText("");
            puhelinnroTFU.setText("");
            errMsgU.setText("");
        });

        uusiAsiakas.setOnAction(e -> {
            asiakkaatSivu.getChildren().clear();
            asiakkaatSivu.getChildren().add(asiakasTitle);
            asiakkaatSivu.getChildren().add(asiakkaatMenu);
            asiakkaatSivu.getChildren().add(asiakkaatLuo);
        });

        hakuBtn.setOnAction(e -> {
            asiakkaatSivu.getChildren().clear();
            asiakkaatSivu.getChildren().add(asiakasTitle);
            asiakkaatSivu.getChildren().add(asiakkaatMenu);
            asiakkaatSivu.getChildren().add(asiakkaatHaeLomake);
            String eNimi;
            String sNimi;
            if(etunimiTF.getText() == ""){
                eNimi = "";
            }else{
                eNimi = "^" + etunimiTF.getText();
            }
            if(sukunimiTF.getText() == ""){
                sNimi = "";
            }else{
                sNimi = "^" + sukunimiTF.getText();
            }
            ArrayList<Asiakas> temp = new ArrayList<Asiakas>();
            for(Asiakas a : asiakkaat){
                Pattern pattern = Pattern.compile(eNimi, Pattern.CASE_INSENSITIVE);
                Pattern pattern2 = Pattern.compile(sNimi, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(a.getEtunimi());
                Matcher matcher2 = pattern2.matcher(a.getSukunimi());
                boolean matchFound = matcher.find();
                boolean matchFound2 = matcher2.find();
                if(matchFound && matchFound2){
                    temp.add(a);
                }
            }
            asiakkaatSivu.getChildren().add(createAsiakkaatBox(temp));
        });

        uusiBtn.setOnAction(e -> {
            int errors = 0;
            String eNimi = etunimiTFU.getText();
            String sNimi = sukunimiTFU.getText();
            String lahiosoite = lahiosoiteTFU.getText();
            String postinro = postinroTFU.getText();
            String postiT = postiTTFU.getText();
            String email = emailTFU.getText();
            String puhnro = puhelinnroTFU.getText();
            boolean postilist = postituslistaCBU.isSelected();
            etunimiUusi.setTextFill(Color.BLACK);
            sukunimiUusi.setTextFill(Color.BLACK);
            lahiosoiteUusi.setTextFill(Color.BLACK);
            postinroUusi.setTextFill(Color.BLACK);
            postiTUusi.setTextFill(Color.BLACK);
            emailUusi.setTextFill(Color.BLACK);
            puhelinnroUusi.setTextFill(Color.BLACK);
            if(eNimi.length() == 0){
                errors += 1;
                etunimiUusi.setTextFill(Color.RED);
            }
            if(sNimi.length() == 0){
                errors += 1;
                sukunimiUusi.setTextFill(Color.RED);
            }
            if(lahiosoite.length() == 0){
                errors += 1;
                lahiosoiteUusi.setTextFill(Color.RED);
            }
            if(postinro.length() == 0){
                errors += 1;
                postinroUusi.setTextFill(Color.RED);
            }
            if(postiT.length() == 0){
                errors += 1;
                postiTUusi.setTextFill(Color.RED);
            }
            if(email.length() == 0){
                errors += 1;
                emailUusi.setTextFill(Color.RED);
            }
            if(puhnro.length() == 0){
                errors += 1;
                puhelinnroUusi.setTextFill(Color.RED);
            }
            boolean flag = true;
            for(Posti p : postit){
                if(p.getPostinro().equals(postinro)){
                    flag = false;
                    if(p.getToimipaikka().equals(postiT)){

                    }else{
                        errors += 1;
                        postinroUusi.setTextFill(Color.RED);
                        postiTUusi.setTextFill(Color.RED);
                        errMsgU.setText("Postinumero ja toimipaikka ei täsmää!");
                    }
                }
            }
            if(flag){
                try{
                    String query = "INSERT INTO posti VALUES (";
                    query += "'" + postinro + "', ";
                    query += "'" + postiT + "')";

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    con.close();

                    setPostit(getPostit());

                }catch(Exception err){
                    System.out.println(err);
                    errMsgU.setText("VIRHE!");
                }
            }

            if(errors == 0){
                errMsgU.setText("");
                try{
                    String query = "INSERT INTO asiakas VALUES (";
                    query += "null" + ", ";
                    query += "'" + postinro + "', ";
                    query += "'" + eNimi + "', ";
                    query += "'" + sNimi + "', ";
                    query += "'" + lahiosoite + "', ";
                    query += "'" + email + "', ";
                    query += "'" + puhnro + "', ";
                    if(postilist){
                        query += 1 + ")";
                    }else{
                        query += 0 + ")";
                    }

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    con.close();
                    
                    etunimiTFU.setText("");
                    sukunimiTFU.setText("");
                    lahiosoiteTFU.setText("");
                    postinroTFU.setText("");
                    postiTTFU.setText("");
                    emailTFU.setText("");
                    puhelinnroTFU.setText("");
                    postituslistaCBU.setSelected(false);
                    
                    setAsiakkaat(getAsiakkaat());


                    errMsgU.setText("Asiakas tallennettu");
                }catch(Exception err){
                    System.out.println(err);
                    errMsgU.setText("Asiakkaan tallennuksessa virhe!");
                }
            }else{
                errMsgU.setText("Virhe! Tarkista syöttämäsi tiedot");
            }
        });


        return sp;
    }
    private VBox createAsiakkaatBox(ArrayList<Asiakas> param){
        VBox asiakkaatBox = new VBox();
        asiakkaatBox.setPadding(new Insets(10,10,10,10));
        if(param.isEmpty()){
            Text noAsiakas = new Text("Asiakkaita ei löytynyt.");
            asiakkaatBox.getChildren().add(noAsiakas);
        }else{
            for(Asiakas a : param){
                String n = "Id: " + a.getAsiakasId() + ", " + a.getEtunimi() + " " + a.getSukunimi();
                VBox aaTiedot = new VBox();
                Text errMsgM = new Text();
                errMsgM.setFill(Color.RED);
                if(a.getPosti().getPostinro().equals("00000") || a.getPosti().getToimipaikka().equals("Rikki")){
                    errMsgM.setText("Virhe postitiedoissa!");
                }
                aaTiedot.getChildren().add(errMsgM);
                GridPane aTiedot = new GridPane();
                aTiedot.setHgap(5);
                aTiedot.setVgap(6);
                aTiedot.setPadding(new Insets(15, 5, 15, 5));
                Label etunimiM = new Label("Etunimi: ");
                TextField etunimiTM = new TextField(a.getEtunimi());
                aTiedot.add(etunimiM, 0,0);
                aTiedot.add(etunimiTM, 1,0);
                Label sukunimiM = new Label("Sukunimi: ");
                TextField sukunimiTM = new TextField(a.getSukunimi());
                aTiedot.add(sukunimiM, 2,0);
                aTiedot.add(sukunimiTM, 3,0);
                Label lahiosoiteM = new Label("Lähiosoite: ");
                TextField lahiosoiteTM = new TextField(a.getLahiOsoite());
                aTiedot.add(lahiosoiteM, 0,1);
                aTiedot.add(lahiosoiteTM, 1,1);
                Label postinroM = new Label("Postinumero: ");
                TextField postinroTM = new TextField(a.getPosti().getPostinro());
                if(postinroTM.getText().equals("00000")){
                    postinroM.setTextFill(Color.RED);
                }
                aTiedot.add(postinroM, 0,2);
                aTiedot.add(postinroTM, 1,2);
                Label postiTM = new Label("Toimipaikka: ");
                TextField postiTTM = new TextField(a.getPosti().getToimipaikka());
                if(postiTTM.getText().equals("Rikki")){
                    postiTM.setTextFill(Color.RED);
                }
                aTiedot.add(postiTM, 2,2);
                aTiedot.add(postiTTM, 3,2);
                Label emailM = new Label("Email: ");
                TextField emailTM = new TextField(a.getEmail());
                aTiedot.add(emailM, 0,3);
                aTiedot.add(emailTM, 1,3);
                Label puhM = new Label("Puhelinnro: ");
                TextField puhTM = new TextField(a.getPuhelinnro());
                aTiedot.add(puhM, 2,3);
                aTiedot.add(puhTM, 3,3);
                CheckBox postituslistaCB = new CheckBox("Suostumus postituslistalle: ");
                postituslistaCB.setSelected(a.getPostituslista());
                aTiedot.add(postituslistaCB, 0, 4);
                Button saveEditBtn = new Button("Tallenna");
                aTiedot.add(saveEditBtn, 1,5);
                Button deleteUserBtn = new Button("Poista");
                aTiedot.add(deleteUserBtn, 2,5);
                
                


                aaTiedot.getChildren().add(aTiedot);
                TitledPane i = new TitledPane(n, aaTiedot);
                i.setExpanded(false);

                saveEditBtn.setOnAction(e -> {
                    int errors = 0;
                    String eNimi = etunimiTM.getText();
                    String sNimi = sukunimiTM.getText();
                    String osoite = lahiosoiteTM.getText();
                    String postinro = postinroTM.getText();
                    String postiTP = postiTTM.getText();
                    String email = emailTM.getText();
                    String puhnro = puhTM.getText();
                    boolean postilist = postituslistaCB.isSelected();
                    etunimiM.setTextFill(Color.BLACK);
                    sukunimiM.setTextFill(Color.BLACK);
                    lahiosoiteM.setTextFill(Color.BLACK);
                    postinroM.setTextFill(Color.BLACK);
                    postiTM.setTextFill(Color.BLACK);
                    emailM.setTextFill(Color.BLACK);
                    puhM.setTextFill(Color.BLACK);
                    if(eNimi.length() == 0){
                        errors += 1;
                        etunimiM.setTextFill(Color.RED);
                    }
                    if(sNimi.length() == 0){
                        errors += 1;
                        sukunimiM.setTextFill(Color.RED);
                    }
                    if(osoite.length() == 0){
                        errors += 1;
                        lahiosoiteM.setTextFill(Color.RED);
                    }
                    if(postinro.length() == 0){
                        errors += 1;
                        postinroM.setTextFill(Color.RED);
                    }else if(postinro.equals("00000")){
                        errors += 1001;
                        postinroM.setTextFill(Color.RED);
                    }
                    if(postiTP.length() == 0){
                        errors += 1;
                        postiTM.setTextFill(Color.RED);
                    }else if(postiTP.equals("Rikki")){
                        errors += 1001;
                        postiTM.setTextFill(Color.RED);
                    }
                    if(email.length() == 0){
                        errors += 1;
                        emailM.setTextFill(Color.RED);
                    }
                    if(puhnro.length() == 0){
                        errors += 1;
                        puhM.setTextFill(Color.RED);
                    }
    
                    if(errors == 0){
                        if(a.checkCopy(eNimi, sNimi, postinro, osoite, email, puhnro, postilist)){
                            
                        }else{
                            try{
                                String query = "UPDATE asiakas SET ";
                                query += "postinro='" + postinro + "', ";
                                query += "etunimi='" + eNimi + "', ";
                                query += "sukunimi='" + sNimi + "', ";
                                query += "lahiosoite='" + osoite + "', ";
                                query += "email='" + email + "', ";
                                query += "puhelinnro='" + puhnro + "', ";
                                if(postilist){
                                    query += "postituslista=" + 1;
                                }else{
                                    query += "postituslista=" + 0;
                                }
                                query += " WHERE asiakas_id=" + a.getAsiakasId();
        
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                                Statement stmt = con.createStatement();
                                stmt.executeUpdate(query);
                                con.close();
                                a.setEtunimi(eNimi);
                                a.setSukunimi(sNimi);
                                a.setLahiOsoite(osoite);
                                for(Posti p : postit){
                                    if(p.getPostinro().equals(postinro)){
                                        a.setPosti(p);
                                        break;
                                    }
                                }
                                a.setEmail(email);
                                a.setPuhelinnro(puhnro);
                                if(postilist){
                                    a.setPostituslista(1);
                                }else{
                                    a.setPostituslista(0);
                                }
                                errMsgM.setText("Muutokset tallennettu");
                                errMsgM.setFill(Color.GREEN);
                                i.setText("Id: " + a.getAsiakasId() + ", " + a.getEtunimi() + " " + a.getSukunimi());
                               
                            }catch(Exception err){
                                System.out.println(err);
                                errMsgM.setText("Muutosten tallennuksessa virhe!");
                            }
                        }
                        
                    }else{
                        if(errors > 1000){
                            errMsgM.setText("Virhe postitiedoissa!");
                        }else{
                            errMsgM.setText("Virhe! Tarkista syöttämäsi tiedot!");
                        }
                    }
                });

                deleteUserBtn.setOnAction(e -> {
                    try{
                        String sql = "DELETE FROM asiakas WHERE asiakas_id=" + a.getAsiakasId();
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(sql);
                        con.close();
                        asiakkaatBox.getChildren().remove(i);
                        for(Asiakas a2 : asiakkaat){
                            if(a2.getAsiakasId() == a.getAsiakasId()){
                                asiakkaat.remove(a);
                                break;
                            }
                        }
                    }catch(Exception err){
                        System.out.println(err);
                    }
                });

                asiakkaatBox.getChildren().add(i);
            }
        }

        return asiakkaatBox;
    }

    private ScrollPane createVarauksetSivu(){
        ScrollPane sp = new ScrollPane();
        VBox varauksetSivu = new VBox();
        sp.setFitToWidth(true);
        //sp.setFitToHeight(true);
        sp.setContent(varauksetSivu);
        varauksetSivu.setStyle("-fx-background-color:#fff;");
        varauksetSivu.setAlignment(Pos.CENTER);
        varauksetSivu.setPadding(new Insets(10,0,0,0));
        Text varausTitle = new Text("Varaukset");
        varausTitle.setStyle("-fx-font: 24 arial;");
        varauksetSivu.getChildren().add(varausTitle);

        HBox varauksetMenu = new HBox(10);
        varauksetMenu.setAlignment(Pos.CENTER);
        varauksetMenu.setPadding(new Insets(10, 0, 10, 0));
        Button listVaraukset = new Button("Varauslista");
        Button uusiVaraus = new Button("Luo uusi varaus");
        varauksetMenu.getChildren().add(listVaraukset);
        varauksetMenu.getChildren().add(uusiVaraus);
        varauksetSivu.getChildren().add(varauksetMenu);

        GridPane varauksetHaeLomake = new GridPane();
        varauksetHaeLomake.setHgap(8);
        varauksetHaeLomake.setVgap(4);
        varauksetHaeLomake.setPadding(new Insets(15, 0, 10, 0));
        varauksetHaeLomake.setAlignment(Pos.CENTER);

        Label nimiHaku = new Label("Nimi: ");
        ComboBox<String> nimiCB = new ComboBox<String>();
        nimiCB.getItems().add("Ei hakuehtoa");
        for(Asiakas a : asiakkaat){
            String name = a.getEtunimi() + " " + a.getSukunimi();
            nimiCB.getItems().add(name);
        }
        nimiCB.setValue("Ei hakuehtoa");
        Label mokkiHaku = new Label("Mökin nimi: ");
        Label postinroHaku = new Label("Postinro: ");
        TextField mokkiTF = new TextField();
        TextField postinroTF = new TextField();
        Label alkupvmHaku = new Label("Alkupvm: ");
        Label loppupvmHaku = new Label("Loppupvm: ");
        //TextField alkupvmTF = new TextField();
        //TextField loppupvmTF = new TextField();
        DatePicker alkupvmDP = new DatePicker();
        DatePicker loppupvmDP = new DatePicker();
        varauksetHaeLomake.add(nimiHaku, 0,0);
        varauksetHaeLomake.add(nimiCB, 1,0);
        varauksetHaeLomake.add(mokkiHaku, 0,2);
        varauksetHaeLomake.add(mokkiTF, 0,3);
        varauksetHaeLomake.add(postinroHaku, 1,2);
        varauksetHaeLomake.add(postinroTF, 1,3);
        varauksetHaeLomake.add(alkupvmHaku, 0,4);
        //varauksetHaeLomake.add(alkupvmTF, 0,5);
        varauksetHaeLomake.add(alkupvmDP, 0,5);
        varauksetHaeLomake.add(loppupvmHaku, 1,4);
        //varauksetHaeLomake.add(loppupvmTF, 1,5);
        varauksetHaeLomake.add(loppupvmDP, 1,5);
        Button hakuBtn = new Button("Hae varausta");
        varauksetHaeLomake.add(hakuBtn, 0,6);
        varauksetSivu.getChildren().add(varauksetHaeLomake);
        
        varauksetSivu.getChildren().add(createVarauksetBox(varaukset));

        
        hakuBtn.setOnAction(e -> {
            varauksetSivu.getChildren().clear();
            varauksetSivu.getChildren().add(varausTitle);
            varauksetSivu.getChildren().add(varauksetMenu);
            varauksetSivu.getChildren().add(varauksetHaeLomake);
            String nimi;
            String mNimi;
            String postinro;
            LocalDate alku = alkupvmDP.getValue();
            LocalDate loppu = loppupvmDP.getValue();

            if(nimiCB.getValue() == null || nimiCB.getValue() == "Ei hakuehtoa"){
                nimi = "";
            }else{
                nimi = nimiCB.getValue();
            }
            if(mokkiTF.getText() == ""){
                mNimi = "";
            }else{
                mNimi = "^" + mokkiTF.getText();
            }
            if(postinroTF.getText() == ""){
                postinro = "";
            }else{
                postinro = "^" + postinroTF.getText();
            }
            
            
            ArrayList<Varaus> temp = new ArrayList<Varaus>();
            for(Varaus v : varaukset){
                Pattern pattern = Pattern.compile(nimi, Pattern.CASE_INSENSITIVE);
                Pattern pattern2 = Pattern.compile(mNimi, Pattern.CASE_INSENSITIVE);
                Pattern pattern3 = Pattern.compile(postinro, Pattern.CASE_INSENSITIVE);

                Matcher matcher = pattern.matcher(v.getAsiakas().getEtunimi() + " " + v.getAsiakas().getSukunimi());
                Matcher matcher2 = pattern2.matcher(v.getMokki().getMokkinimi());
                Matcher matcher3 = pattern3.matcher(v.getMokki().getPosti().getPostinro());

                boolean matchFound = matcher.find();
                boolean matchFound2 = matcher2.find();
                boolean matchFound3 = matcher3.find();

                Date alkuDate = new Date(v.getVarattuAlkuPvm().getTime());
                Date loppuDate = new Date(v.getVarattuLoppuPvm().getTime());
                boolean alkuFlag = true;
                if(alku != null){
                    if(alku.compareTo(alkuDate.toLocalDate()) <= 0){
                        alkuFlag = true;
                    }else{
                        alkuFlag = false;
                    }
                }
                boolean loppuFlag = true;
                if(loppu != null){
                    if(loppu.compareTo(loppuDate.toLocalDate()) <= 0){
                        loppuFlag = true;
                    }else{
                        loppuFlag = false;
                    }
                }
                
                if(matchFound && matchFound2 && matchFound3 && alkuFlag && loppuFlag){
                    temp.add(v);
                }
            }
            varauksetSivu.getChildren().add(createVarauksetBox(temp));
            
        });
        

        return sp;
    }
    private VBox createVarauksetBox(ArrayList<Varaus> param){
        VBox varauksetBox = new VBox();
        varauksetBox.setPadding(new Insets(10,10,10,10));
        if(param.isEmpty()){
            Text noVaraus = new Text("Varauksia ei löytynyt.");
            varauksetBox.getChildren().add(noVaraus);
        }else{
            for(Varaus v : param){
                String vAlkuPvm = "";
                String vLoppuPvm = "";
                if(v.getVarattuAlkuPvm().getDate() < 10){
                    vAlkuPvm += "0";
                    vAlkuPvm += String.valueOf(v.getVarattuAlkuPvm().getDate()) + "/";
                }else{
                    vAlkuPvm += String.valueOf(v.getVarattuAlkuPvm().getDate()) + "/";
                }
                if(v.getVarattuAlkuPvm().getMonth() + 1 < 10){
                    vAlkuPvm += "0";
                    vAlkuPvm += String.valueOf(v.getVarattuAlkuPvm().getMonth() + 1) + "/";
                }else{
                    vAlkuPvm += String.valueOf(v.getVarattuAlkuPvm().getMonth()) + "/";
                }
                vAlkuPvm += String.valueOf(v.getVarattuAlkuPvm().getYear() + 1900);
                if(v.getVarattuLoppuPvm().getDate() < 10){
                    vLoppuPvm += "0";
                    vLoppuPvm += String.valueOf(v.getVarattuLoppuPvm().getDate()) + "/";
                }else{
                    vLoppuPvm += String.valueOf(v.getVarattuLoppuPvm().getDate()) + "/";
                }
                if(v.getVarattuLoppuPvm().getMonth() + 1 < 10){
                    vLoppuPvm += "0";
                    vLoppuPvm += String.valueOf(v.getVarattuLoppuPvm().getMonth() + 1) + "/";
                }else{
                    vLoppuPvm += String.valueOf(v.getVarattuLoppuPvm().getMonth()) + "/";
                }
                vLoppuPvm += String.valueOf(v.getVarattuLoppuPvm().getYear() + 1900);

                String n = "Id: " + v.getVarausId() + ", " + v.getMokki().getMokkinimi() + " : " + vAlkuPvm + " - " + vLoppuPvm;
                VBox vvTiedot = new VBox();
                Text errMsgM = new Text();
                errMsgM.setFill(Color.RED);
                
                vvTiedot.getChildren().add(errMsgM);
                GridPane vTiedot = new GridPane();
                vTiedot.setHgap(5);
                vTiedot.setVgap(6);
                vTiedot.setPadding(new Insets(15, 5, 15, 5));

                Label asiakasLabel = new Label("Asiakas: ");
                if(v.getAsiakas() != null){
                    Button goToAsiakas = new Button((v.getAsiakas().getEtunimi() + " " + v.getAsiakas().getSukunimi()));
                    vTiedot.add(asiakasLabel, 0,0);
                    vTiedot.add(goToAsiakas, 1,0);
                }else{
                    Text noAsiakasLabel = new Text("Ei asiakasta");
                    vTiedot.add(asiakasLabel, 0,0);
                    vTiedot.add(noAsiakasLabel, 1,0);
                }

                Label mokkiLabel = new Label("Mökki: ");
                if(v.getMokki() != null){
                    Button goToMokki = new Button(v.getMokki().getMokkinimi());
                    vTiedot.add(mokkiLabel, 0,1);
                    vTiedot.add(goToMokki, 1,1);
                }else{
                    Text noMokkiLabel = new Text("Ei mökkiä");
                    vTiedot.add(asiakasLabel, 0,0);
                    vTiedot.add(noMokkiLabel, 1,0);
                }
      
                GridPane vTiedot2 = new GridPane();
                vTiedot2.setHgap(5);
                vTiedot2.setVgap(6);
                vTiedot2.setPadding(new Insets(15, 5, 15, 5));

                Text varauksenTiedotTitle = new Text("Varauksen tiedot");
                vTiedot2.add(varauksenTiedotTitle, 0,0);
                GridPane.setColumnSpan(varauksenTiedotTitle, GridPane.REMAINING);

                Label varattupvmLabel = new Label("Varaus päivämäärä: ");
                TextField varattupvmTF = new TextField(String.valueOf(v.getVarattuPvm()));
                vTiedot2.add(varattupvmLabel, 0,1);
                vTiedot2.add(varattupvmTF, 1,1);

                vvTiedot.getChildren().add(vTiedot);
                vvTiedot.getChildren().add(vTiedot2);
                TitledPane i = new TitledPane(n, vvTiedot);
                i.setExpanded(false);
                varauksetBox.getChildren().add(i);

            }

        }
        return varauksetBox;
    }
    
    
    private ScrollPane createLaskutSivu(){
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        //sp.setFitToHeight(true);
        VBox laskutSivu = new VBox();
        sp.setContent(laskutSivu);
        laskutSivu.setStyle("-fx-background-color:#fff;");
        laskutSivu.setAlignment(Pos.CENTER);
        laskutSivu.setPadding(new Insets(10,0,0,0));
        Text laskuTitle = new Text("Laskut");
        laskuTitle.setStyle("-fx-font: 24 arial;");
        laskutSivu.getChildren().add(laskuTitle);
        VBox laskutBox = new VBox();
        laskutBox.setPadding(new Insets(10,10,10,10));
        for(Lasku l : laskut){
            String maksettu = "";
            if(l.getMaksettu() == true){
                maksettu = "Kyllä";
            } else if(l.getMaksettu() == false){
                maksettu = "Ei";
            }
            Label label = new Label(l.getLaskuId() + " " + l.getSumma() + ", maksettu: " + maksettu + ", eräpäivä: " + l.getErapaiva());
            label.setPadding(new Insets(15, 5, 15, 5));
            String n = "Id: " + l.getLaskuId() + ", " + l.getSumma();
            
            Label label1 = new Label("Maksettu: " + maksettu);
            //
            TitledPane i = new TitledPane(n, label);
            i.setExpanded(false);

            laskutBox.getChildren().add(i);
        }
        laskutSivu.getChildren().add(laskutBox);

        return sp;
    }

 
   
    private ScrollPane createPalvelutSivu(){
        ScrollPane sp = new ScrollPane();
        VBox palvelutSivu = new VBox();
        sp.setFitToWidth(true);
        sp.setContent(palvelutSivu);
        palvelutSivu.setStyle("-fx-background-color:#fff;");
        palvelutSivu.setAlignment(Pos.CENTER);
        palvelutSivu.setPadding(new Insets(10,0,0,0));
        Text palvelutTitle = new Text("Palvelut");
        palvelutTitle.setStyle("-fx-font: 24 arial;");
        palvelutSivu.getChildren().add(palvelutTitle);

        HBox palvelutMenu = new HBox(10);
        palvelutMenu.setAlignment(Pos.CENTER);
        palvelutMenu.setPadding(new Insets(10, 0, 10, 0));
        Button uusiPalvelu = new Button("Luo uusi palvelu");
        Button listPalvelut = new Button("Palvelulista");

        palvelutMenu.getChildren().add(listPalvelut);
        palvelutMenu.getChildren().add(uusiPalvelu);
        palvelutSivu.getChildren().add(palvelutMenu);
        
        GridPane palvelutHaeLomake = new GridPane();
        palvelutHaeLomake.setHgap(8);
        palvelutHaeLomake.setVgap(4);
        palvelutHaeLomake.setPadding(new Insets(15, 0, 10, 0));
        palvelutHaeLomake.setAlignment(Pos.CENTER);
        

        //LUO UUSI PALVELU NÄKYMÄ
        GridPane palvelutLuo = new GridPane();
        palvelutLuo.setAlignment(Pos.CENTER);
        palvelutLuo.setPadding(new Insets(15,0,15,0));
        palvelutLuo.setHgap(6);
        palvelutLuo.setVgap(11);
        Label errMsgU = new Label("");
        errMsgU.setTextFill(Color.RED);

        
        Label palveluNimiUusiLabel = new Label("Palvelun nimi: ");
        Label palveluAlueUusiLabel = new Label("Alue: ");
        Label palveluTyyppiUusiLabel = new Label("Palvelun tyyppi: ");
        Label palveluKuvausUusiLabel = new Label("Palvelun kuvaus: ");
        Label palveluHintaUusiLabel = new Label("Hinta: "); 
        Label palveluAlvUusiLabel = new Label("Alv: ");

        getAlueet();

        ComboBox<String> alueUusiCB = new ComboBox<String>();
        alueUusiCB.setPromptText("Valitse alue.");
        for(Alue a : alueet){
            String alueNimiCB = a.getNimi();
            alueUusiCB.getItems().add(alueNimiCB);
        }
    
        TextField palveluNimiUusiTxtField = new TextField();
        TextField palveluTyyppiUusiTxtField = new TextField();
        TextField palveluKuvausUusiTxtField = new TextField();
        TextField palveluHintaUusiTxtField = new TextField();
        TextField palveluAlvUusiTxtField = new TextField();
        
        /**
         * Lisätään yllä luodut näkymään
         */
        palvelutLuo.add(errMsgU, 0,0);

        palvelutLuo.add(palveluNimiUusiLabel, 0, 1);
        palvelutLuo.add(palveluNimiUusiTxtField,0, 2);

        palvelutLuo.add(palveluAlueUusiLabel, 1,1);
        palvelutLuo.add(alueUusiCB, 1,2);

        palvelutLuo.add(palveluTyyppiUusiLabel, 0,3);
        palvelutLuo.add(palveluTyyppiUusiTxtField, 0,4);

        palvelutLuo.add(palveluKuvausUusiLabel, 1, 3);
        palvelutLuo.add(palveluKuvausUusiTxtField, 1, 4);
    
        palvelutLuo.add(palveluHintaUusiLabel, 0,5);
        palvelutLuo.add(palveluHintaUusiTxtField, 0,6);

        palvelutLuo.add(palveluAlvUusiLabel, 1,5);
        palvelutLuo.add(palveluAlvUusiTxtField, 1,6);

    
        Button tallennaPalveluBtn = new Button("Tallenna");
        palvelutLuo.add(tallennaPalveluBtn, 1,9);
        
        uusiPalvelu.setOnAction(e -> {
            palvelutSivu.getChildren().clear();
            palvelutSivu.getChildren().add(palvelutTitle);
            palvelutSivu.getChildren().add(palvelutMenu);
            palvelutSivu.getChildren().add(palvelutLuo);
        });

        listPalvelut.setOnAction(e -> {
            palvelutSivu.getChildren().clear();
            palvelutSivu.getChildren().add(palvelutTitle);
            palvelutSivu.getChildren().add(palvelutMenu);
            palvelutSivu.getChildren().add(palvelutHaeLomake);
            palvelutSivu.getChildren().add(createPalvelutBox(palvelut));
        });

        
        tallennaPalveluBtn.setOnAction(e -> {
            int errors = 0;


            String alueNimiUusi = alueUusiCB.getValue();
            int palveluUusiAlueId = 0;
            for(Alue a : alueet){
                if(a.getNimi() == alueUusiCB.getValue()){
                    palveluUusiAlueId = a.getAlueId();
                }
            }
            String palveluNimiUusi = palveluNimiUusiTxtField.getText();
            String palveluTyyppiUusi = palveluTyyppiUusiTxtField.getText();
            String palveluKuvausUusi = palveluKuvausUusiTxtField.getText();
            String palveluHintaUusi = palveluHintaUusiTxtField.getText();
            String palveluAlvUusi = palveluAlvUusiTxtField.getText();
        
            palveluAlueUusiLabel.setTextFill(Color.BLACK);
            palveluNimiUusiLabel.setTextFill(Color.BLACK);
            palveluTyyppiUusiLabel.setTextFill(Color.BLACK);
            palveluKuvausUusiLabel.setTextFill(Color.BLACK);
            palveluHintaUusiLabel.setTextFill(Color.BLACK);
            palveluAlvUusiLabel.setTextFill(Color.BLACK);
            if(palveluNimiUusi.length() == 0){
                errors += 1;
                palveluNimiUusiLabel.setTextFill(Color.RED);
            }
            if(alueUusiCB.getValue() == null){
                palveluAlueUusiLabel.setTextFill(Color.RED);
            }
            if(palveluTyyppiUusi.length() == 0){
                errors += 1;
                palveluTyyppiUusiLabel.setTextFill(Color.RED);
            }
            if(palveluKuvausUusi.length() == 0){
                errors += 1;
                palveluKuvausUusiLabel.setTextFill(Color.RED);
            }
            if(palveluHintaUusi.length() == 0){
                errors += 1;
                palveluHintaUusiLabel.setTextFill(Color.RED);
            }
            if(palveluAlvUusi.length() == 0){
                errors += 1;
                palveluAlvUusiLabel.setTextFill(Color.RED);
            }
            
            /**
             * Tallennetaan uusi palvelu SQL-tietokantaan
             */
            try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);

            String sql = "INSERT INTO palvelu values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, null);
            pstmt.setInt(2, palveluUusiAlueId);
            pstmt.setString(3, palveluNimiUusi);
            pstmt.setString(4, palveluTyyppiUusi);
            pstmt.setString(5, palveluKuvausUusi);
            pstmt.setDouble(6, Double.parseDouble(palveluHintaUusi));
            pstmt.setDouble(7, Double.parseDouble(palveluAlvUusi));
       
            pstmt.executeUpdate();
            
            conn.close();
            
            palveluNimiUusiTxtField.setText("");
            alueUusiCB.setPromptText("Valitse alue.");
            palveluTyyppiUusiTxtField.setText("");
            palveluKuvausUusiTxtField.setText("");
            palveluHintaUusiTxtField.setText("");
            palveluAlvUusiTxtField.setText("");
                    
            setPalvelut(getPalvelut());
            errMsgU.setTextFill(Color.GREEN);
            errMsgU.setText("Palvelu tallennettu");
        }catch(Exception err){
            System.out.println(err);
            errMsgU.setText("Palvelun tallennuksessa virhe!");
        }
        });
        
        palvelutSivu.getChildren().add(palvelutHaeLomake);
       
        palvelutSivu.getChildren().add(createPalvelutBox(palvelut));
        
    return sp;  
}
    

    private VBox createPalvelutBox(ArrayList<Palvelu> param){
        VBox palvelutBox = new VBox();
        
        palvelutBox.setPadding(new Insets(10,10,10,10));
        if(param.isEmpty()){
            Text noPalvelu = new Text("Palveluita ei löytynyt.");
            palvelutBox.getChildren().add(noPalvelu);
        }else{
            for(Palvelu p : param){
                
                String n = "Palvelun nimi: " + p.getNimi();
                VBox palveluTiedot = new VBox();
                Text errMsgM = new Text();
                errMsgM.setFill(Color.RED);
                
                palveluTiedot.getChildren().add(errMsgM);
                GridPane pTiedot = new GridPane();
                pTiedot.setHgap(5);
                pTiedot.setVgap(6);
                pTiedot.setPadding(new Insets(15, 5, 15, 5));
                if(p != null){

                    Label palveluNimiLabel = new Label("Palvelun nimi: ");
                    TextField palveluNimiTxtField = new TextField(p.getNimi());
                    pTiedot.add(palveluNimiLabel, 0 ,0);
                    pTiedot.add(palveluNimiTxtField, 0,1);
                
                    Label palveluAlueLabel = new Label("Alue");
                    ComboBox palveluAlueCB = new ComboBox<String>();
                    if(p.getAlue()!= null){
                        palveluAlueCB.getItems().add(p.getAlue().getNimi());
                        palveluAlueCB.setValue(p.getAlue().getNimi());
                    }else {
                        palveluAlueCB.getItems().add("Valitse alue.");
                        palveluAlueCB.setValue("Valitse alue.");
                    }
                    for(Alue a : alueet){
                        if(p.getAlue()!= null){
                            if(a.getAlueId() == p.getAlue().getAlueId()){

                            }else{
                                String alueNimi = a.getNimi();
                                palveluAlueCB.getItems().add(alueNimi);
                            }
                        }else{
                            String alueNimi = a.getNimi();
                            palveluAlueCB.getItems().add(alueNimi);
                        }
                       
                    }
                    palveluAlueCB.setPromptText(p.getAlue().getNimi());
                    
                    pTiedot.add(palveluAlueLabel,1, 0);
                    pTiedot.add(palveluAlueCB, 1, 1);
                
                    
                    Label palveluTyyppiLabel = new Label("Tyyppi: ");
                    TextField palveluTyyppiTxtField = new TextField(String.valueOf(p.getTyyppi()));
                    pTiedot.add(palveluTyyppiLabel, 0, 2);
                    pTiedot.add(palveluTyyppiTxtField, 0, 3);
                    
                    Label palveluKuvausLabel = new Label("Kuvaus: ");
                    TextField palveluKuvausTxtField = new TextField(p.getKuvaus());
                    pTiedot.add(palveluKuvausLabel, 1, 2);
                    pTiedot.add(palveluKuvausTxtField, 1, 3);

                    Label palveluHintaLabel = new Label("Hinta: ");
                    TextField palveluHintaTxtField = new TextField(String.valueOf(p.getHinta()));
                    pTiedot.add(palveluHintaLabel, 0, 4);
                    pTiedot.add(palveluHintaTxtField, 0, 5);

                    Label palveluAlvLabel = new Label("Alv: ");
                    TextField palveluAlvTxtField = new TextField(String.valueOf(p.getAlv()));
                    pTiedot.add(palveluAlvLabel,1, 4);
                    pTiedot.add(palveluAlvTxtField, 1, 5);

                   
                    palveluTiedot.getChildren().add(pTiedot);
                    TitledPane i = new TitledPane(n, palveluTiedot);
                    i.setExpanded(false);
                    palvelutBox.getChildren().add(i);

                    Button saveEditBtn = new Button("Tallenna");
                    pTiedot.add(saveEditBtn, 0,8);
                    Button deletePalvleuBtn = new Button("Poista");
                    pTiedot.add(deletePalvleuBtn, 1,8);
                
                 
                    deletePalvleuBtn.setOnAction(e -> {
                        try{
                            
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                            String sql = "DELETE FROM palvelu WHERE palvelu_id = ?";
                            PreparedStatement poista = conn.prepareStatement(sql);
                            
                            poista.setInt(1, p.getPalveluId());
                            poista.executeUpdate();
                            conn.close();
                            palvelutBox.getChildren().remove(i);
                            for(Palvelu p2 : palvelut){
                                if(p2.getPalveluId() == p.getPalveluId()){
                                    palvelut.remove(p);
                                    break;
                                }
                            }
                        }catch(Exception err){
                            System.out.println(err);
                        }
                        
                    });
                
                saveEditBtn.setOnAction(e ->{
                    int errors = 0;

                    int palveluMuokattuAlueId = 0;
                    for(Alue a : alueet){
                        if(a.getNimi() == palveluAlueCB.getValue()){
                            palveluMuokattuAlueId = a.getAlueId();
                        }

                    String palveluMuokattuNimi = palveluNimiTxtField.getText();
                    String palveluMuokattuTyyppi = palveluTyyppiTxtField.getText();
                    String palveluMuokattuKuvaus = palveluKuvausTxtField.getText();
                    String palveluMuokattuHinta = palveluHintaTxtField.getText();
                    String palveluMuokattuAlv = palveluAlvTxtField.getText();
              
                    palveluAlueLabel.setTextFill(Color.BLACK);
                    palveluNimiLabel.setTextFill(Color.BLACK);
                    palveluTyyppiLabel.setTextFill(Color.BLACK);
                    palveluKuvausLabel.setTextFill(Color.BLACK);
                    palveluHintaLabel.setTextFill(Color.BLACK);
                    palveluAlvLabel.setTextFill(Color.BLACK);

                    if(palveluMuokattuNimi.length() == 0){
                        errors += 1;
                        palveluNimiLabel.setTextFill(Color.RED);
                    }
                    if(palveluMuokattuTyyppi.length() == 0){
                        errors += 1;
                        palveluTyyppiLabel.setTextFill(Color.RED);
                    }
                    if(palveluMuokattuKuvaus.length() == 0){
                        errors += 1;
                        palveluKuvausLabel.setTextFill(Color.RED);
                    }
                    if(palveluMuokattuHinta.length() == 0){
                        errors += 1;
                        palveluHintaLabel.setTextFill(Color.RED);
                    }
                    if(palveluMuokattuAlv.length() == 0){
                        errors += 1;
                        palveluAlvLabel.setTextFill(Color.RED);
                    }
    
                    if(errors == 0){
                            try{

                                Class.forName("com.mysql.cj.jdbc.Driver");
                                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);

                                String sql =  "UPDATE palvelu SET alue_id = ?, nimi = ?, tyyppi = ?, kuvaus = ?, hinta = ?, alv = ? WHERE palvelu_id = ?";
                                PreparedStatement update = conn.prepareStatement(sql);
                                
                                update.setInt(1, palveluMuokattuAlueId);
                                update.setString(2, palveluMuokattuNimi);
                                update.setString(3, palveluMuokattuTyyppi);
                                update.setString(4, palveluMuokattuKuvaus);
                                update.setDouble(5, Double.parseDouble(palveluMuokattuHinta));
                                update.setDouble(6, Double.parseDouble(palveluMuokattuAlv));
                                update.setInt(7, p.getPalveluId());
                        
                                update.executeUpdate();
                                
                                conn.close();

                                
                                for(Alue b: alueet){
                                    if(b.getAlueId() == palveluMuokattuAlueId){
                                        p.setAlue(b);
                                    }
                                }
                                p.setNimi(palveluMuokattuNimi);
                                p.setTyyppi(Integer.parseInt(palveluMuokattuTyyppi));
                                p.setKuvaus(palveluMuokattuKuvaus);
                                p.setHinta(Double.parseDouble(palveluMuokattuHinta));
                                p.setAlv(Double.parseDouble(palveluMuokattuAlv));
                                errMsgM.setText("Muutokset tallennettu");
                                errMsgM.setFill(Color.GREEN);
                                i.setText("Palvelun nimi: " + p.getNimi());
                               
                            }catch(Exception err){
                                System.out.println(err);
                                errMsgM.setText("Muutosten tallennuksessa virhe!");
                            }
                        }
                    }
                });    
        }   
    }
}
return palvelutBox;
    }
 
    
    //UUSI MÖKIT SIVU
    private ScrollPane createMokitSivu(){
        ScrollPane sp = new ScrollPane();
        VBox mokitSivu = new VBox();
        sp.setFitToWidth(true);
        //sp.setFitToHeight(true);
        sp.setContent(mokitSivu);
        mokitSivu.setStyle("-fx-background-color:#fff;");
        mokitSivu.setAlignment(Pos.CENTER);
        mokitSivu.setPadding(new Insets(10,0,0,0));
        Text mokitTitle = new Text("Mökit");
        mokitTitle.setStyle("-fx-font: 24 arial;");
        mokitSivu.getChildren().add(mokitTitle);

        HBox mokitMenu = new HBox(10);
        mokitMenu.setAlignment(Pos.CENTER);
        mokitMenu.setPadding(new Insets(10, 0, 10, 0));
        Button uusiMokki = new Button("Luo uusi mökki");
        Button listMokit = new Button("Mökkilista");

        mokitMenu.getChildren().add(listMokit);
        mokitMenu.getChildren().add(uusiMokki);
        mokitSivu.getChildren().add(mokitMenu);
        
        GridPane mokitHaeLomake = new GridPane();
        mokitHaeLomake.setHgap(8);
        mokitHaeLomake.setVgap(4);
        mokitHaeLomake.setPadding(new Insets(15, 0, 10, 0));
        mokitHaeLomake.setAlignment(Pos.CENTER);
        

        //LUO UUSI MÖKKI NÄKYMÄ
        GridPane mokitLuo = new GridPane();
        mokitLuo.setAlignment(Pos.CENTER);
        mokitLuo.setPadding(new Insets(15,0,15,0));
        mokitLuo.setHgap(6);
        mokitLuo.setVgap(11);
        Label errMsgU = new Label("");
        errMsgU.setTextFill(Color.RED);


        // Label mokkiIdUusi = new Label("Mökin id:"); //TULEEKO AUTOMAATTISESTI?
        Label mokkiNimiUusiLabel = new Label("Mökin nimi: ");
        Label mokkiKatuosoiteUusi = new Label("Katuosoite: ");
        Label mokkiAlueUusi = new Label("Alue: ");
        Label mokkiPostinroUusi = new Label("Postinro: "); 
        Label mokkiHintaUusi = new Label("Hinta: ");
        Label mokkiHenklkmUusi = new Label("Henkilömäärä: ");
        Label mokkiKuvausUusi = new Label("Kuvaus: ");
        Label mokkiVarusteluUusi = new Label("Varustelu: ");
        
        TextField mokkiNimiUusiTxtField = new TextField();
        TextField mokkiKatuosoiteUusiTxtField = new TextField();

   
        setAlueet(getAlueet());

        ComboBox<String> alueUusiCB = new ComboBox<String>();
        alueUusiCB.setPromptText("Valitse alue.");
        for(Alue a : alueet){
            String alueNimiCB = a.getNimi();
            alueUusiCB.getItems().add(alueNimiCB);
        }
        TextField mokkiPostinumeroUusiTxtField = new TextField();
        TextField mokkiHintaUusiTxtField = new TextField();
        TextField mokkiHenklkmUusiTxtField = new TextField();
        TextField mokkiKuvausUusiTxtField = new TextField();
        TextField mokkiVarusteluUusiTxtField = new TextField();
        
        /**
         * Lisätään yllä luodut näkymään
         */
        mokitLuo.add(errMsgU, 0,0);

        mokitLuo.add(mokkiNimiUusiLabel, 0,1);
        mokitLuo.add(mokkiNimiUusiTxtField, 0,2);

        mokitLuo.add(mokkiAlueUusi, 1, 1);
        mokitLuo.add(alueUusiCB, 1, 2);

        mokitLuo.add(mokkiKatuosoiteUusi, 0,3);
        mokitLuo.add(mokkiKatuosoiteUusiTxtField, 0,4);

        mokitLuo.add(mokkiPostinroUusi, 1, 3);
        mokitLuo.add(mokkiPostinumeroUusiTxtField, 1, 4);
    
        mokitLuo.add(mokkiHintaUusi, 0,5);
        mokitLuo.add(mokkiHintaUusiTxtField, 0,6);

        mokitLuo.add(mokkiHenklkmUusi, 1,5);
        mokitLuo.add(mokkiHenklkmUusiTxtField, 1,6);

        mokitLuo.add(mokkiKuvausUusi, 0,7);
        mokitLuo.add(mokkiKuvausUusiTxtField, 0,8);

        mokitLuo.add(mokkiVarusteluUusi, 1,7);
        mokitLuo.add(mokkiVarusteluUusiTxtField, 1,8);
    
        Button tallennaMokkiBtn = new Button("Tallenna");
        mokitLuo.add(tallennaMokkiBtn, 1,9);
        
        uusiMokki.setOnAction(e -> {
            mokitSivu.getChildren().clear();
            mokitSivu.getChildren().add(mokitTitle);
            mokitSivu.getChildren().add(mokitMenu);
            mokitSivu.getChildren().add(mokitLuo);
        });

        listMokit.setOnAction(e -> {
            mokitSivu.getChildren().clear();
            mokitSivu.getChildren().add(mokitTitle);
            mokitSivu.getChildren().add(mokitMenu);
            mokitSivu.getChildren().add(mokitHaeLomake);
            mokitSivu.getChildren().add(createMokitBox(mokit));
        });

        
        tallennaMokkiBtn.setOnAction(e -> {
            int errors = 0;
            String mokkiNimiuusi = mokkiNimiUusiTxtField.getText();
            String alueNimi = alueUusiCB.getValue();
            int mokkiUusiAlueId = 0;
            for(Alue a : alueet){
                if(a.getNimi() == alueUusiCB.getValue()){
                    mokkiUusiAlueId = a.getAlueId();
                }
            }
            String mokkiKatuosoite = mokkiKatuosoiteUusiTxtField.getText();
            String mokkiPostinro = mokkiPostinumeroUusiTxtField.getText();
            String mokkiHinta = mokkiHintaUusiTxtField.getText();
            String mokkiHenkLkm = mokkiHenklkmUusiTxtField.getText();
            String mokkiKuvaus = mokkiKuvausUusiTxtField.getText();
            String mokkiVarustelu = mokkiVarusteluUusiTxtField.getText();
            //ALUE
            //POSTINUMERO
            mokkiNimiUusiLabel.setTextFill(Color.BLACK);
            mokkiPostinroUusi.setTextFill(Color.BLACK);
            mokkiKatuosoiteUusi.setTextFill(Color.BLACK);
            mokkiHintaUusi.setTextFill(Color.BLACK);
            mokkiHenklkmUusi.setTextFill(Color.BLACK);
            mokkiKuvausUusi.setTextFill(Color.BLACK);
            mokkiVarusteluUusi.setTextFill(Color.BLACK);
            if(mokkiNimiuusi.length() == 0){
                errors += 1;
                mokkiNimiUusiLabel.setTextFill(Color.RED);
            }
            if(mokkiKatuosoite.length() == 0){
                errors += 1;
                mokkiKatuosoiteUusi.setTextFill(Color.RED);
            }
            if(mokkiHinta.length() == 0){
                errors += 1;
                mokkiHintaUusi.setTextFill(Color.RED);
            }
            if(mokkiHenkLkm.length() == 0){
                errors += 1;
                mokkiHenklkmUusi.setTextFill(Color.RED);
            }
            if(mokkiKuvaus.length() == 0){
                errors += 1;
                mokkiKuvausUusi.setTextFill(Color.RED);
            }
            if(mokkiVarustelu.length() == 0){
                errors += 1;
                mokkiVarusteluUusi.setTextFill(Color.RED);
            }
            
    
            try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);

            String sql = "INSERT INTO mokki values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, null);
            pstmt.setInt(2, mokkiUusiAlueId);
            pstmt.setString(3, mokkiPostinro);
            pstmt.setString(4, mokkiNimiuusi);
            pstmt.setString(5, mokkiKatuosoite);
            pstmt.setDouble(6, Double.parseDouble(mokkiHinta));
            pstmt.setString(7, mokkiKuvaus);
            pstmt.setInt(8, Integer.parseInt(mokkiHenkLkm));
            pstmt.setString(9, mokkiVarustelu);
       
            pstmt.executeUpdate();
            
            conn.close();
            
            mokkiNimiUusiTxtField.setText("");
            alueUusiCB.setPromptText("Valitse alue.");
            mokkiKatuosoiteUusiTxtField.setText("");
            mokkiHintaUusiTxtField.setText("");
            mokkiHenklkmUusiTxtField.setText("");
            mokkiKuvausUusiTxtField.setText("");
            mokkiVarusteluUusiTxtField.setText("");
                    
            setMokit(getMokit());

            errMsgU.setText("Mökki tallennettu");
        }catch(Exception err){
            System.out.println(err);
            errMsgU.setText("Mökin tallennuksessa virhe!");
        }
        });
        
        mokitSivu.getChildren().add(mokitHaeLomake);
       
        mokitSivu.getChildren().add(createMokitBox(mokit));
        
    return sp;  
}
    

    private VBox createMokitBox(ArrayList<Mokki> param){
        VBox mokitBox = new VBox();
        setAlueet(getAlueet());
        mokitBox.setPadding(new Insets(10,10,10,10));
        if(param.isEmpty()){
            Text noMokki = new Text("Mökkejä ei löytynyt.");
            mokitBox.getChildren().add(noMokki);
        }else{
            for(Mokki m : param){
                
                String n = "Mökin nimi: " + m.getMokkinimi();
                VBox mmTiedot = new VBox();
                Text errMsgM = new Text();
                errMsgM.setFill(Color.RED);
                
                mmTiedot.getChildren().add(errMsgM);
                GridPane mTiedot = new GridPane();
                mTiedot.setHgap(5);
                mTiedot.setVgap(6);
                mTiedot.setPadding(new Insets(15, 5, 15, 5));
                if(m != null){
                    Label mokkiNimiLabel = new Label("Mökin nimi: ");
                    TextField mokkiNimiTxtField = new TextField(m.getMokkinimi());
                    mTiedot.add(mokkiNimiLabel, 0 ,0);
                    mTiedot.add(mokkiNimiTxtField, 0,1);
                    
                    Label mokkiAlue = new Label("Alue: ");
                    ComboBox mokkiAlueCB = new ComboBox<String>();
                    
                    mokkiAlueCB.setPromptText(m.getAlue().getNimi());
                    for(Alue a : alueet){
                        String alueNimi = a.getNimi();
                        mokkiAlueCB.getItems().add(alueNimi);
                    }

                    mTiedot.add(mokkiAlue, 1, 0);
                    mTiedot.add(mokkiAlueCB, 1, 1);
                    
                    Label mokkiOsoite = new Label("Katuosoite: ");
                    TextField mokkiOsoiteTxtField = new TextField(m.getKatuosoite());
                    mTiedot.add(mokkiOsoite, 0, 2);
                    mTiedot.add(mokkiOsoiteTxtField, 0, 3);

                    Label mokkiPostinro = new Label("Postinumero: ");
                    TextField mokkiPostinumeroTxtField = new TextField(m.getPosti().getPostinro());
                    mTiedot.add(mokkiPostinro, 1, 2);
                    mTiedot.add(mokkiPostinumeroTxtField, 1, 3);

                    Label mokkiHinta = new Label("Hinta: ");
                    TextField mokkiHintaTxtField = new TextField(String.valueOf(m.gethinta()));
                    mTiedot.add(mokkiHinta,0, 4);
                    mTiedot.add(mokkiHintaTxtField, 0, 5);

                    Label mokkiHenkmaara = new Label("Henkilömäärä: ");
                    TextField mokkiHenkmaaraTxtField = new TextField(String.valueOf(m.getHenkilomaara()));
                    mTiedot.add(mokkiHenkmaara, 1, 4);
                    mTiedot.add(mokkiHenkmaaraTxtField, 1, 5);

                    Label mokkiKuvaus = new Label("Kuvaus: ");
                    TextField mokkiKuvausTxtField = new TextField(m.getKuvaus());
                    mTiedot.add(mokkiKuvaus, 0, 6);
                    mTiedot.add(mokkiKuvausTxtField, 0, 7);

                    Label mokkiVarustelu = new Label("Mökin varustelu: ");
                    TextField mokkiVarusteluTxtField = new TextField(m.getVarustelu());
                    mTiedot.add(mokkiVarustelu, 1, 6);
                    mTiedot.add(mokkiVarusteluTxtField,1, 7);
                
                    mmTiedot.getChildren().add(mTiedot);
                    TitledPane i = new TitledPane(n, mmTiedot);
                    i.setExpanded(false);
                    mokitBox.getChildren().add(i);

                    Button saveEditBtn = new Button("Tallenna");
                    mTiedot.add(saveEditBtn, 0,8);
                    Button deleteMokkiBtn = new Button("Poista");
                    mTiedot.add(deleteMokkiBtn, 1,8);
                

                    deleteMokkiBtn.setOnAction(e -> {
                        try{
                            
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                            String sql = "DELETE FROM mokki WHERE mokki_id = ?";
                            PreparedStatement poista = conn.prepareStatement(sql);
                            
                            poista.setInt(1, m.getMokkiId());
                            poista.executeUpdate();
                            conn.close();
                            mokitBox.getChildren().remove(i);
                            for(Mokki m2 : mokit){
                                if(m2.getMokkiId() == m.getMokkiId()){
                                    mokit.remove(m);
                                    break;
                                }
                            }
                        }catch(Exception err){
                            System.out.println(err);
                        }
                    });
                
                saveEditBtn.setOnAction(e ->{
                    int errors = 0;

            
                    String mokkiMuokattuNimi = mokkiNimiTxtField.getText();
                    int mokkiMuokattuAlueId = 0;
                    for(Alue a : alueet){
                        if(a.getNimi() == mokkiAlueCB.getValue()){
                            mokkiMuokattuAlueId = a.getAlueId();
                        }
                    String mokkiMuokattuOsosite = mokkiOsoiteTxtField.getText();
                    String mokkiMuokattuPostinro = mokkiPostinumeroTxtField.getText();
                    String mokkiMuokattuHinta = mokkiHintaTxtField.getText();
                    String mokkiMuokattuHenkmaara = mokkiHenkmaaraTxtField.getText();
                    String mokkiMuokattuKuvaus = mokkiKuvausTxtField.getText();
                    String mokkiMuokattuVarustelu = mokkiVarusteluTxtField.getText(); 
                    
                    mokkiNimiLabel.setTextFill(Color.BLACK);
                    mokkiAlue.setTextFill(Color.BLACK);
                    mokkiOsoite.setTextFill(Color.BLACK);
                    mokkiPostinro.setTextFill(Color.BLACK);
                    mokkiHinta.setTextFill(Color.BLACK);
                    mokkiHenkmaara.setTextFill(Color.BLACK);
                    mokkiKuvaus.setTextFill(Color.BLACK);
                    mokkiVarustelu.setTextFill(Color.BLACK);

                    if(mokkiMuokattuNimi.length() == 0){
                        errors += 1;
                        mokkiNimiLabel.setTextFill(Color.RED);
                    }
                    if(mokkiMuokattuOsosite.length() == 0){
                        errors += 1;
                        mokkiOsoite.setTextFill(Color.RED);
                    }
                    if(mokkiMuokattuPostinro.length() == 0){
                        errors += 1;
                        mokkiPostinro.setTextFill(Color.RED);
                    }else if(mokkiMuokattuPostinro.equals("00000")){
                        errors += 1001;
                        mokkiPostinro.setTextFill(Color.RED);
                    }
                    if(mokkiMuokattuHinta.length() == 0){
                        errors += 1;
                        mokkiHinta.setTextFill(Color.RED);
                    }
                    if(mokkiMuokattuHenkmaara.length() == 0){
                        errors += 1;
                        mokkiHenkmaara.setTextFill(Color.RED);
                    }
                    if(mokkiMuokattuKuvaus.length() == 0){
                        errors += 1;
                        mokkiKuvaus.setTextFill(Color.RED);
                    }
                    if(mokkiMuokattuVarustelu.length() == 0){
                        errors += 1;
                        mokkiVarustelu.setTextFill(Color.RED);
                    }
    
                    if(errors == 0){
                            try{

                                Class.forName("com.mysql.cj.jdbc.Driver");
                                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);

                                String sql =  "UPDATE mokki SET alue_id = ?, postinro = ?, mokkinimi = ?, katuosoite = ?, hinta = ?, kuvaus = ?, henkilomaara = ?, varustelu = ? WHERE mokki_id = ?";
                                PreparedStatement update = conn.prepareStatement(sql);
                                
                                update.setInt(1, mokkiMuokattuAlueId);
                                update.setString(2, mokkiMuokattuPostinro);
                                update.setString(3, mokkiMuokattuNimi);
                                update.setString(4, mokkiMuokattuOsosite);
                                update.setDouble(5, Double.parseDouble(mokkiMuokattuHinta));
                                update.setString(6, mokkiMuokattuKuvaus);
                                update.setString(7, mokkiMuokattuHenkmaara);
                                update.setString(8, mokkiMuokattuVarustelu);
                                update.setInt(9, m.getMokkiId());
                        
                                update.executeUpdate();
                                
                                conn.close();

                                for(Alue b: alueet){
                                    if(b.getAlueId() == mokkiMuokattuAlueId){
                                        m.setAlue(b);
                                    }
                                }
                                
                                for(Posti p: postit){
                                    if(p.getPostinro() == mokkiMuokattuPostinro){
                                        m.setPosti(p);
                                    }
                                }
                                m.setMokkinimi(mokkiMuokattuNimi);
                                m.setKatuosoite(mokkiMuokattuOsosite);
                                m.setHinta(Double.parseDouble(mokkiMuokattuHinta));
                                m.setKuvaus(mokkiMuokattuKuvaus);
                                m.setHenkilomaara(Integer.parseInt(mokkiMuokattuHenkmaara));
                                m.setVarustelu(mokkiMuokattuVarustelu);
                                errMsgM.setFill(Color.GREEN);
                                errMsgM.setText("Muutokset tallennettu");
                                i.setText("Mokin nimi: " + m.getMokkinimi());
                               
                            }catch(Exception err){
                                System.out.println(err);
                                errMsgM.setFill(Color.RED);
                                errMsgM.setText("Muutosten tallennuksessa virhe!");
                            }
                        }
                    }
                
            });    
        }   
    }
}
return mokitBox;
    }

    /**
     * Luodaan alueet näkymä
     */
    private ScrollPane createAlueetSivu(){
        ScrollPane sp = new ScrollPane();
        VBox alueetSivu = new VBox();
        sp.setFitToWidth(true);
        //sp.setFitToHeight(true);
        sp.setContent(alueetSivu);
        alueetSivu.setStyle("-fx-background-color:#fff;");
        alueetSivu.setAlignment(Pos.CENTER);
        alueetSivu.setPadding(new Insets(10,0,0,0));
        Text alueetTitle = new Text("Alueet");
        alueetTitle.setStyle("-fx-font: 24 arial;");
        alueetSivu.getChildren().add(alueetTitle);

        HBox alueetMenu = new HBox(10);
        alueetMenu.setAlignment(Pos.CENTER);
        alueetMenu.setPadding(new Insets(10, 0, 10, 0));

        Button uusiAlue = new Button("Luo uusi alue");
        Button listAlueet = new Button("Aluelista");

        alueetMenu.getChildren().add(listAlueet);
        alueetMenu.getChildren().add(uusiAlue);
        alueetSivu.getChildren().add(alueetMenu);
        
        GridPane alueetHaeLomake = new GridPane();
        alueetHaeLomake.setHgap(8);
        alueetHaeLomake.setVgap(4);
        alueetHaeLomake.setPadding(new Insets(15, 0, 10, 0));
        alueetHaeLomake.setAlignment(Pos.CENTER);
        

        //LUO UUSI ALUE NÄKYMÄ
        GridPane alueetLuo = new GridPane();
        alueetLuo.setAlignment(Pos.CENTER);
        alueetLuo.setPadding(new Insets(15,0,15,0));
        alueetLuo.setHgap(6);
        alueetLuo.setVgap(11);
        Label errMsgU = new Label("");
        errMsgU.setTextFill(Color.RED);

        Label alueNimiUusiLabel = new Label("Alueen nimi: ");
        TextField alueNimiUusiTxtField = new TextField();
        
        
        /**
         * Lisätään yllä luodut näkymään
         */
        alueetLuo.add(errMsgU, 0,0);

        alueetLuo.add(alueNimiUusiLabel, 0,1);
        alueetLuo.add(alueNimiUusiTxtField, 0,2);


        Button tallennaAlueBtn = new Button("Tallenna");
        alueetLuo.add(tallennaAlueBtn, 1,9);
        
        uusiAlue.setOnAction(e -> {
            alueetSivu.getChildren().clear();
            alueetSivu.getChildren().add(alueetTitle);
            alueetSivu.getChildren().add(alueetMenu);
            alueetSivu.getChildren().add(alueetLuo);
        });

        listAlueet.setOnAction(e -> {
            alueetSivu.getChildren().clear();
            alueetSivu.getChildren().add(alueetTitle);
            alueetSivu.getChildren().add(alueetMenu);
            alueetSivu.getChildren().add(alueetHaeLomake);
            alueetSivu.getChildren().add(createAlueetBox(alueet));
        });
        
        tallennaAlueBtn.setOnAction(e -> {
            
            String alueNimiuusi = alueNimiUusiTxtField.getText();
            
            alueNimiUusiLabel.setTextFill(Color.BLACK);
    
            if(alueNimiuusi.length() == 0){
                
                alueNimiUusiLabel.setTextFill(Color.RED);
                errMsgU.setText("Tarkista tiedot!");
            }else{
            try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);

            String sql = "INSERT INTO alue VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, null);
            pstmt.setString(2, alueNimiuusi);
       
            pstmt.executeUpdate();
            
            conn.close();
            
            alueNimiUusiTxtField.setText("");
                    
            setAlueet(getAlueet());
            errMsgU.setTextFill(Color.GREEN);
            errMsgU.setText("Alue tallennettu");
                }catch(Exception err){
                errMsgU.setText("Alueen tallennuksessa virhe!");
                System.out.println(err);
                
                }
            } 
        });
        
        alueetSivu.getChildren().add(alueetHaeLomake);
       
        alueetSivu.getChildren().add(createAlueetBox(alueet));
        
    return sp;  
}
    

    private VBox createAlueetBox(ArrayList<Alue>param){
        VBox alueetBox = new VBox();
        alueetBox.setPadding(new Insets(10,10,10,10));
        if(param.isEmpty()){
            Text noAlue = new Text("Alueita ei löytynyt.");
            alueetBox.getChildren().add(noAlue);
        }else{
            for(Alue al : param){
                
                String n = "Alueen nimi: " + al.getNimi();
                VBox alTiedot = new VBox();
                Text errMsgM = new Text();
                errMsgM.setFill(Color.RED);
                
                alTiedot.getChildren().add(errMsgM);
                GridPane alueTiedot = new GridPane();
                alueTiedot.setHgap(10);
                alueTiedot.setVgap(6);
                alueTiedot.setPadding(new Insets(15, 5, 15, 5));
                if(al != null){
                   
                
                    Label alueNimiLabel = new Label("Alueen nimi: ");
                    TextField alueNimiTxtField = new TextField(al.getNimi());
                    alueTiedot.add(alueNimiLabel, 1, 0);
                    alueTiedot.add(alueNimiTxtField, 1, 1);
                    
                    alTiedot.getChildren().add(alueTiedot);
                    TitledPane i = new TitledPane(n, alTiedot);
                    i.setExpanded(false);
                    alueetBox.getChildren().add(i);

                    Button saveEditBtn = new Button("Tallenna");
                    alueTiedot.add(saveEditBtn, 0,8);
                    Button deleteAlueBtn = new Button("Poista");
                    alueTiedot.add(deleteAlueBtn, 1,8);
                
    
                    deleteAlueBtn.setOnAction(e -> {
                        try{
                            
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                            String sql = "DELETE FROM alue WHERE alue_id = ?";
                            PreparedStatement poista = conn.prepareStatement(sql);
                            
                            poista.setInt(1, al.getAlueId());
                            poista.executeUpdate();
                            conn.close();
                            alueetBox.getChildren().remove(i);
                            for(Alue al2 : alueet){
                                if(al2.getAlueId() == al.getAlueId()){
                                    mokit.remove(al);
                                    break;
                                }
                            }
                        }catch(Exception err){
                            errMsgM.setText("Alueen poistossa virhe!");
                            System.out.println(err);
                        }
                        setAlueet(getAlueet());
                    });
                
                saveEditBtn.setOnAction(e ->{
                    int errors = 0;
                    String alueMuokattuNimi = alueNimiTxtField.getText();
                
                    alueNimiLabel.setTextFill(Color.BLACK);
                   
                    if(alueMuokattuNimi.length() == 0){
                        errors += 1;
                        alueNimiLabel.setTextFill(Color.RED);
                    } else{
                            try{

                                Class.forName("com.mysql.cj.jdbc.Driver");
                                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);

                                String sql =  "UPDATE alue SET nimi = ? WHERE alue_id = ?";
                                PreparedStatement update = conn.prepareStatement(sql);
                                
                                update.setString(1, alueMuokattuNimi);
                                update.setInt(2, al.getAlueId());
                        
                                update.executeUpdate();
                                
                                conn.close();

                                al.setNimi(alueMuokattuNimi);
                                setAlueet(getAlueet());
                             
                                errMsgM.setText("Muutokset tallennettu");
                                errMsgM.setFill(Color.GREEN);
                                i.setText("Alueen nimi: " + al.getNimi());
                               
                            }catch(Exception err){
                                System.out.println(err);
                                errMsgM.setText("Muutosten tallennuksessa virhe!");
                            }
                        }
            });    
        }   
}

    }    

    return alueetBox;
}

private ScrollPane createRaportitSivu(){
    ScrollPane sp = new ScrollPane();
    VBox raportitSivu = new VBox();
    sp.setFitToWidth(true);
    //sp.setFitToHeight(true);
    sp.setContent(raportitSivu);
    raportitSivu.setStyle("-fx-background-color:#fff;");
    raportitSivu.setAlignment(Pos.CENTER);
    raportitSivu.setPadding(new Insets(10,0,0,0));
    Text raportitTitle = new Text("Raportit");
    raportitTitle.setStyle("-fx-font: 24 arial;");
    raportitSivu.getChildren().add(raportitTitle);

    GridPane raportitMenu = new GridPane();
    raportitMenu.setAlignment(Pos.CENTER);
    raportitMenu.setPadding(new Insets(10, 0, 10, 0));

    Label errMsgU = new Label("");
    errMsgU.setTextFill(Color.RED);
    raportitMenu.add(errMsgU, 0, 6);


    raportitSivu.getChildren().add(raportitMenu);

    //COMBOBOX TÄHÄN
    ComboBox<String> raporttiCB = new ComboBox<String>();
    raporttiCB.setPromptText("Valitse raportti.");
    raporttiCB.getItems().add("Majoittuminen");
    raporttiCB.getItems().add("Ostetut lisäpalvelut");

    
    ComboBox<String> alueetCB = new ComboBox<String>();
    alueetCB.setPromptText("Valitse alue.");
    for(Alue a : alueet){
        String alueNimiCB = a.getNimi();
        alueetCB.getItems().add(alueNimiCB);
    }
        
    DatePicker alkupvmDP = new DatePicker();
    DatePicker loppupvmDP = new DatePicker();

    Button tulostaRaportti = new Button("Tulosta raportti");
    //KALENTERI
    //HAKU BUTTON
    /**
     * Lisää ylläolevat tämän avulla
     */
    raportitMenu.add(raporttiCB, 0, 0);
    raportitMenu.add(alueetCB, 1, 0);
    raportitMenu.add(alkupvmDP, 0, 3);
    raportitMenu.add(loppupvmDP, 1, 3);
    raportitMenu.add(tulostaRaportti, 0, 5);

    GridPane raportitLomake = new GridPane();
    raportitLomake.setHgap(8);
    raportitLomake.setVgap(4);
    raportitLomake.setPadding(new Insets(15, 0, 10, 0));
    raportitLomake.setAlignment(Pos.CENTER);

    raportitSivu.getChildren().add(raportitLomake);
    
    tulostaRaportti.setOnAction(e -> {
        try{
          if(alkupvmDP.getValue().isAfter(loppupvmDP.getValue())){
            errMsgU.setText("Tarkista alku- ja loppupäivämäärät!");
        }else{
            
        for(Mokki m: mokit){
            m.nollaaVaraukset();
        }
        for(Palvelu p: palvelut){
            p.nollaaOstot();
        }
        raportitSivu.getChildren().clear();
        raportitSivu.getChildren().add(raportitTitle);
        raportitSivu.getChildren().add(raportitMenu);
        raportitSivu.getChildren().add(createRaportitBox(mokit, varaukset, alueet , palvelut, alueetCB.getValue(), raporttiCB.getValue(), alkupvmDP.getValue(), loppupvmDP.getValue()));
    }
    }catch(NullPointerException ex){
        errMsgU.setText("Valitse haluttu tarkasteluaikaväli!");
    }
    });

    return sp;
}


private VBox createRaportitBox(ArrayList<Mokki> mokit, ArrayList<Varaus> varaukset, ArrayList<Alue> alueet, ArrayList<Palvelu> palvelut, String valittuAlue, String valittuRaportti, LocalDate alkuPvm, LocalDate loppuPvm){
    VBox raportitBox = new VBox();
    raportitBox.setPadding(new Insets(10,10,10,10));
   
        
        if(valittuRaportti == "Majoittuminen"){

            TableView taulukko = new TableView<Mokki>();
            taulukko.setEditable(false);
            taulukko.setPlaceholder(new Label("Alueella ei ole mökkejä."));
            TableColumn mokkiNimiCol = new TableColumn<Mokki, String>("Mökin nimi");
            TableColumn varaustenLkmCol = new TableColumn<Mokki, Integer>("Varausten lkm");

            ArrayList<Mokki> alueenMokit = new ArrayList<Mokki>();
            for(Mokki m: mokit){
                if(m.getAlue().getNimi().equals(valittuAlue)){
                    if(alueenMokit.contains(m)){
                                
                    }else{
                        alueenMokit.add(m);
                    }
                }
                for(Varaus v: varaukset){
                    if(v.getMokki().getAlue().getNimi().equals(valittuAlue)){
                        LocalDate pvmAlkuMuunnos = v.getVarattuAlkuPvm().toLocalDateTime().toLocalDate();
                        LocalDate pvmLoppuMuunnos = v.getVarattuLoppuPvm().toLocalDateTime().toLocalDate();
                       if (m.getMokkinimi().equals(v.getMokki().getMokkinimi()) && (pvmAlkuMuunnos.isEqual(alkuPvm) || pvmAlkuMuunnos.isAfter(alkuPvm) && (pvmLoppuMuunnos.isEqual(loppuPvm) || pvmLoppuMuunnos.isBefore(loppuPvm) ))){
                            m.varausten_lkm += 1;
                        }
                    }                    
                }
            } 
            
            mokkiNimiCol.setCellValueFactory(new PropertyValueFactory<Mokki, String>("mokkinimi"));
            varaustenLkmCol.setCellValueFactory(new PropertyValueFactory<Mokki, Integer>("varausten_lkm"));

            taulukko.getColumns().addAll(mokkiNimiCol, varaustenLkmCol);
           
            taulukko.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            for(Mokki m2: alueenMokit){
                taulukko.getItems().add(m2);
            };

            raportitBox.getChildren().add(taulukko);


        }else if(valittuRaportti == "Ostetut lisäpalvelut"){
            TableView taulukko = new TableView<Palvelu>();
            taulukko.setEditable(false);
            taulukko.setPlaceholder(new Label("Alueella ei ole palveluja."));
            TableColumn palveluNimiCol = new TableColumn<Palvelu, String>("Palvelun nimi");
            TableColumn ostojenLkmCol = new TableColumn<Palvelu, Integer>("Ostojen lkm");

            ArrayList<Palvelu> alueenPalvelut = new ArrayList<Palvelu>();
            for(Palvelu p: palvelut){
                if(p.getAlue().getNimi().equals(valittuAlue)){
                    if(alueenPalvelut.contains(p)){

                    }else{
                        alueenPalvelut.add(p);
                    }
                }
                for(Varaus v: varaukset){
                    for(VarauksenPalvelu vPalvelu: v.getVarauksenPalvelut()){
                        if(vPalvelu.getPalvelu().getAlue().getNimi().equals(valittuAlue)){
                            LocalDate pvmAlkuMuunnos = v.getVarattuAlkuPvm().toLocalDateTime().toLocalDate();
                            LocalDate pvmLoppuMuunnos = v.getVarattuLoppuPvm().toLocalDateTime().toLocalDate();
                            if ((p.getNimi().equals(vPalvelu.getPalvelu().getNimi())) && (pvmAlkuMuunnos.isEqual(alkuPvm) || pvmAlkuMuunnos.isAfter(alkuPvm) && (pvmLoppuMuunnos.isEqual(loppuPvm) || pvmLoppuMuunnos.isBefore(loppuPvm) ))){
                                p.lisaaOsto();
                            }
                        }
                    }
                }
                
            }

            palveluNimiCol.setCellValueFactory(new PropertyValueFactory<Palvelu, String>("nimi"));
            ostojenLkmCol.setCellValueFactory(new PropertyValueFactory<Palvelu, Integer>("osto_lkm"));

            taulukko.getColumns().addAll(palveluNimiCol, ostojenLkmCol);
           
            taulukko.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


            for(Palvelu p2: alueenPalvelut){
                taulukko.getItems().add(p2);
            };

            raportitBox.getChildren().add(taulukko);
        }else{
            
        }
       

    return raportitBox;
}
}