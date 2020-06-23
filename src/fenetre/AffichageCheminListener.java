/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetre;

import graphe.Graph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author toky
 */
public class AffichageCheminListener implements ActionListener {
    private GrapheFenetre grapheFenetre;
    private String chemin;

    public AffichageCheminListener(GrapheFenetre grapheFenetre, String chemin) {
        this.grapheFenetre = grapheFenetre;
        this.chemin = chemin;
    }
   
    @Override
    public void actionPerformed(ActionEvent e) {
       
    // System.out.println(this.grapheFenetre.cheminAffiche+"/"+chemin);
     if(this.grapheFenetre.cheminAffiche.equals(chemin)){
         this.grapheFenetre.cheminAffiche="";
         this.grapheFenetre.repaint();
         return;
     }
     this.grapheFenetre.cheminAffiche=chemin;
     this.grapheFenetre.repaint();
    }
    
}
