package ua.foxminded.task10.uml.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudRepositoryService<T, ID extends Serializable> {
    T save(T entity);
    T findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    Long count();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll();
}
