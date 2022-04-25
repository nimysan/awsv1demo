package top.cuteworld.aws.awsv1demo.aws;

import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class S3RepositoryWithKMS implements S3Repository {

    /**
     * 这里选用不同的客户端实现
     *
     * @param amazonS3EncryptionV2
     */
    public S3RepositoryWithKMS(@Qualifier("s3EncryptedClientByAWSKms") @Autowired AmazonS3EncryptionV2 amazonS3EncryptionV2) {
        this.amazonS3EncryptionV2 = amazonS3EncryptionV2;
    }

    private final AmazonS3EncryptionV2 amazonS3EncryptionV2;


    public void saveObject(String s3BucketName, String s3ObjectKey, String s3ObjectContent) {
        amazonS3EncryptionV2.putObject(s3BucketName, s3ObjectKey, s3ObjectContent);
        if (log.isDebugEnabled()) {
            log.debug("Save object to bucket {} at {}", s3BucketName, s3ObjectKey);
        }
    }

    public String fetchObject(String s3Bucket, String s3ObjectKey) {
        return amazonS3EncryptionV2.getObjectAsString(s3Bucket, s3ObjectKey);
    }
}
