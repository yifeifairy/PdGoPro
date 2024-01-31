package com.emt.pdgo.next.database.converter;

import androidx.room.TypeConverter;

import com.emt.pdgo.next.database.entity.PdEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PdInfoConverter {

    private final Gson gson = new Gson();

    @TypeConverter
    public String objectToString(List<PdEntity.PdInfoEntity> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public List<PdEntity.PdInfoEntity> stringToObject(String json) {
        Type listType = new TypeToken<List<PdEntity.PdInfoEntity>>(){}.getType();
        return gson.fromJson(json, listType);
    }


}
