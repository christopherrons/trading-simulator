package com.christopher.herron.tradingsimulator.service;

public class Greeting {

    private long id;
    private String content;
    private PriceItem priceItem = new PriceItem(100L, 2, (short) 1);

    public PriceItem getPriceItem() {
        return priceItem;
    }

    public void setPriceItem() {
        this.priceItem = new PriceItem(100L, 2, (short) 1);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}