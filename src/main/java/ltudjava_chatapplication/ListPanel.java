/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltudjava_chatapplication;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.*;

/**
 *
 * @author WIN-10
 */
public class ListPanel extends JPanel{
    private static final int N = 7;
    private JList list; 
    
    public ListPanel(ChatTab ct) {
        
        super(new GridLayout());
        String[] iconList = {"😀","😃", "😄", "😁", "😆","😅","😂","🙂",
                             "🙃","😉","😊","😇","😍","😘","😗","😋","😛",
                             "😜","😐","😑","😶","😏","😒","🙄","😬","😷",
                             "🤒","🤕","🤢","🤧","😎","🤓","😟","🙁","😮",
                             "😲","😳","😦","😧","😨","😰","😢","😭","😖",
                             "😱","😤","😈","☠","💩","❤","💣","🖐","👌","🤞"};
        list = new JList(iconList);
        list.setForeground(Color.red);
        list.setBackground(Color.yellow);
        list.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(N);
        list.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 1) {
               JList target = (JList)event.getSource();
               int index = target.locationToIndex(event.getPoint());
               if (index >= 0) {
                  Object item = target.getModel().getElementAt(index);
                  ct.AppendIcon(item.toString());
                  
               }
            }
         }
      });
        this.add(list);
    }

   
}
