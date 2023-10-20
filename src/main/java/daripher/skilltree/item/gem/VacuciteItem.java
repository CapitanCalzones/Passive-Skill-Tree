package daripher.skilltree.item.gem;

import daripher.skilltree.init.PSTCreativeTabs;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

public class VacuciteItem extends GemItem {
  public VacuciteItem() {
    super(new Properties().tab(PSTCreativeTabs.SKILLTREE));
  }

  @Override
  public void insertInto(
      Player player, ItemStack itemStack, ItemStack gemStack, int socket, double gemPower) {
    GemHelper.removeGems(itemStack);
  }

  @Override
  public boolean canInsertInto(Player player, ItemStack stack, ItemStack gemStack, int socket) {
    return GemHelper.hasGem(stack, 0);
  }

  @Override
  public Optional<Pair<Attribute, AttributeModifier>> getGemBonus(
      Player player, ItemStack stack, ItemStack gemStack) {
    return Optional.empty();
  }

  @Override
  protected void appendBonusesTooltip(ItemStack stack, List<Component> components) {
    MutableComponent gemClass = formatGemClass("anything").withStyle(ChatFormatting.GRAY);
    MutableComponent bonus =
        Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.BLUE);
    components.add(gemClass.append(bonus));
  }
}
