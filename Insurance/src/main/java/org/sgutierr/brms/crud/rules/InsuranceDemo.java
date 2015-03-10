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

            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

