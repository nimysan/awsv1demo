package top.cuteworld.aws.awsv1demo.aws;

public interface S3Repository {
    
    public void saveObject(String s3BucketName, String s3ObjectKey, String s3ObjectContent);

    public String fetchObject(String s3Bucket, String s3ObjectKey);
}
