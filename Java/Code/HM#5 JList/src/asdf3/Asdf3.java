
package asdf3;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import static javax.swing.JOptionPane.*;
import javax.swing.event.*;

public class Asdf3 extends JFrame implements ActionListener,ListSelectionListener {
    
    JButton can=new JButton("cancel");   
    JLabel L=new JLabel("insert");   
    JTextField F=new JTextField(20);   
    JPanel North,East,West,South,Center; 
    
    DefaultListModel first =new DefaultListModel(), second=new DefaultListModel();
    String A[]={"yes","no","NOPE.AVI"};

    public Asdf3()
    {
    super("DO IT");
    JList one=new JList();
   // JList two=new JList(second);
    one.setModel(first);
    
    DefaultListModel da = (DefaultListModel) one.getModel();
    JList two=new JList(da);
    
    second.addElement("yes");
    
    
    Random r= new Random();

    for(int i = 0; i<10;i++)
    {
    first.addElement(r.nextInt(300)+"");
    }
    
    first.add(4,"NO");
    first.set(1,"NO");
    first.add(1,"23");
    
    one.setSelectedIndex(3);
    one.setSelectedValue("23",false);
    System.out.println("Selected index is " + one.getSelectedIndex());
    System.out.println("Selected value is " + one.getSelectedValue());
    //System.out.println("The Size is " + one.getSize());
    System.out.println("The Size is " + first.getSize());
    System.out.println("The Size is " + first.get(10));
    
    first.remove(3);
    System.out.println("The Size is " + first.getSize());
    //first.clear();
    
    System.out.println("The Size is " + first.indexOf("no"));
    System.out.println("The Size is " + first.indexOf("NO"));
    
    
    int cont =0;
    /*
   if(first.contains("23")) 
    for(int i =0;i < first.getSize(); i++ )
    {
      if("NO".equals(first.get(i)))
      {
        cont++;
      }
    }
    */
    int x=0;
    
    while(first.indexOf("NO", x)!= -1 )
    {
      x = (first.indexOf("NO", x))+1;
      cont++;
    }
    
    
    
    System.out.println("The number of NO is " + cont);
    
    System.out.println("The number of NO is " + first.lastIndexOf("NO"));
   
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    North = new JPanel(); East= new JPanel();
    West= new JPanel();South= new JPanel();
    Center= new JPanel(); 
    
    North.add(L);
    North.add(F);
    South.add(can);
    
    Center.add(one);
    Center.add(two);
    
    add(North,BorderLayout.NORTH);
    add(South,BorderLayout.SOUTH);
    
    add(Center,BorderLayout.CENTER);
   

    F.addActionListener(this);
    can.addActionListener(this);
    one.addListSelectionListener(this);
    two.addListSelectionListener(this);
    
    setSize(600,600);
    setLocation(300,50);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
    }
    
    
    public static void main(String[] args) 
    {
        new Asdf3();
    }

    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object T = e.getSource();
        
        if(T==can)
            dispose();
        
        else if (T==F)
        {
        String S=F.getText();
        
        if(!first.contains(S))
        {
        first.add(0, S);
        }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        
        Object T=e.getSource();
        
        if(T==one && e.getValueIsAdjusting()==false)
        {
       // String S=one.getSelectedValue();
        F.setText(one.getSelectedValue());
        }
        
    }
    
}
