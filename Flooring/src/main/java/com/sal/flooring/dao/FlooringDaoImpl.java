package com.sal.flooring.dao;

import com.sal.flooring.dto.Order;
import com.sal.flooring.dto.ProdType;
import com.sal.flooring.dto.Taxes;
import com.sal.flooring.service.FlooringPersistenceException;
import org.springframework.stereotype.Component;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 *
 * @author elizabethyim
 *
 * */
@Component
public class FlooringDaoImpl implements FlooringDao{
    private Map<LocalDate, String> files = new HashMap<>();
    private Map<String, BigDecimal> taxes = new HashMap<>();
    private Map<String, ProdType> products = new HashMap<>();
    public String order_file;
    public String exist_file;
    public FlooringDaoImpl(){
        order_file="";
    }
    public FlooringDaoImpl(String orderFile, String existFile){
        order_file=orderFile;
        exist_file=existFile;
    }
    @Override
    public List<Order> getOrderList(LocalDate date, Map<Integer,Order> orderMap) throws FlooringPersistenceException, IOException {
        if (!files.containsKey(date)){
            throw new FlooringPersistenceException("Error: File does not exist");
        }
        return new ArrayList<Order>(orderMap.values());
    }
    @Override
    public Map<Integer,Order> addOrderToFile(Integer orderNum, Order order, LocalDate date) throws FlooringDaoException, FlooringPersistenceException, IOException {
        loadFiles();
        Map<Integer,Order> orders = loadOrderFile(date);
        orders.put(orderNum, order);
        return orders;
    }
    @Override
    public void writeOrdersToFile(LocalDate date,Map<Integer,Order> orders) throws FlooringPersistenceException, IOException {
        PrintWriter out;
        try{
            order_file=files.get(date);
            out=new PrintWriter(new FileWriter("/Users/elizabethyim/Desktop/wiley_repos/Flooring/Data/"+order_file));
        }catch (IOException ex){
            throw new FlooringPersistenceException("Error: could not save data", ex);
        }
        String orderAsText;
        List<Order> orderList = getOrderList(date,orders);
        for (Order currentOrder : orderList){
            orderAsText = currentOrder.marshalOrderAsText();
            out.println(orderAsText);
            out.flush();
        }
        out.close();
    }

