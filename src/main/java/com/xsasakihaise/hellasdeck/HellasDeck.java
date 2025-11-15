package com.xsasakihaise.hellasdeck;

import com.mojang.brigadier.CommandDispatcher;
import com.xsasakihaise.hellascontrol.api.CoreCheck;
import com.xsasakihaise.hellasdeck.commands.DeckBroadcastCommand;
import com.xsasakihaise.hellasdeck.commands.DeckDrawCommand;
import com.xsasakihaise.hellasdeck.commands.DeckEndCommand;
import com.xsasakihaise.hellasdeck.commands.DeckHelpCommand;
import com.xsasakihaise.hellasdeck.commands.DeckInputCommand;
import com.xsasakihaise.hellasdeck.commands.DeckListCommand;
import com.xsasakihaise.hellasdeck.commands.DeckOutputCommand;
import com.xsasakihaise.hellasdeck.commands.DeckProtectCommand;
import com.xsasakihaise.hellasdeck.commands.DeckStartCommand;
import com.xsasakihaise.hellasdeck.commands.DeckTokensCommand;
import com.xsasakihaise.hellasdeck.commands.DeckTokensGiveCommand;
import com.xsasakihaise.hellasdeck.commands.DeckTokensTakeCommand;
import net.minecraft.command.CommandSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Main entry point for the Hellas Deck sidemod.
 *
 * <p>The mod exposes the Deck of Many Mons feature set to the Hellas Pixelmon
 * environment. At load it validates that the shared HellasCore is present and
 * registers the Brigadier command tree that drives the entire experience.</p>
 */
@Mod("hellasdeck")
public class HellasDeck {
    /**
     * Lazily populated metadata sourced from {@code config/hellasdeck.json}.
     * The data is currently informational only but helps staff confirm the
     * installed feature set.
     */
    public static HellasDeckInfoConfig infoConfig;

    /**
     * Creates the mod instance, ensuring the Hellas core entitlement is valid
     * and wiring the mod into the global Forge event bus so the command tree is
     * registered during server startup.
     */
    public HellasDeck() {
        CoreCheck.verifyCoreLoaded();
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            CoreCheck.verifyEntitled("hellasdeck");
        }

        infoConfig = new HellasDeckInfoConfig();
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(this::onRegisterCommands);
    }

    /**
     * Registers all Deck of Many Mons commands when Brigadier is initialised.
     *
     * @param event Forge registration hook supplying the dispatcher
     */
    private void onRegisterCommands(RegisterCommandsEvent event) {
        if (!ModList.get().isLoaded("hellascontrol")) {
            return;
        }
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        DeckStartCommand.register(dispatcher);
        DeckEndCommand.register(dispatcher);
        DeckDrawCommand.register(dispatcher);
        DeckProtectCommand.register(dispatcher);
        DeckInputCommand.register(dispatcher);
        DeckOutputCommand.register(dispatcher);
        DeckTokensCommand.register(dispatcher);
        DeckTokensGiveCommand.register(dispatcher);
        DeckTokensTakeCommand.register(dispatcher);
        DeckListCommand.register(dispatcher);
        DeckHelpCommand.register(dispatcher);
        DeckBroadcastCommand.register(dispatcher);
    }

}
