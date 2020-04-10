/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetre;

import graphe.Chaine;
import graphe.Graph;
import graphe.GraphValeur;
import graphe.Node;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author toky
 */
public class GrapheFenetre extends javax.swing.JFrame {

    /**
     * Creates new form GrapheFenetre
     */
    private Graph graphe;
    private int rayonNode=50;
    private int distanceNoeud=20;
    private volatile int draggedAtX, draggedAtY;
    private List<Stack<Comparable>> listeChemin=new ArrayList<Stack<Comparable>>();
    public GrapheFenetre(Graph graphe) {
        this.graphe=graphe;
        
        initComponents();
        initGraphePosition();
        try{
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception exc){
            
        }
        this.graphe.triTopologique();
        createLabels();
    
    }
    
    private void initGraphePosition(){
        int x=15;
        int y=rayonNode;
        for(Node node:this.graphe.getNodes()){
            node.setX(x);
            node.setY(y);
            x+=rayonNode+distanceNoeud;
            if(x>=this.panelGraphe.getWidth()){
                x=15;
                y+=rayonNode+distanceNoeud;
                if(y>=panelGraphe.getHeight()){
                    panelGraphe.setBounds(0, 0, panelGraphe.getWidth(), panelGraphe.getHeight()+100);
                    this.setBounds(0, 0, this.getWidth(), this.getHeight()+100);
                    
                }
            }
        }
    
    }
    
    private void createLabels(){
        panelGraphe.removeAll();
        int rayonLab=rayonNode/2;
        for(Node d:this.graphe.getNodes()){
            //System.out.println(d.getName()+":"+d.getX()+","+d.getY());
            
            JButton btn=new JButton(d.getName());
          
            btn.setBounds(d.getX(), d.getY(), rayonNode, rayonNode);
            btn.addMouseMotionListener(new NodeMoveListener(d,panelGraphe));
            
            //lab.setVisible(false);
            panelGraphe.add(btn);
           

            
        }
       

        
    }
    private void rechercherPlusCourtChemin(String depart,String fin){
        Node nodeDepart=null;
        Node nodeFin=null;
        for(Node node:this.graphe.getNodes()){
            if(node.getName().equals(depart)){
                nodeDepart=node;
            }
            if(node.getName().equals(fin)){
                nodeFin=node;
            }
        }
        this.listeChemin=this.graphe.PlusCourtchemin(nodeDepart, nodeFin);
        panelGraphe.repaint();
        
    }
    private void dessinerChemin(Graphics g){
        int x=textDepart.getX();
        int y=155;
        
        for(Stack<Comparable> liste:this.listeChemin){
            String text="";
            float distance=0;
            for(Comparable n:liste){
                text+=n.toString()+"-";
                distance=((Node)n).getDistance();
            }
            text+=distance;
            g.drawString(text, x, y);
             
             y+=30;
        }
        
    }
    private void dessiner(Graphics g)  
    {  
           
            
            for(Node node:this.graphe.getNodes()){
              /*         g.setColor(Color.RED);  
               
               g.drawString(d.getName(), d.getX()+(rayonNode/2)-5, d.getY()+(rayonNode/2)+5);
               g.drawOval(d.getX(),d.getY(),rayonNode,rayonNode);
               g.drawLine(d.getX(), d.getY(), d.getX()+50, d.getY());
               */
              for(Map.Entry<Node,GraphValeur> adjacencyPair:node.getAdjacentNodes().entrySet()){
                  Node destination=adjacencyPair.getKey();
                  Float distance=adjacencyPair.getValue().getDistance();
                   Float flotMax=adjacencyPair.getValue().getFlotMax();
                  drawDirection(g,node,destination,distance,flotMax);
              }
               
            }
            panelGraphe.setBackground(new java.awt.Color(51, 204, 255));
            
            //drawDirection(g,null,null,15);
             
    }
    private boolean cheminsContains(Node a,Node b){
       // System.out.println("Node :"+b.toString()+" size:"+this.listeChemin.size());
        for(Stack<Comparable> list:this.listeChemin){
            String text="";
            for(Comparable c:list){
                text+=c.toString()+"-";
           
            }
            if(text.contains(a.getName()+"-"+b.getName())){
                return true;
            }
            // System.out.println();
        }
       
        return false;
    }
    private void drawDirection(Graphics g,Node a,Node b,Float distance,Float flotMax){
        Point[] locations=a.getSensFleche(b, rayonNode);
        int x=locations[0].x;
        int y=locations[0].y;
        int endX=locations[1].x;
        int endY=locations[1].y;
        int thickness=2;
        int xdist=((x+endX)/2)+15;
        int ydist=((y+endY)/2)+15;
        ydist-=10;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.BLUE);
        if(this.cheminsContains(a, b)){
            g2.setColor(Color.RED);
        }
      
        g2.setStroke(new BasicStroke(thickness));
        
