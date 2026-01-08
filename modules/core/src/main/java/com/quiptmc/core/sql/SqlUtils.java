package com.quiptmc.core.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for SQL operations
 */
public class SqlUtils {

    /**
     * Utility class
     */
    private SqlUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, SqlDatabase> databases = new HashMap<>();

    /**
     * Creates a new database
     *
     * @param name The name of the database
     * @param db   The database to create
     */
    public static void createDatabase(String name, SqlDatabase db) {
        if (!db.init()) {
            System.out.println("There was an error registering database: " + name);
            return;
        }
        databases.put(name, db);
    }

    /**
     * Gets a database
     *
     * @param name The name of the database
     * @return The database
     */
    public static SqlDatabase getDatabase(String name) {
        return databases.getOrDefault(name, null);
    }

    /**
     * Escapes a string for SQL
     *
     * @param str The string to escape
     * @return The escaped string
     */
    public static String escape(String str) {

        String data = null;
        if (str != null && !str.isEmpty()) {
            str = str.replace("\\", "\\\\");
            str = str.replace("'", "\\'");
            str = str.replace("\0", "\\0");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\"", "\\\"");
            str = str.replace("\\x1a", "\\Z");
            data = str;
        }
        return data;

    }

    /**
     * Represents a SQL driver
     */
    public enum SQLDriver {

        /**
         * Represents a SQLite driver
         */
        SQLITE("sqlite"),

        /**
         * Represents a MySQL driver
         */
        MYSQL("mysql");

        final String name;

        /**
         * Creates a new SQL driver
         *
         * @param name The name of the driver
         */
        SQLDriver(String name) {
            this.name = name;
        }

    }

}