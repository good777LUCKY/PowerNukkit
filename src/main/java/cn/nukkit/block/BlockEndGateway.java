package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityEndGateway;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

import javax.annotation.Nonnull;

/**
 * @author PikyCZ
 */
public class BlockEndGateway extends BlockSolid implements BlockEntityHolder<BlockEntityEndGateway> {

    public BlockEndGateway() {
    }

    @Override
    public String getName() {
        return "End Gateway";
    }

    @Override
    public int getId() {
        return END_GATEWAY;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public Class<? extends BlockEntityEndGateway> getBlockEntityClass() {
        return BlockEntityEndGateway.class;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public String getBlockEntityType() {
        return BlockEntity.END_PORTAL;
    }
    
    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null;
    }
    
    @Override
    public boolean canPassThrough() {
        return this.getLevel().getDimension() == Level.DIMENSION_THE_END;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public double getResistance() {
        return 3600000;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BLACK_BLOCK_COLOR;
    }
    
    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(BlockID.AIR));
    }
    
    @Override
    public boolean canBePushed() {
        return false;
    }
    
    @Override
    public boolean canBePulled() {
        return false;
    }
}
