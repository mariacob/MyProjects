package socialnetwork.service;

import socialnetwork.domain.Entity;
import socialnetwork.repository.Repository;

public abstract class AbstractService<ID, E extends Entity<ID>> {
    protected Repository<ID,E> repo;

    public AbstractService(Repository<ID, E> repo) {
        this.repo = repo;
    }

    /**
     *
     * @param id
     * @return the removed entity by ID
     */
    public E removeEntity(ID id){
        return repo.delete(id);
    }

    /**
     *
     * @param id
     * @return the entity found by ID
     */
    public E findEntity(ID id){
        return repo.findOne(id);
    }
    public Iterable<E> getAll(){
        return repo.findAll();
    }
}
