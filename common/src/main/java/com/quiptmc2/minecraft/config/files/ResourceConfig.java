/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc2.minecraft.config.files;


import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.Config;
import com.quiptmc2.core.config.ConfigTemplate;
import com.quiptmc2.core.config.ConfigValue;
import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.core.data.JsonSerializable;

import java.io.File;

@ConfigTemplate(name = "resources",ext = ConfigTemplate.Extension.QPT)
public class ResourceConfig extends Config {

    @ConfigValue
    public String repo_url = "";

    @ConfigValue
    public String repo_branch = "main";

    @ConfigValue
    public Auth auth;

    @ConfigValue
    public Hashes hashes;
    public boolean enabled = true;


    public ResourceConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
        auth = new Auth(integration);
        hashes = new Hashes(integration);
    }

    public static class Auth extends ConfigObject {

        public boolean isPrivate = false;

        public String username = "username";

        public String password = "password";

        public Auth(QuiptIntegration integration) {
            super(integration);
        }
    }


    public static class Hashes extends ConfigObject {

        public String encrypted_zip_hash = "";

        public String commit_hash = "";

        public Hashes(QuiptIntegration integration) {
            super(integration);
        }
    }
}
