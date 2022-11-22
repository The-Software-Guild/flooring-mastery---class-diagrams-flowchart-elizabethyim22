package com.sal.flooring.service;

import com.sal.flooring.dao.FlooringAuditDao;
import com.sal.flooring.dao.FlooringDao;
import com.sal.flooring.dao.FlooringDaoException;
import com.sal.flooring.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
/**
 *
 * @author elizabethyim
 *
 * */
@Component
public class FlooringServiceImpl implements FlooringService {
    FlooringDao dao;
    FlooringAuditDao auditDao;

    @Autowired
    public FlooringServiceImpl(FlooringDao dao, FlooringAuditDao auditDao){
        this.dao=dao;
        this.auditDao=auditDao;
    }
    @Override
    public int getNewId(LocalDate date) throws IOException, FlooringPersistenceException{
        return dao.getNewId(date);
    }
    @Override
    public Order updateOrder(Order currentOrder, int orderNum, LocalDate date, String name, String state,
                             String prodType, BigDecimal area) throws FlooringPersistenceException, IOException{
        return dao.updateOrder(currentOrder, orderNum, date, name, state, prodType, area);
    }
    @Override
    public Order getCurrOrder(int orderNum, LocalDate date) throws IOException, FlooringPersistenceException{
        return dao.getCurrOrder(orderNum, date);
    }
    @Override
    public void saveUpdate(Order newOrder, LocalDate date) throws IOException, FlooringPersistenceException{
        dao.saveUpdate(newOrder, date);
        auditDao.writeAuditEntry("Order number "+Integer.valueOf(newOrder.getOrderNum())+" for "+date.toString()+" has been updated.");
    }
    @Override
    public void deleteOrder(LocalDate date, int orderNum) throws IOException, FlooringPersistenceException{
        dao.deleteOrder(date, orderNum);
        auditDao.writeAuditEntry("Order number "+Integer.valueOf(orderNum)+" for "+date.toString()+" has been deleted.");
    }

    @Override
    public void checkDateExists(LocalDate date) throws FlooringDataValidationException, IOException, FlooringPersistenceException {
        try {
            dao.loadOrderFile(date);
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("Error: This date does not exist");
        }
    }
    @Override
    public boolean checkIdExists(int orderNum, LocalDate date) throws IOException, FlooringPersistenceException {
        Map<Integer, Order> orderMap = dao.loadOrderFile(date);
        if (orderMap.containsKey(orderNum)){
            return true;
        }
        return false;
    }
    public boolean checkDateAfter(LocalDate date) throws FlooringDataValidationException {
        if (date.isAfter(LocalDate.now())){
            return true;
        }
        return false;
    }
    @Override
    public List<Order> getOrderList(LocalDate date,Map<Integer,Order> orderMap) throws FlooringPersistenceException, FlooringDataValidationException, IOException {
        return dao.getOrderList(date,orderMap);
    }
    @Override
    public boolean isTaxable(String state) throws FlooringPersistenceException {
        Map<String, BigDecimal> taxes;
        taxes=dao.loadTaxFile();
        if (taxes.containsKey(state)){
            return true;
        }
        return false;
    }
    public boolean validArea(BigDecimal area) throws FlooringDataValidationException {
        BigDecimal min =new BigDecimal(200);
        if(area.compareTo(min)>=0){
            return true;
        }
        return false;
    }
    public Order createOrder(int orderID, String name, String state, String prodType, BigDecimal area) throws FlooringPersistenceException{
        return dao.createOrder(orderID, name, state, prodType, area);
    }
    @Override
    public Map<Integer,Order> saveOrderToFile(Integer orderNum, Order order, LocalDate date) throws FlooringDaoException, FlooringPersistenceException, IOException {
        auditDao.writeAuditEntry("Order number "+Integer.valueOf(orderNum)+" for "+date.toString()+" added to orders.");
        return dao.addOrderToFile(orderNum, order, date);
    }
    public void writeOrdersToFile(LocalDate date,Map<Integer,Order> orders) throws FlooringPersistenceException, IOException{
        dao.writeOrdersToFile(date,orders);
    }
    @Override
    public Map<LocalDate, String> loadFiles() throws FlooringPersistenceException{
        return dao.loadFiles();
    }
    @Override
    public Map<Integer, Order> loadOrderFile(LocalDate date) throws FlooringPersistenceException, IOException {
        return dao.loadOrderFile(date);
    }

}

