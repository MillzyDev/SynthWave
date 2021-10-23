package dev.millzyg.SynthWave.command.commands.music;

import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import dev.millzyg.SynthWave.command.Response;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {
        TextChannel channel = ctx.getChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        Response response = new Response()
                .setAuthor(ctx.getAuthor())
                .setFooter("Command Message ID: " + ctx.getMessage().getId());

        if (!audioManager.isConnected()) {
            response
                    .setMessage("I'm not in a voice channel silly")
                    .setColour(Color.RED);
            response.sendResponse(channel);
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(ctx.getMember())) {
            response
                    .setMessage("You must be in the same channel as me to use that command")
                    .setColour(Color.RED);
            response.sendResponse(channel);
            return;
        }

        audioManager.closeAudioConnection();
        new Response()
                .setColour(Color.ORANGE)
                .setMessage("Left your voice channel")
                .sendResponse(ctx.getChannel());
        Logger.info("Disconnected from voice channel: " + voiceChannel.getName());
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Makes SynthWave leave the current voice channel";
    }

    @Override
    public List<String> getAliases() {
        String[] aliases = {"disconnect"};
        return Arrays.asList(aliases);
    }
}
