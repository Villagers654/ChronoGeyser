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

package org.geysermc.geyser.registry.populator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.bedrock.codec.v671.Bedrock_v671;
import org.cloudburstmc.protocol.bedrock.codec.v685.Bedrock_v685;
import org.cloudburstmc.protocol.bedrock.codec.v712.Bedrock_v712;
import org.cloudburstmc.protocol.bedrock.codec.v729.Bedrock_v729;
import org.cloudburstmc.protocol.bedrock.codec.v748.Bedrock_v748;
import org.cloudburstmc.protocol.bedrock.codec.v766.Bedrock_v766;
import org.cloudburstmc.protocol.bedrock.codec.v776.Bedrock_v776;
import org.cloudburstmc.protocol.bedrock.codec.v786.Bedrock_v786;
import org.cloudburstmc.protocol.bedrock.codec.v800.Bedrock_v800;
import org.cloudburstmc.protocol.bedrock.codec.v818.Bedrock_v818;
import org.cloudburstmc.protocol.bedrock.codec.v819.Bedrock_v819;
import org.cloudburstmc.protocol.bedrock.codec.v827.Bedrock_v827;
import org.cloudburstmc.protocol.bedrock.codec.v844.Bedrock_v844;
import org.cloudburstmc.protocol.bedrock.codec.v859.Bedrock_v859;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.CreativeItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.CreativeItemGroup;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemVersion;
import org.geysermc.geyser.Constants;
import org.geysermc.geyser.GeyserBootstrap;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.block.custom.CustomBlockData;
import org.geysermc.geyser.api.block.custom.CustomBlockState;
import org.geysermc.geyser.api.block.custom.NonVanillaCustomBlockData;
import org.geysermc.geyser.api.item.custom.CustomItemData;
import org.geysermc.geyser.api.item.custom.CustomItemOptions;
import org.geysermc.geyser.api.item.custom.NonVanillaCustomItemData;
import org.geysermc.geyser.inventory.item.StoredItemMappings;
import org.geysermc.geyser.item.GeyserCustomMappingData;
import org.geysermc.geyser.item.Items;
import org.geysermc.geyser.item.type.BlockItem;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.level.block.property.Properties;
import org.geysermc.geyser.network.GameProtocol;
import org.geysermc.geyser.registry.BlockRegistries;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.registry.populator.conversion.Conversion685_671;
import org.geysermc.geyser.registry.populator.conversion.Conversion712_685;
import org.geysermc.geyser.registry.populator.conversion.Conversion729_712;
import org.geysermc.geyser.registry.populator.conversion.Conversion748_729;
import org.geysermc.geyser.registry.populator.conversion.Conversion766_748;
import org.geysermc.geyser.registry.populator.conversion.Conversion844_827;
import org.geysermc.geyser.registry.type.BlockMappings;
import org.geysermc.geyser.registry.type.GeyserBedrockBlock;
import org.geysermc.geyser.registry.type.GeyserMappingItem;
import org.geysermc.geyser.registry.type.ItemMapping;
import org.geysermc.geyser.registry.type.ItemMappings;
import org.geysermc.geyser.registry.type.NonVanillaItemRegistration;
import org.geysermc.geyser.registry.type.PaletteItem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Populates the item registries.
 */
public class ItemRegistryPopulator {

    record PaletteVersion(String version, int protocolVersion, Map<Item, Item> javaOnlyItems, Remapper remapper) {

        public PaletteVersion(String version, int protocolVersion) {
            this(version, protocolVersion, Collections.emptyMap(), (item, mapping) -> mapping);
        }

        public PaletteVersion(String version, int protocolVersion, Map<Item, Item> javaOnlyItems) {
            this(version, protocolVersion, javaOnlyItems, (item, mapping) -> mapping);
        }

        public PaletteVersion(String version, int protocolVersion, Remapper remapper) {
            this(version, protocolVersion, Collections.emptyMap(), remapper);
        }
    }

    @FunctionalInterface
    interface Remapper {
        @NonNull
        GeyserMappingItem remap(Item item, GeyserMappingItem mapping);
    }

    private static final Map<Integer, Map<Item, Item>> FALLBACKS_BY_PROTOCOL = new HashMap<>();

