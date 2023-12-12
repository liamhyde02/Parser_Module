package helloworld;

import java.io.File;
import java.util.ArrayList;

public class S3FileFetcher {
    public static ArrayList<File> fetchS3Files(String bucketName, ArrayList<String> keys, boolean isLambdaEnvironment) {
        ArrayList<File> files = new ArrayList<>();
        for (String key : keys) {
            File file = S3Downloader.downloadFile(bucketName, key, isLambdaEnvironment);
            files.add(file);
        }
        return files;
    }
}
