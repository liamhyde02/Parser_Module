package helloworld;

import org.json.JSONArray;

import java.util.ArrayList;

public class JavaParserFunctionality implements JavaParser{
    public JSONArray parse(String bucketName, ArrayList<String> keys, boolean isLambdaEnvironment) {
        JSONArray jsonArray = new JSONArray();
        ArrayList<JavaEntity> entities = new ArrayList<>();
        SourceCodeParser sourceCodeParser = new SourceCodeParser();
        try {
            entities = sourceCodeParser.parseSourceFiles(S3FileFetcher.fetchS3Files(bucketName, keys, isLambdaEnvironment));
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityCleaning entityCleaning = new EntityCleaning();
        entityCleaning.clean(entities);
        for (JavaEntity entity : entities) {
            jsonArray.put(entity.toJSON());
        }
        return jsonArray;
    }
}
