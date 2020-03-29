/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetre;

import graphe.Graph;
import graphe.Node;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import org.jfree.data.gantt.Task;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

/**
 *
 * @author toky
 */
public class GanttChart extends JFrame {
    
   private Graph graphe; 
   public GanttChart(String title,Graph graphe) {
      super(title);
  
      this.graphe=graphe;
      // Create dataset
      IntervalCategoryDataset dataset = getCategoryDataset();
    
      // Create chart
      JFreeChart chart = ChartFactory.createGanttChart(
            "Gantt Chart Example | WWW.BORAJI.COM", // Chart title
            "Software Development Phases", // X-Axis Label
            "Timeline", // Y-Axis Label
            dataset);

      ChartPanel panel = new ChartPanel(chart);
      setContentPane(panel);
            try{
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception exc){
            
        }
   }
      private IntervalCategoryDataset getCategoryDataset() {
      
      
      TaskSeries series1 = new TaskSeries("Date plus Tot");
      for(Node node:this.graphe.getNodes()){
          Task task=new Task(node.getName(),node.getDateDebut(),node.getDateFin());
          
          series1.add( task);
      }
      TaskSeriesCollection dataset = new TaskSeriesCollection();
     
      dataset.add(series1);
      
      return dataset;
      }
    
}
