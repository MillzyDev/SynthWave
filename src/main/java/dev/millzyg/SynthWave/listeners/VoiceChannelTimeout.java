package dev.millzyg.SynthWave.listeners;

import dev.millzyg.MillzyLogger.Logger;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class VoiceChannelTimeout extends ListenerAdapter {
    private Timer timer;

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        Logger.info(event.getMember().getUser().getAsTag() + " joined voice channel " + event.getChannelJoined().getName());

        if (event.getChannelJoined().getMembers().size() > 1
                && Objects.requireNonNull(event.getGuild().getSelfMember().getVoiceState()).getChannel() == event.getChannelJoined()
                && timer != null
        ) {
            timer.cancel();
            timer = null;
            Logger.info("VC timeout timer canceled");
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        Logger.info(event.getMember().getUser().getAsTag() + " left voice channel " + event.getChannelLeft().getName());

        if (event.getChannelLeft().getMembers().size() == 1
                && Objects.requireNonNull(event.getGuild().getSelfMember().getVoiceState()).getChannel() == event.getChannelLeft()
        ) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }, 10000);
            Logger.info("Started VC timeout timer");
        }
    }
}
