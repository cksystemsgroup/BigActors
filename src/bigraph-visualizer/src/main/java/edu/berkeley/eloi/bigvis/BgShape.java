package edu.berkeley.eloi.bigvis;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public abstract class BgShape
{
    private BgContext context;
    protected double x = 0.0D;
    protected double y = 0.0D;
    protected double w = 0.0D;
    protected double h = 0.0D;
    protected List<BgShape> children = new ArrayList<BgShape>();
    protected static double PADDING = 30D;

    public BgShape(BgContext context)
    {
        this.context = context;
    }

    public void drawText(Graphics2D g, String s, double x, double y)
    {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int hgt = metrics.getHeight();
        //        int adv = (int) ((double) metrics.stringWidth(s) / 2D);
        g.drawString(s, (int) x + 7, (int) y + hgt + 2);
    }

    public abstract void draw(Graphics2D graphics2d, double d, double d1);

    public abstract void layout(Graphics2D graphics2d);

    public void addChild(BgShape b)
    {
        children.add(b);
    }

    public BgContext getContext()
    {
        return context;
    }

    public int getWidth()
    {
        return (int) w;
    }

    public int getHeight()
    {
        return (int) h;
    }
}
