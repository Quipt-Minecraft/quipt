/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
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
import com.quiptmc.core.data.JsonSerializable;

import java.io.File;

@ConfigTemplate(name = "resources",ext = ConfigTemplate.Extension.QPT)
public class ResourceConfig extends Config {

    @ConfigValue
    public String repo_url = "";

    @ConfigValue
    public String repo_branch = "main";

    @ConfigValue
    public Auth auth = new Auth();

    @ConfigValue
    public Hashes hashes = new Hashes();


    public ResourceConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }

    public static class Auth implements JsonSerializable {

        public boolean isPrivate = false;

        public String username = "username";

        public String password = "password";
    }


    public static class Hashes implements JsonSerializable {

        public String encrypted_zip_hash = "";

        public String commit_hash = "";
    }
}
