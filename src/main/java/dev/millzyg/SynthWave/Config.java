package dev.millzyg.SynthWave;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

public class Config {
    private String discordToken;
    private String prefix;
    public Config(){}

    public String getDiscordToken(){
        return this.discordToken;
    }
    public String getPrefix() { return this.prefix; }
}
