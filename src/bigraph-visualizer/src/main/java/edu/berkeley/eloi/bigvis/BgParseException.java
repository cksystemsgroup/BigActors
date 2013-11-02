package edu.berkeley.eloi.bigvis;

@SuppressWarnings("serial")
public class BgParseException extends Exception
{
    public BgParseException(int index, String s, String msg)
    {
        super(msg + " at " + index + " remaining: '" + s.substring(index) + "'");
    }
}
