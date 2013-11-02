package edu.berkeley.eloi.bigvis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class BgHole extends BgShape
{

    private String mControl;

    public BgHole(BgContext context, String control)
    {
        super(context);
        mControl = control;
    }

    @Override
    public void draw(Graphics2D g, double xOff, double yOff)
    {
        float dash1[] = {
            5F
        };
        BasicStroke dashed = new BasicStroke(1.5F, 1, 1, 10F, dash1, 0.0F);
        g.setStroke(dashed);
        g.setPaint(Color.GRAY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.draw(new java.awt.geom.RoundRectangle2D.Double(x + xOff, y + yOff, w, h, 10D, 10D));
        drawText(g, mControl, x + xOff, y + yOff);
    }

    @Override
    public void layout(Graphics2D g)    // identical to BgNode
    {
        double newW = PADDING;
        double newH = 0.0D;
        
        if (g == null)
        {
            System.out.println("NULL GRAPHICS");
        }

        for (BgShape c : children)
        {
            c.layout(g);
            if (c.h + 2D * PADDING > newH)
            {
                newH = c.h + 2D * PADDING;
            }
            newW += c.w + PADDING;
        }

        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int adv = metrics.stringWidth(mControl);

        w = newW;
        h = newH;

        if (w < (double) adv + PADDING * 2D)
        {
            w = (double) adv + PADDING;
        }

        if (h < 50D)
        {
            h = w;
        }
    }

}
