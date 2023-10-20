package daripher.skilltree.mixin.minecraft;

import daripher.skilltree.api.PlayerContainer;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class MixinBlockEntity implements PlayerContainer {
  private Optional<Player> player = Optional.empty();

  @Override
  public Optional<Player> getViewingPlayer() {
    return player;
  }

  @Override
  public void setViewingPlayer(Optional<Player> player) {
    this.player = player;
  }
}