    static {
        /*
         * Build fallback maps in a consistent, descending chain so that lower (older)
         * protocol versions inherit all mappings from higher (newer) protocol versions.
         *
         * Naming convention: fallbacks_v<bedrock_version_number> (e.g. fallbacks_v827)
         * Registration uses Bedrock_vXXX.CODEC.getProtocolVersion() as the map key.
         */

        // 1.21.100 / v827 - base for newer fallbacks (contains many copper/shelf -> fallback mappings)
        Map<Item, Item> fallbacks_v827 = new HashMap<>();
        fallbacks_v827.put(Items.ACACIA_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.BAMBOO_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.BIRCH_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.CHERRY_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.CRIMSON_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.DARK_OAK_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.JUNGLE_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.MANGROVE_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.OAK_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.PALE_OAK_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.SPRUCE_SHELF, Items.CHISELED_BOOKSHELF);
        fallbacks_v827.put(Items.WARPED_SHELF, Items.CHISELED_BOOKSHELF);

        fallbacks_v827.put(Items.COPPER_BARS, Items.IRON_BARS);
        fallbacks_v827.put(Items.EXPOSED_COPPER_BARS, Items.IRON_BARS);
        fallbacks_v827.put(Items.WEATHERED_COPPER_BARS, Items.IRON_BARS);
        fallbacks_v827.put(Items.OXIDIZED_COPPER_BARS, Items.IRON_BARS);
        fallbacks_v827.put(Items.WAXED_COPPER_BARS, Items.IRON_BARS);
        fallbacks_v827.put(Items.WAXED_EXPOSED_COPPER_BARS, Items.IRON_BARS);
        fallbacks_v827.put(Items.WAXED_WEATHERED_COPPER_BARS, Items.IRON_BARS);
        fallbacks_v827.put(Items.WAXED_OXIDIZED_COPPER_BARS, Items.IRON_BARS);

        fallbacks_v827.put(Items.COPPER_GOLEM_STATUE, Items.ARMOR_STAND);
        fallbacks_v827.put(Items.EXPOSED_COPPER_GOLEM_STATUE, Items.ARMOR_STAND);
        fallbacks_v827.put(Items.WEATHERED_COPPER_GOLEM_STATUE, Items.ARMOR_STAND);
        fallbacks_v827.put(Items.OXIDIZED_COPPER_GOLEM_STATUE, Items.ARMOR_STAND);
        fallbacks_v827.put(Items.WAXED_COPPER_GOLEM_STATUE, Items.ARMOR_STAND);
        fallbacks_v827.put(Items.WAXED_EXPOSED_COPPER_GOLEM_STATUE, Items.ARMOR_STAND);
        fallbacks_v827.put(Items.WAXED_WEATHERED_COPPER_GOLEM_STATUE, Items.ARMOR_STAND);
        fallbacks_v827.put(Items.WAXED_OXIDIZED_COPPER_GOLEM_STATUE, Items.ARMOR_STAND);

        fallbacks_v827.put(Items.COPPER_LANTERN, Items.LANTERN);
        fallbacks_v827.put(Items.EXPOSED_COPPER_LANTERN, Items.LANTERN);
        fallbacks_v827.put(Items.WEATHERED_COPPER_LANTERN, Items.LANTERN);
        fallbacks_v827.put(Items.OXIDIZED_COPPER_LANTERN, Items.LANTERN);
        fallbacks_v827.put(Items.WAXED_COPPER_LANTERN, Items.LANTERN);
        fallbacks_v827.put(Items.WAXED_EXPOSED_COPPER_LANTERN, Items.LANTERN);
        fallbacks_v827.put(Items.WAXED_WEATHERED_COPPER_LANTERN, Items.LANTERN);
        fallbacks_v827.put(Items.WAXED_OXIDIZED_COPPER_LANTERN, Items.LANTERN);

        fallbacks_v827.put(Items.EXPOSED_LIGHTNING_ROD, Items.LIGHTNING_ROD);
        fallbacks_v827.put(Items.WEATHERED_LIGHTNING_ROD, Items.LIGHTNING_ROD);
        fallbacks_v827.put(Items.OXIDIZED_LIGHTNING_ROD, Items.LIGHTNING_ROD);
        fallbacks_v827.put(Items.WAXED_LIGHTNING_ROD, Items.LIGHTNING_ROD);
        fallbacks_v827.put(Items.WAXED_EXPOSED_LIGHTNING_ROD, Items.LIGHTNING_ROD);
        fallbacks_v827.put(Items.WAXED_WEATHERED_LIGHTNING_ROD, Items.LIGHTNING_ROD);
        fallbacks_v827.put(Items.WAXED_OXIDIZED_LIGHTNING_ROD, Items.LIGHTNING_ROD);

        fallbacks_v827.put(Items.COPPER_TORCH, Items.TORCH);
        fallbacks_v827.put(Items.COPPER_HORSE_ARMOR, Items.LEATHER_HORSE_ARMOR);

        // 1.21.93 / v819 - inherit everything from v827, then add/override v819-specific entries if needed
        Map<Item, Item> fallbacks_v819 = new HashMap<>(fallbacks_v827);
        // (no unique additions for v819 in the original except later use in paletteVersions; keep for future additions)

        // 1.21.90 / v818 - inherit from v819, then add the single MUSIC_DISC_LAVA_CHICKEN mapping
        Map<Item, Item> fallbacks_v818 = new HashMap<>(fallbacks_v819);
        fallbacks_v818.put(Items.MUSIC_DISC_LAVA_CHICKEN, Items.MUSIC_DISC_CHIRP);

        // 1.21.80 / v800 - inherit from v818 and add the MUSIC_DISC_TEARS mapping that existed for 1.21.80
        Map<Item, Item> fallbacks_v800 = new HashMap<>(fallbacks_v818);
        fallbacks_v800.put(Items.MUSIC_DISC_TEARS, Items.MUSIC_DISC_5);

        // 1.21.70 / v786 - inherit from v800, then add harnesses + ghast items
        Map<Item, Item> fallbacks_v786 = new HashMap<>(fallbacks_v800);
        fallbacks_v786.put(Items.BLACK_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.BLUE_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.BROWN_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.RED_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.GREEN_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.YELLOW_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.ORANGE_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.MAGENTA_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.LIGHT_BLUE_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.LIME_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.PINK_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.GRAY_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.CYAN_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.PURPLE_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.LIGHT_GRAY_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.WHITE_HARNESS, Items.SADDLE);
        fallbacks_v786.put(Items.HAPPY_GHAST_SPAWN_EGG, Items.EGG);
        fallbacks_v786.put(Items.DRIED_GHAST, Items.PLAYER_HEAD);

        // 1.21.60 / v776 - inherit from v786, then add various plant/egg fallbacks
        Map<Item, Item> fallbacks_v776 = new HashMap<>(fallbacks_v786);
        fallbacks_v776.put(Items.BUSH, Items.SHORT_GRASS);
        fallbacks_v776.put(Items.CACTUS_FLOWER, Items.BUBBLE_CORAL_FAN);
        fallbacks_v776.put(Items.FIREFLY_BUSH, Items.SHORT_GRASS);
        fallbacks_v776.put(Items.LEAF_LITTER, Items.PINK_PETALS);
        fallbacks_v776.put(Items.SHORT_DRY_GRASS, Items.DEAD_BUSH);
        fallbacks_v776.put(Items.TALL_DRY_GRASS, Items.TALL_GRASS);
        fallbacks_v776.put(Items.WILDFLOWERS, Items.PINK_PETALS);
        fallbacks_v776.put(Items.BLUE_EGG, Items.EGG);
        fallbacks_v776.put(Items.BROWN_EGG, Items.EGG);

        // 1.21.50 / v766 - inherit from v776, then add test blocks
        Map<Item, Item> fallbacks_v766 = new HashMap<>(fallbacks_v776);
        fallbacks_v766.put(Items.TEST_BLOCK, Items.STRUCTURE_BLOCK);
        fallbacks_v766.put(Items.TEST_INSTANCE_BLOCK, Items.JIGSAW);

        // 1.21.40 / v748 - inherit from v766, then add pale oak/resin/etc fallbacks (the "1.21.40" set)
        Map<Item, Item> fallbacks_v748 = new HashMap<>(fallbacks_v766);
        fallbacks_v748.put(Items.PALE_OAK_PLANKS, Items.BIRCH_PLANKS);
        fallbacks_v748.put(Items.PALE_OAK_FENCE, Items.BIRCH_FENCE);
        fallbacks_v748.put(Items.PALE_OAK_FENCE_GATE, Items.BIRCH_FENCE_GATE);
        fallbacks_v748.put(Items.PALE_OAK_STAIRS, Items.BIRCH_STAIRS);
        fallbacks_v748.put(Items.PALE_OAK_DOOR, Items.BIRCH_DOOR);
        fallbacks_v748.put(Items.PALE_OAK_TRAPDOOR, Items.BIRCH_TRAPDOOR);
        fallbacks_v748.put(Items.PALE_OAK_SLAB, Items.BIRCH_SLAB);
        fallbacks_v748.put(Items.PALE_OAK_LOG, Items.BIRCH_LOG);
        fallbacks_v748.put(Items.STRIPPED_PALE_OAK_LOG, Items.STRIPPED_BIRCH_LOG);
        fallbacks_v748.put(Items.PALE_OAK_WOOD, Items.BIRCH_WOOD);
        fallbacks_v748.put(Items.PALE_OAK_LEAVES, Items.BIRCH_LEAVES);
        fallbacks_v748.put(Items.PALE_OAK_SAPLING, Items.BIRCH_SAPLING);
        fallbacks_v748.put(Items.STRIPPED_PALE_OAK_WOOD, Items.STRIPPED_BIRCH_WOOD);
        fallbacks_v748.put(Items.PALE_OAK_SIGN, Items.BIRCH_SIGN);
        fallbacks_v748.put(Items.PALE_OAK_HANGING_SIGN, Items.BIRCH_HANGING_SIGN);
        fallbacks_v748.put(Items.PALE_OAK_BOAT, Items.BIRCH_BOAT);
        fallbacks_v748.put(Items.PALE_OAK_CHEST_BOAT, Items.BIRCH_CHEST_BOAT);
        fallbacks_v748.put(Items.PALE_OAK_BUTTON, Items.BIRCH_BUTTON);
        fallbacks_v748.put(Items.PALE_OAK_PRESSURE_PLATE, Items.BIRCH_PRESSURE_PLATE);
        fallbacks_v748.put(Items.RESIN_CLUMP, Items.RAW_COPPER);
        fallbacks_v748.put(Items.RESIN_BRICK_WALL, Items.RED_SANDSTONE_WALL);
        fallbacks_v748.put(Items.RESIN_BRICK_STAIRS, Items.RED_SANDSTONE_STAIRS);
        fallbacks_v748.put(Items.RESIN_BRICK_SLAB, Items.RED_SANDSTONE_SLAB);
        fallbacks_v748.put(Items.RESIN_BLOCK, Items.RED_SANDSTONE);
        fallbacks_v748.put(Items.RESIN_BRICK, Items.BRICK);
        fallbacks_v748.put(Items.RESIN_BRICKS, Items.CUT_RED_SANDSTONE);
        fallbacks_v748.put(Items.CHISELED_RESIN_BRICKS, Items.CHISELED_RED_SANDSTONE);
        fallbacks_v748.put(Items.CLOSED_EYEBLOSSOM, Items.WHITE_TULIP);
        fallbacks_v748.put(Items.OPEN_EYEBLOSSOM, Items.OXEYE_DAISY);
        fallbacks_v748.put(Items.PALE_MOSS_BLOCK, Items.MOSS_BLOCK);
        fallbacks_v748.put(Items.PALE_MOSS_CARPET, Items.MOSS_CARPET);
        fallbacks_v748.put(Items.PALE_HANGING_MOSS, Items.HANGING_ROOTS);
        fallbacks_v748.put(Items.CREAKING_HEART, Items.CHISELED_POLISHED_BLACKSTONE);
        fallbacks_v748.put(Items.CREAKING_SPAWN_EGG, Items.HOGLIN_SPAWN_EGG);

        // Register all maps by their protocol versions so getFallbacks(...) works consistently
        FALLBACKS_BY_PROTOCOL.put(Bedrock_v827.CODEC.getProtocolVersion(), fallbacks_v827);
        FALLBACKS_BY_PROTOCOL.put(Bedrock_v819.CODEC.getProtocolVersion(), fallbacks_v819);
        FALLBACKS_BY_PROTOCOL.put(Bedrock_v818.CODEC.getProtocolVersion(), fallbacks_v818);
        FALLBACKS_BY_PROTOCOL.put(Bedrock_v800.CODEC.getProtocolVersion(), fallbacks_v800);
        FALLBACKS_BY_PROTOCOL.put(Bedrock_v786.CODEC.getProtocolVersion(), fallbacks_v786);
        FALLBACKS_BY_PROTOCOL.put(Bedrock_v776.CODEC.getProtocolVersion(), fallbacks_v776);
        FALLBACKS_BY_PROTOCOL.put(Bedrock_v766.CODEC.getProtocolVersion(), fallbacks_v766);
        FALLBACKS_BY_PROTOCOL.put(Bedrock_v748.CODEC.getProtocolVersion(), fallbacks_v748);
    }

