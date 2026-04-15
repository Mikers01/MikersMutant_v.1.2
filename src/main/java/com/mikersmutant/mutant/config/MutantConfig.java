package com.mikersmutant.mutant.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikersmutant.mutant.MikersMutantMod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class MutantConfig {
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("mikersmutant_config.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static ConfigData DATA = new ConfigData();

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                    DATA = GSON.fromJson(reader, ConfigData.class);
                }
            } else {
                save();
            }
        } catch (IOException e) {
            MikersMutantMod.LOGGER.error("Failed to load config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(DATA, writer);
            }
        } catch (IOException e) {
            MikersMutantMod.LOGGER.error("Failed to save config", e);
        }
    }

    public static class ConfigData {
        public boolean evolution_enabled = true;
        public int max_days = 100;
        public float spawn_rate_multiplier = 1.0f;
        public boolean sun_damage_enabled = true;
        public String block_break_limit = "obsidian";
        public int horde_size_limit = 60;
        public double mutant_health = 80.0;
        public double mutant_speed = 0.35;
    }
}