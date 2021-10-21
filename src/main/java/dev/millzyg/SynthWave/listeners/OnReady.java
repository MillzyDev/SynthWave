package dev.millzyg.SynthWave.listeners;

import dev.millzyg.MillzyLogger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnReady extends ListenerAdapter {
    private JDA jda;
    private SelfUser self;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        jda = event.getJDA();
        self = jda.getSelfUser();

        Logger.info("Logged in as " + self.getAsTag());

        Activity activity = Activity.playing("SynthWave by Millzy");
        jda.getPresence().setActivity(activity);

    }
}
