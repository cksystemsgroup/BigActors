package edu.berkeley.eloi.bigvis;

import java.util.ArrayList;
import java.util.List;

public class BgParser
{
    private BgContext context;
    private String s;
    private int index;

    public BgParser(BgContext context, String inp)
    {
        this.context = context;
        s = inp;
        index = 0;
    }

    public char peek()
    {
        if (index < s.length())
        {
            if (s.charAt(index) == ';')
            {
                return '\0';
            }

            return s.charAt(index);
        }

        return '\0';
    }

    public void consume()
    {
        peek();
        index++;
    }

    public boolean startsWith(String t)
    {
        return s.startsWith(t, index);
    }

    public void consume(String t) throws BgParseException
    {
        if (!startsWith(t))
        {
            throw new BgParseException(index, s, "BgParser: Expected '" + t + "'");
        }

        index += t.length();
    }

    public void ws()
    {
        char c = peek();

        if (c == 0)
        {
            return;
        }

        while (c == ' ' || c == '\t' || c == '\n')
        {
            consume();
            c = peek();
        }
    }

    public boolean isId()
    {
        char c = peek();
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
    }

    public boolean isIdOrHole()
    {
        char c = peek();
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '$';
    }

    public boolean isDigit()
    {
        char c = peek();
        return c >= '0' && c <= '9';
    }

    public String getId() throws BgParseException
    {
        if (!isId())
        {
            throw new BgParseException(index, s, "Expected name!");
        }

        String n = "" + peek();
        consume();

        while (isId() || isDigit())
        {
            n = n + peek();
            consume();
        }

        return n;
    }

    public String getDigit() throws BgParseException
    {
        if (!isDigit())
        {
            throw new BgParseException(index, s, "Expected hole!");
        }

        String n = "" + peek();
        consume();

        while (isDigit())
        {
            n = n + peek();
            consume();
        }

        return n;
    }

    public void links(BgNode b) throws BgParseException
    {
        ws();

        if (peek() == ']')
        {
            return;
        }

        if (peek() == '-')
        {
            consume();
            b.linkPort("");
        }

        if (isId())
        {
            String i = getId();
            b.linkPort(i);
            context.put(i, new java.awt.geom.Point2D.Double(0.0D, 0.0D));
        }

        ws();

        if (peek() == ',')
        {
            consume();
            links(b);
        }
    }

    public List<BgShape> exp() throws BgParseException
    {
        ws();

        if (peek() == '(')
        {
            consume();
            List<BgShape> res = exp();
            ws();
            consume(")");
            return res;
        }

        List<BgShape> n = exp_el();

        ws();

        if (peek() == 0 || peek() == ')')
        {
            return n;
        }

        if (peek() == '|')
        {
            consume();
            List<BgShape> res = exp();
            n.addAll(res);
            return n;
        }

        throw new BgParseException(index, s, "Unexpected expression at position " + index);
    }

    public List<BgShape> exp_el() throws BgParseException
    {
        ws();

        List<BgShape> res = new ArrayList<BgShape>();

        if (peek() == '(')
        {
            consume();
            res = exp();
            ws();
            consume(")");
            return res;
        }

        if (isIdOrHole())
        {
            if (startsWith("nil"))
            {
                consume("nil");
                return res;
            }

            if (startsWith("$"))
            {
                consume("$");
                String digit = getDigit();
                BgShape b = new BgHole(context, digit);
                res.add(b);
                ws();
                return res;
            }

            String id = getId();
            BgNode b = new BgNode(context, id);
            res.add(b);

            ws();

            if (peek() == '[')
            {
                consume();
                links(b);
                ws();
                consume("]");
            }

            ws();

            if (peek() == '.')
            {
                consume();

                for (BgShape cc : exp_el())
                {
                    b.addChild(cc);
                }

                return res;
            }

            if (peek() == '|' || peek() == 0)
            {
                return res;
            }
        }

        if (peek() == 0 || peek() == '|' || peek() == ')')
        {
            return res;
        }

        throw new BgParseException(index, s, "Unknown expression");
    }

    public List<BgShape> parse() throws BgParseException
    {
        return exp();
    }

}
