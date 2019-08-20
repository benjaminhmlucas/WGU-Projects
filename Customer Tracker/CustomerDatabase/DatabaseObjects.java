/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerDatabase;

import Objects.Address;
import Objects.Appointment;
import Objects.City;
import Objects.Country;
import Objects.Customer;
import Objects.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import static java.time.ZoneId.systemDefault;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JOptionPane;


/**
 *
 * @author Small-Dell
 */
public class DatabaseObjects {
    
    private static final ArrayList<Address> allAddresses = new ArrayList<>();
    private static final ArrayList<Appointment> allAppointments = new ArrayList<>();
    private static final ArrayList<City> allCities = new ArrayList<>();
    private static final ArrayList<Country> allCountries = new ArrayList<>();
    private static final ArrayList<Customer> allCustomers = new ArrayList<>();
    private static final ArrayList<User> allUsers = new ArrayList<>();
    private static User currentLoggedInUser = null;
    private final static ArrayList<String> typeSelectorList = new ArrayList<>(Arrays.asList("Informational","Fact Finding","Sales"));
    private final static ArrayList<LocalTime> apptStartTimeList = new ArrayList<>(Arrays.asList(
            LocalTime.of(9,00),
            LocalTime.of(10,00),
            LocalTime.of(11,00),
            LocalTime.of(12,00),
            LocalTime.of(13,00),
            LocalTime.of(14,00),
            LocalTime.of(15,00),
            LocalTime.of(16,00)
        )
    );
    private final static ArrayList<LocalTime> apptEndTimeList = new ArrayList<>(Arrays.asList(
            LocalTime.of(10,00),
            LocalTime.of(11,00),
            LocalTime.of(12,00),
            LocalTime.of(13,00),
            LocalTime.of(14,00),
            LocalTime.of(15,00),
            LocalTime.of(16,00),
            LocalTime.of(17,00)
        )
    );
    private static String systemCountry = System.getProperty("user.country");
    private static String systemLanguage = System.getProperty("user.language");
    private static DateTimeFormatter readableDateFormatter = DateTimeFormatter.ofPattern("MMM dd, YYYY, hh:mm a");
    private static SimpleDateFormat readableDateFormat = new SimpleDateFormat("MMM dd, YYYY, hh:mmaa");

    public static int insertUser(User user){
        int rowsAdded = StatementMaker.makeUpdateDeleteOrInsertStatement(
                "INSERT INTO user VALUES ("
                + user.getUserId()+",'"+user.getUserName()+"','"+user.getPassword()+"',"+user.getActive()
                + ",'"+user.getCreateDate()+"','"+user.getCreatedBy()+"','"+user.getLastUpdate()+"','"
                +user.getLastUpdatedBy()+"');");        
        return rowsAdded;
    }
    
    public static int insertCustomer(Customer customer){
        int rowsAdded = StatementMaker.makeUpdateDeleteOrInsertStatement(
                "INSERT INTO customer VALUES ("
                + customer.getCustomerId()+",'"+customer.getCustomerName()+"','"+customer.getAddressId()+"',"+customer.getActive()
                + ",'"+customer.getCreateDate()+"','"+customer.getCreatedBy()+"','"+customer.getLastUpdate()+"','"
                +customer.getLastUpdatedBy()+"');");        
        return rowsAdded;
    }
            
    public static int insertAppointment(Appointment appt){       
        LocalDateTime start = appt.getStart();
        ZonedDateTime zStart = start.atZone(ZoneId.of(systemDefault().toString()));
        zStart = zStart.withZoneSameInstant(ZoneId.of("UTC"));
        start = zStart.toLocalDateTime();
        
        LocalDateTime end = appt.getEnd();
        ZonedDateTime zEnd = end.atZone(ZoneId.of(systemDefault().toString()));
        zEnd = zEnd.withZoneSameInstant(ZoneId.of("UTC"));
        end = zEnd.toLocalDateTime();
        
        int rowsAdded = StatementMaker.makeUpdateDeleteOrInsertStatement(
        "INSERT INTO appointment VALUES ("+appt.getAppointmentId()+","+appt.getCustomerId()+","+appt.getUserId()
                +",'"+appt.getTitle() +"','"+appt.getDescription()+"','"+appt.getLocation()+"','"+appt.getContact()+"','" 
                +appt.getType()+"','"+appt.getUrl()+"','"+start+"','"+end+"','"+appt.getCreateDate()
                +"','"+appt.getCreatedBy()+"','"+appt.getLastUpdate()+"','"+appt.getLastUpdatedBy() +"');");        
        return rowsAdded;
    }
    
