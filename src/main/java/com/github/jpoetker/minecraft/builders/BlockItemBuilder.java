package com.github.jpoetker.minecraft.builders;


import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.*;
import net.minecraftforge.common.ToolType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class BlockItemBuilder<B extends Block> {
    private Item.Properties itemProperties;
    private B block;

    public BlockItemBuilder(B block) {
        this.itemProperties = new Item.Properties();
        this.block = block;
    }

    public BlockItemBuilder food(Food foodIn) {
        itemProperties.food(foodIn);
        return this;
    }

    public BlockItemBuilder maxStackSize(int maxStackSizeIn) {
        itemProperties.maxStackSize(maxStackSizeIn);
        return this;
    }

    public BlockItemBuilder defaultMaxDamage(int maxDamageIn) {
        itemProperties.defaultMaxDamage(maxDamageIn);
        return this;
    }

    public BlockItemBuilder maxDamage(int maxDamageIn) {
        itemProperties.maxDamage(maxDamageIn);
        return this;
    }

    public BlockItemBuilder containerItem(Item containerItemIn) {
        itemProperties.containerItem(containerItemIn);
        return this;
    }

    public BlockItemBuilder group(ItemGroup groupIn) {
        itemProperties.group(groupIn);
        return this;
    }

    public BlockItemBuilder rarity(Rarity rarityIn) {
        itemProperties.rarity(rarityIn);
        return this;
    }

    public BlockItemBuilder noRepair() {
        itemProperties.setNoRepair();
        return this;
    }

    public BlockItemBuilder addToolType(ToolType type, int level) {
        itemProperties.addToolType(type, level);
        return this;
    }

    public BlockItemBuilder toolTypes(Map<ToolType, Integer> toolTypes) {
        for(Map.Entry<ToolType, Integer> tool : toolTypes.entrySet()) {
            addToolType(tool.getKey(), tool.getValue());
        }
        return this;
    }

    public BlockItemBuilder itemStackTileEntityRenderer(Supplier<Callable<ItemStackTileEntityRenderer>> ister) {
        itemProperties.setISTER(ister);
        return this;
    }

    public BlockItem build() {
        return build(BlockItem.class);
    }

    public <T extends BlockItem> T build(Class<T> clzz) {
        try {
            Constructor<T> constructor = findConstructor(clzz, block.getClass());
            T blockItem = constructor.newInstance(block, itemProperties);
            blockItem.setRegistryName(block.getRegistryName());

            return blockItem;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to instantiate BlockItem for: " + clzz.getName(), e);
        }
    }

    private <T extends BlockItem> Constructor<T> findConstructor(Class<T> blockItemClzz, Class<?> blockClass) {
        if (blockClass != null) {
            try {
                return blockItemClzz.getConstructor(blockClass, Item.Properties.class);
            } catch (NoSuchMethodException e) {
                return findConstructor(blockItemClzz, blockClass.getSuperclass());
            }
        } else {
            throw new IllegalStateException("No suitable constructor found for: " + blockItemClzz.getName());
        }
    }
}
