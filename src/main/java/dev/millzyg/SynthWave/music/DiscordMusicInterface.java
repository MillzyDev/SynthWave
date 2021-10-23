package dev.millzyg.SynthWave.music;

import dev.millzyg.SynthWave.command.CommandContext;
import net.dv8tion.jda.api.JDA;

/**
 * This class exists to allow easy access to discord and JDA fields and methods in the music areas,
 * allowing other kinds of info to be easily transferred.
 * also pushing variables into the music section kinda broke it fsr
 */
public class DiscordMusicInterface {
    public enum Loop {
        OFF,
        TRACK,
        QUEUE
    }

    private static DiscordMusicInterface instance;

    private JDA jda;
    private CommandContext context;

    private DiscordMusicInterface() {}

    public static DiscordMusicInterface getInstance() {
        if (instance == null)
            instance = new DiscordMusicInterface();

        return instance;
    }

    public JDA getJDA() { return this.jda; }
    public void setJDA(JDA jda) { this.jda = jda; }

    public CommandContext getContext() { return this.context; }
    public void setContext(CommandContext context) { this.context = context; }
}
