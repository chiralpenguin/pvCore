package com.purityvanilla.pvcore.database.migration;

import com.purityvanilla.pvlib.database.DatabaseConnector;
import com.purityvanilla.pvlib.database.SchemaDataService;
import com.purityvanilla.pvlib.database.SchemaMigrator;

import java.util.logging.Logger;

public class MigrationHelper {

    public static void handleMigrations(SchemaDataService schemaData, Logger logger, DatabaseConnector database) {
        SchemaMigrator schemaMigrator = new SchemaMigrator(schemaData, logger);

        schemaMigrator.registerMigration(new V2Migration(database));
        schemaMigrator.registerMigration(new V3Migration(database));
        schemaMigrator.handleMigrations();
    }
}
