import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class App extends Application{
    String HOST_USER = "root";
    String HOST_PSWD = "root";
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
                varaus.setVarattuAlkuPvm(rs.getDate(6));
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
                for(Alue a : alueet){
                    if(a.getAlueId() == rs.getInt(2)){
                        palvelu.setAlue(a);
                        break;
                    }
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
                palvelu.setLkm(rs.getInt(3));
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
        //@Niko Lopeta nappi, joka sulkee ohjelman
        Button lopetaNappi = new Button("Lopeta");

        topPane.getChildren().add(etusivuNappi);
        topPane.getChildren().add(asiakasNappi);
        topPane.getChildren().add(laskuNappi);
        topPane.getChildren().add(varausNappi);
        topPane.getChildren().add(palveluNappi);
        topPane.getChildren().add(mokkiNappi);
        //@Niko 
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
        
        /*@Niko
        Toiminnallisuus lopetaNappiin
        Sulkee ohjelman
        */
        lopetaNappi.setOnAction(e ->{
            System.exit(0);
        });

        mainScene = new Scene(mainPanel);

        alkuIkkuna.setTitle("Mökkiläiset");
        //alkuIkkuna.setMaximized(true);
        alkuIkkuna.setScene(mainScene);
        alkuIkkuna.setHeight(800);
        alkuIkkuna.setWidth(650);
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
        asiakkaatLuo.add(postinroUusi, 0,3);
        asiakkaatLuo.add(postinroTFU, 1,3);
        asiakkaatLuo.add(postiTUusi, 0,4);
        asiakkaatLuo.add(postiTTFU, 1,4);
        asiakkaatLuo.add(lahiosoiteUusi, 0,5);
        asiakkaatLuo.add(lahiosoiteTFU, 1,5);
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
        for(Asiakas a : asiakkaat){
            String name = a.getEtunimi() + " " + a.getSukunimi();
            nimiCB.getItems().add(name);
        }
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

            if(nimiCB.getValue() == ""){
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

                boolean alkuFlag = true;
                if(alku != null){
                    java.sql.Date alkuTemp = Date.valueOf(alku);
                }
                boolean loppuFlag = true;
                if(loppu != null){
                    java.sql.Date loppuTemp = Date.valueOf(loppu);
                }
                
                if(matchFound && matchFound2 && matchFound3){
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
                if(v.getAsiakas() != null){
                    Label etunimiM = new Label("Etunimi: ");
                    TextField etunimiTM = new TextField(v.getAsiakas().getEtunimi());
                    vTiedot.add(etunimiM, 0,0);
                    vTiedot.add(etunimiTM, 1,0);
                    Label sukunimiM = new Label("Sukunimi: ");
                    TextField sukunimiTM = new TextField(v.getAsiakas().getSukunimi());
                    vTiedot.add(sukunimiM, 2,0);
                    vTiedot.add(sukunimiTM, 3,0);
                    Label lahiosoiteM = new Label("Lähiosoite: ");
                    TextField lahiosoiteTM = new TextField(v.getAsiakas().getLahiOsoite());
                    vTiedot.add(lahiosoiteM, 0,1);
                    vTiedot.add(lahiosoiteTM, 1,1);
                    Label postinroM = new Label("Postinumero: ");
                    TextField postinroTM = new TextField(v.getAsiakas().getPosti().getPostinro());
                    if(postinroTM.getText().equals("00000")){
                        postinroM.setTextFill(Color.RED);
                    }
                    vTiedot.add(postinroM, 0,2);
                    vTiedot.add(postinroTM, 1,2);
                    Label postiTM = new Label("Toimipaikka: ");
                    TextField postiTTM = new TextField(v.getAsiakas().getPosti().getToimipaikka());
                    if(postiTTM.getText().equals("Rikki")){
                        postiTM.setTextFill(Color.RED);
                    }
                    vTiedot.add(postiTM, 2,2);
                    vTiedot.add(postiTTM, 3,2);
                    Label emailM = new Label("Email: ");
                    TextField emailTM = new TextField(v.getAsiakas().getEmail());
                    vTiedot.add(emailM, 0,3);
                    vTiedot.add(emailTM, 1,3);
                    Label puhM = new Label("Puhelinnro: ");
                    TextField puhTM = new TextField(v.getAsiakas().getPuhelinnro());
                    vTiedot.add(puhM, 2,3);
                    vTiedot.add(puhTM, 3,3);
                }else{

                }
                
                Button saveEditBtn = new Button("Tallenna");
                vTiedot.add(saveEditBtn, 1,4);
                Button deleteUserBtn = new Button("Poista");
                vTiedot.add(deleteUserBtn, 2,4);

                vvTiedot.getChildren().add(vTiedot);
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
            Label label = new Label(l.getLaskuId() + " " + l.getSumma());
            label.setPadding(new Insets(15, 5, 15, 5));
            String n = "Id: " + l.getLaskuId() + ", " + l.getSumma();
            TitledPane i = new TitledPane(n, label);
            i.setExpanded(false);

            laskutBox.getChildren().add(i);
        }
        laskutSivu.getChildren().add(laskutBox);

        return sp;
    }

    private ScrollPane createPalvelutSivu(){
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        //sp.setFitToHeight(true);
        VBox palvelutSivu = new VBox();
        sp.setContent(palvelutSivu);
        palvelutSivu.setStyle("-fx-background-color:#fff;");
        palvelutSivu.setAlignment(Pos.CENTER);
        palvelutSivu.setPadding(new Insets(10,0,0,0));
        Text palveluTitle = new Text("Palvelut");
        palveluTitle.setStyle("-fx-font: 24 arial;");
        palvelutSivu.getChildren().add(palveluTitle);
        VBox palvelutBox = new VBox();
        palvelutBox.setPadding(new Insets(10,10,10,10));
        for(Palvelu p : palvelut){
            Label label = new Label(p.getNimi() + " " + p.getKuvaus());
            label.setPadding(new Insets(15, 5, 15, 5));
            String n = "Id: " + p.getPalveluId() + ", " + p.getNimi();
            TitledPane i = new TitledPane(n, label);
            i.setExpanded(false);

            palvelutBox.getChildren().add(i);
        }
        palvelutSivu.getChildren().add(palvelutBox);

        return sp;
    }

    private ScrollPane createMokitSivu(){
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        //sp.setFitToHeight(true);
        VBox mokitSivu = new VBox();
        sp.setContent(mokitSivu);
        mokitSivu.setStyle("-fx-background-color:#fff;");
        mokitSivu.setAlignment(Pos.CENTER);
        mokitSivu.setPadding(new Insets(10,0,0,0));
        Text mokkiTitle = new Text("Mökit");
        mokkiTitle.setStyle("-fx-font: 24 arial;");
        mokitSivu.getChildren().add(mokkiTitle);
        VBox mokitBox = new VBox();
        mokitBox.setPadding(new Insets(10,10,10,10));
        for(Mokki m : mokit){
            Label label = new Label(m.getPosti().getPostinro() + ", " + m.getMokkinimi());
            label.setPadding(new Insets(15, 5, 15, 5));
            String n = m.getPosti().getPostinro() + ", " + m.getMokkinimi();
            TitledPane i = new TitledPane(n, label);
            i.setExpanded(false);

            mokitBox.getChildren().add(i);
        }
        mokitSivu.getChildren().add(mokitBox);

        return sp;
    }
}
