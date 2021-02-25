package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

/**
 * @author xtypr
 * @since 2015/11/12
 */
public class SayCommand extends VanillaCommand {

    public SayCommand(String name) {
        super(name, "%nukkit.command.say.description", "%commands.say.usage");
        this.setPermission("nukkit.command.say");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("message", CommandParamType.MESSAGE)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }

        String senderString;
        if (sender instanceof Player) {
            senderString = ((Player) sender).getDisplayName();
        } else if (sender instanceof ConsoleCommandSender) {
            senderString = "Server";
        } else {
            senderString = sender.getName() + TextFormat.RESET;
        }

        StringBuilder msg = new StringBuilder();
        for (String arg : args) {
            msg.append(arg).append(" ");
        }
        if (msg.length() > 0) {
            msg = new StringBuilder(msg.substring(0, msg.length() - 1));
        }
        
        sender.getServer().broadcastAnnouncement(senderString, new TranslationContainer("%chat.type.announcement", senderString, msg.toString()));
        
        return true;
    }
}
