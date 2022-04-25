package top.cuteworld.aws.awsv1demo.encrypt;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

/**
 * 帮助保管用来做加密解密的key
 */
public interface KeyHandler {
    KeyPair getKeyPair();
}
