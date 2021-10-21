package dev.millzyg.SynthWave;

import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import dev.millzyg.SynthWave.command.Response;
import dev.millzyg.SynthWave.command.commands.PingCommand;
import dev.millzyg.SynthWave.command.commands.music.JoinCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    public static final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new JoinCommand());

        addCommand(new PingCommand());
    }

    public static List<ICommand> getCommandClasses() {
        return commands;
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("Command already exists...");
        }

        commands.add(cmd);
    }

    @Nullable
    private ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public void handle(GuildMessageReceivedEvent event) throws IOException, AuthenticationException {
        Message message = event.getMessage();

        String[] split = message.getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Main.config.getPrefix()), "")
                .split("\\s");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            message.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            if (cmd.getArgsRequired() && args.isEmpty()) {
                Response response = new Response()
                        .setAuthor(message.getAuthor())
                        .setMessage("This command requires arguments, but none were provided! ```\n" +
                                "Proper Usage:\n" + Main.config.getPrefix() + cmd.getName() + " " + cmd.getUsage() + "\n```"
                                )
                        .setTitle("Invalid Arguments!")
                        .setFooter("Command Message ID: " + message.getId())
                        .setColour(Color.RED);

                response.sendResponse(message.getTextChannel());
                return;
            }

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
            Logger.info(cmd.getName().toUpperCase() + " Command was executed by " + message.getAuthor().getAsTag());
        }

    }
}
