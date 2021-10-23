package dev.millzyg.SynthWave.command.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import dev.millzyg.SynthWave.command.Response;
import dev.millzyg.SynthWave.music.GuildMusicManager;
import dev.millzyg.SynthWave.music.PlayerManager;
import dev.millzyg.SynthWave.music.TrackScheduler;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        TrackScheduler scheduler = guildMusicManager.scheduler;
        AudioPlayer player = guildMusicManager.player;

        if (player.getPlayingTrack() == null) {
            new Response()
                    .setMessage("There's nothing to skip")
                    .setColour(Color.RED)
                    .sendResponse(channel);

            return;
        }

        Logger.info("Skipped the current track");

        new Response()
                .setColour(Color.YELLOW)
                .setMessage("Skipped to the next song \uD83D\uDE0A")
                .sendResponse(channel);

        scheduler.nextTrack();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips to the next track";
    }
}
