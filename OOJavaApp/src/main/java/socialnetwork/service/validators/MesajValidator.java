package socialnetwork.service.validators;

import socialnetwork.domain.Mesaj;
import socialnetwork.repository.Repository;
import socialnetwork.service.ServiceMesaje;

public class MesajValidator implements Validator<Mesaj>{

    @Override
    public void validate(Mesaj entity) throws ValidationException {
        String err = "";
        if(entity.getText().equals(""))
            err = err.concat("Mesajul nu poate fi vid.");
        if(entity.getText().length() > 100)
            err = err.concat("Mesajul este prea lung.");
        if(!err.equals("")){
            throw new ValidationException(err);
        }
    }
}
