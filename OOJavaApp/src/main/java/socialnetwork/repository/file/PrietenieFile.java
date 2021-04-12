package socialnetwork.repository.file;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.Validator;

import java.util.List;


public class PrietenieFile extends AbstractFileRepository<Tuple<Long,Long>, Prietenie> {
    /**
     * Contructor
     * @param fileName
     * @param validator
     */
    public PrietenieFile(String fileName, Validator<Prietenie> validator) {
        super(fileName, validator);
    }


    @Override
    public Prietenie extractEntity(List<String> attributes) {
        Prietenie friendship = new Prietenie();
        Long id1 = Long.parseLong(attributes.get(0));
        Long id2 = Long.parseLong(attributes.get(1));
        Tuple<Long,Long> pr = new Tuple<>(id1,id2);
        friendship.setId(pr);
        return friendship;
    }

    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getId().toString();
    }
}
