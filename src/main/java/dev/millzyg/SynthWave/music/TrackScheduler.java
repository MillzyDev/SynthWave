package dev.millzyg.SynthWave.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import dev.millzyg.SynthWave.command.Response;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final ArrayList<AudioTrack> queue;
    public int selectedIndex = 1;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new ArrayList<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        queue.add(track);
        player.startTrack(track, true);
    }

    public ArrayList<AudioTrack> getQueue() { return queue; }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(queue.get((selectedIndex++)), false);

        AudioTrack track = player.getPlayingTrack();
        if (track != null)
            new Response()
                .setTitle("Now Playing")
                .setColour(Color.ORANGE)
                .setMessage(String.format("[%s - %s](%s)", track.getInfo().title, track.getInfo().author, track.getInfo().uri))
                .sendResponse(DiscordMusicInterface.getInstance().getContext().getChannel());
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason == AudioTrackEndReason.FINISHED) {
            nextTrack();
        }
    }
}


