package database;

public interface CrudInterface<T> {
    T findById(int id);

    void insertRecord(T entity);

    void updateRecord(T entity);

    void deleteRecord(int id);

}
