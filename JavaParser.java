package helloworld;
import org.json.JSONArray;

import java.util.ArrayList;

public interface JavaParser {
    public JSONArray parse(String bucketName, ArrayList<String> keys, boolean isLambdaEnvironment);
}
