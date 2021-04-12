package socialnetwork.domain.DTOs;

import java.time.LocalDate;
// DTO for friend. Helps load data in the GUI table
public class FriendDTO {

    Long id;
    String firstName;
    String lastName;
    LocalDate friendshipDate;

    public FriendDTO(Long id,String firstName, String lastName, LocalDate friendshipDate) {

        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendshipDate = friendshipDate;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getFriendshipDate() {
        return friendshipDate;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                firstName + ' ' +
                 lastName + ' ' +
                " [" + friendshipDate +
                ']';
    }
}
