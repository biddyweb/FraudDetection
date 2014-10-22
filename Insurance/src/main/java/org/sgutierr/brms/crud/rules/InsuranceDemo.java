package org.sgutierr.brms.crud.rules;



import org.sgutierr.brms.crud.model.BlackList;
import org.sgutierr.brms.crud.model.WhiteList;
import org.sgutierr.brms.crud.rules.InsuranceDemoHandler;
import org.sgutierr.brms.servlet.BRMSServlet;


/**
 * This is a sample class to launch a rule.
 */
public class InsuranceDemo {
	
	public static BlackList blackList;
	public WhiteList whiteList;
	
    public static final void main(String[] args) {
        try {
    	
        	        	
        	BRMSServlet servlet= new BRMSServlet();
        	
        	//InsuranceDemoHandler in= new InsuranceDemoHandler();
        	//in.startProces();
        	//in.launchRules();
        	
    		 // KieServices is the factory for all KIE services

            /*
             * InsuredS
             */
            
        	// Approved
        	
        /*
        	
        	Insured Insured1 = new Insured();
            Insured1.setInsuranceNumber("111112341212345678");
            handler.kSession.insert(Insured1);

            // Blacklist
            Insured Insured2 = new Insured();
            Insured2.setInsuranceNumber("999912341212345678");
            handler.kSession.insert(Insured2);

            //Rejected
            Insured Insured3 = new Insured();
            Insured3.setInsuranceNumber("2038");
            handler.kSession.insert(Insured3);

            // Manual
            Insured Insured4 = new Insured();
            Insured4.setInsuranceNumber("2040");
            handler.kSession.insert(Insured4);
            
            // Blacklist NIF
            Insured Insured5 = new Insured();
            Insured5.setNif("12123123J");
            handler.kSession.insert(Insured5);
            
            // Blacklist NIF
            Insured Insured6 = new Insured();
            Insured6.setNif("99123124K");
            handler.kSession.insert(Insured6);
            
            // Fire rules
            handler.kSession.fireAllRules();
            
            // Print Output
            System.out.println("Result 1 (Blacklist): "+Insured1);
            System.out.println("Result 2 (Whitelist): "+Insured2);
            System.out.println("Result 3 (Rejected rule): "+Insured3);
            System.out.println("Result 4 (Manual): "+Insured4);
            System.out.println("Result 5 (Blacklist NIF): "+Insured5);
            System.out.println("Result 6 (Whitelist NIF): "+Insured6);
            
            // Modify facts
            handler.blackList.getAccountNumbers().add("9999");
            handler.whiteList.getAccountNumbers().add("5555");

        */
            /*
             * InsuredS
             */

            //Blacklist
       
        /*	Insured Insured01 = new Insured();
            Insured01.setInsuranceNumber("9999");
            handler.kSession.insert(Insured01);

            // Whitelist
            Insured Insured02 = new Insured();
            Insured02.setInsuranceNumber("5555");
            handler.kSession.insert(Insured02);

            
            // Whitelist
            Insured Insured03 = new Insured();
            Insured03.setInsuranceNumber("8888 111");
            handler.kSession.insert(Insured03);
            
            // Conflicts
            Insured Insured04 = new Insured();
            Insured04.setInsuranceNumber("2222222222");
            handler.kSession.insert(Insured04);
            
            // Conflicts
            Insured Insured05 = new Insured();
            Insured05.setInsuranceNumber("4545");
            Insured05.setNif("1R");
            handler.kSession.insert(Insured05);

            System.out.println("Blacklist size: "+handler.blackList.getAccountNumbers().size() );
            
            // Fire rules
            handler.kSession.fireAllRules();
            
            // Print Output
            System.out.println("Result 01 (Blacklist): "+Insured01);
            System.out.println("Result 02 (Whitelist): "+Insured02);
           
            System.out.println("Result 03 (blacklisted): "+Insured03);
            System.out.println("Blacklist size: "+handler.blackList.getAccountNumbers().size() );
            
            System.out.println("Result 04 (conflict): "+Insured04);

            System.out.println("Result 05 (conflict resolver): "+Insured05);
    	 
    	 */	
           // logger.close();
         //   handler.close();
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

