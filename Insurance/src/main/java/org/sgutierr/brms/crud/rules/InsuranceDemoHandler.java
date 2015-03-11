package org.sgutierr.brms.crud.rules;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.kie.api.runtime.KieSession;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.kie.api.runtime.process.ProcessInstance;

import org.sgutierr.brms.crud.model.*;
import org.sgutierr.brms.crud.util.BRMSUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InsuranceDemoHandler implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5910410083567987549L;
    private Connection conn;
    public KieSession kSession;
    public BlackList blackList;
    public WhiteList whiteList;
    public List<Insured> listInsureds = new ArrayList<Insured>();
    private static final String demoDataSource = "java:jboss/datasources/DVDemoPostgres";
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

    }

    public void startProces() throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        Insured ins = listInsureds.get(0);
        params.put("payment", ins);
        ProcessInstance processInstance = kSession.startProcess("FraudDetection.paymentProcess", params);

        long parentId = processInstance.getId();

        if (kSession != null) {
            saveInsured(ins);
            kSession.dispose();
        }

    }

    private void initLists() throws Exception {

        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(demoDataSource);
        conn = ds.getConnection();
        conn.setAutoCommit(true);

        Statement stat = conn.createStatement();

        // Fill black list accounts

        ResultSet result = stat.executeQuery("SELECT * FROM Payment ");
        while (result.next()) {
            Long idR = result.getLong(1);
            String nameR = result.getString(2);
            String emailR = result.getString(3);
            String rulesOutcomeR = result.getString(4);
            String insuranceNumberR = result.getString(6);
            String nifR = result.getString(5);
            String decisionR = result.getString(7);
            double scoreR = result.getDouble(8);
            BigDecimal totalAmountR = result.getBigDecimal(9);
            String countryCCR = result.getString(10);
            String countrOrderR = result.getString(11);
            Date invoiceDateR = result.getDate(12);

            Insured newInsured = new Insured();
            newInsured.setId(idR);
            newInsured.setName(nameR);
            newInsured.setEmail(emailR);
            newInsured.setRulesOutcome(rulesOutcomeR);
            newInsured.setInsuranceNumber(insuranceNumberR);
            newInsured.setNif(nifR);
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

    public void fillBlackList() {

        List<String> accNumbers = new ArrayList<String>();
        accNumbers.add("12345");
        blackList.setAccountNumbers(accNumbers);

    }


    public void updateLists() {

        try {
            System.out.println("Update BLACKLIST");
            Statement stat = conn.createStatement();
            if (stat.execute("TRUNCATE TABLE BLACK_LIST_ACCOUNTS")) {
                System.out.println("xxxxxxxxxxxxxxxxx");

            }

            ListIterator<String> iter = blackList.getAccountNumbers().listIterator(blackList.getAccountNumbers().size());
            while (iter.hasPrevious()) {
                if (stat.execute("INSERT INTO BLACK_LIST_ACCOUNTS (ACCOUNT_NUMBER) VALUES ('" + iter.previous() + "')")) {
                    System.out.println("Inserted");
                }

            }

            System.out.println("Update WHITELIST");
            if (stat.execute("TRUNCATE TABLE WHITE_LIST_ACCOUNTS")) {
                System.out.println("yyyyyyyyyyyyyyy");

            }

            iter = whiteList.getAccountNumbers().listIterator(whiteList.getAccountNumbers().size());
            while (iter.hasPrevious()) {
                if (stat.execute("INSERT INTO WHITE_LIST_ACCOUNTS (ACCOUNT_NUMBER) VALUES ('" + iter.previous() + "')")) {
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

    public void launchRules() throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        if (kSession != null) {

            if (!listInsureds.isEmpty()) {

                for (Insured s : listInsureds) {
                    if (s.getDecision().equals("initial")) {
                        kSession.insert(s);
                        params.put("payment", s);
                        kSession.startProcess("FraudDetection.paymentProcess", params);
                    }
                }

                // Fire rules
                kSession.fireAllRules();
                if (kSession != null) {

                    for (Insured s : listInsureds) {
                        saveInsured(s);
                    }

                    kSession.dispose();
                }
            }
        } else {
            System.out.println("Invalid KSession");
        }
    }

    public void destroySession() {

        kSession.destroy();
    }

    public void saveInsured(Insured in) throws Exception {
        String output;
        try {
            String url = "http://localhost:8080/fraud-detection-1.0/resources/insureds";
            HttpClient client = new HttpClient();

            PostMethod mPost = new PostMethod(url);
            mPost.setRequestEntity(new StringRequestEntity(serializeInsuredJson(in), "application/json", "UTF-8"));

            client.executeMethod(mPost);
            output = mPost.getResponseBodyAsString();
            mPost.releaseConnection();
            System.out.println("output : " + output);

        } catch (Exception e) {

            throw new Exception("Exception in adding bucket : " + e);
        }

    }

    public String serializeInsuredJson(Insured in) {

        // Configure GSON
        // obtained Gson object
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        // called toJson() method and passed student object as parameter
        // print generated json to console
        return (gson.toJson(in));

    }

    public void close() {

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
