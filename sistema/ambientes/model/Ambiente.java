package model;

public class Ambiente {
    private String id;
    private String nome;
    private TipoAmbiente tipo;
    private int capacidade;
    private String localizacao;
    private boolean ativo;

    public Ambiente(String id, String nome, TipoAmbiente tipo, int capacidade, String localizacao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
        this.ativo = true;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoAmbiente getTipo() {
        return tipo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    // Setters (usados na edição/desativação)
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(TipoAmbiente tipo) {
        this.tipo = tipo;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome +
                " | Tipo: " + tipo +
                " | Capacidade: " + capacidade +
                " | Local: " + localizacao +
                " | Status: " + (ativo ? "Ativo" : "Inativo");
    }
}
