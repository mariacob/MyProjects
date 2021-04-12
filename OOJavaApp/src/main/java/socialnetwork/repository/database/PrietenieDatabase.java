package socialnetwork.repository.database;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.Validator;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Friendship database
 */
public class PrietenieDatabase implements Repository<Tuple<Long,Long>, Prietenie> {
    private final String url;
    private final Validator<Prietenie> validator;

    public PrietenieDatabase(Validator<Prietenie> validator, String url) {
        this.validator = validator;
        this.url = url;
    }

    /**
     * Find all friendships
     * @return
     */
    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> fr = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Prietenii");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("ID_Utilizator1");
                Long id2 = resultSet.getLong("ID_Utilizator2");
                Date data = resultSet.getDate("Date");

                Prietenie pr = new Prietenie();
                pr.setId(new Tuple<>(id1,id2));
                pr.setDate(data.toLocalDate());
                fr.add(pr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fr;
    }


    /**
     * Save a friendship
     * @param entity
     *         entity must be not null
     * @return
     */
    @Override
    public Prietenie save(Prietenie entity) {
        validator.validate(entity);
        int id1 = entity.getId().getLeft().intValue();
        int id2 = entity.getId().getRight().intValue();
        LocalDate data = entity.getDate();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Prietenii(ID_Utilizator1, ID_Utilizator2, Date) VALUES ( " + id1 + "," + id2  + "," + "'" + data.toString() + "'" + ")")) {
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    /**
     * Delete friendship
     * @param longLongTuple id
     * @return
     */
    @Override
    public Prietenie delete(Tuple<Long, Long> longLongTuple) {
        int id1 = longLongTuple.getLeft().intValue();
        int id2 = longLongTuple.getRight().intValue();
        Prietenie pr = this.findOne(longLongTuple);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Prietenii WHERE ID_Utilizator1 = " +  id1 + "AND ID_Utilizator2 = " + id2)) {
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pr;
    }

    /**
     * Update a friendship
     * @param prietenie
     * @return
     */
    @Override
    public Prietenie update(Prietenie prietenie) {
        return prietenie;
    }

    /**
     * Find a friendship
     * @param longLongTuple
     * @return
     */
    @Override
    public Prietenie findOne(Tuple<Long, Long> longLongTuple) {
        Prietenie pr = new Prietenie();
        pr.setId(longLongTuple);
        int id1 = longLongTuple.getLeft().intValue();
        int id2 = longLongTuple.getRight().intValue();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("SELECT Date from Prietenii WHERE ID_Utilizator1 = " +  id1 + "AND ID_Utilizator2 = " + id2);
             ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            Date data = resultSet.getDate("Date");
            pr.setDate(data.toLocalDate());
            pr.setId(longLongTuple);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pr;
    }

}