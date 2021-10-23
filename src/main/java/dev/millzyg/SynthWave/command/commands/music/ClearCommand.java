package dev.millzyg.SynthWave.command.commands.music;

import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import dev.millzyg.SynthWave.command.Response;
import dev.millzyg.SynthWave.music.GuildMusicManager;
import dev.millzyg.SynthWave.music.PlayerManager;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;

public class ClearCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(ctx.getGuild(), ctx.getChannel());

        guildMusicManager.scheduler.getQueue().clear();
        guildMusicManager.player.stopTrack();
        guildMusicManager.player.setPaused(false);

        new Response()
                .setColour(Color.ORANGE)
                .setMessage("Cleared the queue")
                .setFooter("Command Message ID: " + ctx.getMessage().getId())
                .setAuthor(ctx.getAuthor())
                .sendResponse(ctx.getChannel());
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Clears the queue";
    }
}
