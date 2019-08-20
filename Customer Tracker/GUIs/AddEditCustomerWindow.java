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
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
public class AddEditCustomerWindow {
    
    public AddEditCustomerWindow(Stage primaryStage, User passedInUser, Customer passedInCustomer,Address passedInAddress,City passedInCity,Country passedInCountry,boolean passedInIsNewCustomer){
        
        final boolean isNewCustomer = passedInIsNewCustomer;
        User currentUser = passedInUser;
        Customer currentlySelectedCustomer = passedInCustomer;
        Address currentAddress = passedInAddress;
        City currentCity = passedInCity;
        Country currentCountry = passedInCountry;
        
        Label windowTitle;
        if(isNewCustomer){
            windowTitle = new Label("Add Customer");
        } else {
            windowTitle = new Label("Edit Customer");
        }
        
        Stage addEditCustomerWindow = new Stage();
        ToggleButton editToggleBtn = new ToggleButton("Edit Customer");
        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");
        
        GridPane root = new GridPane();
        GridPane userInputBoxes = new GridPane();
        HBox buttonBox = new HBox();
        
        Label customerIdLabel = new Label("Customer Id:");
        Label customerNameLabel = new Label("Customer Name:");
        Label activeLabel = new Label("Active:");
        Label address1Label = new Label("Address(1):");
        Label address2Label = new Label("Address(2):");
        Label cityLabel = new Label("City:");
        Label postalCodeLabel = new Label("Postal Code:");
        Label countryLabel = new Label("Country:");
        Label phoneLabel = new Label("Phone:");        
        Label createDateLabel = new Label("Date Created:");
        Label createdByLabel = new Label("Created By:");
        Label lastUpdateLabel = new Label("Last Updated:");
        Label lastUpdateByLabel = new Label("Last Updated By:");
        
        TextField customerIdField = new TextField("Customer Id");
        TextField customerNameField = new TextField("Customer Name");
        ArrayList<String> activeSelectorList = new ArrayList<>();
        activeSelectorList.add("Active");
        activeSelectorList.add("Inactive");
        ComboBox activeComboBox = new ComboBox(FXCollections.observableArrayList(activeSelectorList));        
        TextField address1Field = new TextField("Address(1)");
        TextField address2Field = new TextField("Address(2)");
        TextField cityField = new TextField("City");
        TextField postalCodeField = new TextField("Postal Code");
        TextField countryField = new TextField("Country");
        TextField phoneField = new TextField("Phone"); 
        TextField createDateField = new TextField("Date Created");
        TextField createdByField = new TextField("Created By");
        TextField lastUpdateField = new TextField("Last Updated");
        TextField lastUpdateByField = new TextField("Last Updated By");
        
        root.setId("root");
        userInputBoxes.setId("userInputBoxes");
        buttonBox.setId("buttonBox");
        windowTitle.setId("windowTitle");        

        userInputBoxes.add(customerNameLabel, 0, 1);
        userInputBoxes.add(customerNameField, 1, 1);
        userInputBoxes.add(activeLabel, 0, 2);
        userInputBoxes.add(activeComboBox, 1, 2);        
        userInputBoxes.add(address1Label, 0, 3);
        userInputBoxes.add(address1Field, 1, 3);
        userInputBoxes.add(address2Label, 0, 4);
        userInputBoxes.add(address2Field, 1, 4);
        userInputBoxes.add(cityLabel, 0, 5);
        userInputBoxes.add(cityField, 1, 5);
        userInputBoxes.add(postalCodeLabel, 0, 6);
        userInputBoxes.add(postalCodeField, 1, 6);
        userInputBoxes.add(countryLabel, 0, 7);
        userInputBoxes.add(countryField, 1, 7);
        userInputBoxes.add(phoneLabel, 0, 8);
        userInputBoxes.add(phoneField, 1, 8);
        
        buttonBox.getChildren().addAll(submit,cancel);

        if(!isNewCustomer){
            userInputBoxes.add(customerIdLabel, 0, 0);
            userInputBoxes.add(customerIdField, 1, 0);
            userInputBoxes.add(createDateLabel, 0, 9);
            userInputBoxes.add(createDateField, 1, 9);
            userInputBoxes.add(createdByLabel, 0, 10);
            userInputBoxes.add(createdByField, 1, 10);
            userInputBoxes.add(lastUpdateLabel, 0, 11);
            userInputBoxes.add(lastUpdateField, 1, 11);
            userInputBoxes.add(lastUpdateByLabel, 0, 12);
            userInputBoxes.add(lastUpdateByField, 1, 12);
            buttonBox.getChildren().add(editToggleBtn);
            submit.setVisible(false);
            cancel.setText("Go Back");
        }
        
        root.add(windowTitle,0,0);
        root.add(userInputBoxes,0,1);
        root.add(buttonBox,0,2);        
        
        customerIdField.setDisable(true);
        createDateField.setDisable(true);
        createdByField.setDisable(true);
        lastUpdateField.setDisable(true);
        lastUpdateByField.setDisable(true);
        
        Scene scene = new Scene(root, 749, 547);        
        
        if(!isNewCustomer){
            customerIdField.setText(""+currentlySelectedCustomer.getCustomerId());
            customerNameField.setText(""+currentlySelectedCustomer.getCustomerName());
            if(currentlySelectedCustomer.getActive()==1){
                activeComboBox.getSelectionModel().select("Active");
            }else{
                activeComboBox.getSelectionModel().select("Inactive");
            }
            address1Field.setText(currentAddress.getAddress());
            address2Field.setText(currentAddress.getAddress2());
            postalCodeField.setText(currentAddress.getPostalCode());
            phoneField.setText(currentAddress.getPhone());
            cityField.setText(currentCity.getCity());
            countryField.setText(currentCountry.getCountry());
            createDateField.setText(currentlySelectedCustomer.getFormattedCreateDate());
            createdByField.setText(currentlySelectedCustomer.getCreatedBy());
            lastUpdateField.setText(currentlySelectedCustomer.getFormattedLastUpdate());
            lastUpdateByField.setText(currentlySelectedCustomer.getLastUpdatedBy());
            customerNameField.setDisable(true);
            activeComboBox.setDisable(true);
            address1Field.setDisable(true);
            address2Field.setDisable(true);
            postalCodeField.setDisable(true);
            phoneField.setDisable(true);
            cityField.setDisable(true);
            countryField.setDisable(true);  
        }
        //this lamba assigns a listener to the edit toggle that allows the user to enable and disable input fields and boxes
        editToggleBtn.setOnAction((ActionEvent event) ->{
            if(editToggleBtn.isSelected()){
                cancel.setText("Cancel");
                submit.setVisible(true);
                customerNameField.setDisable(false);
                activeComboBox.setDisable(false);
                address1Field.setDisable(false);
                address2Field.setDisable(false);
                postalCodeField.setDisable(false);
                phoneField.setDisable(false);
                cityField.setDisable(false);
                countryField.setDisable(false);                
            } else {
                cancel.setText("Go Back");
                submit.setVisible(true);
                customerNameField.setDisable(true);
                activeComboBox.setDisable(true);
                address1Field.setDisable(true);
                address2Field.setDisable(true);
                postalCodeField.setDisable(true);
                phoneField.setDisable(true);
                cityField.setDisable(true);
                countryField.setDisable(true);
            }
        });
        //this lambda performs input validation and submission to the database
        submit.setOnAction((ActionEvent event) -> {
            
            ArrayList<Address> addressList = DatabaseObjects.getAllAddresses();
            ArrayList<City> cityList = DatabaseObjects.getAllCities();
            ArrayList<Country> countryList = DatabaseObjects.getAllCountries();
            ArrayList<String> inputErrorList = new ArrayList<>();
            
            if(customerNameField.getText().trim().equals("")||customerNameField.getText().trim().equals("Customer Name")){
                inputErrorList.add("->Enter a customer name\n");
            }
            if(activeComboBox.getValue()==null){
                inputErrorList.add("->Pick active or inactive\n");
            }
            if(address1Field.getText().trim().equals("")||address1Field.getText().trim().equals("Address(1)")){
                inputErrorList.add("->Enter Address(1)\n");
            }
            if(address2Field.getText().trim().equals("Address(2)")){
                inputErrorList.add("->Enter Address(2) or leave it blank\n");
            }
            if(postalCodeField.getText().trim().equals("")||postalCodeField.getText().trim().equals("Postal Code")){
                inputErrorList.add("->Enter a postal code\n");
            }
            if(postalCodeField.getText().trim().length()>5){
                inputErrorList.add("->Enter a postal code with less than 11 characters\n");
            }            
            if(phoneField.getText().trim().equals("")||phoneField.getText().trim().equals("Phone")){
                inputErrorList.add("->Enter a phone number\n");
            }
            if(cityField.getText().trim().equals("")||cityField.getText().trim().equals("City")){
                inputErrorList.add("->Enter a phone number\n");
            }
            if(countryField.getText().trim().equals("")||countryField.getText().trim().equals("Country")){
                inputErrorList.add("->Enter a phone number\n");
            }
            if(!inputErrorList.isEmpty()){
                StringBuilder errorMessage = new StringBuilder("You must correct the following errors on the form before saving this customer:\n");
                inputErrorList.stream().forEach((error) -> {
                    errorMessage.append(error);
                });                
                JOptionPane.showMessageDialog(null,errorMessage,"Doh!!",JOptionPane.ERROR_MESSAGE);
            }else{
                
                Address addressToInsertOrEdit = null;
                City cityToInsertOrEdit = null;
                Country countryToInsertOrEdit = null;
                Customer customerToInsertorUpdate = null;
                
                int countryId;
                int cityId;
                int addressId;
                int countriesAdded =0;
                int citiesAddedOrEdited =0;
                int addressesAddedOrEdited =0;
                int customersAddedOrEdited =0;
                byte active;
                
                Customer.checkNextId();
                Address.checkNextId();
                City.checkNextId();
                Country.checkNextId();
                
                String addressInputString = address1Field.getText().trim()+address2Field.getText().trim()+postalCodeField.getText().trim();                

                for(Country country:countryList){
                    if(countryField.getText().trim().toLowerCase().equals(country.getCountry().trim().toLowerCase())){
                        countryToInsertOrEdit = country;
                        break;
                    } 
                }

                for(City city:cityList){
                    if(cityField.getText().trim().toLowerCase().equals(city.getCity().trim().toLowerCase())){
                        cityToInsertOrEdit = city;
                        break;
                    }
                }

                for(Address address:addressList){
                    String existingAddressValue = address.getAddress().trim()+address.getAddress2().trim()+address.getPostalCode().trim();
                    if(existingAddressValue.toLowerCase().equals(addressInputString.toLowerCase())){
                        addressToInsertOrEdit = address;
                        break;
                    }
                }
                
                if(countryToInsertOrEdit==null){
                    countryToInsertOrEdit = new Country(countryField.getText().trim(),Timestamp.valueOf(LocalDateTime.now()), 
                        System.getProperty("user.name"),Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"));
                    countriesAdded = DatabaseObjects.insertCountry(countryToInsertOrEdit);
                    System.out.println("Countries Added: "+countriesAdded);
                }               
                countryId = countryToInsertOrEdit.getCountryId();
                
                if(cityToInsertOrEdit==null){
                    cityToInsertOrEdit = new City(cityField.getText().trim(),countryId,Timestamp.valueOf(LocalDateTime.now()), 
                        System.getProperty("user.name"),Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"));
                    citiesAddedOrEdited = DatabaseObjects.insertCity(cityToInsertOrEdit);
                    System.out.println("Cities Added: "+citiesAddedOrEdited);
                }else{
                    if(cityToInsertOrEdit.getCountryId()!=countryId){
                        cityToInsertOrEdit.setCountryId(countryId);
                        citiesAddedOrEdited = DatabaseObjects.updateCity(cityToInsertOrEdit);
                        System.out.println("Cities Edited: "+citiesAddedOrEdited);
                    }
                }
                cityId = cityToInsertOrEdit.getCityId();
                
                if(addressToInsertOrEdit==null){
                    addressToInsertOrEdit = new Address(address1Field.getText().trim(),address2Field.getText().trim(),
                        cityId,postalCodeField.getText().trim(),phoneField.getText().trim(),Timestamp.valueOf(LocalDateTime.now()),
                        System.getProperty("user.name"),Timestamp.valueOf(LocalDateTime.now()),System.getProperty("user.name"));
                    addressesAddedOrEdited = DatabaseObjects.insertAddress(addressToInsertOrEdit);
                    System.out.println("Addresses Added: "+addressesAddedOrEdited);
                }else{
                    if(addressToInsertOrEdit.getCityId()!=cityId){
                        addressToInsertOrEdit.setCityId(cityId);
                        addressesAddedOrEdited = DatabaseObjects.updateAddress(addressToInsertOrEdit);
                        System.out.println("Addresses Edited: "+addressesAddedOrEdited);
                    }
                }
                addressId = addressToInsertOrEdit.getAddressId();
                
                if(activeComboBox.getSelectionModel().getSelectedItem().equals("Active")){
                    active = 1;
                } else {
                    active = 0;
                } 
                
                if(isNewCustomer){                    
                    customerToInsertorUpdate = new Customer(customerNameField.getText().trim(),addressId, (byte)active,
                    Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"),
                    Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"));
                    customersAddedOrEdited = DatabaseObjects.insertCustomer(customerToInsertorUpdate);
                    System.out.println("Customers Added: "+customersAddedOrEdited);
                    ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                                
                    addEditCustomerWindow.hide();
                }else{                    
                    customerToInsertorUpdate = new Customer(currentlySelectedCustomer.getCustomerId(),customerNameField.getText().trim(),addressId, (byte)active,
                    Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"),
                    Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"));
                    int rowsEdited = DatabaseObjects.updateCustomer(customerToInsertorUpdate);
                    System.out.println("Customers Edited: "+rowsEdited);
                    ArrayList<Appointment> appointmentList = DatabaseObjects.getAllAppointments();
                    for(Appointment appt:appointmentList){
                        if(appt.getContact().trim().toLowerCase().equals(currentlySelectedCustomer.getCustomerName().trim().toLowerCase())){
                            appt.setContact(customerNameField.getText().trim());
                            DatabaseObjects.updateAppointment(appt);
                            break;
                        }
                    }
                    ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                                
                    addEditCustomerWindow.hide();
                }
            
            }
            
        
        });
        
        //this lambda allows the user to return to the management window
        cancel.setOnAction((ActionEvent event) -> {
            ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                                
            addEditCustomerWindow.close();
        });
        
        addEditCustomerWindow.setTitle("Add/Edit Customer");
        addEditCustomerWindow.setScene(scene);
        scene.getStylesheets().add("/GUIs/GUIsStyleSheet.css");
        addEditCustomerWindow.show();
        primaryStage.hide();
    }
    
}
