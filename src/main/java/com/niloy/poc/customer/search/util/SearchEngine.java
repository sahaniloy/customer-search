package com.niloy.poc.customer.search.util;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Niloy Saha<niloysaha@gmail.com>
 */
public class SearchEngine<T> {
    public String generateQuery(T object) throws Exception {

        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        Set<String> set = new TreeSet<>((obj1, obj2) -> {
            String[] arry1 = obj1.split(",");
            String[] arry2 = obj2.split(",");

            int sequence1 = Integer.parseInt(arry1[0]);
            int sequence2 = Integer.parseInt(arry2[0]);

            if (sequence1 > sequence2)
                return 1;
            else if (sequence1 < sequence2)
                return -1;
            else
                return 0;
        });

        for (Method m : methods) {
            if (m.getName().startsWith("get")) {
                String fieldName = m.getName().substring(3, m.getName().length()).toUpperCase();
                String fieldValueFrmTO = (String) m.invoke(object);

                if (StringUtils.isNotBlank(fieldValueFrmTO)) {
                    Field field = SearchConstant.class.getField(fieldName);
                    String fieldValueFrmConstFile = (String) field.get(SearchConstant.getInstance());

                    set.add(fieldValueFrmConstFile + "," + fieldValueFrmTO);
                }
            }
        }

        // Prepare the query
        StringBuilder queryBuilder = new StringBuilder();
        set.stream().forEach(field -> queryBuilder.append(converter(field) + SearchConstant.ADD));

        String query = queryBuilder.toString();

        if (StringUtils.isNotEmpty(query)) {
            query = StringUtils.removeEnd(query, SearchConstant.ADD);
        }

        return query;
    }

    private String converter(String fieldEntry) {
        String[] arry = fieldEntry.split(",");
        String clause = arry[1]; // DB clause e.g. UPPER(user.FIRST_NAME)
        String operator = arry[2]; // EQUAL or LIKE or IN or BETWEEN
        String dataType = arry[3]; // STRING or LONG or INT or DATE
        String searchValue = arry[4]; // Search string value

        switch (dataType) {
            // To prepare query for fields of STRING datatype
            case "STRING":
                // To prepare query with '=' or LIKE operator
                if (operator.equals("EQUAL")) {
                    return clause + "='" + searchValue + "'";
                }
                else if (operator.equals("LIKE")) {
                    if (searchValue.contains("*")) {
                        /* To search value as LIKE %abc or ab%c or abc%.
                         * Then the value should be provided as *abc, ab*c or abc*
                         */
                        searchValue = searchValue.replaceAll("\\*", "%");
                        return clause + " like '" + searchValue.toUpperCase() + "'";
                    } else {
                        return clause + " like '%" + searchValue.toUpperCase() + "%'"; // To search value as LIKE %abc%
                    }
                }
                else {
                    // To prepare query with IN operator
                    String[] valuesForIN = searchValue.split("#");
                    String inQuery = "";

                    for (String value : valuesForIN) {
                        inQuery = inQuery + "'" + value.trim() + "',";
                    }
                    inQuery = StringUtils.removeEnd(inQuery, ",");
                    return clause + " IN (" + inQuery + ")";
                }

                // To prepare query for fields of LONG or INT datatype
            case "LONG":
            case "INT":
                // To prepare query with '=' operator
                if (operator.equals("EQUAL")) {
                    return clause + "=" + searchValue;
                }
                else {
                    // To prepare query with IN operator
                    String[] valuesForIN = searchValue.split("#");
                    String inQuery = "";

                    for (String value : valuesForIN) {
                        inQuery = inQuery + value + ",";
                    }
                    inQuery = StringUtils.removeEnd(inQuery, ",");
                    return clause + " IN (" + inQuery + ")";
                }

                // To prepare query for fields of DATE datatype
            case "DATE":
                // To prepare query with '=' operator
                if (operator.equals("EQUAL")) {
                    return clause + "=" + searchValue;
                }
                // To prepare query with '>=' operator
                else if (operator.equals("GREATER_THAN_EQUAL_TO")) {
                    return clause + ">=" + searchValue;
                }
                // To prepare query with '<=' operator
                else if (operator.equals("LESS_THAN_EQUAL_TO")) {
                    return clause + "<=" + searchValue;
                }
                else {
                    // To prepare query with BETWEEN operator
                    String[] valuesForBTW = searchValue.split("#");
                    String startDate = valuesForBTW[0];
                    String endDate = valuesForBTW[1];
                    String isPTDaylightSavings = null;
                    String modifiedClause = null;

                    if(StringUtils.isNotBlank(valuesForBTW[2])) {
                        isPTDaylightSavings = valuesForBTW[2];
                        modifiedClause = "DATE(UTCTOPT(" + clause + ", " + isPTDaylightSavings + "))";
                    }
                    else {
                        modifiedClause = "DATE(" + clause + ")";
                    }

                    return modifiedClause + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }

            default:
                break;
        }

        return null;
    }
}
