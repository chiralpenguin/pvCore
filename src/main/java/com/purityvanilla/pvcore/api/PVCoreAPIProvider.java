package com.purityvanilla.pvcore.api;

import com.purityvanilla.pvcore.PVCore;

public class PVCoreAPIProvider implements PVCoreAPI {
    private final PVCore plugin;

    public PVCoreAPIProvider(PVCore plugin) {
        this.plugin = plugin;
    }
}
