/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe;

import arbre.ArbreGeneral;
import fenetre.GrapheFenetre;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author toky
 */
public class GrapheConsole {

    /**
     * @param args the command line arguments
     */
       public static void afficher(ArbreGeneral arbre){
          Set set = arbre.entrySet();
            Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
          Map.Entry mentry = (Map.Entry)iterator.next();
         System.out.print("noeud: "+((Node)(mentry.getKey())).getName() +" pere:");
         if(mentry.getValue()!=null){
                System.out.println(((Node)(mentry.getValue())).getName());
         }
         else{
                System.out.println("aucun");
         }
      
        }
        
         
     }
    public static void main(String[] args) {
        // TODO code application logic here
   
   // Stack<Node> chemin=graph.PlusCourtchemin( nodeA, nodeF);
   /* while(!chemin.isEmpty()){
        Node d=chemin.pop();
        System.out.println(d.getName()+"-"+d.getDistance());
    }*/
   
   
    /*System.out.println("A->E");
    /*Stack<Node> chemin2=graph.PlusCourtchemin( nodeA, nodeE);
    while(!chemin2.isEmpty()){
        Node d=chemin2.pop();
        System.out.println(d.getName()+"-"+d.getDistance());
    }*/
 GrapheFenetre fenetre=new GrapheFenetre(new Graph());
   fenetre.setVisible(true);

}
}
    
    
    

