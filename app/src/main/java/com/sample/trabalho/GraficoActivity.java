//Resumo: Usamos biblioteca de terceiro para realizar o grafico

package com.sample.trabalho;


import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sample.trabalho.model.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GraficoActivity extends AppCompatActivity {

    ArrayList<Data> datas;
    GraphView graph;

    //Lib terceiro
    LineGraphSeries<DataPoint> series;

    //Formatacao do dia para a linha x
    SimpleDateFormat sdf = new SimpleDateFormat("M/d '-' H'h'");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);
        getSupportActionBar().setHomeButtonEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Grafico de Real para bitcoin");

        graph = (GraphView) findViewById(R.id.graph);


        //Uso da Classe Assincrona
        TaskGrafico tg = new TaskGrafico();
        String url = "https://min-api.cryptocompare.com/data/v2/histohour?fsym=BTC&tsym=BRL&limit=10";
        tg.execute(url);



    }

    class TaskGrafico extends AsyncTask<String, Void, LineGraphSeries<DataPoint>> {



        @Override
        protected LineGraphSeries<DataPoint> doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer grafico = new StringBuffer();
            datas = new ArrayList<>();


            try {

                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                inputStream = conexao.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String linha = "";

                while ((linha = reader.readLine()) != null){
                    grafico.append(linha);
                }

                //json puro
                JSONObject json = new JSONObject(grafico.toString());
                //filtro
                JSONObject jsonAux = new JSONObject(json.getString("Data"));


                ArrayList<Data> listaData = new ArrayList<>();
                int aux = jsonAux.getJSONArray("Data").length();
                for (int i = 0; aux > i; i++){
                    Data data = new Data();

                    //formatacao
                    double resultado = 1/(Double.parseDouble(jsonAux.getJSONArray("Data").getJSONObject(i).getString("close")));
                    Date date = new java.util.Date(Integer.parseInt(jsonAux.getJSONArray("Data").getJSONObject(i).getString("time"))*1000L);

                    data.setTime(date);
                    data.setClose(resultado);

                    datas.add(data);
                }

                //terceiro
                DataPoint[] dp = new DataPoint[datas.size()] ;
                for(int i = 0; dp.length > i; i++){
                    dp[i] = new DataPoint(datas.get(i).getTime(),datas.get(i).getClose());
                }
                series = new LineGraphSeries<DataPoint>(dp);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return series;
        }

        @Override
        protected void onPostExecute(LineGraphSeries<DataPoint> s) {
            super.onPostExecute(s);

            //Cria o grafico
            graph.addSeries(series);

            //formatacao da data
            graph.getGridLabelRenderer().setLabelFormatter((new DefaultLabelFormatter(){
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if(isValueX){
                        return sdf.format(new Date((long) value));
                    }else{
                        return  super.formatLabel(value,isValueX);
                    }
                }
            }));
        }
    }
}
