package crypto;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class CertificateService {

    public static X509Certificate loadCertificate(String path)
            throws Exception {

        CertificateFactory factory =
                CertificateFactory.getInstance("X.509");

        try (FileInputStream fis = new FileInputStream(path)) {

            return (X509Certificate)
                    factory.generateCertificate(fis);
        }
    }

    public static PublicKey getPublicKey(X509Certificate cert) {
        return cert.getPublicKey();
    }

    public static String getEmail(X509Certificate cert) {
        return cert.getSubjectX500Principal().getName();
    }

    public static String getPem(String path)
            throws Exception {

        return Files.readString(Path.of(path));
    }
}
