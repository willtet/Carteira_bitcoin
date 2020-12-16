//Na classe Sessão, criamos sessao para deixar o usuario conectado apos login

package com.sample.trabalho.session;

import com.sample.trabalho.model.Usuario;

public class Sessao {
    public static Usuario usuarioAtual;

    public Sessao(Usuario user) {
        usuarioAtual = user;
    }

    public static void sair(){
        usuarioAtual = null;
    }

    public static void setUsuarioAtual(Usuario usuarioAtual) {
        Sessao.usuarioAtual = usuarioAtual;
    }
}
