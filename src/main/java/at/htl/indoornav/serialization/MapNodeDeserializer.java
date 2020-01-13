package at.htl.indoornav.serialization;

import at.htl.indoornav.entity.MapNode;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MapNodeDeserializer implements JsonDeserializer<MapNode> {

    public MapNodeDeserializer() {
    }

    @Override
    public MapNode deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = null;
        MapNode mapNode = new MapNode();

        try {
            jsonObject = jsonElement.getAsJsonObject();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        mapNode.setNodeId(jsonObject.get("nodeId").getAsLong());
        mapNode.setType(jsonObject.get("type").getAsString());
        mapNode.setName(jsonObject.get("name").getAsString());
        mapNode.setFloor(jsonObject.get("floor").getAsString());
        mapNode.setxCoordinate(jsonObject.get("xCoordinate").getAsDouble());
        mapNode.setyCoordinate(jsonObject.get("yCoordinate").getAsDouble());

        return mapNode;
    }
}
