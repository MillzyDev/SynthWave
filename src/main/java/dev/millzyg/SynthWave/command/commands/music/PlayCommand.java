package dev.millzyg.SynthWave.command.commands.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import dev.millzyg.SynthWave.Main;
import dev.millzyg.SynthWave.command.CommandContext;
import dev.millzyg.SynthWave.command.ICommand;
import dev.millzyg.SynthWave.command.Response;
import dev.millzyg.SynthWave.music.PlayerManager;

import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayCommand implements ICommand {
    private final YouTube youTube;

    public PlayCommand() {
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("SynthWave Personal Music Bot")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        youTube = temp;
    }

    @Override
    public void handle(CommandContext ctx) throws IOException, AuthenticationException {

        String input = String.join(" ", ctx.getArgs());

        if (!isUrl(input)) {
            String ytSearched = searchYouTube(input);

            if (ytSearched == null) {
                new Response()
                        .setAuthor(ctx.getAuthor())
                        .setMessage("No results...")
                        .setColour(Color.RED)
                        .sendResponse(ctx.getChannel());

                return;
            }

            input = ytSearched;
        }
            PlayerManager manager = PlayerManager.getInstance();

            manager.loadAndPlay(ctx.getMessage(), input);
            manager.getGuildMusicManager(ctx.getGuild()).player.getPlayingTrack();

    }

    private boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private String searchYouTube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Main.config.getYouTubeKey())
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception ignored) {}

        return null;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Plays a track";
    }

    @Override
    public Boolean getArgsRequired() {
        return true;
    }

    @Override
    public String getUsage() {
        return "<query/URL>";
    }

    @Override
    public List<String> getAliases() {
        String[] aliases = {"p"};
        return Arrays.asList(aliases);
    }
}
