//Resumo: Consumir API do blockchain para obter valor da cotacao

package com.sample.trabalho;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CotacaoActivity extends AppCompatActivity {
    EditText real;
    TextView bitcoin;
    Button botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotacao);
        getSupportActionBar().setHomeButtonEnabled(true);

        real = findViewById(R.id.real_field);
        bitcoin = findViewById(R.id.bit_field);
        botao = findViewById(R.id.botao);


        //Clickando no botao
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uso da funcao assincrona passando a url
                TaskMain taskMain = new TaskMain();
                String url = "https://blockchain.info/tobtc?currency=BRL&value="+real.getText();
                taskMain.execute(url);

            }
        });

    }

    class TaskMain extends AsyncTask<String, Void, String>{

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
            //Exibe o resultado passado na etapa anterior
            bitcoin.setText("BTC: "+resultado);
        }
    }
}
