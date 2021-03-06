package gravity.gbot.commands.basic;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import gravity.gbot.BuildConfig;
import gravity.gbot.Command;
import gravity.gbot.Main;
import gravity.gbot.music.MusicMaps;
import gravity.gbot.music.PlayerControl;
import gravity.gbot.utils.Config;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;

public class BotInfoCommand implements Command {

    private int userCnt = 0;

    @Override
    public void execute(String[] args, GuildMessageReceivedEvent event) {
        PlayerControl player = new PlayerControl();

        String os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getName() +
                " " + ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getArch() +
                " " + ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getVersion();
        String cpu0 = new DecimalFormat("###.###%").format(ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getProcessCpuLoad());
        String cpu2 = new DecimalFormat("###.###%").format(ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getSystemCpuLoad());
        int cpu1 = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        long ram0 = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() >> 20;
        long ram1 = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() >> 20;

        for (Guild g : event.getJDA().getGuildCache()) {
            userCnt = userCnt + g.getMembers().size();
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Bot Info");
        builder.setColor(Config.GBOT_BLUE);
        builder.setThumbnail(Config.GBOT_EMBED_LOGO);
        builder.addField("Username", event.getGuild().getSelfMember().getUser().getName(), true);
        builder.addField("Discriminator", event.getGuild().getSelfMember().getUser().getDiscriminator(), true);
        builder.addField("Commands", String.valueOf(Main.cmdlist.size() + MusicMaps.musicCmds.size()), true);
        builder.addField("Server Count", String.valueOf(event.getJDA().getGuildCache().size()), true);
        builder.addField("User Count", String.valueOf(userCnt), true);
        builder.addField("Version", BuildConfig.VERSION, true);
        builder.addField("OS", os, true);
        builder.addField("CPU Usage", cpu0 + " / " + cpu2 + " (" + cpu1 + " Cores)" + "\n", false);
        builder.addField("RAM Usage", ram0 + "MB/" + ram1 + "MB" + "\n", false);
        builder.addField("Audio Connections", String.valueOf(player.getActiveConnections(event)), false);
        builder.addField("Language & Library versions", "**Coded in: ** Java (version " + System.getProperty("java.version") + ")" + "\n" + "**JDA version:** " + JDAInfo.VERSION + "\n**LavaPlayer version: **" + PlayerLibrary.VERSION + "\n", false);
        builder.addField("Latest Github Commit", "[" + BuildConfig.GH_COMMIT_SHORT + "](https://github.com/GravityGamer/GravityBot/commit/" + BuildConfig.GH_COMMIT + ")", true);
        builder.addField("Uptime", getUptime(), true);
        event.getChannel().sendMessage(builder.build()).queue();
        userCnt = 0;
    }

    @Override
    public String getUsage() {
        return "botInfo";
    }

    @Override
    public String getDesc() {
        return "Displays info about Gbot.";
    }

    @Override
    public String getAlias() {
        return "botinfo";
    }

    @Override
    public String getType() {
        return "public";
    }

    private String getUptime () {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        long uptimeMs = mxBean.getUptime();
        long s = uptimeMs / 1000;
        long m = s / 60;
        long h = m / 60;
        long d = h / 24;
        return d + "d " + h % 24 + "h " + m % 60 + "m " + s % 60 + "s";
    }
}
