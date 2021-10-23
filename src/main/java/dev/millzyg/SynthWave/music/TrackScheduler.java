package dev.millzyg.SynthWave.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.command.Response;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final TextChannel channel;

    public TrackScheduler(AudioPlayer player, TextChannel channel) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.channel = channel;
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
            Logger.info(String.format("Queued %s by %s", track.getInfo().title, track.getInfo().author));
        }
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void nextTrack() {
        AudioTrack track = player.getPlayingTrack();
        new Response()
                .setColour(Color.ORANGE)
                .setTitle("Now Playing")
                .setMessage(String.format("[%s](%s)", track.getInfo().title, track.getInfo().uri))
                .sendResponse(channel);

        Logger.info(String.format("Now playing %s by %s", track.getInfo().title, track.getInfo().author));
        player.startTrack(queue.poll(), true);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
            Logger.info("Starting next track");
        } else {
            Logger.info("Unable to start next track, end reason was " + endReason.name() + "and value of mayStartNext is " + endReason.mayStartNext);
        }
    }
}

