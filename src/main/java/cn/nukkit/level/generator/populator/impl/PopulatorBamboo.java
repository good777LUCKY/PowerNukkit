package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.helper.EnsureGrassBelow;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;

/**
 * @author GoodLucky777
 */
public class PopulatorBamboo extends PopulatorSurfaceBlock {

    private static BlockState STATE_PODZOL = BlockState.of(PODZOL);
    private static BlockState STATE_BAMBOO = BlockState.of(BAMBOO);
    
    private double podzolProbability = 0.2;
    
    @Override
    protected boolean canStay(int x, int y, int z, FullChunk chunk) {
        return EnsureCover.ensureCover(x, y, z, chunk) && (EnsureGrassBelow.ensureGrassBelow(x, y, z, chunk) || EnsureBelow.ensureBelow(x, y, z, DIRT, chunk) || EnsureBelow.ensureBelow(x, y, z, PODZOL, chunk));
    }
    
    private void generatePodzol(int x, int y, int z, FullChunk chunk, NukkitRandom random) {
        int radius = random.nextBoundedInt(4) + 1;
        
        for (int podzolX = x - radius; podzolX <= x + radius; podzolX++) {
            for (int podzolZ = z - radius; podzolZ <= z + radius; podzolZ++) {
                if ((podzolX - x) * (podzolX - x) + (podzolZ - z) * (podzolZ - z) <= radius * radius) {
                    int checkId = chunk.getBlockId(x, y - 1, z);
                    
                    if (checkId == GRASS || checkId == DIRT) {
                        chunk.setBlockState(x, y - 1, z, STATE_PODZOL);
                    }
                }
            }
        }
    }
    
    @Override
    protected int getBlockId(int x, int z, NukkitRandom random, FullChunk chunk) {
        return BAMBOO << Block.DATA_BITS;
    }
    
    @Override
    protected void placeBlock(int x, int y, int z, int id, FullChunk chunk, NukkitRandom random) {
        if (random.nextDouble() < this.podzolProbability) {
            generatePodzol(x, y, z, chunk, random);
        }
        
        int height = random.nextBoundedInt(12) + 5;
        
    }
    
    public void setPodzolProbability(double podzolProbability) {
        this.podzolProbability = podzolProbability;
    }
}
