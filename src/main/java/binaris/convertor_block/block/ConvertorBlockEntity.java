package binaris.convertor_block.block;

import binaris.convertor_block.custom_recipe.Probability_Recipe;
import binaris.convertor_block.screen.ConvScreenHandler;
import binaris.convertor_block.Convertor_BlockModInitializer;
import binaris.convertor_block.screen.ImplementedInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ConvertorBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory, SidedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 156;

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

    public void tick(World world, BlockPos pos, BlockState state, ConvertorBlockEntity co) {
        if(world.isClient()) {
            return;
        }

        if(co.getStack(OUTPUT_SLOT).isEmpty() || co.getStack(OUTPUT_SLOT).getCount() < co.getStack(OUTPUT_SLOT).getMaxCount()) {
            if(hasRecipe(co)) {
                this.progress++;
                markDirty(world, pos, state);

                if(this.progress >= maxProgress) {
                    craftItem(co);
                    this.progress = 0;
                    markDirty(world, pos, state);
                }
            } else {
                this.progress = 0;
                markDirty(world, pos, state);

            }
        } else {
            this.progress = 0;
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

    @Override
    public int[] getAvailableSlots(Direction side) {
        // Hopper Utility (IDK why this is util, but I put this for the tutorial)
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        // Hopper Utility
        return direction == Direction.UP && slot == 0;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        // Hopper Utility
        return direction == Direction.DOWN && slot == 1;
    }




    private static void craftItem(ConvertorBlockEntity co) {
        List<ItemStack> list_result = Probability_Recipe.recipe_list.get(co.getStack(INPUT_SLOT).getItem());
        Random random = new Random();
        ItemStack result = list_result.get(random.nextInt(list_result.size()));

        co.removeStack(INPUT_SLOT, 1);
        co.setStack(OUTPUT_SLOT, new ItemStack(result.getItem(), co.getStack(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private static boolean hasRecipe(ConvertorBlockEntity co) {
        List<ItemStack> result = Probability_Recipe.recipe_list.get(co.getStack(INPUT_SLOT).getItem());
        boolean hasInput = Probability_Recipe.recipe_list.containsKey(co.getStack(INPUT_SLOT).getItem());
        if(result == null){return false;}

        for(ItemStack stack : result){
            if(hasInput && canInsertAmountIntoOutputSlot(stack, co) && canInsertItemIntoOutputSlot(stack.getItem(), co)){
                return true;
            }
        }
        return false;
    }

    private static boolean canInsertItemIntoOutputSlot(Item item, ConvertorBlockEntity co) {
        return co.getStack(OUTPUT_SLOT).getItem() == item || co.getStack(OUTPUT_SLOT).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(ItemStack result, ConvertorBlockEntity co) {
        return co.getStack(OUTPUT_SLOT).getCount() + result.getCount() <= co.getStack(OUTPUT_SLOT).getMaxCount();
    }
}
