package store.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    
    void remove(T entity);

    int getSize();

    Optional<List<T>> findAll();

    void update(String name, T newData);

    boolean exists(String name);

    void clear();

}
