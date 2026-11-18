package slimeknights.tconstruct.smeltery.block.entity.module;

import org.junit.jupiter.api.Test;
import slimeknights.mantle.block.entity.MantleBlockEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FuelModuleTest {
  @Test
  void idleFuelDoesNotDirtyChunkButConsumptionStillDoes() {
    MantleBlockEntity parent = mock(MantleBlockEntity.class);
    FuelModule module = new FuelModule(parent) {
      @Override public int findFuel(boolean consume) { return 0; }
    };
    module.decreaseFuel(1);
    verify(parent, never()).setChangedFast();
    module.fuel = 5;
    module.decreaseFuel(2);
    assertEquals(3, module.getFuel());
    verify(parent).setChangedFast();
    module.decreaseFuel(10);
    assertEquals(0, module.getFuel());
    verify(parent, times(2)).setChangedFast();
    module.decreaseFuel(1);
    verify(parent, times(2)).setChangedFast();
  }
}
