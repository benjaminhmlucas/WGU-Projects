/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import CustomerDatabase.DatabaseObjects;
import Objects.Address;
import Objects.Appointment;
import Objects.City;
import Objects.Country;
import Objects.Customer;
import Objects.User;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import static java.util.Calendar.SUNDAY;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author lucasb
 */
public class AddEditAppointmentWindow {
    
    public AddEditAppointmentWindow(Stage primaryStage,Appointment passedInAppointment,boolean passedInIsNewAppt){
        
        Appointment currentlySelectedAppt = passedInAppointment;
        final boolean isNewAppointment = passedInIsNewAppt;
        Stage addEditAppointmentWindow = new Stage();
        User currentlyLoggedInUser = DatabaseObjects.getCurrentLoggedInUser();
        Label windowTitle;
        if(isNewAppointment){
            windowTitle = new Label("Add Appointment"); 
        }else{
            windowTitle = new Label("Edit Appointment");
        } 
        
        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");
        ToggleButton editApptToggleBtn = new ToggleButton("Edit Appointment"); 
        
        GridPane root = new GridPane();
        GridPane userInputBoxes = new GridPane();
        HBox buttonBox = new HBox();
        HBox apptDescriptionLabelBox = new HBox();
        HBox apptLocationLabelBox = new HBox();
        
        Label apptIdLabel = new Label("Appointment Id:");
        Label customerIdLabel = new Label("Customer Id:");
        Label userIdLabel = new Label("User Id:");
        Label apptTitleLabel = new Label("Appointment Title:");
        Label apptDescriptionLabel = new Label("Appointment Description:");
        Label apptLocationLabel = new Label("Appointment Location:");
        Label apptContactLabel = new Label("Appointment Contact:");
        Label apptTypeLabel = new Label("Appointment Type:");
        Label customerUrlLabel = new Label("Customer URL:");
        Label apptDateLabel = new Label("Appointment Date:");
        Label apptStartLabel = new Label("Appointment Start Time:");
        Label apptEndLabel = new Label("Appointment End Time:");        
        Label createDateLabel = new Label("Date Created:");
        Label createdByLabel = new Label("Created By:");
        Label lastUpdateLabel = new Label("Last Updated:");
        Label lastUpdateByLabel = new Label("Last Updated By:");
        
        TextField apptIdField = new TextField("Appointment Id");
        ComboBox customerIdComboBox = new ComboBox(FXCollections.observableArrayList(DatabaseObjects.getAllCustomerIds()));
        ComboBox userIdComboBox = new ComboBox (FXCollections.observableArrayList(DatabaseObjects.getAllUserIds()));
        TextField apptTitleField = new TextField("Appointment Title");
        TextArea apptDescriptionArea = new TextArea("Appointment Description");
        TextArea apptLocationArea = new TextArea("Appointment Location");
        Label apptContactLabelValue = new Label();
        ComboBox apptTypeComboBox = new ComboBox(FXCollections.observableArrayList(DatabaseObjects.getTypeSelectorList()));
        TextField customerUrlField = new TextField("Customer URL");
        DatePicker apptDateComboBox = new DatePicker(LocalDate.now());
        ComboBox apptStartComboBox = new ComboBox(FXCollections.observableArrayList(DatabaseObjects.getApptStartTimeList()));  
        ComboBox apptEndComboBox = new ComboBox(FXCollections.observableArrayList(DatabaseObjects.getApptEndTimeList()));  
        TextField createDateField = new TextField("Date Created");
        TextField createdByField = new TextField("Created By");
        TextField lastUpdateField = new TextField("Last Updated");
        TextField lastUpdateByField = new TextField("Last Updated By");

        
        root.setId("root");
        userInputBoxes.setId("userInputBoxes");
        buttonBox.setId("buttonBox");
        windowTitle.setId("windowTitle");
        apptDescriptionLabel.setId("apptDescriptionLabel");
        apptDescriptionLabelBox.setId("#apptDescriptionBox");
        apptLocationLabelBox.setId("#apptLocationBox");
        apptContactLabelValue.setId("apptContactLabelValue");
        apptContactLabelValue.setUnderline(true);
        
        userInputBoxes.add(customerIdLabel, 0, 1);
        userInputBoxes.add(customerIdComboBox , 1, 1);
        userInputBoxes.add(userIdLabel, 0, 2);
        userInputBoxes.add(userIdComboBox, 1, 2);        
        userInputBoxes.add(apptTitleLabel, 0, 3);
        userInputBoxes.add(apptTitleField, 1, 3);
        apptDescriptionLabelBox.getChildren().add(apptDescriptionLabel);//this is for alignment purposes
        userInputBoxes.add(apptDescriptionLabelBox, 0, 4);
        userInputBoxes.add(apptDescriptionArea, 1, 4);
        apptLocationLabelBox.getChildren().add(apptLocationLabel);//this is for alignment purposes
        userInputBoxes.add(apptLocationLabelBox, 0, 5);
        userInputBoxes.add(apptLocationArea, 1, 5);
        userInputBoxes.add(apptContactLabel, 0, 6);
        userInputBoxes.add(apptContactLabelValue, 1, 6);
        userInputBoxes.add(apptTypeLabel, 0, 7);
        userInputBoxes.add(apptTypeComboBox, 1, 7);
        userInputBoxes.add(customerUrlLabel, 0, 8);
        userInputBoxes.add(customerUrlField, 1, 8);
        userInputBoxes.add(apptDateLabel, 0, 9);
        userInputBoxes.add(apptDateComboBox, 1, 9);
        userInputBoxes.add(apptStartLabel, 0, 10);
        userInputBoxes.add(apptStartComboBox, 1, 10);        
        userInputBoxes.add(apptEndLabel, 0, 11);
        userInputBoxes.add(apptEndComboBox, 1, 11);
        
        if(!isNewAppointment){
            userInputBoxes.add(apptIdLabel, 0, 0);
            userInputBoxes.add(apptIdField, 1, 0);            
            userInputBoxes.add(createDateLabel, 0, 12);
            userInputBoxes.add(createDateField, 1, 12);
            userInputBoxes.add(createdByLabel, 0, 13);
            userInputBoxes.add(createdByField, 1, 13);
            userInputBoxes.add(lastUpdateLabel, 0, 14);
            userInputBoxes.add(lastUpdateField, 1, 14);
            userInputBoxes.add(lastUpdateByLabel, 0, 15);
            userInputBoxes.add(lastUpdateByField, 1, 15);        
        }
        
        buttonBox.getChildren().addAll(submit,cancel);
        root.add(windowTitle,0,0);
        root.add(userInputBoxes,0,1);
        root.add(buttonBox,0,2);
        
        apptDescriptionArea.setPrefSize(100, 100);
        apptDescriptionArea.setWrapText(true);
        apptLocationArea.setPrefSize(100, 100);
        apptLocationArea.setWrapText(true);
        apptIdField.setDisable(true);
        createDateField.setDisable(true);
        createdByField.setDisable(true);
        lastUpdateField.setDisable(true);
        lastUpdateByField.setDisable(true);

        if(!isNewAppointment){
            apptIdField.setText(""+currentlySelectedAppt.getAppointmentId());
            customerIdComboBox.getSelectionModel().select(""+currentlySelectedAppt.getCustomerId());
            userIdComboBox.getSelectionModel().select(""+currentlySelectedAppt.getUserId());
            apptTitleField.setText(""+currentlySelectedAppt.getTitle());
            apptDescriptionArea.setText(""+currentlySelectedAppt.getDescription());
            apptLocationArea.setText(""+currentlySelectedAppt.getLocation());
            apptContactLabelValue.setText(""+currentlySelectedAppt.getContact());
            apptTypeComboBox.getSelectionModel().select(currentlySelectedAppt.getType());
            customerUrlField.setText(""+currentlySelectedAppt.getUrl());
            apptDateComboBox.valueProperty().set(currentlySelectedAppt.getStart().toLocalDate());
            apptStartComboBox.valueProperty().set(currentlySelectedAppt.getStart().toLocalTime());
            apptEndComboBox.valueProperty().set(currentlySelectedAppt.getEnd().toLocalTime());
            createDateField.setText(currentlySelectedAppt.getFormattedCreateDate());
            createdByField.setText(currentlySelectedAppt.getCreatedBy());
            lastUpdateField.setText(currentlySelectedAppt.getFormattedLastUpdate());
            lastUpdateByField.setText(currentlySelectedAppt.getLastUpdatedBy());
            buttonBox.getChildren().add(editApptToggleBtn);
            cancel.setText("Go Back");
            submit.setVisible(false);
            userIdComboBox.setDisable(true);
            customerIdComboBox.setDisable(true);
            apptTitleField.setDisable(true);
            apptLocationArea.setDisable(true);
            apptDescriptionArea.setDisable(true);
            apptTypeComboBox.setDisable(true);
            customerUrlField.setDisable(true);
            apptDateComboBox.setDisable(true);
            apptStartComboBox.setDisable(true);
            apptEndComboBox.setDisable(true);                    
        } else{
            userIdComboBox.getSelectionModel().select(""+currentlyLoggedInUser.getUserId());
            userIdComboBox.setDisable(true);            
        }
        //This lambda allows the user to toggle editing on and off in the input fields
        editApptToggleBtn.setOnAction((ActionEvent event) ->{
            if(editApptToggleBtn.isSelected()){
                submit.setVisible(true);
                cancel.setText("Cancel");
                userIdComboBox.setDisable(false);
                customerIdComboBox.setDisable(false);
                apptTitleField.setDisable(false);
                apptLocationArea.setDisable(false);
                apptDescriptionArea.setDisable(false);
                apptTypeComboBox.setDisable(false);
                customerUrlField.setDisable(false);
                apptDateComboBox.setDisable(false);
                apptStartComboBox.setDisable(false);
                apptEndComboBox.setDisable(false);
                
            }else{
                submit.setVisible(false);
                cancel.setText("Go Back");
                userIdComboBox.setDisable(true);
                customerIdComboBox.setDisable(true);
                apptTitleField.setDisable(true);
                apptLocationArea.setDisable(true);
                apptDescriptionArea.setDisable(true);
                apptTypeComboBox.setDisable(true);
                customerUrlField.setDisable(true);
                apptDateComboBox.setDisable(true);
                apptStartComboBox.setDisable(true);
                apptEndComboBox.setDisable(true);                 
            }
        });
        //this lambda allows for input validation and submission to the database of the appointment object.
        submit.setOnAction((ActionEvent event) ->{
            ArrayList<String> inputErrorList = new ArrayList<>();
            if(customerIdComboBox.getValue()==null){
                inputErrorList.add("->Pick a customer id\n");
            }
            if(apptTitleField.getText().trim().equals("")||apptTitleField.getText().trim().equals("Appointment Description")){
                inputErrorList.add("->Enter a description\n");
            }
            if(apptDescriptionArea.getText().trim().equals("")||apptDescriptionArea.getText().trim().equals("Appointment Description")){
                inputErrorList.add("->Enter a location\n");
            }
            if(apptLocationArea.getText().trim().equals("")||apptLocationArea.getText().trim().equals("Appointment Location")){
                inputErrorList.add("->Enter a location\n");
            }
            if(apptContactLabelValue.getText().trim().equals("")||apptContactLabelValue.getText().trim().equals("Appointment Contact")){
                inputErrorList.add("->Enter a contact\n");
            }
            if(apptTypeComboBox.getValue()==null){
                inputErrorList.add("->Pick an appointment type\n");
            }
            Pattern p = Pattern.compile("(www.)([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$");
            Matcher m;
            m=p.matcher(customerUrlField.getText().trim());
            if(customerUrlField.getText().trim().equals("")||customerUrlField.getText().trim().equals("Customer URL")||!m.matches()){
                inputErrorList.add("->Enter a customer URL like 'www.customersite.com'\n");
            }
            if(apptDateComboBox.getValue().isBefore(LocalDate.now())){
                inputErrorList.add("->Date must be today or after today\n");
            }
            LocalDate apptDate = apptDateComboBox.getValue();
            if(apptDate.getDayOfWeek().toString().equals("SUNDAY")||apptDate.getDayOfWeek().toString().equals("SATURDAY")){
                inputErrorList.add("->Pick a date during the week\n");
            }
            if(apptStartComboBox.getValue()==null){
                inputErrorList.add("->Pick a start time\n");
            }
            if(apptEndComboBox.getValue()==null){
                inputErrorList.add("->Pick a end time\n");
            }

            if(!inputErrorList.isEmpty()){
                StringBuilder errorMessage = new StringBuilder("You must correct the following errors on the form before saving this appointment:\n");
                inputErrorList.stream().forEach((error) -> {
                    errorMessage.append(error);
                });                
                JOptionPane.showMessageDialog(null,errorMessage,"Doh!!",JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalTime startTime = (LocalTime) apptStartComboBox.getValue();
            LocalTime endTime = (LocalTime) apptEndComboBox.getValue();
            if(endTime.isBefore(startTime)||endTime.equals(startTime)){
                JOptionPane.showMessageDialog(null,"End time must be after start time","Doh!!",JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDateTime apptStart = LocalDateTime.of(apptDateComboBox.getValue(),startTime);
            LocalDateTime apptEnd = LocalDateTime.of(apptDateComboBox.getValue(),endTime);
            ArrayList<Appointment> apptList = DatabaseObjects.getAllAppointments();
            if(isNewAppointment){
                Appointment.checkNextId();
                Appointment thisAppt = new Appointment(
                    Integer.valueOf((int)customerIdComboBox.getSelectionModel().getSelectedItem()),
                    Integer.valueOf((String)userIdComboBox.getSelectionModel().getSelectedItem()),
                    apptTitleField.getText().trim(),
                    apptDescriptionArea.getText().trim(),
                    apptLocationArea.getText().trim(),
                    apptContactLabelValue.getText().trim(),
                    (String)apptTypeComboBox.getValue(),
                    customerUrlField.getText().trim(),
                    Timestamp.valueOf(apptStart).toLocalDateTime(),
                    Timestamp.valueOf(apptEnd).toLocalDateTime(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    System.getProperty("user.name"),
                    Timestamp.valueOf(LocalDateTime.now()),
                    System.getProperty("user.name"));
                for(Appointment appt:apptList){
                    if(thisAppt.getStart().toString().equals(appt.getStart().toString())||thisAppt.getEnd().toString().equals(appt.getEnd().toString())){
                        if(thisAppt.getCustomerId()==appt.getCustomerId()){
                            JOptionPane.showMessageDialog(null,"This Customer is already scheduled for an appointment during this time","Doh!!",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if(thisAppt.getUserId()==appt.getUserId()){
                            JOptionPane.showMessageDialog(null,"This User is already scheduled for an appointment during this time","Doh!!",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                int rowsAdded = DatabaseObjects.insertAppointment(thisAppt);
                System.out.println("Appointments Added: "+rowsAdded);
                ManagementWindow newWindow = new ManagementWindow(primaryStage,currentlyLoggedInUser);                                
                addEditAppointmentWindow.hide();  
            }else{
                currentlySelectedAppt.setCustomerId(Integer.parseInt(customerIdComboBox.getValue().toString()));
                currentlySelectedAppt.setUserId(Integer.parseInt(userIdComboBox.getValue().toString()));
                currentlySelectedAppt.setTitle(apptTitleField.getText().trim());
                currentlySelectedAppt.setDescription(apptDescriptionArea.getText().trim());
                currentlySelectedAppt.setLocation(apptLocationArea.getText().trim());
                currentlySelectedAppt.setContact(apptContactLabelValue.getText().trim());
                currentlySelectedAppt.setType((String)apptTypeComboBox.getValue());
                currentlySelectedAppt.setUrl(customerUrlField.getText().trim());
                currentlySelectedAppt.setStart(Timestamp.valueOf(apptStart).toLocalDateTime());
                currentlySelectedAppt.setEnd(Timestamp.valueOf(apptEnd).toLocalDateTime());
                currentlySelectedAppt.setLastUpdate(Timestamp.valueOf(LocalDateTime.now()));
                currentlySelectedAppt.setLastUpdatedBy(System.getProperty("user.name"));
                for(Appointment appt:apptList){
                    if((currentlySelectedAppt.getAppointmentId()!=appt.getAppointmentId())&&
                            (currentlySelectedAppt.getStart().toString().equals(appt.getStart().toString())||currentlySelectedAppt.getEnd().toString().equals(appt.getEnd().toString()))){
                        if(currentlySelectedAppt.getCustomerId()==appt.getCustomerId()){
                            JOptionPane.showMessageDialog(null,"This Customer is already scheduled for an appointment during this time","Doh!!",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if(currentlySelectedAppt.getUserId()==appt.getUserId()){
                            JOptionPane.showMessageDialog(null,"This User is already scheduled for an appointment during this time","Doh!!",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }  
                int rowsAdded = DatabaseObjects.updateAppointment(currentlySelectedAppt);
                System.out.println("Appointments Updated: "+rowsAdded);
                ManagementWindow newWindow = new ManagementWindow(primaryStage,currentlyLoggedInUser);                                
                addEditAppointmentWindow.hide();
            }
        });
        //this lambda is used for returning to the previous window
        cancel.setOnAction((ActionEvent event) -> {
            ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                                
            addEditAppointmentWindow.close();
        });
        //this lambda is used to assign an event handler to the contact column that allows the user to jump to the contacts page
        apptContactLabelValue.setOnMouseClicked((MouseEvent me) -> {
            if(me.getButton().equals(MouseButton.PRIMARY)){
                if(me.getClickCount() == 2){
                    Customer selectedCustomer = DatabaseObjects.getCustomerById(currentlySelectedAppt.getCustomerId());
                    Address selectedAddress = DatabaseObjects.getAddressById(selectedCustomer.getAddressId());
                    City selectedCity = DatabaseObjects.getCityById(selectedAddress.getCityId());
                    Country selectedCountry = DatabaseObjects.getCountryById(selectedCity.getCountryId());
                    AddEditCustomerWindow newWindow = new AddEditCustomerWindow(primaryStage, currentlyLoggedInUser,selectedCustomer,selectedAddress,selectedCity,selectedCountry,false);
                    addEditAppointmentWindow.close(); 
                }
            }
        });
        customerIdComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                apptContactLabelValue.setText(DatabaseObjects.getCustomerNameById((int)customerIdComboBox.getValue()));
            }
        });
        apptStartComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                apptEndComboBox.getSelectionModel().select((int)apptStartComboBox.getSelectionModel().getSelectedIndex());
            }
        });
        Scene scene = new Scene(root, 749, 547);        

        addEditAppointmentWindow.setTitle("Add/Edit Appointment");
        addEditAppointmentWindow.setScene(scene);
        scene.getStylesheets().add("/GUIs/GUIsStyleSheet.css");
        addEditAppointmentWindow.show();
        primaryStage.hide();
    }
}
