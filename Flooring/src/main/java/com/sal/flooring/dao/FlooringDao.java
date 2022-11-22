package com.sal.flooring.dao;

import com.sal.flooring.dto.Order;
import com.sal.flooring.dto.ProdType;
import com.sal.flooring.service.FlooringPersistenceException;
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
public interface FlooringDao {
    /**
     *
     * This takes in a date and creates a new file for that date
     *
     * @param date-date of the file to be created
     *
     * */
    public void makeFile(LocalDate date) throws IOException;

    /**
     *
     * This loads a hashmap of the products and prices
     *
     * @return hashmap
     *
     * */
    public Map<String, ProdType> loadProductFile() throws FlooringPersistenceException;

    /**
     *
     * This takes in date and hashmap and changes it to a list
     *
     * @param date-date of the file to retrieve
     * @param orderMap-hashmap of orders
     * @return list
     *
     * */
    public List<Order> getOrderList(LocalDate date,Map<Integer,Order> orderMap) throws FlooringPersistenceException, IOException;

    /**
     *
     * This takes in date and creates a new id using the existing ids
     *
     * @param date-date of the file to retrieve
     * @return order id int
     *
     * */
    public int getNewId(LocalDate date) throws IOException, FlooringPersistenceException;

    /**
     *
     * This loads the existing files
     *
     * @return hashmap of the exisiting files
     *
     * */
    public Map<LocalDate, String> loadFiles() throws FlooringPersistenceException;

    /**
     *
     * This loads a map of all the orders in a file
     *
     * @param date-date of the file to retrieve
     *
     * @return hashmap of the orders
     *
     * */
    public Map<Integer, Order> loadOrderFile(LocalDate date) throws FlooringPersistenceException, IOException;

    /**
     *
     * This loads a hashmap of taxfile
     *
     * @return hashmap
     *
     * */
    public Map<String, BigDecimal> loadTaxFile() throws FlooringPersistenceException;

    /**
     *
     * This creates the new order
     *
     * @param orderID
     * @param name
     * @param state
     * @param prodType
     * @param area
     *
     * @return Order
     *
     * */
    public Order createOrder(int orderID, String name, String state, String prodType, BigDecimal area) throws FlooringPersistenceException;

    /**
     *
     * This gets the tax rate
     *
     * @param state-state to get taxrate of
     *
     * @return taxrate as bigdecimal
     *
     * */
    public BigDecimal getTaxRate(String state) throws FlooringPersistenceException;

    /**
     *
     * This gets the costpersqft
     *
     * @param product-product type
     *
     * @return costpersqft as bigdecimal
     *
     * */
    public BigDecimal getCostPerSqFt(String product) throws FlooringPersistenceException;

    /**
     *
     * This gets the laborcostpersqft
     *
     * @param prodType-product type
     *
     * @return laborcostpersqft as bigdecimal
     *
     * */
    public BigDecimal getLaborCostPerSqFt(String prodType) throws FlooringPersistenceException;

    /**
     *
     * This gets the materialcost
     *
     * @param area
     * @param costPerSqFt
     *
     * @return materialcost as bigdecimal
     *
     * */
    public BigDecimal getMaterialCost(BigDecimal area, BigDecimal costPerSqFt);

    /**
     *
     * This gets the labor cost
     *
     * @param area
     * @param laborCostPerSqFt
     *
     * @return laborcost as bigdecimal
     *
     * */
    public BigDecimal getLaborCost(BigDecimal area, BigDecimal laborCostPerSqFt);

    /**
     *
     * This gets the tax
     *
     * @param materialCost
     * @param laborCost
     * @param taxRate
     *
     * @return tax as bigdecimal
     *
     * */
    public BigDecimal getTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);

    /**
     *
     * This writes the orders to the file
     *
     * @param date
     * @param orders-hashmap of the orders
     *
     * */
    public void writeOrdersToFile(LocalDate date,Map<Integer,Order> orders) throws FlooringPersistenceException, IOException;

    /**
     *
     * This adds new orders to the file
     *
     * @param orderNum
     * @param order
     * @param date
     *
     * @return hashmap of orders
     *
     * */
    public Map<Integer,Order> addOrderToFile(Integer orderNum, Order order, LocalDate date) throws FlooringDaoException, FlooringPersistenceException, IOException;

    /**
     *
     * This updates an existing order
     *
     * @param currentOrder
     * @param orderNum
     * @param date
     * @param name
     * @param state
     * @param prodType
     * @param area
     *
     * @return taxrate as bigdecimal
     *
     * */
    public Order updateOrder(Order currentOrder, int orderNum, LocalDate date, String name, String state,
                             String prodType, BigDecimal area) throws FlooringPersistenceException, IOException;

    /**
     *
     * This retrieves a specific order
     *
     * @param orderNum
     * @param date
     *
     * @return Order
     *
     * */
    public Order getCurrOrder(int orderNum, LocalDate date) throws IOException, FlooringPersistenceException;

    /**
     *
     * This saves the updates
     *
     * @param newOrder
     * @param date
     *
     * */
    public void saveUpdate(Order newOrder, LocalDate date) throws IOException, FlooringPersistenceException;

    /**
     *
     * This deletes an order
     *
     * @param date
     * @param orderNum
     *
     * */
    public void deleteOrder(LocalDate date, int orderNum) throws IOException, FlooringPersistenceException;

}
