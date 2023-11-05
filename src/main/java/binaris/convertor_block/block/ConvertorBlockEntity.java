package binaris.convertor_block.block;

import binaris.convertor_block.screen.ConvScreenHandler;
import binaris.convertor_block.Convertor_BlockModInitializer;
import binaris.convertor_block.screen.ImplementedInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ConvertorBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;
    private static int progress = 0;
    private static int maxProgress = 72;

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    public ConvertorBlockEntity(BlockPos pos, BlockState state) {
        super(Convertor_BlockModInitializer.CONVERTOR_BLOCK_ENTITY, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public static void tick(World world, BlockPos pos, BlockState state, ConvertorBlockEntity co) {
        if(world.isClient()) {
            return;
        }

        if(isOutputSlotEmptyOrReceivable(co)) {
            if(hasRecipe(co)) {
                increaseCraftProgress();
                markDirty(world, pos, state);

                if(hasCraftingFinished()) {
                    craftItem(co);
                    resetProgress();
                }
            } else {
                resetProgress();
            }
        } else {
            resetProgress();
            markDirty(world, pos, state);
        }

    }


    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("conv_progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        nbt.getInt("conv_progress");
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ConvScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public void markDirty() {
        world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        super.markDirty();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    private static void resetProgress() {
        progress = 0;
    }

    private static void craftItem(ConvertorBlockEntity co) {
        co.removeStack(INPUT_SLOT, 1);
        ItemStack result = new ItemStack(Items.LEATHER);

        co.setStack(OUTPUT_SLOT, new ItemStack(result.getItem(), co.getStack(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private static boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private static void increaseCraftProgress() {
        progress++;
    }

    private static boolean hasRecipe(ConvertorBlockEntity co) {
        ItemStack result = new ItemStack(Items.LEATHER);
        boolean hasInput = co.getStack(INPUT_SLOT).getItem() == Items.ROTTEN_FLESH;

        return hasInput && canInsertAmountIntoOutputSlot(result, co) && canInsertItemIntoOutputSlot(result.getItem(), co);
    }

    private static boolean canInsertItemIntoOutputSlot(Item item, ConvertorBlockEntity co) {
        return co.getStack(OUTPUT_SLOT).getItem() == item || co.getStack(OUTPUT_SLOT).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(ItemStack result, ConvertorBlockEntity co) {
        return co.getStack(OUTPUT_SLOT).getCount() + result.getCount() <= co.getStack(OUTPUT_SLOT).getMaxCount();
    }

    private static boolean isOutputSlotEmptyOrReceivable(ConvertorBlockEntity co) {
        return co.getStack(OUTPUT_SLOT).isEmpty() || co.getStack(OUTPUT_SLOT).getCount() < co.getStack(OUTPUT_SLOT).getMaxCount();
    }
}
