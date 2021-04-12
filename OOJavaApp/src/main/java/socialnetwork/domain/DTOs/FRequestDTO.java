package socialnetwork.domain.DTOs;

import socialnetwork.domain.Status;
import socialnetwork.domain.Tuple;

import java.time.LocalDate;
// DTO for friend request. Helps load data in the GUI table
public class FRequestDTO {

    Tuple<Long,Long> id;
    String firstName;
    String lastName;
    Status status;
    LocalDate friendRequestDate;

    public FRequestDTO(Tuple<Long,Long> id,String firstName, String lastName, Status status, LocalDate friendshipRequestDate) {

        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status=status;
        this.friendRequestDate = friendshipRequestDate;
    }

    public LocalDate getFriendRequestDate() {
        return friendRequestDate;
    }

    public Status getStatus() {
        return status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Tuple<Long,Long> getId() {
        return id;
    }

    @Override
    public String toString() {
        return "FRequestDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                ", friendRequestDate=" + friendRequestDate +
                '}';
    }
}
