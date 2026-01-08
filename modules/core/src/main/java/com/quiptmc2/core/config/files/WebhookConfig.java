package com.quiptmc2.core.config.files;

import com.quiptmc2.core.config.objects.ConfigMap;
import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.Config;
import com.quiptmc2.core.config.ConfigTemplate;
import com.quiptmc2.core.config.ConfigValue;
import com.quiptmc2.core.data.exceptions.SimpleQuiptException;
import com.quiptmc2.core.discord.Webhook;
import com.quiptmc2.core.discord.embed.Embed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ConfigTemplate(name = "webhooks", ext = ConfigTemplate.Extension.JSON)
public class WebhookConfig extends Config {

    @ConfigValue
    public ConfigMap<Webhook> webhooks;

    /**
     * Creates a new config file
     *
     * @param file        The file to save to
     * @param name        The name of the config
     * @param extension   The extension of the config
     * @param integration The plugin that owns this config
     */
    public WebhookConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
        webhooks = new ConfigMap<>(integration);
    }

    /**
     * Add a webhook to the manager
     *
     * @param name  The name of the webhook
     * @param id    The id of the webhook
     * @param token The token of the webhook
     * @return The webhook
     */
    public Webhook add(String name, String id, String token) {
        Webhook hook = new Webhook(integration(), name, id, token);
        webhooks.put(hook);
        return hook;
    }

    /**
     * Add a webhook to the manager
     *
     * @param hook The webhook to add
     */
    public void add(Webhook hook) {
        webhooks.put(hook);
    }

    /**
     * Get a webhook by name
     *
     * @param name The name of the webhook
     * @return The webhook
     */
    public Webhook get(String name) {
        return webhooks.get(name);
    }

    /**
     * Send a message to a webhook
     *
     * @param webhookName The name of the webhook
     * @param embed       The embed to send
     * @throws SimpleQuiptException If the request fails
     */
    public void send(String webhookName, Embed embed) throws SimpleQuiptException {
        send(get(webhookName), embed);
    }

    /**
     * Send a message to a webhook
     *
     * @param hook  The webhook to send to
     * @param embed The embed to send
     * @throws SimpleQuiptException If the request fails
     */
    public void send(Webhook hook, Embed embed) throws SimpleQuiptException {
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
    public void send(String webhookName, JSONObject data) throws SimpleQuiptException {
        send(get(webhookName), data);
    }

    /**
     * Send a message to a webhook
     *
     * @param webhookName The webhook to send to
     * @param message The data to send
     * @throws SimpleQuiptException If the request fails
     */
    public void send(String webhookName, String message) throws SimpleQuiptException {
        send(get(webhookName), message);
    }

    /**
     * Send a message to a webhook
     *
     * @param hook    The webhook to send to
     * @param message The data to send
     * @throws SimpleQuiptException If the request fails
     */
    public void send(Webhook hook, String message) throws SimpleQuiptException {
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
    public void send(Webhook hook, JSONObject data) throws SimpleQuiptException {
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
