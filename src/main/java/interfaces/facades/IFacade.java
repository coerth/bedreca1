package interfaces.facades;

import java.util.List;

public interface IFacade <T>
{
    T getById(Integer id);
    List<T> getAll();
    T create(T t);

    T update(T t);

    T delete(Integer id);


}
