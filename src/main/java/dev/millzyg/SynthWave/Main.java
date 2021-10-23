package dev.millzyg.SynthWave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.listeners.OnGuildMessageRecieved;
import dev.millzyg.SynthWave.listeners.OnReady;
import dev.millzyg.SynthWave.listeners.VoiceChannelTimeout;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.commons.io.IOUtils;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public JDA jda;
    public static Config config;

    private Main(String[] args) throws LoginException, IOException {
        // load config
        try (FileInputStream inputStream = new FileInputStream("config.json")) {
            Gson gson = new Gson();
            String jsonStr = IOUtils.toString(inputStream);
            config = gson.fromJson(jsonStr, Config.class);
        }

        Logger.info("Prefix is set to \"" + config.getPrefix() + "\"");

        Logger.info("Attempting login with the token " + config.getDiscordToken());
        Init(config.getDiscordToken());
    }

    private void Init(String token) throws LoginException {
        jda = JDABuilder
                .createDefault(token)
                .addEventListeners(
                        new OnGuildMessageRecieved(),
                        new OnReady(),
                        new VoiceChannelTimeout()
                )
                .build()
        ;
    }

    public static void main(String[] args) throws LoginException, IOException {
        try {
            new Main(args);
        } catch (FileNotFoundException e) {
            Logger.severe(e.getMessage());
            Logger.info("Refreshing config");
            ResourceUtil.ExportResource("/config.json", "config.json");
            Logger.info("Refreshed config... please edit config, and restart");
        }
    }
}
