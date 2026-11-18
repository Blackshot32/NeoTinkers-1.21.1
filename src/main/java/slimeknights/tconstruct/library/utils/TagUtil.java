package slimeknights.tconstruct.library.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import javax.annotation.Nullable;

/** Helpers related to Tag */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagUtil {
  /* Helper functions */

  /**
   * Reads a block position from Tag
   * @param parent  Parent tag
   * @param key     Position key
   * @param offset  Amount to offset position by
   * @return  Block position, or null if invalid or missing
   */
  @Nullable
  public static BlockPos readOptionalPos(CompoundTag parent, String key, BlockPos offset) {
    if (parent.contains(key, Tag.TAG_INT_ARRAY)) {
      // In 1.21+, NbtUtils.writeBlockPos produces an IntArrayTag
      return NbtUtils.readBlockPos(parent, key).map(pos -> pos.offset(offset)).orElse(null);
    } else if (parent.contains(key, Tag.TAG_COMPOUND)) {
      // Backwards compatibility with 1.20 and older compound format
      CompoundTag tag = parent.getCompound(key);
      if (tag.contains("X", Tag.TAG_ANY_NUMERIC) && tag.contains("Y", Tag.TAG_ANY_NUMERIC) && tag.contains("Z", Tag.TAG_ANY_NUMERIC)) {
        return new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z")).offset(offset);
      }
    }
    return null;
  }

  /**
   * Checks if the given tag is a numeric type
   * @param tag  Tag to check
   * @return  True if the type matches
   */
  public static boolean isNumeric(Tag tag) {
    byte type = tag.getId();
    return type == Tag.TAG_BYTE || type == Tag.TAG_SHORT || type == Tag.TAG_INT || type == Tag.TAG_LONG || type == Tag.TAG_FLOAT || type == Tag.TAG_DOUBLE;
  }
}
