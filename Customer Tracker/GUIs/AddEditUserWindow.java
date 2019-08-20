/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import CustomerDatabase.DatabaseObjects;
import CustomerDatabase.StatementMaker;
import Objects.User;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author lucasb
 */
public class AddEditUserWindow {
        
    public AddEditUserWindow(Stage primaryStage, User passedInUser,boolean passedInIsNewUser){
        
        User currentlySelectedUser = passedInUser;
        final boolean isNewUser = passedInIsNewUser;
        Label windowTitle;
        if(isNewUser){
            windowTitle = new Label("Add User");
        }else{
            windowTitle = new Label("Edit User User");
        } 
        
        Stage addEditUserWindow = new Stage();
        
        Label userIdLabel = new Label("User Id:");
        Label userNameLabel = new Label("User Name:");
        Label PasswordLabel = new Label("Password:");
        Label activeLabel = new Label("Active:");
        Label createDateLabel = new Label("Date Created:");
        Label createdByLabel = new Label("Created By:");
        Label lastUpdateLabel = new Label("Last Updated:");
        Label lastUpdateByLabel = new Label("Last Updated By:");
        
        TextField userIdField = new TextField("User Id");
        TextField userNameField = new TextField("User Name");
        PasswordField passwordField = new PasswordField();
        ArrayList<String> activeSelectorList = new ArrayList<>();
        activeSelectorList.add("Active");
        activeSelectorList.add("Inactive");
        ComboBox activeComboBox = new ComboBox(FXCollections.observableArrayList(activeSelectorList));
        TextField createDateField = new TextField("Date Created");
        TextField createdByField = new TextField("Created By");
        TextField lastUpdateField = new TextField("Last Updated");
        TextField lastUpdateByField = new TextField("Last Updated By");

        ToggleButton editToggleBtn = new ToggleButton("Edit User"); 
        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");
        
        GridPane root = new GridPane();
        GridPane userInputBoxes = new GridPane();
        HBox buttonBox = new HBox();
        
        root.setId("root");
        userInputBoxes.setId("userInputBoxes");
        buttonBox.setId("buttonBox");
        windowTitle.setId("windowTitle");

        userInputBoxes.add(userNameLabel, 0, 1);
        userInputBoxes.add(userNameField, 1, 1);
        userInputBoxes.add(PasswordLabel, 0, 2);
        userInputBoxes.add(passwordField, 1, 2);        
        userInputBoxes.add(activeLabel, 0, 3);
        userInputBoxes.add(activeComboBox, 1, 3);
        
        buttonBox.getChildren().addAll(submit,cancel);
                
        if(!passedInIsNewUser){
                buttonBox.getChildren().add(editToggleBtn);
                cancel.setText("Go Back");
                submit.setVisible(false);
                userNameField.setDisable(true);
                passwordField.setDisable(true);
                activeComboBox.setDisable(true);
        } 
        
        root.add(windowTitle,0,0);
           
        root.add(userInputBoxes,0,1);
        root.add(buttonBox,0,2);
        
        userIdField.setDisable(true);
        createDateField.setDisable(true);
        createdByField.setDisable(true);
        lastUpdateField.setDisable(true);
        lastUpdateByField.setDisable(true);
        
        if(currentlySelectedUser != null){
            windowTitle.setText("Edit User");
            userInputBoxes.add(userIdLabel, 0, 0);
            userInputBoxes.add(userIdField, 1, 0);
            userInputBoxes.add(createDateLabel, 0, 4);
            userInputBoxes.add(createDateField, 1, 4);
            userInputBoxes.add(createdByLabel, 0, 5);
            userInputBoxes.add(createdByField, 1, 5);
            userInputBoxes.add(lastUpdateLabel, 0, 6);
            userInputBoxes.add(lastUpdateField, 1, 6);
            userInputBoxes.add(lastUpdateByLabel, 0, 7);
            userInputBoxes.add(lastUpdateByField, 1, 7);
            userIdField.setText(""+currentlySelectedUser.getUserId());
            userNameField.setText(""+currentlySelectedUser.getUserName());
            passwordField.setText(""+currentlySelectedUser.getPassword());
            if(currentlySelectedUser.getActive()==1){
                activeComboBox.getSelectionModel().select("Active");
            }else{
                activeComboBox.getSelectionModel().select("Inactive");
            }
            createDateField.setText(currentlySelectedUser.getFormattedCreateDate());
            createdByField.setText(currentlySelectedUser.getCreatedBy());
            lastUpdateField.setText(currentlySelectedUser.getFormattedLastUpdate());
            lastUpdateByField.setText(currentlySelectedUser.getLastUpdatedBy());        
        } else {
            activeComboBox.getSelectionModel().select("Active");
        }
        
        //this lambda allows the user to toggle editing the user object input fields and boxes
        editToggleBtn.setOnAction((ActionEvent event) ->{
            if(editToggleBtn.isSelected()){
                cancel.setText("Cancel");
                submit.setVisible(true);
                userNameField.setDisable(false);
                passwordField.setDisable(false);
                activeComboBox.setDisable(false);
            } else {
                cancel.setText("Go Back");
                submit.setVisible(false);
                userNameField.setDisable(true);
                passwordField.setDisable(true);
                activeComboBox.setDisable(true);
            }
        });
        
        //This lambda allows the user to validate input and submit a user object into the database
        submit.setOnAction((ActionEvent event) -> {
            byte active;
            ArrayList<String> userNames = DatabaseObjects.getAllUserNames();
            if(activeComboBox.getSelectionModel().getSelectedItem().equals("Active")){
                active = 1;
            } else {
                active = 0;
            }
            for(String userName:userNames){
             if (userName.equals(userNameField.getText().trim())&&isNewUser){
                    JOptionPane.showMessageDialog(null, "The SQL Gods say that user name is already taken!","Doh!!",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if(userNameField.getText().isEmpty()||userNameField.getText().equals("")){
                JOptionPane.showMessageDialog(null, "The SQL Gods say must enter a user name!","Doh!!",JOptionPane.ERROR_MESSAGE);
            }else{
                if(isNewUser){
                    User newUser = new User(userNameField.getText(),passwordField.getText(),(byte)active,
                        Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"),
                        Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"));
                    System.out.println(newUser.toString());
                    int rowsAdded = DatabaseObjects.insertUser(newUser);
                    System.out.println("Users Added: "+rowsAdded);
                    ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                                
                    addEditUserWindow.hide();
                }else{
                    User editedUser = new User(Integer.parseInt(userIdField.getText()),userNameField.getText(),passwordField.getText(),(byte)active,
                        currentlySelectedUser.getCreateDate(), currentlySelectedUser.getCreatedBy(),
                        Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"));
                    System.out.println(editedUser.toString());
                    if(currentlySelectedUser.getUserId()==1){
                        JOptionPane.showMessageDialog(null, "The SQL Gods say they can't set the main user to inactive for security reasons! Plus he is nice.","Doh!!",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(DatabaseObjects.getCurrentLoggedInUser().getUserId()==Integer.parseInt(userIdField.getText())&&activeComboBox.getSelectionModel().getSelectedItem().equals("Inactive")){
                        int returnValue = JOptionPane.showConfirmDialog(null, "Are you sure that you want to set your user account to inactive? "
                                + "Doing so will log you out and YOU WILL NOT BE ABLE TO LOG IN AGAIN!!!!","Are you serious?!?!",JOptionPane.OK_CANCEL_OPTION);
                        if(returnValue==0){
                            LoginWindow newWindow = new LoginWindow();
                            ManagementWindow.setAppointmentMessageShown(false);
                            newWindow.start(primaryStage);
                            addEditUserWindow.close();
                        }
                    }else{
                        int rowsEdited = StatementMaker.makeUpdateDeleteOrInsertStatement(
                            "UPDATE user set "
                                    + "userName = '"+editedUser.getUserName()+"',"
                                    + "password = '"+editedUser.getPassword()+"',"
                                    + "active = "+editedUser.getActive()+","
                                    + "lastUpdate = '"+editedUser.getLastUpdate()+"',"
                                    + "lastUpdateBy = '"+editedUser.getLastUpdatedBy()+"'"
                                    + "WHERE userId = "+editedUser.getUserId());
                        System.out.println("User's Edited: "+rowsEdited);
                        ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                
                        addEditUserWindow.close();                     
                    }
                }        
            } 
        });
        
        //this lambda allows the user to return to the management window
        cancel.setOnAction((ActionEvent event) -> {
            ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                                
            addEditUserWindow.close();
        });
        Scene scene = new Scene(root, 480, 351);          

        addEditUserWindow.setTitle("Add/Edit User");
        addEditUserWindow.setScene(scene);
        scene.getStylesheets().add("/GUIs/GUIsStyleSheet.css");
        addEditUserWindow.show();
        primaryStage.hide();
    }
    
}
