package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.DTOs.FRequestDTO;
import socialnetwork.events.ChangeEventType;
import socialnetwork.events.FriendshipChangeEvent;
import socialnetwork.observer.Observable;
import socialnetwork.observer.Observer;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.RepoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceCereriPrietenie extends AbstractService<Tuple<Long,Long>, CererePrietenie> implements Observable<FriendshipChangeEvent> {
    Repository<Tuple<Long, Long>, Prietenie> repoP;
    Repository<Long,Utilizator> repoU;
    ServicePrietenii servicePrietenii;
    private final List<Observer<FriendshipChangeEvent>> observers=new ArrayList<>();

    public ServiceCereriPrietenie(Repository<Tuple<Long,Long>, CererePrietenie> repo,Repository<Tuple<Long, Long>, Prietenie> repoP, Repository<Long,Utilizator> repoU,ServicePrietenii servicePrietenii) {
        super(repo);
        this.repoP = repoP;
        this.repoU = repoU;
        this.servicePrietenii = servicePrietenii;
    }

    /**
     *
     * @param from id of the sender
     * @param to id of the receiver
     * @return the saved friend request
     */
    public CererePrietenie addCerere(Long from, Long to){
        CererePrietenie c = new CererePrietenie(from,to);
        c.setDate(LocalDate.now());
        repo.findAll().forEach(id->{
            if(id.getId().equals(new Tuple<>(from,to)))
                throw new RepoException("You already sent a friend request to this user");
        });
        repo.save(c);
        return c;
    }

    /**
     * Accept a friend request
     * @param from id of the sender
     * @param to id of the receiver
     */
    public void acceptCerere(Long from, Long to){
        CererePrietenie req = repo.findOne(new Tuple<>(from, to));
        if(req == null)
                throw new RepoException("Request does not exist");
        if(!req.getStatus().equals(Status.PENDING))
            throw new RepoException("Request is not pending.");
        req.setStatus("APPROVED");
        repo.update(req);
        servicePrietenii.addPrietenie(from,to);
        notifyObservers(new FriendshipChangeEvent(ChangeEventType.UPDATE,null));
    }

    /**
     * Reject a friend request
     * @param from id of the sender
     * @param to id of the receiver
     */
    public void refuseCerere(Long from, Long to){
        CererePrietenie req = repo.findOne(new Tuple<>(from, to));
        if(req == null)
            throw new RepoException("Request does not exist");
        if(!req.getStatus().equals(Status.PENDING))
            throw new RepoException("Request is not pending.");
        req.setStatus("REJECTED");
        repo.update(req);
        notifyObservers(new FriendshipChangeEvent(ChangeEventType.UPDATE,null));
    }

    public void deleteRequestsOfUser(Long id){
        List<CererePrietenie> l = StreamSupport.stream(repo.findAll().spliterator(),false).collect(Collectors.toList());
        l.forEach(x->{
            if(x.getId().getLeft().equals(id) || x.getId().getRight().equals(id)) {
                repo.delete(x.getId());
            }
        });
    }

    /**
     *
     * @param id id of the user
     * @return list of users' received friend requests
     */
    public List<FRequestDTO> getRequestsForUser(Long id){
        List<CererePrietenie> requests= StreamSupport
                .stream(repo.findAll().spliterator(),false)
                .filter(fRequest -> {
                    return fRequest.getTo().equals(id);
                })
                .collect(Collectors.toList());
        return requests.stream()
                .map(fRequest->{
                    Utilizator utilizator=repoU.findOne(fRequest.getFrom());
                    return new FRequestDTO(fRequest.getId(),utilizator.getFirstName(),utilizator.getLastName(),fRequest.getStatus(),fRequest.getDate());
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id id of the user
     * @return list of pending friend requests sent by the user
     */
    public List<FRequestDTO> getUserPendingRequests(Long id){
        List<CererePrietenie> requests= StreamSupport
                .stream(repo.findAll().spliterator(),false)
                .filter(fRequest -> {
                    return fRequest.getFrom().equals(id) && fRequest.getStatus().equals(Status.PENDING);
                })
                .collect(Collectors.toList());
        return requests.stream()
                .map(fRequest->{
                    Utilizator utilizator=repoU.findOne(fRequest.getTo());
                    return new FRequestDTO(fRequest.getId(),utilizator.getFirstName(),utilizator.getLastName(),fRequest.getStatus(),fRequest.getDate());
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id id of the friend request
     * @return the removed friend request
     */
    public FRequestDTO removePendingRequest(Tuple<Long,Long> id){
        CererePrietenie deleted_request = repo.delete(id);
        if(deleted_request != null){
            Utilizator utilizator = repoU.findOne(deleted_request.getTo());
            FRequestDTO deleted_dto = new FRequestDTO(deleted_request.getId(),utilizator.getFirstName(),utilizator.getLastName(),deleted_request.getStatus(),deleted_request.getDate());
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE,null));
            return deleted_dto;
        }
        return null;
    }

    /**
     * adds observer to list
     * @param e observer
     */
    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {

        observers.add(e);

    }

    /**
     * removes observer from list
     * @param e observer
     */
    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {

        observers.remove(e);
    }

    /**
     * notifies all the observers
     * @param t friendship change event
     */
    @Override
    public void notifyObservers(FriendshipChangeEvent t) {

        observers.forEach(x->x.update(t));

    }
}
