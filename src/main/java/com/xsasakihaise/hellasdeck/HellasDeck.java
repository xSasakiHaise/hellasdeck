package com.xsasakihaise.hellasdeck;

import com.mojang.brigadier.CommandDispatcher;
import com.xsasakihaise.hellascontrol.api.CoreCheck;
import com.xsasakihaise.hellasdeck.commands.DeckBroadcastCommand;
import com.xsasakihaise.hellasdeck.commands.DeckDependenciesCommand;
import com.xsasakihaise.hellasdeck.commands.DeckDrawCommand;
import com.xsasakihaise.hellasdeck.commands.DeckEndCommand;
import com.xsasakihaise.hellasdeck.commands.DeckFeaturesCommand;
import com.xsasakihaise.hellasdeck.commands.DeckHelpCommand;
import com.xsasakihaise.hellasdeck.commands.DeckInputCommand;
import com.xsasakihaise.hellasdeck.commands.DeckListCommand;
import com.xsasakihaise.hellasdeck.commands.DeckOutputCommand;
import com.xsasakihaise.hellasdeck.commands.DeckProtectCommand;
import com.xsasakihaise.hellasdeck.commands.DeckStartCommand;
import com.xsasakihaise.hellasdeck.commands.DeckTokensCommand;
import com.xsasakihaise.hellasdeck.commands.DeckTokensGiveCommand;
import com.xsasakihaise.hellasdeck.commands.DeckTokensTakeCommand;
import com.xsasakihaise.hellasdeck.commands.DeckVersionCommand;
import net.minecraft.command.CommandSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("hellasdeck")
public class HellasDeck {
    public static HellasDeckInfoConfig infoConfig;

    public HellasDeck() {
        CoreCheck.verifyCoreLoaded();
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            CoreCheck.verifyEntitled("hellasdeck");
        }

        infoConfig = new HellasDeckInfoConfig();
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        if (!ModList.get().isLoaded("hellascontrol")) {
            return;
        }
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        DeckVersionCommand.register(dispatcher, infoConfig);
        DeckDependenciesCommand.register(dispatcher, infoConfig);
        DeckFeaturesCommand.register(dispatcher, infoConfig);
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
