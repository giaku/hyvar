package hyvar.ui.test01;

import java.awt.event.*;
import javax.swing.*;

//////////////////////////////////////////////////////////////////////////
public class Animation2 extends GlgJBean implements ActionListener
{
   //////////////////////////////////////////////////////////////////////////
   // The main demo class
   //////////////////////////////////////////////////////////////////////////
   static final long serialVersionUID = 0;

   static final int NUM_VALUES = 25;
   boolean PerformUpdates = true;
   GlgAnimationValue [] animation_array = new GlgAnimationValue[ NUM_VALUES ];
   static boolean AntiAliasing = true;

   Timer timer = null;

   //////////////////////////////////////////////////////////////////////////
   public Animation2()
   {
      super();
      SetDResource( "$config/GlgAntiAliasing", AntiAliasing ? 1.0 : 0.0 );
   }

   //////////////////////////////////////////////////////////////////////////
   // Starts updates
   //////////////////////////////////////////////////////////////////////////
   public void ReadyCallback( GlgObject viewport )
   {
      super.ReadyCallback( viewport );

      InitializeArrays();
      
      if( timer == null )
      {
         // Restart the timer after each update (instead of using repeats)
         // to avoid flooding the event queue with timer events on slow 
         // machines.
         timer = new Timer( 350, this );
         timer.setRepeats( false );
         timer.start();
      }
   }

   //////////////////////////////////////////////////////////////////////////
   // For use as a stand-alone java demo
   //////////////////////////////////////////////////////////////////////////
   public static void main( final String [] arg )
   {
      SwingUtilities.
        invokeLater( new Runnable(){ @Override
		public void run() { Main( arg ); } } );
   }

   //////////////////////////////////////////////////////////////////////////
   public static void Main( final String [] arg )
   {
      class DemoQuit extends WindowAdapter
      {
         @Override
		public void windowClosing( WindowEvent e ) { System.exit( 0 ); }
      } 

      JFrame frame = new JFrame( "HyVar Dashboard DEMO" );

      frame.setResizable( true );
      frame.setSize( 600, 600 );
      frame.setLocation( 20, 20 );

      Animation2 controls = new Animation2();
      frame.getContentPane().add( controls );

      frame.addWindowListener( new DemoQuit() );
      frame.setVisible( true );

      // Assign a drawing filename after the frame became visible and 
      // determined its client size to avoid unnecessary resizing of 
      // the drawing.
      // Loading the drawing triggers ReadyCallback which starts updates.
      //
      //controls.SetDrawingName( "C:\\eclipseHyVar\\mila_test01\\hyvar.ui.test01\\src\\hyvar\\ui\\test01\\controls_updated.g" );
      controls.SetDrawingName( "C:\\eclipseHyVar\\mila_test01\\hyvar.ui.test01\\src\\hyvar\\ui\\test01\\003.g" );
      //controls.SetDrawingName( "C:\\eclipseHyVar\\mila_test01\\hyvar.ui.test01\\src\\hyvar\\ui\\test01\\controls_1.g" );
   }

   //////////////////////////////////////////////////////////////////////////
   public void UpdateControls()
   { 
      if( timer == null )
        return;   // Prevents race conditions

      if( PerformUpdates )
      {
         // Update all animation_values
         for( int i=0; i < NUM_VALUES; ++i )
           if( animation_array[ i ] != null )
             animation_array[ i ].Iterate();

         Update();   // Show changes
      }

      timer.start();   // Restart the update timer
   }

   //////////////////////////////////////////////////////////////////////////
   public void StartUpdate()
   {
      if( !PerformUpdates )
      {
         PerformUpdates = true;
         SetDResource( "ManualMode", 0.0 );
         Update();
      }
   }

   //////////////////////////////////////////////////////////////////////////
   public void StopUpdate()
   {
      if( PerformUpdates )
      {
         PerformUpdates = false;
         SetDResource( "ManualMode", 1.0 );
         Update();
      }
   }

   //////////////////////////////////////////////////////////////////////////
   public void ToggleAntiAliasing()
   {
      AntiAliasing = !AntiAliasing;
      SetDResource( "$config/GlgAntiAliasing", AntiAliasing ? 1.0 : 0.0 );

      // Restart with the new AntiAliasing setting
      stop();
      start();
   }

