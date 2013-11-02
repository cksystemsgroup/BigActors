package edu.berkeley.eloi.bigvis;

import java.awt.Graphics2D;

public class BgBigraph extends BgShape
{

    public BgBigraph(BgContext context)
    {
        super(context);
    }

    @Override
    public void draw(Graphics2D g, double xOff, double yOff)
    {
        double drX = PADDING;
        double drY = PADDING;

        for (BgShape c : children)
        {
            c.draw(g, drX + xOff, drY + yOff);
            drX += PADDING + c.w;
        }

        for (String s : getContext().keySet())
        {
            g.drawString(s, (int) (getContext().get(s).getX() + xOff), (int) (getContext().get(s).getY()));
        }
    }

    @Override
    public void layout(Graphics2D g)    // identical to BgRegion
    {
        double newW = PADDING;
        double newH = 0.0D;

        for (BgShape c : children)
        {
            c.layout(g);
            if (c.h + 2D * PADDING > newH)
            {
                newH = c.h + 2D * PADDING;
            }
            newW += c.w + PADDING;
        }

        w = newW;
        h = newH;
        
        if (w < 50D)
        {
            w = 50D;
        }
        
        if (h < 50D)
        {
            h = 50D;
        }
        
        if (getContext().isEmpty())
        {
            return;
        }
        
        int nm = getContext().size() - 1;
        
        if (nm == 0)
        {
            nm = 1;
        }
        
        double interval = (w - 2D * PADDING) / (double) nm;
        int i = 0;

        for (String s : getContext().keySet())
        {
            getContext().put(s, new java.awt.geom.Point2D.Double(PADDING + interval * (double) (i++), 20D));
        }
    }
}
