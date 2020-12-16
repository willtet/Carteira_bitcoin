//Resumo: Só depositamos dinheiro imaginario na conta imaginaria com script Update

package com.sample.trabalho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sample.trabalho.dao.ConnectionFactory;
import com.sample.trabalho.session.Sessao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;

public class DepositarActivity extends AppCompatActivity {
    EditText valor;
    Button inserir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositar);

        valor= findViewById(R.id.deposito_valor);
        inserir = findViewById(R.id.b_inserir_deposito);

        inserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valor.getText().equals("") || valor.getText().equals(null)){
                    Toast.makeText(getApplicationContext(),"Preencha o campo corretamente",Toast.LENGTH_SHORT).show();
                }else{
                    TaskInserir tcv = new TaskInserir();
                    tcv.execute();
                }
            }
        });
    }

    class TaskInserir extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            //Formatar real.
            Double valorConvertido = Double.parseDouble(valor.getText().toString());
            DecimalFormat df = new DecimalFormat("#.##");
            Double valorTransformado = Double.parseDouble(df.format(valorConvertido));

            //Desconta e Adicionar na sessao primeiro
            Sessao.usuarioAtual.setCredito(Sessao.usuarioAtual.getCredito()+valorTransformado);

            try {
                if (ConnectionFactory.con == null) {
                    new ConnectionFactory().setConnection();
                }

                if (ConnectionFactory.con != null) {
                    Statement stm = ConnectionFactory.con.createStatement();
                    String sql = "SELECT * FROM usuario;";
                    ResultSet rs = stm.executeQuery(sql);

                    while (rs.next()) {
                        if ((rs.getString("email").trim().equals(Sessao.usuarioAtual.getEmail()))) {

                            String sql2 = "UPDATE usuario set credito = ? where id = ?;";
                            try (PreparedStatement ps = ConnectionFactory.con.prepareStatement(sql2)) {
                                ps.setDouble(1, Sessao.usuarioAtual.getCredito());
                                ps.setInt(2, Sessao.usuarioAtual.getId());

                                ps.execute();
                            }

                        }
                    }

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Adicionado com sucesso", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), DasboardActivity.class);
            startActivity(intent);
            finish();
        }
    }


}