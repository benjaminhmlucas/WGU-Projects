/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import CustomerDatabase.DatabaseObjects;
import CustomerDatabase.SampleData;
import Objects.Address;
import Objects.Appointment;
import Objects.City;
import Objects.Country;
import Objects.Customer;
import Objects.User;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Small-Dell
 */
public class ManagementWindow{
    
    private static boolean adminMode = false;
    private static boolean appointmentMessageShown =false;

    private TableView customerInfoWindow;
    private TableView userInfoWindow;
    private TableView apptInfoWindow;
    private TableView ApptByUserInfoWindow;
    
    public ManagementWindow(Stage primaryStage, User currentlyLoggedInUser){
          
        User currentUser = currentlyLoggedInUser;
        User.resetIdCounter();
        Stage managementWindow = new Stage(); 
            
        ArrayList<Appointment> apptList = new ArrayList<>(DatabaseObjects.getAllAppointments());
        ArrayList<User> userList = new ArrayList<>(DatabaseObjects.getAllUsers());
        ArrayList<Customer> customerList = new ArrayList<>(DatabaseObjects.getAllCustomers());
        final ArrayList<Appointment> apptListForUser = new ArrayList<>();

        customerInfoWindow = new TableView<>(FXCollections.observableArrayList(customerList));
        userInfoWindow = new TableView<>(FXCollections.observableArrayList(userList));
        apptInfoWindow = new TableView<>(FXCollections.observableArrayList(apptList));        
        
        Button addAppointmentBtn = new Button("Add Appointment");
        Button editAppointmentBtn = new Button("Edit Appointment");
        Button deleteAppointmentBtn = new Button("Delete Appointment");
        
        Button deleteAllDataAppointment = new Button("Delete All Database Data(keep user: test)");
        Button createSampleDataAppointment = new Button("Create Sample data(erases data, then adds new data)");
        
        //Report Items
        Button getUserScheduleBtn = new Button("User Schedule");
        Button getApptPerMonthBtn = new Button    ("Appts Per Month");
        Button getApptsPerUserBtn = new Button("  Appts Per User  ");
        Label userInputNameLabel = new Label("User To Search: ");
        TextField userNameInputField = new TextField("Enter a user name");    
        
        Button addCustomerBtn = new Button("Add Customer");
        Button editCustomerBtn = new Button("Edit Customer");
        Button deleteCustomerBtn = new Button("Delete Customer");
        
        Button addUserBtn = new Button("Add User");
        Button editUserBtn = new Button("Edit User");
        Button deleteUserBtn = new Button("Delete User");
        
        Button logoutAppointmentTabBtn = new Button("Logout");
        Button logoutCustomerTabBtn = new Button("Logout");
        Button logoutUserTabBtn = new Button("Logout");
        
        ToggleButton adminModeTogBtn = new ToggleButton("Admin mode");
        
        ToggleGroup filterGroup = new ToggleGroup();
        RadioButton rbAll = new RadioButton("All");
        RadioButton rbMonth = new RadioButton("Next 30 Days");
        RadioButton rbWeek = new RadioButton("Next 7 Days");
        
        Group root = new Group();
        VBox vBoxOuter = new VBox();
        TabPane tabPane = new TabPane();
        VBox vboxApptRoot = new VBox();
        vboxApptRoot.setId("vboxApptRoot");
        VBox vboxReportsRoot = new VBox();
        vboxReportsRoot.setId("vboxReportsRoot");
        VBox vboxCustomersRoot = new VBox();
        vboxCustomersRoot.setId("vboxCustomersRoot");
        VBox vboxUsersRoot = new VBox();
        vboxUsersRoot.setId("vboxUsersRoot");
        VBox firstOpenedReportInfoBox = new VBox();
        firstOpenedReportInfoBox.setId("firstOpenedReportInfoBox");        
        VBox monthlyReportInfoBox = new VBox();
        monthlyReportInfoBox.setId("monthlyReportInfoBox");
        HBox reportsButtons = new HBox();
        reportsButtons.setId("monthlyReportsButtons");
        VBox userScheduleButtonBox = new VBox();
        userScheduleButtonBox.setId("userScheduleButtonBox");
        
        //create tabs for tableview
        ArrayList<String> tabNameList = new ArrayList<>(Arrays.asList("Appointments","Reports","Customer Accounts","User Accounts"));
        
        //name columns for appointments tab
        
        ArrayList<String> apptColumnHeaderText;
        ArrayList<String> apptColumnIdentifier;        
        
        if(!adminMode){
            apptColumnHeaderText = new ArrayList<>(Arrays.asList("Appointment Id","Title","User","Contact","Type","Start Time", "End Time"));
            apptColumnIdentifier = new ArrayList<>(Arrays.asList("appointmentId","title","userName","contactText","type","formattedStart", "formattedEnd"));        
        }else{
            apptColumnHeaderText = new ArrayList<>(Arrays.asList("Appointment Id","Title","User","Contact","Type","Start Time", "End Time","Date Created","Created By","Last updated","Last updated by"));
            apptColumnIdentifier = new ArrayList<>(Arrays.asList("appointmentId","title","userName","contactText","type","formattedStart", "formattedEnd","formattedCreateDate","createdBy","formattedLastUpdate","lastUpdatedBy"));        
        }

        //name columns for customers tab
        ArrayList<String> customerColumnHeaderText = new ArrayList<>(Arrays.asList("Customer ID","Customer Name","Address Id","Customer active","Date Created","Created By","Last updated","Last updated by"));
        ArrayList<String> customerColumnIdentifier = new ArrayList<>(Arrays.asList("customerId","customerName","addressId","translatedActive","formattedCreateDate","createdBy","formattedLastUpdate","lastUpdatedBy"));
        
        //name columns for users tab
        ArrayList<String> userColumnHeaderText = new ArrayList<>(Arrays.asList("User ID","User Name","User active","Date Created","Created By","Last updated","Last updated by"));
        ArrayList<String> userColumnIdentifier = new ArrayList<>(Arrays.asList("userId","userName","translatedActive","formattedCreateDate","createdBy","formattedLastUpdate","lastUpdatedBy"));
        
        //Fill Appointment Tab with data
        for (int i=0; i<apptColumnHeaderText.size(); i++){
            TableColumn<String, String> newColumn = new TableColumn<>(apptColumnHeaderText.get(i));
            newColumn.setCellValueFactory(new PropertyValueFactory<>(apptColumnIdentifier.get(i)));
            apptInfoWindow.getColumns().add(newColumn);
	}
        
        //Fill Customer Tab with data
        for (int i=0; i<customerColumnHeaderText.size(); i++){
            TableColumn<String, String> newColumn = new TableColumn<>(customerColumnHeaderText.get(i));
            newColumn.setCellValueFactory(new PropertyValueFactory<>(customerColumnIdentifier.get(i)));
            customerInfoWindow.getColumns().add(newColumn);
	}
        
        //Fill Customer Tab with data
        for (int i=0; i<customerColumnHeaderText.size(); i++){
            TableColumn<String, String> newColumn = new TableColumn<>(customerColumnHeaderText.get(i));
            newColumn.setCellValueFactory(new PropertyValueFactory<>(customerColumnIdentifier.get(i)));
            customerInfoWindow.getColumns().add(newColumn);
	}
        
        //Fill User Tab with data
        for (int i=0; i<userColumnHeaderText.size(); i++){
            TableColumn<String, String> newColumn = new TableColumn<>(userColumnHeaderText.get(i));
            newColumn.setCellValueFactory(new PropertyValueFactory<>(userColumnIdentifier.get(i)));
            userInfoWindow.getColumns().add(newColumn);
	}
        
        //Create Tabs for Window
        int adminModeTabCount;
        if(adminMode){
            adminModeTabCount = 4;
            adminModeTogBtn.setSelected(true);
        }else{
            adminModeTabCount = 3;
            adminModeTogBtn.setSelected(false);
        }
        
        for (int i = 0; i < adminModeTabCount; i++) {

            Tab tab = new Tab();
            tab.setText(tabNameList.get(i));
            HBox hboxSampleDataButtons = new HBox();
            HBox hboxButtonRow = new HBox();
            hboxSampleDataButtons.setId("buttonBoxHorizontal");
            hboxButtonRow.setId("buttonBoxHorizontal");
            switch(i){
                //Appointment List Tab
                case 0:
                    HBox filterSelectionHBox = new HBox();
                    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                    Timestamp nowPlus15Minutes = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15));
                    Timestamp nowPlus7Days = Timestamp.valueOf(LocalDateTime.now().plusDays(7));
                    Timestamp nowPlus30Days = Timestamp.valueOf(LocalDateTime.now().plusDays(30));
                    for(Appointment appt:DatabaseObjects.getAllAppointments()){
                        if(appt.getStart().isBefore(nowPlus15Minutes.toLocalDateTime())&&!appointmentMessageShown){
                            JOptionPane.showMessageDialog(null, "You have an upcoming appointment in the next 15 minutes!\n"
                                    + "Appointment Info:\n"                                    
                                    + "User: "+DatabaseObjects.getUserById(appt.getUserId()).getUserName()+"\n"
                                    + "Has Appointment: "+appt.getTitle()+"\n"
                                    + "Appointment Description: "+appt.getDescription()+"\n"
                                    + "Appointment Customer Id: "+appt.getCustomerId()+"\n"
                                    + "Location: "+appt.getLocation()+"\n"
                                    + "Start Time: "+appt.getStart().format(DatabaseObjects.getReadableDateFormatter())
                                    +"\n","Alert!",JOptionPane.INFORMATION_MESSAGE);                            
                        }                        
                    }
                    appointmentMessageShown=true;
                    filterGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
                        if (filterGroup.getSelectedToggle() != null) {
                            RadioButton selectedRadioButton = (RadioButton) filterGroup.getSelectedToggle();
                            String toogleGroupValue = selectedRadioButton.getText();
                            FilteredList<Appointment> filteredData = new FilteredList<>(FXCollections.observableArrayList(DatabaseObjects.getAllAppointments()));
                            switch(toogleGroupValue){
                                case "All":
                                    filteredData.setPredicate(row -> {
                                        LocalDateTime rowDate = row.getStart();
                                        return rowDate.isBefore(now.toLocalDateTime()) || rowDate.isAfter(now.toLocalDateTime()) || rowDate.equals(now.toLocalDateTime());
                                    });
                                    break;
                                case "Next 30 Days":
                                    filteredData.setPredicate(row -> {
                                        LocalDateTime rowDate = row.getStart();
                                        return rowDate.isAfter(now.toLocalDateTime()) && rowDate.isBefore(nowPlus30Days.toLocalDateTime());
                                    });
                                    break;
                                case "Next 7 Days":
                                    filteredData.setPredicate(row -> {
                                        LocalDateTime rowDate = row.getStart();
                                        return rowDate.isAfter(now.toLocalDateTime()) && rowDate.isBefore(nowPlus7Days.toLocalDateTime());
                                    });
                                    break;
                            }
                            apptInfoWindow.setItems(filteredData);
                        }
                    });
                    rbAll.setSelected(true);
                    rbAll.setToggleGroup(filterGroup);
                    rbMonth.setToggleGroup(filterGroup);
                    rbWeek.setToggleGroup(filterGroup);
                    filterSelectionHBox.setId("filterSelectionHBox");
                    filterSelectionHBox.getChildren().addAll(rbAll,rbMonth,rbWeek);
                    filterSelectionHBox.setMaxWidth(300);
                    if(adminMode){
                        hboxSampleDataButtons.getChildren().addAll(adminModeTogBtn,createSampleDataAppointment,deleteAllDataAppointment);
                    }else{
                        hboxSampleDataButtons.getChildren().add(adminModeTogBtn);
                    }                    
                    hboxButtonRow.getChildren().addAll(addAppointmentBtn,editAppointmentBtn,deleteAppointmentBtn,logoutAppointmentTabBtn);                    
                    vboxApptRoot.getChildren().addAll(hboxSampleDataButtons,hboxButtonRow,filterSelectionHBox,apptInfoWindow);
                    vboxApptRoot.setAlignment(Pos.CENTER);
                    tab.setContent(vboxApptRoot);
                    break;
                //Reports Tab-> pressing the buttons on this tab changes what is presented.  See lines 400-477
                case 1:
                    vboxReportsRoot.getChildren().addAll(reportsButtons,firstOpenedReportInfoBox);
                    userScheduleButtonBox.getChildren().addAll(userInputNameLabel,userNameInputField,getUserScheduleBtn);
                    reportsButtons.getChildren().addAll(getApptPerMonthBtn,userScheduleButtonBox,getApptsPerUserBtn);
                    Text firstOpenedInfoText = new Text("Please select a report to run:\n"
                            + ">>Enter a user name in the 'User To Search Box' if you would like to search a schedule");
                    firstOpenedReportInfoBox.getChildren().add(firstOpenedInfoText);
                    vboxReportsRoot.setAlignment(Pos.CENTER);
                    tab.setContent(vboxReportsRoot);              

                    break;
                //Customer List Tab
                case 2:
                    hboxButtonRow.getChildren().addAll(addCustomerBtn,editCustomerBtn,deleteCustomerBtn,logoutCustomerTabBtn);                    
                    vboxCustomersRoot.getChildren().addAll(hboxButtonRow,customerInfoWindow);
                    vboxCustomersRoot.setAlignment(Pos.CENTER);
                    tab.setContent(vboxCustomersRoot);
                    break;                    
                //User List Tab
                case 3:
                    hboxButtonRow.getChildren().addAll(addUserBtn,editUserBtn,deleteUserBtn,logoutUserTabBtn);                    
                    vboxUsersRoot.getChildren().addAll(hboxButtonRow,userInfoWindow);
                    vboxUsersRoot.setAlignment(Pos.CENTER);
                    tab.setContent(vboxUsersRoot);
                    break;
            }            

            tabPane.getTabs().add(tab);            
        }
        //Assign Table click handler, link user to a user record(if admin mode is on) or a customer record
        apptInfoWindow.setOnMouseClicked((MouseEvent me) -> {
            if(me.getButton().equals(MouseButton.PRIMARY)){
                if(me.getClickCount() == 2){
                    TablePosition pos = (TablePosition) apptInfoWindow.getSelectionModel().getSelectedCells().get(0);
                    int row = pos.getRow();
                    Appointment  appt = (Appointment)apptInfoWindow.getItems().get(row);
                    TableColumn col = pos.getTableColumn();
                    try{
                        String columnTitle = (String) col.getText();
                        if(columnTitle.equals("Contact")){
                            Appointment selectedAppt = (Appointment)apptInfoWindow.getSelectionModel().getSelectedItem();
                            Customer selectedCustomer = DatabaseObjects.getCustomerById(selectedAppt.getCustomerId());
                            Address selectedAddress = DatabaseObjects.getAddressById(selectedCustomer.getAddressId());
                            City selectedCity = DatabaseObjects.getCityById(selectedAddress.getCityId());
                            Country selectedCountry = DatabaseObjects.getCountryById(selectedCity.getCountryId());
                            AddEditCustomerWindow newWindow = new AddEditCustomerWindow(primaryStage, currentUser,selectedCustomer,selectedAddress,selectedCity,selectedCountry,false);
                            managementWindow.close();
                        }
                        if(columnTitle.equals("User")){
                            if(apptInfoWindow.getSelectionModel().getSelectedItem()!=null){
                                Appointment selectedAppt = (Appointment)apptInfoWindow.getSelectionModel().getSelectedItem();
                                User selectedUser = DatabaseObjects.getUserById(selectedAppt.getUserId());
                                AddEditUserWindow newWindow = new AddEditUserWindow(primaryStage,selectedUser,false);
                                managementWindow.close();
                            } else {
                                JOptionPane.showMessageDialog(null, "The SQL Gods say you they can't find that user!","Doh!!",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        
                    }catch(Exception e){/**Do-Nothin*/}
                }
            }
        });
        
        //Assign Button Functions------------------------------------------------------------------------------------------------------------------------------------------
        //this lambda allows the user to toggle admin mode on and off.  Resetting the screen and presenting alternate sets of data
        adminModeTogBtn.setOnAction((ActionEvent event) ->{
            if(adminModeTogBtn.isSelected()){
                adminMode = true;
                ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                                
                managementWindow.close();
            } else {
                adminMode = false;
                ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                                
                managementWindow.close();
            }
        });
        //this lamdba allows user to delete all data in the database except for the main test user.  'test' cannot be deleted through the GUI
        deleteAllDataAppointment.setOnAction((ActionEvent event) ->{
            DatabaseObjects.deleteAllDatabaseObjects();
            ManagementWindow newWindow = new ManagementWindow(primaryStage,DatabaseObjects.getCurrentLoggedInUser());                                
            managementWindow.close();
            
        });
        //this lambda allows the admin mode user to delete all data in the database and fill it with sample data for testing purposes
        createSampleDataAppointment.setOnAction((ActionEvent event) ->{
            User.resetIdCounter();
            Customer.resetIdCounter();
            Address.resetIdCounter();
            City.resetIdCounter();
            Country.resetIdCounter();
            Appointment.resetIdCounter();
            
            SampleData.createSampleData(9);
            SampleData.addTestData();

            ManagementWindow newWindow = new ManagementWindow(primaryStage,currentUser);                                
            managementWindow.close();
        });
        
        //this lambda allows the user to add an appointment from the appointment tab
        addAppointmentBtn.setOnAction((ActionEvent event) -> {
            Appointment newAppt = null;
            AddEditAppointmentWindow newWindow = new AddEditAppointmentWindow(primaryStage,newAppt,true);
            managementWindow.close();
        });
        
        //this lambda allows the user to edit the selected appointment
        editAppointmentBtn.setOnAction((ActionEvent event) -> {
            if(apptInfoWindow.getSelectionModel().getSelectedItem()!=null){
                Appointment selectedAppt = (Appointment)apptInfoWindow.getSelectionModel().getSelectedItem();
                AddEditAppointmentWindow newWindow = new AddEditAppointmentWindow(primaryStage,selectedAppt,false);
                managementWindow.close();
            } else {
                JOptionPane.showMessageDialog(null, "The SQL Gods say they can't find that appointment!","Doh!!",JOptionPane.ERROR_MESSAGE);
            }
        });
        //this lambda allows the user to delete the selected appointment
        deleteAppointmentBtn.setOnAction((ActionEvent event) -> {
            if(apptInfoWindow.getSelectionModel().getSelectedItem()!=null){
                Appointment selectedAppt = (Appointment)apptInfoWindow.getSelectionModel().getSelectedItem();
                DatabaseObjects.deleteAppointmentById(selectedAppt.getAppointmentId());
                rbAll.setSelected(true);
                apptInfoWindow.setItems(FXCollections.observableArrayList(DatabaseObjects.getAllAppointments()));                
                Appointment.getUsedAppointmentIds().remove((Integer)selectedAppt.getAppointmentId());            
            }else{
                JOptionPane.showMessageDialog(null, "The SQL Gods say you must select an appointment to delete!","Doh!!",JOptionPane.ERROR_MESSAGE);            
            }
        });
        //this lambda allows the user to logout
        logoutAppointmentTabBtn.setOnAction((ActionEvent event) -> {
            LoginWindow newWindow = new LoginWindow();
            appointmentMessageShown =false;
            newWindow.start(primaryStage);
            managementWindow.close();
        });
        //This lambda allows the user to run a report that give the user the number of appointments per month
        getApptPerMonthBtn.setOnAction((ActionEvent event) -> {
                    HashMap <Integer,Integer> apptPerMonth = DatabaseObjects.getAppointmentsPerMonth();
                    Text monthTitle = new Text("Appointments Per Month:");
                    monthTitle.setUnderline(true);
                    monthlyReportInfoBox.getChildren().clear();
                    Label jan = new Label("January: "+apptPerMonth.get(1));
                    Label feb = new Label("February: "+apptPerMonth.get(2));
                    Label mar = new Label("March: "+apptPerMonth.get(3));
                    Label apr = new Label("April: "+apptPerMonth.get(4));
                    Label may = new Label("May: "+apptPerMonth.get(5));
                    Label jun = new Label("June: "+apptPerMonth.get(6));
                    Label jul = new Label("July: "+apptPerMonth.get(7));
                    Label aug = new Label("August: "+apptPerMonth.get(8));
                    Label sep = new Label("September: "+apptPerMonth.get(9));
                    Label oct = new Label("October: "+apptPerMonth.get(10));
                    Label nov = new Label("November: "+apptPerMonth.get(11));
                    Label dec = new Label("December: "+apptPerMonth.get(12));                    
                    monthlyReportInfoBox.getChildren().addAll(monthTitle,jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec);
                    vboxReportsRoot.getChildren().clear();
                    vboxReportsRoot.getChildren().addAll(reportsButtons,monthlyReportInfoBox);
      
        });
        //this lambda allows the user to get a users schedule by typing the user name into the box and pressing the button
        getUserScheduleBtn.setOnAction((ActionEvent event) -> { 
            vboxReportsRoot.getChildren().clear();
            reportsButtons.getChildren().clear();
            String userNameInput = userNameInputField.getText().trim();
            apptListForUser.clear();
            apptListForUser.addAll(DatabaseObjects.getAppointmentsForUserByUserName(userNameInput));
            ApptByUserInfoWindow = new TableView<>(FXCollections.observableArrayList(apptListForUser)); 
            ApptByUserInfoWindow.setMaxSize(560, 300);
            ApptByUserInfoWindow.setMinSize(560, 300);
            ApptByUserInfoWindow.setPrefSize(560, 300);
            ArrayList<String> apptByUserColumnHeaderText = new ArrayList<>(Arrays.asList("Title","Contact","Type","Start Time", "End Time"));
            ArrayList<String> apptByUserColumnIdentifier = new ArrayList<>(Arrays.asList("title","contactText","type","formattedStart", "formattedEnd"));
            for (int i=0; i<apptByUserColumnHeaderText.size(); i++){
                TableColumn<String, String> newColumn = new TableColumn<>(apptByUserColumnHeaderText.get(i));
                newColumn.setCellValueFactory(new PropertyValueFactory<>(apptByUserColumnIdentifier.get(i)));
                ApptByUserInfoWindow.getColumns().add(newColumn);
            }
            reportsButtons.getChildren().addAll(getApptPerMonthBtn,userScheduleButtonBox,getApptsPerUserBtn);
            vboxReportsRoot.getChildren().addAll(reportsButtons,ApptByUserInfoWindow);
            ApptByUserInfoWindow.setOnMouseClicked((MouseEvent me) -> {
                if(me.getButton().equals(MouseButton.PRIMARY)){
                    if(me.getClickCount() == 2){
                        TablePosition pos = (TablePosition) ApptByUserInfoWindow.getSelectionModel().getSelectedCells().get(0);
                        int row = pos.getRow();
                        Appointment  appt = (Appointment)ApptByUserInfoWindow.getItems().get(row);
                        TableColumn col = pos.getTableColumn();
                        try{
                            String columnTitle = (String) col.getText();
                            if(columnTitle.equals("Contact")){
                                Appointment selectedAppt = (Appointment)ApptByUserInfoWindow.getSelectionModel().getSelectedItem();
                                Customer selectedCustomer = DatabaseObjects.getCustomerById(selectedAppt.getCustomerId());
                                Address selectedAddress = DatabaseObjects.getAddressById(selectedCustomer.getAddressId());
                                City selectedCity = DatabaseObjects.getCityById(selectedAddress.getCityId());
                                Country selectedCountry = DatabaseObjects.getCountryById(selectedCity.getCountryId());
                                AddEditCustomerWindow newWindow = new AddEditCustomerWindow(primaryStage, currentUser,selectedCustomer,selectedAddress,selectedCity,selectedCountry,false);
                                managementWindow.close();
                            }
                        }catch(Exception e){
                            /**Do-Nothin*/
                        }
                    }
                }
            });             
        });
        // this lambda allows the user to see a report of the number of appointments per user
        getApptsPerUserBtn.setOnAction((ActionEvent event) -> {
            vboxReportsRoot.getChildren().clear();
            ArrayList<String> customersPerUserList = DatabaseObjects.getApptsPerUserList();
            ObservableList<String> customersPerUserObservableListView = FXCollections.<String>observableArrayList(customersPerUserList);
            ListView<String> customersPerUserListView = new ListView(customersPerUserObservableListView);
            customersPerUserListView.setMaxSize(200, 300);
            customersPerUserListView.setMinSize(200, 300);
            customersPerUserListView.setPrefSize(200, 300);
            vboxReportsRoot.getChildren().addAll(reportsButtons,customersPerUserListView);
        });
        //this lambda takes the user to the add customer window to add a customer to the database
        addCustomerBtn.setOnAction((ActionEvent event) -> {
            Customer newCustomer = null;
            Address newAddress = null;
            City newCity = null;
            Country newCountry = null;
            AddEditCustomerWindow newWindow = new AddEditCustomerWindow(primaryStage,currentUser,newCustomer,newAddress,newCity,newCountry,true);
            managementWindow.close();
        });
        //this lambda takes the user to the edit customer screen      
        editCustomerBtn.setOnAction((ActionEvent event) -> {            
            if(customerInfoWindow.getSelectionModel().getSelectedItem()!=null){
                Customer selectedCustomer = (Customer)customerInfoWindow.getSelectionModel().getSelectedItem();
                Address selectedAddress = DatabaseObjects.getAddressById(selectedCustomer.getAddressId());
                City selectedCity = DatabaseObjects.getCityById(selectedAddress.getCityId());
                Country selectedCountry = DatabaseObjects.getCountryById(selectedCity.getCountryId());

                AddEditCustomerWindow newWindow = new AddEditCustomerWindow(primaryStage, currentUser,selectedCustomer,selectedAddress,selectedCity,selectedCountry,false);
                managementWindow.close();
            } else {
                JOptionPane.showMessageDialog(null, "The SQL Gods say you must select a customer to edit!","Doh!!",JOptionPane.ERROR_MESSAGE);
            }

        });
        //this lambda allows the user to delete the selected customer and checks addresses, cities and countries.  
        //If no customers have the address Id then the address is deleted, this process repeats all the way up to country
        //This ensures that unused address, city and country items are deleted
        deleteCustomerBtn.setOnAction((ActionEvent event) -> {
            if(customerInfoWindow.getSelectionModel().getSelectedItem()!=null){
                Customer selectedCustomer = (Customer)customerInfoWindow.getSelectionModel().getSelectedItem();
                DatabaseObjects.deleteCustomer(selectedCustomer);
                customerInfoWindow.getItems().remove(customerInfoWindow.getSelectionModel().getSelectedItem());
                apptInfoWindow.setItems(FXCollections.observableArrayList(DatabaseObjects.getAllAppointments()));
                apptInfoWindow.refresh();
            }else{
                JOptionPane.showMessageDialog(null, "The SQL Gods say you must select a customer to delete!","Doh!!",JOptionPane.ERROR_MESSAGE);
            }
        });
        //this allows the user to return to the login screen
        logoutCustomerTabBtn.setOnAction((ActionEvent event) -> {
            LoginWindow newWindow = new LoginWindow();
            appointmentMessageShown=false;
            newWindow.start(primaryStage);
            managementWindow.close();
        });
        //this takes the user to the add user screen to add a user to the database
        addUserBtn.setOnAction((ActionEvent event) -> {
            User newUser = null;
            AddEditUserWindow newWindow = new AddEditUserWindow(primaryStage,newUser,true);
            managementWindow.close();
        });
        //this lambda takes the user to the edit user screen
        editUserBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(userInfoWindow.getSelectionModel().getSelectedItem()!=null){
                    User selectedUser = (User)userInfoWindow.getSelectionModel().getSelectedItem();
                    AddEditUserWindow newWindow = new AddEditUserWindow(primaryStage,selectedUser,false);                                
                    managementWindow.close();                   
                } else {
                    JOptionPane.showMessageDialog(null, "The SQL Gods say you must select a user to edit!","Doh!!",JOptionPane.ERROR_MESSAGE);
                }       
            } 
        });
        //this lambda allows the user to delete the selected user object
        deleteUserBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(userInfoWindow.getSelectionModel().getSelectedItem()!=null){                    
                    User selectedUser = (User)userInfoWindow.getSelectionModel().getSelectedItem();
                    if(selectedUser.getUserId()==1){
                        JOptionPane.showMessageDialog(null, "The SQL Gods say they can't delete the main user!","Doh!!",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(DatabaseObjects.getCurrentLoggedInUser().getUserId()==selectedUser.getUserId()){
                        int returnValue = JOptionPane.showConfirmDialog(null, "Are you sure that you want to delete your user account? "
                                + "Doing so will log you out,DESTROY ALL OF YOUR APPOINTMENTS AND YOU WILL NOT BE ABLE TO LOG IN AGAIN!!!!","Are you serious?!?!",JOptionPane.OK_CANCEL_OPTION);
                        if(returnValue==0){
                            DatabaseObjects.deleteAppointmentByUserId(selectedUser.getUserId());
                            DatabaseObjects.deleteUser(selectedUser);
                            LoginWindow newWindow = new LoginWindow();
                            appointmentMessageShown=false;
                            User.getUsedUserIds().remove(selectedUser.getUserId());
                            newWindow.start(primaryStage);
                            managementWindow.close();
                        }
                    } else {
                        DatabaseObjects.deleteAppointmentByUserId(selectedUser.getUserId());
                        DatabaseObjects.deleteUser(selectedUser);                        
                        userInfoWindow.getItems().remove(userInfoWindow.getSelectionModel().getSelectedItem());
                        apptInfoWindow.setItems(FXCollections.observableArrayList(DatabaseObjects.getAllAppointments()));
                        apptInfoWindow.refresh();
                    }                    
                } else {
                    JOptionPane.showMessageDialog(null, "The SQL Gods say you must select a user to delete!","Doh!!",JOptionPane.ERROR_MESSAGE);
                }                 
            }
        });
        //this lambda takes the user back to the login screen 
        logoutUserTabBtn.setOnAction((ActionEvent event) -> {
            LoginWindow newWindow = new LoginWindow();
            appointmentMessageShown=false;
            newWindow.start(primaryStage);
            managementWindow.close();
        });
        
        //set window objects in place 
        tabPane.setId("managementTabPane");
        vBoxOuter.getChildren().add(tabPane);
        root.getChildren().add(vBoxOuter);
        Scene scene = new Scene(root, 1000, 700);
        
        managementWindow.setTitle("Customer Tracker 9000x - User Accounts");
        managementWindow.setScene(scene);
        scene.getStylesheets().add("/GUIs/GUIsStyleSheet.css");
        tabPane.tabClosingPolicyProperty().set(UNAVAILABLE);
        tabPane.prefHeightProperty().bind(managementWindow.heightProperty());
        tabPane.prefWidthProperty().bind(managementWindow.widthProperty());        
        managementWindow.setMaxHeight(800);
        managementWindow.setMinHeight(800);
        managementWindow.setMaxWidth(1000);
        managementWindow.setMinWidth(1000);
        firstOpenedReportInfoBox.setPrefSize(500, 100);
        firstOpenedReportInfoBox.setMaxSize(500, 100);
        firstOpenedReportInfoBox.setMinSize(500, 100);
        monthlyReportInfoBox.setPrefSize(200, 500);
        monthlyReportInfoBox.setMaxSize(200, 500);
        monthlyReportInfoBox.setMinSize(200, 500);
        
        if(!adminMode){
            apptInfoWindow.setMaxSize(710, 525);
            apptInfoWindow.setMinSize(710, 525);
            apptInfoWindow.setPrefSize(710, 525);
        }else{
            apptInfoWindow.setMaxSize(800, 525);
            apptInfoWindow.setMinSize(800, 525);
            apptInfoWindow.setPrefSize(800, 525);
        } 

        customerInfoWindow.setMaxSize(849, 600);
        customerInfoWindow.setMinSize(849, 600);
        customerInfoWindow.setPrefSize(849, 600);        
        userInfoWindow.setMaxSize(680, 600);
        userInfoWindow.setMinSize(680, 600);
        userInfoWindow.setPrefSize(680, 600);

        
        managementWindow.show();
        primaryStage.hide();
        
    }
    public static void setAppointmentMessageShown(boolean shown){
        appointmentMessageShown = shown;
    }
    
}