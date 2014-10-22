package org.sgutierr.brms.crud.services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sgutierr.brms.crud.model.Insured;

public class InsuredService {
  protected EntityManager em;

  public InsuredService(EntityManager em) {
    this.em = em;
  }
  
  public Collection<Insured> findAllInsured() {
    Query query = em.createQuery("SELECT e FROM Insured e");
    return (Collection<Insured>) query.getResultList();
  }
}