package com.sample.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.entity.User;

@Repository
@Transactional
public class UserDao {

  @Autowired
  private SessionFactory _sessionFactory;

  private Session getSession() {
    return _sessionFactory.getCurrentSession();
  }

  public User getByUserId(Integer id) {
    return (User) getSession().load(User.class, id);
  }

  @SuppressWarnings("unchecked")
  public List<User> getUsersByType(String type) {
    return getSession().getNamedQuery(User.FETCH_USERS_TYPE).setParameter("type", type)
        .setCacheable(true).list();
  }

  //////////////////////////////////////////////////////////////////////
  // join fetch to load user with projects

  public User getUserProjectsById(Integer id) {
    return (User) getSession().getNamedQuery(User.FETCH_USER_PROJECTS).setParameter("userId", id)
        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setCacheable(true).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<User> getUsersProjectsByType(String type) {
    return getSession().getNamedQuery(User.FETCH_USERS_PROJECTS_BY_TYPE).setParameter("type", type)
        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setCacheable(true).list();
  }

}
