/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe;

import arbre.ArbreGeneral;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author toky
 */
public class Graph {
    enum Couleur{
        Blanc,
        Gris,
        Noir
    }
    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
        
    }
 
    public Node getNodeByName(String name){
        Node n=null;
        for(Node d:this.nodes){
            if(d.getName().equals(name)){
                return d;
            }
        }
        return n;
    }
    private void  Parcours_Profondeur_Topo(Stack<Node> listeNodes,Node x,int[] t){
        x.setCouleur(Couleur.Gris);
       /* t=new Timestamp(t.getTime()+3600000);
        x.dateDebut=t;*/
      //
       t[0]+=1;
       //x.dateDebut=t[0];
        for(Entry<Node,GraphValeur> adjacencyPair:x.getAdjacentNodes().entrySet()){
           Node adj=adjacencyPair.getKey();
           if(adj.getCouleur()==Couleur.Blanc){
               Parcours_Profondeur_Topo( listeNodes, adj,t);
           }
        }
        x.setCouleur(Couleur.Noir);
       // t=new Timestamp(t.getTime()+3600000);
        //x.dateFin=t;
        t[0]+=1;
        //x.dateFin=t[0];
        listeNodes.push(x);
    }
    public Stack<Node> triTopologique(){
        Stack<Node> listeNodes=new Stack<Node>();
        for(Node node:this.getNodes()){
            node.setCouleur(Couleur.Blanc);
        }
        //Timestamp t=new Timestamp(System.currentTimeMillis());
       int[] t=new int[1]; 
       t[0]=0;
        for(Node node:this.getNodes()){
            if(node.getCouleur()==Couleur.Blanc){
                Parcours_Profondeur_Topo(listeNodes, node,t);
            }
        }
        return listeNodes;
    }
    public  List<Stack<Comparable>> PlusCourtchemin( Node sdeb,Node sarrive) {
        // Initialisation
       Stack<Node> chemin=new Stack<Node>();
        for(Node node:this.getNodes()){
            node.setDistance(Float.MAX_VALUE);
            node.setPredecesseur(new ArrayList<Node>());
        }
        
         sdeb.setDistance(new Float(0));
         
        //Q := ensemble de tous les nœuds
        Set<Node> Q=new HashSet<>(this.getNodes());
       
        
        //tant que Q n'est pas un ensemble vide faire
        while(!Q.isEmpty()){
            Node s1=trouver_min(Q);
            Q.remove(s1);
            //pour chaque nœud s2 voisin de s1 faire
            for(Entry<Node,GraphValeur> adjacencyPair:s1.getAdjacentNodes().entrySet()){
                Node s2=adjacencyPair.getKey();
                Float distanceS1_S2 = adjacencyPair.getValue().getDistance();
                maj_Distances(s1,s2,distanceS1_S2);
            }
        }
        
        //Recherche du plus court chemin
        ArbreGeneral arbre=new ArbreGeneral();
        Node s=sarrive;
        creerArbre(arbre,s,true);
        ArrayList<Comparable> listeFeuille=arbre.getFeuilles();
        List<Stack<Comparable>> listeChaine=new ArrayList<Stack<Comparable>>();
        for(Comparable node:listeFeuille){
            Stack<Comparable> chaine=arbre.ChaineFeuilleRacine(node);
            listeChaine.add(chaine);
            
        }
        return listeChaine;
 }
    private void creerArbre(ArbreGeneral arbre,Node node,boolean start){
        List<Node> listePredecesseur=node.getPredecesseur();
        if(start){
            arbre.put(node,null);
            
        }
        if(listePredecesseur.isEmpty()){
            return;
        }
        for(Node p:listePredecesseur){
            p=p.clone();
            arbre.put(p, node);
            creerArbre(arbre,p,false);
        }
        
    }
    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }
    private static Node trouver_min(Set<Node> Q){
        Float minimum=Float.MAX_VALUE;
        Node sommet=null;
        for(Node node:Q){
            if(node.getDistance()<minimum){
                minimum=node.getDistance();
                sommet=node;
            }
        }
        return sommet;
    }
    private static void maj_Distances(Node s1,Node s2,Float distanceS1_S2){
        if(s2.getDistance()>=s1.getDistance()+distanceS1_S2){
            s2.setDistance(s1.getDistance()+distanceS1_S2);
            s2.getPredecesseur().add(s1);
        }
    }
    
    public void ordonnerTache(Node parent){
        if(parent==null){
            parent=this.getNodeByName("DEBUT");
        }else if(parent.getName().equals("FIN")){
            parent.dateTard=parent.dateDebutTot;
            OrdonnerDatePlusTard(parent);
            return;
        }
        //Set Date plus tot et plus tard
        for(Entry<Node,GraphValeur> adjacencyPair:parent.getAdjacentNodes().entrySet()){
                Node fils=adjacencyPair.getKey();
                calculerDateTot(fils);
        }
        for(Entry<Node,GraphValeur> adjacencyPair:parent.getAdjacentNodes().entrySet()){
                Node fils=adjacencyPair.getKey();
                ordonnerTache(fils);
        }
        
    }
    private void OrdonnerDatePlusTard(Node parent){
       if(parent==null){
            parent=this.getNodeByName("FIN");
        }else if(parent.getName().equals("DEBUT")){
            return;
        }
     
        for(Node predecesseur:parent.getPredecesseurNoeud()){
                calculerDateTard(predecesseur);
                
        }
        for(Node predecesseur:parent.getPredecesseurNoeud()){
                OrdonnerDatePlusTard(predecesseur);
                
        }
    }
    private void calculerDateTard(Node node){
        Float min=Float.MAX_VALUE;
        for(Entry<Node,GraphValeur> adjacencyPair:node.getAdjacentNodes().entrySet()){
            Node successeur=adjacencyPair.getKey();
            Float minTemp=successeur.dateTard-node.dureeTache;
            if(min>minTemp){
                min=minTemp;
            }
        }
        node.dateTard=min;
    }
    private void calculerDateTot(Node node){
        List<Node> listePredecesseur=node.getPredecesseurNoeud();
        Float max=0F;
        
        for(Node prec:listePredecesseur){
            Float maxTemp=prec.dateDebutTot+prec.dureeTache;
            
            if(max<maxTemp){
                max=maxTemp;
            }
        }
        node.dateDebutTot=max;
    }
}

