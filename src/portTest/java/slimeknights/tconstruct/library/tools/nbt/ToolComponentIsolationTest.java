package slimeknights.tconstruct.library.tools.nbt;

import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;

import net.neoforged.neoforge.common.util.flag.FeatureFlagLoader;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ToolComponentIsolationTest {
  private static final ResourceLocation COUNTER = ResourceLocation.fromNamespaceAndPath("tconstruct", "isolation_test");

  @BeforeAll
  static void bootstrap() {
    SharedConstants.tryDetectVersion();
    try (MockedStatic<FeatureFlagLoader> flags = Mockito.mockStatic(FeatureFlagLoader.class)) {
      Bootstrap.bootStrap();
    }
    // These are isolated component tests, without loading the complete mod or a world.
    // Register the same component type so the production deferred holder can resolve it.
    if (!BuiltInRegistries.DATA_COMPONENT_TYPE.containsKey(ToolDataComponents.TOOL_DATA.getId())) {
      if (BuiltInRegistries.DATA_COMPONENT_TYPE instanceof net.minecraft.core.MappedRegistry<?> mapped) {
        mapped.unfreeze();
      }
      Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ToolDataComponents.TOOL_DATA.getId(),
        DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).build());
    }
  }

  @Test
  void publishingTagDoesNotRetainMutableCallerData() {
    ItemStack stack = new ItemStack(Items.STICK);
    CompoundTag input = new CompoundTag();
    CompoundTag nested = new CompoundTag();
    nested.putInt("value", 1);
    input.put("nested", nested);
    ToolDataComponents.setTag(stack, input);
    nested.putInt("value", 2);
    assertEquals(1, ToolDataComponents.getOrEmpty(stack).getCompound("nested").getInt("value"));
  }

  @Test
  void toolViewCannotMutatePublishedOrCopiedItemStack() {
    ToolStack tool = ToolStack.from(Items.STICK, ToolDefinition.EMPTY, new CompoundTag());
    tool.getPersistentData().putInt(COUNTER, 1);
    ItemStack stack = tool.createStack();
    ItemStack copy = stack.copy();
    tool.getPersistentData().putInt(COUNTER, 2);
    assertEquals(1, ToolStack.from(stack).getPersistentData().getInt(COUNTER));
    assertEquals(1, ToolStack.from(copy).getPersistentData().getInt(COUNTER));
  }

  @Test
  void successiveWritesKeepInventorySnapshotsIndependent() {
    ItemStack stack = new ItemStack(Items.STICK);
    ToolDataComponents.setTag(stack, new CompoundTag());
    ToolStack tool = ToolStack.from(stack);
    tool.getPersistentData().putInt(COUNTER, 1);
    ItemStack snapshot = stack.copy();
    tool.getPersistentData().putInt(COUNTER, 2);
    assertEquals(1, ToolStack.from(snapshot).getPersistentData().getInt(COUNTER));
    assertEquals(2, ToolStack.from(stack).getPersistentData().getInt(COUNTER));
    assertFalse(ItemStack.isSameItemSameComponents(stack, snapshot));
  }

  @Test
  void refreshedViewSeesAnotherViewsWritesWithoutLosingOwnNextWrite() {
    ItemStack stack = new ItemStack(Items.STICK);
    ToolDataComponents.setTag(stack, new CompoundTag());
    ToolStack first = ToolStack.from(stack);
    first.getPersistentData().putInt(COUNTER, 1);
    ToolStack second = ToolStack.from(stack);
    second.getPersistentData().putInt(COUNTER, 2);
    first.refreshFromStack();
    assertEquals(2, first.getPersistentData().getInt(COUNTER));
    first.getPersistentData().putInt(COUNTER, 3);
    assertEquals(3, ToolStack.from(stack).getPersistentData().getInt(COUNTER));
  }
}
