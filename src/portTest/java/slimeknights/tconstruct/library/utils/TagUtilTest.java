package slimeknights.tconstruct.library.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagUtilTest {
  @Test
  void readsMinecraftPositionsAndLegacyCoordinatesRelativeToController() {
    BlockPos offset = new BlockPos(100, 64, -200);
    BlockPos relative = new BlockPos(-2, 3, 4);
    CompoundTag data = new CompoundTag();
    data.put("pos", NbtUtils.writeBlockPos(relative));
    assertEquals(relative.offset(offset), TagUtil.readOptionalPos(data, "pos", offset));
    CompoundTag legacy = new CompoundTag();
    legacy.putInt("X", -2);
    legacy.putInt("Y", 3);
    legacy.putInt("Z", 4);
    data.put("pos", legacy);
    assertEquals(relative.offset(offset), TagUtil.readOptionalPos(data, "pos", offset));
    assertNull(TagUtil.readOptionalPos(data, "missing", offset));
    data.putIntArray("pos", new int[] {1, 2});
    assertNull(TagUtil.readOptionalPos(data, "pos", offset));
  }
}
