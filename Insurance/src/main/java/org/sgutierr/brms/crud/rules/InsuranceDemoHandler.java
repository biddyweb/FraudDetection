package org.sgutierr.brms.crud.rules;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.drools.core.RuleBaseConfiguration;
import org.drools.core.conflict.FifoConflictResolver;
import org.drools.core.conflict.SalienceConflictResolver;
import org.drools.core.io.impl.UrlResource;
import org.drools.core.spi.ConflictResolver;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.rule.LiveQuery;
import org.kie.api.runtime.rule.Row;
import org.kie.api.runtime.rule.ViewChangedEventListener;
import org.kie.internal.KnowledgeBaseFactory;
import org.sgutierr.brms.crud.model.*;
import org.sgutierr.brms.crud.rest.InsuredResource;
import org.sgutierr.brms.crud.services.InsuredService;
import org.sgutierr.brms.crud.util.BRMSUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InsuranceDemoHandler implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5910410083567987549L;
	private Connection conn;
    public KieSession kSession;
    public static KieServices ks;
	public BlackList blackList;
	public WhiteList whiteList;
	public List<Insured> listInsureds= new ArrayList<Insured>();
	
	@Inject
	private BRMSUtil brmsUtil;


	public InsuranceDemoHandler() throws Exception {
		initLists();
			
         if (brmsUtil == null) {
		     brmsUtil = new BRMSUtil();
		}
		
		kSession = brmsUtil.getStatefulSession();

             // Insert facts
		kSession.insert(blackList);
		kSession.insert(whiteList);
	
		ViewChangedEventListener listener = new ViewChangedEventListener() {
			
			public void rowRemoved(Row row) {
				if (row.get("$list").equals(ModifiedAccount.LIST_BLACK)) {
					System.out.println("Remove from blacklist " + row.get("$accountNumber"));
					//removeBlackListAccount((String)row.get("$accountNumber"));
				}
				else if (row.get("$list").equals(ModifiedAccount.LIST_WHITE)) {
					System.out.println("Remove from whitelist " + row.get("$accountNumber"));
					//removeWhiteListAccount((String)row.get("$accountNumber"));
				}
			}

			public void rowAdded(Row row) {
				if (row.get("$list").equals(ModifiedAccount.LIST_BLACK)) {
					System.out.println("Add to blacklist " + row.get("$accountNumber"));
					//addBlackListAccount((String)row.get("$accountNumber"));
				}
				else if (row.get("$list").equals(ModifiedAccount.LIST_WHITE)) {
					System.out.println("Add to whitelist " + row.get("$accountNumber"));
					//addWhiteListAccount((String)row.get("$accountNumber"));
				}
			}

			private void addBlackListAccount(String accountNumber) {
				try {
					Statement stat = conn.createStatement();
					stat.executeUpdate("INSERT INTO BLACK_LIST_ACCOUNTS VALUES('" + accountNumber + "')");
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			private void addWhiteListAccount(String accountNumber) {
				try {
					Statement stat = conn.createStatement();
					stat.executeUpdate("INSERT INTO WHITE_LIST_ACCOUNTS VALUES('" + accountNumber + "')");
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			private void removeBlackListAccount(String accountNumber) {
				try {
					Statement stat = conn.createStatement();
					stat.executeUpdate("DELETE FROM BLACK_LIST_ACCOUNTS WHERE ACCOUNT_NUMBER='" + accountNumber + "'");
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			private void removeWhiteListAccount(String accountNumber) {
				try {
					Statement stat = conn.createStatement();
					stat.executeUpdate("DELETE FROM WHITE_LIST_ACCOUNTS WHERE ACCOUNT_NUMBER='" + accountNumber + "'");
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			public void rowDeleted(Row arg0) {
				// TODO Auto-generated method stub
				
			}

			public void rowInserted(Row arg0) {
				// TODO Auto-generated method stub
				
			}

			public void rowUpdated(Row arg0) {
				// TODO Auto-generated method stub
				
			}

		
		};

		// Open the LiveQuery
		//LiveQuery query = kSession.openLiveQuery("ModifiedAccounts",new Object[] {}, listener);
	}

	public void startProces() throws Exception{
		
	   	
	   	
		Map<String, Object> params = new HashMap<String, Object>();
            
		Insured ins=listInsureds.get(0);
	   	params.put("payment", ins );
    	ProcessInstance processInstance =kSession.startProcess("FraudDetection.paymentProcess", params);
    	
        long parentId = processInstance.getId();
        
    	if ( kSession != null ) {
		
    		saveInsured(ins);
            kSession.dispose();
			
		}
        
	   
		
	}
	private void initLists() throws Exception {
	
	
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/zon";
		conn = DriverManager.getConnection(url,"dashboard", "dashboard");
		conn.setAutoCommit(true);

		Statement stat = conn.createStatement();


		// Fill black list accounts
	    
				ResultSet result = stat.executeQuery("SELECT * FROM Payment ");
				while (result.next()) {
				    Long idR=result.getLong(1);
				    String nameR=result.getString(2);
                    String emailR=result.getString(3);
				    String rulesOutcomeR=result.getString(4);;
                    String insuranceNumberR=result.getString(6);
                    String nifR=result.getString(5);
                    String decisionR=result.getString(7); 
                    double scoreR=result.getDouble(8);
                    BigDecimal totalAmountR=result.getBigDecimal(9);
                    String countryCCR=result.getString(10);
                    String countrOrderR=result.getString(11);
                    Date invoiceDateR=result.getDate(12);
                    
                    Insured newInsured= new Insured();
                    newInsured.setId(idR);
                    newInsured.setName(nameR);
                    newInsured.setEmail(emailR);;
                    newInsured.setRulesOutcome(rulesOutcomeR);
                    newInsured.setInsuranceNumber(insuranceNumberR);;
                    newInsured.setNif(nifR);;
                    newInsured.setDecision(decisionR);
                    newInsured.setScore(scoreR);
                    newInsured.setTotalAmount(totalAmountR);
                    newInsured.setCountryCC(countryCCR);
                    newInsured.setCountryOrder(countrOrderR);
                    newInsured.setInvoiceDate(invoiceDateR);
                    
                    listInsureds.add(newInsured);
 			
				}
				result.close();
		
		
		// Fill black list accounts
		blackList = new BlackList();
	     result = stat.executeQuery("SELECT * FROM BLACK_LIST_ACCOUNTS");
		while (result.next()) {
			blackList.getAccountNumbers().add(result.getString(1));
		}
		result.close();

		

		// Fill white list accounts
		whiteList = new WhiteList();
		result = stat.executeQuery("SELECT * FROM WHITE_LIST_ACCOUNTS");
		while (result.next()) {
			whiteList.getAccountNumbers().add(result.getString(1));
		}
		result.close();

		
	}
	
	public void fillBlackList(){
		
		List <String> accNumbers = new ArrayList<String>();
		accNumbers.add("12345");
		blackList.setAccountNumbers(accNumbers);

	}
	

	public void updateLists(){
		try {
			System.out.println("Update BLACKLIST");
			Statement stat = conn.createStatement();
			if (stat.execute("TRUNCATE TABLE BLACK_LIST_ACCOUNTS")){
				System.out.println("xxxxxxxxxxxxxxxxx");
					
			}
				
			
			ListIterator<String> iter = blackList.getAccountNumbers().listIterator(blackList.getAccountNumbers().size());
			 while (iter.hasPrevious()){
					if (stat.execute("INSERT INTO BLACK_LIST_ACCOUNTS (ACCOUNT_NUMBER) VALUES ('"+iter.previous()+"')")){
						System.out.println("Inserted");
					}
			       
			 }
			
			System.out.println("Update WHITELIST");
			if (stat.execute("TRUNCATE TABLE WHITE_LIST_ACCOUNTS")){
				System.out.println("yyyyyyyyyyyyyyy");
					
			}
				
			
			iter = whiteList.getAccountNumbers().listIterator(whiteList.getAccountNumbers().size());
			 while (iter.hasPrevious()){
					if (stat.execute("INSERT INTO WHITE_LIST_ACCOUNTS (ACCOUNT_NUMBER) VALUES ('"+iter.previous()+"')")){
						System.out.println("Inserted");
					}
			       
			 }
				 
			 
			System.out.println("-----------------------");
			} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void traceBlackList() {
		try {
			System.out.println("BLACKLIST");
			Statement stat = conn.createStatement();
			ResultSet result = stat.executeQuery("SELECT * FROM BLACK_LIST_ACCOUNTS");
			while (result.next()) {
				System.out.println(result.getString(1));
			}
			System.out.println("-----------------------");
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void launchRules() throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
      
		if (kSession != null){
				
	
		if (!listInsureds.isEmpty()){
			
			for (Insured s : listInsureds){
				if (s.getDecision().equals(new String("initial"))){
				kSession.insert(s);
			 	params.put("payment", s );
				kSession.startProcess("FraudDetection.paymentProcess",params);
				}
			}
			

			// Fire rules

			kSession.fireAllRules();
            if (kSession != null){
            	
                for (Insured s : listInsureds){
    				saveInsured(s);
    			}

            	kSession.dispose();
            	
            }
                          
            
		}
		}
		else {
			System.out.println("Invalid KSession");
		}
	}
	
	
	public void destroySession (){
		kSession.destroy();
	}
	
	public void saveInsured(Insured in) throws Exception{
		 String output = null;
		      try{
		 	            String url = "http://localhost:8080/model-1.0/resources/insureds";
		 	            HttpClient client = new HttpClient();
		 	            PostMethod mPost = new PostMethod(url);
		 	            mPost.setRequestEntity(new StringRequestEntity(serializeInsuredJson(in),"application/json", "UTF-8"));

		 	        
		 	            client.executeMethod(mPost);
		 	            output = mPost.getResponseBodyAsString( );
		 	            mPost.releaseConnection( );
		 	            System.out.println("output : " + output);
		 	       
		          }catch(Exception e){

		 	        	throw new Exception("Exception in adding bucket : " + e);
		 	        }
		   
	}
	
	public String serializeInsuredJson (Insured in){

		// Configure GSON
		  // obtained Gson object   
		  Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create(); 
				
		  // called toJson() method and passed student object as parameter  
		  // print generated json to console  
		  return(gson.toJson(in));  		
		
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
