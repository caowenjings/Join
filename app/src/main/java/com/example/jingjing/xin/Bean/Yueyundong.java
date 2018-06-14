package com.example.jingjing.xin.Bean;

public class Yueyundong//约运动
{
    private String content;
    private int lcon;
    private String name;

    public Yueyundong(int paramInt, String paramString1, String paramString2)
    {
        this.lcon = paramInt;
        this.name = paramString1;
        this.content = paramString2;
    }

    public String getContent()
    {
        return this.content;
    }

    public int getLcon()
    {
        return this.lcon;
    }

    public String getName()
    {
        return this.name;
    }

    public void setContent(String paramString)
    {
        this.content = paramString;
    }

    public void setLcon(int paramInt)
    {
        this.lcon = paramInt;
    }

    public void setName(String paramString)
    {
        this.name = paramString;
    }
}
