package service;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ValidatedUserKeys {

    private PrivateKey privateKey;

    private PublicKey publicKey;

    public ValidatedUserKeys(
            PrivateKey privateKey,
            PublicKey publicKey
    ) {

        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
