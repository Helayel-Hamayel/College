
package hm1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class HM1 extends JFrame implements ActionListener,KeyListener,ItemListener {

    JButton cal,exit;
    JLabel L1,L2;
    JTextField f;
    JTextArea ar,ar2;
    JRadioButtonMenuItem Octo, Bina,Deca,Hexa;
    JMenuBar B;
    
    public HM1()
    {
    
    super("Converter");
    cal=new JButton("Calculate");
    exit=new JButton("exit");
    L1=new JLabel("Insert an input : ");
    L2=new JLabel("[ Results ]=>");
    
    
    ar=new JTextArea(10,30);
    ar.setText("\t\n\n\n\n\t      ....awaiting for input....");
    ar.setForeground(new Color(255,50,90));
    ar.setEditable(false);
    ar.setLineWrap(true);
    
    f=new JTextField(16);
    f.setText("");
     
    //blue  050-100-255
    //red   255-050-000

    cal.setToolTipText("**START CONVERTING**");
    exit.setToolTipText("**EXIT THE PROGRAM**");
    f.setToolTipText("**Insert an input**");
     
    Bina = new JRadioButtonMenuItem("Binary",true);
    Octo = new JRadioButtonMenuItem("Octal");       
    Deca = new JRadioButtonMenuItem("Decimal");
    Hexa = new JRadioButtonMenuItem("HexaDecimal");

    //B=new JMenuBar();
    //setJMenuBar(B);
    
    ButtonGroup SL=new ButtonGroup();
    SL.add(Bina);
    SL.add(Octo);
    SL.add(Deca);
    SL.add(Hexa);
    
    JPanel S,N,CE,W;
     
    S=new JPanel(); N=new JPanel(); CE=new JPanel(); W=new JPanel(); 
    N.add(L1); 
    N.add(f); 
    N.add(cal);
    
    /*
    B.add(Bina);
    B.add(Octo);
    B.add(Deca);
    B.add(Hexa);
    */ 
     /*note to self : 
     if you want to use the above make sure to remove the four below 
     AND the one of JMENUBAR
     */
    
    CE.add(Bina);
    CE.add(Octo);
    CE.add(Deca);
    CE.add(Hexa);
    
    
    
    CE.add(L2);

    CE.add(ar); 
     
    S.add(exit);
     
    add(CE,BorderLayout.CENTER);
    add(N,BorderLayout.NORTH);
    add(W,BorderLayout.WEST);
    add(S,BorderLayout.SOUTH);
     
    setSize(500,300);
    setResizable(false);
    setLocation(450,150);
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
     
    cal.addActionListener(this);
    exit.addActionListener(this);
    f.addActionListener(this);
     
    f.addKeyListener(this);
    cal.addKeyListener(this);
     
   
     
    Bina.addItemListener(this);
    Octo.addItemListener(this);
    Deca.addItemListener(this);
    Hexa.addItemListener(this);
     
     
    f.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) 
        {
            f.setForeground(new Color(0,155,100));
            cal.doClick();
        }

        @Override
        public void removeUpdate(DocumentEvent e) 
        {
            f.setForeground(new Color(250,150,10));
            cal.doClick(); 
        }

        @Override
        public void changedUpdate(DocumentEvent e) 
        {
            f.setForeground(new Color(0,155,100));
            cal.doClick();
        }

     });

    }//end of constructor
    
    
    
    public static void main(String[] args) 
    {
        new HM1();
    }
    
    
    //ACTION LISTENER
    
    public void actionPerformed(ActionEvent e) 
    {
       Object m=e.getSource();
       
       if(m==exit)
       {
       dispose();
       }
       
       else if(m == cal || m==f)
       {
        String S=f.getText();
        S=S.toUpperCase();
 
          //to prevent the integer taking an "empty-value" and thus causes to crash so it given with the zero value instead of giving an "empty-value"
          if(S.isEmpty())
          {
           S="0";
          }
          
           
          if(Hexa.isSelected())
          {

            int n1=Integer.parseInt(S,16);
               
            ar.setForeground(Color.BLUE);
            
            ar.setText("you have inserted a HexaDecimal => "+S.toUpperCase());
            ar.append("\n\n(2).Binary => "+Integer.toBinaryString(n1));
            ar.append("\n(8).Octal => "+Integer.toOctalString(n1));
            ar.append("\n(10).Decimal => "+n1);

         }
           
          
           
          else if(Deca.isSelected()&&!(S.contains("A") ||S.contains("B") ||S.contains("C") || S.contains("D") ||S.contains("E") || S.contains("F")))
          {
            int n2=Integer.parseInt(S);
                
            ar.setForeground(Color.BLUE);
            
            ar.setText("you have inserted a Decimal => "+S);   
            ar.append("\n\n(02).Binary => "+Integer.toBinaryString(n2));
            ar.append("\n(08).Octal => "+Integer.toOctalString(n2));
            ar.append("\n(16).HexaDecimal => "+Integer.toHexString(n2).toUpperCase());
    
          }
            
          else if(Octo.isSelected()&&!(S.contains("8") || S.contains("9") ||S.contains("A") ||S.contains("B") ||S.contains("C") || S.contains("D") ||S.contains("E") || S.contains("F") ))
          {
            
            int dec=Integer.parseInt(S,8);
               
            ar.setForeground(Color.BLUE);
            
            ar.setText("you have inserted a Octal => "+S); 
            ar.append("\n\n(2).Binary => "+Integer.toBinaryString(dec));
            ar.append("\n(10).Decimal => "+Integer.parseInt(S,8));
            ar.append("\n(16).HexaDecimal => "+Integer.toHexString(dec).toUpperCase());
               
          }
           
          else if (Bina.isSelected()&&(S.contains("0") || S.contains("1")))
          {
            int Decimal=Integer.parseInt(S,2);
              
            ar.setForeground(Color.BLUE);
            
            ar.setText("you have inserted a Binary input => "+S);  
            ar.append("\n\n(08).Octal => "+Integer.toOctalString(Decimal));
            ar.append("\n(10).Decimal => "+Decimal);
            ar.append("\n(16).Hexa-Decimal => "+Integer.toHexString(Decimal).toUpperCase());
          
          }
               
       }

    }
    
    //END OF ACTION LISTENER

    
    //KEY LISTENER
    
    @Override
    public void keyTyped(KeyEvent e) 
    {
        
      String ST;
      ST=f.getText();
        
       if(Bina.isSelected() && (e.getKeyChar()>='0' && e.getKeyChar()<='1') )
       {
           cal.doClick();
       }
        else if(Octo.isSelected() && (e.getKeyChar()>='0' && e.getKeyChar()<='7' ) )
        {
           cal.doClick();
        }
        else if(Deca.isSelected() && (e.getKeyChar()>='0' &&  e.getKeyChar()<='9' ) )    
        {
           cal.doClick();
        }
        else if(Hexa.isSelected() &&  ( (e.getKeyChar()>='0' &&  e.getKeyChar()<='9' ) || (e.getKeyChar()>='A' &&  e.getKeyChar()<='F' ) || ( e.getKeyChar()>='a' &&  e.getKeyChar()<='f' ) ) )
        {
           cal.doClick();
        }
        else if(Hexa.isSelected() && (e.getKeyChar()>'f' || e.getKeyChar()>'F') )
        {
           ar.setForeground(Color.red);
           ar.setText("\n\n\n\n     ERROR-did not insert an appropriate base number input-ERROR");
              
           e.consume();
        }
        
        
        //to prevent the blank text field crash in these two below
        // 8 => BS = BACK-SPACE. TO AVOID MAKING THE SYSTEM THINKS IT IS ONE OF NOT NEEDED VALUE IN NUMBER BASE
        
        else if(e.getKeyChar()==8) 
        {
           cal.doClick();
        }
        
        else if(ST.isEmpty())
        { 
           f.setText("");       
           cal.doClick();           
        }

        else
        {
           ar.setForeground(Color.red);
           ar.setText("\n\n\n\n     ERROR-did not insert an appropriate base number input-ERROR");
              
           e.consume();
        }
       
    }

    @Override
    public void keyPressed(KeyEvent e) 
    {

    }

    @Override
    public void keyReleased(KeyEvent e) 
    {
       
    }
    //END OF KEY LISTENER
    
    
    //ITEM LISTENER
    
   
    int x; //GLOBAL VARIABLE TO SOLVE THE PROBLEM TO KNOW WHICH WAS THE OPTION
    public void itemStateChanged(ItemEvent e)
    {
      Object T = e.getItem();
      int what=e.getStateChange();
      String ST=f.getText();
      ST=ST.toUpperCase();

      if(ST.isEmpty())
        {
         f.setText("0");    
         ST="0"; 
        }
      
      
    //BINARY SECTION
    if(T==Bina && what == ItemEvent.DESELECTED)
    {  
      System.out.println("BINA DESLECTED\n");
      x=2;  
    }
    else if(T==Bina && what == ItemEvent.SELECTED)
    {
      if(x==8)
      {
        int Decimal=Integer.parseInt(ST,8);
        f.setText(Integer.toBinaryString(Decimal));
        
        System.out.println("OCTO TO Bina \n");
      }
        
      if(x==10)
      {
        int Decimal=Integer.parseInt(ST);
        f.setText(Integer.toBinaryString(Decimal));
         
        System.out.println("DECA TO Bina\n");
      }
        
      if(x==16)
      {
        int num=Integer.parseInt(ST,16);
        f.setText(Integer.toBinaryString(num));
   
        System.out.println("HEXA TO Bina\n");
      }
        
     x=0;
    }
    
    //OCTAL SECTION
    if(T==Octo && what == ItemEvent.DESELECTED)
    {
      System.out.println("OCTO DESLECTED\n");
      x=8;  
    }
    else if(T==Octo && what == ItemEvent.SELECTED)
    {
      if(x==2)
      {   
        int Decimal=Integer.parseInt(ST,2);
        f.setText(Integer.toOctalString(Decimal));
              
        System.out.println("BINA to OCTO\n");
      }
        
      if(x==10)
      {
        int Decimal=Integer.parseInt(ST);
        f.setText(Integer.toOctalString(Decimal));
            
        System.out.println("DECA to OCTO\n");
      }
        
      if(x==16)
      {
        int num=Integer.parseInt(ST,16);
        f.setText(Integer.toOctalString(num));
            
        System.out.println("HEXA to OCTO\n");
      }
      
     x=0;
    }
    
     
     //DECIMAL SECTION
    if(T==Deca && what == ItemEvent.DESELECTED)
    {
      System.out.println("DECA DESLECTED\n");
      x=10;
    }
    
    else if(T==Deca && what == ItemEvent.SELECTED)
    {
      if(x==2)
      {
        int Decimal=Integer.parseInt(ST,2);
        f.setText(Integer.toString(Decimal));
 
        System.out.println("BINA TO DECA\n");
      }
        
      if(x==8)
      {
        int Decimal=Integer.parseInt(ST,8);
        f.setText(Integer.toString(Decimal));
            
        System.out.println("OCTO TO DECA\n");
      }
        
      if(x==16)
      {
        int num=Integer.parseInt(ST,16);
        f.setText(Integer.toString(num));
            
        System.out.println("HEXA TO DECA\n");
      }
   
      x=0;     
    }
    
     
     
     //HEXADECIMAL SECTION
    if(T==Hexa && what == ItemEvent.DESELECTED)
    {
      System.out.println("HEXA DESLECTED\n");
      x=16;
    }
    else if(T==Hexa && what == ItemEvent.SELECTED)
    {
      if(x==2)
      {            
        int Decimal=Integer.parseInt(ST,2);
        f.setText(Integer.toHexString(Decimal).toUpperCase());
              
        System.out.println("BINA TO HEXA\n");
      }
        
      if(x==8)
      {
        int Decimal=Integer.parseInt(ST,8);
        f.setText(Integer.toHexString(Decimal).toUpperCase());
            
        System.out.println("OCTO TO HEXA\n");
      }
        
      if(x==10)
      {
        int Decimal=Integer.parseInt(ST);
        f.setText(Integer.toHexString(Decimal).toUpperCase());
            
        System.out.println("DECA TO HEXA\n");
      }    
  
     x=0;
    }
     
    }  
    //END OF ITEM LISTENER
    
}
