/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import CustomerDatabase.DatabaseObjects;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import javafx.scene.text.Text;

/**
 *
 * @author lucasb
 */
public class Appointment {
    private static int apptIdCounter = 1;
    private int appointmentId;
    private int customerId;
    private int userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private LocalDateTime start;
    private LocalDateTime end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private static Set<Integer> usedAppointmentIds = new TreeSet<>();
    
    //only used for creating new appointments//
    public Appointment(int customerId, int userId, String title, String description, String location, String contact, String type, String url, LocalDateTime start, LocalDateTime end, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.appointmentId = apptIdCounter++;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        usedAppointmentIds.add(userId);
    }

    //only used to load existing appointments//
    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, LocalDateTime start, LocalDateTime end, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    public static void resetIdCounter(){
        apptIdCounter = 1;
    }
    public static void checkNextId(){
        usedAppointmentIds.clear();
        usedAppointmentIds.addAll(DatabaseObjects.getAllAppointmentIds());
        apptIdCounter = 0;
        usedAppointmentIds.stream().filter((appointmentId) -> (appointmentId>apptIdCounter)).forEach((appointmentId) -> {
            apptIdCounter=appointmentId;
        });
        apptIdCounter++;
    }
    
    public static ArrayList<Integer> getUsedAppointmentIds() {
        usedAppointmentIds.clear();
        usedAppointmentIds.addAll(DatabaseObjects.getAllAppointmentIds());    
        return new ArrayList<>(usedAppointmentIds);
    }
    
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public Text getContactText(){
        Text contactText = new Text(this.contact);
        contactText.setUnderline(true);
        contactText.setId("contactText");
        return contactText;
    }
    public Text getUserName(){
        Text userName = new Text(DatabaseObjects.getUserById(this.userId).getUserName());
        userName.setUnderline(true);
        userName.setId("userText");
        return userName;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
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

    public String getFormattedStart() {
        return start.format(DatabaseObjects.getReadableDateFormatter());
    }

    public String getFormattedEnd() {
        return end.format(DatabaseObjects.getReadableDateFormatter());
    }

    public String getFormattedCreateDate() {
        return DatabaseObjects.getReadableDateFormat().format(createDate);
    }

    public String getFormattedLastUpdate() {
        return DatabaseObjects.getReadableDateFormat().format(lastUpdate);
    }
    
    @Override
    public String toString(){
        return ("Customer: "+this.getContact()+"\nStart Time: "+this.getFormattedStart()+"\nEndt Time:"+this.getFormattedEnd()+"\nLocation: "+this.getLocation());
    }
}
