/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author toky
 */
public class Chaine {
    private List<Node> listeNoeud=new ArrayList<Node>();
    
    public void addNode(Node a){
        listeNoeud.add(a);
    }
    public Float trouverFlotAmelioration(){
        Float min=Float.MAX_VALUE;
        int n=listeNoeud!=null?listeNoeud.size():0;
        for(int i=0;i<n-1;i++){
            
            Node noeud=listeNoeud.get(i);
            Node noeudSuivant=listeNoeud.get(i+1);
            Map<Node, GraphValeur> adjacencyPair= noeud.getAdjacentNodes();
            List<Node> listePredecesseur=noeud.getPredecesseurNoeud();
            if(adjacencyPair.containsKey(noeudSuivant)){
                GraphValeur valeur=adjacencyPair.get(noeudSuivant);
                Float minTemp=valeur.getFlotMax()-valeur.getDistance();
                if(minTemp<min){
                    min=minTemp;
                }
            }else if(listePredecesseur.contains(noeudSuivant)){
                Map<Node, GraphValeur> adjacencyPair2= noeudSuivant.getAdjacentNodes();
                GraphValeur valeur=adjacencyPair2.get(noeud);
               
                Float minTemp=valeur.getDistance();
                if(minTemp<min){
                    min=minTemp;
                }
            }else{
                
            }
             
        }
        return min;
    }
    public boolean ameliorerChaine(){
        Float flot=this.trouverFlotAmelioration();
        if(flot<=0){
            return false;
        }
        int n=listeNoeud!=null?listeNoeud.size():0;
        for(int i=0;i<n-1;i++){
            Node noeud=listeNoeud.get(i);
            Node noeudSuivant=listeNoeud.get(i+1);
            Map<Node, GraphValeur> adjacencyPair= noeud.getAdjacentNodes();
            List<Node> listePredecesseur=noeud.getPredecesseurNoeud();
             if(adjacencyPair.containsKey(noeudSuivant)){
                GraphValeur valeur=adjacencyPair.get(noeudSuivant);
                valeur.setDistance(valeur.getDistance()+flot);
                
            }else if(listePredecesseur.contains(noeudSuivant)){
                Map<Node, GraphValeur> adjacencyPair2= noeudSuivant.getAdjacentNodes();
                GraphValeur valeur=adjacencyPair2.get(noeud);
                valeur.setDistance(valeur.getDistance()-flot);
            }else{
                
            }           
        } 
        return true;
    }
    
}
