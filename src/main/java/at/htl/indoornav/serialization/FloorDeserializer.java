package at.htl.indoornav.serialization;

import at.htl.indoornav.entity.Floor;
import com.google.gson.*;

import java.lang.reflect.Type;

public class FloorDeserializer implements JsonDeserializer<Floor> {

    public FloorDeserializer() {
    }

    @Override
    public Floor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = null;
        Floor floor = new Floor();

        try {
            jsonObject = jsonElement.getAsJsonObject();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        floor.setFloorId(jsonObject.get("floorId").getAsLong());
        floor.setName(jsonObject.get("name").getAsString());
        floor.setShortName(jsonObject.get("shortName").getAsString());

        return floor;
    }
}
