/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe;

import graphe.Graph.Couleur;
import java.awt.Point;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author toky
 */
public class Node implements Comparable {
    private String name;
    private Couleur couleur;
    private List<Node> predecesseur=new ArrayList<Node>();
    private Float distance=Float.MAX_VALUE;
    private int x=0,y=0;
    private Map<Node, GraphValeur> adjacentNodes = new HashMap<>();
    public Timestamp dateDebut;
    public Timestamp dateFin;
    public Couleur getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }
    

    
    public void setPredecesseur(List<Node> predecesseur) {
        this.predecesseur = predecesseur;
    }

 
    
    
 

  

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addDestination(Node destination, GraphValeur valeur) {
        adjacentNodes.put(destination, valeur);
    }

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }







  

    public Map<Node, GraphValeur> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, GraphValeur> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public List<Node> getPredecesseur() {
        return predecesseur;
    }

   
    public Point[] getSensFleche(Node destination,int widthNode){
        Point[] listePoint=new Point[2];
        listePoint[0]=new Point(0,0);
        listePoint[1]=new Point(0,0);
        int xa=this.getX();
        int xb=destination.getX();
        int ya=this.getY();
        int yb=destination.getY();
        int xd,xf,yd,yf;
        if(xa<xb){
            xd=xa+widthNode;
            xf=xb;
        }else{
            xd=xa;
            xf=xb+widthNode;
        }
        yd=ya+(widthNode/2);
        yf=yb+(widthNode/2);
        listePoint[0].setLocation(xd, yd);
        listePoint[1].setLocation(xf, yf);
        return listePoint;
    }

    
    
    public Node clone(){
        Node node=new Node(this.name);
        node.setPredecesseur(predecesseur);
        node.setAdjacentNodes(adjacentNodes);
        node.setDistance(distance);
        node.setX(x);
        node.setY(y);
        return node;
    }

    @Override
    public String toString() {
        return this.name; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(Object o) {
        if(this.equals(o)){
            return 0;
        } else{
            return 1;
        }
    }
    
    
    
}
