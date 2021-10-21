package dev.millzyg.SynthWave.listeners;

import dev.millzyg.SynthWave.CommandManager;
import dev.millzyg.SynthWave.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.naming.AuthenticationException;
import java.io.IOException;

public class OnGuildMessageRecieved extends ListenerAdapter {
    private User user;
    private Message message;
    private String raw;

    private static final String prefix = Main.config.getPrefix();

    private final CommandManager manager = new CommandManager();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        user = event.getAuthor();
        message = event.getMessage();

        if (user.isBot()) return;

        raw = message.getContentRaw();

        if (!raw.startsWith(prefix)) return;
        try {
            manager.handle(event);
        } catch (AuthenticationException | IOException e) {
            e.printStackTrace();
        }
    }
}
