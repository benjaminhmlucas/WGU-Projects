/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1.inventory;

import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author wonder monkey
 */
public class ModifyPartWindow {

    public ModifyPartWindow(Stage primaryStage, Part selectedPart){
        
        Inventory inventory = Inventory.getInstance();
        Boolean sourceInHouse;
        BorderPane root1 = new BorderPane();
//TOP
        HBox topTitleAndRadioButtons = new HBox();
        topTitleAndRadioButtons.setId("topRowSecondaryScreens");
        Label windowTitle = new Label("Add Parts");
        windowTitle.setId("secondaryMainTitle");
        RadioButton inHouse= new RadioButton("In House");
        RadioButton outSourced= new RadioButton("Out Sourced");
        ToggleGroup sourceGroup = new ToggleGroup();
        inHouse.setToggleGroup(sourceGroup);
        outSourced.setToggleGroup(sourceGroup);
        inHouse.setSelected(true);
        root1.setTop(topTitleAndRadioButtons);
        topTitleAndRadioButtons.getChildren().addAll(windowTitle,inHouse,outSourced);

//MIDDLE
        GridPane grid = new GridPane();
        grid.setId("centerGrid");
        root1.setCenter(grid);

        Label idLab = new Label("ID");
        Label nameLab = new Label("Name");
        Label invLab = new Label("Inv");
        Label priceLab = new Label("Price/Cost");
        Label maxLab = new Label("Max");
        Label minLab = new Label("Min");
        Label companyLab = new Label("Company");
        Label machineIdLab = new Label("Machine ID");

        //set passed in part values to modify in text fields
        TextField idText = new TextField(Integer.toString(selectedPart.getPartId()));
        idText.setDisable(true);
        TextField nameText = new TextField(selectedPart.getName());            
        TextField invText = new TextField(Integer.toString(selectedPart.getInStock())); 
        TextField priceText = new TextField(Double.toString(selectedPart.getPrice()));
        TextField maxText = new TextField(Integer.toString(selectedPart.getMax()));
        TextField minText = new TextField(Integer.toString(selectedPart.getMin()));        
        TextField MachineIdText = new TextField("Mach ID");
        TextField companyText = new TextField("Comp Nm");

        grid.add(idLab,0,0);
        grid.add(idText,1,0);
        grid.add(nameLab,0,1);
        grid.add(nameText,1,1);
        grid.add(invLab,0,2);
        grid.add(invText,1,2);
        grid.add(priceLab,0,3);
        grid.add(priceText,1,3);
        grid.add(maxLab,0,4);
        grid.add(maxText,1,4);
        grid.add(minLab,2,4);
        grid.add(minText,3,4);
        
        if(InHouse.class.isInstance(selectedPart)){
            InHouse selectedInHousePart = (InHouse)selectedPart;
            MachineIdText.setText(Integer.toString(selectedInHousePart.getMachNum()));
            sourceGroup.selectToggle(inHouse); 
            grid.add(machineIdLab,0,5);
            grid.add(MachineIdText,1,5);
            sourceInHouse = true;
        } else {
            OutSourced selectedOutSourcedPart = (OutSourced)selectedPart;
            companyText.setText(selectedOutSourcedPart.getCompName());
            sourceGroup.selectToggle(outSourced);
            grid.add(companyLab,0,5);
            grid.add(companyText,1,5);
            sourceInHouse = false;
        }

//TOGGLE ACTIONS for In House/Out Sourced
        sourceGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) -> {
            RadioButton rb = (RadioButton)sourceGroup.getSelectedToggle();
            if(rb != null){
                if(rb.getText().equals("Out Sourced")){
                    grid.getChildren().remove(machineIdLab);
                    grid.getChildren().remove(MachineIdText);
                    grid.add(companyLab,0,5);
                    grid.add(companyText,1,5);
                } else {
                    grid.getChildren().remove(companyLab);
                    grid.getChildren().remove(companyText);
                    grid.add(machineIdLab,0,5);
                    grid.add(MachineIdText,1,5);
                }
            }
        });

