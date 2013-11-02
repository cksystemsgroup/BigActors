package edu.berkeley.eloi.bigvis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class BgRegion extends BgShape
{
    private int mIndex;

    public BgRegion(BgContext context, int index)
    {
        super(context);
        mIndex = index;
    }

    @Override
    public void draw(Graphics2D g, double xOff, double yOff)
    {
        float dash1[] = {
            5F
        };
        BasicStroke dashed = new BasicStroke(1.0F, 1, 1, 10F, dash1, 0.0F);
        g.setStroke(dashed);
        g.setPaint(Color.BLACK);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.draw(new java.awt.geom.RoundRectangle2D.Double(x + xOff, y + yOff, w, h, 10D, 10D));
        drawText(g, (new StringBuilder()).append("").append(mIndex).toString(), x + xOff, y + yOff);
        double drX = PADDING;
        double drY = PADDING;

        for (BgShape c : children)
        {
            c.draw(g, drX + xOff, drY + yOff);
            drX += PADDING + c.w;
        }
    }

    @Override
    public void layout(Graphics2D g)    // identical to BgBigraph
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
