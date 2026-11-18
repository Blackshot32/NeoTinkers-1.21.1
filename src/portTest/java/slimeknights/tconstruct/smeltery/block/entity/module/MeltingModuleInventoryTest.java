package slimeknights.tconstruct.smeltery.block.entity.module;

import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MeltingModuleInventoryTest {
  @Test
  void largeInventorySizesSurviveSaveAndReload() {
    for (int size : new int[] {127, 128, 255, 256, 294, 300, 512, 10920}) {
      MeltingModuleInventory inventory = new MeltingModuleInventory(null, null, null);
      CompoundTag data = new CompoundTag();
      data.putInt("size", size);
      inventory.readFromTag(null, data);
      MeltingModuleInventory restored = new MeltingModuleInventory(null, null, null);
      restored.readFromTag(null, inventory.writeToTag(null));
      assertEquals(size, restored.getSlots(), "size=" + size);
    }
  }

  @Test
  void oldUnsignedByteSizesStillLoad() {
    MeltingModuleInventory inventory = new MeltingModuleInventory(null, null, null);
    CompoundTag data = new CompoundTag();
    data.putByte("size", (byte) 200);
    inventory.readFromTag(null, data);
    assertEquals(200, inventory.getSlots());
  }
}
