package dev.millzyg.SynthWave.command.commands.music;

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

public class UnpauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(ctx.getGuild(), ctx.getChannel());

        guildMusicManager.player.setPaused(false);

        new Response()
                .setColour(Color.YELLOW)
                .setMessage("▶️ Unpaused the player")
                .sendResponse(ctx.getChannel());
    }

    @Override
    public String getName() {
        return "unpause";
    }

    @Override
    public String getDescription() {
        return "Resumes current track";
    }

    @Override
    public List<String> getAliases() {
        String[] aliases = {"resume"};
        return Arrays.asList(aliases);
    }
}