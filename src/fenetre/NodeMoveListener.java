/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetre;

import graphe.Node;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author toky
 */
public class NodeMoveListener implements MouseMotionListener,MouseListener {

    private Node node;
    private int previousx=0;
    private int previousy=0;
    private JPanel panel;
    private boolean start=true;
    private volatile int draggedAtX, draggedAtY;
    public NodeMoveListener(Node node,JPanel panel) {
        this.node = node;
        this.panel=panel;
        previousx=node.getX();
        previousy=node.getY();
    }
    
   
    private void modifierPosition(MouseEvent e){
        

    }

    @Override
    public void mouseDragged(MouseEvent e) {
   
       // System.out.println("Mouse dragged");
      
        int x=e.getX();
        int y=e.getY();
   

        JButton lab=(JButton)e.getSource();
                  lab.setLocation(e.getX() - draggedAtX + lab.getLocation().x,
               e.getY() - draggedAtY + lab.getLocation().y);
        //lab.setLocation(e.getPoint());
        node.setX(lab.getLocation().x);
        node.setY(lab.getLocation().y);
        panel.repaint();
        
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
           // System.out.println("Mouse Moved");
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
                 draggedAtX = e.getX();
                draggedAtY = e.getY();
  }

    @Override
    public void mouseReleased(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}
