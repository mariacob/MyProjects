package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.validators.RepoException;
import socialnetwork.service.validators.ValidationException;
import socialnetwork.service.validators.Validator;
import socialnetwork.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    protected Map<ID,E> entities;

    /**
     * keeps a map for the entities
     * @param validator
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("Entity must not be null");
        validator.validate(entity);
        if(entities.containsKey(entity.getId())){
            throw new RepoException("ID already exist");
        }
        return entities.putIfAbsent(entity.getId(),entity);
    }

    @Override
    public E delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException("ID must not be null");
        if(! entities.containsKey(id)){
            throw new RepoException("ID doesn't exist");
        }
        return entities.remove(id);
    }

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;


    }

}
