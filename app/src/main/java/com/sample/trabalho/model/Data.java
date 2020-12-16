//classe para facilitar o uso dos dados
package com.sample.trabalho.model;

import java.util.Date;

public class Data {
    private Date time;
    private Double close;


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }
}
