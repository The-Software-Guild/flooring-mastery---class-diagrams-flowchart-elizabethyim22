package com.mattu.flooring.dao;

import com.mattu.flooring.service.FlooringDaoStubImpl;
import com.sal.flooring.dao.FlooringDao;
import com.sal.flooring.dao.FlooringDaoImpl;
import com.sal.flooring.dto.Order;
import com.sal.flooring.service.FlooringPersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author elizabethyim
 *
 * */
public class FlooringDaoImplTest {
    String testFile="testOrder.txt";
    String existFile="testExistFile.txt";
    FlooringDao testDao = new FlooringDaoImpl(testFile,existFile);
    FlooringDao stubDao = new FlooringDaoStubImpl();

    public FlooringDaoImplTest() throws FlooringPersistenceException {

    }
    @BeforeEach
    public void setUp() throws Exception{
    }
    @Test
    public void testGetOrderList() throws Exception{
        System.out.println("getOrderList");
        Order order1 = new Order("1,Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06");
        Map<Integer,Order> orderMap = new HashMap<>();
        orderMap.put(1,order1);
        LocalDate date = LocalDate.parse("2022-01-01");
        List<Order> result = stubDao.getOrderList(date,orderMap);
        List<Order> expResult = new ArrayList<>();
        expResult.add(order1);
        assertEquals(expResult, result, "Test correct order list is retrieved");
    }
    @Test
    public void testGetNewId() throws Exception{
        System.out.println("getNewId");
        LocalDate date = LocalDate.parse("2022-01-01");
        int result = stubDao.getNewId(date);
        int expResult = 2;
        assertEquals(expResult,result, "Test gets the correct new id");
    }
    @Test
    public void testLoadFiles() throws FlooringPersistenceException{
        System.out.println("loadFiles");
        LocalDate date = LocalDate.parse("2022-01-01");
        String orderName = "order1";
        Map<LocalDate,String> expResult = new HashMap<>();
        Map<LocalDate,String> result = stubDao.loadFiles();
        expResult.put(date,orderName);
        assertEquals(expResult,result,"Test to see that files are loaded");
        assertTrue(result.containsKey(date),"Map contains the correct key");
        assertEquals(result.size(),1,"Map should have size 1");
    }
    @Test
    public void testLoadOrderFile() throws Exception {
        System.out.println("loadOrderFile");
        LocalDate date = LocalDate.parse("2022-01-01");
        int id = 1;
        Order order1 = new Order("1,Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06");
        Map<Integer,Order> expResult = new HashMap<>();
        expResult.put(id,order1);
        Map<Integer,Order> result = stubDao.loadOrderFile(date);
        assertEquals(expResult,result,"Test to see that correct order file loaded");
        assertTrue(result.containsKey(1), "Map contains correct key");
        assertEquals(result.size(),1,"Map should have size 1");
    }
    @Test
    public void testCreateOrder() throws Exception{
        int id = 1;
        String name = "Ada Lovelace";
        String state = "CA";
        String prodType = "Tile";
        BigDecimal area = new BigDecimal(249.00);
        Order expResult = new Order("1,Ada Lovelace,CA,25.00,Tile,249,3.50,4.15,871.50,1033.35,476,2380.85");
        Order result = testDao.createOrder(id,name,state,prodType,area);
        assertEquals(expResult,result, "Test if create order works properly");
        assertNotNull(result, "Check that result is not null");
    }
    @Test
    public void testGetTaxRate() throws Exception{
        System.out.println("getTaxRate");
        String state = "CA";
        BigDecimal expResult = new BigDecimal(25.00);
        expResult = expResult.setScale(2, RoundingMode.HALF_UP);
        BigDecimal result = testDao.getTaxRate(state);
        assertEquals(expResult,result,"Test that getTaxRate gets the correct rate");
    }
    @Test
    public void testGetCostPerSqFt() throws Exception{
        System.out.println("getCostPerSqFt");
        String prodType = "Tile";
        BigDecimal expResult = new BigDecimal(3.50);
        expResult = expResult.setScale(2, RoundingMode.HALF_UP);
        BigDecimal result = testDao.getCostPerSqFt(prodType);
        assertEquals(expResult,result, "Test that getCostPerSqFt gets the correct price");
    }
    @Test
    public void getLaborCostPerSqFt() throws Exception{
        System.out.println("getLaborCostPerSqFt");
        String prodType = "Tile";
        BigDecimal expResult = new BigDecimal(4.15);
        expResult = expResult.setScale(2, RoundingMode.HALF_UP);
        BigDecimal result = testDao.getLaborCostPerSqFt(prodType);
        assertEquals(expResult,result,"Test that getLaborCostPerSqFt gets the correct price");
    }
    @Test
    public void testGetMaterialCost() throws Exception{
        System.out.println("getMaterialCost");
        BigDecimal area = new BigDecimal(249);
        BigDecimal costPerSqFt = new BigDecimal(3.50);
        BigDecimal expResult = area.multiply(costPerSqFt);
        expResult = expResult.setScale(1, RoundingMode.HALF_UP);
        BigDecimal result = testDao.getMaterialCost(area,costPerSqFt);
        assertEquals(expResult,result,"Test that getMaterialCostPerSqFt gets the correct price");
    }
    @Test
    public void testGetLaborCost() throws Exception{
        System.out.println("getLaborCostPerSqFt");
        BigDecimal area = new BigDecimal(249);
        BigDecimal laborCostPerSqFt = new BigDecimal(4.15);
        BigDecimal expResult = area.multiply(laborCostPerSqFt);
        expResult = expResult.setScale(2, RoundingMode.HALF_UP);
        BigDecimal result = testDao.getLaborCost(area,laborCostPerSqFt);
        assertEquals(expResult,result,"Test that getLaborCost gets the correct price");
    }
    @Test
    public void testGetTax() throws Exception{
        System.out.println("getTax");
        BigDecimal materialCost = new BigDecimal(871.5);
        BigDecimal laborCost = new BigDecimal(1033.35);
        BigDecimal taxRate = new BigDecimal(25.00);
        BigDecimal expResult = new BigDecimal(476.21);
        expResult = expResult.setScale(0,RoundingMode.HALF_UP);
        BigDecimal result = testDao.getTax(materialCost,laborCost,taxRate);
        assertEquals(expResult,result,"Test that getTax gets the correct price");
    }
    @Test
    public void testAddOrderToFile() throws Exception{
        int orderNum = 1;
        LocalDate date = LocalDate.parse("2020-01-01");
        Order order1 = new Order("1,Ada Lovelace,CA,25.00,Tile,249,3.50,4.15,871.50,1033.35,476,2380.85");
        Map<Integer,Order> expResult = new HashMap<>();
        expResult.put(1, order1);
        Map<Integer, Order> result = stubDao.addOrderToFile(orderNum,order1,date);
        assertEquals(expResult,result,"Test that addOrderToFile correctly adds orders");
        assertNotNull(result,"Check returned map is not null");
        assertEquals(result.size(),1,"Check returned map contains order");
    }
    @Test
    public void testUpdateOrder() throws Exception {
        System.out.println("updateOrder");
       Order order1 =  new Order("1,Ada Lovelace,CA,25.00,Tile,249,3.50,4.15,871.50,1033.35,476,2380.85");
       int orderNum = 1;
       LocalDate date = LocalDate.parse("2020-01-01");
       String name = "Elizabeth Yim";
       String state = "";
       String prodType = "";
       BigDecimal area = new BigDecimal(0);
       Order expResult = new Order("1,Elizabeth Yim,CA,25.00,Tile,249,3.50,4.15,871.50,1033.35,476,2380.85");
       Order result = testDao.updateOrder(order1,1,date,name,state,prodType,area);
       assertEquals(expResult,result,"Test that updateOrder correctly updates the order");
    }
    @Test
    public void testGetCurrOrder() throws Exception{
        System.out.println("getCurrOrder");
        int orderNum=1;
        LocalDate date = LocalDate.parse("2020-01-01");
        Order expResult = new Order("1,Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06");
        Order result = stubDao.getCurrOrder(orderNum,date);
        assertEquals(expResult,result,"Test that getCurrOrder returns the correct order");
    }
}
