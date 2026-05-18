package database.entity;

public class Chaveiro {

    private int kid;
    private int uid;
    private byte[] privateKeyEncrypted;
    private String certificadoPem;

    public int getKid() { return kid; }
    public void setKid(int kid) { this.kid = kid; }

    public int getUid() { return uid; }
    public void setUid(int uid) { this.uid = uid; }

    public byte[] getPrivateKeyEncrypted() { return privateKeyEncrypted; }
    public void setPrivateKeyEncrypted(byte[] v) { this.privateKeyEncrypted = v; }

    public String getCertificadoPem() { return certificadoPem; }
    public void setCertificadoPem(String v) { this.certificadoPem = v; }
}