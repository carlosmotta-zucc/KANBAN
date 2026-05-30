package service;

import exception.ValidacaoException;
import model.TipoUsuario;
import model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class AutenticacaoService {
    private List<Usuario> usuarios = new ArrayList<>();
    private int proximoId = 3;

    public AutenticacaoService() {
        // Inicializa usuários mock padrão
        usuarios.add(new Usuario("1", "Arthur Aluno", "arthur@unoesc.edu.br", TipoUsuario.Aluno, "1234"));
        usuarios.add(new Usuario("2", "Alice Administradora", "admin@unoesc.edu.br", TipoUsuario.Administrador, "1234"));
    }

    public void validarLogin(String email, String senha) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacaoException("Email é obrigatório", "email");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new ValidacaoException("Senha é obrigatória", "senha");
        }
    }

    public void validarCadastro(String nome, String email, TipoUsuario tipo, String senha) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório", "nome");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacaoException("Email é obrigatório", "email");
        }
        if (tipo == null) {
            throw new ValidacaoException("Tipo de usuário é obrigatório", "tipo");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new ValidacaoException("Senha é obrigatória", "senha");
        }
    }

    public Usuario cadastrar(String nome, String email, TipoUsuario tipo, String senha) {
        validarCadastro(nome, email, tipo, senha);

        // Verifica duplicidade de e-mail
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email.trim())) {
                throw new IllegalArgumentException("Este e-mail já está cadastrado");
            }
        }

        Usuario novo = new Usuario(String.valueOf(proximoId++), nome.trim(), email.trim(), tipo, senha);
        usuarios.add(novo);
        return novo;
    }

    public Usuario login(String email, String senha) {
        validarLogin(email, senha);

        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email.trim()) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Credenciais inválidas");
    }
}
