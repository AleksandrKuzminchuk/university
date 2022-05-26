package ua.foxminded.task10.uml.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudRepositoryDao<T, ID extends Serializable> {
    Optional<T> save(T entity);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    Long count();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll();
}
