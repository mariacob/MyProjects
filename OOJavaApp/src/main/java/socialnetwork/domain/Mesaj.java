package socialnetwork.domain;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Utilizator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
// Message class. Inherits from entity
public class Mesaj extends Entity<Long> {
    Utilizator from;
    String text;
    List<Utilizator> to;
    LocalDateTime date;

    public Mesaj(Utilizator from, List<Utilizator> to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public Utilizator getFrom() {
        return from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return  from.getFirstName() + " " + from.getLastName() + ": " + text + " [" + getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                "] ";
    }
}