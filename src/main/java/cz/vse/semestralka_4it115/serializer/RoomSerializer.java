package cz.vse.semestralka_4it115.serializer;


import cz.vse.semestralka_4it115.logic.space.Room;

import java.lang.reflect.Type;

/**
 * The RoomSerializer class provides a custom JSON serializer for Room objects.
 * It defines how Room objects are converted into JSON format using the Gson library.
 * This serializer ensures that only relevant properties of a Room (id, name, description, lock status, exits, items, and other people) are included in the JSON output.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-03-30
 */
public class RoomSerializer implements JsonSerializer<Room> {
    /**
     * Executes serialize functionality.
     *
     * @param room      the room parameter.
     * @param typeOfSrc the typeOfSrc parameter.
     * @param context   the context parameter.
     * @return the JsonElement result.
     */
    @Override
    public JsonElement serialize(Room room, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonRoom = new JsonObject();

        jsonRoom.addProperty("id", room.getId());
        jsonRoom.addProperty("name", room.getName());
        jsonRoom.addProperty("description", room.getDescription());
        jsonRoom.addProperty("isLocked", room.isLocked());
        jsonRoom.add("exits", context.serialize(room.getExits()));
        jsonRoom.add("items", context.serialize(room.getItems()));
        jsonRoom.add("otherPeople", context.serialize(room.getOtherPeople()));

        return jsonRoom;
    }
}
