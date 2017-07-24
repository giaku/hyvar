package reply.speadometer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JPanelDrawAnimation extends JFrame
{
	//inner Class
	public class MyJPanel extends JPanel
	{
	    public int x 	= 10;	//Start Drawing from X=10	
	    public int delay 	= 500; 	//milliseconds
	    MyJPanel()
	    {
		 ActionListener counter = new ActionListener() {
			public void actionPerformed(ActionEvent evt) 
			{ 
			      repaint();
			      x++;
			}};
		 new Timer(delay, counter).start();
	    }

	    public void paintComponent(Graphics g)
	    {
		super.paintComponent(g);
		//super.paintComponent(g1);
		
		 int fontSize = 70;

		    g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
		     
		    g.setColor(Color.red);
		 
		//g.setColor(Color.black);
		//g.fillRect(x,40,50,50);
		//g.drawLine(10, 40, 10, 60);
		//g.drawLine(10, 60, 30, 60);
		g.draw3DRect(40, 50, 100, 100, true);
		
		g.drawString("1",80, 110);
		g.drawString("Gear Advise",40,50);
		
		//g.setFont(Font.ITALIC);
		//g.setColor(Color.black);

		/*g.setColor(Color.red);
		g.fillRect(x,100,50,50);

		g.setColor(Color.green);
		g.fillRect(x,160,50,50);

		g.setColor(Color.blue);
		g.fillRect(x,220,50,50);

		g.setColor(Color.yellow);
		g.fillRect(x,280,50,50);*/

	    }
	
	}

	public static void main(String args [])
	{	      
	  new JPanelDrawAnimation ();		
	}

	JPanelDrawAnimation ()
	{
	    Container container = getContentPane();
	    MyJPanel panel = new MyJPanel();
	    container.add(panel);	  
	    
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(400,400);
	    setTitle("JPanel Draw Rect Animation");
	    setVisible(true);
	}
}
