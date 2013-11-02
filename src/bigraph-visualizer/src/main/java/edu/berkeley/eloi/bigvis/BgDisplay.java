package edu.berkeley.eloi.bigvis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BgDisplay extends JPanel
{

    private List<BgShape> shapes;
    private JFrame frame;

    public BgDisplay(JFrame app)
    {
        super(true);
        frame = app;
    }

    void display(String bgstring)
    {
        int regionsCounter = 0;
        shapes = new ArrayList<BgShape>();
        BgContext context = new BgContext();
        BgBigraph bigraph = new BgBigraph(context);
        String bgstringRegions[] = bgstring.split("\\|\\|");

        for (String s : bgstringRegions)
        {
            BgRegion r = new BgRegion(context, regionsCounter++);

            try
            {
                BgParser bgp = new BgParser(context, s);
                for (BgShape nn : bgp.parse())
                {
                    r.addChild(nn);
                }
            }
            catch (BgParseException e)
            {
                JOptionPane.showMessageDialog(frame, "Parse Error: " + e.getMessage());
            }

            bigraph.addChild(r);
        }

        shapes.add(bigraph);

        Graphics2D tmpg = (Graphics2D) getGraphics();
        if (tmpg == null)
        {
            tmpg = (Graphics2D) frame.getGraphics();
        }

        if (tmpg == null)
        {
            return;
        }

        bigraph.layout(tmpg);
        setPreferredSize(new Dimension(bigraph.getWidth(), bigraph.getHeight()));
        revalidate();
        repaint();
        frame.setSize(new Dimension(bigraph.getWidth(), bigraph.getHeight()));
    }

    @Override
    public void paintComponent(Graphics gr)
    {
        Graphics2D g = (Graphics2D) gr;
        g.setPaint(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (BgShape s : shapes)
        {
            s.layout(g);
            s.draw(g, 25D, 100D);
        }
    }
}