    public static int insertAddress(Address addr){
        int rowsAdded = StatementMaker.makeUpdateDeleteOrInsertStatement(
            "INSERT INTO address VALUES ("
            + addr.getAddressId()+",'"+addr.getAddress()+"','"+addr.getAddress2()+"',"+addr.getCityId()
            + ",'"+addr.getPostalCode()+"','"+addr.getPhone()+"','"+addr.getCreateDate()+"','"+addr.getCreatedBy()+"','"+addr.getLastUpdate()+"','"
            +addr.getLastUpdatedBy()+"');");        
        return rowsAdded;
    }
    
    public static int insertCity(City city){
        int rowsAdded = StatementMaker.makeUpdateDeleteOrInsertStatement(
            "INSERT INTO city VALUES ("
            + city.getCityId()+",'"+city.getCity()+"',"+city.getCountryId()+",'"+city.getCreateDate()+"','"+city.getCreatedBy()+"','"+city.getLastUpdate()+"','"
            +city.getLastUpdatedBy()+"');");        
        return rowsAdded;
    }
    
    public static int insertCountry(Country country){
        int rowsAdded = StatementMaker.makeUpdateDeleteOrInsertStatement(
            "INSERT INTO country VALUES ("
            + country.getCountryId()+",'"+country.getCountry()+"','"+country.getCreateDate()+"','"+country.getCreatedBy()+"','"+country.getLastUpdate()+"','"
            + country.getLastUpdatedBy()+"');");           
        return rowsAdded;
    }
    public static int updateUser(User editedUser){
        int rowsEdited = StatementMaker.makeUpdateDeleteOrInsertStatement(
        "UPDATE user set "
            + "userName = '"+editedUser.getUserName()+"',"
            + "password = '"+editedUser.getPassword()+"',"
            + "active = "+editedUser.getActive()+","
            + "lastUpdate = '"+editedUser.getLastUpdate()+"',"
            + "lastUpdateBy = '"+editedUser.getLastUpdatedBy()+"'"
            + "WHERE userId = "+editedUser.getUserId());
        return rowsEdited;
    }
    
    public static int updateCustomer(Customer editedCustomer){
        int rowsEdited = StatementMaker.makeUpdateDeleteOrInsertStatement(
            "UPDATE customer set "
                + "customerName = '"+editedCustomer.getCustomerName()+"',"
                + "addressId = "+editedCustomer.getAddressId()+","
                + "active = "+editedCustomer.getActive()+","
                + "lastUpdate = '"+editedCustomer.getLastUpdate()+"',"
                + "lastUpdateBy = '"+editedCustomer.getLastUpdatedBy()+"'"
                + "WHERE customerId = "+editedCustomer.getCustomerId());
        return rowsEdited;
    }
    
    public static int updateCountry(Country editedCountry){
        int rowsEdited = StatementMaker.makeUpdateDeleteOrInsertStatement(
            "UPDATE country set "
                + "country = '"+editedCountry.getCountry()+"',"
                + "lastUpdate = '"+editedCountry.getLastUpdate()+"',"
                + "lastUpdateBy = '"+editedCountry.getLastUpdatedBy()+"'"
                + "WHERE countryId = "+editedCountry.getCountryId());
        return rowsEdited;
    }
    
    public static int updateCity(City editedCity){
        int rowsEdited = StatementMaker.makeUpdateDeleteOrInsertStatement(
            "UPDATE city set "
                + "city = '"+editedCity.getCity()+"',"
                + "countryId = "+editedCity.getCountryId()+","
                + "lastUpdate = '"+editedCity.getLastUpdate()+"',"
                + "lastUpdateBy = '"+editedCity.getLastUpdatedBy()+"'"
                + "WHERE cityId = "+editedCity.getCityId());
        return rowsEdited;
    } 
    
    
    public static int updateAddress(Address editedAddress){
        int rowsEdited = StatementMaker.makeUpdateDeleteOrInsertStatement(
        "UPDATE address set "
            +"address = '"+editedAddress.getAddress()+"',"
            +"address2 = '"+editedAddress.getAddress2()+"',"
            +"cityId = "+editedAddress.getCityId()+","
            +"postalCode = '"+editedAddress.getPostalCode()+"',"
            +"phone = '"+editedAddress.getPhone()+"',"
            +"lastUpdate = '"+editedAddress.getLastUpdate()+"',"
            +"lastUpdateBy = '"+editedAddress.getLastUpdatedBy()+"' "
            +"WHERE addressId = "+editedAddress.getAddressId());
        return rowsEdited;
    }    
    
