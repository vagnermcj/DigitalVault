package database.entity;

public class Grupo {

    private int gid;

    private String nome;

    public Grupo() {
    }

    public Grupo(int gid, String nome) {
        this.gid = gid;
        this.nome = nome;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}