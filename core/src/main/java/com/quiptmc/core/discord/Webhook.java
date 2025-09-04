/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.core.discord;

import com.quiptmc.core.config.ConfigObject;
import com.quiptmc.core.data.JsonSerializable;


public class Webhook extends ConfigObject {

    public String token;

    public String uuid;
    /**
     * A simple webhook object
     *
     * @param id    The ID of the webhook
     * @param token The token of the webhook
     */
    public Webhook(String name, String id, String token){
        super.id = name;
        this.token = token;
        this.uuid = id;
    }

    public Webhook() {
    }

    public String name(){
        return id();
    }

    public String token(){
        return token;
    }
    /**
     * Get the URL of the webhook
     *
     * @return The URL of the webhook
     */
    public String url() {
        return "https://discord.com/api/webhooks/" + id() + "/" + token();
    }
}
