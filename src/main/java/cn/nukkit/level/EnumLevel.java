package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitMath;

public enum EnumLevel {
    OVERWORLD,
    NETHER,
    THE_END
    ;

    Level level;

    public Level getLevel() {
        return level;
    }

    public static void initLevels() {
        OVERWORLD.level = Server.getInstance().getDefaultLevel();

        // attempt to load the nether world if it is allowed in server properties
        if (Server.getInstance().isNetherAllowed() && !Server.getInstance().loadLevel("nether")) {

            // Nether is allowed, and not found, create the default nether world
            Server.getInstance().getLogger().info("No level called \"nether\" found, creating default nether level.");

            // Generate seed for nether and get nether generator
            long seed = System.currentTimeMillis();
            Class<? extends Generator> generator = Generator.getGenerator("nether");

            // Generate the nether world
            Server.getInstance().generateLevel("nether", seed, generator);

            // Finally, load the level if not already loaded and set the level
            if (!Server.getInstance().isLevelLoaded("nether")) {
                Server.getInstance().loadLevel("nether");
            }

        }

        NETHER.level = Server.getInstance().getLevelByName("nether");

        if (NETHER.level == null) {
            // Nether is not found or disabled
            Server.getInstance().getLogger().alert("No level called \"nether\" found or nether is disabled in server properties! Nether functionality will be disabled.");
        }
        
        // The End
        if (Server.getInstance().isTheEndAllowed() && !Server.getInstance().loadLevel("the_end")) {
            Server.getInstance().getLogger().info("No level called \"the_end\" found, creating default the end level.");
            long seed = System.currentTimeMillis();
            Class<? extends Generator> generator = Generator.getGenerator("the_end");
            Server.getInstance().generateLevel("the_end", seed, generator);
            if (!Server.getInstance().isLevelLoaded("the_end")) {
                Server.getInstance().loadLevel("the_end");
            }
        }

        THE_END.level = Server.getInstance().getLevelByName("the_end");

        if (THE_END.level == null) {
            Server.getInstance().getLogger().alert("No level called \"the_end\" found or the end is disabled in server properties! Nether functionality will be disabled.");
        }
    }

    public static Level getOtherNetherPair(Level current)   {
        if (current == OVERWORLD.level) {
            return NETHER.level;
        } else if (current == NETHER.level) {
            return OVERWORLD.level;
        } else {
            throw new IllegalArgumentException("Neither overworld nor nether given!");
        }
    }

    public static Position moveToNether(Position current)   {
        if (NETHER.level == null) {
            return null;
        } else {
            if (current.level == OVERWORLD.level) {
                return new Position(current.getFloorX() >> 3, NukkitMath.clamp(current.getFloorY(), 70, 118), current.getFloorZ() >> 3, NETHER.level);
            } else if (current.level == NETHER.level) {
                return new Position(current.getFloorX() << 3, NukkitMath.clamp(current.getFloorY(), 70, 246), current.getFloorZ() << 3, OVERWORLD.level);
            } else {
                throw new IllegalArgumentException("Neither overworld nor nether given!");
            }
        }
    }

    private static final int mRound(int value, int factor) {
        return Math.round((float) value / factor) * factor;
    }
}
