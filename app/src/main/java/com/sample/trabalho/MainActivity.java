//Resumo: Nessa pagina, pegamos a conexao pela classe ConnectionFactory e usamos script de select para ver se o usuario estava no nosso banco de dados. E tambem criamos uma ligacao para criar um usuario caso precise.

package com.sample.trabalho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.trabalho.dao.ConnectionFactory;
import com.sample.trabalho.model.Usuario;
import com.sample.trabalho.session.Sessao;

import java.sql.ResultSet;
import java.sql.Statement;



public class MainActivity extends AppCompatActivity{
    Button botao;
    EditText usuario;
    EditText senha;
    TextView cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //criando referencia para campos da tela de activity_main.xml
        botao = findViewById(R.id.b_entrar);
        usuario = findViewById(R.id.user_field);
        senha = findViewById(R.id.pass_field);
        cadastro = findViewById(R.id.cadastre_text);

        //quando clica no botao de Entrar
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tenta estabelecer uma conexao
                try {
                    //Se não tiver uma conexao ativa, cria um usando fabrica de conexões
                    if (ConnectionFactory.con == null) {
                        new ConnectionFactory().setConnection();
                    }

                    //Se tiver conexao
                    if (ConnectionFactory.con != null) {
                        //declara que vai fazer uma conexao usando a classe
                        Statement stm = ConnectionFactory.con.createStatement();
                        //sql
                        String sql = "SELECT * FROM usuario;";

                        //pede para executar o sql
                        ResultSet rs = stm.executeQuery(sql);

                        //rs devolve resultados , nesse caso uma lista de usuarios, rode enquanto tiver usuario no banco
                        while(rs.next()){
                            //se o email e senha confere com o que está no banco
                            if ((rs.getString("email").toString().trim().equals(usuario.getText().toString().trim())) && (rs.getString("senha").toString().trim().equals(senha.getText().toString().trim()))){
                                //cria sessao
                                Usuario u = new Usuario(rs.getInt("id"),rs.getString("nome"),rs.getString("email"),rs.getString("cpf"),rs.getString("endereco"),rs.getString("telefone"),rs.getString("senha"),rs.getDouble("credito"),rs.getDouble("bitcoin"));
                                new Sessao(u);

                                //Intencao de ir a Dashboard
                                Intent intent = new Intent(getApplicationContext(), DasboardActivity.class);
                                //Vai para Dashboard
                                startActivity(intent);
                            }else{
                                //Se nao tiver esse usuario na tabela, não deixa passar
                                Toast.makeText(getApplicationContext(), "Usuário ou senha não conferem!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //vai para area de cadastro
        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });
    }
}