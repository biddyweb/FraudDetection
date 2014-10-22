package org.sgutierr.brms.crud.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(name="payment")
public class Insured implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 15)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;

    @NotNull
    @NotEmpty
    @Email
    private String email;


    @Size(min = 3, max = 100)
    @Column(name = "rules_outcome")
    private String rulesOutcome;

    @NotNull
    @NotEmpty  
    @Column(name = "insurance_number")
    private String insuranceNumber;
	
    @NotNull
    @NotEmpty
    @Column(name = "currency")
    private String nif;
	
 
    private String decision = "initial";
	
    public Double score;
    
    @NotNull
    @Column(name = "total_amount")
    private BigDecimal totalAmount; 
    
    @NotNull
    @Size(min = 2, max = 25)
    @Column(name = "country_cc")
    private String countryCC;
    
    @NotNull
    @Size(min = 2, max = 25)
    @Column(name = "country_order")
    private String countryOrder;
    
    // ensures that the value of invoice must be in the past.
    @Past
    @Column(name ="invoice_date")
    private Date invoiceDate;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRulesOutcome() {
        return rulesOutcome;
    }

    public void setRulesOutcome(String rulesOutcome) {
        this.rulesOutcome = rulesOutcome;
    }
    
    /**
	 * 0: initial
	 * 1: approved
	 * 2: rejected
	 * 3: manual
	 * */
	final static String accountDecisions[]= {"initial", "approved", "rejected", "manual"};

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String number) {
		this.insuranceNumber = number;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCountryCC() {
		return countryCC;
	}

	public void setCountryCC(String countryCC) {
		this.countryCC = countryCC;
	}

	public String getCountryOrder() {
		return countryOrder;
	}

	public void setCountryOrder(String countryOrder) {
		this.countryOrder = countryOrder;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date orderDate) {
		this.invoiceDate = orderDate;
	}

    public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

		
	@Override
	public String toString() {
		return "Insured [insuranceNumber=" + insuranceNumber + ", nif=" + nif + ", decision="
				+ decision + "]";
	}
}
