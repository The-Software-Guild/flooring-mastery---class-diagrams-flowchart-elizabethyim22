package com.sal.flooring.dto;

import com.sal.flooring.service.FlooringPersistenceException;

import java.math.BigDecimal;
import java.util.regex.PatternSyntaxException;

public class Taxes {
    private String stateAbr;
    private String state;
    private BigDecimal tax;
    private final String DELIMITER=",";

    public Taxes(String taxString) throws FlooringPersistenceException{
        try{
            String[] fields = taxString.split(DELIMITER);
            this.stateAbr=fields[0];
            this.state=fields[1];
            this.tax=new BigDecimal((fields[2]));
        }catch(PatternSyntaxException ex){
            throw new FlooringPersistenceException(ex.getMessage());
        }catch(NullPointerException | NumberFormatException ex) {
            throw new FlooringPersistenceException(ex.getMessage());
        }

    }

    public String getStateAbr(){
        return stateAbr;
    }
    public void setStateAbr(String stateAbr){
        this.stateAbr=stateAbr;
    }
    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state=state;
    }
    public BigDecimal getTax(){
        return tax;
    }
    public void setTax(BigDecimal tax){
        this.tax=tax;
    }

}
