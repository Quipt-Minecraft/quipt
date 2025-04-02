/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.core.discord;

import com.quiptmc.core.discord.embed.Embed;
import com.quiptmc.core.exceptions.SimpleQuiptException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to manage webhooks
 */
public class WebhookManager {

    private final static Map<String, Webhook> WEBHOOKS = new HashMap<>();

    /**
     * Utility class
     */
    private WebhookManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Add a webhook to the manager
     *
     * @param name  The name of the webhook
     * @param id    The id of the webhook
     * @param token The token of the webhook
     * @return The webhook
     */
    public static Webhook add(String name, String id, String token) {
        Webhook hook = new Webhook(id, token);
        WEBHOOKS.put(name, hook);
        return hook;
    }

    /**
     * Get a webhook by name
     *
     * @param name The name of the webhook
     * @return The webhook
     */
    public static Webhook get(String name) {
        return WEBHOOKS.getOrDefault(name, null);
    }

    /**
     * Send a message to a webhook
     *
     * @param webhookName The name of the webhook
     * @param embed       The embed to send
     * @throws SimpleQuiptException If the request fails
     */
    public static void send(String webhookName, Embed embed) throws SimpleQuiptException {
        send(get(webhookName), embed);
    }

    /**
     * Send a message to a webhook
     *
     * @param hook  The webhook to send to
     * @param embed The embed to send
     * @throws SimpleQuiptException If the request fails
     */
    public static void send(Webhook hook, Embed embed) throws SimpleQuiptException {
        JSONObject data = new JSONObject();
        data.put("embeds", new JSONArray().put(embed.json()));
        send(hook, data);
    }

    /**
     * Send a message to a webhook
     *
     * @param webhookName The name of the webhook
     * @param data        The data to send
     * @throws SimpleQuiptException If the request fails
     */
    public static void send(String webhookName, JSONObject data) throws SimpleQuiptException {
        send(get(webhookName), data);
    }

    /**
     * Send a message to a webhook
     *
     * @param webhookName The webhook to send to
     * @param message The data to send
     * @throws SimpleQuiptException If the request fails
     */
    public static void send(String webhookName, String message) throws SimpleQuiptException {
        send(get(webhookName), message);
    }

    /**
     * Send a message to a webhook
     *
     * @param hook    The webhook to send to
     * @param message The data to send
     * @throws SimpleQuiptException If the request fails
     */
    public static void send(Webhook hook, String message) throws SimpleQuiptException {
        JSONObject data = new JSONObject();
        data.put("content", message);
        send(hook, data);

    }

    /**
     * Send a message to a webhook
     *
     * @param hook The webhook to send to
     * @param data The data to send
     * @throws SimpleQuiptException If the request fails
     */
    public static void send(Webhook hook, JSONObject data) throws SimpleQuiptException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(hook.url())).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(data.toString())).build();

        final HttpClient client = HttpClient.newHttpClient();

        final HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new SimpleQuiptException("Failed to send http request!");
        }

        final int statusCode = response.statusCode();
        if (!(statusCode >= 200 && statusCode < 300)) {
            throw new SimpleQuiptException("Http status code " + statusCode + "! Response was: '" + response.body() + "'.");
        }

        // From JDK 21 the HttpClient class extends AutoCloseable, but as we want to support Minecraft versions
        //  that use JDK 17, where HttpClient doesn't extend AutoCloseable, we need to check if it's
        //  an instance of AutoCloseable before trying to close it.
        //noinspection ConstantValue
        if (client instanceof AutoCloseable) {
            try {
                ((AutoCloseable) client).close();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
