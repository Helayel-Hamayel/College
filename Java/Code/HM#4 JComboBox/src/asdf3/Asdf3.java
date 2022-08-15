
package asdf3;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import static javax.swing.JOptionPane.*;
import javax.swing.event.*;

public class Asdf3 extends JDialog implements ActionListener{
    
    JButton can=new JButton("cancel");   
    JLabel L=new JLabel("insert");   
    JTextField F=new JTextField(20);   
    JPanel North,East,West,South,Center; 
    JComboBox box=new JComboBox();
    
    DefaultListModel first =new DefaultListModel(), second=new DefaultListModel();
    String A[]={"yes","no","NOPE.AVI"};

    public Asdf3()
    {
    //super("DO IT");
    
    
    
   box.addItem(A[2]);
   box.addItem("Lynnix Fallker");
   
   box.insertItemAt("yes", 0);
   box.insertItemAt("yess", 0);
   box.insertItemAt("yes2", 0);
   box.insertItemAt("yes23", 0);
   
   System.out.println("item Count = "+box.getItemCount());
   System.out.println("item at 1 = "+box.getItemAt(1));
   
   box.removeItemAt(1); 
   
   System.out.println("item Count = "+box.getItemCount());
   
   box.removeItem("y"); 
   
   System.out.println("item Count = "+box.getItemCount());
   
   //box.removeAllItems(); // removes all items
   
   System.out.println("The selected Item is = "+box.getSelectedItem());
   System.out.println("The selected index is = "+box.getSelectedIndex());
   
   box.setSelectedItem("Lynnix Fallker");
   System.out.println("The selected Item is = "+box.getSelectedItem());
   box.setSelectedIndex(0);
   System.out.println("The selected Item is = "+box.getSelectedItem()); 
   
   
   
   
    North = new JPanel(); East= new JPanel();
    West= new JPanel();South= new JPanel();
    Center= new JPanel(); 
    
    North.add(box);
    South.add(can);
    
    
    
    add(North,BorderLayout.NORTH);
    add(South,BorderLayout.SOUTH);
    add(Center,BorderLayout.CENTER);
   
    F.addActionListener(this);
    can.addActionListener(this);
    
    setSize(600,600);
    setLocation(300,50);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

    
    
}
