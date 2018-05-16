package io.github.syst3ms.lookup;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.material.Door;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final ImmutableMap<Character, String> FACINGS = ImmutableMap.<Character, String>builder()
            .put('u', "up")
            .put('d', "down")
            .put('n', "north")
            .put('s', "south")
            .put('e', "east")
            .put('w', "west")
            .build();
    public static final ImmutableList<String> ROTATIONS = ImmutableList
            .of(
                    "south", "south-southwest", "southwest", "west-southwest",
                    "west", "west-northwest", "northwest", "north-northwest",
                    "north", "north-northeast", "northeast", "east-northeast",
                    "east", "east-southeast", "southeast", "south-southeast"
            );

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

    private static String getFacing(String layout, int i) {
        return FACINGS.get(layout.charAt(i % layout.length()));
    }

    public static Map<String, Object> dataValueToBlockState(Material material, int value) {
        assert value >= 0 && value <= 16;
        Map<String, Object> blockStates = new HashMap<>();
        blockStates.put("material", material); // ID changes are accounted for
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
            blockStates.put("facing", getFacing("nsew", value - 2));
        } else if (material == Material.BED) {
            blockStates.put("part", hasFlag(value, 8) ? "head" : "foot");
            blockStates.put("occupied", hasFlag(value & 7, 4));
            blockStates.put("facing", getFacing("swne", value & 3));
        } else if (material == Material.BEETROOT_BLOCK ||
                   material == Material.CARROT ||
                   material == Material.POTATO ||
                   material == Material.WHEAT ||
                   material == Material.CHORUS_FLOWER) {
            blockStates.put("age", value);
        } else if (material == Material.BONE_BLOCK) {
            int div = value / 4; // % 3 for super safety
            blockStates.put("axis", Character.toString("yxz".charAt(value)));
        } else if (material == Material.BREWING_STAND) {
            blockStates.put("has_bottle_2", hasFlag(value, 4));
            blockStates.put("has_bottle_1", hasFlag(value, 2));
            blockStates.put("has_bottle_0", hasFlag(value, 1));
        } else if (material == Material.STONE_BUTTON || material == Material.WOOD_BUTTON) {
            blockStates.put("powered", hasFlag(value, 8)); // assignment is needed here
            blockStates.put("facing", getFacing("dewsnu", value & 7));
        } else if (material == Material.CACTUS || material == Material.SUGAR_CANE_BLOCK) {
            blockStates.put("age", value);
        } else if (material == Material.CAKE_BLOCK) {
            blockStates.put("bites", value);
        } else if (material == Material.CARPET ||
                   material == Material.WOOL ||
                   material == Material.STAINED_CLAY ||
                   material == Material.STAINED_GLASS ||
                   material == Material.STAINED_GLASS_PANE ||
                   material == Material.CONCRETE ||
                   material == Material.CONCRETE_POWDER) {
            blockStates.put("color", COLORS.get(value));
        } else if (material == Material.CAULDRON) {
            blockStates.put("level", value);
        } else if (material == Material.COBBLE_WALL) {
            blockStates.put("variant", value == 1 ? "mossy_cobblestone" : "cobblestone");
        } else if (material == Material.COCOA) {
            blockStates.put("age", value >> 2);
            blockStates.put("facing", getFacing("nesw", value & 3));
        } else if (material == Material.COMMAND || material == Material.COMMAND_CHAIN || material == Material.COMMAND_REPEATING) {
            blockStates.put("facing", getFacing("dunswe", value));
        } else if (material == Material.DAYLIGHT_DETECTOR || material == Material.DAYLIGHT_DETECTOR_INVERTED) {
            blockStates.put("power", value);
        } else if (material == Material.DIRT) {
            // Note that in 1.12, dirt uses a "variant" block state, whereas
            // in 1.13, dirt, coarse dirt and podzol are different blocks altogether
            // blockStates.put("material", value == 2 ? Material.PODZOL : value == 1 ? Material.COARSE_DIRT : material)
            // FIXME uncomment with Spigot 1.13
        } else if (material == Material.DISPENSER || material == Material.DROPPER) {
            // I was tempted to make a feminist joke here
            blockStates.put("triggered", hasFlag(value, 8));
            blockStates.put("facing", getFacing("dunswe", value & 7));
        } else if (material.getData() == Door.class) {
            //1111
            boolean upper = hasFlag(value, 8);
            blockStates.put("half", upper ? "upper" : "lower");
            // Love the fact that block states differ for top and bottom parts
            if (upper) {
                blockStates.put("powered", hasFlag(value, 2));
                blockStates.put("hinge", hasFlag(value, 1) ? "left" : "right");
            } else {
                blockStates.put("open", hasFlag(value, 4));
                blockStates.put("facing", getFacing("eswn", value & 3));
            }
        }
        return blockStates;
    }
}
