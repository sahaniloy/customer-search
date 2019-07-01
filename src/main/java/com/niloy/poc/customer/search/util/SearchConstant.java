package com.niloy.poc.customer.search.util;

/**
 * @author Niloy Saha<niloysaha@gmail.com>
 */
public class SearchConstant {
    private static SearchConstant instance = new SearchConstant();

    private SearchConstant() {};

    public static SearchConstant getInstance() {
        return instance;
    }

    //Follow the order to create the search query
    //number is needed to maintain the search order, DB field based on your query, operator e.g. LIKE/EQUAL/IN, datatype e.g. STRING/INT/LONG
    public static final String FNAME = "1,UPPER(customer.fname),LIKE,STRING";
    public static final String MNAME = "2,UPPER(customer.mname),LIKE,STRING";
    public static final String LNAME = "3,UPPER(customer.lname),LIKE,STRING";
    public static final String EMAIL = "4,customer.email,EQUAL,STRING";
    public static final String SSN = "5,customer.SSN,EQUAL,STRING";
    public static final String PHONE = "6,customer.phone,EQUAL,LONG";

    public static final String ADD = " AND ";
}
