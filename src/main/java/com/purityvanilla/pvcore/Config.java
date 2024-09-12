package com.purityvanilla.pvcore;

import com.purityvanilla.pvlib.config.ConfigFile;
import com.purityvanilla.pvlib.config.Messages;

public class Config extends ConfigFile {
    private final Boolean verbose;

    public Config() {
        super("plugins/pvCore/config.yml");
        messages = new Messages(this, "plugins/pvCore/messages.json");

        verbose = configRoot.node("verbose").getBoolean();
    }

    public Boolean getVerbose() {
        return verbose;
    }
}
