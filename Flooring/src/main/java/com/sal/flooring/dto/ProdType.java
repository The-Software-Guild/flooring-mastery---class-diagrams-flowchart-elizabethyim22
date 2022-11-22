package com.sal.flooring.dto;

import com.sal.flooring.service.FlooringPersistenceException;

import java.math.BigDecimal;
import java.util.regex.PatternSyntaxException;

public class ProdType {
    private String type;
    private BigDecimal costPerSqFt;
    private BigDecimal laborCostPerSqFt;
    private final String DELIMITER=",";

    public ProdType(String productString) throws FlooringPersistenceException {
        try{
            String[] fields = productString.split(DELIMITER);
            this.type=fields[0];
            this.costPerSqFt=new BigDecimal(fields[1]);
            this.laborCostPerSqFt=new BigDecimal(fields[2]);
        }catch(PatternSyntaxException ex){
            throw new FlooringPersistenceException(ex.getMessage());
        }catch(NullPointerException | NumberFormatException ex) {
            throw new FlooringPersistenceException(ex.getMessage());
        }
    }
    public String getType(){
        return type;
    }
    public BigDecimal getCostPerSqFt(){
        return costPerSqFt;
    }
    public BigDecimal getLaborCostPerSqFt(){
        return laborCostPerSqFt;
    }
}
