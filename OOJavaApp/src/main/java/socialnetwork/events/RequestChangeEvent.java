package socialnetwork.events;

import socialnetwork.domain.DTOs.FRequestDTO;

/**
 * Event for request for the Observer pattern
 */
public class RequestChangeEvent extends AbstractEvent{


    private ChangeEventType type;
    private FRequestDTO data, oldData;

    public RequestChangeEvent(ChangeEventType type, FRequestDTO data) {
        this.type = type;
        this.data = data;
    }

    public RequestChangeEvent(ChangeEventType type, FRequestDTO data, FRequestDTO oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public FRequestDTO getData() {
        return data;
    }

    public FRequestDTO getOldData() {
        return oldData;
    }
}
