package dev.millzyg.SynthWave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.millzyg.MillzyLogger.Logger;
import dev.millzyg.SynthWave.listeners.OnGuildMessageRecieved;
import dev.millzyg.SynthWave.listeners.OnReady;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public JDA jda;
    public static Config config;

    private Main(String[] args) throws LoginException {
        // load config from resources
        InputStreamReader isReader =
                new InputStreamReader(
                        Objects.requireNonNull(this.getClass().getResourceAsStream("/config.json"))
                );
        BufferedReader reader = new BufferedReader(isReader);
        // serialise reader to Config object
        Gson gson = new Gson();
        String jsonStr = reader.lines().collect(Collectors.joining());
        config = gson.fromJson(jsonStr, Config.class);

        Logger.info("Prefix is set to \"" + config.getPrefix() + "\"");

        // check if new token was provided
        if (args.length > 0 && args[0].length() > 0) {
            Logger.info("Attempting to login with the token: " + args[0]);
            Init(args[0]);
        } else if (config.getDiscordToken().length() > 0) { // login with token in config if no args are provided
            Logger.info("Attempting to login with the token: " + config.getDiscordToken());
            Init(config.getDiscordToken());
        } else { // if there is no token provided at all, the process will throw an exception
            Logger.severe("Unable to fetch token or no token was provided.");
            throw new LoginException();
        }
    }

    private void Init(String token) throws LoginException {
        jda = JDABuilder
                .createDefault(token)
                .addEventListeners(
                        new OnGuildMessageRecieved(),
                        new OnReady()
                )
                .build()
        ;
    }

    public static void main(String[] args) throws LoginException {
        new Main(args);
    }
}