    public static int updateAppointment(Appointment editedAppt){
        LocalDateTime start = editedAppt.getStart();
        ZonedDateTime zStart = start.atZone(ZoneId.of(systemDefault().toString()));
        zStart = zStart.withZoneSameInstant(ZoneId.of("UTC"));
        start = zStart.toLocalDateTime();
        
        LocalDateTime end = editedAppt.getEnd();
        ZonedDateTime zEnd = end.atZone(ZoneId.of(systemDefault().toString()));
        zEnd = zEnd.withZoneSameInstant(ZoneId.of("UTC"));
        end = zEnd.toLocalDateTime();
        int rowsEdited = StatementMaker.makeUpdateDeleteOrInsertStatement(
        "UPDATE appointment set "
            +"customerId = "+editedAppt.getCustomerId()+","
            +"userId = "+editedAppt.getUserId()+","
            +"title = '"+editedAppt.getTitle()+"',"
            +"description = '"+editedAppt.getDescription()+"',"
            +"location = '"+editedAppt.getLocation()+"',"
            +"contact = '"+editedAppt.getContact()+"',"
            +"type = '"+editedAppt.getType()+"',"
            +"url = '"+editedAppt.getUrl()+"',"
            +"start = '"+start+"',"
            +"end = '"+end+"',"
            +"lastUpdate = '"+editedAppt.getLastUpdate()+"',"
            +"lastUpdateBy = '"+editedAppt.getLastUpdatedBy()+"' "
            +"WHERE appointmentId = "+editedAppt.getAppointmentId());
        return rowsEdited;
    }    
    
