/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe;

/**
 *
 * @author toky
 */
public class GraphValeur {
    private Float distance=Float.MAX_VALUE;
    private Float flotMax=Float.MAX_VALUE;
    public Float ancienFlot=0F;
    public GraphValeur() {
    }
    public GraphValeur(Float distance,Float flotMax) {
        this.distance=distance;
        this.flotMax=flotMax;
        this.ancienFlot=distance;
    }
     public GraphValeur(Float distance) {
        this.distance=distance;
       
    }
    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
        
    }

    public Float getFlotMax() {
        return flotMax;
    }

    public void setFlotMax(Float flotMax) {
        this.flotMax = flotMax;
    }
    
}
