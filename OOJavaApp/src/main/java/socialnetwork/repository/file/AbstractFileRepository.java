package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();

    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                List<String> attr = Arrays.asList(linie.split(";"));
                E e = extractEntity(attr);
                super.save(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sau cu lambda - curs 4, sem 4 si 5
//        Path path = Paths.get(fileName);
//        try {
//            List<String> lines = Files.readAllLines(path);
//            lines.forEach(linie -> {
//                E entity=extractEntity(Arrays.asList(linie.split(";")));
//                super.save(entity);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E entity);

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public E save(E entity) {
        E e = super.save(entity);
        writeToFile(entity);
        return e;

    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        writeAllToFile();
        return e;
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public E update(E entity) {
        E e = super.update(entity);
        writeAllToFile();
        return e;
    }

    /**
     *
     * @param entity
     */
    protected void writeToFile(E entity) {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName, true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * writes all the entities from memory to file
     */
    protected void writeAllToFile() {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Map.Entry<ID, E> entry : this.entities.entrySet()) {
                E e = entry.getValue();
                bW.write(createEntityAsString(e));
                bW.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}