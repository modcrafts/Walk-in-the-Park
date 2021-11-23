package dev.efnilite.witp.schematic;

import dev.efnilite.witp.WITP;
import dev.efnilite.witp.util.Verbose;
import dev.efnilite.witp.util.task.Tasks;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores schematics so they don't have to be read every time
 */
public class SchematicCache {

    public static volatile Map<String, Schematic> cache = new HashMap<>();

    public static void read() {
        Tasks.time("schematicsLoad");
        Verbose.info("Initializing schematics...");
        cache.clear();
        File folder = new File(WITP.getInstance().getDataFolder() + "/schematics/");
        File[] files = folder.listFiles((dir, name) -> name.contains("parkour-") || name.contains("spawn-island"));
        for (File file : files) {
            String fileName = file.getName();
            Schematic schematic = new Schematic().file(fileName);
            schematic.read();
            if (schematic.isSupported()) {
                cache.put(fileName, schematic);
            }
        }
        Verbose.info("Found " + (files.length - cache.keySet().size()) + " unsupported schematics.");
        Verbose.info("Loaded all schematics in " + Tasks.end("schematicsLoad") + "ms!");
    }

    public static Schematic getSchematic(String name) {
        return cache.get(name);
    }
}