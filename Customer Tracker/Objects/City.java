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
public class City {
    private static int cityIdCounter = 1;
    private int cityId;
    private String city;
    private int countryId;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private static Set<Integer> usedCityIds = new TreeSet<>();

    //only used for creating new cities//
    public City(String city, int countryId, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.cityId = cityIdCounter++;
        this.city = city;
        this.countryId = countryId;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        usedCityIds.add(cityId);
    }
    
    //only used to load existing addresses//
    public City(int citId, String city, int countryId, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.cityId = citId;
        this.city = city;
        this.countryId = countryId;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    public static void resetIdCounter(){
        cityIdCounter = 1;
    }
        
    public static void checkNextId(){
        usedCityIds.clear();
        usedCityIds.addAll(DatabaseObjects.getAllCityIds());
        cityIdCounter = 0;
        usedCityIds.stream().filter((cityId) -> (cityId>cityIdCounter)).forEach((cityId) -> {
            cityIdCounter=cityId;
        });
        cityIdCounter++;
    }
    
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
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
