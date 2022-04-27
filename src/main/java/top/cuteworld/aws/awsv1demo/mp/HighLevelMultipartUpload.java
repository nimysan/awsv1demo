package top.cuteworld.aws.awsv1demo.mp;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 分段上传
 */
@Component
@Slf4j
public class HighLevelMultipartUpload {

    public void sample() {
        {
            Regions clientRegion = Regions.US_EAST_2;
            String bucketName = "emr.cuteworld.top";
            String keyName = "mp/obj1";
            String filePath = "/tmp/file.out";

            try {
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withRegion(clientRegion)
                        .withCredentials(new ProfileCredentialsProvider())
                        .build();
                TransferManager tm = TransferManagerBuilder.standard()
                        .withS3Client(s3Client)
                        .build();

                PutObjectRequest request = new PutObjectRequest(
                        bucketName, keyName, new File(filePath));

                //  设置上传进度条
                request.setGeneralProgressListener(new ProgressListener() {
                    @Override
                    public void progressChanged(ProgressEvent progressEvent) {
                        log.info("Transferred bytes: " +
                                progressEvent.getBytesTransferred());
                    }
                });


                // TransferManager processes all transfers asynchronously,
                // so this call returns immediately.
                Upload upload = tm.upload(request);


                log.info("Object upload started");

                // Optionally, wait for the upload to finish before continuing.
                upload.waitForCompletion();
                log.info("Object upload complete");
            } catch (AmazonServiceException | InterruptedException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();
            } catch (SdkClientException e) {
                // Amazon S3 couldn't be contacted for a response, or the client
                // couldn't parse the response from Amazon S3.
                e.printStackTrace();
            }
        }
    }


}

