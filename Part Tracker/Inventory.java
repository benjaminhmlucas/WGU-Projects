/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author wonder monkey
 */
public class Inventory {
    private static Inventory singleInventory = null; 
    //This list is bound to table view
    public static ObservableList<Part> filterablePartsList; 
    //This list is used for saving whole list when filtering for searches
    public static ObservableList<Part> savedPartsList;
    //This list is bound to table view
    public static ObservableList<Product> filterableProductsList; 
    //This list is used for saving whole list when filtering for searches    
    public static ObservableList<Product> savedProductsList;


    private Inventory() 
    { 
        filterablePartsList = FXCollections.observableArrayList(
            new OutSourced("Part1",5,5,0,100,"McBreaky's"),
            new InHouse("Part2",10,10,0,100,66),
            new InHouse("Part3",15,15,0,100,78)
        );
        savedPartsList = FXCollections.observableArrayList(filterablePartsList);

        filterableProductsList = FXCollections.observableArrayList(
            new Product("Product1",5,15,0,100,FXCollections.observableArrayList(
                lookupPart(1),lookupPart(2)
            )),
            new Product("Product2",10,20,0,100,FXCollections.observableArrayList(
                lookupPart(1),lookupPart(3)
            )),
            new Product("Product3",15,30,0,100,FXCollections.observableArrayList(
                lookupPart(1),lookupPart(2),lookupPart(3)
            ))
        );
        savedProductsList = FXCollections.observableArrayList(filterableProductsList);
        
    } 
    
    public static Inventory getInstance() 
    { 
        if (singleInventory == null){ 
            singleInventory = new Inventory(); 
        }
        return singleInventory; 
    }
    
    public void addProduct(Product newProduct){
        savedProductsList.add(newProduct);
        resetProductList();
    }
    
    public boolean removeProduct(int prodNum){
        boolean removed = savedProductsList.remove(lookupProduct(prodNum));
        resetProductList();
        return removed;
    }
    
    public Product lookupProduct(int prodNum){
        Product searchProduct = null;
        for(Product prod:savedProductsList){
            if(prodNum == prod.getProductId()){
                searchProduct = prod;
            }
        }
        return searchProduct;
    }
    
    public void updateProduct(int prodNum, Product newProduct){
        removeProduct(prodNum);
        addProduct(newProduct);
    }
    
    public void resetProductList(){
        filterableProductsList.clear();
        savedProductsList.stream().forEach((part) -> {
            filterableProductsList.add(part);
        });
        Inventory.filterableProductsList.sort((a,b)->Integer.compare(a.getProductId(), b.getProductId()));
    }
    
    public void addPart(Part newPart){
        savedPartsList.add(newPart);
        resetPartList();   
    }
    
    public boolean deletePart(Part oldPart){
            boolean removed = savedPartsList.remove(oldPart);
            resetPartList();          
            return removed;
    }
    
    public Part lookupPart(int pNum){
        Part searchPart = null;
        for(Part part:savedPartsList){
            if(pNum == part.getPartId()){
                searchPart = part;
            }
        }
        return searchPart;
    }
    
    public void updatePart(int pNum,Part updatedPart){
       deletePart(lookupPart(pNum)); 
       addPart(updatedPart); 
    }
    
    public void resetPartList(){
        filterablePartsList.clear();
        savedPartsList.stream().forEach((part) -> {
            filterablePartsList.add(part);
        });
        Inventory.filterablePartsList.sort((a,b)->Integer.compare(a.getPartId(), b.getPartId()));
    }
}


