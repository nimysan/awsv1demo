package top.cuteworld.aws.awsv1demo.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.StaticEncryptionMaterialsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.cuteworld.aws.awsv1demo.encrypt.KeyHandler;

import javax.annotation.PreDestroy;
import java.security.KeyPair;

@Slf4j
@Component
public class S3RepositoryByCustomEncrypt {

    private final KeyHandler keyHandler;
    private final AmazonS3EncryptionV2 amazonS3EncryptionV2;

    public S3RepositoryByCustomEncrypt(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;

        //初始化一个 配置了 客户端加密的客户端
        CryptoConfigurationV2 cryptoConfigurationV2 = new CryptoConfigurationV2().withCryptoMode(CryptoMode.StrictAuthenticatedEncryption);
        KeyPair keyPair = keyHandler.getKeyPair();
        StaticEncryptionMaterialsProvider staticEncryptionMaterialsProvider = new StaticEncryptionMaterialsProvider(new EncryptionMaterials(keyPair));

        amazonS3EncryptionV2 = AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(Regions.US_EAST_2)
                .withCryptoConfiguration(cryptoConfigurationV2) //加密配置
                .withEncryptionMaterialsProvider(staticEncryptionMaterialsProvider) //加密材料提供方 keyPair在这里维护
                .build();
    }

    public void saveObject(String s3BucketName, String s3ObjectKey, String s3ObjectContent) {
        amazonS3EncryptionV2.putObject(s3BucketName, s3ObjectKey, s3ObjectContent);
        if (log.isDebugEnabled()) {
            log.debug("Save object to bucket {} at {}", s3BucketName, s3ObjectKey);
        }
    }

    public String fetchObject(String s3Bucket, String s3ObjectKey) {
        return amazonS3EncryptionV2.getObjectAsString(s3Bucket, s3ObjectKey);
    }

    @PreDestroy
    void destroy() {
        log.info("Will shutdown the s3 client");
        if (amazonS3EncryptionV2 != null) {
            amazonS3EncryptionV2.shutdown();
        }

    }

}

