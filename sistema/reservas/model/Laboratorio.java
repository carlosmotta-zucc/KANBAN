package model;

public class Laboratorio {
    private String id;
    private String nome;
    private int capacidade;
    private String localizacao;
    private boolean ativo;

    public Laboratorio(String id, String nome, int capacidade, String localizacao) {
        this.id = id;
        this.nome = nome;
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

    public int getCapacidade() {
        return capacidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome +
                " | Capacidade: " + capacidade +
                " | Local: " + localizacao +
                " | Status: " + (ativo ? "Ativo" : "Inativo");
    }
}
