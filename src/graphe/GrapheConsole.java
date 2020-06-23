/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe;

import fenetre.GanttChart;
import arbre.ArbreGeneral;
import fenetre.GrapheFenetre;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

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
 /*GrapheFenetre fenetre=new GrapheFenetre(new Graph());
   fenetre.setVisible(true);*/
  /* GanttChart example=new GanttChart("Taches");
         example.setSize(800, 400);
         example.setLocationRelativeTo(null);
         example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         example.setVisible(true);
 
*/
  Graph graphe=new Graph();
  /*Node debut=new Node("DEBUT");
  Node a=new Node("A");
  Node b=new Node("B");
  Node c=new Node("C");
  Node d=new Node("D");
  Node e=new Node("E");
  
  Node fin=new Node("FIN");
 // graphe.addNode(debut);
  graphe.addNode(a);
  graphe.addNode(b);
  graphe.addNode(c);
  graphe.addNode(d);
  graphe.addNode(e);
  //graphe.addNode(fin);
  
  //debut.addDestination(a,new GraphValeur(0F));
  //debut.addDestination(b,new GraphValeur(0F));
  b.addDestination(d, new GraphValeur(4F));
  a.addDestination(c, new GraphValeur(2F));
  c.addDestination(e, new GraphValeur(4F));
  d.addDestination(e, new GraphValeur(5F));
  
  //e.addDestination(fin, new GraphValeur(6F));
  /*graphe.ordonnerTache(null,new Timestamp(System.currentTimeMillis()));
  for(Node node:graphe.getNodes()){
      System.out.println(node.toString()+":"+node.getDateDebut()+"|"+node.getDateFin());
  }
  GanttChart example=new GanttChart("Taches",graphe);
         example.setSize(800, 400);
         example.setLocationRelativeTo(null);
         example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         example.setVisible(true);
 
*/
 
   
  GrapheFenetre fenetre=new GrapheFenetre(graphe);
   fenetre.setExtendedState(JFrame.MAXIMIZED_BOTH); 
 
  fenetre.setVisible(true);
}
    
}
    
    
    

