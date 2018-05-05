package io.github.syst3ms.lookup;

import com.google.common.collect.ImmutableList;
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

    public static Map<String, Object> dataValueToBlockState(Material material, int value) {
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
        } else if (material == Material.WALL_BANNER) {
            blockStates.put("facing", FACINGS.get(value - 2));
        }
        return blockStates;
    }
}
