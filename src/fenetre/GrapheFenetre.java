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
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

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
    public String cheminAffiche="";
    private Node tempa,tempb;
    private Node tempStringA ;
    public GrapheFenetre(Graph graphe) {
        this.graphe=graphe;
        
        initComponents();
        initGraphePosition();
        this.createDatePicker();
       
        try{
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        }catch(Exception exc){
            
        }
        this.icons();
        this.graphe.colorierGraphe();
        createLabels();
    
    }
    private void icons(){
         try {
            Image img = ImageIO.read(getClass().getResource("../resources/import.png"));
            img=img.getScaledInstance(-1, boutonImport.getHeight(),Image.SCALE_SMOOTH);
            ImageIcon ico=new ImageIcon(img);   
            boutonImport.setIcon(ico);
            
            img = ImageIO.read(getClass().getResource("../resources/save.png"));
            img=img.getScaledInstance(-1, boutonSave.getHeight(),Image.SCALE_SMOOTH);
            ico=new ImageIcon(img);   
            boutonSave.setIcon(ico);
            
            
            img = ImageIO.read(getClass().getResource("../resources/upgrade.png"));
            img=img.getScaledInstance(-1, boutonFlot.getHeight(),Image.SCALE_SMOOTH);
            ico=new ImageIcon(img);   
            boutonFlot.setIcon(ico);
            
             img = ImageIO.read(getClass().getResource("../resources/shortcut.png"));
            img=img.getScaledInstance(-1, boutonChemin.getHeight(),Image.SCALE_SMOOTH);
            ico=new ImageIcon(img);   
            boutonChemin.setIcon(ico);
            boutonChemin1.setIcon(ico);
            
            img = ImageIO.read(getClass().getResource("../resources/gantt.png"));
            img=img.getScaledInstance(-1, boutonGantt.getHeight(),Image.SCALE_SMOOTH);
            ico=new ImageIcon(img);   
            boutonGantt.setIcon(ico);
            
          } catch (Exception ex) {
            System.out.println(ex);
          }
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
            
            JButton btn=new JButton(d.getName()){
                Shape shape;    
                protected void paintComponent(Graphics g) {
                                // if the button is pressed and ready to be released
                                if (getModel().isArmed()) {
                                g.setColor(Color.lightGray);
                                } else {
                                g.setColor(getBackground());
                                }

                                g.fillOval(0, 0, getSize().width-1, getSize().height-1);

                                super.paintComponent(g);
                        }

                    // paint a round border as opposed to a rectangular one
                    protected void paintBorder(Graphics g) {
                    g.setColor(getForeground());
                    g.drawOval(0, 0, getSize().width-1, getSize().height-1);
                    }

                    // only clicks within the round shape should be accepted
                    public boolean contains(int x, int y) {
                    if (shape == null || !shape.getBounds().equals(getBounds())) {
                    shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
                    }

                    return shape.contains(x, y);
                    }
                
            };
            btn.setContentAreaFilled(false);
            btn.setBackground(d.getBackgroundColor());
            btn.setBounds(d.getX(), d.getY(), rayonNode, rayonNode);
            btn.addMouseMotionListener(new NodeMoveListener(d,panelGraphe));
            
            //lab.setVisible(false);
            panelGraphe.add(btn);
           

            
        }
       

        
    }
    private void rechercherPlusLongChemin(String depart,String fin){
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
        this.listeChemin=this.graphe.PlusCourtchemin(nodeDepart, nodeFin,false);
        this.cheminAffiche="";
        boutonAfficher();
        
        panelGraphe.repaint();
        
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
        this.listeChemin=this.graphe.PlusCourtchemin(nodeDepart, nodeFin,true);
        this.cheminAffiche="";
        boutonAfficher();
        
        panelGraphe.repaint();
        
    }
    private void ShowListeTaches(){
                // Frame initiallization 
        JFrame f;
        JTable j;        
        f = new JFrame(); 
  
        // Frame Title 
        f.setTitle("Liste des tâches"); 
        java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
        
        Timestamp debutprojet=new Timestamp(selectedDate.getTime());
        debutprojet.setHours(8);
        debutprojet.setMinutes(0);
        this.graphe.ordonnerTache(null, debutprojet);
        // Data to be displayed in the JTable 
        String[][] data = this.graphe.getDataJTable();
       
        // Column Names 
        String[] columnNames = { "Tache", "Jours","Date début","Date fin", "Predecesseur" }; 
  
        // Initializing the JTable 
        j = new JTable(data, columnNames); 
        j.setBounds(30, 40, 200, 300); 
        
        // adding it to JScrollPane 
        JScrollPane sp = new JScrollPane(j); 
        f.add(sp); 
        // Frame Size 
        f.setSize(1200, 600); 
        // Frame Visible = true 
        f.setVisible(true); 
    }
    private void boutonAfficher(){
        this.panelChemin.removeAll();
        int x=5;
        int y=15;
        //this.panelChemin.removeAll();
        for(Stack<Comparable> liste:this.listeChemin){
            String text="";
            float distance=0;
            for(Comparable n:liste){
                text+=n.toString()+"->";
                distance=((Node)n).getDistance();
            }
            //text+=distance;
            
            System.out.println("Chemin:"+text);
            JButton afficher=new JButton("Afficher");
            afficher.addActionListener(new AffichageCheminListener(this,text));
            afficher.setBounds(x+200, y-15, 150, 20);
            
            this.panelChemin.add(afficher);
           
             y+=30;
        }
        
    }
    private void dessinerChemin(Graphics g){
        //this.panelChemin.removeAll();
        int x=5;
        int y=15;
        //this.panelChemin.removeAll();
        for(Stack<Comparable> liste:this.listeChemin){
            String text="";
            float distance=0;
            for(Comparable n:liste){
                text+=n.toString()+"->";
                distance=((Node)n).getDistance();
            }
           
          
            text=text.substring(0,text.length()-2);
            System.out.println("chemin affiche:"+cheminAffiche);
              if(cheminAffiche.contains(text)){
                 
                g.setColor(Color.red);
              }
            text+=" = "+distance;
            
           // System.out.println("Chemin:"+text);
          /*  JButton afficher=new JButton("Afficher");
            
            afficher.setBounds(x+200, y-15, 150, 20);
            
            this.panelChemin.add(afficher);
           */
           
            g.drawString(text, x, y);
            g.setColor(Color.black);
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
                   System.out.println();
                  drawDirection(g,node,destination,adjacencyPair.getValue().ancienFlot,distance,flotMax);
              }
               
            }
            panelGraphe.setBackground(new java.awt.Color(51, 204, 255));
            
            //drawDirection(g,null,null,15);
             
    }
    private boolean cheminsContains(Node a,Node b){
       // System.out.println("Node :"+b.toString()+" size:"+this.listeChemin.size());
        /*for(Stack<Comparable> list:this.listeChemin){
            String text="";
            for(Comparable c:list){
                text+=c.toString()+"-";
           
            }
            if(text.contains(a.getName()+"-"+b.getName())){
                return true;
            }
            // System.out.println();
        }*/
           return (cheminAffiche.contains(a.getName()+"->"+b.getName()));
                
            
        
    }
    private void drawDirection(Graphics g,Node a,Node b,Float ancien,Float distance,Float flotMax){
        if(tempa==b && tempb==a){
            return;
        }
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
        g2.setColor(Color.GREEN);
        if(this.cheminsContains(a, b)){
            g2.setColor(Color.RED);
        }
        g2.setColor(Color.BLACK);
        if(flotMax.compareTo(distance)==0){
            g2.setColor(Color.RED);
            
        }else {
           // System.out.println("ancien:"+ancien);
           if(ancien<distance)
            g2.setColor(Color.yellow);
        }
        if(b.getAdjacentNodes().containsKey(a)){
            if(this.tempStringA!=a){
                
            
            xdist=((x+endX)/2)-30;
            ydist=((y+endY)/2)+35;
                
           
            QuadCurve2D quadCurve=new QuadCurve2D.Double();
            quadCurve.setCurve(x, y, xdist-20, ydist, endX, endY);
            g2.draw(quadCurve);
            System.out.println(b.toString()+":->"+a.getName()+"="+b.getAdjacentNodes().get(a).getFlotMax()+" /");
            String stDist=""+b.getAdjacentNodes().get(a).getDistance();
            if(b.getAdjacentNodes().get(a).getFlotMax()!=0){
                    
                stDist+="/"+b.getAdjacentNodes().get(a).getFlotMax();
            }
           
            
            stDist+=" ("+b.toString()+"->"+a.toString()+")";
           g2.drawString(stDist, xdist, ydist);
            ydist=((y+endY)/2)-55;
         
            QuadCurve2D quadCurve2=new QuadCurve2D.Double();
            quadCurve2.setCurve(x, y, xdist, ydist, endX, endY);
            g2.draw(quadCurve2);  stDist=""+distance;
            
            if(flotMax!=0){
                stDist+="/"+flotMax;
            }
            stDist+=" ("+a.toString()+"->"+b.toString()+")";
        
                
              g2.drawString(stDist, xdist, ydist);
            tempStringA=b;
            
          
            }
        }else{
           g2.drawLine(x, y, endX, endY);; 
            String stDist=""+distance;
                if(flotMax!=0){
                    stDist+="/"+flotMax;
                }
             
            g2.drawString(stDist, xdist, ydist);
        }
       
       // 
        g2.setColor(Color.GRAY);
        //g2.fillOval(x-3, y-5, 10, 10);
       
        
        //System.out.println("flot max:"+flotMax+" distance:"+distance + " "+(flotMax.compareTo(distance)==0));
        
       
        
        g2.setColor(Color.GRAY);
        
        if(graphe.isOriented()){
            //g2.setColor(a.getBackgroundColor());
            
          drawArrowHead(g2,x,y,endX,endY);
          
        }
        g2.dispose();
        
    }
    private void drawArrowHead(Graphics2D g2,int x,int y,int endX,int endY) {

        Polygon arrowHead = new Polygon();
        AffineTransform tx = new AffineTransform();
        arrowHead.addPoint(0, 5);
        arrowHead.addPoint(-7, -7);
        arrowHead.addPoint(7, -7);
        int xtemp=x;
        int ytemp=y;
        int endXtemp=endX;
        int endYtemp=endY;
        tx.setToIdentity();
        double angle = Math.atan2(endYtemp - ytemp, endXtemp - xtemp);
        tx.translate(endXtemp, endYtemp);
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
        panelControl = new javax.swing.JPanel(){
            public void paint(Graphics g){
                super.paint(g);

            }
        };
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textDepart = new javax.swing.JTextField();
        textFin = new javax.swing.JTextField();
        boutonChemin = new javax.swing.JButton();
        boutonImport = new javax.swing.JButton();
        boutonSave = new javax.swing.JButton();
        boutonFlot = new javax.swing.JButton();
        panelChemin = new javax.swing.JPanel();
        boutonChemin1 = new javax.swing.JButton();
        panelControl2 = new javax.swing.JPanel();
        boutonGantt = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

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

        panelControl.setBackground(new java.awt.Color(0, 204, 204));

        jLabel1.setText("Depart");

        jLabel2.setText("Fin");

        boutonChemin.setText("Plus court");
        boutonChemin.setName(""); // NOI18N
        boutonChemin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonCheminActionPerformed(evt);
            }
        });

        boutonImport.setLabel("Importer Graphe");
        boutonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonImportActionPerformed(evt);
            }
        });

        boutonSave.setText("Enregister");
        boutonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonSaveActionPerformed(evt);
            }
        });

        boutonFlot.setText("Maximisation Flot");
        boutonFlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonFlotActionPerformed(evt);
            }
        });

        panelChemin = new javax.swing.JPanel(){
            public void paint(Graphics g){
                super.paint(g);
                dessinerChemin(g);
            }
        };

        javax.swing.GroupLayout panelCheminLayout = new javax.swing.GroupLayout(panelChemin);
        panelChemin.setLayout(panelCheminLayout);
        panelCheminLayout.setHorizontalGroup(
            panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelCheminLayout.setVerticalGroup(
            panelCheminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 124, Short.MAX_VALUE)
        );

        boutonChemin1.setText("Plus long");
        boutonChemin1.setName(""); // NOI18N
        boutonChemin1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonChemin1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelControlLayout = new javax.swing.GroupLayout(panelControl);
        panelControl.setLayout(panelControlLayout);
        panelControlLayout.setHorizontalGroup(
            panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelChemin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelControlLayout.createSequentialGroup()
                        .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelControlLayout.createSequentialGroup()
                                .addComponent(boutonImport)
                                .addGap(43, 43, 43)
                                .addComponent(boutonSave))
                            .addGroup(panelControlLayout.createSequentialGroup()
                                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textDepart, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(46, 46, 46)
                                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(textFin, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelControlLayout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(boutonFlot))
                            .addGroup(panelControlLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(boutonChemin)
                                .addGap(32, 32, 32)
                                .addComponent(boutonChemin1)))
                        .addGap(20, 20, 20)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        panelControlLayout.setVerticalGroup(
            panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boutonSave)
                    .addComponent(boutonFlot)
                    .addComponent(boutonImport))
                .addGap(26, 26, 26)
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textDepart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(boutonChemin)
                        .addComponent(boutonChemin1)))
                .addGap(18, 18, 18)
                .addComponent(panelChemin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        boutonChemin.getAccessibleContext().setAccessibleName("");

        panelControl2.setBackground(new java.awt.Color(255, 255, 0));

        boutonGantt.setText("Ordonner tâches");
        boutonGantt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonGanttActionPerformed(evt);
            }
        });

        jLabel3.setText("Debut de  projet");

        javax.swing.GroupLayout panelControl2Layout = new javax.swing.GroupLayout(panelControl2);
        panelControl2.setLayout(panelControl2Layout);
        panelControl2Layout.setHorizontalGroup(
            panelControl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControl2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(boutonGantt)
                .addGap(179, 179, 179))
            .addGroup(panelControl2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel3)
                .addContainerGap(430, Short.MAX_VALUE))
        );
        panelControl2Layout.setVerticalGroup(
            panelControl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControl2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(boutonGantt)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelGraphe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelControl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGraphe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelControl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boutonCheminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonCheminActionPerformed
        // TODO add your handling code here:
        this.rechercherPlusCourtChemin(this.textDepart.getText().trim(), this.textFin.getText().trim());
        panelControl.repaint();
    }//GEN-LAST:event_boutonCheminActionPerformed
    private void createDatePicker(){
  
        System.out.println("datepicker");
     UtilDateModel model = new UtilDateModel();
     model.setSelected(true);
     JDatePanelImpl datePanel = new JDatePanelImpl(model);
      datePicker = new JDatePickerImpl(datePanel,new DateLabelFormatter());
     datePicker.setVisible(true);
     datePicker.setBounds(20,40,200,27);
     this.panelControl2.add(datePicker);
    }
    private void saveFile(File fichier){
        try {
            if(fichier.exists()){
            
                fichier.delete();
                fichier.createNewFile();
            }else{
                fichier.delete();
                File b=new File(fichier.getPath()+".txt");
                System.out.println("b:"+b);
                b.createNewFile();
                fichier=b;
                
            }
            
            FileWriter filewriter=new FileWriter(fichier);
            String nodeinfo="";
            int n=this.graphe.getNodes()==null?0:this.graphe.getNodes().size();
            String type="oriente";
            if(!this.graphe.isOriented()){
                type="non-oriente";
            }
            filewriter.write(type+"\n");
            for(int i=0;i<n;i++){
               Node node=this.graphe.getNodes().get(i);
               nodeinfo+=node.getName()+":"+node.getX()+","+node.getY();
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
       String type="";
       try{
           BufferedReader br = new BufferedReader(new FileReader(fichier)); 
           //System.out.println(br);
           String st; 
           while ((st = br.readLine()) != null){ 
               st=st.trim();
             ligne++;
            
             if(ligne==1){
                 type=st;
                 if(type.equals("non-oriente")){
                     this.graphe.setOriented(false);
                 }else{
                     this.graphe.setOriented(true);
                 }
             }
             else if(ligne==2){
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
                 
                 GraphValeur valeur=new GraphValeur(Float.parseFloat(values[1]),Float.parseFloat(values[2]));
               
                 a.addDestination(b, valeur);
                 if(type.equals("non-oriente")){
                   b.addDestination(a, valeur);
                 }
                 //a.setFloatMax(Float.parseFloat(values[2]));
             }
           } 
          
          this.graphe.colorierGraphe();
          this.createLabels();
          
          panelGraphe.repaint();
          
       }catch(Exception exc){
           exc.printStackTrace();
       }
    }
    private void boutonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonImportActionPerformed
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
    }//GEN-LAST:event_boutonImportActionPerformed

    private void boutonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonSaveActionPerformed
        // TODO add your handling code here:
        
        try{   
             
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choisir un texte");
                
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Texte uniquement", "txt");
		jfc.addChoosableFileFilter(filter);
                
		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			System.out.println(jfc.getSelectedFile().getPath());
                        this.saveFile(jfc.getSelectedFile());
		}
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }//GEN-LAST:event_boutonSaveActionPerformed

    private void boutonFlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonFlotActionPerformed
        // TODO add your handling code here:
        this.graphe.maximiserFlot();
        this.panelGraphe.repaint();
        
        
        
    }//GEN-LAST:event_boutonFlotActionPerformed

    private void boutonGanttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonGanttActionPerformed
        // TODO add your handling code here:
        this.ShowListeTaches();
        GanttChart example=new GanttChart("Taches",graphe);
         example.setSize(1200, 800);
         example.setLocationRelativeTo(null);
         //example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         example.setVisible(true);
    }//GEN-LAST:event_boutonGanttActionPerformed

    private void boutonChemin1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonChemin1ActionPerformed
        // TODO add your handling code here:
        this.rechercherPlusLongChemin(this.textDepart.getText().trim(), this.textFin.getText().trim());
        panelControl.repaint();
    }//GEN-LAST:event_boutonChemin1ActionPerformed

    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boutonChemin;
    private javax.swing.JButton boutonChemin1;
    private javax.swing.JButton boutonFlot;
    private javax.swing.JButton boutonGantt;
    private javax.swing.JButton boutonImport;
    private javax.swing.JButton boutonSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel panelChemin;
    private javax.swing.JPanel panelControl;
    private javax.swing.JPanel panelControl2;
    private javax.swing.JPanel panelGraphe;
    private javax.swing.JTextField textDepart;
    private javax.swing.JTextField textFin;
    // End of variables declaration//GEN-END:variables
    private JDatePickerImpl datePicker;
    
    
   static class MyTableModel extends DefaultTableModel {

    List<Color> rowColours = Arrays.asList(
        Color.RED,
        Color.GREEN,
        Color.CYAN
    );

    public void setRowColour(int row, Color c) {
        rowColours.set(row, c);
        fireTableRowsUpdated(row, row);
    }

    public Color getRowColour(int row) {
        return rowColours.get(row);
    }

    @Override
    public int getRowCount() {
        return 3;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return String.format("%d %d", row, column);
    }
}
 
}

