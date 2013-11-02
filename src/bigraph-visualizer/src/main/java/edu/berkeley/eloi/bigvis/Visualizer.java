package edu.berkeley.eloi.bigvis;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Visualizer extends JFrame
{
    BgDisplay disp;

    public Visualizer(String s)
    {
        super("Bigraph Visualisation");
        JPanel p = new JPanel(new BorderLayout());
        getContentPane().add(p);
        disp = new BgDisplay(this);
        p.add(new JScrollPane(disp), "Center");
        JPanel entryPanel = new JPanel(new BorderLayout());
        entryPanel.setPreferredSize(new Dimension(450, 100));
        entryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(550, 550));
        pack();
        if (s != null)
        {
            disp.display(s);
        }
    }

    public void update(String term)
    {
        disp.display(term);
    }
}
