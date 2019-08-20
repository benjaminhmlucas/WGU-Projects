/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;



import CustomerDatabase.DatabaseObjects;
import CustomerDatabase.DBConnector;
import CustomerDatabase.SampleData;
import CustomerDatabase.StatementMaker;
import Objects.User;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author lucasb
 */
public class LoginWindow extends Application {
    
    @Override
    public void start(Stage primaryStage) {
//These settings allow you to change the time zone and language to test these functionalities.
//        Locale.setDefault( new Locale("es"));
//        System.setProperty("user.language","es");
//        System.setProperty("user.country","es");
//        System.setProperty("user.timezone", "UTC");
//        System.setProperty("system.timezone", "UTC");
        
        DatabaseObjects.setSystemCountry(System.getProperty("user.country"));
        DatabaseObjects.setSystemLanguage(System.getProperty("user.language"));
        
        Button loginBtn = new Button();
        Button closeBtn = new Button();
        primaryStage.setTitle("Customer Tracker 9000x");
        loginBtn.setText("Login");        
        closeBtn.setText("Close Program");
        loginBtn.setDefaultButton(true);
        Label welcomeMessage = new Label("Welcome to\nCustomer Tracker 9000x.\nPlease enter login information.\n");
        welcomeMessage.setTextAlignment(TextAlignment.CENTER);

        Label userNameLabel = new Label("Username: ");
        Label passwordLabel = new Label("Password: ");
        TextField userNameField = new TextField("test");
        TextField passwordField = new TextField("test");
        switch(DatabaseObjects.getSystemLanguage()){
            case "fr":
                primaryStage.setTitle("Suivi du client 9000x");
                loginBtn.setText("S'identifier");        
                closeBtn.setText("Fermer le programme");
                welcomeMessage.setText("Bienvenue dans \n Customer Tracker 9000x.\n S'il vous plaît entrer les informations de connexion.\n");
                userNameLabel.setText("Nom d'utilisateur:");
                passwordLabel.setText("Mot de passe:");
                break;
            case "es":
                primaryStage.setTitle("Rastreador de clientes 9000x");
                loginBtn.setText("Iniciar sesión");        
                closeBtn.setText("Cerrar programa");
                welcomeMessage.setText("Bienvenido a \nCustomer Tracker 9000x. \nPor favor, introduzca la información de inicio de sesión.\n");
                userNameLabel.setText("Nombre de usuario:");
                passwordLabel.setText("contraseña:");
                break;
        }         
        //This lambda expression assigns steps to be taken when the loginBtn button is pressed
        loginBtn.setOnAction((ActionEvent event) -> {
            System.out.println(DatabaseObjects.getSystemCountry());
            System.out.println(DatabaseObjects.getSystemLanguage());

            if(DBConnector.getConnection().getConn()!= null){
                try {                        
//These settings allow you to insert users into the database in case the original test user user gets erased by accident
//                        SampleData.createSampleData(3);
//                        SampleData.addTestData();
                    ResultSet rs = StatementMaker.makeSelectStatement("SELECT * FROM user");
                    while(rs.next()){
                        if(rs.getString(2).equals(userNameField.getText().trim())&&rs.getString(3).equals(passwordField.getText())){
                            User loginUser = new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getByte(4),
                                    rs.getTimestamp(5),rs.getString(6),
                                    rs.getTimestamp(7),rs.getString(8));
                            if(loginUser.getActive()==1){
                                System.out.println(DatabaseObjects.getSystemCountry());
                                System.out.println(DatabaseObjects.getSystemLanguage());
                                DatabaseObjects.setCurrentLoggedInUser(loginUser);
                                Path path = Paths.get("src/CustomerDatabase/SignInLog.log");
                                try {
                                    FileWriter fileWriter = new FileWriter(path.toString(), true);
                                    try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
                                        printWriter.println("User Login @"+Timestamp.valueOf(LocalDateTime.now())+"\nby User:"+loginUser.getUserName()+"\nOn System:"+System.getProperty("user.name"));
                                    }
                                } catch (IOException ex) {
                                    switch(DatabaseObjects.getSystemLanguage()){
                                        case "en":
                                            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't find SignInLog.log!"+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
                                            break;
                                        case "fr":
                                            JOptionPane.showMessageDialog(null, "Les dieux SQL disent qu'ils ne peuvent pas trouver SignInLog.log!","Doh!!",JOptionPane.ERROR_MESSAGE);
                                            break;
                                        case "es":
                                            JOptionPane.showMessageDialog(null, "¡Los Dioses SQL dicen que no pueden encontrar SignInLog.log!","Doh!!",JOptionPane.ERROR_MESSAGE);
                                            break;
                                        default:
                                            JOptionPane.showMessageDialog(null, "The SQL Gods say do not know your language!","Doh!!",JOptionPane.ERROR_MESSAGE);
                                            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't find SignInLog.log!","Doh!!",JOptionPane.ERROR_MESSAGE);
                                    }                                
                                }
                                ManagementWindow newWindow = new ManagementWindow(primaryStage,loginUser);
                                primaryStage.close();
                                return;
                            }else{
                                switch(DatabaseObjects.getSystemLanguage()){
                                    case "en":
                                        JOptionPane.showMessageDialog(null, "The SQL Gods say you are no longer active!","Doh!!",JOptionPane.ERROR_MESSAGE);
                                        break;
                                    case "fr":
                                        JOptionPane.showMessageDialog(null, "Les dieux SQL disent que vous n'êtes plus actif!","Doh!!",JOptionPane.ERROR_MESSAGE);
                                        break;
                                    case "es":
                                        JOptionPane.showMessageDialog(null, "¡Los Dioses SQL dicen que ya no estás activo!","Doh!!",JOptionPane.ERROR_MESSAGE);
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "The SQL Gods say do not know your language!","Doh!!",JOptionPane.ERROR_MESSAGE);
                                        JOptionPane.showMessageDialog(null, "The SQL Gods say you are no longer active!","Doh!!",JOptionPane.ERROR_MESSAGE);
                                }
                                return;     
                            }
                        }
                    }
                    switch(DatabaseObjects.getSystemLanguage()){
                        case "en":
                            JOptionPane.showMessageDialog(null, "The SQL Gods say your username or password is incorrect!","Doh!!",JOptionPane.ERROR_MESSAGE);
                            break;
                        case "fr":
                            JOptionPane.showMessageDialog(null, "Les dieux SQL disent que votre nom d'utilisateur ou mot de passe est incorrect!","Doh!!",JOptionPane.ERROR_MESSAGE);
                            break;
                        case "es":
                            JOptionPane.showMessageDialog(null, "¡Los Dioses SQL dicen que su nombre de usuario o contraseña son incorrectos!","Doh!!",JOptionPane.ERROR_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "The SQL Gods say do not know your language!","Doh!!",JOptionPane.ERROR_MESSAGE);
                            JOptionPane.showMessageDialog(null, "The SQL Gods say your username or password is incorrect!","Doh!!",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    switch(DatabaseObjects.getSystemLanguage()){
                        case "en":
                            JOptionPane.showMessageDialog(null, "The SQL Gods say something is amiss!-->>"+ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                            break;
                        case "fr":
                            JOptionPane.showMessageDialog(null, "Les dieux SQL disent que quelque chose ne va pas!","Erreur!",JOptionPane.ERROR_MESSAGE);
                            break;
                        case "es":
                            JOptionPane.showMessageDialog(null, "¡Los dioses de SQL dicen que algo anda mal!","¡Error!",JOptionPane.ERROR_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "The SQL Gods say do not know your language!","Error!",JOptionPane.ERROR_MESSAGE);
                            JOptionPane.showMessageDialog(null, "The SQL Gods say something is amiss!-->>"+ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                    } 
                }
            } else {
                switch(DatabaseObjects.getSystemLanguage()){
                    case "en":
                        JOptionPane.showMessageDialog(null, "Connection not available!","Error!",JOptionPane.ERROR_MESSAGE);
                        break;
                    case "fr":
                        JOptionPane.showMessageDialog(null, "Connexion non disponible!","Erreur!",JOptionPane.ERROR_MESSAGE);
                        break;
                    case "es":
                        JOptionPane.showMessageDialog(null, "¡Conexión no disponible!","¡Error!",JOptionPane.ERROR_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "The SQL Gods say do not know your language!","Error!",JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(null, "Connection not available!","Error!",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        closeBtn.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        
        GridPane root = new GridPane();
        HBox rootTop = new HBox();
        GridPane rootBottom = new GridPane();
        GridPane buttonPane = new GridPane();
        
        root.setAlignment(Pos.CENTER);
        rootTop.setAlignment(Pos.CENTER);
        rootBottom.setAlignment(Pos.CENTER);
        buttonPane.setAlignment(Pos.BOTTOM_RIGHT);
        
        root.setId("rootPane");
        rootTop.setId("rootTop");
        rootBottom.setId("rootBottom");
        buttonPane.add(loginBtn,1,1);
        buttonPane.add(closeBtn,2,1);
        
        rootTop.getChildren().add(welcomeMessage);
        rootBottom.add(userNameLabel,0,2);
        rootBottom.add(passwordLabel,0,3);
        rootBottom.add(userNameField,1,2);
        rootBottom.add(passwordField,1,3);
        rootBottom.add(buttonPane, 1, 4);
        
        root.add(rootTop,0,0);
        root.add(rootBottom,0,1);
        
        Scene scene = new Scene(root, 600, 400);
        
 
        primaryStage.setScene(scene);
        scene.getStylesheets().add("/GUIs/GUIsStyleSheet.css");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
