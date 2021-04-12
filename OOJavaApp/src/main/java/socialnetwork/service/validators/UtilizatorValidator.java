package socialnetwork.service.validators;

import socialnetwork.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        String msgs = "";

        if(! entity.getFirstName().matches("^[A-Z]{1}[a-z]+$")){
            msgs = msgs.concat("First name is not valid\n");
        }
        if(! entity.getLastName().matches("^[A-Z]{1}[a-z]+$")){
            msgs = msgs.concat("Last name is not valid\n");
        }
        if(! msgs.equals("")){
            throw new ValidationException(msgs);
        }
    }
}
