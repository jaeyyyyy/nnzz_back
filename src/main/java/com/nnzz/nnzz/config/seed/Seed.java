package com.nnzz.nnzz.config.seed;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Seed {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private final byte[] pbszUserKey;
    private final byte[] pbszIV;

    public Seed(@Value("${seed.key}") String key, @Value("${seed.iv}") String IV) {
        this.pbszUserKey = key.getBytes(UTF_8);
        this.pbszIV = IV.getBytes(UTF_8);
    }

    public String encrypt(String rawMessage) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] message = rawMessage.getBytes(UTF_8);
        byte[] encryptedMessage = KISA_SEED_CBC.SEED_CBC_Encrypt(pbszUserKey, pbszIV, message, 0, message.length);
        return new String(encoder.encode(encryptedMessage), UTF_8);
    }

    public String decrypt(String encryptedMessage) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] message = decoder.decode(encryptedMessage);
        byte[] decryptedMessage = KISA_SEED_CBC.SEED_CBC_Decrypt(pbszUserKey, pbszIV, message, 0, message.length);
        return new String(decryptedMessage, UTF_8);
    }
}
