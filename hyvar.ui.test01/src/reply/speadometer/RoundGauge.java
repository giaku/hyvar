package reply.speadometer;

import java.awt.Dimension;
import javax.swing.JFrame;
/*
 *  this code started 15JUN2007 17:10 pm
 *  finished          15JUN2007 23:07 pm
 */
public class RoundGauge {
  
RoundGaugePanel rp;
Dimension size = new Dimension( 500, 500);
 
public RoundGauge(){
rp = new RoundGaugePanel( size );
JFrame frame = new JFrame("RoundGaugePanel Test" );
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setSize(size);
frame.setMaximumSize(size);
frame.setPreferredSize(size);
frame.add(rp);
frame.pack();
frame.setVisible(true);
}
 
public void updateValue( int i) { rp.updateValue( i ); }
public void setMaxValue( int i) { rp.setMaxValue(i); }
 
public static void main(String[] args) {
RoundGauge r = new RoundGauge();
r.setMaxValue(10000);
// I'm not messing with the event dispatch thread here 'cause
// nothing touches the swing components at all....one of them
// reads an integer variable I'm modifying but that's okay
for( int i = 0; i < 99; i++) {
try {
r.updateValue(i * 100 );
Thread.sleep(100);
} catch(InterruptedException ie) {/**/}
}
} // close main
} // close class RoundGauge
