/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import CustomerDatabase.DatabaseObjects;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author lucasb
 */
public class Customer {
    private static int customerIdCounter = 1;
    private int customerId;
    private String customerName;
    private int addressId;
    private byte active;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private static Set<Integer> usedCustomerIds = new TreeSet<>();
    
    //only used for creating new customers//
    public Customer(String customerName, int addressId, byte active, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.customerId = customerIdCounter++;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdateBy;
        usedCustomerIds.add(addressId);
    }
    
    //only used to load existing customers//
    public Customer(int customerId, String customerName, int addressId, byte active, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdateBy;
    }
    
    public static void resetIdCounter(){
        customerIdCounter = 1;
    }
    public static void checkNextId(){
        usedCustomerIds.clear();
        usedCustomerIds.addAll(DatabaseObjects.getAllCustomerIds());
        customerIdCounter = 0;
        usedCustomerIds.stream().filter((customerId) -> (customerId>customerIdCounter)).forEach((customerId) -> {
            customerIdCounter=customerId;
        });
        customerIdCounter++;
    }
    
    public static ArrayList<Integer> getUsedAppointmentIds() {
        usedCustomerIds.clear();
        usedCustomerIds.addAll(DatabaseObjects.getAllCustomerIds());
        return new ArrayList<>(usedCustomerIds);
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public byte getActive() {
        return active;
    }

    public void setActive(byte active) {
        this.active = active;
    }
    
    public String getTranslatedActive() {
        if(active == 1){
            return "Active";
        }else{
            return "Inactive";
        }
    }
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }   
    
    public String getFormattedCreateDate() {
        return DatabaseObjects.getReadableDateFormat().format(createDate);
    }

    public String getFormattedLastUpdate() {
        return DatabaseObjects.getReadableDateFormat().format(lastUpdate);
    }
}
