package ch.sebooom.dump1090.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
