package com.sal.flooring.service;

import com.sal.flooring.dao.FlooringDaoException;
import com.sal.flooring.dto.Order;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
/**
 *
 * @author elizabethyim
 *
 * */
public interface FlooringService {
    public int getNewId(LocalDate date) throws IOException, FlooringPersistenceException;
    /**
     *
     * This checks that the date is after today's date
     *
     * @param date
     *
     * @return boolean
     *
     * */
    public boolean checkDateAfter(LocalDate date) throws FlooringDataValidationException;
    /**
     *
     * This checks that the inputted state is taxable
     *
     * @param state
     *
     * @return boolean
     *
     * */
    public boolean isTaxable(String state) throws FlooringPersistenceException;

    /**
     *
     * This checks that the given area is greater than 200
     *
     * @param area
     *
     * @return boolean
     *
     * */
    public boolean validArea(BigDecimal area) throws FlooringDataValidationException;

    /**
     *
     * This checks if the date given already exists
     *
     * @param date
     *
     * */
    public void checkDateExists(LocalDate date) throws FlooringDataValidationException, IOException, FlooringPersistenceException;

    /**
     *
     * This checks if given ID already exists
     *
     * @param orderNum
     * @param date
     *
     * @return boolean
     *
     * */
    public boolean checkIdExists(int orderNum, LocalDate date) throws IOException, FlooringPersistenceException;
    public List<Order> getOrderList(LocalDate date,Map<Integer,Order> orderMap) throws FlooringPersistenceException, FlooringDataValidationException, IOException;
    public Map<LocalDate, String> loadFiles() throws FlooringPersistenceException;
    public Map<Integer, Order> loadOrderFile(LocalDate date) throws FlooringPersistenceException, IOException;
    public Order createOrder(int orderID, String name, String state, String prodType, BigDecimal area) throws FlooringPersistenceException;
    public Map<Integer,Order> saveOrderToFile(Integer orderNum, Order order, LocalDate date) throws FlooringDaoException, FlooringPersistenceException, IOException;
    public void writeOrdersToFile(LocalDate date,Map<Integer,Order> orders) throws FlooringPersistenceException, IOException;
    public Order updateOrder(Order currentOrder, int orderNum, LocalDate date, String name, String state,
                             String prodType, BigDecimal area) throws FlooringPersistenceException, IOException;
    public Order getCurrOrder(int orderNum, LocalDate date) throws IOException, FlooringPersistenceException;
    public void saveUpdate(Order newOrder, LocalDate date) throws IOException, FlooringPersistenceException;
    public void deleteOrder(LocalDate date, int orderNum) throws IOException, FlooringPersistenceException;
}
