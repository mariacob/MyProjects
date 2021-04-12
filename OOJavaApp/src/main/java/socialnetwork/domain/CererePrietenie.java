package socialnetwork.domain;

import java.time.LocalDate;
// Friend request class. Inherits from Entity
public class CererePrietenie extends Entity<Tuple<Long,Long>>{
    private Long from;
    private Long to;
    private Status status;
    LocalDate date;


    public CererePrietenie(Long from, Long to) {
        this.from = from;
        this.to = to;
        this.status = Status.PENDING;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
