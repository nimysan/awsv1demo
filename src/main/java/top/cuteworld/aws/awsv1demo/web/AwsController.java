package top.cuteworld.aws.awsv1demo.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cuteworld.aws.awsv1demo.aws.S3Repository;
import top.cuteworld.aws.awsv1demo.mp.HighLevelMultipartUpload;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("aws")
public class AwsController {

    private final S3Repository s3Repository;
    private final HighLevelMultipartUpload highLevelMultipartUpload;

    /**
     * @param s3Bucket
     * @param s3ObjectKey
     * @param s3ObjectContent 明文内容 - 在程序内部加密。 S3服务器上存储的是加密后的内容
     */
    @GetMapping("s3/upload")
    public void uploadToS3(String s3Bucket, String s3ObjectKey, String s3ObjectContent) {
        s3Repository.saveObject(s3Bucket, s3ObjectKey, s3ObjectContent);
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
        return s3Repository.fetchObject(s3Bucket, s3ObjectKey);
    }

    /**
     * 触发一个多分段上传的例子
     */
    @GetMapping("s3/multipartupload")
    public void multipartUpload() {
        highLevelMultipartUpload.sample();
    }

}
