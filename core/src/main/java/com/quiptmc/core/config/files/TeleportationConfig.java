/*
 * Copyright (c) 2024-2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.core.config.files;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.config.Config;
import com.quiptmc.core.config.ConfigTemplate;
import com.quiptmc.core.config.ConfigValue;
import org.json.JSONObject;

import java.io.File;

@ConfigTemplate(name = "teleport_points")
public class TeleportationConfig extends Config {

    @ConfigValue
    public int request_timeout = 60; // in seconds

    @ConfigValue
    public JSONObject locations = new JSONObject();

    public TeleportationConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }

    /**
     * @param file The file to save to
     * @param name The name of the config
     */
}
