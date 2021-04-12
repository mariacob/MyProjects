package socialnetwork.repository.database;

import socialnetwork.domain.CererePrietenie;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.Validator;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Database repository for the friend request
 */
public class CererePrietenieDB implements Repository<Tuple<Long,Long>, CererePrietenie> {
    private final String url;
    private final Validator<CererePrietenie> validator;

    public CererePrietenieDB(Validator<CererePrietenie> validator,String url) {
        this.url = url;
        this.validator = validator;
    }

    /**
     * Find a friend request by id
     * @param longLongTuple
     * @return
     */
    @Override
    public CererePrietenie findOne(Tuple<Long, Long> longLongTuple) {
        CererePrietenie c = new CererePrietenie(longLongTuple.getLeft(),longLongTuple.getRight());
        c.setId(longLongTuple);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("SELECT Status,Date from CereriPrietenii WHERE FromUser = " + longLongTuple.getLeft()  + " AND ToUser = " + longLongTuple.getRight());
             ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            String status = resultSet.getString("Status");
            LocalDate date = resultSet.getDate("Date").toLocalDate();
            c.setStatus(status);
            c.setDate(date);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * Get all the friend requests
     * @return
     */
    @Override
    public Iterable<CererePrietenie> findAll() {
        Set<CererePrietenie> cereri = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("SELECT * from CereriPrietenii");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long from = resultSet.getLong("FromUser");
                Long to = resultSet.getLong("ToUser");
                String status = resultSet.getString("Status");
                LocalDate date = resultSet.getDate("Date").toLocalDate();

               CererePrietenie c = new CererePrietenie(from,to);
               c.setId(new Tuple<>(from,to));
               c.setStatus(status);
               c.setDate(date);
               cereri.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cereri;
    }

    /**
     * Saves the friend request in the database
     * @param entity
     *         entity must be not null
     * @return the saved friend request
     */
    @Override
    public CererePrietenie save(CererePrietenie entity) {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO CereriPrietenii(FromUser, ToUser, Status, Date) VALUES ( " + entity.getFrom() + "," + entity.getTo()  + "," + "'" + entity.getStatus() + "'" + "," + "'" + entity.getDate() + "'" + ")")) {
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    /**
     * deletes a friend request by id
     * @param longLongTuple
     * @return the deleted friend request
     */
    @Override
    public CererePrietenie delete(Tuple<Long, Long> longLongTuple) {
        CererePrietenie c = this.findOne(longLongTuple);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM CereriPrietenii WHERE FromUser = " +  c.getFrom() + " AND ToUser = " + c.getTo())) {
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return c;
    }

    /**
     * Updates a friend request
     * @param entity
     *          entity must not be null
     * @return the updated request
     */
    @Override
    public CererePrietenie update(CererePrietenie entity) {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("UPDATE CereriPrietenii SET Status =" +  "'" +  entity.getStatus() + "'" + "WHERE FromUser = " +  entity.getFrom() + " AND ToUser = " + entity.getTo())) {
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }
}
