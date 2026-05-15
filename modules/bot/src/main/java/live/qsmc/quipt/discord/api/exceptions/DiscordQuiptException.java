package live.qsmc.quipt.discord.api.exceptions;


import live.qsmc.quipt.core.data.exceptions.SimpleQuiptException;

public class DiscordQuiptException extends SimpleQuiptException {
    public DiscordQuiptException(String message) {
        super(message);
    }
}
