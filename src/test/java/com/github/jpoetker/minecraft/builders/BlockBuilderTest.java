package com.github.jpoetker.minecraft.builders;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockBuilderTest {

    @Test
    public void should_build_block_with_configured_properties() throws Exception {
        Block block = new BlockBuilder<>(Block.class)
                .withRock()
                .hardnessAndResistance(0.5f)
                .slipperiness(0.1f)
                .withBlockName("jpoetker_test_rock")
                .build();

        assertNotNull(block);
        assertSame(Block.class, block.getClass());

        assertEquals("jpoetker_test_rock", block.getRegistryName().getPath());
    }

    @Test
    public void should_build_ore_block() throws Exception {
        OreBlock block = new BlockBuilder<>(OreBlock.class)
                .withRock()
                .withBlockName("jpoetker_ore_block")
                .build();

        assertNotNull(block);
        assertSame(OreBlock.class, block.getClass());
    }
}
