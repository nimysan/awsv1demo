package top.cuteworld.aws.awsv1demo.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cuteworld.aws.awsv1demo.aws.S3RepositoryByCustomEncrypt;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("aws")
public class AwsController {

    private final S3RepositoryByCustomEncrypt s3RepositoryByCustomEncrypt;

    /**
     * @param s3Bucket
     * @param s3ObjectKey
     * @param s3ObjectContent 明文内容 - 在程序内部加密。 S3服务器上存储的是加密后的内容
     */
    @GetMapping("s3/upload")
    public void uploadToS3(String s3Bucket, String s3ObjectKey, String s3ObjectContent) {
        s3RepositoryByCustomEncrypt.saveObject(s3Bucket, s3ObjectKey, s3ObjectContent);
    }

    /**
     * 获取加密的内容
     *
     * @param s3Bucket
     * @param s3ObjectKey
     * @return
     */
    @GetMapping("s3/fetch")
    public String fetchFromS3(String s3Bucket, String s3ObjectKey) {
        return s3RepositoryByCustomEncrypt.fetchObject(s3Bucket, s3ObjectKey);
    }

}
