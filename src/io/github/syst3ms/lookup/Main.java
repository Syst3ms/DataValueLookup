package io.github.syst3ms.lookup;

import com.google.common.collect.ImmutableList;
import com.sun.org.apache.bcel.internal.generic.LADD;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final ImmutableList<String> FACINGS = ImmutableList.of("north", "south", "east", "west");
    public static final ImmutableList<String> ROTATIONS = ImmutableList
            .of(
                    "south", "south-southwest", "southwest", "west-southwest",
                    "west", "west-northwest", "northwest", "north-northwest",
                    "north", "north-northeast", "northeast", "east-northeast",
                    "east", "east-southeast", "southeast", "south-southeast"
            );
    public static final ImmutableList<String> AXIS = ImmutableList.of("x", "y", "z");
    public static final ImmutableList<String> COLORS = ImmutableList.of(
            "white",
            "orange",
            "magenta",
            "light_blue",
            "yellow",
            "lime",
            "pink",
            "gray",
            "silver",
            "cyan",
            "purple",
            "blue",
            "brown",
            "green",
            "red",
            "black"
    );

    private static boolean hasFlag(int value, int flag) {
        return (value & flag) == flag;
    }

    public static Map<String, Object> dataValueToBlockState(Material material, int value) {
        assert value >= 0 && value <= 16;
        Map<String, Object> blockStates = new HashMap<>();
        if (material == Material.ANVIL) {
            int damage = value / 4;
            blockStates.put("damage", damage);
            int mod = value % 4;
            if (mod == 0) {
                blockStates.put("facing", "north");
            } else if (mod == 1) {
                blockStates.put("facing", "east");
            } else if (damage == 0) {
                blockStates.put("facing", mod == 2 ? "south" : "west");
            } else {
                blockStates.put("facing", mod == 2 ? "west" : "south");
            }
        } else if (material == Material.STANDING_BANNER) {
            blockStates.put("rotation", ROTATIONS.get(value));
        } else if (material == Material.WALL_BANNER ||
                   material == Material.LADDER ||
                   material == Material.FURNACE ||
                   material == Material.CHEST ||
                   material == Material.TRAPPED_CHEST) {
            blockStates.put("facing", FACINGS.get(value - 2));
        } else if (material == Material.BED) {
            blockStates.put("part", hasFlag(value, 8) ? "head" : "foot");
            blockStates.put("occupied", hasFlag(value, 4));
            if (value == 0) {
                blockStates.put("facing", "south");
            } else if (value == 1) {
                blockStates.put("facing", "west");
            } else if (value == 2) {
                blockStates.put("facing", "north");
            } else {
                assert value == 3;
                blockStates.put("facing", "east");
            }
        } else if (material == Material.BEETROOT_BLOCK ||
                   material == Material.CARROT ||
                   material == Material.POTATO ||
                   material == Material.WHEAT ||
                   material == Material.CHORUS_FLOWER) {
            blockStates.put("age", value);
        } else if (material == Material.BONE_BLOCK) {
            int div = value / 4; // % 3 for super safety
            blockStates.put("axis", div == 0 ? "y" : div == 1 ? "x" : "z");
        } else if (material == Material.BREWING_STAND) {
            blockStates.put("has_bottle_2", hasFlag(value, 4));
            blockStates.put("has_bottle_1", hasFlag(value, 2));
            blockStates.put("has_bottle_0", hasFlag(value, 1));
        } else if (material == Material.STONE_BUTTON || material == Material.WOOD_BUTTON) {
            blockStates.put("powered", (value ^= 8) < 8); // assignment is needed here
            if (value == 0) {
                blockStates.put("facing", "down");
            } else if (value == 1) {
                blockStates.put("facing", "east");
            } else if (value == 2) {
                blockStates.put("facing", "west");
            } else if (value == 3) {
                blockStates.put("facing", "south");
            } else if (value == 4) {
                blockStates.put("facing", "north");
            } else {
                assert value == 5;
                blockStates.put("facing", "up");
            }
        } else if (material == Material.CACTUS || material == Material.SUGAR_CANE_BLOCK) {
            blockStates.put("age", value);
        } else if (material == Material.CAKE_BLOCK) {
            blockStates.put("bites", value);
        } else if (material == Material.CARPET ||
                   material == Material.WOOL ||
                   material == Material.STAINED_CLAY ||
                   material == Material.STAINED_GLASS ||
                   material == Material.STAINED_GLASS_PANE) {
            blockStates.put("color", COLORS.get(value));
        } else if (material == Material.CAULDRON) {
            blockStates.put("level", value);
        } else if (material == Material.COBBLE_WALL) {
            blockStates.put("variant", value == 1 ? "mossy_cobblestone" : "cobblestone");
        }
        return blockStates;
    }
}
