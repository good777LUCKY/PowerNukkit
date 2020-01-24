package cn.nukkit.blockentity;

import cn.nukkit.item.Item;
import cn.nukkit.level.chunk.Chunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityCampfire extends BlockEntitySpawnable {
    private List<Item> itemsInFire;
    private int[] cookingTimes;
    private int[] cookingTotalTimes;

    public BlockEntityCampfire(Chunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        itemsInFire = new ArrayList<>();
        if (namedTag.contains("Items")) {
            ListTag<CompoundTag> items = namedTag.getList("Items", CompoundTag.class);
            for (CompoundTag tag : items.getAll()) {
                Item i = NBTIO.getItemHelper(tag);
                itemsInFire.add(tag.getByte("Slot"), i);
            }
        }

        if (namedTag.contains("CookingTimes")) {
            cookingTimes = namedTag.getIntArray("CookingTimes");
        } else {
            cookingTimes = new int[]{0, 0, 0, 0};
        }

        if (namedTag.contains("CookingTotalTimes")) {
            cookingTotalTimes = namedTag.getIntArray("CookingTotalTimes");
        } else {
            cookingTotalTimes = new int[]{0, 0, 0, 0};
        }
        super.initBlockEntity();
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return null;
    }

    @Override
    public boolean isBlockEntityValid() {
        return false;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        namedTag.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
        namedTag.putIntArray("CookingTimes", this.cookingTimes);
        if (!itemsInFire.isEmpty()) {
            ListTag<CompoundTag> items = new ListTag<>("Items");
            for (Item item : itemsInFire) {
                items.add(NBTIO.putItemHelper(item, itemsInFire.indexOf(item)));
            }
            namedTag.putList(items);
        }
    }
}
