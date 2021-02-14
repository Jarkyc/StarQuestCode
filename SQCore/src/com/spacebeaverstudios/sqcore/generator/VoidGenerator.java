package com.spacebeaverstudios.sqcore.generator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {

        return new ArrayList<BlockPopulator>();

    }

    @Override
    public boolean canSpawn (World world, int x, int z) {

        return true;

    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {

        ChunkData chunkData = createChunkData(world);

        for (int biomeX = 0; biomeX < 16; biomeX ++) {

            for (int biomeY = 0; biomeY < 16; biomeY ++) {

                for (int biomeZ = 0; biomeZ < 16; biomeZ ++) {

                    biome.setBiome(biomeX, biomeY, biomeZ, Biome.DARK_FOREST);

                }

            }

        }

        return chunkData;

    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {

        return new Location(world, 0, 128, 0);

    }


}
