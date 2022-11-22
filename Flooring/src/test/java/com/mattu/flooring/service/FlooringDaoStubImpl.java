package com.mattu.flooring.service;

import com.sal.flooring.dao.FlooringDao;
import com.sal.flooring.dao.FlooringDaoException;
import com.sal.flooring.dto.Order;
import com.sal.flooring.dto.ProdType;
import com.sal.flooring.service.FlooringPersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
/**
 *
 * @author elizabethyim
 *
 * */
public class FlooringDaoStubImpl implements FlooringDao {
    public Order onlyOrder;
    public Map<LocalDate,Order> files = new HashMap<>();
    public LocalDate date =LocalDate.parse("2022-01-01");
    public BigDecimal tax = new BigDecimal(25.00);
    public BigDecimal costPerSqFt = new BigDecimal(3.50);
    public Map<String,BigDecimal> taxes = new HashMap<>();
    public Map<String,BigDecimal> products = new HashMap<>();
    public FlooringDaoStubImpl() throws FlooringPersistenceException {
        onlyOrder = new Order("1,Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06");
        files.put(date, onlyOrder);
        taxes.put("CA", tax);
        products.put("Tile",costPerSqFt);
    }
    public FlooringDaoStubImpl(Order testOrder){
        this.onlyOrder=testOrder;
    }
    @Override
    public List<Order> getOrderList(LocalDate date, Map<Integer,Order> orderMap) throws FlooringPersistenceException{
        return new ArrayList<Order>(orderMap.values());
    }
    @Override
    public int getNewId(LocalDate date) throws IOException, FlooringPersistenceException {
        int id = 1;

        if (files.containsKey(date)){
            Map<Integer,Order> orders = loadOrderFile(date);
            List<Order> orderList=new ArrayList<>();
            orderList.add(onlyOrder);
            List<Integer> idList=new ArrayList<Integer>();
            for (Order order: orderList){
                idList.add(order.getOrderNum());
            }
            Collections.sort(idList);
            id=idList.get(idList.size()-1)+1;
            return id;
        } else {
            return id;
        }
    }

    @Override
    public void makeFile(LocalDate date) throws IOException {

    }

    @Override
    public Map<String, ProdType> loadProductFile() throws FlooringPersistenceException {
        return null;
    }


    @Override
    public Map<LocalDate, String> loadFiles() throws FlooringPersistenceException {
        Scanner scanner;
        try{
            scanner=new Scanner(
                    new BufferedReader(
                            new FileReader("testExistFile.txt")));
        }catch(FileNotFoundException ex){
            throw new FlooringPersistenceException("Error: could not load data into memory", ex);
        }
        String currentLine;
        LocalDate date;
        Map<LocalDate,String> result = new HashMap<>();
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            String[] fields = currentLine.split(",");
            //might not be making proper local date object
            date = LocalDate.parse(fields[0]);
            String fileName=fields[1];
            result.put(date,fileName);
        }
        scanner.close();
        return result;
    }

    @Override
    public Map<Integer, Order> loadOrderFile(LocalDate date) throws FlooringPersistenceException, IOException {
        Scanner scanner;
        try{
            scanner=new Scanner(
                    new BufferedReader(
                            new FileReader("testOrder.txt")));
        }catch(FileNotFoundException ex){
            throw new FlooringPersistenceException("Error: could not load data", ex);
        }
        String currentLine;
        Order currentOrder;
        Map<Integer,Order> orderMap = new HashMap<>();
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder =new Order(currentLine);
            orderMap.put(currentOrder.getOrderNum(), currentOrder);
        }
        scanner.close();
        return orderMap;
    }

    @Override
    public Map<String, BigDecimal> loadTaxFile() throws FlooringPersistenceException {
        return taxes;
    }

    @Override
    public Order createOrder(int orderID, String name, String state, String prodType, BigDecimal area) throws FlooringPersistenceException {
        if (orderID == onlyOrder.getOrderNum()){
            return onlyOrder;
        }else {
            return null;
        }
    }

    @Override
    public BigDecimal getTaxRate(String state) throws FlooringPersistenceException {
        return taxes.get(state);
    }

    @Override
    public BigDecimal getCostPerSqFt(String prodType) throws FlooringPersistenceException {
        if (prodType==onlyOrder.getProdType()){
            return onlyOrder.getLaborCostPerSqft();
        }else{
            return null;
        }
    }

    @Override
    public BigDecimal getLaborCostPerSqFt(String prodType) throws FlooringPersistenceException {
        if (prodType==onlyOrder.getProdType()){
            return onlyOrder.getLaborCostPerSqft();
        }else{
            return null;
        }
    }

    @Override
    public BigDecimal getMaterialCost(BigDecimal area, BigDecimal costPerSqFt) {
        if (area==onlyOrder.getArea()){
            return onlyOrder.getMaterialCost();
        }else{
            return null;
        }
    }

    @Override
    public BigDecimal getLaborCost(BigDecimal area, BigDecimal laborCostPerSqFt) {
        if (area==onlyOrder.getArea()){
            return onlyOrder.getLaborCost();
        }else{
            return null;
        }
    }

    @Override
    public BigDecimal getTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate) {
        if (materialCost == onlyOrder.getMaterialCost()){
            return onlyOrder.getTax();
        }else{
            return null;
        }
    }

    @Override
    public void writeOrdersToFile(LocalDate date, Map<Integer, Order> orders) throws FlooringPersistenceException, IOException {
    }

    @Override
    public Map<Integer, Order> addOrderToFile(Integer orderNum, Order order, LocalDate date) throws FlooringDaoException, FlooringPersistenceException, IOException {
        Map<Integer,Order> orders = new HashMap<>();
        orders.put(orderNum, order);
        return orders;
    }

    @Override
    public Order updateOrder(Order currentOrder, int orderNum, LocalDate date, String name, String state, String prodType, BigDecimal area) throws FlooringPersistenceException, IOException {
        return null;
    }

    @Override
    public Order getCurrOrder(int orderNum, LocalDate date) throws IOException, FlooringPersistenceException {
        if(orderNum == onlyOrder.getOrderNum()){
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public void saveUpdate(Order newOrder, LocalDate date) throws IOException, FlooringPersistenceException {

    }

    @Override
    public void deleteOrder(LocalDate date, int orderNum) throws IOException, FlooringPersistenceException {

    }


}
