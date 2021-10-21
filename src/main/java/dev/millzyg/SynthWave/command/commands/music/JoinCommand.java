package dev.millzyg.SynthWave.command.commands.music;

import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import dev.millzyg.SynthWave.command.Response;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;

public class JoinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {
        TextChannel channel = ctx.getChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        Response response = new Response()
                .setAuthor(ctx.getAuthor())
                .setFooter("Command Message ID: " + ctx.getMessage().getId());

        if (audioManager.isConnected()) {
            response
                    .setTitle("Unable to join channel")
                    .setMessage("SynthWave is already connected to a voice channel...")
                    .setColour(Color.RED);
            response.sendResponse(channel);
            return;
        }

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            response
                    .setTitle("Unable to join voice channel")
                    .setMessage("SynthWave requires you to be in a channel in order to join said channel...")
                    .setColour(Color.RED);
            response.sendResponse(channel);
            return;
        }

        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        Member selfMember = ctx.getGuild().getSelfMember();

        if (!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
            response
                    .setTitle("Unable to join voice channel")
                    .setMessage("SynthWave does not have permission to join that voice channel...")
                    .setColour(Color.RED);
            response.sendResponse(channel);
            return;
        }

        audioManager.openAudioConnection(voiceChannel);
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Makes SynthWave join the voice channel you are currently in.";
    }
}
