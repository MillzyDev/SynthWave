package dev.millzyg.SynthWave.command.commands;

import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import java.awt.*;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx)
    {
        JDA jda = ctx.getJDA();

        EmbedBuilder eb = new EmbedBuilder();

        eb.setAuthor(ctx.getAuthor().getAsTag(), null, ctx.getAuthor().getAvatarUrl());
        eb.setColor(Color.orange);

        jda.getRestPing().queue( (time) -> {
                ctx.getChannel()
                        .sendMessageEmbeds(eb.setDescription("⏳ API Latency: `" + time + "ms`\n \uD83D\uDC93 WS Heartbeat: `"+ jda.getGatewayPing() + "ms`").build()).queue();
                Logger.info("API Latency: " + time + "ms - WS Heartbeat: "+ jda.getGatewayPing() + "ms");
            }

        );
    }

    @Override
    public String getName()
    {
        return "ping";
    }

    @Override
    public String getDescription()
    {
        return "Round trip delay";
    }
}
