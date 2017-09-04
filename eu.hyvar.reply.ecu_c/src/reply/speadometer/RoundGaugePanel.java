package reply.speadometer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
  
import javax.swing.*;
  
public class RoundGaugePanel extends JPanel implements Runnable {
 
int targetValue = 0;
int MAX_VALUE = 100; // default to 100 for now
int value = 0;
Thread repaintThread;
int degreesPerSecond = 1;
Dimension Size;
double gaugeWidth;
double gaugeHeight;
int    centerX = (int)(gaugeWidth/2.0);
int    centerY = (int)(gaugeHeight/2.0);
double zeroAngle = 225.0;
double maxAngle  = -45; 
double range = zeroAngle - maxAngle;
 
RoundGaugePanel(Dimension size) {
Size = size;
gaugeWidth = Size.width  * 0.75;
gaugeHeight = Size.height * 0.75;
centerX = (int)(gaugeWidth/2.0);
centerY = (int)(gaugeHeight/2.0);
setSize(Size);
setMaximumSize(Size);
setPreferredSize(Size);
repaintThread = new Thread( this );
repaintThread.setDaemon(true);
repaintThread.start();
}
 
public void updateValue( int i ){ targetValue = i; }
public void setMaxValue( int i) { MAX_VALUE = i; }
 
private void paintTheBackground( Graphics g) {
g.setColor(Color.black);
g.fillRect(0, 0, Size.width, Size.height);
g.setColor(Color.white);
g.fillOval(0, 0, (int)gaugeWidth, (int)gaugeHeight);
// now the lines and the arcs on the gauge
g.setColor( Color.lightGray);
g.drawLine(centerX, centerY, (int) gaugeWidth -25, (int)gaugeHeight-25);
g.drawLine(centerX, centerY, 23, (int)gaugeHeight-25);
g.setColor( Color.blue);
g.drawArc( 10, 10, (int)gaugeWidth-20, (int)gaugeHeight-20, -45, 270);
// yellow doesn't show up really well on a white background
//g.setColor( Color.yellow);
//g.drawArc( 10, 10, (int)gaugeWidth-20, (int)gaugeHeight-20, 0, 45);
// this red line doesn't add much to the gauge so....
//g.setColor( Color.red);
//g.drawArc( 10, 10, (int)gaugeWidth-20, (int)gaugeHeight-20, -45, 45 );
g.setColor( Color.black);
g.drawString("0%", centerX - 45, centerY + 55);
g.drawString("50%", centerX - 5, centerY - 50);
g.drawString("100%", centerX + 15, centerY + 55);
 
}
 
@Override
public void paintComponent(Graphics g){
Color oldColor;
oldColor = g.getColor();
paintTheBackground( g);
  
g.setColor(Color.red);
int x1 = centerX, 
    x2 = x1, 
    y1 = centerY, 
    y2 = y1;
double angleToUse = zeroAngle - 1.0 * range *( value * 1.0 / MAX_VALUE * 1.0);
    x2 += (int)( Math.cos(Math.toRadians(angleToUse))*centerX);
    y2 -= (int)( Math.sin(Math.toRadians(angleToUse))*centerY);
g.drawLine(x1, y1, x2, y2 );
g.setColor(Color.black);
g.drawString(""+ value, centerX - 10, centerY + 30);
g.setColor(oldColor);
}
@Override
public void run() {
int oldValue = 32768;
while ( true ) {
try {
Thread.sleep(100);
} catch( InterruptedException ie) { /**/ }
 
if ( targetValue != value ) {
if ( degreesPerSecond < Math.abs(targetValue - value) ) value = targetValue;
else if ( targetValue < value ) value -= degreesPerSecond;
else if ( targetValue > value ) value += degreesPerSecond;
}
 
if( oldValue != value) {
repaint();
oldValue = value;
}
} // close while()
} // close run()
} // close class RoundGaugePanel
