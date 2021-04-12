package socialnetwork.repository.database;

import socialnetwork.domain.Mesaj;
import socialnetwork.domain.ReplyMesaj;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.RepoException;
import socialnetwork.service.validators.Validator;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Database Repository for Messages
 */
public class MesajeDB implements Repository<Long, Mesaj> {
    private final String url;
    private final Validator<Mesaj> validator;
    private final Repository<Long, Utilizator> repoU;

    public MesajeDB(Validator<Mesaj> validator, String url, Repository<Long, Utilizator> repoU) {
        this.url = url;
        this.validator = validator;
        this.repoU = repoU;
    }

    /**
     * Find a message
     * @param aLong id
     * @return
     */
    @Override
    public Mesaj findOne(Long aLong) {
        Mesaj msg = null;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("SELECT FromUser,Text,Reply,Date from Mesaje WHERE ID_Mesaj = " + aLong);
             ResultSet resultSet = statement.executeQuery()) {
            while(resultSet.next()){
                String text = resultSet.getString("Text");
                long reply = resultSet.getLong("Reply");
                LocalDateTime data = LocalDateTime.parse(resultSet.getString("Date"));
                Long from = resultSet.getLong("FromUser");
                List<Long> to = new ArrayList<>();
                PreparedStatement statement2 = connection.prepareStatement("SELECT ID_Utilizator FROM MesajeToUtilizatori WHERE ID_Mesaj = " + aLong);
                ResultSet resultSet2 = statement2.executeQuery();
                while(resultSet2.next()){
                    Long idU = resultSet2.getLong("ID_Utilizator");
                    to.add(idU);
                }
                Utilizator fromUser = repoU.findOne(from);
                List<Utilizator> toUsers = new ArrayList<>();
                to.forEach(id->{
                    toUsers.add(repoU.findOne(id));
                });
                if(reply == 0){
                    msg = new Mesaj(fromUser, toUsers, text);
                }
                else{
                    PreparedStatement statement3 = connection.prepareStatement("SELECT FromUser,Text,Reply,Date from Mesaje WHERE ID_Mesaj = " + reply);
                    ResultSet resultSet3 = statement3.executeQuery();

                    if(resultSet3.next()) {
                        Long id_reply = resultSet3.getLong("ID_Mesaj");
                        String text_reply = resultSet.getString("Text");
                        LocalDateTime data_reply = LocalDateTime.parse(resultSet.getString("Data"));
                        Mesaj replyMsg = new Mesaj(null, null, text_reply);
                        replyMsg.setDate(data_reply);
                        replyMsg.setId(id_reply);
                        msg = new ReplyMesaj(fromUser, toUsers, text, replyMsg);
                    }

                }
                msg.setId(aLong);
                msg.setDate(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * Find all messages
     * @return
     */
    @Override
    public Iterable<Mesaj> findAll() {
        Set<Mesaj> msgs = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("SELECT ID_Mesaj from Mesaje");
             ResultSet resultSet = statement.executeQuery()) {
            while(resultSet.next()){
                Long id = resultSet.getLong("ID_Mesaj");
                Mesaj msg = findOne(id);
                msgs.add(msg);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msgs;
    }

    /**
     * Save a message
     * @param entity
     *         entity must be not null
     * @return
     */
    @Override
    public Mesaj save(Mesaj entity) {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url)){
            if(entity.getClass().getName().contains("ReplyMesaj")) {
                Mesaj reply;
                Field f = ReplyMesaj.class.getDeclaredField("reply");
                f.setAccessible(true);
                reply = (Mesaj) f.get(entity);
                if(! reply.getTo().contains(entity.getFrom()))
                    throw new RepoException("Nu se poate raspunde. Acest mesaj nu era pentru utilizatorul emitator.");
                entity.getTo().forEach(u->{
                    if(!(reply.getTo().contains(u) || reply.getFrom().equals(u)))
                        throw new RepoException("Nu puteti trimite raspunsul unui utilizator care nu era inclus in conversatie. ");
                });

                PreparedStatement statement = connection.prepareStatement("INSERT INTO Mesaje(Text,FromUser,Date, Reply) VALUES (" + "'" + entity.getText() + "'" + "," + entity.getFrom() + "," + "'" + entity.getDate().toString() + "'" + "," + reply.getId() + ")", Statement.RETURN_GENERATED_KEYS );
                statement.execute();
                ResultSet keys = statement.getGeneratedKeys();
                Long key = null;
                if (keys.next()){
                    key = keys.getLong(1);
                }
                entity.setId(key);
                entity.getTo().forEach(u -> {
                    try {
                        PreparedStatement statement2 = connection.prepareStatement("INSERT INTO MesajeToUtilizatori(ID_Mesaj,ID_Utilizator) VALUES (" + entity.getId() + "," + u.getId() + ")");
                        statement2.execute();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            }
            else{
                PreparedStatement statement3 = connection.prepareStatement("INSERT INTO Mesaje(Text,FromUser,Date) VALUES (" + "'" + entity.getText() + "'" + "," + entity.getFrom().getId() + "," + "'" + entity.getDate().toString() + "'" + ")", Statement.RETURN_GENERATED_KEYS );
                statement3.execute();
                ResultSet keys = statement3.getGeneratedKeys();
                Long key = null;
                if (keys.next()){
                    key = keys.getLong(1);
                }
                entity.setId(key);
                entity.getTo().forEach(u -> {
                    try {
                        PreparedStatement statement4 = connection.prepareStatement("INSERT INTO MesajeToUtilizatori(ID_Mesaj,ID_Utilizator) VALUES (" + entity.getId() + "," + u.getId() + ")");
                        statement4.execute();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            }
        } catch (SQLException | NoSuchFieldException | IllegalAccessException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    /**
     * Delete a message
     * @param aLong id
     * @return
     */
    @Override
    public Mesaj delete(Long aLong) {
        Mesaj e = this.findOne(aLong);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM MesajeToUtilizatori WHERE ID_Mesaj = " + aLong);
             PreparedStatement statement2 = connection.prepareStatement("DELETE FROM Mesaje WHERE ID_Mesaj = " + aLong)){
            statement.execute();
            statement2.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return e;
    }

    /**
     * Update a message
     * @param entity
     *          entity must not be null
     * @return
     */
    @Override
    public Mesaj update(Mesaj entity) {
        return null;
    }

    public void deleteMesajToUtilizator(Long msg, Long u){
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM MesajeToUtilizatori WHERE ID_Mesaj = " + msg + " AND ID_Utilizator = " + u)){
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
