package ch.sebooom.dump1090;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
