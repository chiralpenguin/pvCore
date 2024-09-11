package com.purityvanilla.pvcore;

import com.purityvanilla.pvlib.ConfigFile;
import io.leangen.geantyref.TypeToken;

import java.util.List;

public class Config extends ConfigFile {
    private final Boolean verbose;
    private final List<String> rules;

    public Config(String filename) {
        super(filename);

        verbose = configRoot.node("verbose").getBoolean();
        rules = configRoot.node("rules").getList(new TypeToken<String>(){}.getType());
    }

    public Boolean getVerbose() {
        return verbose;
    }

}
