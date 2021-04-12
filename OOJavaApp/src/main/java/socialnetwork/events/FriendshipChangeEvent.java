package socialnetwork.events;

import socialnetwork.domain.DTOs.FriendDTO;

/**
 * Event for friendship for the Observer pattern
 */
public class FriendshipChangeEvent extends AbstractEvent{

    private ChangeEventType type;
    private FriendDTO data, oldData;

    public FriendshipChangeEvent(ChangeEventType type, FriendDTO data) {
        this.type = type;
        this.data = data;
    }

    public FriendshipChangeEvent(ChangeEventType type, FriendDTO data, FriendDTO oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public FriendDTO getData() {
        return data;
    }

    public FriendDTO getOldData() {
        return oldData;
    }
}
