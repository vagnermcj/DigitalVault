package database.entity;

public class Usuario {

    private Integer uid;
    private String login;
    private String nome;
    private Integer gid;

    private String senhaHash;

    private String totpSecretEncrypted;

    private Integer kid;

    private Integer errosSenha;
    private Integer errosTotp;

    // getters/setters
}