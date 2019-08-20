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
public class User {
    private static int userIdCounter = 1;
    private final int userId;
    private String userName;
    private String password;
    private byte active;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private static Set<Integer> usedUserIds = new TreeSet<>();
    
    //only used for creating new users//
    public User(String userName, String password, byte active, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.userId = userIdCounter++;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        usedUserIds.add(userId);
    }
    
    //only used to load existing users//
    public User(int userId, String userName, String password, byte active, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;       
    }
    
    public static void resetIdCounter(){
        usedUserIds.clear();
        usedUserIds.addAll(DatabaseObjects.getAllUserIds());
        userIdCounter = 1;
        usedUserIds.stream().filter((userId) -> (userId>userIdCounter)).forEach((userId) -> {
            userIdCounter=userId;
        });
        userIdCounter++;
    }

    public static ArrayList<Integer> getUsedUserIds() {
        return new ArrayList<>(usedUserIds);
    }
        
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getActive() {
        return active;
    }
    
    public String getTranslatedActive() {
        if(active == 1){
            return "Active";
        }else{
            return "Inactive";
        }
    }
    public void setActive(byte active) {
        this.active = active;
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
    
    @Override
    public String toString(){
        return ("("+getUserId()+",'"+getUserName()+"','"+getPassword()+"',\n"+getActive()+ ","+getCreateDate()+",'"+getCreatedBy()+"',\n"+getLastUpdate()+",'"+getLastUpdatedBy()+"')");
    }

}
