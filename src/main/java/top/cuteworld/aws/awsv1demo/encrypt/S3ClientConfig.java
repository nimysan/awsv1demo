package top.cuteworld.aws.awsv1demo.encrypt;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.CreateKeyRequest;
import com.amazonaws.services.kms.model.CreateKeyResult;
import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Configuration
public class S3ClientConfig {

    @Qualifier("s3EncryptedClientByJava")
    @Bean
    public AmazonS3EncryptionV2 composeS3Client() throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //初始化一个 配置了 客户端加密的客户端
        CryptoConfigurationV2 cryptoConfigurationV2 = new CryptoConfigurationV2().withCryptoMode(CryptoMode.StrictAuthenticatedEncryption);
        StaticEncryptionMaterialsProvider staticEncryptionMaterialsProvider = new StaticEncryptionMaterialsProvider(new EncryptionMaterials(keyPair));

        return AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(Regions.US_EAST_2)
                .withCryptoConfiguration(cryptoConfigurationV2) //加密配置
                .withEncryptionMaterialsProvider(staticEncryptionMaterialsProvider) //加密材料提供方 keyPair在这里维护
                .build();
    }

    @Qualifier("s3EncryptedClientByAWSKms")
    @Bean
    public AmazonS3EncryptionV2 composeS3ClientByKMS() throws NoSuchAlgorithmException {

        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .build();

        // 这里是每次都创建一个key， 正常使用应该使用一个维护好的key
        CreateKeyRequest createKeyRequest = new CreateKeyRequest();
        CreateKeyResult createKeyResult = kmsClient.createKey(createKeyRequest);

// --
        // specify an AWS KMS key ID
        String keyId = createKeyResult.getKeyMetadata().getKeyId();

        //初始化一个 配置了 客户端加密的客户端
        CryptoConfigurationV2 cryptoConfigurationV2 = new CryptoConfigurationV2().withCryptoMode(CryptoMode.StrictAuthenticatedEncryption);

        //最重要的差别在这里
        StaticEncryptionMaterialsProvider staticEncryptionMaterialsProvider = new KMSEncryptionMaterialsProvider(keyId);

        return AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(Regions.US_EAST_2)
                .withCryptoConfiguration(cryptoConfigurationV2) //加密配置
                .withEncryptionMaterialsProvider(staticEncryptionMaterialsProvider) //加密材料提供方 keyPair在这里维护
                .build();
    }
}
