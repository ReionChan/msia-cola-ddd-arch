package io.github.reionchan.users.repository;

/**
 * @author Reion
 * @date 2024-11-21
 **/
public interface IRoleRepository {

    String SPLIT = ",";

    boolean allRolesExist(String roleStr);
}
