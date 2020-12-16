//Resumo: Criamos botoes que levam suas respectivas ações, e usamos sessao para recuperar dados
package com.sample.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sample.trabalho.session.Sessao;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DasboardActivity extends AppCompatActivity {
    Button cotacao;
    Button grafico;
    Button compraVenda;
    Button historico;
    Button deposito;
    TextView saldoReal;
    TextView saldoBit;
    TextView nome;

    //Definir formatacao do bitcoin
    DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //definir formatacao
        df.setMaximumFractionDigits(340);

        cotacao = findViewById(R.id.b_cotacao);
        grafico = findViewById(R.id.b_grafico);
        compraVenda = findViewById(R.id.b_compra_venda);
        historico = findViewById(R.id.b_historico);
        deposito = findViewById(R.id.b_deposito);
        saldoReal = findViewById(R.id.saldo_real_text);
        saldoBit = findViewById(R.id.saldo_bitcoin_text);
        nome = findViewById(R.id.nome_dashboard);

        //Na sessão criada na pagina anterior (no login), usamos os dados que o foi fornecido para apresentar no dashboard
        nome.setText("Olá "+Sessao.usuarioAtual.getNome());
        saldoReal.setText("Saldo real: R$"+ Sessao.usuarioAtual.getCredito());
        saldoBit.setText("Saldo Bitcoin: "+ df.format(Sessao.usuarioAtual.getBitcoin()));

        cotacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CotacaoActivity.class);
                startActivity(intent);
            }
        });

        grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GraficoActivity.class);
                startActivity(intent);
            }
        });

        compraVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompraVendaActivity.class);
                startActivity(intent);
            }
        });

        historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoricoActivity.class);
                startActivity(intent);
            }
        });

        deposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DepositarActivity.class);
                startActivity(intent);
            }
        });
    }
}
