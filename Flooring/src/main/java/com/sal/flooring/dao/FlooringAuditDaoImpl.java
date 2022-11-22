package com.sal.flooring.dao;

import com.sal.flooring.service.FlooringPersistenceException;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Component
public class FlooringAuditDaoImpl implements FlooringAuditDao{
    public static final String AUDIT_FILE = "audit.txt";
    public void writeAuditEntry(String entry) throws FlooringPersistenceException{
        PrintWriter out;
        try{
            out=new PrintWriter(new FileWriter(AUDIT_FILE,true));
        } catch (IOException e){
            throw new FlooringPersistenceException("Error: Could not persist audit information");
        }
        LocalDateTime timeStamp = LocalDateTime.now();
        out.println(timeStamp.toString()+" : "+ entry);
        out.flush();
    }
}
