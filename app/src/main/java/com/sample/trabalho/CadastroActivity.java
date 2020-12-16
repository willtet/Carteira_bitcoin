//Resumo: Nessa pagina, usamos validacao para verificar se campo preenchido está vazio, validacao de email se está cadastrada no banco com script select e inserimos o usuario com script insert, no final, fechamos a conexao para não gastar a banda
package com.sample.trabalho;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sample.trabalho.dao.ConnectionFactory;
import com.sample.trabalho.model.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CadastroActivity extends AppCompatActivity {

    EditText nome;
    EditText email;
    EditText cpf;
    EditText endereco;
    EditText tel;
    EditText senha;
    EditText confSenha;
    Button send;
    Usuario usuario = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        nome = findViewById(R.id.nome_cad);
        email = findViewById(R.id.email_cad);
        cpf = findViewById(R.id.cpf_cad);
        endereco = findViewById(R.id.end_cad);
        tel = findViewById(R.id.tel_cad);
        senha = findViewById(R.id.senha_cad);
        confSenha = findViewById(R.id.conf_senha_cad);
        send = findViewById(R.id.b_cadastrar);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validacao de campo vazio
                if (nome.getText().toString().trim() != "" && email.getText().toString().trim() != "" && cpf.getText().toString().trim() != "" && endereco.getText().toString().trim() != "" && tel.getText().toString().trim() != "" && senha.getText().toString().trim() != "" && confSenha.getText().toString().trim() != "" && senha.getText().toString().trim().equals(confSenha.getText().toString().trim())){

                    //Criamos um usuario para facilitar na hora do acesso dos dados
                    usuario = new Usuario(nome.getText().toString().trim(),email.getText().toString().trim(),cpf.getText().toString().trim(),endereco.getText().toString().trim(), tel.getText().toString().trim(),senha.getText().toString().trim(),0.0,0.0);
                    //Chama a validacao de email no banco
                    if (conferirUsuario()) {
                        //Se nao tiver cria o usuario no banco, passamos parametro do usuario para facilitar na hora de acessar os dados
                        criarUsuario(usuario);

                        //Envia mensagem de sucesso
                        Toast.makeText(getApplicationContext(), "Criado com sucesso", Toast.LENGTH_SHORT).show();

                        //fecha a pagina
                        finish();
                    }else{
                        //Caso exista o email, manda um aviso
                        Toast.makeText(getApplicationContext(), "Email Existente", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //Else da validacao de campo vazio
                    Toast.makeText(getApplicationContext(), "Digite corretamente os campos", Toast.LENGTH_SHORT).show();
                }




            }
        });



    }

    //validacao no banco de dados para verificar se existe mesmo email
    private boolean conferirUsuario() {
        try {
            //varifica conexao
            if (ConnectionFactory.con == null) {
                new ConnectionFactory().setConnection();
            }

            if (ConnectionFactory.con != null) {
                Statement stm = ConnectionFactory.con.createStatement();
                String sql = "SELECT * FROM usuario;";
                ResultSet rs = stm.executeQuery(sql);

                while(rs.next()){
                    //Caso exista, retorna falso
                    if ((rs.getString("email").toString().trim().equals(usuario.getEmail()))){
                        return false;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //Caso não tenha, retorna true
        return true;
    }

    //Criar Usuario no banco de dados
    private void criarUsuario(Usuario u) {
        //Verifica conexao
        try {
            if (ConnectionFactory.con == null) {
                new ConnectionFactory().setConnection();
            }
            if (ConnectionFactory.con != null) {

                //Aqui, usamos o script de insert para cadastrar no banco de dados
                Statement stm = ConnectionFactory.con.createStatement();
                String sql = "INSERT INTO usuario(nome,email,cpf,endereco,telefone,senha,credito,bitcoin) VALUES (?,?,?,?,?,?,?,?);";
                try(PreparedStatement ps = ConnectionFactory.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    ps.setString(1, u.getNome());
                    ps.setString(2, u.getEmail());
                    ps.setString(3, u.getCpf());
                    ps.setString(4, u.getEndereco());
                    ps.setString(5, u.getTelefone());
                    ps.setString(6, u.getSenha());
                    ps.setDouble(7, u.getCredito());
                    ps.setDouble(8, u.getBitcoin());

                    ps.execute();

                    //O usuario recebe de volta o id auto incrementado do banco
                    try(ResultSet rs = ps.getGeneratedKeys()){
                        while(rs.next()) {
                            //E insere no Usuario q foi criado, para facilidade de acesso caso precise
                            u.setId(rs.getInt(1));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                stm.executeQuery(sql);

                //Fecha a conexao para n consumir banda
                ConnectionFactory.con.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
