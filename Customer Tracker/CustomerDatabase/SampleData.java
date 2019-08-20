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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Small-Dell
 */
public class SampleData {
    
    private static ArrayList<User> userList = new ArrayList<>();
    private static ArrayList<Appointment> apptList = new ArrayList<>();
    private static ArrayList<Customer> customerList = new ArrayList<>();
    private static ArrayList<Address> addressList = new ArrayList<>();
    private static ArrayList<City> cityList = new ArrayList<>();
    private static ArrayList<Country> countryList = new ArrayList<>();
   
    private SampleData(){

    }
   
    public static boolean createSampleData(int numUsers){
        DatabaseObjects.deleteAllDatabaseObjects();
        userList.clear();
        userList = createUsers(numUsers);
        apptList.clear();
        apptList = createAppointments(numUsers*3,numUsers);
        customerList.clear();
        customerList = createCustomers(numUsers*3);
        addressList.clear();
        addressList = createAddresses(numUsers*3);
        cityList.clear();
        cityList = createCities(numUsers);
        countryList.clear();
        countryList = createCountries(numUsers);
        return true;
   }
   
    
    public static ArrayList<User> createUsers(int numUsers){   
        ArrayList<User> moreUsers = new ArrayList<>();
        if(DatabaseObjects.getUserById(1)==null){
            moreUsers.add(new User(1,"test","test", (byte)1, Timestamp.valueOf(LocalDateTime.now()), 
                System.getProperty("user.name"),Timestamp.valueOf(LocalDateTime.now()), 
                System.getProperty("user.name")));  
        }
        for(int i = 1;i<numUsers;i++){
            moreUsers.add(new User("test"+(i+1),"test"+(i+1), (byte)1, 
                Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"),
                Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name")));
        }
        return moreUsers;
    }
    
    public static ArrayList<Appointment> createAppointments(int numAppointments,int numUsers){
        ArrayList<Appointment> moreAppointments = new ArrayList<>();
        int userId = 1;
        ArrayList<String> descriptionList = new ArrayList<>(Arrays.asList("Informational","Fact Finding","Sales"));
        ArrayList<String> typeList = DatabaseObjects.getTypeSelectorList();
        ArrayList<LocalTime> startTimeList = DatabaseObjects.getApptStartTimeList();
        for(int i = 0;i<numAppointments;i++){
            if(userId==numUsers+1){userId=1;}
            LocalDateTime startTime = LocalDateTime.of(LocalDate.now().plusWeeks(i), startTimeList.get(i%8));
            LocalDateTime endTime = startTime.plusHours(1);
            moreAppointments.add(
                    new Appointment(
                        i+1, 
                        userId++, 
                        "Appointment"+(i+1), 
                        descriptionList.get(i%3),
                        "Location"+i,
                        "Customer"+(i+1), 
                        typeList.get(i%3), 
                        "www.site"+i+".com", 
                        startTime,
                        endTime,
                        Timestamp.valueOf(LocalDateTime.now()),
                        System.getProperty("user.name"),
                        Timestamp.valueOf(LocalDateTime.now()),
                        System.getProperty("user.name")));
        }
        return moreAppointments;
    }
    
    public static ArrayList<Customer> createCustomers(int numCustomers){
        ArrayList<Customer> moreCustomers = new ArrayList<>();
        for(int i = 0;i<numCustomers;i++){
            moreCustomers.add(new Customer (("Customer"+(i+1)),(i+1), (byte)1,
                    Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"),
                    Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name")));
        }
        return moreCustomers;    
    }
    
    public static ArrayList<Address> createAddresses(int numAddresses){
        ArrayList<Address> moreAddresses = new ArrayList<>();
        int addressNumber = 123;
        int aptNumber = 1;
            for(int i = 0;i<numAddresses;i++){
                moreAddresses.add(new Address((addressNumber++)+" Address st", "apt No. "+ (aptNumber++),
                        (i%3)+1,"29940", "888-444-555", Timestamp.valueOf(LocalDateTime.now()), 
                        System.getProperty("user.name"),Timestamp.valueOf(LocalDateTime.now()), 
                        System.getProperty("user.name")));
        }
        return moreAddresses;
    }
    
    public static ArrayList<City> createCities(int numCities){
        ArrayList<City> moreCities = new ArrayList<>();
            for(int i = 0;i<numCities;i++){
                moreCities.add(new City("City" + (i+1),i+1, 
                        Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"),
                        Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name")));
        }
        return moreCities;
    }
    public static ArrayList<Country> createCountries(int numCountries){
        ArrayList<Country> moreCountries = new ArrayList<>();
        for(int i = 0;i<numCountries;i++){
        moreCountries.add(new Country("Country" + (i+1), 
                Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name"),
                Timestamp.valueOf(LocalDateTime.now()), System.getProperty("user.name")));
        }
        return moreCountries;
    }

    
    //used to add sample data to database
    public static void addTestData(){
        int userRowsAdded = 0;
        int apptRowsAdded = 0;
        int customerRowsAdded = 0;
        int addressRowsAdded = 0;
        int cityRowsAdded = 0;
        int countryRowsAdded = 0;
        
        //Clear out all tables
        DatabaseObjects.deleteAllDatabaseObjects();
        
        for(User user:userList){
            System.out.println(user.toString());
            int rowsAdded = DatabaseObjects.insertUser(user);
            userRowsAdded = userRowsAdded + rowsAdded;            
        }
        
        for(Country country:countryList){
            System.out.println(country.toString());
            int rowsAdded = DatabaseObjects.insertCountry(country);
            countryRowsAdded = countryRowsAdded + rowsAdded;            
        }
        
        for(City city:cityList){
            System.out.println(city.toString());
            int rowsAdded = DatabaseObjects.insertCity(city);
            cityRowsAdded = cityRowsAdded + rowsAdded;            
        }
        
        for(Address addr:addressList){
            System.out.println(addr.toString());
            int rowsAdded = DatabaseObjects.insertAddress(addr);
            addressRowsAdded = addressRowsAdded + rowsAdded;            
        }
        
        for(Customer customer: customerList){
            System.out.println(customer.toString());
            int rowsAdded = DatabaseObjects.insertCustomer(customer);
            customerRowsAdded = customerRowsAdded + rowsAdded;
        }
        
        for(Appointment appt:apptList){
            System.out.println(appt.toString());
            int rowsAdded = DatabaseObjects.insertAppointment(appt);
            apptRowsAdded = apptRowsAdded + rowsAdded;            
        }
        
        System.out.println("User Rows Added:" + userRowsAdded);
        System.out.println("Appointment Rows Added:" + apptRowsAdded);
        System.out.println("Customer Rows Added:" + customerRowsAdded);
        System.out.println("Address Rows Added:" + addressRowsAdded);
        System.out.println("City Rows Added:" + cityRowsAdded);
        System.out.println("Country Rows Added:" + countryRowsAdded);
    }

    public static ArrayList<User> getUserList() {
        return userList;
    }

    public static ArrayList<Appointment> getApptList() {
        return apptList;
    }

    public static ArrayList<Customer> getCustomerList() {
        return customerList;
    }

    public static ArrayList<Address> getAddressList() {
        return addressList;
    }

    public static ArrayList<City> getCityList() {
        return cityList;
    }

    public static ArrayList<Country> getCountryList() {
        return countryList;
    }
    
}
