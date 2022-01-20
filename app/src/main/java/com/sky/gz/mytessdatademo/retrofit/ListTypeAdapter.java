package com.sky.gz.mytessdatademo.retrofit;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author INX
 * @ClassNmae
 * @Description
 * @Date 2020/10/27 9:26
 */
public class ListTypeAdapter extends TypeAdapter<List> {
    @Override
    public void write(JsonWriter out, List value) throws IOException {
        try {
            if (value == null) {
                value = new ArrayList();
            }
            out.value(new Gson().toJson(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case NULL:
                in.nextNull();
                return null;
            case STRING:
                String content = in.nextString().trim();
                if (TextUtils.isEmpty(content)
                        || "null".equalsIgnoreCase(content)
                        || "[]".equalsIgnoreCase(content)) {
                    return new ArrayList();
                }
            default:
                throw new JsonParseException("Expected List but was " + peek);
        }
    }
}
