package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemItemFrame;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.Faceable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static cn.nukkit.blockproperty.CommonBlockProperties.FACING_DIRECTION;
import static cn.nukkit.math.BlockFace.AxisDirection.POSITIVE;

/**
 * @author Pub4Game
 * @since 03.07.2016
 */
@PowerNukkitDifference(since = "1.4.0.0-PN", info = "Implements BlockEntityHolder only in PowerNukkit")
@PowerNukkitDifference(since = "1.4.0.0-PN", info = "Implements Faceable only in PowerNukkit")
public class BlockItemFrame extends BlockTransparentMeta implements BlockEntityHolder<BlockEntityItemFrame>, Faceable {

    @Deprecated
    private final static int[] FACING = new int[]{4, 5, 3, 2, 1, 0}; // TODO when 1.13 support arrives, add UP/DOWN facings

    @Deprecated
    private final static int FACING_BITMASK = 0b0111;
    @Deprecated
    private final static int MAP_BIT = 0b1000;
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BlockProperties PROPERTIES = new BlockProperties(FACING_DIRECTION);
    
    public BlockItemFrame() {
        this(0);
    }

    public BlockItemFrame(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return ITEM_FRAME_BLOCK;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public String getBlockEntityType() {
        return BlockEntity.ITEM_FRAME;
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Nonnull
    @Override
    public Class<? extends BlockEntityItemFrame> getBlockEntityClass() {
        return BlockEntityItemFrame.class;
    }

    @Override
    public String getName() {
        return "Item Frame";
    }

    @PowerNukkitDifference(info = "Allow to stay in walls", since = "1.3.0.0-PN")
    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            BlockFace thisFace = getFacing();
            BlockFace touchingFace = thisFace.getOpposite();
            Block side = this.getSide(touchingFace);
            if (!isSupportValid(side, thisFace)) {
                this.level.useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @PowerNukkitOnly
    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public boolean onActivate(@Nonnull Item item, Player player) {
        BlockEntityItemFrame itemFrame = getOrCreateBlockEntity();
        if (itemFrame.getItem().isNull()) {
        	Item itemOnFrame = item.clone();
        	if (player != null && player.isSurvival()) {
        		itemOnFrame.setCount(itemOnFrame.getCount() - 1);
                player.getInventory().setItemInHand(itemOnFrame);
        	}
            itemOnFrame.setCount(1);
            itemFrame.setItem(itemOnFrame);
            this.getLevel().addLevelEvent(this, LevelEventPacket.EVENT_SOUND_ITEM_FRAME_ITEM_ADDED);
        } else {
            itemFrame.setItemRotation((itemFrame.getItemRotation() + 1) % 8);
            this.getLevel().addLevelEvent(this, LevelEventPacket.EVENT_SOUND_ITEM_FRAME_ITEM_ROTATED);
        }
        return true;
    }

    @PowerNukkitDifference(info = "Allow to place on walls", since = "1.3.0.0-PN")
    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (isSupportValid(target, face) || (block.isSolid() && !block.canBeReplaced()))) {
            return false;
        }
        
        this.setBlockFace(face.getOpposite());
        CompoundTag nbt = new CompoundTag()
                .putByte("ItemRotation", 0)
                .putFloat("ItemDropChance", 1.0f);
        if (item.hasCustomBlockData()) {
            for (Tag aTag : item.getCustomBlockData().getAllTags()) {
                nbt.put(aTag.getName(), aTag);
            }
        }
        BlockEntityItemFrame frame = BlockEntityHolder.setBlockAndCreateEntity(this, true, true, nbt);
        if (frame == null) {
            return false;
        }
        
        this.getLevel().addSound(this, Sound.BLOCK_ITEMFRAME_PLACE);
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, layer, Block.get(BlockID.AIR), true, true);
        this.getLevel().addLevelEvent(this, LevelEventPacket.EVENT_SOUND_ITEM_FRAME_REMOVED);
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        BlockEntityItemFrame itemFrame = getBlockEntity();
        int chance = new Random().nextInt(100) + 1;
        if (itemFrame != null && chance <= (itemFrame.getItemDropChance() * 100)) {
            return new Item[]{
                    toItem(), itemFrame.getItem().clone()
            };
        } else {
            return new Item[]{
                    toItem()
            };
        }
    }

    @Override
    public Item toItem() {
        return new ItemItemFrame();
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntityItemFrame blockEntity = getBlockEntity();

        if (blockEntity != null) {
            return blockEntity.getAnalogOutput();
        }

        return super.getComparatorInputOverride();
    }

    public BlockFace getFacing() {
        return getPropertyValue(FACING_DIRECTION);
    }

    @Override
    public double getHardness() {
        return 0.25;
    }

    @Override
    public boolean breaksWhenMoved() {
        return true;
    }

    @Override
    public boolean sticksToPiston() {
        return false;
    }

    @PowerNukkitOnly("Will calculate the correct AABB")
    @Since("1.3.0.0-PN")
    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        double[][] aabb = {
                {2.0/16, 14.0/16},
                {2.0/16, 14.0/16},
                {2.0/16, 14.0/16}
        };
        
        BlockFace facing = getFacing();
        if (facing.getAxisDirection() == POSITIVE) {
            int axis = facing.getAxis().ordinal();
            aabb[axis][0] = 0;
            aabb[axis][1] = 1.0/16;
        }
        
        return new SimpleAxisAlignedBB(
                aabb[0][0] + x, aabb[1][0] + y, aabb[2][0] + z, 
                aabb[0][1] + x, aabb[1][1] + y, aabb[2][1] + z
        );
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static boolean isSupportValid(Block support, BlockFace face) {
        if (support.isSolid()) {
            return true;
        }
        
        if (support.getId() == BlockID.CACTUS || support instanceof BlockWallBase || support instanceof BlockFence || support instanceof BlockPressurePlateBase || support instanceof BlockSlab || support instanceof BlockChest || support instanceof BlockDoor) {
            return true;
        }
        
        return false;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Override
    public BlockFace getBlockFace() {
        return getFacing();
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(FACING_DIRECTION, face);
    }
}
