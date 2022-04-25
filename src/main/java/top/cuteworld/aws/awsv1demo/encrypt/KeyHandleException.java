package top.cuteworld.aws.awsv1demo.encrypt;

/**
 * 维护key出现的异常
 */
public class KeyHandleException extends Exception {
    public KeyHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyHandleException(String message) {
        super(message);
    }
}