//BOTTOM
        HBox bottomButtons = new HBox();
        bottomButtons.setId("bottomButtonsSecondary");
        Button saveBtn = new Button("        Save        ");
        Button cancelBtn = new Button("       Cancel       ");
        bottomButtons.getChildren().addAll(saveBtn,cancelBtn);
        root1.setBottom(bottomButtons);

        saveBtn.setOnAction((ActionEvent e) -> {
            //check for format exceptions and validate input
            ArrayList<String> validationErrorsList = new ArrayList<>();
            //part name text field check
            if(nameText.getText().equals("")){
                validationErrorsList.add("\nName field cannot be empty");
            }
            if(nameText.getText().equals("Part Name")){
                validationErrorsList.add("\nYou must change name field from 'Part Name'");
            }
            //inventory text field check & Min/Max text field check)
            Integer invValue = -1;
            Double prcValue = -1.00;
            Integer minValue = -1;
            Integer maxValue = -1;
            Integer machValue = -1;                

            try{
                invValue = Integer.parseInt(invText.getText());
                try{
                    minValue = Integer.parseInt(minText.getText());
                    if(invValue < minValue){
                        validationErrorsList.add("\nInv field must be above " + (minValue - 1));
                    }
                }catch(Exception ex){
                    validationErrorsList.add("\nMin must be a number");
                }
                try{
                    maxValue = Integer.parseInt(maxText.getText());
                    if(invValue > maxValue){
                        validationErrorsList.add("\nInv field must be below " + (maxValue + 1));
                    }
                }catch(Exception ex){
                    validationErrorsList.add("\nMax must be a number");
                }
                if(maxValue<minValue){
                    validationErrorsList.add("\nMax value cannot be lower than min value");
                }                
                if(invText.getText().equals("")){
                    validationErrorsList.add("\nInv field cannot be empty");
                }
                if(invText.getText().equals("Inv")){
                    validationErrorsList.add("\nYou must change Inv field from 'Inv'");
                }                    
            }catch(Exception ex){
                validationErrorsList.add("\nInventory must be a number");
            }
            //PriceField Check
            try{
                prcValue = Double.parseDouble(priceText.getText());
                if(prcValue < 0){
                    validationErrorsList.add("\nPrice field cannot be negative");
                }                    
            }catch(Exception ex){
                validationErrorsList.add("\nPrice must be a number");                    
            }
            //if inhouse check machine id
            if(inHouse.isSelected()){
                try{
                    machValue = Integer.parseInt(MachineIdText.getText());
                    if(machValue < 0){
                    validationErrorsList.add("\nMachine ID cannot be negative");
                }
                }catch(Exception ex){
                    validationErrorsList.add("\nMachine ID must be a number");
                }
                //else check company text field
            } else{
                if(companyText.getText().equals("")){
                    validationErrorsList.add("\nCompany field cannot be empty");
                }
                if(companyText.getText().equals("Comp Nm")){
                    validationErrorsList.add("\nCompany name cannot be 'Comp Nm'");
                }                    
            }
            //if error list is not empty give user error list message otherwise check radio buttons and create new part, then add to part list
            if(validationErrorsList.size()>0){
                JOptionPane.showMessageDialog(null, "These fields need to be corrected before new part can be saved:"+validationErrorsList.toString().replace("[", "").replace("]", ""), "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                //else make new updated part and switch with old part.
                if(inHouse.isSelected()){                    
                    InHouse modPart = new InHouse(selectedPart.getPartId(),nameText.getText(),invValue,prcValue,minValue,maxValue,machValue);
                    inventory.updatePart(selectedPart.getPartId(),modPart);
                    inventory.resetPartList();
                    inventory.resetProductList();
                    saveBtn.getScene().getWindow().hide();
                    primaryStage.show(); 
                } else {                                       
                    OutSourced modPart = new OutSourced(selectedPart.getPartId(),nameText.getText(),invValue,prcValue,minValue,maxValue,companyText.getText());
                    inventory.updatePart(selectedPart.getPartId(),modPart);
                    inventory.resetPartList();
                    inventory.resetProductList();
                    saveBtn.getScene().getWindow().hide();
                    primaryStage.show();
                }
            }
        });

        cancelBtn.setOnAction((ActionEvent e) -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit Modify Part Window?", "CAUTION!", JOptionPane.OK_CANCEL_OPTION);            
            if(choice == 0){                    
                inventory.resetProductList();
                inventory.resetPartList();                
                cancelBtn.getScene().getWindow().hide();
                primaryStage.show();
            }  
        });


        Scene scene = new Scene(root1, 550, 400);
        scene.getStylesheets().add("software1/inventory/Styles.css");
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Add Part");
        newWindow.setScene(scene);
        // Set position of window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);
        newWindow.show();
    
    }
}
