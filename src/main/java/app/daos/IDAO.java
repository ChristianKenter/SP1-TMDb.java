package app.daos;

import java.util.List;

public interface IDAO<T, ID> {
    T create(T entity);
    T read(ID id);
    List<T> readAll();
    T update(T entity);
    void delete(ID id);
}