package service;

import exception.AcessoNegadoException;
import exception.ValidacaoException;
import model.Ambiente;
import model.Perfil;
import model.TipoAmbiente;
import java.util.ArrayList;
import java.util.List;

public class AmbienteService {
    private List<Ambiente> ambientes = new ArrayList<>();
    private int proximoId = 1;

    public AmbienteService() {
        // Dados de exemplo para facilitar os testes no console
        ambientes.add(new Ambiente(String.valueOf(proximoId++), "Sala 101", TipoAmbiente.Sala, 40, "Bloco A"));
        ambientes.add(new Ambiente(String.valueOf(proximoId++), "Lab de Informática 1", TipoAmbiente.Laboratorio, 25, "Bloco B"));
    }

    // --- Controle de acesso: somente Administrador (KAN-03) ---
    private void verificarPermissaoAdministrador(Perfil perfil) {
        if (perfil != Perfil.Administrador) {
            throw new AcessoNegadoException(
                    "Apenas administradores podem cadastrar, editar ou desativar ambientes");
        }
    }

    // --- Validação dos dados (mensagens claras, alinhado ao KAN-12) ---
    private void validarDados(String nome, TipoAmbiente tipo, int capacidade, String localizacao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do ambiente é obrigatório", "nome");
        }
        if (tipo == null) {
            throw new ValidacaoException("Tipo do ambiente é obrigatório (Sala ou Laboratorio)", "tipo");
        }
        if (capacidade <= 0) {
            throw new ValidacaoException("Capacidade deve ser maior que zero", "capacidade");
        }
        if (localizacao == null || localizacao.trim().isEmpty()) {
            throw new ValidacaoException("Localização é obrigatória", "localizacao");
        }
    }

    private Ambiente buscarOuFalhar(String id) {
        Ambiente ambiente = buscarPorId(id);
        if (ambiente == null) {
            throw new IllegalArgumentException("Ambiente não encontrado para o ID: " + id);
        }
        return ambiente;
    }

    private void verificarNomeDuplicado(String nome, String idIgnorado) {
        for (Ambiente a : ambientes) {
            if (a.getNome().equalsIgnoreCase(nome.trim()) && !a.getId().equals(idIgnorado)) {
                throw new IllegalArgumentException("Já existe um ambiente com o nome: " + nome.trim());
            }
        }
    }

    // --- Criar ---
    public Ambiente cadastrar(Perfil perfil, String nome, TipoAmbiente tipo, int capacidade, String localizacao) {
        verificarPermissaoAdministrador(perfil);
        validarDados(nome, tipo, capacidade, localizacao);
        verificarNomeDuplicado(nome, null);

        Ambiente novo = new Ambiente(String.valueOf(proximoId++), nome.trim(), tipo, capacidade, localizacao.trim());
        ambientes.add(novo);
        return novo;
    }

    // --- Editar ---
    public Ambiente editar(Perfil perfil, String id, String nome, TipoAmbiente tipo, int capacidade, String localizacao) {
        verificarPermissaoAdministrador(perfil);
        Ambiente ambiente = buscarOuFalhar(id);
        validarDados(nome, tipo, capacidade, localizacao);
        verificarNomeDuplicado(nome, id);

        ambiente.setNome(nome.trim());
        ambiente.setTipo(tipo);
        ambiente.setCapacidade(capacidade);
        ambiente.setLocalizacao(localizacao.trim());
        return ambiente;
    }

    // --- Desativar ---
    public Ambiente desativar(Perfil perfil, String id) {
        verificarPermissaoAdministrador(perfil);
        Ambiente ambiente = buscarOuFalhar(id);
        if (!ambiente.isAtivo()) {
            throw new IllegalArgumentException("Ambiente já está desativado");
        }
        ambiente.setAtivo(false);
        return ambiente;
    }

    // --- Reativar (complementar ao desativar) ---
    public Ambiente reativar(Perfil perfil, String id) {
        verificarPermissaoAdministrador(perfil);
        Ambiente ambiente = buscarOuFalhar(id);
        if (ambiente.isAtivo()) {
            throw new IllegalArgumentException("Ambiente já está ativo");
        }
        ambiente.setAtivo(true);
        return ambiente;
    }

    // --- Consultas (leitura liberada) ---
    public List<Ambiente> listarTodos() {
        return new ArrayList<>(ambientes);
    }

    public Ambiente buscarPorId(String id) {
        if (id == null) {
            return null;
        }
        for (Ambiente a : ambientes) {
            if (a.getId().equals(id.trim())) {
                return a;
            }
        }
        return null;
    }
}
