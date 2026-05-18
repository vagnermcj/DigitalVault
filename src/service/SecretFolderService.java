package service;

import crypto.*;

import database.entity.SecretFileRecord;
import database.entity.Usuario;

import session.RuntimeSession;

import javax.crypto.SecretKey;

import java.nio.file.Files;
import java.nio.file.Path;

import java.security.PrivateKey;
import java.security.PublicKey;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

// Vagner Messias da Costa Junior - 2112851
// Túlio Martins de Lima - 2212968

public class SecretFolderService {

    public List<SecretFileRecord> loadIndex(
            String folder,
            PrivateKey privateKey,
            PublicKey publicKey
    ) throws Exception {

        Usuario current =
                RuntimeSession.getCurrentUser();

        byte[] envelope =
                Files.readAllBytes(
                        Path.of(folder, "index.env")
                );

        byte[] seed =
                EnvelopeService.decryptSeed(
                        envelope,
                        privateKey
                );

        SecretKey aesKey =
                AESService.generateKey(
                        Base64.getEncoder()
                                .encodeToString(seed)
                );

        byte[] encrypted =
                Files.readAllBytes(
                        Path.of(folder, "index.enc")
                );

        byte[] decrypted =
                AESService.decrypt(
                        encrypted,
                        aesKey
                );

        byte[] signature =
                Files.readAllBytes(
                        Path.of(folder, "index.asd")
                );

        boolean valid =
                SignatureService.verify(
                        encrypted,
                        signature,
                        publicKey
                );

        if (!valid) {

            throw new Exception(
                    "Falha integridade/autenticidade"
            );
        }

        List<SecretFileRecord> result =
                new ArrayList<>();

        String content =
                new String(decrypted);

        String[] lines =
                content.split("\n");

        for (String line : lines) {

            if (line.isBlank()) {
                continue;
            }

            String[] parts =
                    line.split(" ");

            SecretFileRecord record =
                    new SecretFileRecord();

            record.setCodigo(parts[0]);
            record.setNome(parts[1]);
            record.setDono(parts[2]);
            record.setGrupo(parts[3]);

            if (current.getGrupoNome()
                    .equals("Administrador")) {

                result.add(record);

            } else {

                boolean owner =
                        record.getDono()
                                .equalsIgnoreCase(
                                        current.getLogin()
                                );

                boolean sameGroup =
                        record.getGrupo()
                                .equalsIgnoreCase(
                                        current.getGrupoNome()
                                );

                if (owner || sameGroup) {
                    result.add(record);
                }
            }
        }

        return result;
    }

    public boolean canAccess(
            SecretFileRecord record,
            Usuario user
    ) {

        return record.getDono()
                .equalsIgnoreCase(user.getLogin());
    }

    public void decryptSecretFile(
            String folder,
            SecretFileRecord record,
            PrivateKey privateKey,
            PublicKey publicKey
    ) throws Exception {

        Path enc =
                Path.of(folder,
                        record.getCodigo() + ".enc");

        Path env =
                Path.of(folder,
                        record.getCodigo() + ".env");

        Path asd =
                Path.of(folder,
                        record.getCodigo() + ".asd");

        byte[] encrypted =
                Files.readAllBytes(enc);

        byte[] envelope =
                Files.readAllBytes(env);

        byte[] signature =
                Files.readAllBytes(asd);

        byte[] seed =
                EnvelopeService.decryptSeed(
                        envelope,
                        privateKey
                );

        SecretKey aesKey =
                AESService.generateKey(
                        Base64.getEncoder()
                                .encodeToString(seed)
                );

        boolean valid =
                SignatureService.verify(
                        encrypted,
                        signature,
                        publicKey
                );

        if (!valid) {

            throw new Exception(
                    "Assinatura inválida"
            );
        }

        byte[] decrypted =
                AESService.decrypt(
                        encrypted,
                        aesKey
                );

        Files.write(
                Path.of(folder,
                        record.getNome()),
                decrypted
        );
    }
}