package com.example.finalexam;

public class Stock {
    private String name;
    private String code;
    private String price;
    private String change;

    public Stock(String name, String code, String price, String change) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.change = change;
    }

    // Getter方法
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getPrice() { return price; }
    public String getChange() { return change; }
}
