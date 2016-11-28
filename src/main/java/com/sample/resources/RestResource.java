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

  /**
   * Get the lazy loading collection by calling size()
   * 
   * @return
   */
  @GET
  @Path("get-user-projects")
  public List<Project> getUserProjects() {
    Hibernate.initialize(userDao.getByUserId(Integer.valueOf(1)).getProjects());
    // or call userDao.getByUserId(Integer.valueOf(1)).getProjects().size();
    return userDao.getByUserId(Integer.valueOf(1)).getProjects();
  }

  @GET
  @Path("get-user-projects-query-cache")
  public List<Project> getUserProjectsByNamedQuery() {
    User user = userDao.getProjectsByUserId(Integer.valueOf(1)).get(0);
    user.getProjects().size();
    return user.getProjects();
  }
}
