package dev.millzyg.SynthWave.command;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx) throws IOException, AuthenticationException;

    String getName();
    String getDescription();

    default Boolean getArgsRequired() { return false; }

    default String getUsage() { return ""; }

    default List<String> getAliases() { return Collections.emptyList(); }
}
