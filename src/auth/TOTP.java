package auth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

public class TOTP {
    private byte[] key = null;
    private long timeStepInSeconds = 30;
    private Base32 base32;

    public TOTP(String base32EncodedSecret, long timeStepInSeconds) throws Exception {
        this.timeStepInSeconds = timeStepInSeconds;
        this.base32 = new Base32(Base32.Alphabet.BASE32, false, false);

        // Decodificar BASE32
        this.key = base32.fromString(base32EncodedSecret);

        if (this.key == null || this.key.length != 20) {
            throw new Exception("Chave inválida: deve ter 20 bytes (160 bits)");
        }
    }

    private String getTOTPCodeFromHash(byte[] hash) {
        int offset = hash[hash.length - 1] & 0x0F;

        int binary = ((hash[offset] & 0x7F) << 24) |
                ((hash[offset + 1] & 0xFF) << 16) |
                ((hash[offset + 2] & 0xFF) << 8) |
                (hash[offset + 3] & 0xFF);

        int otp = binary % 1000000;

        return String.format("%06d", otp);
    }

    private byte[] HMAC_SHA1(byte[] counter, byte[] keyByteArray) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(keyByteArray, "HmacSHA1");
            mac.init(keySpec);
            return mac.doFinal(counter);
        } catch (Exception e) {
            throw new RuntimeException("Erro HMAC-SHA1", e);
        }
    }

    private String TOTPCode(long timeInterval) {
        // Converter timeInterval para 8 bytes (big-endian)
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(timeInterval);
        byte[] counter = buffer.array();

        // Calcular HMAC-SHA1
        byte[] hash = HMAC_SHA1(counter, this.key);

        // Extrair código TOTP
        return getTOTPCodeFromHash(hash);
    }

    public String generateCode() {
        long timeInterval = System.currentTimeMillis() / 1000 / timeStepInSeconds;
        return TOTPCode(timeInterval);
    }

    public boolean validateCode(String inputTOTP) {
        long currentInterval = System.currentTimeMillis() / 1000 / timeStepInSeconds;

        String code0 = TOTPCode(currentInterval - 1);
        String code1 = TOTPCode(currentInterval);
        String code2 = TOTPCode(currentInterval + 1);

        return inputTOTP.equals(code0) ||
                inputTOTP.equals(code1) ||
                inputTOTP.equals(code2);
    }

    public static String genRandomKey() {
        SecureRandom random = new SecureRandom();
        byte[] RandKey = new byte[20]; // 160 bits
        random.nextBytes(RandKey);

        Base32 base32 = new Base32(Base32.Alphabet.BASE32, false, false);
        return base32.toString(RandKey);
    }
}