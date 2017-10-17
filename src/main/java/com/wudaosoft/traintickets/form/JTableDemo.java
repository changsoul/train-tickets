/* 
 * Copyright(c)2010-2017 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
 
package com.wudaosoft.traintickets.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/** 
 * @author Changsoul Wu
 * 
 */
public class JTableDemo extends JTable {
	   
    private Color selectionColor = new Color(207,228,249);//行选择颜色
    private Color evenRowColor = new Color(233,242,241);//奇数行颜色
    private Color oddRowColor = new Color(255,255,255);//偶数行颜色
    private Color gridColor = new Color(236,233,216);//网格颜色
    private int rowHeight = 30;//行高度
   
    public JTableDemo(TableModel tableModel){
       super(tableModel);
       this.setGridColor(gridColor);
       this.setRowHeight(rowHeight);
    }
   
    public TableCellRenderer getCellRenderer(int row, int column) {
       return new MyCellRenderer();
    }
   
    class MyCellRenderer extends DefaultTableCellRenderer{
      
        public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column){
        Component cell = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);  
        this.setColor(cell, table, isSelected, hasFocus, row, column);
            return cell;
        }
        /*
         * 设置颜色
         */
        private void setColor(Component component,JTable table,boolean isSelected,boolean hasFocus,int row,int column){
        if(isSelected){
            component.setBackground(selectionColor);
            setBorder(null);//去掉边
        }else{
            if(row%2 == 0){
               component.setBackground(evenRowColor);  
            }else{
               component.setBackground(oddRowColor);
            }
        }
        }
    }
    public static void main(String[] args){
       JFrame frame = new JFrame("java Swing Jtable Demo");
       //初始化测试数据
       String[] tableTitleArray = {"ID","Name","Sex"};
        Object[][] body = new Object[6][tableTitleArray.length];
        for(int i = 0; i < 6;i++){
        body[i][0] = i;
            body[i][1] = "张三";
            body[i][2] = "男";
        }
        JTableDemo table = new JTableDemo(new DefaultTableModel(body,tableTitleArray));
        frame.getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);
       
        //设置JFrame属性
       frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       frame.setSize(640,480);
       frame.setLocationRelativeTo(null);
       frame.setVisible(true);
    }
}
