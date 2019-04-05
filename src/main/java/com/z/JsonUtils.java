package com.z;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    public static boolean isValidJSON(final String json) {
        boolean valid = false;

        if (json == null) {
            return valid;
        }

        if (! json.contains("{")) {
            return valid;
        }
        try {
            final JsonParser parser = new ObjectMapper().getFactory()
                    .createParser(json);
            while (parser.nextToken() != null) {
            }
            valid = true;
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return valid;
    }
}
