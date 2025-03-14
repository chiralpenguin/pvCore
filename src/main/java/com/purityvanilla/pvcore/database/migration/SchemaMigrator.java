package com.purityvanilla.pvcore.database.migration;

import com.purityvanilla.pvcore.Config;
import com.purityvanilla.pvcore.database.DatabaseConnector;
import com.purityvanilla.pvcore.database.SchemaDataService;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SchemaMigrator {
    private final SchemaDataService schemaData;
    private final Logger logger;
    private final Config config;
    private final DatabaseConnector database;
    private final List<Migration> migrations;

    public SchemaMigrator(SchemaDataService schemaData, Logger logger, Config config, DatabaseConnector database) {
        this.schemaData = schemaData;
        this.logger = logger;
        this.config = config;
        this.database = database;

        migrations = new ArrayList<>();
        registerMigrations();
    }

    private void registerMigrations() {
        migrations.add(new V2Migration(database));
    }

    public void handleMigrations() {
        int currentVersion = schemaData.getCurrentVersion();
        int dbVersion = schemaData.getDBVersion();

        if (dbVersion >= currentVersion) {
            return;
        }

        logger.info(PlainTextComponentSerializer.plainText().serialize(config.getMessage(
                "database-migration", CustomTagResolvers.databaseMigrationResolver(currentVersion, dbVersion))));

        for (Migration migration : migrations) {
            if (dbVersion < migration.getVersion()) {
                migration.migrate();
            }
        }

        schemaData.updateSchemaVersion();
    }
}