package dev.millzyg.SynthWave.command.commands;

import dev.millzyg.SynthWave.CommandManager;
import dev.millzyg.SynthWave.Main;
import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HelpCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {
        List<String> args = ctx.getArgs();

        if (args.size() == 0) {
            StringBuilder commands = new StringBuilder();

            for (ICommand Command : CommandManager.getCommandClasses())
            {
                commands.append("`").append(Main.config.getPrefix()).append(Command.getName()).append("`").append(" - ").append(Command.getDescription()).append("\n");
            }

            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.orange);
            eb.setTitle("Commands");

            eb.setDescription(commands + "\nYou can do `" + Main.config.getPrefix() + "commands " + getUsage() + "` to get more details on a command");

            ctx.getChannel().sendMessageEmbeds(eb.build()).queue();
            return;
        }

        final String name = args.get(0).toLowerCase(Locale.ROOT);
        ICommand command = null;

        for (ICommand _command : CommandManager.commands) {
            if (_command.getName().equals(name)) {
                command = _command;
                break;
            } else {
                for (String alias : _command.getAliases()) {
                    if (alias.equals(name)) {
                        command = _command;
                        break;
                    }
                }
                command = null;
            }
        }

        if (command == null) {
            ctx.getChannel().sendMessage("I was unable to find that command...").queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.orange);

        eb.setTitle(command.getName().toUpperCase() + " Command");

        if (!command.getDescription().isEmpty()) eb.setDescription(command.getDescription());
        if (!command.getAliases().isEmpty()) eb.addField("**Aliases:**", command.getAliases().stream().map(i -> i).collect(Collectors.joining(", ")), false);
        if (!command.getUsage().isEmpty()) eb.addField("**Usage:**", Main.config.getPrefix() + command.getName() + " " + command.getUsage(), false);

        ctx.getChannel().sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows a list of commands";
    }

    @Override
    public String getUsage() {
        return "[Command Name]";
    }

    @Override
    public List<String> getAliases() {
        String[] aliases = {"cmds", "commands", "h"};
        return Arrays.asList(aliases);
    }
}
