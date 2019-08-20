/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1.inventory;

import javafx.collections.ObservableList;

/**
 *
 * @author lucasb
 */
public class Product {
    private static int productIDCounter = 0;
    private int productID;
    private String name;
    private int inStock;
    private double price;
    private int min;
    private int max;
    private ObservableList<Part> PartsInProductList;
    
    //autoID consturctor
    Product(String pName,int pInventory,double pPrice,int inMin,int inMax,ObservableList<Part> partsOfProduct){
        productIDCounter +=1;
        productID = productIDCounter;
        this.name = pName;
        this.inStock = pInventory;
        this.price = pPrice;
        this.min = inMin;
        this.max = inMax;
        this.PartsInProductList = partsOfProduct;
    };
        //modify product constructor
    Product(int ID,String pName,int pInventory,double pPrice,int inMin,int inMax,ObservableList<Part> partsOfProduct){
        productID = ID;
        this.name = pName;
        this.inStock = pInventory;
        this.price = pPrice;
        this.min = inMin;
        this.max = inMax;
        this.PartsInProductList = partsOfProduct;
};
    public ObservableList<Part> getPartsInProductList(){
         return this.PartsInProductList;
    }    
    public void addAssociatedPart(Part newPart){
        PartsInProductList.add(newPart);
    }
        
    public boolean removeAssociatedPart(int oldPartId){
        return PartsInProductList.remove(lookupAssociatedPart(oldPartId));
    }
    
    public Part lookupAssociatedPart(int Id){
        Part searchPart = null;
        for(Part part:PartsInProductList){
            if(Id == part.getPartId()){
                searchPart = part;
            }
        }
        return searchPart;
    }
    
    public int getProductId() {
        return productID;
    }

    void setProductId(int productId) {
        this.productID = productId;
    }

    public String getName() {
        return name;
    }

    void setName(String productName) {
        this.name = productName;
    }

    public int getInStock() {
        return inStock;
    }

    void setInStock(int productInventory) {
        this.inStock = productInventory;
    }

    public double getPrice() {
        return price;
    }

    void setPrice(double productPrice) {
        this.price = productPrice;
    }    

    public int getMin() {
        return min;
    }

    void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    void setMax(int max) {
        this.max = max;
    }
    
}
