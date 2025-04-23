/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.minecraft.files.resource;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.storage.Config;
import com.quiptmc.core.storage.ConfigTemplate;
import com.quiptmc.core.storage.ConfigValue;
import com.quiptmc.core.storage.NestedConfig;

@ConfigTemplate(name = "hashes")
public class HashesNestedConfig<T extends Config> extends NestedConfig<T> {

    @ConfigValue
    public String encrypted_zip_hash = "";

    @ConfigValue
    public String commit_hash = "";


    public HashesNestedConfig(T parent, String name, QuiptIntegration integration) {
        super(parent, name, integration);
    }
}
