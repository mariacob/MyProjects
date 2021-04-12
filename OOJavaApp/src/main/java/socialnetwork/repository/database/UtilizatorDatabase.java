package socialnetwork.repository.database;

import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User database repository
 */
public class UtilizatorDatabase implements Repository<Long, Utilizator> {
    private final String url;
    private final Validator<Utilizator> validator;

    public UtilizatorDatabase(Validator<Utilizator> validator, String url) {
        this.validator = validator;
        this.url = url;
    }


    /**
     * find all users
     * @return
     */
    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Utilizatori");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID_Utilizator");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");

                Utilizator utilizator = new Utilizator(firstName, lastName);
                utilizator.setId(id);
                users.add(utilizator);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * save a user
     * @param entity
     *         entity must be not null
     * @return
     */
    @Override
    public Utilizator save(Utilizator entity) {
        validator.validate(entity);
        String fN = entity.getFirstName();
        String lN = entity.getLastName();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Utilizatori(FirstName, LastName) VALUES (" + "'" + fN + "'" + "," + "'" + lN + "'" + ")")){
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    /**
     * deletes a user
     * @param aLong
     * @return
     */
    @Override
    public Utilizator delete(Long aLong) {
        Utilizator e = this.findOne(aLong);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Utilizatori WHERE ID_Utilizator = " + aLong)) {
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return e;
    }

    /**
     * update a user
     * @param entity
     *          entity must not be null
     * @return
     */
    public Utilizator update(Utilizator entity) {
        validator.validate(entity);
        int ID = entity.getId().intValue();
        String fN = entity.getFirstName();
        String lN = entity.getLastName();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("UPDATE Utilizatori SET FirstName =" +  "'" +  fN + "'" +", LastName = " + "'" + lN + "'" +" WHERE ID_Utilizator = " + ID)) {
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    /**
     * find a user
     * @param aLong
     * @return
     */
    @Override
    public Utilizator findOne(Long aLong) {
        Utilizator u = null;
        int ID = aLong.intValue();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement("SELECT FirstName,LastName from Utilizatori  WHERE ID_Utilizator = " + ID);
             ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");

                u = new Utilizator(firstName, lastName);
                u.setId(aLong);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }
}