        g2.drawLine(x, y, endX, endY);;
        String stDist=""+distance;
        if(flotMax!=0){
            stDist+="/"+flotMax;
        }
        g2.drawString(stDist, xdist, ydist);
        drawArrowHead(g2,x,y,endX,endY);
        g2.dispose();
        
    }
    private void drawArrowHead(Graphics2D g2,int x,int y,int endX,int endY) {

        Polygon arrowHead = new Polygon();
        AffineTransform tx = new AffineTransform();
        arrowHead.addPoint(0, 5);
        arrowHead.addPoint(-10, -10);
        arrowHead.addPoint(10, -10);

        tx.setToIdentity();
        double angle = Math.atan2(endY - y, endX - x);
        tx.translate(endX, endY);
        tx.rotate(angle - Math.PI / 2d);

        g2.setTransform(tx);
        g2.draw(arrowHead);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelGraphe = new javax.swing.JPanel(){
            public void paint(Graphics g){
                super.paint(g);
                dessiner(g);
            }

        };
        panelChemin = new javax.swing.JPanel(){
            public void paint(Graphics g){
                super.paint(g);
                dessinerChemin(g);
            }
        };
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textDepart = new javax.swing.JTextField();
        textFin = new javax.swing.JTextField();
        boutonChemin = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        panelAjout = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        dateDebut = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        buttonGantt = new javax.swing.JButton();
        buttonTaches = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1080, 768));

        panelGraphe.setBackground(new java.awt.Color(51, 204, 255));

        javax.swing.GroupLayout panelGrapheLayout = new javax.swing.GroupLayout(panelGraphe);
        panelGraphe.setLayout(panelGrapheLayout);
        panelGrapheLayout.setHorizontalGroup(
            panelGrapheLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 684, Short.MAX_VALUE)
        );
        panelGrapheLayout.setVerticalGroup(
            panelGrapheLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 573, Short.MAX_VALUE)
        );

        panelChemin.setBackground(new java.awt.Color(0, 204, 204));

        jLabel1.setText("Depart");

        jLabel2.setText("Fin");

        boutonChemin.setText("Plus court");
        boutonChemin.setName(""); // NOI18N
        boutonChemin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonCheminActionPerformed(evt);
            }
        });

        jButton1.setLabel("Importer Graphe");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Tri topologique");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText("Enregister");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Maximisation Flot");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCheminLayout = new javax.swing.GroupLayout(panelChemin);
        panelChemin.setLayout(panelCheminLayout);
        panelCheminLayout.setHorizontalGroup(
            panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCheminLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCheminLayout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5))
                    .addGroup(panelCheminLayout.createSequentialGroup()
                        .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textDepart, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(55, 55, 55)
                        .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFin, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(boutonChemin))
                .addContainerGap())
        );
        panelCheminLayout.setVerticalGroup(
            panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCheminLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(26, 26, 26)
                .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textDepart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boutonChemin))
                .addContainerGap(189, Short.MAX_VALUE))
        );

        boutonChemin.getAccessibleContext().setAccessibleName("");

        panelAjout.setBackground(new java.awt.Color(255, 102, 255));

        jLabel3.setText("Date debut du projet");

        dateDebut.setEnabled(false);
        dateDebut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateDebutActionPerformed(evt);
            }
        });

        jLabel4.setText("Noeud 1");

        jLabel5.setText("Noeud 2");

        jLabel6.setText("Flot");

        jLabel7.setText("Flot max");

        jButton3.setText("Ajouter");

        buttonGantt.setText("Afficher gantt");
        buttonGantt.setEnabled(false);

        buttonTaches.setText("Ordonner taches");
        buttonTaches.setEnabled(false);

        jCheckBox1.setText("Gestion de projet");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAjoutLayout = new javax.swing.GroupLayout(panelAjout);
        panelAjout.setLayout(panelAjoutLayout);
        panelAjoutLayout.setHorizontalGroup(
            panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAjoutLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAjoutLayout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelAjoutLayout.createSequentialGroup()
                        .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dateDebut)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAjoutLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelAjoutLayout.createSequentialGroup()
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26))
                                    .addGroup(panelAjoutLayout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelAjoutLayout.createSequentialGroup()
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30))
                                    .addGroup(panelAjoutLayout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(69, 69, 69)))
                                .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelAjoutLayout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(55, 55, 55))
                                    .addGroup(panelAjoutLayout.createSequentialGroup()
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjoutLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjoutLayout.createSequentialGroup()
                                        .addComponent(jButton3)
                                        .addGap(224, 224, 224))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjoutLayout.createSequentialGroup()
                                        .addComponent(buttonTaches)
                                        .addGap(18, 18, 18)
                                        .addComponent(buttonGantt)))))))
                .addContainerGap())
        );
        panelAjoutLayout.setVerticalGroup(
            panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAjoutLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateDebut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonGantt)
                    .addComponent(buttonTaches))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addGap(35, 35, 35)
                .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(3, 3, 3)
                .addGroup(panelAjoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelGraphe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelChemin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAjout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGraphe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelChemin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAjout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boutonCheminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonCheminActionPerformed
        // TODO add your handling code here:
        this.rechercherPlusCourtChemin(this.textDepart.getText().trim(), this.textFin.getText().trim());
        panelChemin.repaint();
    }//GEN-LAST:event_boutonCheminActionPerformed
    
    private void saveFile(File fichier){
        try {
            if(!fichier.exists()){
                fichier.createNewFile();
            }
            
            FileWriter filewriter=new FileWriter(fichier);
            String nodeinfo="";
            int n=this.graphe.getNodes()==null?0:this.graphe.getNodes().size();
            for(int i=0;i<n;i++){
               Node node=this.graphe.getNodes().get(i);
               nodeinfo=node.getName()+":"+node.getX()+","+node.getY();
               if(i!=n-1){
                   nodeinfo+=";";
               }
            }
            nodeinfo.trim();
            filewriter.write(nodeinfo+"\n");
            String nodeInfo;
            for(Node node:this.graphe.getNodes()){
                for(Map.Entry<Node,GraphValeur> adjacencyPair:node.getAdjacentNodes().entrySet()){
                    String str="";
                    Node destination=adjacencyPair.getKey();
                    GraphValeur valeur=adjacencyPair.getValue();
                    str=node.getName()+"->"+destination.getName()+":"+valeur.getDistance()+":"+valeur.getFlotMax()+"\n";
                    filewriter.write(str);
                }   
            }
            filewriter.close();
        } catch (Exception ex) {
         ex.printStackTrace();
        }
    }
    private void importGraph(File fichier){
       this.graphe=new Graph();
       int ligne=0;
       try{
           BufferedReader br = new BufferedReader(new FileReader(fichier)); 
           System.out.println(br);
           String st; 
           while ((st = br.readLine()) != null){ 
               st=st.trim();
             ligne++;
            
             if(ligne==1){
                 String[] listeNoeud=st.split(";");
                 for(String noeudValues:listeNoeud){
                     String[] val=noeudValues.split(":");
                     
                     Node node=new Node(val[0]);
                     String[] valCoord=val[1].split(",");
                     node.setX(Integer.parseInt(valCoord[0]));
                     node.setY(Integer.parseInt(valCoord[1]));
                     this.graphe.addNode(node);
                 }
             }else{
                 String[] values=st.split(":");
                 Node a=graphe.getNodeByName(values[0].substring(0, values[0].indexOf("-")));
                 Node b=graphe.getNodeByName(values[0].substring(values[0].indexOf(">")+1));
                 
                 GraphValeur valeur=new GraphValeur();
                 valeur.setDistance(Float.parseFloat(values[1]));
                 valeur.setFlotMax(Float.parseFloat(values[2]));
                 a.addDestination(b, valeur);
                 //a.setFloatMax(Float.parseFloat(values[2]));
             }
           } 
          
          this.graphe.triTopologique(); 
          this.createLabels();
          
          panelGraphe.repaint();
       }catch(Exception exc){
           exc.printStackTrace();
       }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try{
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choisir un texte");
                
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Texte uniquement", "txt");
		jfc.addChoosableFileFilter(filter);

		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			System.out.println(jfc.getSelectedFile().getPath());
                        this.importGraph(jfc.getSelectedFile());
		}
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.out.println("Tri topologique");
        Stack<Node> listeNode=this.graphe.triTopologique();
        while(!listeNode.isEmpty()){
            Node a=listeNode.pop();
        //    System.out.println("`"+a.toString()+"` dateDebut:"+a.dateDebut+" dateFin:"+a.dateFin);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void dateDebutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateDebutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateDebutActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        this.buttonGantt.setEnabled(!this.buttonGantt.isEnabled());
        this.buttonTaches.setEnabled(!this.buttonTaches.isEnabled());
        this.dateDebut.setEnabled(!this.dateDebut.isEnabled());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
        try{
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choisir un texte");
                
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Texte uniquement", "txt");
		jfc.addChoosableFileFilter(filter);
                
		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			System.out.println(jfc.getSelectedFile().getPath());
                        //this.importGraph(jfc.getSelectedFile());
		}
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        this.graphe.maximiserFlot();
        this.panelGraphe.repaint();
        
        
        
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boutonChemin;
    private javax.swing.JButton buttonGantt;
    private javax.swing.JButton buttonTaches;
    private javax.swing.JTextField dateDebut;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JPanel panelAjout;
    private javax.swing.JPanel panelChemin;
    private javax.swing.JPanel panelGraphe;
    private javax.swing.JTextField textDepart;
    private javax.swing.JTextField textFin;
    // End of variables declaration//GEN-END:variables
}
