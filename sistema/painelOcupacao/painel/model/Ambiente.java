package painel.model;

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

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoAmbiente getTipo() {
        return tipo;
    }

    public void setTipo(TipoAmbiente tipo) {
        this.tipo = tipo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " (" + tipo + ") | Cap: " + capacidade + " | Local: " + localizacao + " | Status: " + (ativo ? "Ativo" : "Inativo");
    }
}
