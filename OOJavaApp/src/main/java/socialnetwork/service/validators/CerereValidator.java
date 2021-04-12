package socialnetwork.service.validators;

import socialnetwork.domain.CererePrietenie;

public class CerereValidator implements Validator<CererePrietenie> {
    @Override
    public void validate(CererePrietenie entity) throws ValidationException {
        String err = "";
        if(entity.getTo() == entity.getFrom())
            err = err.concat("Un utilizator nu isi poate trimite cerere de prietenie lui insusi.");
        if(!err.equals("")){
            throw new ValidationException(err);
        }
    }
}
