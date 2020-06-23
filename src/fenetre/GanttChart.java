/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetre;

import graphe.Graph;
import graphe.Node;
import java.awt.Color;
import java.awt.Paint;
import java.awt.SystemColor;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import org.jfree.data.gantt.Task;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarPainter;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.TimePeriod;

/**
 *
 * @author toky
 */
public class GanttChart extends JFrame {
    
   private Graph graphe; 
   public GanttChart(String title,Graph graphe) {
      super(title);
         try{
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception exc){
            
        }
      this.graphe=graphe;
      // Create dataset
      IntervalCategoryDataset dataset = getCategoryDataset();
    
      // Create chart
      JFreeChart chart = ChartFactory.createGanttChart(
            "Liste des taches", // Chart title
            "Tâches", // X-Axis Label
            "Date", // Y-Axis Label
            dataset);
      
      ChartPanel panel = new ChartPanel(chart);
      setContentPane(panel);
     
               CategoryPlot cplot = (CategoryPlot)chart.getPlot();
    cplot.setBackgroundPaint(SystemColor.inactiveCaption);//change background color

    //set  bar chart color
    Paint[] colors = new Paint[this.graphe.getNodes().size()];
    for (int i = 0; i < colors.length; i++) {
        colors[i] =  Color.blue ;
    }
    //cplot.setRenderer(new  CustomRenderer(colors));
    StandardBarPainter p=new StandardBarPainter();
    
    ((BarRenderer)cplot.getRenderer()).setBarPainter(new StandardBarPainter());

    BarRenderer r = (BarRenderer)chart.getCategoryPlot().getRenderer();
    r.setSeriesPaint(0, Color.YELLOW);
    r.setSeriesPaint(1, Color.RED);
   }
      private IntervalCategoryDataset getCategoryDataset() {
      
      
      TaskSeries series1 = new TaskSeries("Date plus Tot");
      List<Node> liste=this.graphe.triNoeudParDateDebut();
      int i=0;
     // Task sub=new Task("Sous taches",new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()+2*3600*1000*24));
     Task parent=null;
     Node nodeparent=null;
     List<Node> listeFin=this.graphe.noeudSansSuccesseur();
     System.out.println(listeFin);
     for(Node node:liste){
          Task task=new Task(node.getName(),node.getDateDebut(),node.getDateFin());
          
          if(listeFin.contains(node)){
              Timestamp t=new Timestamp(node.getDateDebut().getTime()-3*3600*1000);
              t.setHours(node.getDateDebut().getHours()-1);
            task=new Task(node.getName(),t,node.getDateFin());
            
          }
         /* if(parent!=null){
              if(node.getPredecesseurNoeud().contains(nodeparent)){
                  parent.addSubtask(task);
              }
          }
          
          parent=task;*/
          nodeparent=node;
          series1.add( task);
        
      }
      TaskSeriesCollection dataset = new TaskSeriesCollection();
      
      dataset.add(series1);
      
      return dataset;
      }
          class CustomRenderer extends BarRenderer {

        /** The colors. */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors  the colors.
         */
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }
    
}
