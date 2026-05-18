package database.entity;

import java.sql.Timestamp;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class Usuario {

    private Integer uid;
    private String login;
    private String nome;
    private Integer gid;

    private String grupoNome;

    private String senhaHash;

    private String totpSecretEncrypted;

    private Integer kid;

    private Integer errosSenha;
    private Integer errosTotp;
    private Integer totalAcessos;

    private Timestamp bloqueadoAte;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getGrupoNome() {
        return grupoNome;
    }

    public void setGrupoNome(String grupoNome) {
        this.grupoNome = grupoNome;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getTotpSecretEncrypted() {
        return totpSecretEncrypted;
    }

    public void setTotpSecretEncrypted(String totpSecretEncrypted) {
        this.totpSecretEncrypted = totpSecretEncrypted;
    }

    public Integer getKid() {
        return kid;
    }

    public void setKid(Integer kid) {
        this.kid = kid;
    }

    public Integer getErrosSenha() {
        return errosSenha;
    }

    public void setErrosSenha(Integer errosSenha) {
        this.errosSenha = errosSenha;
    }

    public Integer getErrosTotp() {
        return errosTotp;
    }

    public void setErrosTotp(Integer errosTotp) {
        this.errosTotp = errosTotp;
    }

    public Integer getTotalAcessos() {
        return totalAcessos;
    }

    public void setTotalAcessos(Integer totalAcessos) {
        this.totalAcessos = totalAcessos;
    }

    public Timestamp getBloqueadoAte() {
        return bloqueadoAte;
    }

    public void setBloqueadoAte(Timestamp bloqueadoAte) {
        this.bloqueadoAte = bloqueadoAte;
    }
}

