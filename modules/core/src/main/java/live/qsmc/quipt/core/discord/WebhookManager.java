package live.qsmc.quipt.core.discord;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;
import live.qsmc.quipt.core.config.objects.ConfigMap;
import live.qsmc.quipt.core.discord.embed.Embed;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.HttpHeaders;
import live.qsmc.quipt.core.utils.net.HttpMethod;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpResponse;

@ConfigTemplate(name = "webhooks", ext = ConfigTemplate.Extension.JSON)
public class WebhookManager extends Config {

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
    public WebhookManager(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
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
     * @throws FileNotFoundException If the request fails
     */
    public HttpResponse<String> send(String webhookName, Embed embed) throws FileNotFoundException {
        return send(get(webhookName), embed);
    }

    /**
     * Send a message to a webhook
     *
     * @param hook  The webhook to send to
     * @param embed The embed to send
     * @throws FileNotFoundException If the request fails
     */
    public HttpResponse<String> send(Webhook hook, Embed embed) throws FileNotFoundException {
        JSONObject data = new JSONObject();
        data.put("embeds", new JSONArray().put(embed.json()));
        return send(hook, data);
    }

    /**
     * Send a message to a webhook
     *
     * @param webhookName The name of the webhook
     * @param data        The data to send
     * @throws FileNotFoundException If the request fails
     */
    public HttpResponse<String> send(String webhookName, JSONObject data) throws FileNotFoundException {
        return send(get(webhookName), data);
    }

    /**
     * Send a message to a webhook
     *
     * @param webhookName The webhook to send to
     * @param message The data to send
     * @throws FileNotFoundException If the request fails
     */
    public HttpResponse<String> send(String webhookName, String message) throws FileNotFoundException {
        return send(get(webhookName), message);
    }

    /**
     * Send a message to a webhook
     *
     * @param hook    The webhook to send to
     * @param message The data to send
     * @throws FileNotFoundException If the request fails
     */
    public HttpResponse<String> send(Webhook hook, String message) throws FileNotFoundException {
        JSONObject data = new JSONObject();
        data.put("content", message);
        return send(hook, data);
    }

    /**
     * Send a message to a webhook
     *
     * @param hook The webhook to send to
     * @param data The data to send
     * @throws FileNotFoundException If the request fails
     */
    public HttpResponse<String> send(Webhook hook, JSONObject data) throws FileNotFoundException {
        return NetworkUtils.post(HttpConfig.defaults(HttpHeaders.CONTENT_TYPE("application/json")), hook.url(), data);
    }

    public HttpResponse<String> edit(Webhook hook, long messageId, JSONObject data) throws FileNotFoundException {
        return NetworkUtils.patch(
            HttpConfig.defaults(HttpHeaders.CONTENT_TYPE("application/json")),
            hook.url() + "/messages/" + messageId,
            data
        );
    }

    public HttpResponse<String> delete(Webhook hook, String messageId){

        try {
            return NetworkUtils.request(HttpConfig.defaults(HttpHeaders.CONTENT_TYPE("application/json")), hook.url() + "/messages/" + messageId, HttpMethod.DELETE, null, HttpResponse.BodyHandlers.ofString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
