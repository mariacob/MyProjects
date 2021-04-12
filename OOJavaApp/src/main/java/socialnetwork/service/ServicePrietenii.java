package socialnetwork.service;

import socialnetwork.domain.DTOs.FriendDTO;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.events.ChangeEventType;
import socialnetwork.events.FriendshipChangeEvent;
import socialnetwork.observer.Observable;
import socialnetwork.observer.Observer;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.RepoException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServicePrietenii extends AbstractService<Tuple<Long,Long>,Prietenie> implements Observable<FriendshipChangeEvent> {

    Repository<Long, Utilizator> repoU;
    private final List<Observer<FriendshipChangeEvent>> observers=new ArrayList<>();

    public ServicePrietenii(Repository<Tuple<Long, Long>, Prietenie> repoP,Repository<Long,Utilizator> repoU) {
        super(repoP);
        this.repoU = repoU;
    }

    /**
     * deletes all the friendships of a user
     * @param id user's id
     */
    public void deteleFriendshipsOfUser(Long id){
        List<Prietenie> l = StreamSupport.stream(repo.findAll().spliterator(),false).collect(Collectors.toList());
        l.forEach(x->{
            if(x.getId().getLeft().equals(id) || x.getId().getRight().equals(id)) {
                repo.delete(x.getId());
            }
        });
    }

    /**
     *
     * @param id1 first user's id
     * @param id2 second user's id
     * @return the added friendship
     */
    public Prietenie addPrietenie(Long id1, Long id2){
        Prietenie fr = new Prietenie();
        fr.setDate(LocalDate.now());
        fr.setId(new Tuple<>(id1,id2));
        repo.findAll().forEach(id->{
            if(id.getId().equals(new Tuple<>(id1,id2)))
                throw new RepoException("Already friends!");
        });
        repo.save(fr);
        notifyObservers(new FriendshipChangeEvent(ChangeEventType.ADD,null));
        return fr;
    }

    /**
     * adds observer to the list
     * @param e observer
     */
    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {

        observers.add(e);

    }

    /**
     * removes an observer from the list
     * @param e observer
     */
    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {

        observers.remove(e);
    }

    /**
     * notify all the observers
     * @param t friendship change event
     */
    @Override
    public void notifyObservers(FriendshipChangeEvent t) {

        observers.forEach(x->x.update(t));

    }

    /**
     *
     * @param id user id
     * @return returns a list of friend dtos of a user's friends
     */
    public List<FriendDTO> getFriendsOfUser(Long id) {
        List<Prietenie> list = StreamSupport
                .stream(repo.findAll().spliterator(),false)
                .filter(x-> x.getId().getLeft().equals(id) || x.getId().getRight().equals(id))
                .collect(Collectors.toList());
        return list
                .stream()
                .map(x->{
                    if(!x.getId().getLeft().equals(id)){
                        Utilizator utilizator=repoU.findOne(x.getId().getLeft());
                        //System.out.println(utilizator);
                        return new FriendDTO(utilizator.getId(),utilizator.getFirstName(),utilizator.getLastName(),x.getDate());
                    }
                    else{
                        Utilizator utilizator=repoU.findOne(x.getId().getRight());
                        //System.out.println(utilizator);
                        return new FriendDTO(utilizator.getId(),utilizator.getFirstName(),utilizator.getLastName(),x.getDate());
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id user's id
     * @param from starting date
     * @param to ending date
     * @return list of friend dtos of user's friends added between 2 dates
     */
    public List<FriendDTO> getFriendsDate(Long id, LocalDate from, LocalDate to){
        return  StreamSupport
                .stream(repo.findAll().spliterator(),false)
                .filter(pr -> (pr.getId().getRight().equals(id) || pr.getId().getLeft().equals(id) ) &&  pr.getDate().isAfter(from) && pr.getDate().isBefore(to))
                .map(pr-> {
                    if (!pr.getId().getLeft().equals(id)) {
                        Utilizator utilizator = repoU.findOne(pr.getId().getLeft());
                        return new FriendDTO(utilizator.getId(), utilizator.getFirstName(), utilizator.getLastName(), pr.getDate());
                    } else {
                        Utilizator utilizator = repoU.findOne(pr.getId().getRight());
                        return new FriendDTO(utilizator.getId(), utilizator.getFirstName(), utilizator.getLastName(), pr.getDate());
                    }
                })
                .collect(Collectors.toList());
    }
}

//    /**
//     *
//     * @param u starting vertice
//     * @param l list of vertices in the component
//     */
//    private void DFSUtil(Utilizator u, List<Utilizator> l) {
//        // Mark the current node as visited
//        u.makeVisited();
//        // Recur for all the vertices
//        // adjacent to this vertex
//        u.getFriends().forEach(x->{
//            if(!repoU.findOne(x).isVisited()) {
//                l.add(repoU.findOne(x));
//                DFSUtil(repoU.findOne(x), l);
//            }
//        });
//
//    }
//
//    /**
//     *
//     * @return the number of connected components
//     */
//    public int connectedComponents() {
//        // Mark all the vertices as not visited
//       repoU.findAll().forEach(x->x.makeUnvisited());
//        int comps = 0;
//        for (Utilizator u : repoU.findAll()) {
//            if (!u.isVisited()) {
//                List<Utilizator> l = new ArrayList<>();
//                int sz;
//                l.add(u);
//                DFSUtil(u, l);
//                comps++;
//            }
//        }
//        return comps;
//    }
//
//    /**
//     *
//     * @return a list of vertices from the largest community
//     */
//    public List<Utilizator> largestConnectedComp() {
//        // Mark all the vertices as not visited
//        repoU.findAll().forEach(x->x.makeUnvisited());
//        List<Utilizator> maxL = new ArrayList<>();
//        int max = 0;
//        for (Utilizator u : repoU.findAll()) {
//            if (!u.isVisited()) {
//                List<Utilizator> l = new ArrayList<>();
//                l.add(u);
//                DFSUtil(u, l);
//                if (l.size() > max) {
//                    maxL = l;
//                    max = l.size();
//                }
//            }
//        }
//        return maxL;
//    }
