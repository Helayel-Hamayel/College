


package asdf2;


import java.awt.*;
import java.awt.event.*;
import java.util.EventListener;
import javax.swing.*;
import static javax.swing.JOptionPane.*;
import javax.swing.event.*;

public class Asdf2 extends JFrame implements ActionListener,MenuListener,FocusListener,WindowListener{

    JMenu One,Two;
    JMenuItem Bold,Italic,Red,Exit;
    JButton CORR,INCORR,TEST;
    JButton Ok,Cancel;
    JMenuBar B;
    
    Icon OK=new ImageIcon("C:\\Users\\Sgm.Jackson037\\Desktop\\OK-512.png");
    Icon CROSS=new ImageIcon("C:\\Users\\Sgm.Jackson037\\Desktop\\cross-512.png");
   
    public Asdf2()
    {
        
    super("FUKYO");
    
    B=new JMenuBar();
    setJMenuBar(B);
    
    One=new JMenu("FILE");
    Two=new JMenu("THING");
    
    Bold=new JMenuItem("Bold");
    Italic=new JMenuItem("Italic");
    Red=new JMenuItem("Red");
    Exit=new JMenuItem("Exit");

    CORR=new JButton("CORRECT");
    INCORR=new JButton("INCORRECT");
    TEST=new JButton("BOTH");
    
    
    Ok=new JButton("OKEY");
    Cancel=new JButton("Cancel");
    
    B.add(One);
    B.add(Two);
    //B.add(Ok);
    
    One.add(Bold);
    //One.add(Italic);
    One.add(Red);
    //One.addSeparator();
    One.add(Exit);
    //One.add(Cancel);
    
    Two.add(TEST);
    Two.add(CORR);
    Two.add(INCORR);
    
    
    TEST.addFocusListener(this);
    CORR.addFocusListener(this);
    INCORR.addFocusListener(this);
    
    Exit.addActionListener(this);
    Bold.addActionListener(this);
    Red.addActionListener(this);
    
    addWindowListener(this);
    addWindowFocusListener(new WindowAdapter()
    {
    public void windowGainedFocus(WindowEvent e)
    {
     System.out.println("window has Gained Focus\n");
    }
   
    public void windowLostFocus(WindowEvent e)
    {
    System.out.println("window has Lost Focus\n");
    }
    
    });
    
    JMenuBar x=getJMenuBar();
    int count=x.getMenuCount();
    
    for(int i =0; i <count;i++)
    {
        x.getMenu(i).addMenuListener(this);
    } //connecting all JMenu to Menu LISTENER

    setSize(600,300);
    setLocation(200,350);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
    } //end of constructor
 
    public static void main(String[] args) 
    {
        new Asdf2();
    }

    
    //MENU LISTENER
    
    @Override
     public void menuSelected(MenuEvent e) 
    {
     getContentPane().setBackground(Color.RED);
    }
      
     @Override
     public void menuDeselected(MenuEvent e) 
     {
      getContentPane().setBackground(Color.BLUE);
     }
    
    @Override
    public void menuCanceled(MenuEvent e) {
      
    }
     
    //END OF MENU LISTENER
     
     
     
    //ACTION LISTENER
     
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object M = e.getSource();
        if(M==Exit)
        {
            dispose();
        }
        
        else if(M == Bold)
        {
            
            Component A[]=getJMenuBar().getComponents();
            JMenu z=(JMenu)A[0];

            Font K=z.getFont();
            int count = z.getItemCount();
            
            for(int i=0;i<count;i++)
            {
                JMenuItem R=z.getItem(i);
                R.setFont(new Font(null,Font.BOLD,20));
            }
           
        }
        
        else if(M==Red)
        {
            
            JMenu z=getJMenuBar().getMenu(0);
            int count = z.getItemCount();
            for(int i=0;i<count;i++)
            { 
                //JMenuItem R=z.getItem(i);
                z.getItem(i).setForeground(Color.RED); 
            }
        }
        
    //END OF ACTION LISTENER
        
    
    //FOCUS LISTENER
        
    }

    @Override
    public void focusGained(FocusEvent e) {
        
        if(e.getSource()==TEST)
     {
     ((JButton)CORR).setIcon(OK);
     ((JButton)INCORR).setIcon(OK);
     
     }
        
     ((JButton)e.getSource()).setIcon(OK);
    }

    @Override
    public void focusLost(FocusEvent e) {
     
      ((JButton)e.getSource()).setIcon(CROSS);
    }
    
    //END OF FOCUS LISTENER

    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("window is Opened\n");
    }

    @Override
    public void windowClosing(WindowEvent e) {
     System.out.println("window is Closing\n");
    }

    @Override
    public void windowClosed(WindowEvent e) {
       System.out.println("window is Closed\n");
    }

    @Override
    public void windowIconified(WindowEvent e) {
      System.out.println("window is Iconified\n");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
      System.out.println("window is Deiconified\n");
    }

    @Override
    public void windowActivated(WindowEvent e) {
         System.out.println("window is Activated\n");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        System.out.println("window is Deactivated\n");
    }
    

    

   
    
}
