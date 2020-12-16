package com.sample.trabalho;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sample.trabalho.dao.ConnectionFactory;
import com.sample.trabalho.session.Sessao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CompraVendaActivity extends AppCompatActivity {
    DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    Calendar calendar = Calendar.getInstance();
    java.util.Date currentDate = calendar.getTime();

    TextView saldo;
    EditText campoPreco;
    Button compra;
    Button venda;
    String valorBitCoin = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_venda);

        df.setMaximumFractionDigits(340);


        saldo = findViewById(R.id.saldo_compra_venda);
        campoPreco = findViewById(R.id.campo_preco);
        compra = findViewById(R.id.b_compra);
        venda = findViewById(R.id.b_venda);

        saldo.setText("Saldo Bitcoin: "+ df.format(Sessao.usuarioAtual.getBitcoin()));



        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (campoPreco.getText().equals("") || campoPreco.getText().equals(null)){
                    Toast.makeText(getApplicationContext(),"Preencha o campo corretamente",Toast.LENGTH_SHORT).show();
                }else{
                    TaskCompra tcv = new TaskCompra();
                    String url = "https://blockchain.info/tobtc?currency=BRL&value=1";
                    tcv.execute(url);
                }
            }
        });

        venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campoPreco.getText().equals("") || campoPreco.getText().equals(null)){
                    Toast.makeText(getApplicationContext(),"Preencha o campo corretamente",Toast.LENGTH_SHORT).show();
                }else{
                    TaskVenda tcv = new TaskVenda();
                    String url = "https://blockchain.info/tobtc?currency=BRL&value=1";
                    tcv.execute(url);
                }
            }
        });




    }

    class TaskCompra extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer cotacao = new StringBuffer();


            try {

                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                inputStream = conexao.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String linha = "";

                while ((linha = reader.readLine()) != null){
                    cotacao.append(linha);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return cotacao.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            valorBitCoin = resultado;

            //transformar o bit em real.
            Double valorConvertido = (Double.parseDouble(campoPreco.getText().toString()))/Double.parseDouble(valorBitCoin);
            DecimalFormat df = new DecimalFormat("#.##");
            Double valorTransformado = Double.parseDouble(df.format(valorConvertido));

            //se tem dinheiro na conta
            if(Sessao.usuarioAtual.getCredito() >= valorTransformado){

                //Desconta na sessao primeiro
                Double auxiliar = Sessao.usuarioAtual.getCredito();
                Sessao.usuarioAtual.setCredito(auxiliar-valorTransformado);
                Sessao.usuarioAtual.setBitcoin(Sessao.usuarioAtual.getBitcoin()+Double.parseDouble(campoPreco.getText().toString()));

                //atualiza o banco de dados conforme o id da sessao que é igual do bando de dados
                try {
                    if (ConnectionFactory.con == null) {
                        new ConnectionFactory().setConnection();
                    }

                    if (ConnectionFactory.con != null) {
                        Statement stm = ConnectionFactory.con.createStatement();
                        String sql = "SELECT * FROM usuario;";
                        ResultSet rs = stm.executeQuery(sql);

                        while(rs.next()){
                            if ((rs.getString("email").trim().equals(Sessao.usuarioAtual.getEmail()))){

                                String sql2 = "UPDATE usuario set credito = ?, bitcoin = ? where id = ?;";
                                try(PreparedStatement ps = ConnectionFactory.con.prepareStatement(sql2)){
                                    ps.setDouble(1, Sessao.usuarioAtual.getCredito());
                                    ps.setDouble(2, Sessao.usuarioAtual.getBitcoin());
                                    ps.setInt(3, Sessao.usuarioAtual.getId());

                                    ps.execute();
                                }

                                String sql3 = "INSERT INTO transacao(id_user ,data ,tipo ,valor) VALUES (?,?,?,?);";
                                try(PreparedStatement ps2 = ConnectionFactory.con.prepareStatement(sql3)){
                                    ps2.setInt(1, Sessao.usuarioAtual.getId());
                                    ps2.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                                    ps2.setString(3, "c");
                                    ps2.setDouble(4,  Double.parseDouble(campoPreco.getText().toString()));

                                    ps2.execute();
                                }
                            }
                        }

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                Toast.makeText(getApplicationContext(), "Comprado com sucesso", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DasboardActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Sem Saldo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class TaskVenda extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer cotacao = new StringBuffer();


            try {

                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                inputStream = conexao.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String linha = "";

                while ((linha = reader.readLine()) != null){
                    cotacao.append(linha);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return cotacao.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            valorBitCoin = resultado;

            //transformar o bit em real.
            Double valorConvertido = (Double.parseDouble(campoPreco.getText().toString()))/Double.parseDouble(valorBitCoin);
            DecimalFormat df = new DecimalFormat("#.##");
            Double valorTransformado = Double.parseDouble(df.format(valorConvertido));

            //se tem dinheiro na conta
            if(Sessao.usuarioAtual.getBitcoin() >= Double.parseDouble(campoPreco.getText().toString())){

                //Desconta e Adicionar na sessao primeiro
                Double auxiliar = Sessao.usuarioAtual.getCredito();
                Sessao.usuarioAtual.setCredito(auxiliar+valorTransformado);
                Sessao.usuarioAtual.setBitcoin(Sessao.usuarioAtual.getBitcoin()-Double.parseDouble(campoPreco.getText().toString()));

                //atualiza o banco de dados conforme o id da sessao que é igual do bando de dados
                try {
                    if (ConnectionFactory.con == null) {
                        new ConnectionFactory().setConnection();
                    }

                    if (ConnectionFactory.con != null) {
                        Statement stm = ConnectionFactory.con.createStatement();
                        String sql = "SELECT * FROM usuario;";
                        ResultSet rs = stm.executeQuery(sql);

                        while(rs.next()){
                            if ((rs.getString("email").trim().equals(Sessao.usuarioAtual.getEmail()))){

                                String sql2 = "UPDATE usuario set credito = ?, bitcoin = ? where id = ?;";
                                try(PreparedStatement ps = ConnectionFactory.con.prepareStatement(sql2)){
                                    ps.setDouble(1, Sessao.usuarioAtual.getCredito());
                                    ps.setDouble(2, Sessao.usuarioAtual.getBitcoin());
                                    ps.setInt(3, Sessao.usuarioAtual.getId());

                                    ps.execute();
                                }

                                String sql3 = "INSERT INTO transacao(id_user ,data ,tipo ,valor) VALUES (?,?,?,?);";
                                try(PreparedStatement ps2 = ConnectionFactory.con.prepareStatement(sql3)){
                                    ps2.setInt(1, Sessao.usuarioAtual.getId());
                                    ps2.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                                    ps2.setString(3, "v");
                                    ps2.setDouble(4,  Double.parseDouble(campoPreco.getText().toString()));

                                    ps2.execute();
                                }
                            }
                        }

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Sem Saldo", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DasboardActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Sem Saldo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
