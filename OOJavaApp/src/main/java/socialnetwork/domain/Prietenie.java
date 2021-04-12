package socialnetwork.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Friendship class. inherits from entity
public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDate date;

    public Prietenie(){
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate d){
        date = d;
    }
}
