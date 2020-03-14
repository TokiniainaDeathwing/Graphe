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

    public GraphValeur() {
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
