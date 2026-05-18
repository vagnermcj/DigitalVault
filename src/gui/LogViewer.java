package gui;

import config.DatabaseConfig;

import crypto.CertificateService;
import crypto.RSAService;
import crypto.SignatureService;

import java.io.Console;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import java.security.cert.X509Certificate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogViewer {

    public static void main(String[] args) {

        try {

            if (args.length < 2) {

                System.out.println(
                        "Uso:"
                );

                System.out.println(
                        "java audit.LogViewer <cert.pem> <private.bin>"
                );

                return;
            }

            String certPath = args[0];

            String keyPath = args[1];

            Console console = System.console();

            if (console == null) {

                System.out.println(
                        "Console indisponível."
                );

                return;
            }

            char[] phraseChars =
                    console.readPassword(
                            "Frase secreta: "
                    );

            String phrase =
                    new String(phraseChars);

            X509Certificate cert =
                    CertificateService.loadCertificate(
                            certPath
                    );

            PublicKey publicKey =
                    CertificateService.getPublicKey(
                            cert
                    );

            PrivateKey privateKey =
                    RSAService.loadPrivateKey(
                            keyPath,
                            phrase
                    );

            byte[] random =
                    new byte[2048];

            new SecureRandom()
                    .nextBytes(random);

            byte[] signature =
                    SignatureService.sign(
                            random,
                            privateKey
                    );

            boolean valid =
                    SignatureService.verify(
                            random,
                            signature,
                            publicKey
                    );

            if (!valid) {

                System.out.println(
                        "Falha validação da chave privada."
                );

                return;
            }

            String login =
                    extractEmail(
                            cert.getSubjectX500Principal()
                                    .getName()
                    );

            if (login == null) {

                System.out.println(
                        "Administrador inválido."
                );

                return;
            }

            try (Connection conn =
                         DatabaseConfig.getConnection()) {

                PreparedStatement stmt =
                        conn.prepareStatement(
                                """
                                SELECT
                                    r.data_hora,
                                    r.mid,
                                    m.mensagem,
                                    u.login AS login_name,
                                    r.arquivo_nome
                                FROM Registros r
                                JOIN Mensagens m
                                    ON r.mid = m.mid
                                LEFT JOIN Usuarios u
                                    ON r.uid = u.uid
                                ORDER BY r.data_hora
                                """
                        );

                ResultSet rs =
                        stmt.executeQuery();

                System.out.println(
                        "\n===== LOG VIEWER =====\n"
                );

                while (rs.next()) {

                    String dataHora =
                            rs.getString(
                                    "data_hora"
                            );

                    int mid =
                            rs.getInt("mid");

                    String mensagem =
                            rs.getString(
                                    "mensagem"
                            );

                    String loginName =
                            rs.getString(
                                    "login_name"
                            );

                    String arquivoNome =
                            rs.getString(
                                    "arquivo_nome"
                            );

                    if (loginName != null) {

                        mensagem =
                                mensagem.replace(
                                        "<login_name>",
                                        loginName
                                );
                    }

                    if (arquivoNome != null) {

                        mensagem =
                                mensagem.replace(
                                        "<arq_name>",
                                        arquivoNome
                                );
                    }

                    System.out.printf(
                            "%s | %04d | %s%n",
                            dataHora,
                            mid,
                            mensagem
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro: " + e.getMessage()
            );

            System.exit(1);
        }
    }

    private static String extractEmail(
            String subject
    ) {

        String[] parts =
                subject.split(",");

        for (String p : parts) {

            p = p.trim();

            if (p.startsWith("EMAILADDRESS=")) {

                return p.substring(
                        "EMAILADDRESS=".length()
                );
            }

            if (p.startsWith("E=")) {

                return p.substring(2);
            }
        }

        return null;
    }
}