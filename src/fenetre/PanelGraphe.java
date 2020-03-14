/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetre;

import graphe.Node;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author toky
 */
public class PanelGraphe extends JPanel {
    private Set<Node> listeNodes=null;

    public Set<Node> getListeNodes() {
        return listeNodes;
    }

    public void setListeNodes(Set<Node> listeNodes) {
        this.listeNodes = listeNodes;
    }
    public PanelGraphe(Set<Node> listeNodes){
        this.listeNodes=listeNodes;
    }
    public void paint(Graphics g)  
    {  
            
            
            for(Node d:listeNodes){
               g.setColor(Color.RED);  
               
               //g.drawString(d.getName(), d.getX()+(rayonNode/2)-5, d.getY()+(rayonNode/2)+5);
               g.drawOval(d.getX(),d.getY(),50,50);
               
               
            }
            
            //drawDirection(g,null,null,15);
           
    }
}
