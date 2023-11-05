package binaris.convertor_block;

import binaris.convertor_block.block.ConvBlock;
import binaris.convertor_block.block.ConvertorBlockEntity;
import binaris.convertor_block.custom_recipe.Probability_Recipe;
import binaris.convertor_block.screen.ConvScreen;
import binaris.convertor_block.screen.ConvScreenHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer.BOX;

public class Convertor_BlockModInitializer implements ModInitializer {
	public static final String MOD_ID = "convertor_block";
	@Deprecated
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Block CONVERTOR_BLOCK = new ConvBlock(FabricBlockSettings.create().strength(4.0F));
	public static ScreenHandlerType<ConvScreenHandler> CONV_SCREEN_HANDLER;
	public static final BlockEntityType<ConvertorBlockEntity> CONVERTOR_BLOCK_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			new Identifier(MOD_ID, "convertor_block_entity"),
			FabricBlockEntityTypeBuilder.create(ConvertorBlockEntity::new, CONVERTOR_BLOCK).build()
	);

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "convertor"), CONVERTOR_BLOCK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "convertor"), new BlockItem(CONVERTOR_BLOCK, new FabricItemSettings()));
		Registry.register(Registries.BLOCK_ENTITY_TYPE, BOX, FabricBlockEntityTypeBuilder.create(ConvertorBlockEntity::new, CONVERTOR_BLOCK).build(null));
		CONV_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "convertor"),
						new ExtendedScreenHandlerType<>((int syncId, PlayerInventory playerInventory, PacketByteBuf inventory) -> new ConvScreenHandler(syncId, playerInventory, (PacketByteBuf) inventory)));
		HandledScreens.register(CONV_SCREEN_HANDLER, ConvScreen::new);
		Probability_Recipe.loadRecipes();
	}
}