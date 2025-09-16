/*
 * Copyright (c) 2019-2025 GeyserMC. http://geysermc.org
 * Copyright (c) 2025 ChronoGeyser Contributors. https://github.com/Villagers654/ChronoGeyser
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author ChronoGeyser Contributors
 * @link https://github.com/Villagers654/ChronoGeyser
 */

package org.geysermc.geyser.registry.populator.conversion;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.registry.type.GeyserMappingItem;

import java.util.List;
import java.util.Map;

public class Conversion748_729 {

    private static final Map<String, Integer> NEW_PLAYER_HEADS = Map.of(
        "minecraft:skeleton_skull", 0,
        "minecraft:wither_skeleton_skull", 1,
        "minecraft:zombie_head", 2,
        "minecraft:player_head", 3,
        "minecraft:creeper_head", 4,
        "minecraft:dragon_head", 5,
        "minecraft:piglin_head", 6
    );

    private static final List<String> WOOD_BLOCKS_NEEDING_STRIPPED_BIT = List.of(
        "minecraft:cherry_wood",
        "minecraft:mangrove_wood"
    );

    public static GeyserMappingItem remapItem(Item item, GeyserMappingItem mapping) {
        mapping = Conversion766_748.remapItem(item, mapping);
        String identifier = mapping.getBedrockIdentifier();

        if (NEW_PLAYER_HEADS.containsKey(identifier)) {
            return mapping.withBedrockIdentifier("minecraft:skull")
                .withBedrockData(NEW_PLAYER_HEADS.get(identifier));
        }

        if (identifier.equals("minecraft:mushroom_stem")) {
            return mapping.withBedrockIdentifier("minecraft:brown_mushroom_block");
        }

        return mapping;
    }

    public static NbtMap remapBlock(NbtMap tag) {
        tag = Conversion766_748.remapBlock(tag);

        // Add missing stripped_bit for non-stripped wood/hyphae blocks
        String name = tag.getString("name");
        if (WOOD_BLOCKS_NEEDING_STRIPPED_BIT.contains(name) && !tag.getCompound("states").containsKey("stripped_bit")) {
            NbtMap states = tag.getCompound("states");
            NbtMapBuilder statesBuilder = states.toBuilder();
            statesBuilder.putByte("stripped_bit", (byte) 0);
            tag = tag.toBuilder().putCompound("states", statesBuilder.build()).build();
        }

        if (NEW_PLAYER_HEADS.containsKey(name)) {
            return tag.toBuilder().putString("name", "minecraft:skull").build();
        }

        if (name.equals("minecraft:mushroom_stem")) {
            return tag.toBuilder().putString("name", "minecraft:brown_mushroom_block").build();
        }

        return tag;
    }
}
