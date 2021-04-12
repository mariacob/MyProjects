package socialnetwork.service.validators;

import socialnetwork.domain.Prietenie;

public class PrietenieValidator implements  Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        String msgs = "";
        if(! entity.getId().getLeft().toString().matches("^[0-9]+$")){
            msgs = msgs.concat("First id is not valid!");
        }
        if(! entity.getId().getRight().toString().matches("^[0-9]+$")){
            msgs = msgs.concat("Second id is not valid!");
        }
        if(!msgs.equals("")){
            throw new ValidationException(msgs);
        }
    }
}
