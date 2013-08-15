/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.model.filtering;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wlu
 */
public class Filter {

    private Pattern pattern = null;

    public void setRegex(String reg) {
        pattern = Pattern.compile(reg);
    }

    public boolean matches(String val) {
        Matcher matcher = pattern.matcher(val);
        return matcher.matches();
    }
    
    public static void main(String args[]){
        Filter filter = new Filter();
        filter.setRegex("^Test.*");
        System.out.println(filter.matches("TestUser"));
    }
}
