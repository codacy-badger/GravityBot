package gravity.gbot.commands.owner;

import gravity.gbot.BuildConfig;
import gravity.gbot.Command;
import gravity.gbot.utils.Config;
import gravity.gbot.utils.GuildConfig;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;

public class Eval implements Command {

    @Override
    public void execute(String[] args, GuildMessageReceivedEvent event) {
        if (!event.getAuthor().getId().equals(BuildConfig.OWNER_ID)) {
            event.getChannel().sendMessage("This Command is reserved for the bot owner.").queue();
        } else {

            ScriptEngine engine;
            ScriptEngineManager factory = new ScriptEngineManager();

            engine = factory.getEngineByName("groovy");


            try {
                engine.put("Config", Config.class);
                engine.put("event", event);
                Object out = engine.eval(event.getMessage().getContentRaw().replace(GuildConfig.getPrefix(event.getGuild().getId(), this.getClass().getName()) + "eval ", ""));
                if (out != null) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("Successfully Evaluated");
                    builder.setColor(Color.WHITE);
                    builder.setDescription(out.toString());
                    event.getChannel().sendMessage(builder.build()).queue();
                }
            } catch (ScriptException e) {
                event.getChannel().sendMessage(e.getMessage()).queue();
            }

        }
    }


    @Override
    public String getUsage() {
        return "eval (Java/Groovy Code)";
    }

    @Override
    public String getDesc() {
        return "Executes Java/Groovy Code and sends the output to a text channel.";
    }

    @Override
    public String getAlias() {
        return "eval";
    }

    @Override
    public String getType() {
        return "owner";
    }
}
