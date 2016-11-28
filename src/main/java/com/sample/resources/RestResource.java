package com.sample.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sample.entity.Project;
import com.sample.entity.User;
import com.sample.repository.UserDao;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("/api")
@Transactional(readOnly = true)
public class RestResource {

  @Autowired
  private UserDao userDao;

  @GET
  @Path("get-user")
  public String getUser() {
    return userDao.getByUserId(Integer.valueOf(1)).getName();
  }

  //////////////////////////////////////////////////////////////////////
  // get projects for a single user

  /**
   * Get the lazy loading collection by calling size()
   * 
   * @return
   */
  @GET
  @Path("get-user-projects")
  public List<Project> getUserProjects() {
    User user = userDao.getByUserId(Integer.valueOf(1));
    Hibernate.initialize(user.getProjects());
    // or call userDao.getByUserId(Integer.valueOf(1)).getProjects().size();
    return user.getProjects();
  }

  @GET
  @Path("get-user-projects-query-cache")
  public List<Project> getUserProjectsByNamedQuery() {
    User user = userDao.getProjectsByUserId(Integer.valueOf(1)).get(0);
    user.getProjects().size();
    return user.getProjects();
  }

  //////////////////////////////////////////////////////////////////////
  // get projects for multiple users

  /**
   * Best solution: one join fetch query to load users and their projects with query cache enabled,
   * so only one query can happen when call the endpoints multiple times
   * 
   * @return
   */
  @GET
  @Path("get-users-by-type")
  public List<User> getUsersByType() {
    List<User> users = userDao.getProjectsByUserType("x");
    users.forEach(user -> Hibernate.initialize(user.getProjects()));
    return users;
  }

  /**
   * N+1 performance issue. You see one query for users and N queries for queries by user Due to
   * cache, N+1 round-trip to database only happen once. After that, data loads from cache.
   * 
   * @return
   */
  @GET
  @Path("get-users-by-type-no-join-fetch")
  public List<User> getUserByTypeNoJoinFetch() {
    List<User> users = userDao.getUsersByType("x");
    users.forEach(user -> Hibernate.initialize(user.getProjects()));
    return users;
  }
}
