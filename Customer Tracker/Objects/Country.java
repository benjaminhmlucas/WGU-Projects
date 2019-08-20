/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import CustomerDatabase.DatabaseObjects;
import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;


/**
 *
 * @author lucasb
 */
public class Country {
    private static int countryIdCounter = 1;
    private int countryId;
    private String country;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private static Set<Integer> usedCountryIds = new TreeSet<>();

    //only used for creating new countries//
    public Country(String country, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.countryId = countryIdCounter++;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        usedCountryIds.add(countryId);
        
    }
    //only used to load existing countries//
    public Country(int countryId, String country, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.countryId = countryId;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    public static void resetIdCounter(){
        countryIdCounter = 1;
    }
    
    public static void checkNextId(){
        usedCountryIds.clear();
        usedCountryIds.addAll(DatabaseObjects.getAllCountryIds());
        countryIdCounter = 0;
        usedCountryIds.stream().filter((countryId) -> (countryId>countryIdCounter)).forEach((customerId) -> {
            countryIdCounter=customerId;
        });
        countryIdCounter++;
    }
    
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
    
}
