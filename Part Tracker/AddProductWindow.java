/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1.inventory;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author wonder monkey
 */
public class AddProductWindow {    

    public AddProductWindow(Stage primaryStage){
        
        Inventory inventory = Inventory.getInstance();
        inventory.resetPartList();
        ObservableList<Part> newPartsList = FXCollections.observableArrayList();
        HBox root = new HBox();
        VBox rootLeft = new VBox();//for data entry
        rootLeft.setId("leftRootVBox");
        VBox rootRight = new VBox();//for search list and part list
        root.getChildren().addAll(rootLeft,rootRight);
        
        //Left Side Data Entry***************************************************
        Label title = new Label("Add Product");
        title.setId("secondaryMainTitle");
        GridPane dataEntry = new GridPane();
        dataEntry.setId("dataEntryGrid");
        rootLeft.getChildren().addAll(title,dataEntry);
        
        Label idLab = new Label("ID");
        Label nameLab = new Label("Name");
        Label invLab = new Label("Inv");
        Label priceLab = new Label("Price");
        Label maxLab = new Label("Max");
        Label minLab = new Label("Min");
        
        TextField idText = new TextField("Auto Gen - Disabled");
        idText.setDisable(true);        
        TextField nameText = new TextField("Product Name");
        TextField invText = new TextField("Inv");
        TextField priceText = new TextField("Price");
        TextField maxText = new TextField("Max");        
        TextField minText = new TextField("Min");
        
        dataEntry.add(idLab,0,0);
        dataEntry.add(idText,1,0,2,1);
        dataEntry.add(nameLab,0,1);
        dataEntry.add(nameText,1,1,2,1);
        dataEntry.add(invLab,0,2);
        dataEntry.add(invText,1,2);
        dataEntry.add(priceLab,0,3);
        dataEntry.add(priceText,1,3);
        dataEntry.add(maxLab,0,4);
        dataEntry.add(maxText,1,4);
        dataEntry.add(minLab,2,4);
        dataEntry.add(minText,3,4);
        
        //Right Side Tables******************************************************
        HBox searchBar = new HBox();
        searchBar.setId("searchBar");
        Button searchBtn = new Button("     Search     ");
        TextField searchText = new TextField();
        searchBar.getChildren().addAll(searchBtn,searchText);
                
        TableView<Part> tViewPartSearch = new TableView<>(Inventory.filterablePartsList);
        
        TableColumn<Part, String> pId = new TableColumn<>("Part ID                  ");
        pId.setCellValueFactory(new PropertyValueFactory<>("partId"));
        tViewPartSearch.getColumns().add(pId);
        
        TableColumn<Part, String> pName = new TableColumn<>("Part Name              ");
        pName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tViewPartSearch.getColumns().add(pName);
        
        TableColumn<Part, String> pInv = new TableColumn<>("Inventory Level         ");
        pInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        tViewPartSearch.getColumns().add(pInv);
        
        TableColumn<Part, String> pPrice = new TableColumn<>("Price/Cost per Unit   ");
        pPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tViewPartSearch.getColumns().add(pPrice); 
               
        tViewPartSearch.setMinHeight(100);
        tViewPartSearch.setMinWidth(600);
        
        HBox addBtnRow = new HBox();
        addBtnRow.setId("addBtnRow");
        Button addBtn = new Button("      Add      ");
        addBtnRow.getChildren().add(addBtn);
        
        TableView<Part> tViewPartsInProduct = new TableView<>(newPartsList);
        
        TableColumn<Part, String> pIdInProduct = new TableColumn<>("Part ID                  ");
        pIdInProduct.setCellValueFactory(new PropertyValueFactory<>("partId"));
        tViewPartsInProduct.getColumns().add(pIdInProduct);
        
        TableColumn<Part, String> pNameInProduct = new TableColumn<>("Part Name              ");
        pNameInProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        tViewPartsInProduct.getColumns().add(pNameInProduct);
        
        TableColumn<Part, String> pInvInProduct = new TableColumn<>("Inventory Level         ");
        pInvInProduct.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        tViewPartsInProduct.getColumns().add(pInvInProduct);
        
        TableColumn<Part, String> pPriceInProduct = new TableColumn<>("Price/Cost per Unit   ");
        pPriceInProduct.setCellValueFactory(new PropertyValueFactory<>("price"));
        tViewPartsInProduct.getColumns().add(pPriceInProduct); 
               
        tViewPartsInProduct.setMinHeight(100);
        tViewPartsInProduct.setMinWidth(600);

        HBox deleteBtnRow = new HBox();
        deleteBtnRow.setId("deleteBtnRow");
        Button deleteBtn = new Button("     delete     ");
        deleteBtnRow.getChildren().add(deleteBtn);
        
        HBox saveCancelBtnRow = new HBox();
        saveCancelBtnRow.setId("saveCancelBtnRow");
        Button saveBtn = new Button("     Save     ");
        Button cancelBtn = new Button("     Cancel     ");
        saveCancelBtnRow.getChildren().addAll(saveBtn,cancelBtn);        
//Buttons Action Handlers
        //Parts Search Button
        searchBtn.setOnAction((ActionEvent event) -> {
            String searchTerm = searchText.getText();
            ObservableList<Part> searchPartsList = FXCollections.observableArrayList();
            inventory.resetPartList();
            Inventory.filterablePartsList.stream().filter((part) -> (part.getName().toLowerCase().contains(searchTerm.toLowerCase()))).forEach((part) -> {
                searchPartsList.add(part);
            });
            if(searchPartsList.size()>0){                
                Inventory.filterablePartsList.clear();
                searchPartsList.stream().forEach((part) -> {
                    Inventory.filterablePartsList.add(part);
                });
            }
            else{
                JOptionPane.showMessageDialog(null, "Sorry, nothing matched!", "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);
            }
        }); 
        //adds selected part from the top list to the bottom list
        addBtn.setOnAction((ActionEvent e)->{
            if(tViewPartSearch.getSelectionModel().getSelectedItem() != null){
                Part selectedPart = tViewPartSearch.getSelectionModel().getSelectedItem();
                newPartsList.add(selectedPart);
            }
            else{
                JOptionPane.showMessageDialog(null, "Please select a part to add!", "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        deleteBtn.setOnAction((ActionEvent e)->{
            if(tViewPartsInProduct.getSelectionModel().getSelectedItem() != null){
                Part selectedPart = tViewPartsInProduct.getSelectionModel().getSelectedItem();
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete part?", "CAUTION!", JOptionPane.OK_CANCEL_OPTION);            
                if(choice == 0){                    
                    newPartsList.remove(selectedPart);
                }            
            }
            else{
                JOptionPane.showMessageDialog(null, "Please select a part to delete!", "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);
            }
        });        
        saveBtn.setOnAction((ActionEvent e) -> {
            //check for format exceptions and validate input
            ArrayList<String> validationErrorsList = new ArrayList<>();
            //part name text field check
            if(nameText.getText().equals("")){
                validationErrorsList.add("\nName field cannot be empty");
            }
            if(nameText.getText().equals("Product Name")){
                validationErrorsList.add("\nYou must change name field from 'Product Name'");
            }
            //inventory text field check & Min/Max text field check)
            Integer invValue = -1;
            Double prcValue = -1.00;
            Integer minValue = -1;
            Integer maxValue = -1;

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
                double partsTotal = 0;
                for(Part part:newPartsList){
                    partsTotal += part.getPrice();
                }
                if(prcValue < partsTotal){
                    validationErrorsList.add("\nPrice field cannot be less than total price of parts");
                }                    
                if(prcValue < 0){
                    validationErrorsList.add("\nPrice field cannot be negative");
                }                    
            }catch(Exception ex){
                validationErrorsList.add("\nPrice must be a number");                    
            }
            if(newPartsList.size()<1){
                validationErrorsList.add("\nAdd at least one part to product");
            }
            //if error list is not empty give user error list message otherwise check radio buttons and create new part, then add to part list
            if(validationErrorsList.size()>0){
                JOptionPane.showMessageDialog(null, "These fields need to be corrected before new part can be saved:"+validationErrorsList.toString().replace("[", "").replace("]", ""), "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Product newProduct = new Product(nameText.getText(), invValue, prcValue, minValue, maxValue, newPartsList);
                inventory.addProduct(newProduct);             
                inventory.resetPartList();
                inventory.resetProductList();
                saveBtn.getScene().getWindow().hide();
                primaryStage.show();
            }
        });
        cancelBtn.setOnAction((ActionEvent e) -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit Add Product Window?", "CAUTION!", JOptionPane.OK_CANCEL_OPTION);            
            if(choice == 0){                    
                inventory.resetPartList();
                inventory.resetProductList();
                cancelBtn.getScene().getWindow().hide();
                primaryStage.show();
            }  
        });
        rootRight.getChildren().addAll(searchBar,tViewPartSearch,addBtnRow,tViewPartsInProduct,deleteBtnRow,saveCancelBtnRow);
        
        Scene scene = new Scene(root, 1200, 500);
        scene.getStylesheets().add("software1/inventory/Styles.css");
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Add Product");
        newWindow.setScene(scene);
        // Set position of window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);
        newWindow.show();
    }
}
