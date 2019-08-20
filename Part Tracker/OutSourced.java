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
public class OutSourced extends Part{
    
    private String compName;
    //autoID constructor
    public OutSourced(String pName, int pInventory, double pPrice, int inMin, int inMax,String cName) {
        super(pName, pInventory, pPrice, inMin, inMax);
        this.compName = cName;
    }
    //constructor used to retain id in modify part window when switching from InHouse type to OutSource type
    public OutSourced(int id, String pName, int pInventory, double pPrice, int inMin, int inMax,String cName) {
        super(id, pName, pInventory, pPrice, inMin, inMax);
        this.compName = cName;
    }

    public String getCompName() {
        return compName;
    }

    void setCompName(String compName) {
        this.compName = compName;
    }
    
    
    
}
