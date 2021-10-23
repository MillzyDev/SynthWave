package dev.millzyg.SynthWave.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.command.Response;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager instance;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    private PlayerManager() {
        this.musicManagers = new HashMap<Long, GuildMusicManager>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild, TextChannel channel) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager, channel);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(Message message, String trackUrl) {
        TextChannel channel = message.getTextChannel();
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild(), channel);

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                Response response = new Response()
                        .setColour(Color.ORANGE)
                        .setMessage(String.format("Queued [%s - %s](%s) [<@!%s>]", audioTrack.getInfo().title, audioTrack.getInfo().author, trackUrl, message.getAuthor().getId()));
                response.sendResponse(channel);

                Logger.info("Loaded song" + audioTrack.getInfo().title);

                play(musicManager, audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

                new Response()
                        .setColour(Color.ORANGE)
                        .setMessage("Queued **" + audioPlaylist.getTracks().size() + "** tracks")
                        .sendResponse(channel);

                Logger.info(String.format("Loaded playlist %s", audioPlaylist.getName()));

                for (AudioTrack track : audioPlaylist.getTracks()) {
                    play(musicManager, track);
                }
            }

            @Override
            public void noMatches() {
                new Response()
                        .setColour(Color.RED)
                        .setMessage("There were no matches with the URL `" + trackUrl + "`")
                        .sendResponse(channel);
                Logger.info(String.format("No matches with the URL %s", trackUrl));
            }

            @Override
            public void loadFailed(FriendlyException e) {
                new Response()
                        .setColour(Color.GREEN)
                        .setMessage("Unable to play track: " + e.getMessage())
                        .sendResponse(channel);
                Logger.info(String.format("Unable to play track: %s", e.getMessage()));
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }
}