    public static boolean deleteAllDatabaseObjects(){
        try {
            System.out.println("Appointments Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From appointment"));
            System.out.println("Users Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From user WHERE userId <> 1"));
            System.out.println("Customers Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From customer"));
            System.out.println("Addresses Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From address"));
            System.out.println("Cities Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From city"));
            System.out.println("Countries Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From country"));
            User.resetIdCounter();
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they couldn't delete everything!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public static boolean deleteAppointment(Appointment appt){
        try {
            System.out.println("Appointments Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From appointment WHERE appointmentId="+appt.getAppointmentId()));
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they couldn't delete the appointment!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
            
    public static boolean deleteAppointmentById(int apptId){
        try {
            System.out.println("Appointments Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From appointment WHERE appointmentId="+apptId));
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they couldn't delete the appointment!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public static boolean deleteAppointmentByUserId(int userId){
        try {
            System.out.println("Appointments Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From appointment WHERE userId="+userId));
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they couldn't delete the appointment!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
        
    public static boolean deleteUser(User user){
        if(user.getUserId()==1){
            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't delete the main user!","Doh!!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            System.out.println("Users Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From user WHERE userId="+user.getUserId()));
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they couldn't delete the user!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public static boolean deleteCustomer(Customer selectedCustomer){
        Address selectedAddress = DatabaseObjects.getAddressById(selectedCustomer.getAddressId());
        City selectedCity = DatabaseObjects.getCityById(selectedAddress.getCityId());
        Country selectedCountry = DatabaseObjects.getCountryById(selectedCity.getCountryId());                    

        int numCitiesWithCountryId=0;
        int numAddressesWithCityId=0;
        int numCustomersWithAddressId=0;

        System.out.println("Appointments Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("DELETE FROM appointment WHERE customerId = "+selectedCustomer.getCustomerId()));
        System.out.println("Customers Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("DELETE from customer where customerId = "+selectedCustomer.getCustomerId()));

        //check how many customers have the same address as the customer that was just deleted. If none, delete address
        try {
            ResultSet rsCustomersWithSelectedAddressIdCount = StatementMaker.makeSelectStatement("SELECT Count(*) FROM customer WHERE addressId = "+selectedAddress.getAddressId());        
            while(rsCustomersWithSelectedAddressIdCount.next()){
                numCustomersWithAddressId = rsCustomersWithSelectedAddressIdCount.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't can't count that address Id!!","Doh!!",JOptionPane.ERROR_MESSAGE);                      
        }                    
        if(numCustomersWithAddressId<1){
            System.out.println("Addresses Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("DELETE from address where addressId = "+selectedAddress.getAddressId()));
        }
        //Check how many addresses have the same city as the one that was just deleted.  Delete the city if there are none.
        try {
            ResultSet rsAddressesWithSelectedCityIdCount = StatementMaker.makeSelectStatement("SELECT Count(*) FROM address WHERE cityId = "+selectedCity.getCityId());
            while(rsAddressesWithSelectedCityIdCount.next()){
                numAddressesWithCityId = rsAddressesWithSelectedCityIdCount.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't can't count that city Id!!","Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        if(numAddressesWithCityId<1){
            System.out.println("Cities Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("DELETE from city where cityId = "+selectedCity.getCityId()));
        }
        //Check how many cities have the same countryId as the one that was just deleted.  Delete the country if there are none.
        try {
            ResultSet rsCitiesWithSelectedCountryIdCount = StatementMaker.makeSelectStatement("SELECT Count(*) FROM city WHERE countryId = "+selectedCountry.getCountryId());
            while(rsCitiesWithSelectedCountryIdCount.next()){
                numCitiesWithCountryId = rsCitiesWithSelectedCountryIdCount.getInt(1);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't count that country Id!","Doh!!",JOptionPane.ERROR_MESSAGE);
        }                    
        if(numCitiesWithCountryId<1){
            System.out.println("Countries Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("DELETE from country where countryId = "+selectedCountry.getCountryId()));
        }
        return true;
    }   
    
    public static boolean deleteCountry(Country country){
        try {
            System.out.println("Countries Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From country WHERE countryId="+country.getCountryId()));
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they couldn't delete the country!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public static boolean deleteCity(City city){
        try {
            System.out.println("Cities Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From city WHERE cityId="+city.getCityId()));
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they couldn't delete the country!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public static boolean deleteAddress(Address address){
        try {
            System.out.println("Addresses Deleted: "+StatementMaker.makeUpdateDeleteOrInsertStatement("Delete From address WHERE addressId="+address.getAddressId()));
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they couldn't delete the country!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public static User getUserById(int userId){
        ResultSet rsUser = StatementMaker.makeSelectStatement("SELECT * FROM user WHERE userId="+userId);       
        User user = null;
        try {
            while(rsUser.next()){
                user = new User(rsUser.getInt("userId"),rsUser.getString("userName"),rsUser.getString("password"),
                    rsUser.getByte("active"), rsUser.getTimestamp("createDate"),rsUser.getString("createdBy"),
                    rsUser.getTimestamp("lastUpdate"),rsUser.getString("lastUpdateBy"));
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have can't find that user!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return user;
    }
    
    public static int getUserIdByUserName(String userName){
        ResultSet rsUserId = StatementMaker.makeSelectStatement("SELECT userId FROM user WHERE userName = '"+userName+"'");       
        int userId = -1;
        try {
            while(rsUserId.next()){
                userId = rsUserId.getInt("userId");
            }            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have can't find a user with that name!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return userId;
    }
    public static Customer getCustomerById(int customerId){
        ResultSet rsCustomer = StatementMaker.makeSelectStatement("SELECT * FROM customer WHERE customerId="+customerId);       
        Customer customer = null;
        try {
            while(rsCustomer.next()){
                customer = new Customer(rsCustomer.getInt("customerId"),rsCustomer.getString("CustomerName"),rsCustomer.getInt("addressId"),
                    rsCustomer.getByte("active"), rsCustomer.getTimestamp("createDate"),rsCustomer.getString("createdBy"),
                    rsCustomer.getTimestamp("lastUpdate"),rsCustomer.getString("lastUpdateBy"));
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't find that customer!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return customer;
    }    
    
    public static String getCustomerNameById(int customerId) {
        ResultSet rsCustomerName = StatementMaker.makeSelectStatement("SELECT customerName FROM customer WHERE customerId="+customerId);
        String customerName = null;
        try {
            while(rsCustomerName.next()){
                customerName = rsCustomerName.getString("customerName");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't find that customer name!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return customerName;
    }    
    public static Country getCountryById(int countryId) {
        Country country = null;
        ResultSet rsCountry = StatementMaker.makeSelectStatement("SELECT * FROM country WHERE countryId = "+countryId+";");
        try {
            while(rsCountry.next()){
                country = new Country(rsCountry.getInt("countryId"),rsCountry.getString("country"),
                        rsCountry.getTimestamp("createDate"),rsCountry.getString("createdBy"),
                        rsCountry.getTimestamp("lastUpdate"),rsCountry.getString("lastUpdateBy"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't find that country!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return country;
    }
    
    public static City getCityById(int cityId) {
        City city = null;
        ResultSet rsCity = StatementMaker.makeSelectStatement("SELECT * FROM city WHERE cityId = "+cityId+";");
        try {
            while(rsCity.next()){
                city = new City(rsCity.getInt("cityId"),rsCity.getString("city"),rsCity.getInt("countryId"),
                        rsCity.getTimestamp("createDate"),rsCity.getString("createdBy"),
                        rsCity.getTimestamp("lastUpdate"),rsCity.getString("lastUpdateBy"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't find that city!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return city;
    }
    
    public static Address getAddressById(int addressId) {
        Address address=null;
        ResultSet rsAddress = StatementMaker.makeSelectStatement("SELECT * FROM address WHERE addressId = "+addressId+";");
        try {
            while(rsAddress.next()){
                address = new Address(rsAddress.getInt("addressId"),rsAddress.getString("address"),
                        rsAddress.getString("address2"),rsAddress.getInt("cityId"),rsAddress.getString("postalCode"),
                        rsAddress.getString("phone") ,rsAddress.getTimestamp("createDate"),rsAddress.getString("createdBy"),
                        rsAddress.getTimestamp("lastUpdate"),rsAddress.getString("lastUpdateBy"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they can't find that address!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return address;
    }
    

    public static ArrayList<Appointment> getAllAppointments() {
        allAppointments.clear();        
        ResultSet rsAppt = StatementMaker.makeSelectStatement("SELECT * FROM appointment");        
        try {
            while(rsAppt.next()){
                
                LocalDateTime start = rsAppt.getTimestamp("start").toLocalDateTime();
                ZonedDateTime zStart = start.atZone(ZoneId.of(systemDefault().toString()));
                zStart.withZoneSameInstant(ZoneId.of(systemDefault().toString()));
                start = zStart.toLocalDateTime();
                
                LocalDateTime end = rsAppt.getTimestamp("end").toLocalDateTime();
                ZonedDateTime zEnd = end.atZone(ZoneId.of(systemDefault().toString()));
                zEnd.withZoneSameInstant(ZoneId.of(systemDefault().toString()));
                end = zEnd.toLocalDateTime();
                
                allAppointments.add(new Appointment(rsAppt.getInt("appointmentId"),rsAppt.getInt("customerId"),
                        rsAppt.getInt("userId"),rsAppt.getString("title"),rsAppt.getString("description"),
                        rsAppt.getString("location"),rsAppt.getString("contact"),rsAppt.getString("type"),
                        rsAppt.getString("url"),start,end,rsAppt.getTimestamp("createDate"),rsAppt.getString("createdBy"),
                        rsAppt.getTimestamp("lastUpdate"),rsAppt.getString("lastUpdateBy")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the apapointment list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return allAppointments;
    }
 
    public static ArrayList<Appointment> getAppointmentsForUserByUserName(String userName) {
        ArrayList<Appointment> appointmentsForUser = new ArrayList<>();
        if(getUserIdByUserName(userName)==-1){
            JOptionPane.showMessageDialog(null, "The SQL Gods say there is no user by that name, try again!","Doh!!",JOptionPane.ERROR_MESSAGE);            
            return appointmentsForUser;
        }
        int userId = getUserIdByUserName(userName);
        try {
            ResultSet rsApptByUser = StatementMaker.makeSelectStatement("SELECT * FROM appointment WHERE userId = "+userId);    
            while(rsApptByUser.next()){
                
                LocalDateTime start = rsApptByUser.getTimestamp("start").toLocalDateTime();
                ZonedDateTime zStart = start.atZone(ZoneId.of(systemDefault().toString()));
                zStart.withZoneSameInstant(ZoneId.of(systemDefault().toString()));
                start = zStart.toLocalDateTime();
                
                LocalDateTime end = rsApptByUser.getTimestamp("end").toLocalDateTime();
                ZonedDateTime zEnd = end.atZone(ZoneId.of(systemDefault().toString()));
                zEnd.withZoneSameInstant(ZoneId.of(systemDefault().toString()));
                end = zEnd.toLocalDateTime();
                
                appointmentsForUser.add(new Appointment(rsApptByUser.getInt("appointmentId"),rsApptByUser.getInt("customerId"),
                        rsApptByUser.getInt("userId"),rsApptByUser.getString("title"),rsApptByUser.getString("description"),
                        rsApptByUser.getString("location"),rsApptByUser.getString("contact"),rsApptByUser.getString("type"),
                        rsApptByUser.getString("url"),start,end,rsApptByUser.getTimestamp("createDate"),rsApptByUser.getString("createdBy"),
                        rsApptByUser.getTimestamp("lastUpdate"),rsApptByUser.getString("lastUpdateBy")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the appointment list for that user name!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        
        return appointmentsForUser;
    }
    
    public static ArrayList<Integer> getAllAppointmentIds() {
        ArrayList<Integer> apptIdList = new ArrayList<>();
        ResultSet rsApptIds = StatementMaker.makeSelectStatement("SELECT appointmentId FROM appointment");
        try {
            while(rsApptIds.next()){
                apptIdList.add(rsApptIds.getInt("appointmentId"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the user Id list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return apptIdList;
    }
    
    public static HashMap<Integer, Integer> getAppointmentsPerMonth(){
        ResultSet rsApptStart = StatementMaker.makeSelectStatement("SELECT start FROM appointment");
        HashMap<Integer, Integer> monthApptTotals = new HashMap<>();
        for(int i = 1;i<=12;i++){
            monthApptTotals.put(i,0);
        }
        try {
            while(rsApptStart.next()){
            int monthNumber = rsApptStart.getTimestamp("start").toLocalDateTime().getMonthValue();
            int totalApptToNumberToIncrement;
            switch(monthNumber){
                case 1:
                    totalApptToNumberToIncrement = monthApptTotals.get(1);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(1,totalApptToNumberToIncrement);
                    break;
                case 2:
                    totalApptToNumberToIncrement = monthApptTotals.get(2);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(2,totalApptToNumberToIncrement);                    
                    break;
                case 3:
                    totalApptToNumberToIncrement = monthApptTotals.get(3);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(3,totalApptToNumberToIncrement);                    
                    break;
                case 4:
                    totalApptToNumberToIncrement = monthApptTotals.get(4);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(4,totalApptToNumberToIncrement);                   
                    break;
                case 5:
                    totalApptToNumberToIncrement = monthApptTotals.get(5);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(5,totalApptToNumberToIncrement);                   
                    break;
                case 6:
                    totalApptToNumberToIncrement = monthApptTotals.get(6);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(6,totalApptToNumberToIncrement);                    
                    break;
                case 7:
                    totalApptToNumberToIncrement = monthApptTotals.get(7);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(7,totalApptToNumberToIncrement);                    
                    break;
                case 8:
                    totalApptToNumberToIncrement = monthApptTotals.get(8);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(8,totalApptToNumberToIncrement);                   
                    break;
                case 9:
                    totalApptToNumberToIncrement = monthApptTotals.get(9);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(9,totalApptToNumberToIncrement);                    
                    break;
                case 10:
                    totalApptToNumberToIncrement = monthApptTotals.get(10);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(10,totalApptToNumberToIncrement);                   
                    break;
                case 11:
                    totalApptToNumberToIncrement = monthApptTotals.get(11);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(11,totalApptToNumberToIncrement);                    
                    break;
                case 12:
                    totalApptToNumberToIncrement = monthApptTotals.get(12);
                    totalApptToNumberToIncrement++;
                    monthApptTotals.put(12,totalApptToNumberToIncrement);                    
                    break;
        }
            
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with retrieving appointments per month!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }

        return monthApptTotals;
    }
    
    public static ArrayList<String> getApptsPerUserList() {
        ArrayList<String> apptsPerUserList = new ArrayList<>();
        TreeMap<String, Integer> userApptTotals = new TreeMap<>();
        ArrayList<User> usersList = getAllUsers();
        ArrayList<Appointment> apptList = getAllAppointments();
        for(User user:usersList){
            userApptTotals.put(user.getUserName(),0);
            for(Appointment appt:apptList){
                if(user.getUserId()==appt.getUserId()){
                    int apptTotal = userApptTotals.get(user.getUserName());
                    userApptTotals.put(user.getUserName(),++apptTotal);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : userApptTotals.entrySet()) {
            apptsPerUserList.add(entry.getKey() + ": " + entry.getValue());
        }        
        return apptsPerUserList;
    }
    public static ArrayList<Customer> getAllCustomers() {
        allCustomers.clear();
        ResultSet rsCustomer = StatementMaker.makeSelectStatement("SELECT * FROM customer");
        try {
            while(rsCustomer.next()){
                allCustomers.add(new Customer(rsCustomer.getInt("customerId"),rsCustomer.getString("CustomerName"),rsCustomer.getInt("addressId"),
                    rsCustomer.getByte("active"), rsCustomer.getTimestamp("createDate"),rsCustomer.getString("createdBy"),
                    rsCustomer.getTimestamp("lastUpdate"),rsCustomer.getString("lastUpdateBy")));        
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the customer list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return allCustomers;
    }
    
    public static ArrayList<Integer> getAllCustomerIds() {
        ArrayList<Integer> customerIdList = new ArrayList<>();
        ResultSet rsCustomerIds = StatementMaker.makeSelectStatement("SELECT customerId FROM customer");
        try {
            while(rsCustomerIds.next()){
                customerIdList.add(rsCustomerIds.getInt("customerId"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the customer Id list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return customerIdList;
    }
    
        
    public static ArrayList<Integer> getAllUserIds() {
        ArrayList<Integer> userIdList = new ArrayList<>();
        ResultSet rsUserIds = StatementMaker.makeSelectStatement("SELECT userId FROM user");
        try {
            while(rsUserIds.next()){
                userIdList.add(rsUserIds.getInt("userId"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the user Id list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return userIdList;
    }
    
    public static ArrayList<String> getAllUserNames() {
        ArrayList<String> userNameList = new ArrayList<>();
        ResultSet rsUserNames = StatementMaker.makeSelectStatement("SELECT userName FROM user");
        try {
            while(rsUserNames.next()){
                userNameList.add(rsUserNames.getString("userName"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the user Id list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return userNameList;
    }
    
    public static ArrayList<User> getAllUsers() {
        allUsers.clear();
        ResultSet rsUser = StatementMaker.makeSelectStatement("SELECT * FROM user");       
        try {
            while(rsUser.next()){
                allUsers.add(new User(rsUser.getInt("userId"),rsUser.getString("userName"),rsUser.getString("password"),
                    rsUser.getByte("active"), rsUser.getTimestamp("createDate"),rsUser.getString("createdBy"),
                    rsUser.getTimestamp("lastUpdate"),rsUser.getString("lastUpdateBy")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the user list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }        
        return allUsers;
    }
    
    public static ArrayList<Address> getAllAddresses() {
        allAddresses.clear();
        ResultSet rsAddress = StatementMaker.makeSelectStatement("SELECT * FROM address");       
        try {
            while(rsAddress.next()){
                allAddresses.add(new Address(rsAddress.getInt("addressId"),rsAddress.getString("address"),
                        rsAddress.getString("address2"),rsAddress.getInt("cityId"),rsAddress.getString("postalCode"),
                        rsAddress.getString("phone") ,rsAddress.getTimestamp("createDate"),rsAddress.getString("createdBy"),
                        rsAddress.getTimestamp("lastUpdate"),rsAddress.getString("lastUpdateBy")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the address list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        } 
        return allAddresses;
    }
    
    public static ArrayList<Integer> getAllAddressIds() {
        ArrayList<Integer> addressIdList = new ArrayList<>();
        ResultSet rsAddressIds = StatementMaker.makeSelectStatement("SELECT addressId FROM address");
        try {
            while(rsAddressIds.next()){
                addressIdList.add(rsAddressIds.getInt("addressId"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the address Id list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return addressIdList;
    }
    
    public static ArrayList<String> getAllAddressesStrings() {
        ArrayList<String> addressStringList = new ArrayList<>();
        ResultSet rsAddresses = StatementMaker.makeSelectStatement("SELECT address,address2 FROM address");       
        try {
            while(rsAddresses.next()){
                addressStringList.add(rsAddresses.getString("address") + " " + rsAddresses.getString("address2"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the address string list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        } 
        return addressStringList;
    }
    
    public static ArrayList<City> getAllCities() {
        allCities.clear();
        ResultSet rsCity = StatementMaker.makeSelectStatement("SELECT * FROM city");       
        try {
            while(rsCity.next()){
                allCities.add(new City(rsCity.getInt("cityId"),rsCity.getString("city"),rsCity.getInt("countryId"),
                        rsCity.getTimestamp("createDate"),rsCity.getString("createdBy"),
                        rsCity.getTimestamp("lastUpdate"),rsCity.getString("lastUpdateBy")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the city list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }         
        return allCities;
    }
    
    public static ArrayList<String> getAllCitiesNames() {
        ArrayList<String> cityNameList = new ArrayList<>();
        ResultSet rsCities = StatementMaker.makeSelectStatement("SELECT city FROM city");       
        try {
            while(rsCities.next()){
                cityNameList.add(rsCities.getString("city"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the city name list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }         
        return cityNameList;
    }
    
    public static ArrayList<Integer> getAllCityIds() {
        ArrayList<Integer> cityIdList = new ArrayList<>();
        ResultSet rsCityIds = StatementMaker.makeSelectStatement("SELECT cityId FROM city");
        try {
            while(rsCityIds.next()){
                cityIdList.add(rsCityIds.getInt("cityId"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the city Id list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return cityIdList;
    }
    
    public static ArrayList<Country> getAllCountries() {
        allCountries.clear();
        ResultSet rsCountry = StatementMaker.makeSelectStatement("SELECT * FROM country");       
        try {
            while(rsCountry.next()){
                allCountries.add(new Country(rsCountry.getInt("countryId"),rsCountry.getString("country"),
                        rsCountry.getTimestamp("createDate"),rsCountry.getString("createdBy"),
                        rsCountry.getTimestamp("lastUpdate"),rsCountry.getString("lastUpdateBy")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the country list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }        
        return allCountries;
    }
    
    public static ArrayList<String> getAllCountriesNames() {
        ArrayList<String> countryNameList = new ArrayList<>();
        ResultSet rsCountries = StatementMaker.makeSelectStatement("SELECT country FROM country");       
        try {
            while(rsCountries.next()){
                countryNameList.add(rsCountries.getString("country"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the city name list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }        
        return countryNameList;
    }

    public static ArrayList<Integer> getAllCountryIds() {
        ArrayList<Integer> countryIdList = new ArrayList<>();
        ResultSet rsCountryIds = StatementMaker.makeSelectStatement("SELECT countryId FROM country");
        try {
            while(rsCountryIds.next()){
                countryIdList.add(rsCountryIds.getInt("countryId"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The SQL Gods say they have an issue with the country Id list!--> "+ex.getMessage(),"Doh!!",JOptionPane.ERROR_MESSAGE);
        }
        return countryIdList;
    }
    
    public static User getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }

    public static void setCurrentLoggedInUser(User currentLoggedInUser) {
        DatabaseObjects.currentLoggedInUser = currentLoggedInUser;
    }

    public static ArrayList<String> getTypeSelectorList() {
        return typeSelectorList;
    }

    public static ArrayList<LocalTime> getApptStartTimeList() {
        return apptStartTimeList;
    }

    public static ArrayList<LocalTime> getApptEndTimeList() {
        return apptEndTimeList;
    }

    public static DateTimeFormatter getReadableDateFormatter() {
        return readableDateFormatter;
    }

    public static SimpleDateFormat getReadableDateFormat() {
        return readableDateFormat;
    }
    
    public static String getSystemCountry() {
        return systemCountry;
    }

    public static void setSystemCountry(String systemCountry) {
        DatabaseObjects.systemCountry = systemCountry;
    }

    public static String getSystemLanguage() {
        return systemLanguage;
    }

    public static void setSystemLanguage(String systemLanguage) {
        DatabaseObjects.systemLanguage = systemLanguage;
    }    

}
