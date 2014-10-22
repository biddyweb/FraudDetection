package org.sgutierr.brms.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sgutierr.brms.crud.rules.InsuranceDemoHandler;

@WebServlet( urlPatterns="/brmsGUI",
		    loadOnStartup = 1,
	        asyncSupported = true		
		)
public class BRMSServlet extends HttpServlet {

   private InsuranceDemoHandler handler;
   
   public void init(ServletConfig config) {
	        System.out.println("My servlet has been initialized");
	    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
		  try {
			  System.out.println("Servlet executing");
			  
			    if (handler == null){		    	
				    handler = new InsuranceDemoHandler();
	    	        handler.launchRules();		
	    	        }
			    else{
			    	handler.destroySession();
			    	handler = new InsuranceDemoHandler();
			    	handler.launchRules();
			    	System.out.println("Servlet executing second time");
			    }
			    handler.close();
			    
         
	        } catch (Throwable t) {
	            t.printStackTrace();
	        }
		
    }
	
	
}
