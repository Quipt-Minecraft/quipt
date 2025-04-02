/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.core.discord;

/**
 * A simple webhook object
 *
 * @param id    The ID of the webhook
 * @param token The token of the webhook
 */
public record Webhook(String id, String token) {

    /**
     * Get the URL of the webhook
     *
     * @return The URL of the webhook
     */
    public String url() {
        return "https://discord.com/api/webhooks/" + id() + "/" + token();
    }
}
