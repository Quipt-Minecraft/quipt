package com.quiptmc.core.discord.embed;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Represents a discord embed
 */
public class Embed {

    private final JSONObject data = new JSONObject();


    /**
     * Creates a new embed
     */
    public Embed() {
        data.put("type", "rich");
        timestamp(new Date());
    }

    /**
     * Sets the title of the embed
     * @param title The title of the embed
     * @return The embed
     */
    public Embed title(String title) {
        data.put("title", title);
        return this;
    }

    /**
     * Sets the description of the embed
     * @param description The description of the embed
     * @return The embed
     */
    public Embed description(String description) {
        data.put("description", description);
        return this;
    }

    /**
     * Sets the url of the embed
     * @param url The url of the embed
     * @return The embed
     */
    public Embed url(String url) {
        data.put("url", url);
        return this;
    }

    /**
     * Sets the color of the embed
     * @param color The color of the embed
     * @return The embed
     */
    public Embed color(int color) {
        data.put("color", color);
        return this;
    }

    /**
     * Sets the timestamp of the embed
     * @param date The timestamp of the embed
     * @return The embed
     */
    public Embed timestamp(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String iso = df.format(date);
        data.put("timestamp", iso);
        return this;
    }

    /**
     * Sets the footer of the embed
     * @param text The text of the footer
     * @param icon_url The icon url of the footer
     * @return The embed
     */
    public Embed footer(String text, String icon_url) {
        JSONObject footer = new JSONObject();
        footer.put("text", text);
        footer.put("icon_url", icon_url);
        data.put("footer", footer);
        return this;
    }

    /**
     * Sets the image of the embed
     * @param url The url of the image
     * @return The embed
     */
    public Embed image(String url) {
        JSONObject image = new JSONObject();
        image.put("url", url);
        data.put("image", image);
        return this;
    }

    /**
     * Sets the thumbnail of the embed
     * @param url The url of the thumbnail
     * @return The embed
     */
    public Embed thumbnail(String url) {
        JSONObject thumbnail = new JSONObject();
        thumbnail.put("url", url);
        data.put("thumbnail", thumbnail);
        return this;
    }

    /**
     * Sets the author of the embed
     * @param name The name of the author
     * @param url The url of the author
     * @param icon_url The icon url of the author
     * @return The embed
     */
    public Embed author(String name, String url, String icon_url) {
        JSONObject author = new JSONObject();
        author.put("name", name);
        author.put("url", url);
        author.put("icon_url", icon_url);
        data.put("author", author);
        return this;
    }

    /**
     * Adds a field to the embed
     * @param name The name of the field
     * @param value The value of the field
     * @param inline Whether the field is inline
     * @return The embed
     */
    public Embed addField(String name, String value, boolean inline) {
        JSONObject field = new JSONObject();
        field.put("name", name);
        field.put("value", value);
        field.put("inline", inline);
        if(!data.has("fields")) data.put("fields", new JSONArray());
        data.getJSONArray("fields").put(field);
        return this;
    }

    /**
     * Converts this embed to a JSON object
     * @return The JSON object
     */
    public JSONObject json() {
        return data;
    }
}