    /**
     * Returns the item fallback map for the given protocol version.
     *
     * @param protocolVersion protocol version number
     * @return fallback mapping for items, or an empty map if none exist
     */
    public static Map<Item, Item> getFallbacks(int protocolVersion) {
        return FALLBACKS_BY_PROTOCOL.getOrDefault(protocolVersion, Collections.emptyMap());
    }

    public static void populate() {
        List<Item> bundles = List.of(Items.BUNDLE, Items.BLACK_BUNDLE, Items.BLUE_BUNDLE, Items.BROWN_BUNDLE, Items.CYAN_BUNDLE, Items.GRAY_BUNDLE,
            Items.GREEN_BUNDLE, Items.LIGHT_BLUE_BUNDLE, Items.LIGHT_GRAY_BUNDLE, Items.LIME_BUNDLE, Items.MAGENTA_BUNDLE, Items.ORANGE_BUNDLE, Items.RED_BUNDLE,
            Items.PINK_BUNDLE, Items.PURPLE_BUNDLE, Items.WHITE_BUNDLE, Items.YELLOW_BUNDLE);
        Map<Item, Item> pre1_21_2Items = new HashMap<>(getFallbacks(Bedrock_v748.CODEC.getProtocolVersion()));
        bundles.forEach(bundle -> pre1_21_2Items.put(bundle, Items.SHULKER_SHELL));

        List<PaletteVersion> paletteVersions = new ArrayList<>();
        paletteVersions.add(new PaletteVersion("1_20_80", Bedrock_v671.CODEC.getProtocolVersion(), pre1_21_2Items, Conversion685_671::remapItem));
        paletteVersions.add(new PaletteVersion("1_21_0", Bedrock_v685.CODEC.getProtocolVersion(), pre1_21_2Items, Conversion712_685::remapItem));
        paletteVersions.add(new PaletteVersion("1_21_20", Bedrock_v712.CODEC.getProtocolVersion(), pre1_21_2Items, Conversion729_712::remapItem));
        paletteVersions.add(new PaletteVersion("1_21_30", Bedrock_v729.CODEC.getProtocolVersion(), pre1_21_2Items, Conversion748_729::remapItem));
        paletteVersions.add(new PaletteVersion("1_21_40", Bedrock_v748.CODEC.getProtocolVersion(), getFallbacks(Bedrock_v748.CODEC.getProtocolVersion()), Conversion766_748::remapItem));
        paletteVersions.add(new PaletteVersion("1_21_50", Bedrock_v766.CODEC.getProtocolVersion(), getFallbacks(Bedrock_v766.CODEC.getProtocolVersion())));
        paletteVersions.add(new PaletteVersion("1_21_60", Bedrock_v776.CODEC.getProtocolVersion(), getFallbacks(Bedrock_v776.CODEC.getProtocolVersion())));
        paletteVersions.add(new PaletteVersion("1_21_70", Bedrock_v786.CODEC.getProtocolVersion(), getFallbacks(Bedrock_v786.CODEC.getProtocolVersion())));
        paletteVersions.add(new PaletteVersion("1_21_80", Bedrock_v800.CODEC.getProtocolVersion(), getFallbacks(Bedrock_v800.CODEC.getProtocolVersion())));
        paletteVersions.add(new PaletteVersion("1_21_90", Bedrock_v818.CODEC.getProtocolVersion(), getFallbacks(Bedrock_v818.CODEC.getProtocolVersion()), Conversion844_827::remapItem));
        paletteVersions.add(new PaletteVersion("1_21_93", Bedrock_v819.CODEC.getProtocolVersion(), getFallbacks(Bedrock_v819.CODEC.getProtocolVersion()), Conversion844_827::remapItem));
        paletteVersions.add(new PaletteVersion("1_21_100", Bedrock_v827.CODEC.getProtocolVersion(), getFallbacks(Bedrock_v827.CODEC.getProtocolVersion()), Conversion844_827::remapItem));
        paletteVersions.add(new PaletteVersion("1_21_110", Bedrock_v844.CODEC.getProtocolVersion()));
        paletteVersions.add(new PaletteVersion("1_21_120", Bedrock_v859.CODEC.getProtocolVersion()));

        GeyserBootstrap bootstrap = GeyserImpl.getInstance().getBootstrap();

        TypeReference<Map<String, GeyserMappingItem>> mappingItemsType = new TypeReference<>() { };

        Map<String, GeyserMappingItem> items;
        try (InputStream stream = bootstrap.getResourceOrThrow("mappings/items.json")) {
            // Load item mappings from Java Edition to Bedrock Edition
            items = GeyserImpl.JSON_MAPPER.readValue(stream, mappingItemsType);
        } catch (Exception e) {
            throw new AssertionError("Unable to load Java runtime item IDs", e);
        }

        boolean customItemsAllowed = GeyserImpl.getInstance().getConfig().isAddNonBedrockItems();

        // List values here is important compared to HashSet - we need to preserve the order of what's given to us
        // (as of 1.19.2 Java) to replicate some edge cases in Java predicate behavior where it checks from the bottom
        // of the list first, then ascends.
        Multimap<String, CustomItemData> customItems = MultimapBuilder.hashKeys().arrayListValues().build();
        List<NonVanillaCustomItemData> nonVanillaCustomItems = customItemsAllowed ? new ObjectArrayList<>() : Collections.emptyList();

        if (customItemsAllowed) {
            CustomItemRegistryPopulator.populate(items, customItems, nonVanillaCustomItems);
        }

        // We can reduce some operations as Java information is the same across all palette versions
        boolean firstMappingsPass = true;

        /* Load item palette */
        for (PaletteVersion palette : paletteVersions) {
            TypeReference<List<PaletteItem>> paletteEntriesType = new TypeReference<>() {};

            List<PaletteItem> itemEntries;
            try (InputStream stream = bootstrap.getResourceOrThrow(String.format("bedrock/runtime_item_states.%s.json", palette.version()))) {
                itemEntries = GeyserImpl.JSON_MAPPER.readValue(stream, paletteEntriesType);
            } catch (Exception e) {
                throw new AssertionError("Unable to load Bedrock runtime item IDs", e);
            }

            NbtMap vanillaComponents;
            try (InputStream stream = bootstrap.getResourceOrThrow("bedrock/item_components.%s.nbt".formatted(palette.version()))) {
                vanillaComponents = (NbtMap) NbtUtils.createGZIPReader(stream, true, true).readTag();
            } catch (Exception e) {
                throw new AssertionError("Unable to load Bedrock item components", e);
            }

            // Used for custom items
            int nextFreeBedrockId = 0;
            Int2ObjectMap<ItemDefinition> registry = new Int2ObjectOpenHashMap<>();
            Map<String, ItemDefinition> definitions = new Object2ObjectLinkedOpenHashMap<>();

            for (PaletteItem entry : itemEntries) {
                int id = entry.getId();
                if (id >= nextFreeBedrockId) {
                    nextFreeBedrockId = id + 1;
                }

                // Some items, e.g. food, are not component based but still have components
                NbtMap components = vanillaComponents.getCompound(entry.getName());
                if (components == null && entry.isComponentBased()) {
                    // FIXME needs a proper item components file update
                    if (!entry.getName().contains("lava_chicken")) {
                        throw new RuntimeException("Could not find vanilla components for vanilla component based item! " + entry.getName());
                    } else {
                        components = NbtMap.EMPTY;
                    }
                }

                ItemDefinition definition = new SimpleItemDefinition(entry.getName().intern(), id, ItemVersion.from(entry.getVersion()), entry.isComponentBased(), components);
                definitions.put(entry.getName(), definition);
                registry.put(definition.getRuntimeId(), definition);
            }

            Object2ObjectMap<String, BlockDefinition> bedrockBlockIdOverrides = new Object2ObjectOpenHashMap<>();
            Object2IntMap<String> blacklistedIdentifiers = new Object2IntOpenHashMap<>();

            Object2ObjectMap<CustomBlockData, ItemDefinition> customBlockItemDefinitions = new Object2ObjectOpenHashMap<>();

            List<ItemDefinition> buckets = new ObjectArrayList<>();

            List<ItemMapping> mappings = new ObjectArrayList<>();
            // Temporary mapping to create stored items
            Map<Item, ItemMapping> javaItemToMapping = new Object2ObjectOpenHashMap<>();

            List<CreativeItemData> creativeItems = new ArrayList<>();
            Set<String> noBlockDefinitions = new ObjectOpenHashSet<>();

            // Fix: Usage of structure blocks/voids in recipes
            // https://github.com/GeyserMC/Geyser/issues/2890
            noBlockDefinitions.add("minecraft:structure_block");
            noBlockDefinitions.add("minecraft:structure_void");

            AtomicInteger creativeNetId = new AtomicInteger();
            CreativeItemRegistryPopulator.populate(palette, definitions, items, (itemBuilder, groupId) -> {
                ItemData item = itemBuilder.netId(creativeNetId.incrementAndGet()).build();
                creativeItems.add(new CreativeItemData(item, item.getNetId(), groupId));

                if (item.getBlockDefinition() != null) {
                    String identifier = item.getDefinition().getIdentifier();

                    // Add override for item mapping, unless it already exists... then we know multiple states can exist
                    if (!blacklistedIdentifiers.containsKey(identifier)) {
                        if (bedrockBlockIdOverrides.containsKey(identifier)) {
                            bedrockBlockIdOverrides.remove(identifier);
                            // Save this as a blacklist, but also as knowledge of what the block state name should be
                            blacklistedIdentifiers.put(identifier, item.getBlockDefinition().getRuntimeId());
                        } else {
                            // Unless there's multiple possibilities for this one state, let this be
                            bedrockBlockIdOverrides.put(identifier, item.getBlockDefinition());
                        }
                    }
                } else {
                    // Item mappings should also NOT have a block definition for these.
                    noBlockDefinitions.add(item.getDefinition().getIdentifier());
                }
            });

            List<CreativeItemGroup> creativeItemGroups;
            if (palette.protocolVersion < 776) {
                creativeItemGroups = new ArrayList<>();
            } else {
                creativeItemGroups = CreativeItemRegistryPopulator.readCreativeItemGroups(palette, creativeItems);
            }

            BlockMappings blockMappings = BlockRegistries.BLOCKS.forVersion(palette.protocolVersion());

            Set<Item> javaOnlyItems = new ObjectOpenHashSet<>();
            Collections.addAll(javaOnlyItems, Items.SPECTRAL_ARROW, Items.DEBUG_STICK,
                    Items.KNOWLEDGE_BOOK, Items.TIPPED_ARROW);
            if (!customItemsAllowed) {
                javaOnlyItems.add(Items.FURNACE_MINECART);
            }
            // Java-only items for this version
            javaOnlyItems.addAll(palette.javaOnlyItems().keySet());

            Int2ObjectMap<String> customIdMappings = new Int2ObjectOpenHashMap<>();
            Set<String> registeredItemNames = new ObjectOpenHashSet<>(); // This is used to check for duplicate item names

            for (Map.Entry<String, GeyserMappingItem> entry : items.entrySet()) {
                Item javaItem = Registries.JAVA_ITEM_IDENTIFIERS.get(entry.getKey());
                if (javaItem == null) {
                    throw new RuntimeException("Extra item in mappings? " + entry.getKey());
                }
                GeyserMappingItem mappingItem;
                Item replacementItem = palette.javaOnlyItems().get(javaItem);
                if (replacementItem != null) {
                    mappingItem = items.get(replacementItem.javaIdentifier()); // java only item, a java id fallback has been provided
                } else {
                    // check if any mapping changes need to be made on this version
                    mappingItem = palette.remapper().remap(javaItem, entry.getValue());
                }

                if (customItemsAllowed && javaItem == Items.FURNACE_MINECART) {
                    // Will be added later
                    mappings.add(null);
                    continue;
                }

                String bedrockIdentifier = mappingItem.getBedrockIdentifier();
                ItemDefinition definition = definitions.get(bedrockIdentifier);
                if (definition == null) {
                    throw new RuntimeException("Missing Bedrock ItemDefinition in version " + palette.version() + " for mapping: " + mappingItem);
                }

                BlockDefinition bedrockBlock = null;
                Integer firstBlockRuntimeId = entry.getValue().getFirstBlockRuntimeId();
                BlockDefinition customBlockItemOverride = null;
                if (firstBlockRuntimeId != null) {
                    BlockDefinition blockOverride = bedrockBlockIdOverrides.get(bedrockIdentifier);

                    // We'll do this here for custom blocks we want in the creative inventory so we can piggyback off the existing logic to find these
                    // blocks in creativeItems
                    CustomBlockData customBlockData = BlockRegistries.CUSTOM_BLOCK_ITEM_OVERRIDES.getOrDefault(javaItem.javaIdentifier(), null);
                    if (customBlockData != null) {
                        // this block has a custom item override and thus we should use its runtime ID for the ItemMapping
                        if (customBlockData.includedInCreativeInventory()) {
                            CustomBlockState customBlockState = customBlockData.defaultBlockState();
                            customBlockItemOverride = blockMappings.getCustomBlockStateDefinitions().getOrDefault(customBlockState, null);
                        }
                    }

                    // If it's a custom block we can't do this because we need to make sure we find the creative item
                    if (blockOverride != null && customBlockItemOverride == null) {
                        // Straight from BDS is our best chance of getting an item that doesn't run into issues
                        bedrockBlock = blockOverride;
                    } else {
                        // Try to get an example block runtime ID from the creative contents packet, for Bedrock identifier obtaining
                        int aValidBedrockBlockId = blacklistedIdentifiers.getOrDefault(bedrockIdentifier, customBlockItemOverride != null ? customBlockItemOverride.getRuntimeId() : -1);
                        if (aValidBedrockBlockId == -1 && customBlockItemOverride == null) {
                            // Fallback
                            if (!noBlockDefinitions.contains(entry.getValue().getBedrockIdentifier())) {
                                bedrockBlock = blockMappings.getBedrockBlock(firstBlockRuntimeId);
                            }
                        } else {
                            // As of 1.16.220, every item requires a block runtime ID attached to it.
                            // This is mostly for identifying different blocks with the same item ID - wool, slabs, some walls.
                            // However, in order for some visuals and crafting to work, we need to send the first matching block state
                            // as indexed by Bedrock's block palette
                            // There are exceptions! But, ideally, the block ID override should take care of those.
                            NbtMapBuilder requiredBlockStatesBuilder = NbtMap.builder();
                            String correctBedrockIdentifier = blockMappings.getDefinition(aValidBedrockBlockId).getState().getString("name");
                            boolean firstPass = true;
                            // Block states are all grouped together. In the mappings, we store the first block runtime ID in order,
                            // and the last, if relevant. We then iterate over all those values and get their Bedrock equivalents
                            int lastBlockRuntimeId = entry.getValue().getLastBlockRuntimeId() == null ? firstBlockRuntimeId : entry.getValue().getLastBlockRuntimeId();
                            for (int i = firstBlockRuntimeId; i <= lastBlockRuntimeId; i++) {
                                GeyserBedrockBlock bedrockBlockRuntimeId = blockMappings.getVanillaBedrockBlock(i);
                                NbtMap blockTag = bedrockBlockRuntimeId.getState();
                                String bedrockName = blockTag.getString("name");
                                if (!bedrockName.equals(correctBedrockIdentifier)) {
                                    continue;
                                }
                                NbtMap states = blockTag.getCompound("states");

                                if (firstPass) {
                                    firstPass = false;
                                    if (states.isEmpty()) {
                                        // No need to iterate and find all block states - this is the one, as there can't be any others
                                        bedrockBlock = bedrockBlockRuntimeId;
                                        break;
                                    }
                                    requiredBlockStatesBuilder.putAll(states);
                                    continue;
                                }
                                for (Map.Entry<String, Object> nbtEntry : states.entrySet()) {
                                    Object value = requiredBlockStatesBuilder.get(nbtEntry.getKey());
                                    if (value != null && !nbtEntry.getValue().equals(value)) { // Null means this value has already been removed/deemed as unneeded
                                        // This state can change between different block states, and therefore is not required
                                        // to build a successful block state of this
                                        requiredBlockStatesBuilder.remove(nbtEntry.getKey());
                                    }
                                }
                                if (requiredBlockStatesBuilder.isEmpty()) {
                                    // There are no required block states
                                    // E.G. there was only a direction property that is no longer in play
                                    // (States that are important include color for glass)
                                    break;
                                }
                            }

                            NbtMap requiredBlockStates = requiredBlockStatesBuilder.build();
                            if (bedrockBlock == null) {
                                // We need to loop around again (we can't cache the block tags above) because Bedrock can include states that we don't have a pairing for
                                // in it's "preferred" block state - I.E. the first matching block state in the list
                                for (GeyserBedrockBlock block : blockMappings.getBedrockRuntimeMap()) {
                                    if (block == null) {
                                        continue;
                                    }
                                    NbtMap blockTag = block.getState();
                                    if (blockTag.getString("name").equals(correctBedrockIdentifier)) {
                                        NbtMap states = blockTag.getCompound("states");
                                        boolean valid = true;
                                        for (Map.Entry<String, Object> nbtEntry : requiredBlockStates.entrySet()) {
                                            if (!states.get(nbtEntry.getKey()).equals(nbtEntry.getValue())) {
                                                // A required block state doesn't match - this one is not valid
                                                valid = false;
                                                break;
                                            }
                                        }
                                        if (valid) {
                                            bedrockBlock = block;
                                            break;
                                        }
                                    }
                                }
                                if (bedrockBlock == null) {
                                    throw new RuntimeException("Could not find a block match for " + entry.getKey());
                                }
                            }

                            // Because we have replaced the Bedrock block ID, we also need to replace the creative contents block runtime ID
                            // That way, creative items work correctly for these blocks

                            // Set our custom block override now if there is one
                            if (customBlockItemOverride != null) {
                                bedrockBlock = customBlockItemOverride;
                            }

                            for (int j = 0; j < creativeItems.size(); j++) {
                                CreativeItemData itemData = creativeItems.get(j);
                                if (itemData.getItem().getDefinition().equals(definition)) {
                                    if (itemData.getItem().getDamage() != 0) {
                                        break;
                                    }

                                    NbtMap states = ((GeyserBedrockBlock) itemData.getItem().getBlockDefinition()).getState().getCompound("states");

                                    boolean valid = true;
                                    for (Map.Entry<String, Object> nbtEntry : requiredBlockStates.entrySet()) {
                                        if (!Objects.equals(states.get(nbtEntry.getKey()), nbtEntry.getValue())) {
                                            // A required block state doesn't match - this one is not valid
                                            valid = false;
                                            break;
                                        }
                                    }
                                    if (valid) {
                                        if (customBlockItemOverride != null && customBlockData != null) {
                                            // Assuming this is a valid custom block override we'll just register it now while we have the creative item
                                            int customProtocolId = nextFreeBedrockId++;
                                            mappingItem = mappingItem.withBedrockData(customProtocolId);
                                            bedrockIdentifier = customBlockData.identifier();
                                            definition = new SimpleItemDefinition(bedrockIdentifier, customProtocolId, ItemVersion.DATA_DRIVEN, true, NbtMap.EMPTY);
                                            registry.put(customProtocolId, definition);
                                            customBlockItemDefinitions.put(customBlockData, definition);
                                            customIdMappings.put(customProtocolId, bedrockIdentifier);

                                            CreativeItemData newData = new CreativeItemData(itemData.getItem().toBuilder()
                                                .definition(definition)
                                                .blockDefinition(bedrockBlock)
                                                .netId(itemData.getNetId())
                                                .count(1)
                                                .build(), itemData.getNetId(), 0);

                                            creativeItems.set(j, newData);
                                        } else {
                                            CreativeItemData creativeItemData = new CreativeItemData(itemData.getItem().toBuilder()
                                                .blockDefinition(bedrockBlock)
                                                .build(), itemData.getNetId(), 0);

                                            creativeItems.set(j, creativeItemData);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                ItemMapping.ItemMappingBuilder mappingBuilder = ItemMapping.builder()
                        .bedrockIdentifier(bedrockIdentifier.intern())
                        .bedrockDefinition(definition)
                        .bedrockData(mappingItem.getBedrockData())
                        .bedrockBlockDefinition(bedrockBlock)
                        .javaItem(javaItem);

                if (mappingItem.getToolType() != null) {
                    mappingBuilder = mappingBuilder.toolType(mappingItem.getToolType().intern());
                }

                if (javaOnlyItems.contains(javaItem)) {
                    // These items don't exist on Bedrock, so set up a variable that indicates they should have custom names
                    mappingBuilder = mappingBuilder.translationString((javaItem instanceof BlockItem ? "block." : "item.") + entry.getKey().replace(":", "."));
                    GeyserImpl.getInstance().getLogger().debug("Adding " + entry.getKey() + " as an item that needs to be translated.");
                }

                // Add the custom item properties, if applicable
                List<Pair<CustomItemOptions, ItemDefinition>> customItemOptions;
                Collection<CustomItemData> customItemsToLoad = customItems.get(javaItem.javaIdentifier());
                if (customItemsAllowed && !customItemsToLoad.isEmpty()) {
                    customItemOptions = new ObjectArrayList<>(customItemsToLoad.size());

                    for (CustomItemData customItem : customItemsToLoad) {
                        int customProtocolId = nextFreeBedrockId++;

                        String customItemName = customItem instanceof NonVanillaCustomItemData nonVanillaItem ? nonVanillaItem.identifier() : Constants.GEYSER_CUSTOM_NAMESPACE + ":" + customItem.name();
                        if (!registeredItemNames.add(customItemName)) {
                            if (firstMappingsPass) {
                                GeyserImpl.getInstance().getLogger().error("Custom item name '" + customItemName + "' already exists and was registered again! Skipping...");
                            }
                            continue;
                        }

                        GeyserCustomMappingData customMapping = CustomItemRegistryPopulator.registerCustomItem(
                                customItemName, javaItem, mappingItem, customItem, customProtocolId, palette.protocolVersion
                        );

                        if (customItem.creativeCategory().isPresent()) {
                            CreativeItemData creativeItemData = new CreativeItemData(ItemData.builder()
                                    .netId(creativeNetId.incrementAndGet())
                                    .definition(customMapping.itemDefinition())
                                    .blockDefinition(null)
                                    .count(1)
                                    .build(), creativeNetId.get(), customItem.creativeCategory().getAsInt());
                            creativeItems.add(creativeItemData);
                        }
                        customItemOptions.add(Pair.of(customItem.customItemOptions(), customMapping.itemDefinition()));
                        registry.put(customMapping.integerId(), customMapping.itemDefinition());

                        customIdMappings.put(customMapping.integerId(), customMapping.stringId());
                    }

                    // Important for later to find the best match and accurately replicate Java behavior
                    Collections.reverse(customItemOptions);
                } else {
                    customItemOptions = Collections.emptyList();
                }
                mappingBuilder.customItemOptions(customItemOptions);

                ItemMapping mapping = mappingBuilder.build();

                if (javaItem.javaIdentifier().contains("bucket") && !javaItem.javaIdentifier().contains("milk")) {
                    buckets.add(definition);
                }

                mappings.add(mapping);
                javaItemToMapping.put(javaItem, mapping);
            }

            // Add the light block level since it doesn't exist on java but we need it for item conversion
            Int2ObjectMap<ItemMapping> lightBlocks = new Int2ObjectOpenHashMap<>();

            for (int i = 0; i <= Properties.LEVEL.high(); i++) {
                ItemDefinition lightBlock = definitions.get("minecraft:light_block_" + i);
                if (lightBlock == null) {
                    break;
                }

                ItemMapping lightBlockEntry = ItemMapping.builder()
                    .javaItem(Items.LIGHT)
                    .bedrockIdentifier("minecraft:light_block_" + i)
                    .bedrockDefinition(lightBlock)
                    .bedrockData(0)
                    .bedrockBlockDefinition(null)
                    .customItemOptions(Collections.emptyList())
                    .build();
                lightBlocks.put(lightBlock.getRuntimeId(), lightBlockEntry);
            }

            ItemDefinition lodestoneCompass = definitions.get("minecraft:lodestone_compass");
            if (lodestoneCompass == null) {
                throw new RuntimeException("Lodestone compass not found in item palette!");
            }

            // Add the lodestone compass since it doesn't exist on java but we need it for item conversion
            ItemMapping lodestoneEntry = ItemMapping.builder()
                    .javaItem(Items.COMPASS)
                    .bedrockIdentifier("minecraft:lodestone_compass")
                    .bedrockDefinition(lodestoneCompass)
                    .bedrockData(0)
                    .bedrockBlockDefinition(null)
                    .customItemOptions(Collections.emptyList())
                    .build();

            if (customItemsAllowed) {
                // Add furnace minecart
                int furnaceMinecartId = nextFreeBedrockId++;
                ItemDefinition definition = new SimpleItemDefinition("geysermc:furnace_minecart", furnaceMinecartId, ItemVersion.DATA_DRIVEN, true, registerFurnaceMinecart(furnaceMinecartId));
                definitions.put("geysermc:furnace_minecart", definition);
                registry.put(definition.getRuntimeId(), definition);

                mappings.set(Items.FURNACE_MINECART.javaId(), ItemMapping.builder()
                        .javaItem(Items.FURNACE_MINECART)
                        .bedrockIdentifier("geysermc:furnace_minecart")
                        .bedrockDefinition(definition)
                        .bedrockData(0)
                        .bedrockBlockDefinition(null)
                        .customItemOptions(Collections.emptyList()) // TODO check for custom items with furnace minecart
                        .build());

                creativeItems.add(new CreativeItemData(ItemData.builder()
                    .usingNetId(true)
                    .netId(creativeNetId.incrementAndGet())
                    .definition(definition)
                    .count(1)
                    .build(), creativeNetId.get(), 99)); // todo do not hardcode!

                // Register any completely custom items given to us
                IntSet registeredJavaIds = new IntOpenHashSet(); // Used to check for duplicate item java ids
                for (NonVanillaCustomItemData customItem : nonVanillaCustomItems) {
                    if (!registeredJavaIds.add(customItem.javaId())) {
                        if (firstMappingsPass) {
                            GeyserImpl.getInstance().getLogger().error("Custom item java id " + customItem.javaId() + " already exists and was registered again! Skipping...");
                        }
                        continue;
                    }

                    int customItemId = nextFreeBedrockId++;
                    NonVanillaItemRegistration registration = CustomItemRegistryPopulator.registerCustomItem(customItem, customItemId, palette.protocolVersion);

                    ItemMapping mapping = registration.mapping();
                    Item javaItem = registration.javaItem();
                    while (javaItem.javaId() >= mappings.size()) {
                        // Fill with empty to get to the correct size
                        mappings.add(ItemMapping.AIR);
                    }
                    mappings.set(javaItem.javaId(), mapping);
                    registry.put(customItemId, mapping.getBedrockDefinition());

                    if (customItem.creativeCategory().isPresent()) {
                        CreativeItemData creativeItemData = new CreativeItemData(ItemData.builder()
                            .definition(registration.mapping().getBedrockDefinition())
                            .netId(creativeNetId.incrementAndGet())
                            .count(1)
                            .build(), creativeNetId.get(), customItem.creativeCategory().getAsInt());

                        creativeItems.add(creativeItemData);
                    }
                }
            }

            // Register the item forms of custom blocks
            if (BlockRegistries.CUSTOM_BLOCKS.get().length != 0) {
                for (CustomBlockData customBlock : BlockRegistries.CUSTOM_BLOCKS.get()) {
                    // We might've registered it already with the vanilla blocks so check first
                    if (customBlockItemDefinitions.containsKey(customBlock)) {
                        continue;
                    }

                    // Non-vanilla custom blocks will be handled in the item
                    // registry, so we don't need to do anything here.
                    if (customBlock instanceof NonVanillaCustomBlockData) {
                        continue;
                    }

                    int customProtocolId = nextFreeBedrockId++;
                    String identifier = customBlock.identifier();

                    final ItemDefinition definition = new SimpleItemDefinition(identifier, customProtocolId, ItemVersion.NONE, false, null);
                    registry.put(customProtocolId, definition);
                    customBlockItemDefinitions.put(customBlock, definition);
                    customIdMappings.put(customProtocolId, identifier);

                    GeyserBedrockBlock bedrockBlock = blockMappings.getCustomBlockStateDefinitions().getOrDefault(customBlock.defaultBlockState(), null);

                    if (bedrockBlock != null && customBlock.includedInCreativeInventory()) {
                        CreativeItemData creativeItemData = new CreativeItemData(ItemData.builder()
                            .definition(definition)
                            .blockDefinition(bedrockBlock)
                            .netId(creativeNetId.incrementAndGet())
                            .count(1)
                            .build(), creativeNetId.get(), customBlock.creativeCategory().id());
                        creativeItems.add(creativeItemData);
                    }
                }
            }

            ItemMappings itemMappings = ItemMappings.builder()
                    .items(mappings.toArray(new ItemMapping[0]))
                    .creativeItems(creativeItems)
                    .creativeItemGroups(creativeItemGroups)
                    .itemDefinitions(registry)
                    .storedItems(new StoredItemMappings(javaItemToMapping))
                    .javaOnlyItems(javaOnlyItems)
                    .buckets(buckets)
                    .lightBlocks(lightBlocks)
                    .lodestoneCompass(lodestoneEntry)
                    .customIdMappings(customIdMappings)
                    .customBlockItemDefinitions(customBlockItemDefinitions)
                    .build();

            Registries.ITEMS.register(palette.protocolVersion(), itemMappings);

            firstMappingsPass = false;
        }
    }

    private static NbtMap registerFurnaceMinecart(int nextFreeBedrockId) {
        NbtMapBuilder builder = NbtMap.builder();
        builder.putString("name", "geysermc:furnace_minecart")
                .putInt("id", nextFreeBedrockId);

        NbtMapBuilder itemProperties = NbtMap.builder();

        NbtMapBuilder componentBuilder = NbtMap.builder();
        // Conveniently, as of 1.16.200, the furnace minecart has a texture AND translation string already.
        // Not so conveniently, the way to set an icon changed in 1.20.60
        NbtMap iconMap = NbtMap.builder()
            .putCompound("textures", NbtMap.builder()
                    .putString("default", "minecart_furnace")
                    .build())
            .build();
        itemProperties.putCompound("minecraft:icon", iconMap);
        componentBuilder.putCompound("minecraft:display_name", NbtMap.builder().putString("value", "item.minecartFurnace.name").build());

        // Indicate that the arm animation should play on rails
        List<NbtMap> useOnTag = Collections.singletonList(NbtMap.builder().putString("tags", "q.any_tag('rail')").build());
        componentBuilder.putCompound("minecraft:entity_placer", NbtMap.builder()
                .putList("dispense_on", NbtType.COMPOUND, useOnTag)
                .putString("entity", "minecraft:minecart")
                .putList("use_on", NbtType.COMPOUND, useOnTag)
                .build());

        // We always want to allow offhand usage when we can - matches Java Edition
        itemProperties.putBoolean("allow_off_hand", true);
        itemProperties.putBoolean("hand_equipped", false);
        itemProperties.putInt("max_stack_size", 1);
        itemProperties.putString("creative_group", "itemGroup.name.minecart");
        itemProperties.putInt("creative_category", 4); // 4 - "Items"

        componentBuilder.putCompound("item_properties", itemProperties.build());
        builder.putCompound("components", componentBuilder.build());
        return builder.build();
    }
}
