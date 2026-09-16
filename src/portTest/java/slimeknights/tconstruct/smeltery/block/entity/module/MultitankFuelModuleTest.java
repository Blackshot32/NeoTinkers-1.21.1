package slimeknights.tconstruct.smeltery.block.entity.module;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MultitankFuelModuleTest {
  private static final BlockPos FIRST = new BlockPos(1, 64, 0);
  private static final BlockPos SECOND = new BlockPos(2, 64, 0);

  /** Models the result of a capability cache after a provider invalidates. */
  private static class TestModule extends MultitankFuelModule {
    final Map<BlockPos,IFluidHandler> providers = new HashMap<>();
    IFluidHandler consumedFrom;

    TestModule(List<BlockPos> positions) {
      super(null, () -> positions);
    }

    @Override
    protected IFluidHandler getHandlerAt(BlockPos pos) {
      return providers.get(pos);
    }

    @Override
    protected int tryLiquidFuel(IFluidHandler handler, boolean consume) {
      if (consume) consumedFrom = handler;
      return 1000;
    }
  }

  private static IFluidHandler tank(int capacity) {
    IFluidHandler handler = mock(IFluidHandler.class);
    when(handler.getTankCapacity(0)).thenReturn(capacity);
    return handler;
  }

  @Test
  void replacedCapabilityIsUsedWithoutRebuildingStructure() {
    TestModule module = new TestModule(List.of(FIRST));
    module.providers.put(FIRST, tank(1000));
    assertEquals(1000, module.getTankCapacity(0));
    module.providers.put(FIRST, tank(4000));
    assertEquals(4000, module.getTankCapacity(0));
  }

  @Test
  void missingCapabilityRecoversAndDoesNotKeepRemovedTank() {
    TestModule module = new TestModule(List.of(FIRST));
    module.providers.put(FIRST, tank(1000));
    assertEquals(1000, module.getTankCapacity(0));
    module.providers.remove(FIRST);
    assertEquals(0, module.getTankCapacity(0));
    module.providers.put(FIRST, tank(4000));
    assertEquals(4000, module.getTankCapacity(0));
  }

  @Test
  void changingPositionsWithSameTankCountRefreshesConnections() {
    List<BlockPos> positions = new ArrayList<>(List.of(FIRST));
    TestModule module = new TestModule(positions);
    module.providers.put(FIRST, tank(1000));
    module.providers.put(SECOND, tank(4000));
    assertEquals(1000, module.getTankCapacity(0));
    positions.set(0, SECOND);
    assertEquals(4000, module.getTankCapacity(0));
  }

  @Test
  void activeFuelHandlerIsResolvedAgainAfterReplacement() {
    TestModule module = new TestModule(List.of(FIRST));
    IFluidHandler old = tank(1000), replacement = tank(4000);
    module.providers.put(FIRST, old);
    assertEquals(1000, module.findFuel(true));
    assertSame(old, module.consumedFrom);
    module.providers.put(FIRST, replacement);
    assertEquals(1000, module.findFuel(true));
    assertSame(replacement, module.consumedFrom);
  }

  @Test
  void removedActiveTankCannotSupplyFuel() {
    List<BlockPos> positions = new ArrayList<>(List.of(FIRST));
    TestModule module = new TestModule(positions);
    module.providers.put(FIRST, tank(1000));
    assertEquals(1000, module.findFuel(true));
    positions.clear();
    module.consumedFrom = null;
    assertEquals(0, module.findFuel(true));
    assertNull(module.consumedFrom);
  }
}
