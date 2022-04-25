# S3 client side 加密

> 本例子仅针对S3客户端加密的存储的一个说明，如何安全的保存和管理密钥不在例子范围内；

## 资源引用

* [The Bouncy castle library jar is required on the classpath to enable authenticated encryption的问题](https://www.bouncycastle.org/latest_releases.html)

* [S3客户端加密机制说明](https://aws.amazon.com/blogs/developer/amazon-s3-client-side-authenticated-encryption/)

## 说明

初始化客户端

```java
CryptoConfigurationV2 cryptoConfigurationV2 = new CryptoConfigurationV2().withCryptoMode(CryptoMode.StrictAuthenticatedEncryption);
KeyPair keyPair = keyHandler.getKeyPair();
StaticEncryptionMaterialsProvider staticEncryptionMaterialsProvider = new StaticEncryptionMaterialsProvider(new EncryptionMaterials(keyPair));

amazonS3EncryptionV2 = AmazonS3EncryptionClientV2Builder.standard()
        .withRegion(Regions.US_EAST_2)
        .withCryptoConfiguration(cryptoConfigurationV2) //加密配置
        .withEncryptionMaterialsProvider(staticEncryptionMaterialsProvider) //加密材料提供方 keyPair在这里维护
        .build();
```

写对象

```java
 amazonS3EncryptionV2.putObject(s3BucketName, s3ObjectKey, s3ObjectContent);
```

读对象

```java
amazonS3EncryptionV2.getObjectAsString(s3Bucket, s3ObjectKey);
```