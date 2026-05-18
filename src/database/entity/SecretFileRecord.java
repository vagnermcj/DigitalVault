package database.entity;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class SecretFileRecord {

    private String codigo;

    private String nome;

    private String dono;

    private String grupo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}