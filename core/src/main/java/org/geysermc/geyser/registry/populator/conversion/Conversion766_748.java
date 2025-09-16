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
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.Blocks;
import org.geysermc.geyser.registry.type.GeyserMappingItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.geysermc.geyser.registry.populator.conversion.ConversionHelper.withName;
import static org.geysermc.geyser.registry.populator.conversion.ConversionHelper.withoutStates;

public class Conversion766_748 {
    static List<String> PALE_WOODEN_BLOCKS = new ArrayList<>();
    static List<String> OTHER_NEW_BLOCKS = new ArrayList<>();

    static {
        Set.of(
            Blocks.PALE_OAK_WOOD,
            Blocks.PALE_OAK_PLANKS,
            Blocks.PALE_OAK_SAPLING,
            Blocks.PALE_OAK_LOG,
            Blocks.STRIPPED_PALE_OAK_LOG,
            Blocks.STRIPPED_PALE_OAK_WOOD,
            Blocks.PALE_OAK_LEAVES,
            Blocks.PALE_OAK_HANGING_SIGN,
            Blocks.PALE_OAK_PRESSURE_PLATE,
            Blocks.PALE_OAK_TRAPDOOR,
            Blocks.PALE_OAK_BUTTON,
            Blocks.PALE_OAK_STAIRS,
            Blocks.PALE_OAK_SLAB,
            Blocks.PALE_OAK_FENCE_GATE,
            Blocks.PALE_OAK_FENCE,
            Blocks.PALE_OAK_DOOR
        ).forEach(block -> PALE_WOODEN_BLOCKS.add(block.javaIdentifier().value()));

        // Some things are of course stupid
        PALE_WOODEN_BLOCKS.add("pale_oak_standing_sign");
        PALE_WOODEN_BLOCKS.add("pale_oak_wall_sign");
        PALE_WOODEN_BLOCKS.add("pale_oak_double_slab");

        Set.of(
            Blocks.PALE_MOSS_BLOCK,
            Blocks.PALE_MOSS_CARPET,
            Blocks.PALE_HANGING_MOSS,

            Blocks.OPEN_EYEBLOSSOM,
            Blocks.CLOSED_EYEBLOSSOM,

            Blocks.RESIN_CLUMP,
            Blocks.RESIN_BLOCK,
            Blocks.RESIN_BRICKS,
            Blocks.RESIN_BRICK_STAIRS,
            Blocks.RESIN_BRICK_SLAB,
            Blocks.RESIN_BRICK_WALL,
            Blocks.CHISELED_RESIN_BRICKS,

            Blocks.CREAKING_HEART
        ).forEach(block -> OTHER_NEW_BLOCKS.add(block.javaIdentifier().value()));

        OTHER_NEW_BLOCKS.add("resin_brick_double_slab");
    }

    public static GeyserMappingItem remapItem(Item item, GeyserMappingItem mapping) {
        String identifier = mapping.getBedrockIdentifier().replace("minecraft:", "");
        if (PALE_WOODEN_BLOCKS.contains(identifier)) {
            return mapping.withBedrockIdentifier("minecraft:" + identifier.replace("pale_oak", "birch"));
        }

        if (OTHER_NEW_BLOCKS.contains(identifier)) {
            return switch (identifier) {
                case "resin_brick_double_slab" -> mapping.withBedrockIdentifier("minecraft:red_sandstone_double_slab");
                case "pale_moss_block" -> mapping.withBedrockIdentifier("minecraft:moss_block");
                case "pale_moss_carpet" -> mapping.withBedrockIdentifier("minecraft:moss_carpet");
                case "pale_hanging_moss" -> mapping.withBedrockIdentifier("minecraft:hanging_roots");
                case "open_eyeblossom" -> mapping.withBedrockIdentifier("minecraft:oxeye_daisy");
                case "closed_eyeblossom" -> mapping.withBedrockIdentifier("minecraft:white_tulip");
                case "resin_clump" -> mapping.withBedrockIdentifier("minecraft:raw_copper");
                case "resin_block" -> mapping.withBedrockIdentifier("minecraft:red_sandstone");
                case "resin_bricks" -> mapping.withBedrockIdentifier("minecraft:cut_red_sandstone");
                case "resin_brick_stairs" -> mapping.withBedrockIdentifier("minecraft:red_sandstone_stairs");
                case "resin_brick_slab" -> mapping.withBedrockIdentifier("minecraft:red_sandstone_slab");
                case "resin_brick_wall" -> mapping.withBedrockIdentifier("minecraft:red_sandstone_wall");
                case "chiseled_resin_bricks" -> mapping.withBedrockIdentifier("minecraft:chiseled_red_sandstone");
                case "creaking_heart" -> mapping.withBedrockIdentifier("minecraft:chiseled_polished_blackstone");
                case "resin_brick" -> mapping.withBedrockIdentifier("minecraft:brick");
                default -> throw new IllegalStateException("missing replacement for new item! " + identifier);
            };
        }

        return mapping;
    }

    public static NbtMap remapBlock(NbtMap tag) {
        tag = Conversion776_766.remapBlock(tag);

        String name = tag.getString("name").replace("minecraft:", "");
        if (PALE_WOODEN_BLOCKS.contains(name)) {
            return withName(tag, name.replace("pale_oak", "birch"));
        }

        if (OTHER_NEW_BLOCKS.contains(name)) {
            return switch (name) {
                case "resin_brick_double_slab" -> withName(tag,"red_sandstone_double_slab");
                case "pale_moss_block" -> withName(tag, "moss_block");
                case "pale_moss_carpet" -> withoutStates("moss_carpet");
                case "pale_hanging_moss" -> withoutStates("hanging_roots");
                case "open_eyeblossom" -> withoutStates("oxeye_daisy");
                case "closed_eyeblossom" -> withoutStates("white_tulip");
                case "resin_clump" -> withoutStates("unknown");
                case "resin_block" -> withoutStates("red_sandstone");
                case "resin_bricks" -> withoutStates("cut_red_sandstone");
                case "resin_brick_stairs" -> withName(tag, "red_sandstone_stairs");
                case "resin_brick_slab" -> withName(tag, "red_sandstone_slab");
                case "resin_brick_wall" -> withName(tag, "red_sandstone_wall");
                case "chiseled_resin_bricks" -> withName(tag, "chiseled_red_sandstone");
                case "creaking_heart" -> withoutStates("chiseled_polished_blackstone");
                default -> throw new IllegalStateException("missing replacement for new block! " + name);
            };
        }

        return tag;
    }
}
