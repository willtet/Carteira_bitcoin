package com.sample.trabalho.model;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Transacao {
    private int id;
    private int fk_id;
    private Date data;
    private String tipo;
    private Double valor;


    public Transacao(int id, int fk_id, Date data, String tipo, Double valor) {
        this.id = id;
        this.fk_id = fk_id;
        this.data = data;
        this.tipo = tipo;
        this.valor = valor;
    }

    @Override
    public String toString() {
        String tipoFormatado;
        if (this.tipo.equals("c")) {
            tipoFormatado = "Compra";
        }else{
            tipoFormatado = "Venda";
        }

        //Definir formatacao do bitcoin
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

        //definir formatacao
        df.setMaximumFractionDigits(340);
        return "ID: "+id+" -- Tipo: "+tipoFormatado+" -- Valor(Bitcoin): "+df.format(valor)+" -- Data:"+data;
    }
}
