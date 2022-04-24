package top.cuteworld.aws.awsv1demo.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.StaticEncryptionMaterialsProvider;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class S3RepositoryByCustomEncrypt {

    void rsaS3CustomEncrypt(String bucket_name) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        // --
        // generate an asymmetric key pair for testing
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String s3ObjectKey = "EncryptedContent3.txt";
        String s3ObjectContent = "This is the 3rd content to encrypt";


        // --

        AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(Regions.US_WEST_2)
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode(CryptoMode.StrictAuthenticatedEncryption))
                .withEncryptionMaterialsProvider(new StaticEncryptionMaterialsProvider(new EncryptionMaterials(keyPair)))
                .build();

        s3Encryption.putObject(bucket_name, s3ObjectKey, s3ObjectContent);
        System.out.println(s3Encryption.getObjectAsString(bucket_name, s3ObjectKey));
        s3Encryption.shutdown();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        S3RepositoryByCustomEncrypt s3RepositoryByCustomEncrypt = new S3RepositoryByCustomEncrypt();
        s3RepositoryByCustomEncrypt.rsaS3CustomEncrypt("emr.cuteworld.top");
    }
}

