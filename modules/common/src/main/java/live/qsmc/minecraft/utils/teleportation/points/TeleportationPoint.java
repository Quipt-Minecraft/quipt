/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package live.qsmc.minecraft.utils.teleportation.points;

import live.qsmc.minecraft.utils.teleportation.Location;
import org.json.JSONObject;

public record TeleportationPoint(String name, Type type, Location location) {

    public TeleportationPoint(JSONObject obj){
        this(obj.getString("name"), Type.valueOf(obj.getString("type")), Location.of(obj.getString("location")));
    }

    public JSONObject serialize(){
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("type", type.name());
        obj.put("location", location.serialize());
        return obj;
    }


    public enum Type {
        SPAWN,
        HOME,
        WARP
    }
}
