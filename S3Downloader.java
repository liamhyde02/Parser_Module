package helloworld;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.core.sync.ResponseTransformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class S3Downloader {
    public static File downloadFile(String bucketName, String key, boolean isLambdaEnvironment) {
        S3Client s3Client = S3Client.builder().region(Region.US_EAST_1).build(); // replace with your region
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();

        String filePath = isLambdaEnvironment ? "/tmp/" + key : key;
        File file = new File(filePath);

        try (InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
             OutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bytesRead);
                bytesRead = inputStream.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
}