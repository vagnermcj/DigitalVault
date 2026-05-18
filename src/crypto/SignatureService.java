package crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class SignatureService {

    public static byte[] sign(byte[] data,
                              PrivateKey privateKey)
            throws Exception {

        Signature signature =
                Signature.getInstance("SHA256withRSA");

        signature.initSign(privateKey);

        signature.update(data);

        return signature.sign();
    }

    public static boolean verify(byte[] data,
                                 byte[] signed,
                                 PublicKey publicKey)
            throws Exception {

        Signature signature =
                Signature.getInstance("SHA256withRSA");

        signature.initVerify(publicKey);

        signature.update(data);

        return signature.verify(signed);
    }

    public static boolean validatePrivateKey(
            PrivateKey privateKey,
            PublicKey publicKey
    ) throws Exception {

        byte[] randomData = new byte[9216];

        new java.security.SecureRandom().nextBytes(randomData);

        byte[] signature =
                SignatureService.sign(randomData, privateKey);

        return SignatureService.verify(
                randomData,
                signature,
                publicKey
        );
    }
}
