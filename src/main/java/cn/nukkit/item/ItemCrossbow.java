package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

/**
 * @author GoodLucky777
 */
public class ItemCrossbow extends ItemTool {

    public ItemCrossbow() {
        this(0, 1);
    }
    
    public ItemCrossbow(Integer meta) {
        this(meta, 1);
    }
    
    public ItemCrossbow(Integer meta, int count) {
        super(CROSSBOW, meta, count, "Crossbow");
    }
    
    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_CROSSBOW;
    }
    
    @Override
    public boolean onUse(Player player, int ticksUsed) {
        if (ticksUsed < getChargeTick()) {
            return true;
        }
        
        return true;
    }
    
    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        return !shoot();
    }
    
    @Override
    public boolean onRelease(Player player, int ticksUsed) {
        return true;
    }
    
    public int getChargeTick() {
        int quickChargeLevel = this.getEnchantmentLevel(Enchantment.ID_TRIDENT_QUICK_CHARGE);
        return 25 - (quickChargeLevel == 0 ? 0 : quickChargeLevel * 5);
    }
    
    public boolean isLoaded() {
        return true;
    }
    
    public boolean shoot() {
        if (!isLoaded()) {
            return false;
        }
        
        return true;
    }
    
    public boolean launchProjectile() {
        return true;
    }
}
