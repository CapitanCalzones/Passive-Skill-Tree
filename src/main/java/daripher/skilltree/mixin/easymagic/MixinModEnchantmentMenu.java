package daripher.skilltree.mixin.easymagic;

import daripher.skilltree.api.EnchantmentMenuExtention;
import daripher.skilltree.container.ContainerHelper;
import fuzs.easymagic.mixin.accessor.EnchantmentMenuAccessor;
import fuzs.easymagic.world.inventory.ModEnchantmentMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModEnchantmentMenu.class)
public class MixinModEnchantmentMenu {
  private @Shadow @Final DataSlot enchantmentSeed;

  @Redirect(
      method = "updateLevels",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentCost(Lnet/minecraft/util/RandomSource;IILnet/minecraft/world/item/ItemStack;)I"))
  private int reduceLevelRequirements(
      RandomSource random, int slot, int power, ItemStack itemStack) {
    int levelRequirement = EnchantmentHelper.getEnchantmentCost(random, slot, power, itemStack);
    int[] costsBeforeReduction = ((EnchantmentMenuExtention) this).getCostsBeforeReduction();
    costsBeforeReduction[slot] = levelRequirement;
    Optional<Player> player = ContainerHelper.getViewingPlayer((ModEnchantmentMenu) (Object) this);
    if (!player.isPresent()) return levelRequirement;
    int decreasedRequirement =
        daripher.skilltree.enchantment.EnchantmentHelper.adjustLevelRequirement(
            levelRequirement, player.get());
    return decreasedRequirement;
  }

  @Inject(
      method = "createEnchantmentInstance",
      at = @At("RETURN"),
      cancellable = true,
      remap = false)
  private void amplifyEnchantments(
      ItemStack itemStack,
      int slot,
      CallbackInfoReturnable<List<EnchantmentInstance>> callbackInfo) {
    int[] costsBeforeReduction = ((EnchantmentMenuExtention) this).getCostsBeforeReduction();
    List<EnchantmentInstance> enchantments =
        ((EnchantmentMenuAccessor) this)
            .callGetEnchantmentList(itemStack, slot, costsBeforeReduction[slot]);
    RandomSource random = RandomSource.create(enchantmentSeed.get());
    Optional<Player> player = ContainerHelper.getViewingPlayer((ModEnchantmentMenu) (Object) this);
    if (!player.isPresent()) return;
    daripher.skilltree.enchantment.EnchantmentHelper.amplifyEnchantments(
        enchantments, random, player.get());
    callbackInfo.setReturnValue(enchantments);
  }
}