   //////////////////////////////////////////////////////////////////////////
   void InitializeArrays()
   {
      // Initilize simulation controlling parameters

      /* 3 top gages */
     animation_array[ 0 ] =
        new GlgAnimationValue( this, GlgAnimationValue.SIN,
                              0, 47, 10.0, 250.0, "Seedometer/Value" );
     
     animation_array [1] = 
    	new GlgAnimationValue(this, GlgAnimationValue.RANDOM,
				0,1,1,6,"TextBoxMi1/Value");
     
     animation_array [2] = new GlgAnimationValue(this, GlgAnimationValue.RANDOM,
				0,1,0,1,"Gear/Visibility");
     /*animation_array[ 2 ] =
    	        new GlgAnimationValue( this, GlgAnimationValue.RANDOM,
    	                              0, 100, 0.0, 3.0, "Light1/Value" );
      /*animation_array [1] = new GlgAnimationValue(this, GlgAnimationValue.RANDOM,
    		  					0,1,1,6,"Element/Value");*/
    
   }


   //////////////////////////////////////////////////////////////////////////
   // ActionListener method to use the bean as update timer's ActionListener.
   //////////////////////////////////////////////////////////////////////////
   @Override
public void actionPerformed( ActionEvent e )
   {
      UpdateControls();
   }

   //////////////////////////////////////////////////////////////////////////
   // Handles user input: in this demo, it's pressing the start and stop 
   // buttons.
   //////////////////////////////////////////////////////////////////////////
   public void InputCallback( GlgObject vp, GlgObject message_obj )
   {
      String
        origin,
        format,
        action;

      super.InputCallback( vp, message_obj );

      origin = message_obj.GetSResource( "Origin" );
      format = message_obj.GetSResource( "Format" );
      action = message_obj.GetSResource( "Action" );
      // subaction = message_obj.GetSResource( "SubAction" );

      if( action.equals( "ValueChanged" ) )
      {
         // Stop automatic updates to let the user play with controls
         // when a control's value is adjusted with the mouse.
         StopUpdate();
      }
      else if( format.equals( "Button" ) )
      {
         if( !action.equals( "Activate" ) )
           return;
         
         if( origin.equals( "Start" ) )
           StartUpdate();
         else if( origin.equals( "Stop" ) )
           StopUpdate();
      }

      Update();
   }

   //////////////////////////////////////////////////////////////////////////
   public void StopUpdates()
   {
      if( timer != null )
      {
         timer.stop();
         timer = null;
      }
   }

   //////////////////////////////////////////////////////////////////////////
   // Invoked by the browser to stop the applet
   //////////////////////////////////////////////////////////////////////////
   public void stop()
   {
      StopUpdates();
      super.stop();
   }

   //////////////////////////////////////////////////////////////////////////
   // Inner class for a Runnable interface.
   // Provides an interface between JavaScript and Java: invokes applet's 
   // methods in a synchronous way as required by Swing.
   //////////////////////////////////////////////////////////////////////////
   class GlgBeanRunnable implements Runnable
   {
      Animation2 bean;
      String request_name;
      int value;

      public GlgBeanRunnable( Animation2 bean_p, 
                             String request_name_p, int value_p )
      {
         bean = bean_p;
         request_name = request_name_p;
         value = value_p;
      }

      @Override
	public void run()
      {
         if( request_name.equals( "Start" ) )
           bean.StartUpdate();
         else if( request_name.equals( "Stop" ) )
           bean.StopUpdate();
         else if( request_name.equals( "ToggleAntiAliasing" ) )
           bean.ToggleAntiAliasing();
         else
           PrintToJavaConsole( "Invalid request name: " + 
                              request_name + "\n" );
      }
   }

   //////////////////////////////////////////////////////////////////////////
   // Provides an interface between JavaScript and Java: invokes applet's 
   // methods in a synchronous way as required by Swing.
   //////////////////////////////////////////////////////////////////////////
   public void SendRequest( String request_name, int value )
   {
      GlgBeanRunnable runnable = 
        new GlgBeanRunnable( this, request_name, value );

      SwingUtilities.invokeLater( runnable );
   }
}

