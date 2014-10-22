package org.sgutierr.brms.crud.rest;

import org.sgutierr.brms.crud.model.*;
import org.sgutierr.brms.crud.pagination.PaginatedListWrapper;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST Service to expose the data to display in the UI grid.
 *
 *  */
@Stateless
@ApplicationPath("/resources")
@Path("insureds")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InsuredResource extends Application {
    @PersistenceContext
    private EntityManager entityManager;

    private Integer countInsureds() {
        Query query = entityManager.createQuery("SELECT COUNT(p.id) FROM Insured p");
        return ((Long) query.getSingleResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    private List<Insured> findInsureds(int startPosition, int maxResults, String sortFields, String sortDirections) {
        Query query = entityManager.createQuery("SELECT p FROM Insured p ORDER BY " + sortFields + " " + sortDirections);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    private PaginatedListWrapper<Insured> findInsureds(PaginatedListWrapper<Insured> wrapper) {
        wrapper.setTotalResults(countInsureds());
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(findInsureds(start,
                                    wrapper.getPageSize(),
                                    wrapper.getSortFields(),
                                    wrapper.getSortDirections()));
        return wrapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PaginatedListWrapper<Insured> listPersons(@DefaultValue("1")
                                                    @QueryParam("page")
                                                    Integer page,
                                                    @DefaultValue("id")
                                                    @QueryParam("sortFields")
                                                    String sortFields,
                                                    @DefaultValue("asc")
                                                    @QueryParam("sortDirections")
                                                    String sortDirections) {
        PaginatedListWrapper<Insured> paginatedListWrapper = new PaginatedListWrapper<>();
        paginatedListWrapper.setCurrentPage(page);
        paginatedListWrapper.setSortFields(sortFields);
        paginatedListWrapper.setSortDirections(sortDirections);
        paginatedListWrapper.setPageSize(10);
        return findInsureds(paginatedListWrapper);
    }

    @GET
    @Path("{id}")
    public Insured getInsured( @PathParam("id") Long id) {
        return entityManager.find(Insured.class, id);
    }

    @POST
    public Insured saveInsured(Insured insured) {
        if (insured.getId() == null) {
        	Insured personToSave = new Insured();
            personToSave.setName(insured.getName());
            personToSave.setInsuranceNumber(insured.getInsuranceNumber());
            personToSave.setNif(insured.getNif());
            personToSave.setRulesOutcome(insured.getRulesOutcome());
            personToSave.setEmail(insured.getEmail());
            personToSave.setDecision("initial");
            personToSave.setScore(insured.getScore());
            personToSave.setTotalAmount(insured.getTotalAmount());
            personToSave.setCountryCC(insured.getCountryCC());
            personToSave.setCountryOrder(insured.getCountryOrder());
            personToSave.setInvoiceDate(insured.getInvoiceDate());
            entityManager.persist(insured);
        } else {
        	Insured personToUpdate = getInsured(insured.getId());
            personToUpdate.setName(insured.getName());
            personToUpdate.setInsuranceNumber(insured.getInsuranceNumber());
            personToUpdate.setNif(insured.getNif());
            personToUpdate.setRulesOutcome(insured.getRulesOutcome());
            personToUpdate.setEmail(insured.getEmail());
            personToUpdate.setDecision(insured.getDecision());
            personToUpdate.setScore(insured.getScore());
            personToUpdate.setTotalAmount(insured.getTotalAmount());
            personToUpdate.setCountryCC(insured.getCountryCC());
            personToUpdate.setCountryOrder(insured.getCountryOrder());
            personToUpdate.setInvoiceDate(insured.getInvoiceDate());
            
            insured = entityManager.merge(personToUpdate);
        }

        return insured;
    }

    @DELETE
    @Path("{id}")
    public void deleteInsured(@PathParam("id") Long id) {
        entityManager.remove(getInsured(id));
    }
}
