package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceUtilizatori extends AbstractService<Long,Utilizator>{


    Repository<Tuple<Long,Long>, Prietenie> repoP;

    public ServiceUtilizatori(Repository<Long, Utilizator> repo, Repository<Tuple<Long,Long>, Prietenie> repoP) {
        super(repo);
        this.repoP = repoP;
    }

    /**
     *
     * @param firstName user's first name
     * @param lastName user's last name
     * @return the added user
     */
    public Utilizator addUtilizator(String firstName, String lastName) {
        Utilizator u = new Utilizator(firstName,lastName);
        return repo.save(u);
    }

    /**
     *
     * @param ID user's id
     * @param firstName user's first name
     * @param lastName user's last name
     * @return the updated user
     */
    public Utilizator updateUser(Long ID, String firstName, String lastName){
        Utilizator u = new Utilizator(firstName,lastName);
        u.setId(ID);
        repo.update(u);
        return u;
    }

    /**
     *
     * @param id user's id
     * @return a list of user's friends and the date of the friendship
     */
    public List<Tuple<Long, LocalDate>> getFriendsAndDate(Long id){
        return StreamSupport
                .stream(repoP.findAll().spliterator(),false)
                .filter(x->(x.getId().getLeft().equals(id)) || x.getId().getRight().equals(id))
                .map(x->{
                    if(x.getId().getLeft().equals(id))
                        return new Tuple<>(x.getId().getRight(), x.getDate());
                    else
                        return new Tuple<>(x.getId().getLeft(), x.getDate());
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id user's id
     * @param m month
     * @return list of user's friends made in a month
     */
    public List<Tuple<Long, LocalDate>> getFriendsByMonth(Long id, Month m) {
        return StreamSupport
                .stream(repoP.findAll().spliterator(),false)
                .filter(x->(((x.getId().getLeft().equals(id)) || x.getId().getRight().equals(id)) && x.getDate().getMonth().equals(m)))
                .map(x->{
                    if(x.getId().getLeft().equals(id))
                        return new Tuple<>(x.getId().getRight(), x.getDate());
                    else
                        return new Tuple<>(x.getId().getLeft(), x.getDate());
                })
                .collect(Collectors.toList());
    }

}
