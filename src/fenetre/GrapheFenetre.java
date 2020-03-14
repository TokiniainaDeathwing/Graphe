/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetre;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
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

        javax.swing.GroupLayout panelCheminLayout = new javax.swing.GroupLayout(panelChemin);
        panelChemin.setLayout(panelCheminLayout);
        panelCheminLayout.setHorizontalGroup(
            panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCheminLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCheminLayout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addContainerGap())
                    .addGroup(panelCheminLayout.createSequentialGroup()
                        .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textDepart, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(47, 47, 47)
                        .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCheminLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(134, 134, 134))
                            .addGroup(panelCheminLayout.createSequentialGroup()
                                .addComponent(textFin, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addComponent(boutonChemin)
                                .addContainerGap(20, Short.MAX_VALUE))))))
        );
        panelCheminLayout.setVerticalGroup(
            panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCheminLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(26, 26, 26)
                .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textDepart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boutonChemin))
                .addContainerGap(147, Short.MAX_VALUE))
        );

        boutonChemin.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelGraphe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelChemin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelChemin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(panelGraphe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boutonCheminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonCheminActionPerformed
        // TODO add your handling code here:
        this.rechercherPlusCourtChemin(this.textDepart.getText().trim(), this.textFin.getText().trim());
        panelChemin.repaint();
    }//GEN-LAST:event_boutonCheminActionPerformed
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
           
          this.createLabels();
          panelGraphe.repaint();
       }catch(Exception exc){
           exc.printStackTrace();
       }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try{
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
            System.out.println("`"+a.toString()+"` dateDebut:"+a.dateDebut+" dateFin:"+a.dateFin);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boutonChemin;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel panelChemin;
    private javax.swing.JPanel panelGraphe;
    private javax.swing.JTextField textDepart;
    private javax.swing.JTextField textFin;
    // End of variables declaration//GEN-END:variables
}