    @Override
    public Map<Integer, Order> loadOrderFile(LocalDate date) throws FlooringPersistenceException, IOException {
        Map<Integer, Order> orders = new HashMap<>();
        if (!files.containsKey(date)){
            makeFile(date);
            orders.clear();
        }
        order_file=files.get(date);
        Scanner scanner;
        try{
            scanner=new Scanner(
                    new BufferedReader(
                            new FileReader("/Users/elizabethyim/Desktop/wiley_repos/Flooring/Data/"+order_file)));
        }catch(FileNotFoundException ex){
            throw new FlooringPersistenceException("Error: could not load data", ex);
        }
        String currentLine;
        Order currentOrder;
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder =new Order(currentLine);
            orders.put(currentOrder.getOrderNum(), currentOrder);
        }
        scanner.close();
        return orders;
    }

    @Override
    public int getNewId(LocalDate date) throws IOException, FlooringPersistenceException {
        int id = 1;
        if (files.containsKey(date)){
            Map<Integer,Order> orders = loadOrderFile(date);
            List<Order> orderList = this.getOrderList(date,orders);
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
    public Map<LocalDate, String> loadFiles() throws FlooringPersistenceException{
        Scanner scanner;
        try{
            scanner=new Scanner(
                    new BufferedReader(
                            new FileReader("files.txt")));
        }catch(FileNotFoundException ex){
            throw new FlooringPersistenceException("Error: could not load data into memory", ex);
        }
        String currentLine;
        LocalDate date;
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            String[] fields = currentLine.split(",");
            date = LocalDate.parse(fields[0]);
            String fileName=fields[1];
            files.put(date, fileName);
        }
        scanner.close();
        return files;
    }
    @Override
    public void makeFile(LocalDate date) throws IOException {
        String dateString=date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        String fileName="orders_"+dateString+".txt";
        files.put(date, fileName);
        File newFile= new File("/Users/elizabethyim/Desktop/wiley_repos/Flooring/Data/"+fileName);
        newFile.createNewFile();
        PrintWriter writer =new PrintWriter(new FileWriter("files.txt"));
        for (LocalDate dateKey : files.keySet()){
            String fileString = dateKey + "," + files.get(dateKey);
            writer.println(fileString);
            writer.flush();
        }
        writer.close();
    }

    @Override
    public Map<String, BigDecimal> loadTaxFile() throws FlooringPersistenceException{
        order_file="taxes.txt";
        Scanner scanner;
        try{
            scanner=new Scanner(
                    new BufferedReader(
                            new FileReader(order_file)));
        }catch(FileNotFoundException ex){
            throw new FlooringPersistenceException("Error: could not load data into memory", ex);
        }
        String currentLine;
        Taxes currentTax;
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTax =new Taxes(currentLine);
            taxes.put(currentTax.getStateAbr(), currentTax.getTax());
        }
        scanner.close();
        return taxes;

    }
    public Order createOrder(int orderID, String name, String state, String prodType, BigDecimal area) throws FlooringPersistenceException {
        Order order = new Order(orderID);
        order.setCustomerName(name);
        order.setState(state);
        order.setProdType(prodType);
        order.setArea(area);
        BigDecimal taxRate = getTaxRate(state);
        order.setTaxRate(taxRate);
        BigDecimal costPerSqFt = getCostPerSqFt(prodType);
        order.setCostPerSqft(costPerSqFt);
        order.setLaborCostPerSqft(getLaborCostPerSqFt(prodType));
        BigDecimal materialCost = getMaterialCost(area, costPerSqFt);
        order.setMaterialCost(materialCost);
        BigDecimal laborCost = getLaborCost(area, getLaborCostPerSqFt(prodType));
        order.setLaborCost(laborCost);
        BigDecimal tax =getTax(materialCost, laborCost, taxRate);
        order.setTax(tax);
        order.setTotal(materialCost.add(laborCost).add(tax));
        return order;
    }
    @Override
    public BigDecimal getTaxRate(String state) throws FlooringPersistenceException{
        Map<String, BigDecimal> taxes = new HashMap<>();
        taxes=loadTaxFile();
        return taxes.get(state);
    }
    @Override
    public Map<String, ProdType> loadProductFile() throws FlooringPersistenceException{
        order_file="products.txt";
        Scanner scanner;
        try{
            scanner=new Scanner(
                    new BufferedReader(
                            new FileReader(order_file)));
        }catch(FileNotFoundException ex){
            throw new FlooringPersistenceException("Error: could not load data into memory", ex);
        }
        String currentLine;
        ProdType currentProduct;
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct =new ProdType(currentLine);
            products.put(currentProduct.getType(), currentProduct);
        }
        scanner.close();
        return products;
    }
    @Override
    public BigDecimal getCostPerSqFt(String prodType) throws FlooringPersistenceException{
        Map<String, ProdType> products = new HashMap<>();
        products=loadProductFile();
        ProdType product = products.get(prodType);
        BigDecimal costPerSqFt=product.getCostPerSqFt();
        return costPerSqFt;
    }
    @Override
    public BigDecimal getLaborCostPerSqFt(String prodType) throws FlooringPersistenceException{
        Map<String, ProdType> products = new HashMap<>();
        products=loadProductFile();
        ProdType product = products.get(prodType);
        BigDecimal laborCostPerSqFt=product.getLaborCostPerSqFt();
        return laborCostPerSqFt;
    }
    @Override
    public BigDecimal getMaterialCost(BigDecimal area, BigDecimal costPerSqFt){
        BigDecimal materialCost = area.multiply(costPerSqFt);
        return materialCost;
    }
    @Override
    public BigDecimal getLaborCost(BigDecimal area, BigDecimal laborCostPerSqFt){
        BigDecimal laborCost = area.multiply(laborCostPerSqFt);
        laborCost = laborCost.setScale(2,RoundingMode.HALF_UP);
        return laborCost;
    }
    @Override
    public BigDecimal getTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate){
        BigDecimal tax = (materialCost.add(laborCost)).multiply(taxRate.divide(BigDecimal.valueOf(100)));
        tax=tax.setScale(0, RoundingMode.HALF_UP);
        return tax;
    }

    @Override
    public Order getCurrOrder(int orderNum, LocalDate date) throws IOException, FlooringPersistenceException {
        Map<Integer, Order> orderMap = loadOrderFile(date);
        return orderMap.get(orderNum);
    }
    @Override
    public void saveUpdate(Order newOrder, LocalDate date) throws IOException, FlooringPersistenceException {
        Map<Integer, Order> orderMap = loadOrderFile(date);
        orderMap.put(newOrder.getOrderNum(), newOrder);
        writeOrdersToFile(date,orderMap);
    }
    @Override
    public Order updateOrder(Order currentOrder, int orderNum, LocalDate date, String name, String state,
                            String prodType, BigDecimal area) throws FlooringPersistenceException, IOException {

        Order newOrder=new Order(orderNum);
        if (!name.equals("")){
            newOrder.setCustomerName(name);
        }else{
            newOrder.setCustomerName(currentOrder.getCustomerName());
        }
        if (!state.equals("")){
            newOrder.setState(state);
            newOrder.setTaxRate(getTaxRate(state));
        } else {
            newOrder.setState(currentOrder.getState());
            newOrder.setTaxRate(currentOrder.getTaxRate());
        }
        if (!prodType.equals("")){
            newOrder.setProdType(prodType);
            newOrder.setCostPerSqft(getCostPerSqFt(prodType));
            newOrder.setLaborCostPerSqft(getLaborCostPerSqFt(prodType));
        } else {
            newOrder.setProdType(currentOrder.getProdType());
            newOrder.setCostPerSqft(currentOrder.getCostPerSqft());
            newOrder.setLaborCostPerSqft(currentOrder.getLaborCostPerSqft());
        }
        if(area.compareTo(BigDecimal.ZERO) == 0){
            newOrder.setArea(currentOrder.getArea());
        } else {
            newOrder.setArea(area);
        }
        newOrder.setMaterialCost(getMaterialCost(newOrder.getArea(),newOrder.getCostPerSqft()));
        newOrder.setLaborCost(getLaborCost(newOrder.getArea(), newOrder.getLaborCostPerSqft()));
        newOrder.setTax(getTax(newOrder.getMaterialCost(), newOrder.getLaborCost(), newOrder.getTaxRate()));
        newOrder.setTotal(newOrder.getMaterialCost().add(newOrder.getLaborCost()).add(newOrder.getTax()));
        return newOrder;
    }
    @Override
    public void deleteOrder(LocalDate date, int orderNum) throws IOException, FlooringPersistenceException {
        Map<Integer, Order> orderMap = loadOrderFile(date);
        orderMap.remove(orderNum);
        writeOrdersToFile(date,orderMap);
    }


}
