package server.database;

import commons.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestUserRepository implements UserRepository {

    public static final String FIND_ALL = "Find All";
    public static final String FIND_BY_ID = "Find By Id";
    public static final String GET_BY_ID = "Get By Id";
    public static final String EXISTS_BY_ID = "Exists By Id";
    public static final String SAVE = "Save";
    public static final String DELETE_BY_ID = "Delete By Id";
    public static final String FIND_BY_USERNAME = "Find By Username";

    private List<String> calls;
    private List<User> users;

    public TestUserRepository() {
        this.calls = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void call(String method){
        calls.add(method);
    }

    public List<String> getCalls(){
        return calls;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public User findByUsername(String username) {
        calls.add(FIND_BY_USERNAME);
        for(User u: this.users){
            if(u.username.equals(username))
                return u;
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        call(FIND_ALL);
        return users;
    }

    @Override
    public List<User> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<User> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return users.size();
    }

    @Override
    public void deleteById(Integer integer) {
        call(DELETE_BY_ID);
        for (User u : users) {
            if (u.id == integer) {
                users.remove(u);
            }
        }
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends User> S save(S entity) {
        call(SAVE);
        entity.id = this.users.size();
        this.users.add(entity);
        return entity;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<User> findById(Integer integer) {
        calls.add(FIND_BY_ID);
        for(User u: this.users){
            if(u.id == integer)
                return Optional.of(u);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        call(EXISTS_BY_ID);
        for(User u: this.users){
            if(u.id == integer)
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public User getOne(Integer integer) {
        return null;
    }

    @Override
    public User getById(Integer integer) {
        calls.add(GET_BY_ID);
        for(User u: this.users){
            if(u.id == integer)
                return u;
        }
        return null;
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example,
        Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
