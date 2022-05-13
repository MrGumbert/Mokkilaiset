import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Desktop;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import javax.mail.Session;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application{
    String HOST_USER = "root";
    String HOST_PSWD = "Pattu";
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM asiakas ORDER BY etunimi");
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
                lasku.setViitenumero(rs.getString(3));
                lasku.setSumma(rs.getDouble(4));
                lasku.setAlv(rs.getDouble(5));
                lasku.setMaksettu(rs.getInt(6));
                lasku.setErapaiva(rs.getDate(7));
                lasku.setLaskupaiva(rs.getDate(8));
                lasku.setLaskutyyppi(rs.getString(9));
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
                int alueId = rs.getInt(2);
                if(alueId == 0){
                    palvelu.setAlue(null);
                }else{
                    for(Alue a : alueet){
                        if(a.getAlueId() == rs.getInt(2)){
                            palvelu.setAlue(a);
                            break;
                        }
                    }
                }
                palvelu.setNimi(rs.getString(3));
                palvelu.setTyyppi(rs.getString(4));
                palvelu.setKuvaus(rs.getString(5));
                palvelu.setHinta(rs.getDouble(6));
                palvelu.setAlv(rs.getDouble(7));
                temp.add(palvelu);
            }
            con.close();
            for(Alue a : alueet){
                for(Palvelu p : temp){
                    if(p.getAlue() != null){
                        if(a.getAlueId() == p.getAlue().getAlueId()){
                            a.addPalvelu(p);
                        }
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
        /**
         * @Niko
         * Lisätään napit yläosaan
         */ 
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
        Text etusivuTitle = new Text("Mökkiläiset");
        etusivuTitle.setStyle("-fx-font: 32 arial;");
        etusivu.getChildren().add(etusivuTitle);
        Text etusivuVersio = new Text("Versio 1.0");
        etusivuVersio.setStyle("-fx-font: 24 arial;");
        etusivu.getChildren().add(etusivuVersio);
        

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

        Label idHakuLabel = new Label("Hae asiakkaan id:llä: ");
        idHakuLabel.setTextFill(Color.BLACK);
        TextField idHakuTF = new TextField();
        Label taiHae = new Label("Tai hae hakuehdoilla");
        taiHae.setTextFill(Color.BLACK);
        Label etunimiHaku = new Label("Etunimi: ");
        etunimiHaku.setTextFill(Color.BLACK);
        Label sukunimiHaku = new Label("Sukunimi: ");
        sukunimiHaku.setTextFill(Color.BLACK);
        TextField etunimiTF = new TextField();
        TextField sukunimiTF = new TextField();
        asiakkaatHaeLomake.add(idHakuLabel, 0,0);
        asiakkaatHaeLomake.add(idHakuTF, 1, 0);
        asiakkaatHaeLomake.add(taiHae, 0,1);
        asiakkaatHaeLomake.add(etunimiHaku, 0,2);
        asiakkaatHaeLomake.add(etunimiTF, 0,3);
        asiakkaatHaeLomake.add(sukunimiHaku, 1,2);
        asiakkaatHaeLomake.add(sukunimiTF, 1,3);
        Button hakuBtn = new Button("Hae asiakasta");
        asiakkaatHaeLomake.add(hakuBtn, 0,4);
        GridPane.setColumnSpan(hakuBtn, 2);
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
        etunimiUusi.setTextFill(Color.BLACK);
        Label sukunimiUusi = new Label("Sukunimi: ");
        sukunimiUusi.setTextFill(Color.BLACK);
        Label postinroUusi = new Label("Postinro: ");
        postinroUusi.setTextFill(Color.BLACK);
        Label postiTUusi = new Label("Toimipaikka: ");
        postiTUusi.setTextFill(Color.BLACK);
        Label lahiosoiteUusi = new Label("Lähiosoite: ");
        lahiosoiteUusi.setTextFill(Color.BLACK);
        Label emailUusi = new Label("Email: ");
        emailUusi.setTextFill(Color.BLACK);
        Label puhelinnroUusi = new Label("Puhelinnro: ");
        puhelinnroUusi.setTextFill(Color.BLACK);
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

            errMsgU.setText("");
            errMsgU.setTextFill(Color.RED);

            etunimiTFU.setText("");
            sukunimiTFU.setText("");
            lahiosoiteTFU.setText("");
            postinroTFU.setText("");
            postiTTFU.setText("");
            emailTFU.setText("");
            puhelinnroTFU.setText("");
            errMsgU.setText("");
            postituslistaCBU.setSelected(false);

            etunimiUusi.setTextFill(Color.BLACK);
            sukunimiUusi.setTextFill(Color.BLACK);
            lahiosoiteUusi.setTextFill(Color.BLACK);
            postinroUusi.setTextFill(Color.BLACK);
            postiTUusi.setTextFill(Color.BLACK);
            emailUusi.setTextFill(Color.BLACK);
            puhelinnroUusi.setTextFill(Color.BLACK);
        });

        uusiAsiakas.setOnAction(e -> {
            asiakkaatSivu.getChildren().clear();
            asiakkaatSivu.getChildren().add(asiakasTitle);
            asiakkaatSivu.getChildren().add(asiakkaatMenu);
            asiakkaatSivu.getChildren().add(asiakkaatLuo);

            idHakuTF.setText("");
            etunimiTF.setText("");
            sukunimiTF.setText("");
        });

        hakuBtn.setOnAction(e -> {
            asiakkaatSivu.getChildren().clear();
            asiakkaatSivu.getChildren().add(asiakasTitle);
            asiakkaatSivu.getChildren().add(asiakkaatMenu);
            asiakkaatSivu.getChildren().add(asiakkaatHaeLomake);
            String hakuId = idHakuTF.getText();
            if(hakuId.length() != 0 || hakuId.trim().length() != 0){
                try {
                    int id = Integer.valueOf(hakuId);
                    ArrayList<Asiakas> temp = new ArrayList<Asiakas>();
                    for(Asiakas a : asiakkaat){
                        if(a.getAsiakasId() == id){
                            temp.add(a);
                        }
                    }
                    asiakkaatSivu.getChildren().add(createAsiakkaatBox(temp));
                } catch (NumberFormatException ee) {
                    ArrayList<Asiakas> temp = new ArrayList<Asiakas>();
                    asiakkaatSivu.getChildren().add(createAsiakkaatBox(temp));
                }
            }else{
                String eNimi;
                String sNimi;
                if(etunimiTF.getText() == "" || etunimiTF.getText().trim().length() == 0){
                    eNimi = "";
                }else{
                    eNimi = "^" + etunimiTF.getText();
                }
                if(sukunimiTF.getText() == "" || sukunimiTF.getText().trim().length() == 0){
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
            }
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
            if(eNimi.length() == 0 || eNimi.trim().length() == 0 || eNimi.length() > 20){
                errors += 1;
                etunimiUusi.setTextFill(Color.RED);
            }
            if(sNimi.length() == 0 || sNimi.trim().length() == 0 || sNimi.length() > 40){
                errors += 1;
                sukunimiUusi.setTextFill(Color.RED);
            }
            if(lahiosoite.length() == 0 || lahiosoite.trim().length() == 0 || lahiosoite.length() > 40){
                errors += 1;
                lahiosoiteUusi.setTextFill(Color.RED);
            }
            if(postinro.length() != 5){
                errors += 1;
                postinroUusi.setTextFill(Color.RED);
            }else{
                boolean flag = false;
                for(int i = 0; i < 5; i++){
                    if(Character.isDigit(postinro.charAt(i))){

                    }else{
                        flag = true;
                    }
                }
                if(flag){
                    errors += 1;
                    postinroUusi.setTextFill(Color.RED);
                }
            }
            if(postiT.length() == 0 || postiT.trim().length() == 0 || postiT.length() > 45){
                errors += 1;
                postiTUusi.setTextFill(Color.RED);
            }
            if(email.length() == 0 || email.trim().length() == 0 || email.length() > 50){
                errors += 1;
                emailUusi.setTextFill(Color.RED);
            }else{
                boolean flag = true;
                for(int k = 0; k < email.length(); k++){
                    if(email.charAt(k) == '@'){
                        flag = false;
                    }
                }
                if(flag){
                    errors += 1;
                    emailUusi.setTextFill(Color.RED);
                }
            }
            if(puhnro.length() == 0 || puhnro.trim().length() == 0 || puhnro.length() > 15){
                errors += 1;
                puhelinnroUusi.setTextFill(Color.RED);
            }else{
                boolean flag = false;
                for(int i = 0; i < puhnro.length(); i++){
                    if(i == 0){
                        if(puhnro.charAt(i) == '+'){

                        }else if(Character.isDigit(puhnro.charAt(i))){

                        }else{
                            flag = true;
                        }
                    }else{
                        if(Character.isDigit(puhnro.charAt(i))){
                            
                        }else if(puhnro.charAt(i) == ' '){

                        }else{
                            flag = true;
                        }
                    }
                }
                if(flag){
                    errors += 1;
                    puhelinnroUusi.setTextFill(Color.RED);
                }
            }
            boolean flag = true;
            for(Posti p : postit){
                if(p.getPostinro().equals(postinro)){
                    flag = false;
                    if(p.getToimipaikka().toUpperCase().equals(postiT.toUpperCase())){

                    }else{
                        errors += 1;
                        postinroUusi.setTextFill(Color.RED);
                        postiTUusi.setTextFill(Color.RED);
                        errMsgU.setText("Postinumero ja toimipaikka ei täsmää!");
                    }
                }
            }
            if(flag){
                errors += 1;
                postinroUusi.setTextFill(Color.RED);
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
        LocalDate dateNow = LocalDate.now();
        if(param.isEmpty()){
            Text noAsiakas = new Text("Asiakkaita ei löytynyt.");
            asiakkaatBox.getChildren().add(noAsiakas);
        }else{
            for(Asiakas a : param){
                boolean voikoPoistaa = true;
                for(Varaus v: varaukset){
                    if(v.getAsiakas().getAsiakasId() == a.getAsiakasId()){
                        Date vahvDate = new Date(v.getVahvistusPvm().getTime());
                        LocalDate vahvLocalDate = vahvDate.toLocalDate();
                        if(dateNow.compareTo(vahvLocalDate) > 0){
                            voikoPoistaa = false;
                        }
                    }
                }
                String n = a.getEtunimi() + " " + a.getSukunimi()  + " (" + a.getPuhelinnro() + ")";
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
                Label asiakasIDLabel = new Label("Asiakkaan id: ");
                asiakasIDLabel.setTextFill(Color.BLACK);
                Text asiakasIDText = new Text(String.valueOf(a.getAsiakasId()));
                asiakasIDText.setFill(Color.BLACK);
                aTiedot.add(asiakasIDLabel, 0,0);
                aTiedot.add(asiakasIDText, 1,0);
                Label etunimiM = new Label("Etunimi: ");
                etunimiM.setTextFill(Color.BLACK);
                TextField etunimiTM = new TextField(a.getEtunimi());
                aTiedot.add(etunimiM, 0,1);
                aTiedot.add(etunimiTM, 1,1);
                Label sukunimiM = new Label("Sukunimi: ");
                sukunimiM.setTextFill(Color.BLACK);
                TextField sukunimiTM = new TextField(a.getSukunimi());
                aTiedot.add(sukunimiM, 2,1);
                aTiedot.add(sukunimiTM, 3,1);
                Label lahiosoiteM = new Label("Lähiosoite: ");
                lahiosoiteM.setTextFill(Color.BLACK);
                TextField lahiosoiteTM = new TextField(a.getLahiOsoite());
                aTiedot.add(lahiosoiteM, 0,2);
                aTiedot.add(lahiosoiteTM, 1,2);
                Label postinroM = new Label("Postinumero: ");
                postinroM.setTextFill(Color.BLACK);
                TextField postinroTM = new TextField(a.getPosti().getPostinro());
                if(postinroTM.getText().equals("00000")){
                    postinroM.setTextFill(Color.RED);
                }
                aTiedot.add(postinroM, 0,3);
                aTiedot.add(postinroTM, 1,3);
                Label postiTM = new Label("Toimipaikka: ");
                postiTM.setTextFill(Color.BLACK);
                TextField postiTTM = new TextField(a.getPosti().getToimipaikka().toUpperCase());
                if(postiTTM.getText().equals("Rikki")){
                    postiTM.setTextFill(Color.RED);
                }
                aTiedot.add(postiTM, 2,3);
                aTiedot.add(postiTTM, 3,3);
                Label emailM = new Label("Email: ");
                emailM.setTextFill(Color.BLACK);
                TextField emailTM = new TextField(a.getEmail());
                aTiedot.add(emailM, 0,4);
                aTiedot.add(emailTM, 1,4);
                Label puhM = new Label("Puhelinnro: ");
                puhM.setTextFill(Color.BLACK);
                TextField puhTM = new TextField(a.getPuhelinnro());
                aTiedot.add(puhM, 2,4);
                aTiedot.add(puhTM, 3,4);
                CheckBox postituslistaCB = new CheckBox("Suostumus postituslistalle: ");
                postituslistaCB.setSelected(a.getPostituslista());
                aTiedot.add(postituslistaCB, 0, 5);
                Button saveEditBtn = new Button("Tallenna");
                aTiedot.add(saveEditBtn, 1,6);

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
                    if(eNimi.length() == 0 || eNimi.trim().length() == 0 || eNimi.length() > 20){
                        errors += 1;
                        etunimiM.setTextFill(Color.RED);
                    }
                    if(sNimi.length() == 0 || sNimi.trim().length() == 0 || sNimi.length() > 40){
                        errors += 1;
                        sukunimiM.setTextFill(Color.RED);
                    }
                    if(osoite.length() == 0 || osoite.trim().length() == 0 || osoite.length() > 40){
                        errors += 1;
                        lahiosoiteM.setTextFill(Color.RED);
                    }
                    if(postinro.length() != 5){
                        errors += 1;
                        postinroM.setTextFill(Color.RED);
                    }else if(postinro.equals("00000")){
                        errors += 1001;
                        postinroM.setTextFill(Color.RED);
                    }else{
                        boolean flag = false;
                        for(int i2 = 0; i2 < 5; i2++){
                            if(Character.isDigit(postinro.charAt(i2))){
        
                            }else{
                                flag = true;
                            }
                        }
                        if(flag){
                            errors += 1;
                            postinroM.setTextFill(Color.RED);
                        }
                    }
                    if(postiTP.length() == 0 || postiTP.trim().length() == 0 || postiTP.length() > 45){
                        errors += 1;
                        postiTM.setTextFill(Color.RED);
                    }else if(postiTP.equals("Rikki")){
                        errors += 1001;
                        postiTM.setTextFill(Color.RED);
                    }
                    if(email.length() == 0 || email.trim().length() == 0 || email.length() > 50){
                        errors += 1;
                        emailM.setTextFill(Color.RED);
                    }else{
                        boolean flag = true;
                        for(int k = 0; k < email.length(); k++){
                            if(email.charAt(k) == '@'){
                                flag = false;
                            }
                        }
                        if(flag){
                            errors += 1;
                            emailM.setTextFill(Color.RED);
                        }
                    }
                    if(puhnro.length() == 0 || puhnro.trim().length() == 0 || puhnro.length() > 15){
                        errors += 1;
                        puhM.setTextFill(Color.RED);
                    }else{
                        boolean flag = false;
                        for(int i2 = 0; i2 < puhnro.length(); i2++){
                            if(i2 == 0){
                                if(puhnro.charAt(i2) == '+'){

                                }else if(Character.isDigit(puhnro.charAt(i2))){

                                }else{
                                    flag = true;
                                }
                            }else{
                                if(Character.isDigit(puhnro.charAt(i2))){
                                    
                                }else if(puhnro.charAt(i2) == ' '){

                                }else{
                                    flag = true;
                                }
                            }
                        }
                        if(flag){
                            errors += 1;
                            puhM.setTextFill(Color.RED);
                        }
                    }
                    boolean flag = true;
                    for(Posti p : postit){
                        if(p.getPostinro().equals(postinro)){
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        errors += 1001;
                        postinroM.setTextFill(Color.RED);
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
                                i.setText(a.getEtunimi() + " " + a.getSukunimi() + " (" + a.getPuhelinnro() + ")");
                               
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

                if(voikoPoistaa){
                    Button deleteUserBtn = new Button("Poista");
                    aTiedot.add(deleteUserBtn, 2,6);
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
                }

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

        Label idHakuLabel = new Label("Hae varauksen id:llä: ");
        idHakuLabel.setTextFill(Color.BLACK);
        TextField idHakuTF = new TextField();

        Label taiHae = new Label("Tai hae muilla hakuehdoilla");
        taiHae.setTextFill(Color.BLACK);

        Label nimiHaku = new Label("Nimi: ");
        nimiHaku.setTextFill(Color.BLACK);
        ComboBox<String> nimiCB = new ComboBox<String>();
        nimiCB.getItems().add("Ei hakuehtoa");
        for(Asiakas a : asiakkaat){
            String name = a.getEtunimi() + "_" + a.getSukunimi() + ":" + a.getPuhelinnro();
            nimiCB.getItems().add(name);
        }
        nimiCB.setValue("Ei hakuehtoa");
        Label mokkiHaku = new Label("Mökin nimi: ");
        mokkiHaku.setTextFill(Color.BLACK);
        Label postinroHaku = new Label("Postinro: ");
        postinroHaku.setTextFill(Color.BLACK);
        TextField mokkiTF = new TextField();
        TextField postinroTF = new TextField();
        Label alkupvmHaku = new Label("Alkupvm: ");
        alkupvmHaku.setTextFill(Color.BLACK);
        Label loppupvmHaku = new Label("Loppupvm: ");
        loppupvmHaku.setTextFill(Color.BLACK);
        //TextField alkupvmTF = new TextField();
        //TextField loppupvmTF = new TextField();
        DatePicker alkupvmDP = new DatePicker();
        DatePicker loppupvmDP = new DatePicker();
        varauksetHaeLomake.add(idHakuLabel, 0,0);
        varauksetHaeLomake.add(idHakuTF, 1,0);
        varauksetHaeLomake.add(taiHae, 0,1);
        varauksetHaeLomake.add(nimiHaku, 0,2);
        varauksetHaeLomake.add(nimiCB, 1,2);
        varauksetHaeLomake.add(mokkiHaku, 0,3);
        varauksetHaeLomake.add(mokkiTF, 0,4);
        varauksetHaeLomake.add(postinroHaku, 1,3);
        varauksetHaeLomake.add(postinroTF, 1,4);
        varauksetHaeLomake.add(alkupvmHaku, 0,5);
        //varauksetHaeLomake.add(alkupvmTF, 0,5);
        varauksetHaeLomake.add(alkupvmDP, 0,6);
        varauksetHaeLomake.add(loppupvmHaku, 1,5);
        //varauksetHaeLomake.add(loppupvmTF, 1,5);
        varauksetHaeLomake.add(loppupvmDP, 1,6);
        Button hakuBtn = new Button("Hae varausta");
        varauksetHaeLomake.add(hakuBtn, 0,7);
        varauksetSivu.getChildren().add(varauksetHaeLomake);
        
        varauksetSivu.getChildren().add(createVarauksetBox(varaukset));

        VBox uusiVarausSivu = new VBox();
        uusiVarausSivu.setAlignment(Pos.CENTER);
        uusiVarausSivu.setPadding(new Insets(15,30,15,30));
        GridPane uusiVarausAsGrid = new GridPane();
        uusiVarausSivu.getChildren().add(uusiVarausAsGrid);
        uusiVarausAsGrid.setPadding(new Insets(15,0,15,0));
        uusiVarausAsGrid.setHgap(5);
        uusiVarausAsGrid.setVgap(10);
        Label errMsgU = new Label("");
        errMsgU.setTextFill(Color.RED);
        Label valAsiakas = new Label("Valitse asiakas: ");
        valAsiakas.setTextFill(Color.BLACK);
        ComboBox<String> asiakkaatCB = new ComboBox<String>();
        asiakkaatCB.getItems().add("Uusi asiakas");
        for(Asiakas a : asiakkaat){
            String name = a.getEtunimi() + "_" + a.getSukunimi() + ":" + a.getPuhelinnro();
            asiakkaatCB.getItems().add(name);
        }
        asiakkaatCB.setValue("Uusi asiakas");

        Label etunimiLabel = new Label("Etunimi: ");
        etunimiLabel.setTextFill(Color.BLACK);
        TextField etunimiText = new TextField();

        Label sukunimiLabel = new Label("Sukunimi: ");
        sukunimiLabel.setTextFill(Color.BLACK);
        TextField sukunimiText = new TextField();

        Label lahiosoiteLabel = new Label("Lähiosoite: ");
        lahiosoiteLabel.setTextFill(Color.BLACK);
        TextField lahiosoiteText = new TextField();

        Label postinroLabel = new Label("Postinro: ");
        postinroLabel.setTextFill(Color.BLACK);
        TextField postinroText = new TextField();

        Label toimipaikkaLabel = new Label("Toimipaikka: ");
        toimipaikkaLabel.setTextFill(Color.BLACK);
        TextField toimipaikkaText = new TextField();

        Label emailLabel = new Label("Email: ");
        emailLabel.setTextFill(Color.BLACK);
        TextField emailText = new TextField();

        Label puhelinnroLabel = new Label("Puhelinnro: ");
        puhelinnroLabel.setTextFill(Color.BLACK);
        TextField puhelinnroText = new TextField();

        CheckBox postituslistaCBU = new CheckBox("Suostumus postituslistalle");

        uusiVarausAsGrid.add(valAsiakas, 0,0);
        uusiVarausAsGrid.add(asiakkaatCB, 1,0);
        uusiVarausAsGrid.add(etunimiLabel, 0,1);
        uusiVarausAsGrid.add(etunimiText, 1,1);
        uusiVarausAsGrid.add(sukunimiLabel, 0,2);
        uusiVarausAsGrid.add(sukunimiText, 1,2);
        uusiVarausAsGrid.add(lahiosoiteLabel, 0,3);
        uusiVarausAsGrid.add(lahiosoiteText, 1,3);
        uusiVarausAsGrid.add(postinroLabel, 0,4);
        uusiVarausAsGrid.add(postinroText, 1,4);
        uusiVarausAsGrid.add(toimipaikkaLabel, 0,5);
        uusiVarausAsGrid.add(toimipaikkaText, 1,5);
        uusiVarausAsGrid.add(emailLabel, 0,6);
        uusiVarausAsGrid.add(emailText, 1,6);
        uusiVarausAsGrid.add(puhelinnroLabel, 0,7);
        uusiVarausAsGrid.add(puhelinnroText, 1,7);
        uusiVarausAsGrid.add(postituslistaCBU, 0,8);

        asiakkaatCB.setOnAction(e -> {
            String as = asiakkaatCB.getValue();
            if(as == "Uusi asiakas"){
                etunimiText.setText("");
                etunimiText.setEditable(true);
                etunimiText.setDisable(false);

                sukunimiText.setText("");
                sukunimiText.setEditable(true);
                sukunimiText.setDisable(false);

                lahiosoiteText.setText("");
                lahiosoiteText.setEditable(true);
                lahiosoiteText.setDisable(false);

                postinroText.setText("");
                postinroText.setEditable(true);
                postinroText.setDisable(false);

                toimipaikkaText.setText("");
                toimipaikkaText.setEditable(true);
                toimipaikkaText.setDisable(false);

                emailText.setText("");
                emailText.setEditable(true);
                emailText.setDisable(false);

                puhelinnroText.setText("");
                puhelinnroText.setEditable(true);
                puhelinnroText.setDisable(false);

                postituslistaCBU.setSelected(false);
                postituslistaCBU.setDisable(false);
            }else{
                for(Asiakas a : asiakkaat){
                    if(as.split(":")[0].split("_")[0].equals(a.getEtunimi()) && as.split(":")[0].split("_")[1].equals(a.getSukunimi()) && as.split(":")[1].equals(a.getPuhelinnro())){
                        etunimiText.setText(a.getEtunimi());
                        etunimiText.setEditable(false);
                        etunimiText.setDisable(true);

                        sukunimiText.setText(a.getSukunimi());
                        sukunimiText.setEditable(false);
                        sukunimiText.setDisable(true);

                        lahiosoiteText.setText(a.getLahiOsoite());
                        lahiosoiteText.setEditable(false);
                        lahiosoiteText.setDisable(true);

                        postinroText.setText(a.getPosti().getPostinro());
                        postinroText.setEditable(false);
                        postinroText.setDisable(true);

                        toimipaikkaText.setText(a.getPosti().getToimipaikka().toUpperCase());
                        toimipaikkaText.setEditable(false);
                        toimipaikkaText.setDisable(true);

                        emailText.setText(a.getEmail());
                        emailText.setEditable(false);
                        emailText.setDisable(true);

                        puhelinnroText.setText(a.getPuhelinnro());
                        puhelinnroText.setEditable(false);
                        puhelinnroText.setDisable(true);

                        postituslistaCBU.setSelected(a.getPostituslista());
                        postituslistaCBU.setDisable(true);
                    }
                }
            }
        });

        GridPane uusiVarausGrid = new GridPane();
        uusiVarausGrid.setPadding(new Insets(15,0,15,0));
        uusiVarausGrid.setHgap(5);
        uusiVarausGrid.setVgap(10);

        Text uusiVarausErrMsg = new Text("");
        uusiVarausErrMsg.setFill(Color.RED);

        Label uusiVarausMokkiLabel = new Label("Mökki: ");
        uusiVarausMokkiLabel.setTextFill(Color.BLACK);
        ComboBox<String> uusiVarausMokkiCB = new ComboBox<String>();
        uusiVarausMokkiCB.getItems().add("Valitse mökki");
        uusiVarausMokkiCB.setValue("Valitse mökki");
        for(Mokki m : mokit){
            uusiVarausMokkiCB.getItems().add(m.getMokkinimi());
        }

        Label varausAlkuLabel = new Label("Varauksen alkupvm: ");
        varausAlkuLabel.setTextFill(Color.BLACK);
        DatePicker varausAlkuDP = new DatePicker();

        Label varausLoppuLabel = new Label("Varauksen loppupvm: ");
        varausLoppuLabel.setTextFill(Color.BLACK);
        DatePicker varausLoppuDP = new DatePicker();

        CheckBox laskuCBU = new CheckBox("Haluan laskun sähköpostitse");

        uusiVarausGrid.add(uusiVarausErrMsg, 0,0);
        uusiVarausGrid.add(uusiVarausMokkiLabel, 0,1);
        uusiVarausGrid.add(uusiVarausMokkiCB, 1,1);
        uusiVarausGrid.add(varausAlkuLabel, 0,2);
        uusiVarausGrid.add(varausAlkuDP, 1,2);
        uusiVarausGrid.add(varausLoppuLabel, 0,3);
        uusiVarausGrid.add(varausLoppuDP, 1,3);
        uusiVarausGrid.add(laskuCBU, 0,4);
        Button uusiBtn = new Button("Tallenna");
        uusiVarausGrid.add(uusiBtn, 0,5);
        GridPane.setColumnSpan(uusiBtn, 2);
        GridPane.setColumnSpan(uusiVarausErrMsg, 2);

        uusiVarausSivu.getChildren().add(uusiVarausGrid);

        varausAlkuDP.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                LocalDate dateNow = LocalDate.now();
                if(date.compareTo(dateNow) < 0){
                    setDisable(true);
                }
            }
        });

        varausLoppuDP.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                LocalDate dateNow = LocalDate.now();
                if(date.compareTo(dateNow) < 1){
                    setDisable(true);
                }
            }
        });

        uusiVarausMokkiCB.setOnAction(e -> {
            varausAlkuDP.setValue(null);
            varausLoppuDP.setValue(null);
            Mokki temp = null;
            for(Mokki m : mokit){
                if(m.getMokkinimi().equals(uusiVarausMokkiCB.getValue())){
                    temp = m;
                }
            }
            if(temp != null){
                int tid = temp.getMokkiId();
                varausAlkuDP.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        for(Varaus v : varaukset){
                            if(v.getMokki().getMokkiId() == tid){
                                super.updateItem(date, empty);
                                Date alkuDate = new Date(v.getVarattuAlkuPvm().getTime());
                                Date loppuDate = new Date(v.getVarattuLoppuPvm().getTime());

                                if(date.compareTo(alkuDate.toLocalDate()) >= 0 && date.compareTo(loppuDate.toLocalDate()) <= 0){
                                    setDisable(true);
                                }
                            }
                        }
                        LocalDate dateNow = LocalDate.now();
                        if(date.compareTo(dateNow) < 0){
                            setDisable(true);
                        }
                    }
                });
    
                varausLoppuDP.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        for(Varaus v : varaukset){
                            if(v.getMokki().getMokkiId() == tid){
                                super.updateItem(date, empty);
                                Date alkuDate = new Date(v.getVarattuAlkuPvm().getTime());
                                Date loppuDate = new Date(v.getVarattuLoppuPvm().getTime());
                                if(date.compareTo(alkuDate.toLocalDate()) >= 0 && date.compareTo(loppuDate.toLocalDate()) <= 0){
                                    setDisable(true);
                                }
                            }
                        }
                        LocalDate dateNow = LocalDate.now();
                        if(date.compareTo(dateNow) < 1){
                            setDisable(true);
                        }
                    }
                });
            }else{
                varausAlkuDP.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        LocalDate dateNow = LocalDate.now();
                        if(date.compareTo(dateNow) < 1){
                            setDisable(true);
                        }else{
                            setDisable(false);
                        }
                        varausAlkuDP.setValue(null);
                    }
                });
    
                varausLoppuDP.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        LocalDate dateNow = LocalDate.now();
                        if(date.compareTo(dateNow) < 1){
                            setDisable(true);
                        }else{
                            setDisable(false);
                        }
                        varausLoppuDP.setValue(null);
                    }
                });
            }
        });

        varausAlkuDP.setOnAction(e -> {
            LocalDate alkuValue = varausAlkuDP.getValue();
            LocalDate loppuValue = varausLoppuDP.getValue();
            if(alkuValue != null){
                if(loppuValue != null){
                    if(alkuValue.compareTo(loppuValue) >= 0){
                        varausLoppuDP.setValue(null);
                    }
                }
                varausLoppuDP.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        if(date.compareTo(alkuValue) <= 0){
                            setDisable(true);
                        }else{
                            setDisable(false);
                        }
                        LocalDate dateNow = LocalDate.now();
                        if(date.compareTo(dateNow) < 1){
                            setDisable(true);
                        }
                    }
                });
            }else{
                varausLoppuDP.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        LocalDate dateNow = LocalDate.now();
                        if(date.compareTo(dateNow) < 1){
                            setDisable(true);
                        }
                    }
                });
            }
        });

        varausLoppuDP.setOnAction(e -> {
            LocalDate alkuValue = varausAlkuDP.getValue();
            LocalDate loppuValue = varausLoppuDP.getValue();
            if(loppuValue != null){
                if(alkuValue != null){
                    if(loppuValue.compareTo(alkuValue) <= 0){
                        varausAlkuDP.setValue(null);
                    }
                }
                varausAlkuDP.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        if(date.compareTo(loppuValue) >= 0){
                            setDisable(true);
                        }else{
                            setDisable(false);
                        }
                        LocalDate dateNow = LocalDate.now();
                        if(date.compareTo(dateNow) < 0){
                            setDisable(true);
                        }
                    }
                });
            }else{
                varausAlkuDP.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        LocalDate dateNow = LocalDate.now();
                        if(date.compareTo(dateNow) < 0){
                            setDisable(true);
                        }
                    }
                });
            }
        });

        uusiBtn.setOnAction(e -> {
            uusiVarausErrMsg.setText("");
            etunimiLabel.setTextFill(Color.BLACK);
            sukunimiLabel.setTextFill(Color.BLACK);
            lahiosoiteLabel.setTextFill(Color.BLACK);
            postinroLabel.setTextFill(Color.BLACK);
            toimipaikkaLabel.setTextFill(Color.BLACK);
            emailLabel.setTextFill(Color.BLACK);
            puhelinnroLabel.setTextFill(Color.BLACK);
            uusiVarausMokkiLabel.setTextFill(Color.BLACK);
            varausAlkuLabel.setTextFill(Color.BLACK);
            varausLoppuLabel.setTextFill(Color.BLACK);
            if(asiakkaatCB.getValue().equals("Uusi asiakas")){
                int errors = 0;
                String eNimi = etunimiText.getText();
                String sNimi = sukunimiText.getText();
                String lahiosoite = lahiosoiteText.getText();
                String postinro = postinroText.getText();
                String toimipaikka = toimipaikkaText.getText().toUpperCase();
                String email = emailText.getText();
                String puhelinnro = puhelinnroText.getText();
                boolean postituslista = postituslistaCBU.isSelected();
                
                String mokinNimi = uusiVarausMokkiCB.getValue();
                LocalDate alkuPvm = varausAlkuDP.getValue();
                LocalDate loppuPvm = varausLoppuDP.getValue();
                if(eNimi.length() == 0 || eNimi.trim().length() == 0 || eNimi.length() > 20){
                    errors += 1;
                    etunimiLabel.setTextFill(Color.RED);

                }
                if(sNimi.length() == 0 || sNimi.trim().length() == 0 || sNimi.length() > 40){
                    errors += 1;
                    sukunimiLabel.setTextFill(Color.RED);
                }
                if(lahiosoite.length() == 0 || lahiosoite.trim().length() == 0 || lahiosoite.length() > 40){
                    errors += 1;
                    lahiosoiteLabel.setTextFill(Color.RED);
                }
                if(postinro.length() != 5){
                    errors += 1;
                    postinroLabel.setTextFill(Color.RED);
                }else{
                    boolean flag = false;
                    for(int i = 0; i < 5; i++){
                        if(Character.isDigit(postinro.charAt(i))){
    
                        }else{
                            flag = true;
                        }
                    }
                    if(flag){
                        errors += 1;
                        postinroLabel.setTextFill(Color.RED);
                    }
                }
                if(toimipaikka.length() == 0 || toimipaikka.trim().length() == 0 || toimipaikka.length() > 45){
                    errors += 1;
                    toimipaikkaLabel.setTextFill(Color.RED);
                }
                if(postinro.length() == 5 && toimipaikka.length() != 0){
                    boolean flag = false;
                    for(Posti p : postit){
                        if(p.getPostinro().equals(postinro)){
                            if(p.getToimipaikka().toUpperCase().equals(toimipaikka.toUpperCase())){
                                flag = true;
                            }
                        }
                    }
                    if(!flag){
                        errors += 100;
                        postinroLabel.setTextFill(Color.RED);
                        toimipaikkaLabel.setTextFill(Color.RED);
                    }
                }
                if(email.length() == 0 || email.trim().length() == 0 || email.length() > 50){
                    errors += 1;
                    emailLabel.setTextFill(Color.RED);
                }else{
                    boolean flag = true;
                    for(int k = 0; k < email.length(); k++){
                        if(email.charAt(k) == '@'){
                            flag = false;
                        }
                    }
                    if(flag){
                        errors += 1;
                        emailLabel.setTextFill(Color.RED);
                    }
                }
                if(puhelinnro.length() == 0 || puhelinnro.trim().length() == 0 || puhelinnro.length() > 15){
                    errors += 1;
                    puhelinnroLabel.setTextFill(Color.RED);
                }else{
                    boolean flag = false;
                    for(int i = 0; i < puhelinnro.length(); i++){
                        if(i == 0){
                            if(puhelinnro.charAt(i) == '+'){
    
                            }else if(Character.isDigit(puhelinnro.charAt(i))){
    
                            }else{
                                flag = true;
                            }
                        }else{
                            if(Character.isDigit(puhelinnro.charAt(i))){
                                
                            }else if(puhelinnro.charAt(i) == ' '){
    
                            }else{
                                flag = true;
                            }
                        }
                    }
                    if(flag){
                        errors += 1;
                        puhelinnroLabel.setTextFill(Color.RED);
                    }
                }

                if(mokinNimi == "Valitse mökki"){
                    errors += 1;
                    uusiVarausMokkiLabel.setTextFill(Color.RED);
                }
                if(alkuPvm == null){
                    errors += 1;
                    varausAlkuLabel.setTextFill(Color.RED);
                }
                if(loppuPvm == null){
                    errors += 1;
                    varausLoppuLabel.setTextFill(Color.RED);
                }
                if(alkuPvm != null && loppuPvm != null){
                    if(alkuPvm.compareTo(loppuPvm) >= 0){
                        errors += 1;
                        varausAlkuLabel.setTextFill(Color.RED);
                        varausLoppuLabel.setTextFill(Color.RED);
                    }
                }
                for(Asiakas a : asiakkaat){
                    boolean flag = false;
                    if(a.checkCopy(eNimi, sNimi, postinro, lahiosoite, email, puhelinnro, postituslista)){
                        flag = true;
                    }
                    if(flag){
                        errors -= 2000;
                    }
                }



                if(errors != 0){
                    if(errors > 20){
                        uusiVarausErrMsg.setText("Postinumerossa ja/tai toimipaikassa virhe.");
                    }else if(errors < -100){
                        uusiVarausErrMsg.setText("Virhe! Asiakas on jo olemassa.");
                        etunimiLabel.setTextFill(Color.RED);
                        sukunimiLabel.setTextFill(Color.RED);
                        lahiosoiteLabel.setTextFill(Color.RED);
                        postinroLabel.setTextFill(Color.RED);
                        toimipaikkaLabel.setTextFill(Color.RED);
                        emailLabel.setTextFill(Color.RED);
                        puhelinnroLabel.setTextFill(Color.RED);
                    }else{
                        uusiVarausErrMsg.setText("Virhe! Tarkista syöttämäsi tiedot.");
                    }
                }else{
                    uusiVarausErrMsg.setText("");
                    try{
                        String query = "INSERT INTO asiakas VALUES (";
                        query += "null" + ", ";
                        query += "'" + postinro + "', ";
                        query += "'" + eNimi + "', ";
                        query += "'" + sNimi + "', ";
                        query += "'" + lahiosoite + "', ";
                        query += "'" + email + "', ";
                        query += "'" + puhelinnro + "', ";
                        if(postituslista){
                            query += 1 + ")";
                        }else{
                            query += 0 + ")";
                        }
    
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);
                        
                        asiakkaatCB.setValue("Uusi asiakas");
                        etunimiText.setText("");
                        sukunimiText.setText("");
                        lahiosoiteText.setText("");
                        postinroText.setText("");
                        toimipaikkaText.setText("");
                        emailText.setText("");
                        puhelinnroText.setText("");
                        postituslistaCBU.setSelected(false);
                        
                        setAsiakkaat(getAsiakkaat());
                        int asiakasID = 0;
                        for(Asiakas a : asiakkaat){
                            int calc = 0;
                            if(a.getEtunimi().equals(eNimi)){
                                calc += 1;
                            }
                            if(a.getSukunimi().equals(sNimi)){
                                calc += 1;
                            }
                            if(a.getLahiOsoite().equals(lahiosoite)){
                                calc += 1;
                            }
                            if(a.getPosti().getPostinro().equals(postinro)){
                                calc += 1;
                            }
                            if(a.getPuhelinnro().equals(puhelinnro)){
                                calc += 1;
                            }
                            if(a.getEmail().equals(email)){
                                calc += 1;
                            }
                            if(a.getPostituslista() == postituslista){
                                calc += 1;
                            }
                            if(calc == 7){
                                asiakasID = a.getAsiakasId();
                            }
                        }
                        int mokkiID = 0;
                        double mokkiHinta = 0;
                        for(Mokki m : mokit){
                            if(mokinNimi.equals(m.getMokkinimi())){
                                mokkiID = m.getMokkiId();
                                mokkiHinta = m.getHinta();
                            }
                        }
                        Timestamp timestampNow = new Timestamp(System.currentTimeMillis());
                        String timeNow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(timestampNow);
                        LocalDate vahvistusPvm = alkuPvm.minusDays(14);
                        String vahvistusTime = String.valueOf(vahvistusPvm) + " 12:00:00";
                        String alkuPvmTime = String.valueOf(alkuPvm) + " 12:00:00";
                        String loppuPvmTime = String.valueOf(loppuPvm) + " 12:00:00";
                        if(asiakasID != 0){
                            String query2 = "INSERT INTO varaus VALUES (";
                            query2 += "null" + ", '";
                            query2 += asiakasID + "', '";
                            query2 += mokkiID + "', '";
                            query2 += timeNow + "', '";
                            query2 += vahvistusTime + "', '";
                            query2 += alkuPvmTime + "', '";
                            query2 += loppuPvmTime + "')";

                            Statement stmt2 = con.createStatement();
                            stmt2.executeUpdate(query2);
                            

                            setVaraukset(getVaraukset());

                            varausAlkuDP.setValue(null);
                            varausLoppuDP.setValue(null);
                            uusiVarausMokkiCB.setValue("Valitse mökki");
                            varausAlkuDP.setDayCellFactory(picker -> new DateCell() {
                                public void updateItem(LocalDate date, boolean empty) {
                                    LocalDate dateNow = LocalDate.now();
                                    if(date.compareTo(dateNow) < 0){
                                        setDisable(true);
                                    }else{
                                        setDisable(false);
                                    }
                                }
                            });
                
                            varausLoppuDP.setDayCellFactory(picker -> new DateCell() {
                                public void updateItem(LocalDate date, boolean empty) {
                                    LocalDate dateNow = LocalDate.now();
                                    if(date.compareTo(dateNow) < 1){
                                        setDisable(true);
                                    }else{
                                        setDisable(false);
                                    }
                                }
                            });
                        }
                        boolean laskutapa = laskuCBU.isSelected();
                        int varausID = 0;
                        for(Varaus v : varaukset){
                            String temp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(v.getVarattuAlkuPvm());
                            String temp2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(v.getVarattuLoppuPvm());
                            int calc = 0;
                            if(v.getMokki().getMokkiId() == mokkiID){
                                calc += 1;
                            }
                            if(temp.equals(alkuPvmTime)){
                                calc += 1;
                            }
                            if(temp2.equals(loppuPvmTime)){
                                calc += 1;
                            }
                            if(calc == 3){
                                varausID = v.getVarausId();
                            }
                        }
                        if(varausID != 0){
                            long days = ChronoUnit.DAYS.between(alkuPvm, loppuPvm);
                            double summa = days * mokkiHinta;
                            double alv = 24.0;
                            int maksettu = 0;
                            String erapaiva = new SimpleDateFormat("yyyy-MM-dd").format(loppuPvm.plusDays(31));
                            String laskutyyppi = "";
                            if(laskutapa){
                                laskutyyppi = "sposti";
                            }else{
                                laskutyyppi = "posti";
                            }
                            String viitenumero;
                            while(true){
                                boolean flag = false;
                                String temp = "";
                                for(int i = 0; i < 9; i++){
                                    double tempInt = Math.floor(Math.random() * 10);
                                    temp += String.valueOf(tempInt).charAt(0);
                                }
                                for(Lasku l : laskut){
                                    if(l.getViitenumero().equals(temp)){
                                        flag = true;
                                    }
                                }
                                if(flag){
                                    continue;
                                }else{
                                    viitenumero = temp;
                                    break;
                                }
                            }
                            String query3 = "INSERT INTO lasku VALUES (";
                            query3 += "null" + ", '";
                            query3 += varausID + "', '";
                            query3 += viitenumero + "', '";
                            query3 += summa + "', '";
                            query3 += alv + "', ";
                            query3 += maksettu + ", '";
                            query3 += erapaiva + "', ";
                            query3 += "null, '";
                            query3 += laskutyyppi + "')";

                            Statement stmt3 = con.createStatement();
                            stmt3.executeUpdate(query3);
                            

                            setLaskut(getLaskut());
                            

                        }
                        
                        
    
                        con.close();
                        uusiVarausErrMsg.setText("Asiakas ja varaus tallennettu.");
                    }catch(Exception err){
                        System.out.println(err);
                        uusiVarausErrMsg.setText("Asiakkaan tallennuksessa virhe!");
                    }
                }

            }else{
                int errors = 0;
                String asiakasNimi = asiakkaatCB.getValue();

                String mokinNimi = uusiVarausMokkiCB.getValue();

                LocalDate alkuPvm = varausAlkuDP.getValue();
                LocalDate loppuPvm = varausLoppuDP.getValue();

                if(mokinNimi == "Valitse mökki"){
                    errors += 1;
                    uusiVarausMokkiLabel.setTextFill(Color.RED);
                }
                if(alkuPvm == null){
                    errors += 1;
                    varausAlkuLabel.setTextFill(Color.RED);
                }
                if(loppuPvm == null){
                    errors += 1;
                    varausLoppuLabel.setTextFill(Color.RED);
                }

                if(errors != 0){
                    uusiVarausErrMsg.setText("Virhe! Tarkista syöttämäsi tiedot.");
                }else{
                    uusiVarausErrMsg.setText("");
                    try{
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                        
                        asiakkaatCB.setValue("Uusi asiakas");
                        etunimiText.setText("");
                        sukunimiText.setText("");
                        lahiosoiteText.setText("");
                        postinroText.setText("");
                        toimipaikkaText.setText("");
                        emailText.setText("");
                        puhelinnroText.setText("");
                        postituslistaCBU.setSelected(false);
                        
                        int asiakasID = 0;
                        for(Asiakas a : asiakkaat){
                            if(asiakasNimi.split(":")[0].split("_")[0].equals(a.getEtunimi()) && asiakasNimi.split(":")[0].split("_")[1].equals(a.getSukunimi()) && asiakasNimi.split(":")[1].equals(a.getPuhelinnro())){
                                asiakasID = a.getAsiakasId();
                            }
                        }
                        
                        int mokkiID = 0;
                        double mokkiHinta = 0;
                        for(Mokki m : mokit){
                            if(mokinNimi.equals(m.getMokkinimi())){
                                mokkiID = m.getMokkiId();
                                mokkiHinta = m.getHinta();
                            }
                        }
                        Timestamp timestampNow = new Timestamp(System.currentTimeMillis());
                        String timeNow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(timestampNow);
                        LocalDate vahvistusPvm = alkuPvm.minusDays(14);
                        String vahvistusTime = String.valueOf(vahvistusPvm) + " 12:00:00";
                        String alkuPvmTime = String.valueOf(alkuPvm) + " 12:00:00";
                        String loppuPvmTime = String.valueOf(loppuPvm) + " 12:00:00";
                        if(asiakasID != 0){
                            String query2 = "INSERT INTO varaus VALUES (";
                            query2 += "null" + ", '";
                            query2 += asiakasID + "', '";
                            query2 += mokkiID + "', '";
                            query2 += timeNow + "', '";
                            query2 += vahvistusTime + "', '";
                            query2 += alkuPvmTime + "', '";
                            query2 += loppuPvmTime + "')";

                            Statement stmt2 = con.createStatement();
                            stmt2.executeUpdate(query2);
                            

                            setVaraukset(getVaraukset());

                            varausAlkuDP.setValue(null);
                            varausLoppuDP.setValue(null);
                            uusiVarausMokkiCB.setValue("Valitse mökki");
                            varausAlkuDP.setDayCellFactory(picker -> new DateCell() {
                                public void updateItem(LocalDate date, boolean empty) {
                                    LocalDate dateNow = LocalDate.now();
                                    if(date.compareTo(dateNow) < 0){
                                        setDisable(true);
                                    }else{
                                        setDisable(false);
                                    }
                                }
                            });
                
                            varausLoppuDP.setDayCellFactory(picker -> new DateCell() {
                                public void updateItem(LocalDate date, boolean empty) {
                                    LocalDate dateNow = LocalDate.now();
                                    if(date.compareTo(dateNow) < 1){
                                        setDisable(true);
                                    }else{
                                        setDisable(false);
                                    }
                                }
                            });
                        }
                        
                        boolean laskutapa = laskuCBU.isSelected();
                        int varausID = 0;
                        for(Varaus v : varaukset){
                            String temp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(v.getVarattuAlkuPvm());
                            String temp2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(v.getVarattuLoppuPvm());
                            int calc = 0;
                            if(v.getMokki().getMokkiId() == mokkiID){
                                calc += 1;
                            }
                            if(temp.equals(alkuPvmTime)){
                                calc += 1;
                            }
                            if(temp2.equals(loppuPvmTime)){
                                calc += 1;
                            }
                            if(calc == 3){
                                varausID = v.getVarausId();
                            }
                        }
                        if(varausID != 0){
                            long days = ChronoUnit.DAYS.between(alkuPvm, loppuPvm);
                            double summa = days * mokkiHinta;
                            double alv = 24.0;
                            int maksettu = 0;
                            LocalDate erapaivaTemp = loppuPvm.plusDays(31);
                            String erapaiva = String.valueOf(erapaivaTemp);
                            String laskutyyppi = "";
                            if(laskutapa){
                                laskutyyppi = "sposti";
                            }else{
                                laskutyyppi = "posti";
                            }
                            String viitenumero;
                            while(true){
                                boolean flag = false;
                                String temp = "";
                                for(int i = 0; i < 9; i++){
                                    double tempInt = Math.floor(Math.random() * 10);
                                    temp += String.valueOf(tempInt).charAt(0);
                                }
                                for(Lasku l : laskut){
                                    if(l.getViitenumero().equals(temp)){
                                        flag = true;
                                    }
                                }
                                if(flag){
                                    continue;
                                }else{
                                    viitenumero = temp;
                                    break;
                                }
                            }
                            String query3 = "INSERT INTO lasku VALUES (";
                            query3 += "null" + ", '";
                            query3 += varausID + "', '";
                            query3 += viitenumero + "', '";
                            query3 += summa + "', '";
                            query3 += alv + "', ";
                            query3 += maksettu + ", '";
                            query3 += erapaiva + "', ";
                            query3 += "null, '";
                            query3 += laskutyyppi + "')";

                            Statement stmt3 = con.createStatement();
                            stmt3.executeUpdate(query3);
                            

                            setLaskut(getLaskut());
                            

                        }
                        
    
                        con.close();
                        uusiVarausErrMsg.setText("Varaus tallennettu.");
                    }catch(Exception err){
                        System.out.println(err);
                        uusiVarausErrMsg.setText("Varauksen tallennuksessa virhe!");
                    }
                }
            }
        });

        listVaraukset.setOnAction(e -> {
            varauksetSivu.getChildren().clear();
            varauksetSivu.getChildren().add(varausTitle);
            varauksetSivu.getChildren().add(varauksetMenu);
            varauksetSivu.getChildren().add(varauksetHaeLomake);
            varauksetSivu.getChildren().add(createVarauksetBox(varaukset));

            asiakkaatCB.setValue("Uusi asiakas");
            uusiVarausMokkiCB.setValue("Valitse mökki");
            varausAlkuDP.setValue(null);
            varausLoppuDP.setValue(null);
            varausAlkuDP.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    LocalDate dateNow = LocalDate.now();
                    if(date.compareTo(dateNow) < 0){
                        setDisable(true);
                    }else{
                        setDisable(false);
                    }
                }
            });

            varausLoppuDP.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    LocalDate dateNow = LocalDate.now();
                    if(date.compareTo(dateNow) < 1){
                        setDisable(true);
                    }else{
                        setDisable(false);
                    }
                }
            });
            
        });

        uusiVaraus.setOnAction(e -> {
            varauksetSivu.getChildren().clear();
            varauksetSivu.getChildren().add(varausTitle);
            varauksetSivu.getChildren().add(varauksetMenu);
            varauksetSivu.getChildren().add(uusiVarausSivu);

            idHakuTF.setText("");
            nimiCB.setValue("Ei hakuehtoa");
            mokkiTF.setText("");
            postinroTF.setText("");
            alkupvmDP.setValue(null);
            loppupvmDP.setValue(null);
        });

        hakuBtn.setOnAction(e -> {
            idHakuLabel.setTextFill(Color.BLACK);
            varauksetSivu.getChildren().clear();
            varauksetSivu.getChildren().add(varausTitle);
            varauksetSivu.getChildren().add(varauksetMenu);
            varauksetSivu.getChildren().add(varauksetHaeLomake);
            String hakuId = idHakuTF.getText();
            if(hakuId.length() != 0 || hakuId.trim().length() != 0){
                try {
                    int id = Integer.valueOf(hakuId);
                    ArrayList<Varaus> temp = new ArrayList<Varaus>();
                    for(Varaus v : varaukset){
                        if(v.getVarausId() == id){
                            temp.add(v);
                        }
                    }
                    varauksetSivu.getChildren().add(createVarauksetBox(temp));
                } catch (NumberFormatException ee) {
                    ArrayList<Varaus> temp = new ArrayList<Varaus>();
                    varauksetSivu.getChildren().add(createVarauksetBox(temp));
                }
            }else{
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
                if(mokkiTF.getText().length() == 0 || mokkiTF.getText().trim().length() == 0){
                    mNimi = "";
                }else{
                    mNimi = "^" + mokkiTF.getText();
                }
                if(postinroTF.getText().length() == 0 || postinroTF.getText().trim().length() == 0){
                    postinro = "";
                }else{
                    postinro = "^" + postinroTF.getText();
                }
                
                
                ArrayList<Varaus> temp = new ArrayList<Varaus>();
                for(Varaus v : varaukset){
                    Pattern pattern = Pattern.compile(nimi, Pattern.CASE_INSENSITIVE);
                    Pattern pattern2 = Pattern.compile(mNimi, Pattern.CASE_INSENSITIVE);
                    Pattern pattern3 = Pattern.compile(postinro, Pattern.CASE_INSENSITIVE);

                    Matcher matcher = pattern.matcher(v.getAsiakas().getEtunimi() + "_" + v.getAsiakas().getSukunimi() + ":" + v.getAsiakas().getPuhelinnro());
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
            }
            
        });
        

        return sp;
    }
    private VBox createVarauksetBox(ArrayList<Varaus> param){
        VBox varauksetBox = new VBox();
        varauksetBox.setPadding(new Insets(10,10,10,10));
        if(param.isEmpty()){
            Text noVaraus = new Text("Varauksia ei löytynyt.");
            noVaraus.setFill(Color.BLACK);
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

                String n = v.getMokki().getMokkinimi() + " : " + vAlkuPvm + " - " + vLoppuPvm + " (" + v.getAsiakas().getEtunimi() + " " + v.getAsiakas().getSukunimi() + ")";
                VBox vvTiedot = new VBox();
                Text errMsgM = new Text();
                errMsgM.setFill(Color.RED);
                
                vvTiedot.getChildren().add(errMsgM);
                GridPane vTiedot = new GridPane();
                vTiedot.setHgap(5);
                vTiedot.setVgap(6);
                vTiedot.setPadding(new Insets(15, 5, 15, 5));
            
                Label varauksenIdLabel = new Label("Varauksen ID: ");
                varauksenIdLabel.setTextFill(Color.BLACK);
                Text varausIDText = new Text(String.valueOf(v.getVarausId()));
                varausIDText.setFill(Color.BLACK);
                vTiedot.add(varauksenIdLabel, 0,0);
                vTiedot.add(varausIDText, 1,0);

                Label asiakasLabel = new Label("Asiakas: ");
                asiakasLabel.setTextFill(Color.BLACK);
                if(v.getAsiakas() != null){
                    Text asiakasText = new Text(v.getAsiakas().getEtunimi() + " " + v.getAsiakas().getSukunimi() + " (ID: " + String.valueOf(v.getAsiakas().getAsiakasId() + ")"));
                    asiakasText.setFill(Color.BLACK);
                    vTiedot.add(asiakasLabel, 0,1);
                    vTiedot.add(asiakasText, 1,1);
                }else{
                    Text noAsiakasLabel = new Text("Ei asiakasta");
                    noAsiakasLabel.setFill(Color.BLACK);
                    vTiedot.add(asiakasLabel, 0,1);
                    vTiedot.add(noAsiakasLabel, 1,1);
                }

                Label mokkiLabel = new Label("Mökki: ");
                mokkiLabel.setTextFill(Color.BLACK);
                if(v.getMokki() != null){
                    Text mokkiText = new Text(v.getMokki().getMokkinimi() + " (ID: " + String.valueOf(v.getMokki().getMokkiId()) + ")");
                    mokkiText.setFill(Color.BLACK);
                    vTiedot.add(mokkiLabel, 0,2);
                    vTiedot.add(mokkiText, 1,2);
                }else{
                    Text noMokkiLabel = new Text("Ei mökkiä");
                    noMokkiLabel.setFill(Color.BLACK);
                    vTiedot.add(mokkiLabel, 0,2);
                    vTiedot.add(noMokkiLabel, 1,2);
                }

                Label laskuLabel = new Label("Lasku id: ");
                laskuLabel.setTextFill(Color.BLACK);
                for(Lasku l : laskut){
                    if(l.getVaraus() != null){
                        if(l.getVaraus().getVarausId() == v.getVarausId()){
                            Text laskuText = new Text(String.valueOf(l.getLaskuId()));
                            laskuText.setFill(Color.BLACK);
                            vTiedot.add(laskuLabel, 0,3);
                            vTiedot.add(laskuText, 1,3);
                        }
                    }
                }
      
                GridPane vTiedot2 = new GridPane();
                vTiedot2.setHgap(5);
                vTiedot2.setVgap(6);
                vTiedot2.setPadding(new Insets(15, 5, 15, 5));
                Text varauksenTiedotTitle = new Text("Varauksen tiedot");
                varauksenTiedotTitle.setFill(Color.BLACK);
                vTiedot2.add(varauksenTiedotTitle, 0,0);
                GridPane.setColumnSpan(varauksenTiedotTitle, GridPane.REMAINING);
                Label varattupvmLabel = new Label("Varaus aika: ");
                varattupvmLabel.setTextFill(Color.BLACK);
                Text varattupvmTE = new Text(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(v.getVarattuPvm()));
                varattupvmTE.setFill(Color.BLACK);
                Label vahvistuspvmLabel = new Label("Vahvistus aika: ");
                vahvistuspvmLabel.setTextFill(Color.BLACK);
                if(v.getVahvistusPvm() == null){
                    Text vahvistuspvmTE = new Text("Placeholder");
                    vahvistuspvmTE.setFill(Color.BLACK);
                    vTiedot2.add(vahvistuspvmTE, 1,2);
                }else{
                    Text vahvistuspvmTE = new Text(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(v.getVahvistusPvm()));
                    vahvistuspvmTE.setFill(Color.BLACK);
                    vTiedot2.add(vahvistuspvmTE, 1,2);
                }
                Label varattuAlkuLabel = new Label("Varauksen alku: ");
                varattuAlkuLabel.setTextFill(Color.BLACK);
                Text varattuAlkuTE = new Text(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(v.getVarattuAlkuPvm()));
                varattuAlkuTE.setFill(Color.BLACK);
                Label varattuLoppuLabel = new Label("Varauksen loppu: ");
                varattuLoppuLabel.setTextFill(Color.BLACK);
                Text varattuLoppuTE = new Text(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(v.getVarattuLoppuPvm()));
                varattuLoppuTE.setFill(Color.BLACK);
                Label mokinHintaLabel = new Label("Vuokrahinta: ");
                mokinHintaLabel.setTextFill(Color.BLACK);
                double vuokraSumma = 0;
                for(Lasku l : laskut){
                    if(l.getVaraus() != null){
                        if(l.getVaraus().getVarausId() == v.getVarausId()){
                            vuokraSumma = l.getSumma();
                        }
                    }
                }
                if(vuokraSumma > 0){
                    Text mokinHinta = new Text(String.valueOf(vuokraSumma) + " €");
                    mokinHinta.setFill(Color.BLACK);
                    vTiedot2.add(mokinHinta, 1,5);
                }else{
                    Text mokinHinta = new Text("Ei hintaa");
                    mokinHinta.setFill(Color.BLACK);
                    vTiedot2.add(mokinHinta, 1,5);
                }
                vTiedot2.add(varattupvmLabel, 0,1);
                vTiedot2.add(varattupvmTE, 1,1);
                vTiedot2.add(vahvistuspvmLabel, 0,2);
                vTiedot2.add(varattuAlkuLabel, 0,3);
                vTiedot2.add(varattuAlkuTE, 1,3);
                vTiedot2.add(varattuLoppuLabel, 0,4);
                vTiedot2.add(varattuLoppuTE, 1,4);
                vTiedot2.add(mokinHintaLabel, 0,5);
                VBox varPalVBox = new VBox();
                VBox varPalVBox2 = new VBox();
                varPalVBox.getChildren().add(varPalVBox2);
                Text varPal = new Text("Varauksen palvelut: ");
                varPal.setFill(Color.BLACK);
                varPalVBox2.getChildren().add(varPal);
                if(v.getVarauksenPalvelut().size() != 0){
                    for(VarauksenPalvelu p : v.getVarauksenPalvelut()){
                        VBox vbox = new VBox();
                        vbox.setPadding(new Insets(10,0,10,0));
                        Label pal = new Label(p.getPalvelu().getNimi() + " (" + p.getPalvelu().getHinta() + " €/kpl)");
                        pal.setTextFill(Color.BLACK);
                        Label lkmL = new Label("Lukumäärä: " + String.valueOf(p.getLkm()));
                        lkmL.setTextFill(Color.BLACK);
                        Label kokHinta = new Label("Kokonaishinta: " + String.valueOf(p.getPalvelu().getHinta() * p.getLkm()) + " €");
                        kokHinta.setTextFill(Color.BLACK);
                        Label ajankohtaL = new Label("Ajankohta: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(p.getAjankohta()));
                        ajankohtaL.setTextFill(Color.BLACK);
                        vbox.getChildren().add(pal);
                        vbox.getChildren().add(lkmL);
                        vbox.getChildren().add(kokHinta);
                        vbox.getChildren().add(ajankohtaL);
                        varPalVBox2.getChildren().add(vbox);
                        LocalDate dateNow = LocalDate.now();
                        Date loppuDate = new Date(v.getVarattuLoppuPvm().getTime());
                        LocalDate loppuLocalDate = loppuDate.toLocalDate();
                        if(dateNow.compareTo(loppuLocalDate) <= 0){
                            Button delVarPal = new Button("Poista palvelu");
                            vbox.getChildren().add(delVarPal);
                            delVarPal.setOnAction(e -> {
                                try{
                                    String sql = "DELETE FROM varauksen_palvelut WHERE varaus_id=" + v.getVarausId() + " AND palvelu_id=" + p.getPalvelu().getPalveluId();
                                    Class.forName("com.mysql.cj.jdbc.Driver");
                                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                                    Statement stmt = con.createStatement();
                                    stmt.executeUpdate(sql);
                                    con.close();
                                    varPalVBox2.getChildren().remove(vbox);
                                    v.getVarauksenPalvelut().remove(p);
                                    for(VarauksetPalvelut vp : varauksetPalvelut){
                                        if(vp.getVarausId() == v.getVarausId() && vp.getPalveluId() == p.getPalvelu().getPalveluId()){
                                            varauksetPalvelut.remove(vp);
                                            break;
                                        }
                                    }
                                    if(v.getVarauksenPalvelut().size() == 0){
                                        Text eiVarPal = new Text("Varauksella ei ole palveluita.");
                                        eiVarPal.setFill(Color.BLACK);
                                        varPalVBox2.getChildren().add(eiVarPal);
                                    }
                                }catch(Exception err){
                                    System.out.println(err);
                                }
                            });
                        }

                    }
                }else{
                    Text eiVarPal = new Text("Varauksella ei ole palveluita.");
                    eiVarPal.setFill(Color.BLACK);
                    varPalVBox2.getChildren().add(eiVarPal);
                }

                LocalDate dateNow = LocalDate.now();
                Date loppuDate = new Date(v.getVarattuLoppuPvm().getTime());
                LocalDate loppuLocalDate = loppuDate.toLocalDate();
                if(dateNow.compareTo(loppuLocalDate) <= 0){
                    GridPane newVarPalGrid = new GridPane();
                    newVarPalGrid.setPadding(new Insets(10,0,20,0));
                    newVarPalGrid.setHgap(5);
                    newVarPalGrid.setVgap(6);
                    Label newVarPal = new Label("Lisää uusi palvelu: ");
                    Text newVarPalErr = new Text();
                    Label nVP = new Label("Palvelu: ");
                    ComboBox<String> nCBP = new ComboBox<String>();
                    nCBP.getItems().add("Valitse palvelu");
                    for(Palvelu palvelu : palvelut){
                        if(v.getMokki().getAlue().getAlueId() == palvelu.getAlue().getAlueId()){
                            nCBP.getItems().add(palvelu.getNimi() + " (" + palvelu.getHinta() + " €/kpl)");
                        }
                    }
                    nCBP.setValue("Valitse palvelu");

                    Label nLKM = new Label("Lukumäärä: ");
                    ComboBox<Integer> nCBL = new ComboBox<Integer>();
                    int lask = 1;
                    while(true){
                        nCBL.getItems().add(lask);
                        if(lask == 10){
                            break;
                        }else{
                            lask += 1;
                            continue;
                        }
                    }
                    nCBL.setValue(1);
                    Label nAjankohta = new Label("Päivämäärä: ");
                    DatePicker nAP = new DatePicker();
                    nAP.setDayCellFactory(picker -> new DateCell() {
                        public void updateItem(LocalDate date, boolean empty) {
                            super.updateItem(date, empty);
                            Date alkuDate = new Date(v.getVarattuAlkuPvm().getTime());
                            Date loppuDate = new Date(v.getVarattuLoppuPvm().getTime());
                            setDisable(empty || date.compareTo(alkuDate.toLocalDate()) < 0 || date.compareTo(loppuDate.toLocalDate()) >= 0);
                        }
                    });

                    Label nTimeAjan = new Label("Kellonaika (\"hh:mm\"): ");
                    TextField nTTF = new TextField();

                    Button newVarPalBtn = new Button("Lisää palvelu");

                    newVarPal.setTextFill(Color.BLACK);
                    newVarPalErr.setFill(Color.RED);
                    nVP.setTextFill(Color.BLACK);
                    nLKM.setTextFill(Color.BLACK);
                    nAjankohta.setTextFill(Color.BLACK);
                    nTimeAjan.setTextFill(Color.BLACK);

                    newVarPalGrid.add(newVarPal, 0,0);
                    newVarPalGrid.add(newVarPalErr, 0,1);
                    newVarPalGrid.add(nVP, 0,2);
                    newVarPalGrid.add(nCBP, 1,2);
                    newVarPalGrid.add(nLKM, 0,3);
                    newVarPalGrid.add(nCBL, 1,3);
                    newVarPalGrid.add(nAjankohta, 0,4);
                    newVarPalGrid.add(nAP, 1,4);
                    newVarPalGrid.add(nTimeAjan, 0,5);
                    newVarPalGrid.add(nTTF, 1,5);
                    newVarPalGrid.add(newVarPalBtn, 0,6);

                    varPalVBox.getChildren().add(newVarPalGrid);

                    newVarPalBtn.setOnAction(e -> {
                        String palveCB  = nCBP.getValue();
                        String palvelun_nimi = palveCB.split(" ")[0];
                        Integer lkmInt = nCBL.getValue();
                        LocalDate ajankohtaDate = nAP.getValue();
                        String ajankohtaTime = nTTF.getText();
                        int palvelu_id = -1;
                        java.sql.Timestamp ajanTimeStamp = null;
                        int errors = 0;
                        if(palvelun_nimi.equals("Valitse palvelu")){
                            errors += 1;
                            nVP.setTextFill(Color.RED);
                        }else{
                            for(Palvelu palve : palvelut){
                                if(v.getMokki().getAlue().getAlueId() == palve.getAlue().getAlueId()){
                                    if(palvelun_nimi.equals(palve.getNimi())){
                                        palvelu_id = palve.getPalveluId();
                                    }
                                }
                            }
                        }
                        if(ajankohtaDate == null){
                            errors += 1;
                            nAjankohta.setTextFill(Color.RED);
                        }
                        if(ajankohtaTime == ""){
                            errors += 1;
                            nTimeAjan.setTextFill(Color.RED);
                        }else{
                            if(Integer.valueOf(ajankohtaTime.split(":")[0]) < 0 || Integer.valueOf(ajankohtaTime.split(":")[0]) > 23){
                                errors += 1;
                                nTimeAjan.setTextFill(Color.RED);
                            }
                            if(Integer.valueOf(ajankohtaTime.split(":")[1]) < 0 || Integer.valueOf(ajankohtaTime.split(":")[1]) > 59){
                                errors += 1;
                                nTimeAjan.setTextFill(Color.RED);
                            }
                        }
                        String tempAikaString = ajankohtaTime + ":00";
                        Timestamp testTime = Timestamp.valueOf(ajankohtaDate + " " + tempAikaString);
                        ajanTimeStamp = testTime;
                        if(errors != 0 || ajanTimeStamp == null){
                            newVarPalErr.setText("Virhe! Tarkista syöttämäsi tiedot.");
                        }else{
                            try{
                                String query = "INSERT INTO varauksen_palvelut VALUES (";
                                query += v.getVarausId() + ", ";
                                query += "'" + palvelu_id + "', ";
                                query += "'" + ajanTimeStamp + "', ";
                                query += "'" + lkmInt + "')";
            
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                                Statement stmt = con.createStatement();
                                stmt.executeUpdate(query);
                                con.close();
                                
                                nCBP.setValue("Valitse palvelu");
                                nCBL.setValue(1);
                                nAP.setValue(null);
                                nTTF.setText("");
                                nVP.setTextFill(Color.BLACK);
                                nAjankohta.setTextFill(Color.BLACK);
                                nTimeAjan.setTextFill(Color.BLACK);
                                
                                VarauksetPalvelut temp = new VarauksetPalvelut();
                                temp.setVarausId(v.getVarausId());
                                temp.setPalveluId(palvelu_id);
                                temp.setAjankohta(ajanTimeStamp);
                                temp.setLkm(lkmInt);
                                varauksetPalvelut.add(temp);
    
                                VarauksenPalvelu temp2 = new VarauksenPalvelu();
                                for(Palvelu palvo : palvelut){
                                    if(palvo.getPalveluId() == palvelu_id){
                                        temp2.setPalvelu(palvo);
                                    }
                                }
                                temp2.setAjankohta(ajanTimeStamp);
                                temp2.setLkm(lkmInt);
                                v.addVarauksenPalvelu(temp2);
    
                                VBox vbox = new VBox();
                                vbox.setPadding(new Insets(10,0,10,0));
                                Label pal = new Label("Palvelu: " + palvelun_nimi);
                                pal.setTextFill(Color.BLACK);
                                Label lkmL = new Label("Lukumäärä: " + String.valueOf(lkmInt));
                                lkmL.setTextFill(Color.BLACK);
                                Label ajankohtaL = new Label("Ajankohta: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(ajanTimeStamp));
                                ajankohtaL.setTextFill(Color.BLACK);
                                vbox.getChildren().add(pal);
                                vbox.getChildren().add(lkmL);
                                vbox.getChildren().add(ajankohtaL);
                                varPalVBox2.getChildren().add(vbox);
                                Button delVarPal = new Button("Poista palvelu");
                                vbox.getChildren().add(delVarPal);
    
                                delVarPal.setOnAction(ee -> {
                                    try{
                                        String sql = "DELETE FROM varauksen_palvelut WHERE varaus_id=" + v.getVarausId() + " AND palvelu_id=" + temp.getPalveluId();
                                        Class.forName("com.mysql.cj.jdbc.Driver");
                                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);
                                        Statement stmte = conn.createStatement();
                                        stmte.executeUpdate(sql);
                                        conn.close();
                                        varPalVBox2.getChildren().remove(vbox);
                                        v.getVarauksenPalvelut().remove(temp2);
                                        for(VarauksetPalvelut vp : varauksetPalvelut){
                                            if(vp.getVarausId() == v.getVarausId() && vp.getPalveluId() == temp2.getPalvelu().getPalveluId()){
                                                varauksetPalvelut.remove(vp);
                                                break;
                                            }
                                        }
                                        if(v.getVarauksenPalvelut().size() == 0){
                                            Text eiVarPal = new Text("Varauksella ei ole palveluita.");
                                            eiVarPal.setFill(Color.BLACK);
                                            varPalVBox2.getChildren().add(eiVarPal);
                                        }
                                    }catch(Exception err){
                                        System.out.println(err);
                                    }
                                });
                                newVarPalErr.setText("Palvelu lisätty");
                            }catch(Exception err){
                                System.out.println(err);
                                newVarPalErr.setText("Palvelun lisäämisessä virhe!");
                            }
                        }
                        
                    });
                }

                vvTiedot.getChildren().add(vTiedot);
                vvTiedot.getChildren().add(vTiedot2);
                vvTiedot.getChildren().add(varPalVBox);

                Date vahvDate = new Date(v.getVahvistusPvm().getTime());
                LocalDate vahvLocalDate = vahvDate.toLocalDate();
                if(dateNow.compareTo(vahvLocalDate) <= 0){
                    Button delVaraus = new Button("Poista varaus");
                    vvTiedot.getChildren().add(delVaraus);

                    TitledPane i = new TitledPane(n, vvTiedot);
                    i.setExpanded(false);
                    varauksetBox.getChildren().add(i);

                    delVaraus.setOnAction(e -> {
                        try{
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);

                            String sql2 = "DELETE FROM varauksen_palvelut WHERE varaus_id=" + v.getVarausId();
                            Statement stmt2 = con.createStatement();
                            stmt2.executeUpdate(sql2);

                            String sql3 = "DELETE FROM lasku WHERE varaus_id=" + v.getVarausId();
                            Statement stmt3 = con.createStatement();
                            stmt3.executeUpdate(sql3);

                            String sql = "DELETE FROM varaus WHERE varaus_id=" + v.getVarausId();
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(sql);

                            con.close();

                            for(Lasku l : laskut){
                                if(l.getVaraus() != null){
                                    if(l.getVaraus().getVarausId() == v.getVarausId()){
                                        laskut.remove(l);
                                        break;
                                    }
                                }
                            }
                            varauksetBox.getChildren().remove(i);
                            for(Varaus v2 : varaukset){
                                if(v2.getVarausId() == v.getVarausId()){
                                    varaukset.remove(v);
                                    break;
                                }
                            }
                        }catch(Exception err){
                            System.out.println(err);
                        }
                    });
                }else{
                    TitledPane i = new TitledPane(n, vvTiedot);
                    i.setExpanded(false);
                    varauksetBox.getChildren().add(i);
                }
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
        
        GridPane laskunroHaku = new GridPane();
        laskunroHaku.setHgap(8);
        laskunroHaku.setVgap(4);
        laskunroHaku.setPadding(new Insets(15, 0, 10, 0));
        laskunroHaku.setAlignment(Pos.CENTER);
        Label hakuTeksti = new Label("Hae laskunumerolla:");
        hakuTeksti.setTextFill(Color.BLACK);
        TextField laskuHaku = new TextField();
        Label taiHae = new Label("Tai hae asiakkaan etu- ja/tai sukunimellä: ");
        taiHae.setTextFill(Color.BLACK);
        TextField eNimiHaku = new TextField();
        TextField sNimiHaku = new TextField();

        Button btLaskunroHaku = new Button("Hae");
        
        laskunroHaku.add(hakuTeksti, 0,0);
        laskunroHaku.add(laskuHaku, 1,0);
        laskunroHaku.add(taiHae, 0,1);
        laskunroHaku.add(eNimiHaku, 1,1);
        laskunroHaku.add(sNimiHaku, 2,1);
        laskunroHaku.add(btLaskunroHaku, 0,2);
        laskutSivu.getChildren().add(laskunroHaku);
        
        btLaskunroHaku.setOnAction(e -> {
            laskutSivu.getChildren().clear();
            laskutSivu.getChildren().add(laskuTitle);
            laskutSivu.getChildren().add(laskunroHaku);
           
            if(laskuHaku.getText().trim().length() != 0){
                try{
                    int id = Integer.valueOf(laskuHaku.getText());
                    ArrayList<Lasku> tempLasku = new ArrayList<Lasku>();
                    for(Lasku l : laskut){
                        if (l.getLaskuId() == id){
                            tempLasku.add(l);
                        }

                    }
                    laskutSivu.getChildren().add(createLaskutBox(tempLasku));
                }
                catch(NumberFormatException E){
                    ArrayList<Lasku> tempLasku = new ArrayList<Lasku>();
                    laskutSivu.getChildren().add(createLaskutBox(tempLasku));
                }
                
                
            }else{
                String patternE;
                String patternS;
                if(eNimiHaku.getText().length() == 0 || eNimiHaku.getText().trim().length() == 0){
                    patternE = "";
                }else{
                    patternE = "^" + eNimiHaku.getText();
                }
                if(sNimiHaku.getText().length() == 0 || sNimiHaku.getText().trim().length() == 0){
                    patternS = "";
                }else{
                    patternS = "^" + sNimiHaku.getText();
                }

                ArrayList<Lasku> temp = new ArrayList<Lasku>();
                for(Lasku l : laskut){
                    if(l.getVaraus() == null){
                        continue;
                    }
                    if(l.getVaraus().getAsiakas() == null){
                        continue;
                    }
                    Pattern pattern = Pattern.compile(patternE, Pattern.CASE_INSENSITIVE);
                    Pattern pattern2 = Pattern.compile(patternS, Pattern.CASE_INSENSITIVE);

                    Matcher matcher = pattern.matcher(l.getVaraus().getAsiakas().getEtunimi());
                    Matcher matcher2 = pattern2.matcher(l.getVaraus().getAsiakas().getSukunimi());

                    boolean matchFound = matcher.find();
                    boolean matchFound2 = matcher2.find();
                    if(matchFound && matchFound2){
                            temp.add(l);
                    }
                }
                laskutSivu.getChildren().add(createLaskutBox(temp));
            }
        });
        
        
        laskutSivu.getChildren().add(createLaskutBox(laskut));

        return sp;
    }

    private VBox createLaskutBox(ArrayList<Lasku> param){
        SimpleDateFormat DateFor = new SimpleDateFormat("dd.MM.yyyy");
        VBox laskutBox = new VBox();
        laskutBox.setPadding(new Insets(10,10,10,10));
        
        if(param.isEmpty()){

            Label label = new Label("Laskuja ei löytynyt.");
            laskutBox.getChildren().add(label);
            
        }else{
                
            if(param.size() > 1 ){

                for(Lasku l : param){


                    VBox llTiedot = new VBox();
                    Text errMsgM = new Text();
                    errMsgM.setFill(Color.RED);

                    llTiedot.getChildren().add(errMsgM);
                    GridPane lTiedot = new GridPane();
                    lTiedot.setHgap(5);
                    lTiedot.setVgap(6);
                    lTiedot.setPadding(new Insets(5, 1, 5, 1));

                    String n = "Id: " + l.getLaskuId() + ", ";
                    if(l.getVaraus() == null){
                        n += "Varaus poistettu ";
                    }else{
                        if(l.getVaraus().getAsiakas() == null){
                            n += "Asiakas poistettu ";
                        }else{
                            n += l.getVaraus().getAsiakas().getEtunimi() + " " + l.getVaraus().getAsiakas().getSukunimi();
                        }
                    }
                    n += " (" + String.valueOf(l.getSumma()) + "€)";

                    //@Niko
                    //Hakee tiedon onko lasku maksettu vai ei
                    String maksettu = "";
                    if(l.getMaksettu() == true){
                        maksettu = "Kyllä";
                    } else if(l.getMaksettu() == false){
                        maksettu = "Ei";
                    }

                    String nn = "Laskun päiväys: ";
                    if(l.getLaskupaiva() == null){
                        nn += "Ei maksettu";
                    }else{
                        nn += DateFor.format(l.getLaskupaiva());
                    }

                    nn += "\n\nMaksaja:\n"; 
                    if(l.getVaraus() == null){
                        nn += "Varaus poistettu";
                    }else{
                        if(l.getVaraus().getAsiakas() == null){
                            nn += "Asiakas poistettu";
                        }else{
                            nn += l.getVaraus().getAsiakas().getEtunimi() + " " + l.getVaraus().getAsiakas().getSukunimi();
                        }
                    }
                    nn += "\n\n\n\n Maksettu: " + maksettu;
                    
                    Label lblTied = new Label(nn);
                    llTiedot.getChildren().add(lTiedot);
                    llTiedot.getChildren().add(lblTied);

                    TitledPane i = new TitledPane(n, llTiedot);
                    i.setExpanded(false);
                    laskutBox.getChildren().add(i);

                }
            }
            else{
                for(Lasku l : param){
                //@Niko
                //Hakee tiedon onko lasku maksettu vai ei
                
                //System.out.println(l);
                String maksettu = "";
                if(l.getMaksettu() == true){
                    maksettu = "Kyllä";
                } else if(l.getMaksettu() == false){
                    maksettu = "Ei";
                }

                String laskuPalvelut = "";
                Double laskuSum = 0.0;
                ArrayList<VarauksenPalvelu> palvot = l.getVaraus().getVarauksenPalvelut();
                for(int k = 0; k < l.getVaraus().getVarauksenPalvelut().size(); k++){
                    laskuPalvelut += "\nPalvelu: " + palvot.get(k).getPalvelu().getNimi();
                    laskuPalvelut += "\nPalvelun ajankohta: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(palvot.get(k).getAjankohta());
                    laskuPalvelut += "\nPalvelun määrä: " + String.valueOf(palvot.get(k).getLkm());
                    laskuPalvelut += "\nPalvelun yksikköhinta: " + String.valueOf(palvot.get(k).getPalvelu().getHinta()) + " €";
                    laskuPalvelut += "\nPalvelun kokonaissumma: " + String.valueOf(palvot.get(k).getPalvelu().getHinta() * palvot.get(k).getLkm()) + " €";
                    Double alv = (palvot.get(k).getPalvelu().getHinta() * palvot.get(k).getLkm()) - (palvot.get(k).getPalvelu().getHinta() * palvot.get(k).getLkm() / (1 + (palvot.get(k).getPalvelu().getAlv() / 100)));
                    DecimalFormat alvFor = new DecimalFormat("#.00");
                    laskuPalvelut += "\nAlv kokonaissummasta: " + String.valueOf(alvFor.format(alv)) + " €";
                    laskuPalvelut += "\n";
                    laskuSum += palvot.get(k).getPalvelu().getHinta() * palvot.get(k).getLkm();
                }

                Date alkuDate = new Date(l.getVaraus().getVarattuAlkuPvm().getTime());
                Date loppuDate = new Date(l.getVaraus().getVarattuLoppuPvm().getTime());
                long days = ChronoUnit.DAYS.between(alkuDate.toLocalDate(), loppuDate.toLocalDate());

                Label label = new Label("LASKU\n\n" + "Laskuttaja:\nMökkiläiset\nTestitie 3\n70800 KUOPIO\n\n" 
                        + "Laskun päiväys:\n" + DateFor.format(l.getLaskupaiva()) + "\n\nMaksaja:\n"
                        + l.getVaraus().getAsiakas().getEtunimi() + " " + l.getVaraus().getAsiakas().getSukunimi() + "\n"
                        + l.getVaraus().getAsiakas().getLahiOsoite() + "\n" + l.getVaraus().getAsiakas().getPosti().getPostinro() 
                        + "\n\nLaskun numero:\n" + l.getLaskuId() 
                        + "\n\nLaskuerittely:\n"
                        + "Mökki: " +  l.getVaraus().getMokki().getMokkinimi()
                        + "\nAjankohta: " + DateFor.format(l.getVaraus().getVarattuAlkuPvm()) + "-" + DateFor.format(l.getVaraus().getVarattuLoppuPvm())
                        + "\n\nYhden yön hinta: " + l.getVaraus().getMokki().getHinta() + " €"
                        + "\nÖiden lukumäärä: " + days
                        + "\n\nMökin kokonaissumma: " + l.getSumma() + " €"
                        + "\n\nLisäpalvelut:\n"
                        + laskuPalvelut
                        + "\n\nLaskun kokonaissumma: " + String.valueOf(l.getSumma() + laskuSum) + " €"
                        + "\n\nLaskun eräpäivä:  " + DateFor.format(l.getErapaiva()) 
                        + "\n\nSaajan IBAN:  FI11 1111 1111 1111 11 " 
                        + "\n\nViitenumero: " + l.getViitenumero()
                        + "\n\n\n Maksettu: " + maksettu
                );
                label.setPadding(new Insets(15, 5, 15, 5));

                String n = "Id: " + l.getLaskuId() + ", ";
                    if(l.getVaraus() == null){
                        n += "Varaus poistettu ";
                    }else{
                        if(l.getVaraus().getAsiakas() == null){
                            n += "Asiakas poistettu ";
                        }else{
                            n += l.getVaraus().getAsiakas().getEtunimi() + " " + l.getVaraus().getAsiakas().getSukunimi();
                        }
                    }
                    n += " (" + String.valueOf(l.getSumma()) + "€)";
                                
                VBox lLasku = new VBox();
                
                GridPane laskuNapit = new GridPane();
                laskuNapit.setHgap(8);
                laskuNapit.setVgap(4);
                laskuNapit.setPadding(new Insets(15, 0, 10, 0));
                laskuNapit.setAlignment(Pos.CENTER);
                Button btLasku = new Button("Tulosta lasku");
                Button btLahetaLasku = new Button("Lähetä lasku");
                laskuNapit.add(btLasku, 0,0);
                laskuNapit.add(btLahetaLasku, 1,0);
                
                lLasku.getChildren().add(label);
                lLasku.getChildren().add(laskuNapit);
                
                TitledPane i = new TitledPane(n, lLasku);
                
                i.setExpanded(false);

                laskutBox.getChildren().add(i);
                String sahkopostios = l.getVaraus().getAsiakas().getEmail();
                String mokki = l.getVaraus().getMokki().getMokkinimi();
                String aika = l.getVaraus().getVarattuAlkuPvm() + " - " + l.getVaraus().getVarattuLoppuPvm();
                
                String laskuString = "LASKU\n\n" + "Laskuttaja:\nMökkiläiset\nTestitie 3\n70800 KUOPIO\n\n" 
                + "Laskun päiväys:\n" + DateFor.format(l.getLaskupaiva()) + "\n\nMaksaja:\n"
                + l.getVaraus().getAsiakas().getEtunimi() + " " + l.getVaraus().getAsiakas().getSukunimi() + "\n"
                + l.getVaraus().getAsiakas().getLahiOsoite() + "\n" + l.getVaraus().getAsiakas().getPosti().getPostinro() 
                + "\n\nLaskun numero:\n" + l.getLaskuId() 
                + "\n\nLaskuerittely:\n"
                + "Mökki: " +  l.getVaraus().getMokki().getMokkinimi()
                + "\nAjankohta: " + DateFor.format(l.getVaraus().getVarattuAlkuPvm()) + "-" + DateFor.format(l.getVaraus().getVarattuLoppuPvm())
                + "\n\nYhden yön hinta: " + l.getVaraus().getMokki().getHinta() + " €"
                + "\nÖiden lukumäärä: " + days
                + "\n\nMökin kokonaissumma: " + l.getSumma() + " €"
                + "\n\nLisäpalvelut:\n"
                + laskuPalvelut
                + "\n\nLaskun kokonaissumma: " + String.valueOf(l.getSumma() + laskuSum) + " €"
                + "\n\nLaskun eräpäivä:  " + DateFor.format(l.getErapaiva()) 
                + "\n\nSaajan IBAN:  FI11 1111 1111 1111 11 " 
                + "\n\nViitenumero: " + l.getViitenumero();

                btLasku.setOnAction(e -> {
                    try {
                        laskuTulostus(l.getLaskuId(), laskuString);
                    } catch (IOException E){
                        System.out.println(E);
                    }
                });
                
                btLahetaLasku.setOnAction(e -> {
                    try {
                        LahetaEmail(laskuString, sahkopostios, mokki, aika, true);
                    } catch (IOException E){
                        System.out.println(E);
                    }
                });

                }
            }

        }
        return laskutBox;
    }

    private void laskuTulostus(int lasku_id, String str) throws IOException {
 
        String path = "C://laskut/";
        File folder = new File(path);

        if (!folder.exists()) {
            folder.mkdir();
        }

        File tiedosto = new File(path + "lasku" + lasku_id + ".txt");

        if (tiedosto.createNewFile()) {
            PrintWriter kirjoitusTiedosto = new PrintWriter(tiedosto);
            String Laskuteksti = str;
            kirjoitusTiedosto.print(Laskuteksti + " \n");
            kirjoitusTiedosto.close();
        
        }
        
        if(!Desktop.isDesktopSupported()){  
            System.out.println("not supported");  
            return;  
        }  
        
        Desktop desktop = Desktop.getDesktop();  
        if(tiedosto.exists()) {         
            desktop.open(tiedosto);              
        }  
        
    }
   // TÄSSÄ YRITETTY SAADA SÄHKÖPOSTILLA LÄHETYS TOIMIMAAN MUTTA EI SAATU TOIMIMAAN. EPÄILLÄÄN ETTÄ VIKA YAHOO:SSA.
    private void LahetaEmail(String str, String sahkopostios, String mokki, String aika, boolean flag) throws IOException {
        if(flag){

        }else{
            try
            {
                final String fromEmail = "mokkilaiset@yahoo.com"; 
                final String password = "M0kk1la1s3t"; 
                final String toEmail = "tjokela@student.uef.fi"; 

                System.out.println("TLSEmail Start");
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.mail.yahoo.com"); 
                props.put("mail.smtp.port", "465"); 
                props.put("mail.smtp.auth", "Required"); 
                props.put("mail.smtp.starttls.enable", "true"); 

                System.out.println("Calling getPasswordAuthentication");
                Authenticator auth = new Authenticator() {

                        protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(fromEmail, password);
                        }
                };
                System.out.println("After getPasswordAuthentication");
                
                Session session = Session.getInstance(props, auth);

                String body = str;
                MimeMessage msg = new MimeMessage(session);
                msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
                msg.addHeader("format", "flowed");
                msg.addHeader("Content-Transfer-Encoding", "8bit");

                msg.setFrom(new InternetAddress(fromEmail, "NoReply-JD"));
                msg.setReplyTo(InternetAddress.parse(fromEmail, false));

                msg.setSubject("Laskunne varauksesta " + mokki  + " ajankohdalle " + aika, "UTF-8");
                msg.setText(body, "UTF-8");
                msg.setSentDate(new java.util.Date());
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
                System.out.println("Message is ready");
                Transport.send(msg);

                System.out.println("Email Sent Successfully!!");
            }
            catch (Exception e) {
            e.printStackTrace();
            }
        }
    }

    //UUSI PALVELUT NÄKYMÄ
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

        Label idHakuLabel = new Label("Hae palvelun id:llä: ");
        idHakuLabel.setTextFill(Color.BLACK);
        TextField idHakuTF = new TextField();
        Label taiHae = new Label("Tai hae hakuehdoilla");
        taiHae.setTextFill(Color.BLACK);

        Label alueHakuLabel = new Label("Hae alueella: ");
        alueHakuLabel.setTextFill(Color.BLACK);
        ComboBox<String> alueCB = new ComboBox<String>();
        alueCB.getItems().add("Ei hakuehtoa");
        for(Alue a : alueet){
            String alue = a.getNimi();
            alueCB.getItems().add(alue);
        }
        alueCB.setValue("Ei hakuehtoa");

        Label nimiHaku = new Label("Hae palvelu nimellä: ");
        nimiHaku.setTextFill(Color.BLACK);
        TextField nimiHakuTF = new TextField();

        Button hakuBtn = new Button("Hae palvelua");

        palvelutHaeLomake.add(idHakuLabel, 0,0);
        palvelutHaeLomake.add(idHakuTF, 1,0);
        palvelutHaeLomake.add(taiHae, 0,1);
        palvelutHaeLomake.add(alueHakuLabel, 0,2);
        palvelutHaeLomake.add(alueCB, 1,2);
        palvelutHaeLomake.add(nimiHaku, 0,3);
        palvelutHaeLomake.add(nimiHakuTF, 1,3);
        palvelutHaeLomake.add(hakuBtn, 0,4);
        GridPane.setColumnSpan(hakuBtn, 2);
        
        hakuBtn.setOnAction(e -> {
            palvelutSivu.getChildren().clear();
            palvelutSivu.getChildren().add(palvelutTitle);
            palvelutSivu.getChildren().add(palvelutMenu);
            palvelutSivu.getChildren().add(palvelutHaeLomake);
            String hakuId = idHakuTF.getText();
            if(hakuId.length() != 0 || hakuId.trim().length() != 0){
                try {
                    int id = Integer.valueOf(hakuId);
                    ArrayList<Palvelu> temp = new ArrayList<Palvelu>();
                    for(Palvelu p : palvelut){
                        if(p.getPalveluId() == id){
                            temp.add(p);
                        }
                    }
                    palvelutSivu.getChildren().add(createPalvelutBox(temp));
                } catch (NumberFormatException ee) {
                    ArrayList<Palvelu> temp = new ArrayList<Palvelu>();
                    palvelutSivu.getChildren().add(createPalvelutBox(temp));
                }
            }else{
                String alue;
                String palveluNimi;
                if(alueCB.getValue() == null || alueCB.getValue() == "Ei hakuehtoa"){
                    alue = "";
                }else{
                    alue = alueCB.getValue();
                }
                if(nimiHakuTF.getText().length() == 0 || nimiHakuTF.getText().trim().length() == 0){
                    palveluNimi = "";
                }else{
                    palveluNimi = "^" + nimiHakuTF.getText();
                }

                ArrayList<Palvelu> temp = new ArrayList<Palvelu>();
                for(Palvelu p : palvelut){
                    Pattern pattern = Pattern.compile(alue, Pattern.CASE_INSENSITIVE);
                    Pattern pattern2 = Pattern.compile(palveluNimi, Pattern.CASE_INSENSITIVE);
                
                    Matcher matcher = pattern.matcher(p.getAlue().getNimi());
                    Matcher matcher2 = pattern2.matcher(p.getNimi());

                    boolean matchFound = matcher.find();
                    boolean matchFound2 = matcher2.find();

                    if(matchFound && matchFound2){
                        temp.add(p);
                    }
                }
                palvelutSivu.getChildren().add(createPalvelutBox(temp));
            }
        });
        

        //LUO UUSI PALVELU NÄKYMÄ
        GridPane palvelutLuo = new GridPane();
        palvelutLuo.setAlignment(Pos.CENTER);
        palvelutLuo.setPadding(new Insets(15,0,15,0));
        palvelutLuo.setHgap(6);
        palvelutLuo.setVgap(11);
        Label errMsgU = new Label("");
        errMsgU.setTextFill(Color.RED);

        Label palveluAlueUusiLabel = new Label("Palvelun alue: ");
        palveluAlueUusiLabel.setTextFill(Color.BLACK);
        Label palveluNimiUusiLabel = new Label("Palvelun nimi: ");
        palveluNimiUusiLabel.setTextFill(Color.BLACK);
        //Mikä tämä on??
        Label palveluTyyppiUusiLabel = new Label("Palvelun tyyppi: ");
        palveluTyyppiUusiLabel.setTextFill(Color.BLACK);
        Label palveluKuvausUusiLabel = new Label("Palvelun kuvaus: ");
        palveluKuvausUusiLabel.setTextFill(Color.BLACK);
        Label palveluHintaUusiLabel = new Label("Hinta: "); 
        palveluHintaUusiLabel.setTextFill(Color.BLACK);
        Label palveluAlvUusiLabel = new Label("Alv: ");
        palveluAlvUusiLabel.setTextFill(Color.BLACK);

        setAlueet(getAlueet());

        ComboBox<String> alueUusiCB = new ComboBox<String>();
        alueUusiCB.getItems().add("Valitse alue");
        for(Alue a : alueet){
            String alueNimiCB = a.getNimi();
            alueUusiCB.getItems().add(alueNimiCB);
        }
        alueUusiCB.setValue("Valitse alue");
    
        TextField palveluNimiUusiTxtField = new TextField();
        TextField palveluTyyppiUusiTxtField = new TextField();
        TextField palveluKuvausUusiTxtField = new TextField();
        TextField palveluHintaUusiTxtField = new TextField();
        TextField palveluAlvUusiTxtField = new TextField();
        
        /**
         * Lisätään yllä luodut näkymään
         */
        palvelutLuo.add(errMsgU, 0,0);

        palvelutLuo.add(palveluAlueUusiLabel, 0,1);
        palvelutLuo.add(alueUusiCB, 0,2);

        palvelutLuo.add(palveluNimiUusiLabel, 1, 1);
        palvelutLuo.add(palveluNimiUusiTxtField, 1, 2);

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

            idHakuTF.setText("");
            alueCB.setValue("Ei hakuehtoa");
            nimiHakuTF.setText("");
        });

        listPalvelut.setOnAction(e -> {
            palvelutSivu.getChildren().clear();
            palvelutSivu.getChildren().add(palvelutTitle);
            palvelutSivu.getChildren().add(palvelutMenu);
            palvelutSivu.getChildren().add(palvelutHaeLomake);
            palvelutSivu.getChildren().add(createPalvelutBox(palvelut));

            palveluAlueUusiLabel.setTextFill(Color.BLACK);
            palveluNimiUusiLabel.setTextFill(Color.BLACK);
            palveluTyyppiUusiLabel.setTextFill(Color.BLACK);
            palveluKuvausUusiLabel.setTextFill(Color.BLACK);
            palveluHintaUusiLabel.setTextFill(Color.BLACK);
            palveluAlvUusiLabel.setTextFill(Color.BLACK);

            errMsgU.setText("");
            errMsgU.setTextFill(Color.RED);
            alueUusiCB.setValue("Valitse alue");
            palveluNimiUusiTxtField.setText("");
            palveluTyyppiUusiTxtField.setText("");
            palveluKuvausUusiTxtField.setText("");
            palveluHintaUusiTxtField.setText("");
            palveluAlvUusiTxtField.setText("");
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
            if(alueNimiUusi == "Valitse alue"){
                errors += 1;
                palveluAlueUusiLabel.setTextFill(Color.RED);
            }
            if(palveluNimiUusi.length() == 0 || palveluNimiUusi.trim().length() == 0 || palveluNimiUusi.length() > 40){
                errors += 1;
                palveluNimiUusiLabel.setTextFill(Color.RED);
            }
            if(palveluTyyppiUusi.length() == 0 || palveluTyyppiUusi.trim().length() == 0 || palveluTyyppiUusi.length() > 25){
                errors += 1;
                palveluTyyppiUusiLabel.setTextFill(Color.RED);
            }
            if(palveluKuvausUusi.length() == 0 || palveluKuvausUusi.trim().length() == 0 || palveluKuvausUusi.length() > 225){
                errors += 1;
                palveluKuvausUusiLabel.setTextFill(Color.RED);
            }
            if(palveluHintaUusi.length() == 0 || palveluHintaUusi.trim().length() == 0 || palveluHintaUusi.length() > 5){
                errors += 1;
                palveluHintaUusiLabel.setTextFill(Color.RED);
            }else{
                try{
                    Double hintaUusi = Double.parseDouble(palveluHintaUusi);
                } catch(NumberFormatException ee){
                    errors += 1;
                    palveluHintaUusiLabel.setTextFill(Color.RED);
                }
            }
            if(palveluAlvUusi.length() == 0 || palveluAlvUusi.trim().length() == 0 || palveluAlvUusi.length() > 5){
                errors += 1;
                palveluAlvUusiLabel.setTextFill(Color.RED);
            }else{
                try{
                    Double alvUusi = Double.parseDouble(palveluAlvUusi);
                }catch(NumberFormatException ee){
                    errors += 1;
                    palveluAlvUusiLabel.setTextFill(Color.RED);
                }
            }
            
            /*TOINEN TAPA TALLENTAA MYSQL
            TÄÄ TOIMII MULLA
            */
            if(errors != 0){
                errMsgU.setText("Virhe! Tarkista syöttämäsi tiedot.");
                errMsgU.setTextFill(Color.RED);
            }else {
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
                    alueUusiCB.setValue("Valitse alue");
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
                
                String n = "Id: " + p.getPalveluId() + ", " + p.getNimi();
                if(p.getAlue() != null){
                    n += " (" + p.getAlue().getNimi() + ")";
                }
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
                    palveluNimiLabel.setTextFill(Color.BLACK);
                    TextField palveluNimiTxtField = new TextField(p.getNimi());
                    pTiedot.add(palveluNimiLabel, 0 ,0);
                    pTiedot.add(palveluNimiTxtField, 0,1);

                    Label palveluAlueLabel = new Label("Palvelun alue: ");
                    palveluAlueLabel.setTextFill(Color.BLACK);
                    ComboBox<String> palveluAlueCB = new ComboBox<String>();
                    if(p.getAlue() == null){
                        palveluAlueCB.getItems().add("Valitse alue");
                        palveluAlueCB.setValue("Valitse alue");
                        for(Alue a : alueet){
                            palveluAlueCB.getItems().add(a.getNimi());
                        }
                    }else{
                        for(Alue a : alueet){
                            palveluAlueCB.getItems().add(a.getNimi());
                        }
                        palveluAlueCB.setValue(p.getAlue().getNimi());
                    }
                    pTiedot.add(palveluAlueLabel, 1,0);
                    pTiedot.add(palveluAlueCB, 1,1);


                    Label palveluTyyppiLabel = new Label("Tyyppi: ");
                    palveluTyyppiLabel.setTextFill(Color.BLACK);
                    TextField palveluTyyppiTxtField = new TextField(String.valueOf(p.getTyyppi()));
                    pTiedot.add(palveluTyyppiLabel, 1, 2);
                    pTiedot.add(palveluTyyppiTxtField, 1, 3);
                        
                    Label palveluKuvausLabel = new Label("Kuvaus: ");
                    palveluKuvausLabel.setTextFill(Color.BLACK);
                    TextField palveluKuvausTxtField = new TextField(p.getKuvaus());
                    pTiedot.add(palveluKuvausLabel, 0, 2);
                    pTiedot.add(palveluKuvausTxtField, 0, 3);

                    Label palveluHintaLabel = new Label("Hinta: ");
                    palveluHintaLabel.setTextFill(Color.BLACK);
                    TextField palveluHintaTxtField = new TextField(String.valueOf(p.getHinta()));
                    pTiedot.add(palveluHintaLabel, 1, 4);
                    pTiedot.add(palveluHintaTxtField, 1, 5);

                    Label palveluAlvLabel = new Label("ALV: ");
                    palveluAlvLabel.setTextFill(Color.BLACK);
                    TextField palveluAlvTxtField = new TextField(String.valueOf(p.getAlv()));
                    pTiedot.add(palveluAlvLabel,0, 4);
                    pTiedot.add(palveluAlvTxtField, 0, 5);
                    
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

                        
                        String palveluMuokattuNimi = palveluNimiTxtField.getText();
                        String palveluMuokattuAlue = palveluAlueCB.getValue();
                        int palveluMuokattuAlueId = 0;

                        String palveluMuokattuTyyppi = palveluTyyppiTxtField.getText();
                        String palveluMuokattuKuvaus = palveluKuvausTxtField.getText();
                        String palveluMuokattuHinta = palveluHintaTxtField.getText();
                        String palveluMuokattuAlv = palveluAlvTxtField.getText();
                        int tyyppi = 1;
                        System.out.println(palveluMuokattuAlue);

                        palveluAlueLabel.setTextFill(Color.BLACK);
                        palveluNimiLabel.setTextFill(Color.BLACK);
                        palveluTyyppiLabel.setTextFill(Color.BLACK);
                        palveluKuvausLabel.setTextFill(Color.BLACK);
                        palveluHintaLabel.setTextFill(Color.BLACK);
                        palveluAlvLabel.setTextFill(Color.BLACK);

                        if(palveluMuokattuNimi.length() == 0 || palveluMuokattuNimi.trim().length() == 0 || palveluMuokattuNimi.length() > 40){
                            errors += 1;
                            palveluNimiLabel.setTextFill(Color.RED);
                        }
                        if(palveluMuokattuAlue == "Valitse alue"){
                            errors += 1;
                            palveluAlueLabel.setTextFill(Color.RED);
                        }else{
                            for(Alue a : alueet){
                                if(a.getNimi().equals(palveluMuokattuAlue)){
                                    palveluMuokattuAlueId = a.getAlueId();
                                    System.out.println(palveluMuokattuAlueId);
                                }
                            }
                            if(palveluMuokattuAlueId == 0){
                                errors += 1;
                                palveluAlueLabel.setTextFill(Color.RED);
                            }
                        }
                        if(palveluMuokattuTyyppi.length() == 0 || palveluMuokattuTyyppi.trim().length() == 0 || palveluMuokattuTyyppi.length() > 25){
                            errors += 1;
                            palveluTyyppiLabel.setTextFill(Color.RED);
                        }
                        if(palveluMuokattuKuvaus.length() == 0 || palveluMuokattuKuvaus.trim().length() == 0 || palveluMuokattuKuvaus.length() > 225){
                            errors += 1;
                            palveluKuvausLabel.setTextFill(Color.RED);
                        }
                        if(palveluMuokattuHinta.length() == 0 || palveluMuokattuHinta.trim().length() == 0 || palveluMuokattuHinta.length() > 5){
                            errors += 1;
                            palveluHintaLabel.setTextFill(Color.RED);
                        }else{
                            try{
                                Double muokattuHinta = Double.parseDouble(palveluMuokattuHinta);
                            }catch(NumberFormatException ee){
                                errors += 1;
                                palveluHintaLabel.setTextFill(Color.RED);
                            }
                        }
                        if(palveluMuokattuAlv.length() == 0 || palveluMuokattuAlv.trim().length() == 0 || palveluMuokattuAlv.length() > 5){
                            errors += 1;
                            palveluAlvLabel.setTextFill(Color.RED);
                        }else{
                            try{
                                Double muokattuAlv = Double.parseDouble(palveluMuokattuAlv);
                            }catch(NumberFormatException ee){
                                errors += 1;
                                palveluAlvLabel.setTextFill(Color.RED);
                            }
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

                                p.setNimi(palveluMuokattuNimi);
                                p.setTyyppi(palveluMuokattuTyyppi);
                                p.setKuvaus(palveluMuokattuKuvaus);
                                p.setHinta(Double.parseDouble(palveluMuokattuHinta));
                                p.setAlv(Double.parseDouble(palveluMuokattuAlv));
                                errMsgM.setText("Muutokset tallennettu");
                                errMsgM.setFill(Color.GREEN);
                                i.setText("Id: " + p.getPalveluId() + ", " + p.getNimi() + " (" + p.getAlue().getNimi() + ")");
                                
                            }catch(Exception err){
                                System.out.println(err);
                                errMsgM.setText("Muutosten tallennuksessa virhe!");
                                errMsgM.setFill(Color.RED);
                            }
                        }else{
                            errMsgM.setText("Virhe! Tarkista syöttämäsi tiedot.");
                            errMsgM.setFill(Color.RED);
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

            Label idHakuLabel = new Label("Hae mökin id:llä: ");
            idHakuLabel.setTextFill(Color.BLACK);
            TextField idHakuTF = new TextField();
            Label taiHae = new Label("Tai hae hakuehdoilla");
            taiHae.setTextFill(Color.BLACK);

            Label alueHakuLabel = new Label("Hae alueella: ");
            alueHakuLabel.setTextFill(Color.BLACK);
            ComboBox<String> alueCB = new ComboBox<String>();
            alueCB.getItems().add("Ei hakuehtoa");
            for(Alue a : alueet){
                String alue = a.getNimi();
                alueCB.getItems().add(alue);
            }
            alueCB.setValue("Ei hakuehtoa");

            Label postinroHaku = new Label("Hae mökin postinumerolla: ");
            postinroHaku.setTextFill(Color.BLACK);
            TextField postinroHakuTF = new TextField();

            Label nimiHaku = new Label("Hae mökin nimellä: ");
            nimiHaku.setTextFill(Color.BLACK);
            TextField nimiHakuTF = new TextField();


            Button hakuBtn = new Button("Hae mökkiä");

            mokitHaeLomake.add(idHakuLabel, 0,0);
            mokitHaeLomake.add(idHakuTF, 1,0);
            mokitHaeLomake.add(taiHae, 0,1);
            mokitHaeLomake.add(alueHakuLabel, 0,2);
            mokitHaeLomake.add(alueCB, 1,2);
            mokitHaeLomake.add(postinroHaku, 0,3);
            mokitHaeLomake.add(postinroHakuTF, 1,3);
            mokitHaeLomake.add(nimiHaku, 0,4);
            mokitHaeLomake.add(nimiHakuTF, 1,4);
            mokitHaeLomake.add(hakuBtn, 0,5);
            GridPane.setColumnSpan(hakuBtn, 2);

            hakuBtn.setOnAction(e -> {
                mokitSivu.getChildren().clear();
                mokitSivu.getChildren().add(mokitTitle);
                mokitSivu.getChildren().add(mokitMenu);
                mokitSivu.getChildren().add(mokitHaeLomake);
                String hakuId = idHakuTF.getText();
                if(hakuId.length() != 0 || hakuId.trim().length() != 0){
                    try {
                        int id = Integer.valueOf(hakuId);
                        ArrayList<Mokki> temp = new ArrayList<Mokki>();
                        for(Mokki m : mokit){
                            if(m.getMokkiId() == id){
                                temp.add(m);
                            }
                        }
                        mokitSivu.getChildren().add(createMokitBox(temp));
                    } catch (NumberFormatException ee) {
                        ArrayList<Mokki> temp = new ArrayList<Mokki>();
                        mokitSivu.getChildren().add(createMokitBox(temp));
                    }
                }else{
                    String alue;
                    String postinro;
                    String mokkiNimi;
                    if(alueCB.getValue() == null || alueCB.getValue() == "Ei hakuehtoa"){
                        alue = "";
                    }else{
                        alue = alueCB.getValue();
                    }
                    if(postinroHakuTF.getText().length() == 0 || postinroHakuTF.getText().trim().length() == 0){
                        postinro = "";
                    }else{
                        postinro = postinroHakuTF.getText();
                    }
                    if(nimiHakuTF.getText().length() == 0 || nimiHakuTF.getText().trim().length() == 0){
                        mokkiNimi = "";
                    }else{
                        mokkiNimi = "^" + nimiHakuTF.getText();
                    }

                    ArrayList<Mokki> temp = new ArrayList<Mokki>();
                    for(Mokki m : mokit){
                        Pattern pattern = Pattern.compile(alue, Pattern.CASE_INSENSITIVE);
                        Pattern pattern2 = Pattern.compile(postinro, Pattern.CASE_INSENSITIVE);
                        Pattern pattern3 = Pattern.compile(mokkiNimi, Pattern.CASE_INSENSITIVE);
                    
                        Matcher matcher = pattern.matcher(m.getAlue().getNimi());
                        Matcher matcher2 = pattern2.matcher(m.getPosti().getPostinro());
                        Matcher matcher3 = pattern3.matcher(m.getMokkinimi());

                        boolean matchFound = matcher.find();
                        boolean matchFound2 = matcher2.find();
                        boolean matchFound3 = matcher3.find();

                        if(matchFound && matchFound2 && matchFound3){
                            temp.add(m);
                        }
                    }
                    mokitSivu.getChildren().add(createMokitBox(temp));
                }
            });
            

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
            mokkiNimiUusiLabel.setTextFill(Color.BLACK);
            Label mokkiKatuosoiteUusi = new Label("Katuosoite: ");
            mokkiKatuosoiteUusi.setTextFill(Color.BLACK);
            Label mokkiAlueUusi = new Label("Alue: ");
            mokkiAlueUusi.setTextFill(Color.BLACK);
            Label mokkiPostinroUusi = new Label("Postinro: "); 
            mokkiPostinroUusi.setTextFill(Color.BLACK);
            Label mokkiHintaUusi = new Label("Hinta: ");
            mokkiHintaUusi.setTextFill(Color.BLACK);
            Label mokkiHenklkmUusi = new Label("Henkilömäärä: ");
            mokkiHenklkmUusi.setTextFill(Color.BLACK);
            Label mokkiKuvausUusi = new Label("Kuvaus: ");
            mokkiKuvausUusi.setTextFill(Color.BLACK);
            Label mokkiVarusteluUusi = new Label("Varustelu: ");
            mokkiVarusteluUusi.setTextFill(Color.BLACK);
            
            TextField mokkiNimiUusiTxtField = new TextField();
            TextField mokkiKatuosoiteUusiTxtField = new TextField();

            /**
             * Päivittää alueen tiedot comboboxia varten
             */
            setAlueet(getAlueet());

            ComboBox<String> alueUusiCB = new ComboBox<String>();
            alueUusiCB.getItems().add("Valitse alue");
            for(Alue a : alueet){
                String alueNimiCB = a.getNimi();
                alueUusiCB.getItems().add(alueNimiCB);
            }
            alueUusiCB.setValue("Valitse alue");
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

                idHakuTF.setText("");
                alueCB.setValue("Ei hakuehtoa");
                postinroHakuTF.setText("");
                nimiHakuTF.setText("");
            });

            listMokit.setOnAction(e -> {
                mokitSivu.getChildren().clear();
                mokitSivu.getChildren().add(mokitTitle);
                mokitSivu.getChildren().add(mokitMenu);
                mokitSivu.getChildren().add(mokitHaeLomake);
                mokitSivu.getChildren().add(createMokitBox(mokit));

                mokkiNimiUusiLabel.setTextFill(Color.BLACK);
                mokkiKatuosoiteUusi.setTextFill(Color.BLACK);
                mokkiPostinroUusi.setTextFill(Color.BLACK);
                mokkiHintaUusi.setTextFill(Color.BLACK);
                mokkiHenklkmUusi.setTextFill(Color.BLACK);
                mokkiKuvausUusi.setTextFill(Color.BLACK);
                mokkiVarusteluUusi.setTextFill(Color.BLACK);

                errMsgU.setText("");
                errMsgU.setTextFill(Color.RED);
                alueUusiCB.setValue("Valitse alue");
                mokkiNimiUusiTxtField.setText("");
                mokkiKatuosoiteUusiTxtField.setText("");
                mokkiPostinumeroUusiTxtField.setText("");
                mokkiHintaUusiTxtField.setText("");
                mokkiHenklkmUusiTxtField.setText("");
                mokkiKuvausUusiTxtField.setText("");
                mokkiVarusteluUusiTxtField.setText("");

            });

            
            tallennaMokkiBtn.setOnAction(e -> {
                int errors = 0;
                String mokkiNimiuusi = mokkiNimiUusiTxtField.getText();
                System.out.println(mokkiNimiuusi);
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
                if(mokkiPostinro.length() != 5){
                    errors += 1;
                    mokkiPostinroUusi.setTextFill(Color.RED);
                }else{
                    boolean flag = false;
                    for(int i = 0; i < 5; i++){
                        if(Character.isDigit(mokkiPostinro.charAt(i))){
        
                        }else{
                            flag = true;
                        }
                    }
                    if(flag){
                        errors += 1;
                        mokkiPostinroUusi.setTextFill(Color.RED);
                    }
                }
                boolean flag = true;
                for(Posti p : postit){
                    if(p.getPostinro().equals(mokkiPostinro)){
                        flag = false;
                    }
                }
                if(flag){
                    errors += 1;
                    mokkiPostinroUusi.setTextFill(Color.RED);
                }
                if(mokkiNimiuusi.length() == 0 || mokkiNimiuusi.trim().length() == 0 || mokkiNimiuusi.length() > 45){
                    errors += 1;
                    mokkiNimiUusiLabel.setTextFill(Color.RED);
                }
                if(alueNimi == "Valitse alue"){
                    errors += 1;
                    mokkiAlueUusi.setTextFill(Color.RED);
                }
                if(mokkiKatuosoite.length() == 0 || mokkiKatuosoite.trim().length() == 0 || mokkiKatuosoite.length() > 45){
                    errors += 1;
                    mokkiKatuosoiteUusi.setTextFill(Color.RED);
                }
                if(mokkiHinta.length() == 0 || mokkiHinta.trim().length() == 0 || mokkiHinta.length() > 5){
                    errors += 1;
                    mokkiHintaUusi.setTextFill(Color.RED);
                }else{
                    try{
                        Double hintaUusi = Double.parseDouble(mokkiHinta);
                    }catch(NumberFormatException ee){
                        errors += 1;
                        mokkiHintaUusi.setTextFill(Color.RED);
                    }
                }
                if(mokkiHenkLkm.length() == 0 || mokkiHenkLkm.trim().length() == 0 || mokkiHenkLkm.length() > 3){
                    errors += 1;
                    mokkiHenklkmUusi.setTextFill(Color.RED);
                }else{
                    try{
                        int henkLkm = Integer.valueOf(mokkiHenkLkm);
                    }catch(NumberFormatException ee){
                        errors += 1;
                        mokkiHenklkmUusi.setTextFill(Color.RED);
                    }
                }
                if(mokkiKuvaus.length() == 0 || mokkiKuvaus.trim().length() == 0 || mokkiKuvaus.length() > 150){
                    errors += 1;
                    mokkiKuvausUusi.setTextFill(Color.RED);
                }
                if(mokkiVarustelu.length() == 0 || mokkiVarustelu.trim().length() == 0 || mokkiVarustelu.length() > 100){
                    errors += 1;
                    mokkiVarusteluUusi.setTextFill(Color.RED);
                }
                
                /*TOINEN TAPA TALLENTAA MYSQL
                TÄÄ TOIMII MULLA
                */
                if(errors != 0){

                }else{
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
    
                }
            });

            mokitSivu.getChildren().add(mokitHaeLomake);
            mokitSivu.getChildren().add(createMokitBox(mokit));  
        return sp;  
    }
    private VBox createMokitBox(ArrayList<Mokki> param){
        VBox mokitBox = new VBox();
        mokitBox.setPadding(new Insets(10,10,10,10));
        if(param.isEmpty()){
            Text noMokki = new Text("Mökkejä ei löytynyt.");
            mokitBox.getChildren().add(noMokki);
        }else{
            for(Mokki m : param){
                String n = "Id: " + m.getMokkiId() + ", " + m.getMokkinimi();
                VBox mmTiedot = new VBox();
                mmTiedot.setPadding(new Insets(10,5,10,5));

                DatePicker mokkiDP = new DatePicker();
                mokkiDP.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        for(Varaus v : varaukset){
                            if(v.getMokki().getMokkiId() == m.getMokkiId()){
                                super.updateItem(date, empty);
                                Date alkuDate = new Date(v.getVarattuAlkuPvm().getTime());
                                Date loppuDate = new Date(v.getVarattuLoppuPvm().getTime());

                                if(date.compareTo(alkuDate.toLocalDate()) >= 0 && date.compareTo(loppuDate.toLocalDate()) <= 0){
                                    setDisable(true);
                                }
                            }
                        }
                    }
                });
                DatePickerSkin mokkiDPSkin = new DatePickerSkin(mokkiDP);
                Node popupContent = mokkiDPSkin.getPopupContent();
                mmTiedot.getChildren().add(popupContent);

                Text errMsgM = new Text();
                errMsgM.setFill(Color.RED);
                mmTiedot.getChildren().add(errMsgM);
                GridPane mTiedot = new GridPane();
                mTiedot.setHgap(5);
                mTiedot.setVgap(6);
                mTiedot.setPadding(new Insets(15, 5, 15, 5));
                if(m != null){
                    Label mokkiNimiLabel = new Label("Mökin nimi: ");
                    mokkiNimiLabel.setTextFill(Color.BLACK);
                    TextField mokkiNimiTxtField = new TextField(m.getMokkinimi());
                    mTiedot.add(mokkiNimiLabel, 0 ,0);
                    mTiedot.add(mokkiNimiTxtField, 0,1);
                    
                    /**
                     * Alue näytetään, mutta sitä ei voi muokata
                    **/
                    Label mokkiAlue = new Label("Alue: ");
                    mokkiAlue.setTextFill(Color.BLACK);
                    TextField mokkiAlueTxtField = new TextField(m.getAlue().getNimi());
                    mokkiAlueTxtField.setEditable(false);
                    mTiedot.add(mokkiAlue, 1, 0);
                    mTiedot.add(mokkiAlueTxtField, 1, 1);
                    
                    Label mokkiOsoite = new Label("Katuosoite: ");
                    mokkiOsoite.setTextFill(Color.BLACK);
                    TextField mokkiOsoiteTxtField = new TextField(m.getKatuosoite());
                    mTiedot.add(mokkiOsoite, 0, 2);
                    mTiedot.add(mokkiOsoiteTxtField, 0, 3);
                
                    Label mokkiPostinro = new Label("Postinumero: ");
                    mokkiPostinro.setTextFill(Color.BLACK);
                    TextField mokkiPostinumeroTxtField = new TextField(m.getPosti().getPostinro());
                    mokkiPostinumeroTxtField.setEditable(false);
                    mTiedot.add(mokkiPostinro, 1, 2);
                    mTiedot.add(mokkiPostinumeroTxtField, 1, 3);

                    Label mokkiHinta = new Label("Hinta: ");
                    mokkiHinta.setTextFill(Color.BLACK);
                    TextField mokkiHintaTxtField = new TextField(String.valueOf(m.getHinta()));
                    mTiedot.add(mokkiHinta,0, 4);
                    mTiedot.add(mokkiHintaTxtField, 0, 5);

                    Label mokkiHenkmaara = new Label("Henkilömäärä: ");
                    mokkiHenkmaara.setTextFill(Color.BLACK);
                    TextField mokkiHenkmaaraTxtField = new TextField(String.valueOf(m.getHenkilomaara()));
                    mTiedot.add(mokkiHenkmaara, 1, 4);
                    mTiedot.add(mokkiHenkmaaraTxtField, 1, 5);

                    Label mokkiKuvaus = new Label("Kuvaus: ");
                    mokkiKuvaus.setTextFill(Color.BLACK);
                    TextField mokkiKuvausTxtField = new TextField(m.getKuvaus());
                    mTiedot.add(mokkiKuvaus, 0, 6);
                    mTiedot.add(mokkiKuvausTxtField, 0, 7);

                    Label mokkiVarustelu = new Label("Mökin varustelu: ");
                    mokkiVarustelu.setTextFill(Color.BLACK);
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
                    
                    /**
                    * Poistaa valitun mökin tietokannasta ja ohjelman mökkien listauksesta.
                    *
                    **/
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
                        String mokkiMuokattuAlue = mokkiAlueTxtField.getText();

                        String mokkiMuokattuOsoite = mokkiOsoiteTxtField.getText();
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

                        if(mokkiMuokattuNimi.length() == 0 || mokkiMuokattuNimi.trim().length() == 0 || mokkiMuokattuNimi.length() > 45){
                            errors += 1;
                            mokkiNimiLabel.setTextFill(Color.RED);
                        }
                        if(mokkiMuokattuOsoite.length() == 0 || mokkiMuokattuOsoite.trim().length() == 0 || mokkiMuokattuOsoite.length() > 45){
                            errors += 1;
                            mokkiOsoite.setTextFill(Color.RED);
                        }
                        if(mokkiMuokattuHinta.length() == 0 || mokkiMuokattuHinta.trim().length() == 0 || mokkiMuokattuHinta.length() > 5){
                            errors += 1;
                            mokkiHinta.setTextFill(Color.RED);
                        }
                        if(mokkiMuokattuHenkmaara.length() == 0 || mokkiMuokattuHenkmaara.trim().length() == 0 || mokkiMuokattuHenkmaara.length() > 3){
                            errors += 1;
                            mokkiHenkmaara.setTextFill(Color.RED);
                        }
                        if(mokkiMuokattuKuvaus.length() == 0 || mokkiMuokattuKuvaus.trim().length() == 0 || mokkiMuokattuKuvaus.length() > 150){
                            errors += 1;
                            mokkiKuvaus.setTextFill(Color.RED);
                        }
                        if(mokkiMuokattuVarustelu.length() == 0 || mokkiMuokattuVarustelu.trim().length() == 0 || mokkiMuokattuVarustelu.length() > 100){
                            errors += 1;
                            mokkiVarustelu.setTextFill(Color.RED);
                        }
        
                        if(errors == 0){
                            if(m.checkCopy(mokkiMuokattuAlue, mokkiMuokattuPostinro, mokkiMuokattuNimi, mokkiMuokattuOsoite)){
                                
                            }else{
                                try{

                                    Class.forName("com.mysql.cj.jdbc.Driver");
                                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkilaiset", HOST_USER, HOST_PSWD);

                                    String sql =  "UPDATE mokki SET mokkinimi = ?, hinta = ?, kuvaus = ?, henkilomaara = ?, varustelu = ? WHERE mokki_id = ?";
                                    PreparedStatement update = conn.prepareStatement(sql);
                                    
                                
                                    update.setString(1, mokkiMuokattuNimi);
                                    update.setDouble(2, Double.parseDouble(mokkiMuokattuHinta));
                                    update.setString(3, mokkiMuokattuKuvaus);
                                    update.setString(4, mokkiMuokattuHenkmaara);
                                    update.setString(5, mokkiMuokattuVarustelu);
                                    update.setInt(6, m.getMokkiId());
                            
                                    update.executeUpdate();
                                    
                                    conn.close();
                                
                                    m.setMokkinimi(mokkiMuokattuNimi);
                                    m.setHinta(Double.parseDouble(mokkiMuokattuHinta));
                                    m.setKuvaus(mokkiMuokattuKuvaus);
                                    m.setHenkilomaara(Integer.parseInt(mokkiMuokattuHenkmaara));
                                    m.setVarustelu(mokkiMuokattuVarustelu);
                                    errMsgM.setText("Muutokset tallennettu");
                                    errMsgM.setFill(Color.GREEN);
                                    i.setText("Id: " + m.getMokkiId() + ", " + m.getMokkinimi());
                                
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
        return mokitBox;
    }

    /**
        * Luodaan alueet näkymä
    **/
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


        // Label mokkiIdUusi = new Label("Mökin id:"); //TULEEKO AUTOMAATTISESTI?
        Label alueNimiUusiLabel = new Label("Alueen nimi: ");
        alueNimiUusiLabel.setTextFill(Color.BLACK);
        TextField alueNimiUusiTxtField = new TextField();
            
            
        /**
            * Lisätään yllä luodut näkymään
        **/
        alueetLuo.add(errMsgU, 0,0);

        alueetLuo.add(alueNimiUusiLabel, 0,1);
        alueetLuo.add(alueNimiUusiTxtField, 0,2);


        Button tallennaAlueBtn = new Button("Tallenna");
        alueetLuo.add(tallennaAlueBtn, 0,3);
            
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

            errMsgU.setText("");
            errMsgU.setTextFill(Color.RED);
            alueNimiUusiLabel.setTextFill(Color.BLACK);
            alueNimiUusiTxtField.setText("");
        });
            
        tallennaAlueBtn.setOnAction(e -> {
            int errors = 0;
            String alueNimiuusi = alueNimiUusiTxtField.getText();
            errMsgU.setTextFill(Color.RED);
            alueNimiUusiLabel.setTextFill(Color.BLACK);
        
            if(alueNimiuusi.length() == 0){
                errors += 1;
                alueNimiUusiLabel.setTextFill(Color.RED);
            }
            /*TOINEN TAPA TALLENTAA MYSQL
            TÄÄ TOIMII MULLA
            */
            if(errors != 0){
                errMsgU.setText("Virhe! Tarkista syöttämäsi tiedot.");
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
                    System.out.println(err);
                    errMsgU.setText("Alueen tallennuksessa virhe!");
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
                    
                String n = "Id: " + al.getAlueId() + ", " + al.getNimi();
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
                    alueNimiLabel.setTextFill(Color.BLACK);
                    TextField alueNimiTxtField = new TextField(al.getNimi());
                    alueTiedot.add(alueNimiLabel, 0, 0);
                    alueTiedot.add(alueNimiTxtField, 0, 1);
    
                    alTiedot.getChildren().add(alueTiedot);
                    TitledPane i = new TitledPane(n, alTiedot);
                    i.setExpanded(false);
                    alueetBox.getChildren().add(i);
                
                    Button saveEditBtn = new Button("Tallenna");
                    alueTiedot.add(saveEditBtn, 0,2);
                    Button deleteAlueBtn = new Button("Poista");
                    alueTiedot.add(deleteAlueBtn, 1,2);
                    
        
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
                                    alueet.remove(al);
                                    break;
                                }
                            }
                            for(Palvelu p : palvelut){
                                if(p.getAlue().getAlueId() == al.getAlueId()){
                                    p.setAlue(null);
                                }
                            }
                        }catch(Exception err){
                            System.out.println(err);
                        }
                    });
                    
                    //HALUTAANKO EDES ALUEEN NIMEÄ VOIDA MUOKATA?
                    saveEditBtn.setOnAction(e ->{
                        String alueMuokattuNimi = alueNimiTxtField.getText();
                        errMsgM.setFill(Color.RED);
                        alueNimiLabel.setTextFill(Color.BLACK);

                        if(alueMuokattuNimi.length() == 0 || alueMuokattuNimi.trim().length() == 0 || alueMuokattuNimi.length() > 40){
                            alueNimiLabel.setTextFill(Color.RED);
                            errMsgM.setText("Virhe! Tarkista syöttämäsi tiedot.");
                        }else{
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
                                errMsgM.setText("Muutokset tallennettu");
                                errMsgM.setFill(Color.GREEN);
                                i.setText("Id: " + al.getAlueId() + ", " + al.getNimi());
                                
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
               errMsgU.setText(""); 
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
