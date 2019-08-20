/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1.inventory;

/**
 *
 * @author lucasb
 */
public abstract class Part {
    private static int partIDCounter = 0;
    private int partID;
    private String name;
    private int inStock;
    private double price;
    private int min;
    private int max;
    //autoID consturctor
    Part(String pName,int pInventory,double pPrice,int inMin,int inMax){
        partIDCounter += 1;
        this.partID = partIDCounter;
        this.name = pName;
        this.inStock = pInventory;
        this.price = pPrice;
        this.min = inMin;
        this.max = inMax;
    }
    //set id for modify part window constructor - allows part to retain old part number when switch from 
    //InHouse type to OutSourced type and vice versa
    Part(int id,String pName,int pInventory,double pPrice,int inMin,int inMax){        
        this.partID = id;
        this.name = pName;
        this.inStock = pInventory;
        this.price = pPrice;
        this.min = inMin;
        this.max = inMax;
    }

    public int getPartId() {
        return partID;
    }

    void setPartId(int partId) {
        this.partID = partId;
    }

    public String getName() {
        return name;
    }

    void setName(String partName) {
        this.name = partName;
    }

    public int getInStock() {
        return inStock;
    }

    void setInStock(int partStock) {
        this.inStock = partStock;
    }

    public double getPrice() {
        return price;
    }

    void setPrice(double partPrice) {
        this.price = partPrice;
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
