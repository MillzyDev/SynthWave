package dev.millzyg.SynthWave.command.commands.music;

import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import dev.millzyg.SynthWave.command.Response;
import dev.millzyg.SynthWave.music.GuildMusicManager;
import dev.millzyg.SynthWave.music.PlayerManager;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(ctx.getGuild());

        guildMusicManager.player.setPaused(true);

        Logger.info("Paused the player");

        new Response()
                .setColour(Color.YELLOW)
                .setMessage("⏸️ Paused the player")
                .sendResponse(ctx.getChannel());
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getDescription() {
        return "Pauses current track";
    }
}
