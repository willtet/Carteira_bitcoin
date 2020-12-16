//Resumo: Usamos script select para objeter dados do anco de dados e colocar na lista filtrado por id do usuario

package com.sample.trabalho;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.sample.trabalho.dao.ConnectionFactory;
import com.sample.trabalho.model.Transacao;
import com.sample.trabalho.session.Sessao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class HistoricoActivity extends AppCompatActivity {

    ArrayList<Transacao> transacoes = new ArrayList<>();
    ListView lista;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        setTitle("Transações");
        lista = (ListView) findViewById(R.id.lista);

        //Classe assincrona
        Taskhistorico task = new Taskhistorico();
        task.execute();


    }
    class Taskhistorico extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //verifica conexao
                if (ConnectionFactory.con == null) {
                    new ConnectionFactory().setConnection();
                }

                if (ConnectionFactory.con != null) {
                    Statement stm = ConnectionFactory.con.createStatement();
                    String sql = "SELECT * FROM transacao;";
                    ResultSet rs = stm.executeQuery(sql);

                    //Se achar o id_fk igual da Sessa, armazenar na lista
                    while(rs.next()){
                        if (rs.getInt("id_user")== Sessao.usuarioAtual.getId()) {
                            Transacao transacao = new Transacao(rs.getInt("id"), rs.getInt("id_user"), rs.getDate("data"), rs.getString("tipo"), rs.getDouble("valor"));
                            transacoes.add(transacao);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Cria lista no app
            ArrayAdapter<Transacao> adapter = new ArrayAdapter<Transacao>(getApplicationContext(), android.R.layout.simple_list_item_1, transacoes);

            //Coloca itens do array na lista
            lista.setAdapter(adapter);
        }
    }
}
