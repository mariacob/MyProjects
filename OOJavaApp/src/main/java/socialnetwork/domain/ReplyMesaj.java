package socialnetwork.domain;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Class for a message reply. Extends message, adds reference to the replied message
 */
public class ReplyMesaj extends Mesaj{
    private Mesaj reply;

    public ReplyMesaj(Utilizator from, List<Utilizator> to, String text, Mesaj reply) {
        super(from, to, text);
        this.reply = reply;
    }

    public Mesaj getReply() {
        return reply;
    }

    @Override
    public String toString() {
        return "Reply for: " + reply.getText() + " [" + reply.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +" ]\n       "
                + from.getFirstName() + " " + from.getLastName() + ": " + text + " [" + getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                "] ";
    }
}
