package cz.vse.semestralka_4it115.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import cz.vse.semestralka_4it115.logic.item.Armor;
import cz.vse.semestralka_4it115.logic.item.Food;
import cz.vse.semestralka_4it115.logic.item.Item;
import cz.vse.semestralka_4it115.logic.item.Weapon;

import java.lang.reflect.Type;

/**
 * Deserializes JSON items into concrete item subclasses.
 */
public class ItemDeserializer implements JsonDeserializer<Item> {

    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        String name = getString(obj, "name", "No name");
        int value = getInt(obj, "value", 0);
        double weight = getDouble(obj, "weight", 0);

        if (obj.has("heal")) {
            return new Food(name, value, weight, getDouble(obj, "heal", 0));
        }
        if (obj.has("damage")) {
            return new Weapon(name, value, weight, getDouble(obj, "damage", 0));
        }
        if (obj.has("defense") || obj.has("defence")) {
            double defense = obj.has("defense")
                    ? getDouble(obj, "defense", 0)
                    : getDouble(obj, "defence", 0);
            return new Armor(name, value, weight, defense);
        }

        return new Item(name, value, weight);
    }

    private static String getString(JsonObject obj, String key, String fallback) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            return fallback;
        }
        return obj.get(key).getAsString();
    }

    private static int getInt(JsonObject obj, String key, int fallback) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            return fallback;
        }
        return obj.get(key).getAsInt();
    }

    private static double getDouble(JsonObject obj, String key, double fallback) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) {
            return fallback;
        }
        return obj.get(key).getAsDouble();
    }
}
