package com.mattu.flooring.service;

import com.sal.flooring.service.FlooringDataValidationException;
import com.sal.flooring.service.FlooringPersistenceException;
import com.sal.flooring.service.FlooringService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *
 * @author elizabethyim
 *
 * */
public class FlooringServiceImplTest {
    FlooringService testService;
    public FlooringServiceImplTest(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testService = ctx.getBean("serviceLayer",FlooringService.class);
    }

    @Test
    public void testCheckDateExists() throws IOException, FlooringPersistenceException, FlooringDataValidationException {
     try{
         System.out.println("checkDateExists");
         LocalDate date = LocalDate.parse("2022-01-01");
         testService.checkDateExists(date);
     }catch (FlooringDataValidationException ex){
         fail("Date should exist. No exception should be thrown");
     }
    }
    @Test
    public void testCheckIdExists() throws IOException, FlooringPersistenceException {
        try{
            System.out.println("checkIdExists");
            LocalDate date = LocalDate.parse("2022-01-01");
            int id = 1;
            testService.checkIdExists(id,date);
        }catch(FlooringPersistenceException ex){
            fail("Id should exist. No exception should be thrown");
        }
    }
    @Test
    public void testCheckDateAfter() throws FlooringDataValidationException {
        try{
            System.out.println("checkDateAfter");
            LocalDate date = LocalDate.parse("2023-01-01");
            boolean isAfter = testService.checkDateAfter(date);
            assertTrue(isAfter,"Test if date is after today's date.");
        }catch(FlooringDataValidationException ex){
            fail("Date should be after current date. No exception should be thrown");
        }
    }
    @Test
    public void testIsTaxable() throws FlooringPersistenceException {
        try{
            System.out.println("isTaxable");
            String state = "CA";
            boolean isTaxable = testService.isTaxable(state);
            assertTrue(isTaxable,"Test if state is taxable");
        }catch(FlooringPersistenceException ex){
            fail("CA is taxable. No exception should be thrown");
        }
    }
    @Test
    public void testValidArea() throws FlooringDataValidationException {
        System.out.println("validArea");
        BigDecimal area = new BigDecimal(250);
        boolean isValidArea = testService.validArea(area);
        assertTrue(isValidArea,"Test if area inputted is valid");
    }
}
