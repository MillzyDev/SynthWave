package dev.millzyg.SynthWave.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class Response {
    private User author;
    private String title, message, footer, thumbnail;
    private Color colour;

    public Response setAuthor(User author) { this.author = author; return this; }

    public Response setTitle(String title) { this.title = title; return this; }

    public Response setMessage(String message) { this.message = message; return this; }

    public Response setFooter(String footer) { this.footer = footer; return this; }

    public Response setColour(Color colour) { this.colour = colour; return this; }

    public Response setThumbnail(String thumbnail) { this.thumbnail = thumbnail; return this;}

    public void sendResponse(TextChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();

        if (author != null) eb.setAuthor(author.getName(), null, author.getAvatarUrl());
        if (title != null) eb.setTitle(title);
        if (message != null) eb.setDescription(message);
        if (footer != null) eb.setFooter(footer);
        if (colour != null) eb.setColor(colour);
        if (thumbnail != null) eb.setThumbnail(thumbnail);

        channel.sendMessageEmbeds(eb.build()).queue();
    }
}
