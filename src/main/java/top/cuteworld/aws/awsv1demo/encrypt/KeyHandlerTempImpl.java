package top.cuteworld.aws.awsv1demo.encrypt;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * 通过Java自带的KeyGenerator临时生产一对密钥
 */
@Slf4j
public class KeyHandlerTempImpl implements KeyHandler {

    private KeyPair keyPair;

    private String keyPairInitializeMessage;

    public KeyHandlerTempImpl() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize the key pair", e);
        }
    }

    @Override
    public KeyPair getKeyPair() {
        if (keyPair == null) {
            throw new RuntimeException("Failed to initialize the key pair");
        }
        return keyPair;
    }
}
