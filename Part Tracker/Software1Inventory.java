/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1.inventory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author lucasb
 */
public class Software1Inventory extends Application {
    
    static TableView<Product> tViewProducts;
    static TableView<Part> tViewParts;

    @Override
    public void start(Stage primaryStage) {
        Inventory inventory = Inventory.getInstance();
        
//main pane****************************************************
        BorderPane root = new BorderPane();
        
//Top Title****************************************************
        Label mainTitle = new Label();
        mainTitle.setText("Inventory Management System");
        mainTitle.setId("mainTitle");
        root.setTop(mainTitle);
        
//BEGIN Center Tables and Controls*****************************
        HBox centerTables = new HBox();
        centerTables.setAlignment(Pos.CENTER);
        root.setCenter(centerTables);
    //Table Parts Group
        VBox partsGroup = new VBox();
        partsGroup.setMaxWidth(610);
        partsGroup.setMinWidth(610);
        partsGroup.setId("partsGroup");
        //Top of Group
        HBox partsTop = new HBox();
        HBox rightPartsTop = new HBox();
        rightPartsTop.setId("rightTopRowTables");
        HBox leftPartsTop = new HBox();
        leftPartsTop.setId("leftTopRowTables");
        partsTop.setId("topRowTables");
        Label partsLabel = new Label("Parts");
        partsLabel.setId("tableHeaderLabelParts");
        Button partsSearchButton = new Button("Search");
        TextField partsSearchText = new TextField();
        partsSearchText.setId("searchField");
        rightPartsTop.getChildren().addAll(partsSearchButton,partsSearchText);
        leftPartsTop.getChildren().add(partsLabel);
        partsTop.getChildren().addAll(leftPartsTop,rightPartsTop);
        //Parts Table - Middle Group***********************************************************

        tViewParts = new TableView<>(Inventory.filterablePartsList);            
        
//set Columns
        TableColumn<Part, String> pId = new TableColumn<>("Part ID                  ");
        pId.setCellValueFactory(new PropertyValueFactory<>("partId"));
        tViewParts.getColumns().add(pId);
        
        TableColumn<Part, String> pName = new TableColumn<>("Part Name              ");
        pName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tViewParts.getColumns().add(pName);
        
        TableColumn<Part, String> pInv = new TableColumn<>("Inventory Level         ");
        pInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        tViewParts.getColumns().add(pInv);
        
        TableColumn<Part, String> pPrice = new TableColumn<>("Price/Cost per Unit   ");
        pPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tViewParts.getColumns().add(pPrice); 
               
        tViewParts.setMinHeight(200);
        
        //Buttons - Bottom Group
        HBox partsBottom = new HBox();
        partsBottom.setId("bottomRowButtons");
        Button addPart = new Button ("      Add     ");
        Button ModifyPart = new Button ("     Modify    ");
        Button deletePart = new Button ("    Delete    ");
        partsBottom.getChildren().addAll(addPart,ModifyPart,deletePart);
        partsGroup.getChildren().addAll(partsTop,tViewParts,partsBottom);
    //Table Products Group
        VBox productsGroup = new VBox();
        productsGroup.setId("productsGroup");
        productsGroup.setMaxWidth(575);
        productsGroup.setMinWidth(575);
        //Top of Group
        HBox productsTop = new HBox();
        productsTop.setId("topRowTables");
        Label productsLabel = new Label("Products");
        productsLabel.setId("tableHeaderLabelProducts");
        Button productsSearchButton = new Button("Search");
        TextField productsSearchText = new TextField();
        productsSearchText.setId("searchField");
        productsTop.getChildren().addAll(productsLabel,productsSearchButton,productsSearchText);        
                 
        tViewProducts  = new TableView<>(Inventory.filterableProductsList);
        
        //set Columns
        TableColumn<Product, String> pdctId = new TableColumn<>("Product ID         ");
        pdctId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tViewProducts.getColumns().add(pdctId);
        
        TableColumn<Product, String> pdctName = new TableColumn<>("Product Name     ");
        pdctName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tViewProducts.getColumns().add(pdctName);
        
        TableColumn<Product, String> pdctInv = new TableColumn<>("Inventory Level   ");
        pdctInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        tViewProducts.getColumns().add(pdctInv);
        
        TableColumn<Product, String> pdctPrice = new TableColumn<>("Price per Unit             ");
        pdctPrice.setCellValueFactory(new PropertyValueFactory<>("price"));        
        tViewProducts.getColumns().add(pdctPrice);       
        tViewProducts.setMinHeight(200);
        
        //Buttons - Bottom Group
        HBox productsBottom = new HBox();
        productsBottom.setId("bottomRowButtons");
        Button addProduct = new Button ("      Add     ");
        Button modifyProduct = new Button ("     Modify    ");
        Button deleteProduct = new Button ("    Delete    ");
        productsBottom.getChildren().addAll(addProduct,modifyProduct,deleteProduct);
        productsGroup.getChildren().addAll(productsTop,tViewProducts,productsBottom);
        centerTables.getChildren().addAll(partsGroup,productsGroup);
//END Center Tables and Controls*******************************
        
//Exit Button Row**********************************************
        HBox bottomExit =  new HBox();
        bottomExit.setId("bottomExitButton");
        Button exitBtn = new Button("         Exit         ");
        bottomExit.getChildren().add(exitBtn);
        root.setBottom(bottomExit);


//Parts Button Actions/New Windows***********************************************
        //Parts Search Button
        partsSearchButton.setOnAction((ActionEvent event) -> {
            String searchTerm = partsSearchText.getText();
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
        //Call AddPartWindow**********************************************************************************
        addPart.setOnAction((ActionEvent event) -> {
            AddPartWindow s = new AddPartWindow(primaryStage);
            partsSearchText.setText("");
            productsSearchText.setText("");
            primaryStage.hide();
        });
        //Call Modify Part Window   
        ModifyPart.setOnAction((ActionEvent event) -> {
            
            if(tViewParts.getSelectionModel().getSelectedItem() != null){
                Part selectedPart = tViewParts.getSelectionModel().getSelectedItem();
                ModifyPartWindow s = new ModifyPartWindow(primaryStage,selectedPart);
                partsSearchText.setText("");
                productsSearchText.setText("");                
                primaryStage.hide();
            } else{
                JOptionPane.showMessageDialog(null, "Please select a part to modify!", "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);            
            }
        });
        //Delete Part Button
        deletePart.setOnAction((ActionEvent event) -> {
            if(tViewParts.getSelectionModel().getSelectedItem() != null){
                Part selectedPart = tViewParts.getSelectionModel().getSelectedItem();
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete part?", "CAUTION!", JOptionPane.OK_CANCEL_OPTION);            
                if(choice == 0){                    
                    inventory.deletePart(selectedPart);
                    inventory.resetPartList();
                }              
            } else{
                JOptionPane.showMessageDialog(null, "Please select a part to delete!", "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
//Products Button Actions/New Windows***********************************************   
        //Products Search Button
        productsSearchButton.setOnAction((ActionEvent event) -> {
            String searchTerm = productsSearchText.getText();
            ObservableList<Product> searchProductsList = FXCollections.observableArrayList();
            inventory.resetProductList();            
            Inventory.filterableProductsList.stream().filter((product) -> (product.getName().toLowerCase().contains(searchTerm.toLowerCase()))).forEach((product) -> {
                searchProductsList.add(product);
            });
            if(searchProductsList.size()>0){                
                Inventory.filterableProductsList.clear();
                searchProductsList.stream().forEach((part) -> {
                    Inventory.filterableProductsList.add(part);
                });
            }
            else{
                JOptionPane.showMessageDialog(null, "Sorry, nothing matched!", "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);
            }
        });        
        //Call AddProductWindow**********************************************************************************
        addProduct.setOnAction((ActionEvent event) -> {
            AddProductWindow s = new AddProductWindow(primaryStage);
            partsSearchText.setText("");
            productsSearchText.setText("");            
            primaryStage.hide();
        });
        //Call ModifyProductWindow**********************************************************************************
        modifyProduct.setOnAction((ActionEvent event) -> {
            if(tViewProducts.getSelectionModel().getSelectedItem() != null){
                Product selectedProduct = tViewProducts.getSelectionModel().getSelectedItem();
                ModifyProductWindow s = new ModifyProductWindow(primaryStage,selectedProduct);
                partsSearchText.setText("");
                productsSearchText.setText("");            
                primaryStage.hide();
            } else{
                JOptionPane.showMessageDialog(null, "Please select a product to modify!", "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);            
            }
        });
        deleteProduct.setOnAction((ActionEvent event) -> {
            if(tViewProducts.getSelectionModel().getSelectedItem() != null){
                Product selectedProduct = tViewProducts.getSelectionModel().getSelectedItem();
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete part?", "CAUTION!", JOptionPane.OK_CANCEL_OPTION);            
                if(choice == 0){                    
                    inventory.removeProduct(selectedProduct.getProductId());
                    inventory.resetProductList();
                }              
            } else{
                JOptionPane.showMessageDialog(null, "Please select a part to delete!", "Whoopsie!", JOptionPane.INFORMATION_MESSAGE);
            }
        });        
        //exit button**************************************************************************************
        exitBtn.setOnAction((ActionEvent e) ->{ 
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit prgram?", "CAUTION!", JOptionPane.OK_CANCEL_OPTION);            
            if(choice == 0){                    
                Platform.exit();
            }              
        });
                
        
//Main Scene***********************************************************************************************
        Scene scene = new Scene(root, 1200, 470);
        scene.getStylesheets().add("software1/inventory/Styles.css");
        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}
