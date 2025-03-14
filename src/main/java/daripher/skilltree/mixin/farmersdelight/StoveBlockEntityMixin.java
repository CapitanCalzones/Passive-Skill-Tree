package daripher.skilltree.mixin.farmersdelight;

import daripher.skilltree.container.ContainerHelper;
import daripher.skilltree.util.FoodHelper;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;

@Mixin(value = StoveBlockEntity.class)
public class StoveBlockEntityMixin {
  @SuppressWarnings("DefaultAnnotationParam")
  @Redirect(
      method = "cookAndOutputItems",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/item/crafting/CampfireCookingRecipe;getResultItem()Lnet/minecraft/world/item/ItemStack;",
              remap = true),
      remap = false)
  private ItemStack setCookedFoodBonuses(CampfireCookingRecipe recipe) {
    ItemStack result = recipe.getResultItem();
    @SuppressWarnings("DataFlowIssue")
    StoveBlockEntity stove = (StoveBlockEntity) (Object) this;
    Optional<Player> player = ContainerHelper.getViewingPlayer(stove);
    if (player.isEmpty()) return result;
    FoodHelper.setCraftedFoodBonuses(result, player.get());
    return result;
  }
}
