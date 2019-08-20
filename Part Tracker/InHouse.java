/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software1.inventory;

/**
 *
 * @author wonder monkey
 */
public class InHouse extends Part{
    
    private int machNum;
    
    //autoID constructor
    public InHouse(String pName, int pInventory, double pPrice, int inMin, int inMax, int mNum) {
        super(pName, pInventory, pPrice, inMin, inMax);
        this.machNum = mNum;
    }
    //constructor used to retain id in modify part window when switching from OutSource type to InHouse type
    public InHouse(int id, String pName, int pInventory, double pPrice, int inMin, int inMax, int mNum) {
        super(id, pName, pInventory, pPrice, inMin, inMax);
        this.machNum = mNum;

    }

    public int getMachNum() {
        return machNum;
    }

    void setMachNum(int machNum) {
        this.machNum = machNum;
    }
    
}
