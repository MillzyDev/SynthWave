package dev.millzyg.SynthWave.command.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import dev.millzyg.SynthWave.command.Response;
import dev.millzyg.SynthWave.music.GuildMusicManager;
import dev.millzyg.SynthWave.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static java.awt.Color.ORANGE;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        ArrayList<AudioTrack> queue = musicManager.scheduler.getQueue();

        if (queue.isEmpty()) {
            new Response()
                    .setAuthor(ctx.getAuthor())
                    .setMessage("The queue is empty :(")
                    .setColour(ORANGE)
                    .sendResponse(ctx.getChannel());

            return;
        }

        int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> tracks = new ArrayList<>(queue);
        StringBuilder q = new StringBuilder();
        q.append("```nim\n");

        AudioTrackInfo currentlyPlaying = musicManager.player.getPlayingTrack().getInfo();
        q.append(String.format("Currently playing: %s - %s\n\n", currentlyPlaying.title, currentlyPlaying.author));

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = tracks.get(i);
            AudioTrackInfo info = track.getInfo();

            q.append(String.format("%o) %s - %s\n", i + 1, info.title, info.author));
        }

        q.append("\n This is the end of the queue!\n```");
        channel.sendMessage(q.toString()).queue();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Shows all currently queued songs";
    }

    @Override
    public List<String> getAliases() {
        String[] aliases = {"q"};
        return Arrays.asList(aliases);
    }
}
