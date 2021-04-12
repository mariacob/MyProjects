package socialnetwork.service;

import socialnetwork.domain.Mesaj;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.ReplyMesaj;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceMesaje extends AbstractService<Long, Mesaj>{
    Repository<Long, Utilizator> repoU;

    public ServiceMesaje(Repository<Long, Mesaj> repo, Repository<Long, Utilizator> repoU) {
        super(repo);
        this.repoU = repoU;
    }

    /**
     *
     * @param from sender
     * @param to receiver
     * @param text message text
     * @return saved message
     */
    public Mesaj addMesaj(Long from, List<Long> to, String text){
        Utilizator u1 = repoU.findOne(from);
        List<Utilizator> u2 = new ArrayList<>();
        to.forEach(id->{
            u2.add(repoU.findOne(id));
        });
        Mesaj msg = new Mesaj(u1, u2, text);
        msg.setDate(LocalDateTime.now());
        repo.save(msg);
        return msg;
    }

    public Mesaj addReplyMesaj(Long from, List<Long> to, String text, Mesaj reply){
        Utilizator u1 = repoU.findOne(from);
        List<Utilizator> u2 = new ArrayList<>();
        to.forEach(id->{
            u2.add(repoU.findOne(id));
        });
        Mesaj msg = new ReplyMesaj(u1, u2, text,repo.findOne(reply.getId()));
        msg.setDate(LocalDateTime.now());
        repo.save(msg);
        return msg;
    }

    /**
     *
     * @param u1 id of sender
     * @param u2 id of receiver
     * @return list of sorted messages between u1 and u2
     */
    public List<Mesaj> getSortedMesaje(Long u1, Long u2){
        return StreamSupport.stream(repo.findAll().spliterator(),false)
                .filter(msg->(msg.getFrom().equals(repoU.findOne(u1)) && msg.getTo().contains(repoU.findOne(u2))) || (msg.getFrom().equals(repoU.findOne(u2)) && msg.getTo().contains(repoU.findOne(u1))) || (msg.getTo().contains(repoU.findOne(u1)) && msg.getTo().contains(repoU.findOne(u2))))
                .sorted((msg1,msg2)->{
                    if(msg1.getDate().equals(msg2.getDate())){
                        return msg1.getId().compareTo(msg2.getId());
                    }
                    return msg1.getDate().compareTo(msg2.getDate());
                })
                .collect(Collectors.toList());
    }

    public void removeMessagesOfUser(Long id){
        List<Mesaj> l = StreamSupport.stream(repo.findAll().spliterator(),false).collect(Collectors.toList());
        l.forEach(x->{
            if(x.getFrom().equals(id) || (x.getTo().size()==1 && x.getTo().contains(id))) {
                repo.delete(x.getId());
            }
            else if(x.getTo().contains(id)){
                try {
                    Method m = this.repo.getClass().getMethod("deleteMesajToUtilizator",Long.class,Long.class);
                    m.invoke(this.repo,x.getId(),id);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *
     * @param id user id
     * @param from starting date
     * @param to ending date
     * @return list of messages received by user betwwen 2 dates
     */
    public List<Mesaj> getMesajeDate(Long id, LocalDate from, LocalDate to){
        return StreamSupport
                .stream(repo.findAll().spliterator(),false)
                .filter(msg->msg.getTo().contains(repoU.findOne(id)) && msg.getDate().toLocalDate().isAfter(from) && msg.getDate().toLocalDate().isBefore(to))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param u1 first user's id
     * @param u2 second user's id
     * @param from starting date
     * @param to ending date
     * @return list of messages sent between 2 dates between 2 users
     */
    public List<Mesaj> getConversatieDate(Long u1, Long u2, LocalDate from, LocalDate to){
        return StreamSupport.stream(repo.findAll().spliterator(),false)
                .filter(msg->(msg.getFrom().equals(repoU.findOne(u2)) && msg.getTo().contains(repoU.findOne(u1))))
                .filter(msg -> msg.getDate().toLocalDate().isAfter(from) && msg.getDate().toLocalDate().isBefore(to))
                .collect(Collectors.toList());
    }

}
