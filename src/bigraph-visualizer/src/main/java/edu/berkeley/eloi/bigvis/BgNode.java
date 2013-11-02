package edu.berkeley.eloi.bigvis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;

public class BgNode extends BgShape
{
    private String mControl;
    private List<String> ports = new ArrayList<String>();

    public BgNode(BgContext context, String control)
    {
        super(context);
        mControl = control;
    }

    @Override
    public void draw(Graphics2D g, double xOff, double yOff)
    {
        BasicStroke strk = new BasicStroke(1.5F, 1, 1);
        g.setStroke(strk);
        g.setPaint(Color.black);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.draw(new java.awt.geom.RoundRectangle2D.Double(x + xOff, y + yOff, w, h, 20D, 20D));
        drawText(g, mControl, x + xOff, y + yOff);
        double drX = PADDING;
        double drY = PADDING;

        for (BgShape c : children)
        {
            c.draw(g, drX + xOff, drY + yOff);
            drX += PADDING + c.w;
        }

        if (ports.isEmpty())
        {
            return;
        }

        int sz = ports.size() - 1;
        if (sz == 0)
        {
            sz = 1;
        }

        double interval = (w - PADDING) / (double) sz;
        double px = PADDING / 2D;

        for (String s : ports)
        {
            if ("".equals(s))
            {
                g.draw(new java.awt.geom.Ellipse2D.Double((px + xOff) - 2D, (y + yOff) - 2D, 5D, 5D));
                px += interval;
                continue;
            }

            Point2D targ = getContext().get(s);
            if (targ == null)
            {
                continue;
            }

            double ctrlX = (px + xOff + (targ.getX() + PADDING)) / 2D;
            int dotDiam = 8;
            g.setStroke(strk);
            QuadCurve2D q = new java.awt.geom.QuadCurve2D.Double();
            q.setCurve(px + xOff + (double) (dotDiam / 2), (y + yOff) - (double) (dotDiam / 2), ctrlX,
                (y + yOff) - 25D, targ.getX() + PADDING, targ.getY() + 5D);
            g.setPaint(Color.BLUE);
            g.draw(q);
            g.setPaint(Color.BLACK);
            java.awt.geom.Ellipse2D.Double dot =
                new java.awt.geom.Ellipse2D.Double(px + xOff + (double) (dotDiam / 4), (y + yOff)
                    - (double) dotDiam, dotDiam, dotDiam);
            g.fill(dot);
            px += interval;
        }
    }

    @Override
    public void layout(Graphics2D g)    // identical to BgHole
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

    public void linkPort(String p)
    {
        ports.add(p);
    }

}
