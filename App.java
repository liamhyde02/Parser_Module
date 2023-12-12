package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        try {
            JSONObject body = null;
            try {
                body = new JSONObject(input.getBody());
            } catch (JSONException e) {
                return response
                        .withBody("{\"error\": \"Failed to parse JSON: " + e.getMessage() + "\"}")
                        .withStatusCode(500);
            }

            // Extract bucket name and keys from the body
            String bucketName = body.getString("bucket");
            ArrayList<String> keys = new ArrayList<>();
            JSONArray keysJsonArray = null;
            try {
                keysJsonArray = body.getJSONArray("fileTags");
            } catch (JSONException e) {
                return response
                        .withBody("{\"error\": \"Failed to parse JSON: " + e.getMessage() + body.toString() + "\"}")
                        .withStatusCode(500);
            }
            for (int i = 0; i < keysJsonArray.length(); i++) {
                keys.add(keysJsonArray.getString(i));
            }

            // Call the parse method with the GitHub URL and isLambdaEnvironment set to true
            JavaParserFunctionality functionality = new JavaParserFunctionality();
            JSONArray result = functionality.parse(bucketName, keys, true);

            return response
                    .withStatusCode(200)
                    .withBody(result.toString());
        } catch (JSONException e) {
            return response
                    .withBody("{\"error\": \"Failed to parse JSON: " + e.getMessage() + "\"}")
                    .withStatusCode(500);
        }  catch (Exception e) {
            return response
                    .withBody("{\"error\": \"An unexpected error occurred: " + e.getMessage() + "\"}")
                    .withStatusCode(500);
        }
    }
}