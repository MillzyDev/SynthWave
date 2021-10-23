package dev.millzyg.SynthWave.command.commands.music;

import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import dev.millzyg.SynthWave.command.Response;
import dev.millzyg.SynthWave.music.PlayerManager;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class VolumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {
        PlayerManager playerManager = PlayerManager.getInstance();

        try {
            int volume = Integer.parseInt(ctx.getArgs().get(0));

            playerManager.getGuildMusicManager(ctx.getGuild(), ctx.getChannel()).player.setVolume(volume);

            new Response()
                    .setMessage(String.format("Changed volume to **%s**", volume))
                    .setColour(Color.ORANGE)
                    .sendResponse(ctx.getChannel());
        } catch (NumberFormatException e) {
            new Response()
                    .setMessage("New volume must be a **number**")
                    .setColour(Color.RED)
                    .sendResponse(ctx.getChannel());
        }


    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getDescription() {
        return "Changes the song volume";
    }

    @Override
    public Boolean getArgsRequired() {
        return true;
    }

    @Override
    public String getUsage() {
        return "<New Volume>";
    }

    @Override
    public List<String> getAliases() {
        String[] aliases = {"v"};
        return Arrays.asList(aliases);
    }
}
