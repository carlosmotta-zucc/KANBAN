package service;

import exception.ValidacaoException;
import model.TipoUsuario;
import model.Usuario;

public class AutenticacaoService {
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

    public Usuario login(String email, String senha) {
        validarLogin(email, senha);

        // Usuários simulados para teste
        if (email.equalsIgnoreCase("arthur@unoesc.edu.br") && senha.equals("1234")) {
            return new Usuario("1", "Arthur Aluno", "arthur@unoesc.edu.br", TipoUsuario.Aluno, "1234");
        } else if (email.equalsIgnoreCase("admin@unoesc.edu.br") && senha.equals("1234")) {
            return new Usuario("2", "Alice Administradora", "admin@unoesc.edu.br", TipoUsuario.Administrador, "1234");
        } else {
            throw new IllegalArgumentException("Credenciais inválidas");
        }
    }
}
