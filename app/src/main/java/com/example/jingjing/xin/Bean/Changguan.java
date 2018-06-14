package com.example.jingjing.xin.Bean;

public class Changguan {
    private String lcon;
    private String word;
    private String adress;
    private String type;

    public Changguan(String lcon, String word, String adress) {
        this.lcon = lcon;
        this.word = word;
        this.adress = adress;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getLcon() {
        return lcon;
    }

    public void setLcon(String lcon) {
        this.lcon = lcon;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}


