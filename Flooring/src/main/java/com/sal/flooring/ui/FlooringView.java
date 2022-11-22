package com.sal.flooring.ui;

import com.sal.flooring.dto.Order;
import com.sal.flooring.service.FlooringDataValidationException;
import com.sal.flooring.service.FlooringPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elizabethyim
 *
 * */

@Component
public class FlooringView {
    private UserIO io;

    @Autowired
    public FlooringView(UserIO io){
        this.io=io;
    }
    public int displayMainMenuAndGetSelection(){
        io.print("* * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<FLOORING PROGRAM>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Exit");
        io.print("*");
        io.print("* * * * * * * * * * * * * * * * * * * * *");
        return io.readInt("Please select an option: ", 1, 5);
    }

    public LocalDate getDate() throws FlooringDataValidationException {
        LocalDate date = io.readDate("Please input date: MM/DD/YYYY");
        return date;
    }
    public int getOrderNum() throws FlooringDataValidationException{
        int orderNum = io.readInt("Please input order number: ");
        return orderNum;
    }
    public String getCustomerName() throws FlooringDataValidationException{
        String name = io.readString("Please input Customer Name: ");
        return name;
    }
    public String getNewCustomerName(String name) throws FlooringDataValidationException{
        return io.readString("Please input new Customer Name: " + "(" + name + ")");
    }

    public String getState() throws FlooringDataValidationException{
        String state = io.readString("Please input Customer State abbreviation: ");
        return state;
    }
    public String getNewState() throws FlooringDataValidationException{
        String state = io.readString("Please input new Customer State abbreviation: ");
        return state;
    }

    public String getProdType() throws FlooringDataValidationException{
        io.print("***** Product Type :: CostPerSqFt :: LaborCostPerSqFt *****");
        io.print("1. Carpet, $2.25, $2.10");
        io.print("2. Laminate, $1.75, $2.10");
        io.print("3. Tile, $3.50, $4.15");
        io.print("4. Wood, $5.15, $4.74");
        io.print("");
        int prodType= io.readInt("Please input Product Type: ", 1, 4);
        switch(prodType){
            case 1:
                return "Carpet";
            case 2:
                return "Laminate";
            case 3:
                return "Tile";
            case 4:
                return "Wood";
        }
        return null;
    }

    public String getNewProdType() throws FlooringDataValidationException{
        io.print("***** Product Type :: CostPerSqFt :: LaborCostPerSqFt *****");
        io.print("1. Carpet, $2.25, $2.10");
        io.print("2. Laminate, $1.75, $2.10");
        io.print("3. Tile, $3.50, $4.15");
        io.print("4. Wood, $5.15, $4.74");
        io.print("5. No Change");
        io.print("");
        int prodType= io.readInt("Please input new Product Type: ",1,5);
        switch(prodType){
            case 1:
                return "Carpet";
            case 2:
                return "Laminate";
            case 3:
                return "Tile";
            case 4:
                return "Wood";
            case 5:
                return "";
        }
        return null;
    }
    public BigDecimal getArea() throws FlooringDataValidationException{
        BigDecimal area = io.readBigDecimal("Please input area: ");
        return area;
    }
    public String getNewArea() throws FlooringDataValidationException{
        String area = io.readString("Please input new area: ");
        return area;
    }
    public void displayOrderList(List<Order> orderList){
        for (Order currentOrder : orderList){
            String OrderInfo=String.format("%s: %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s,",
                    currentOrder.getOrderNum(),
                    currentOrder.getCustomerName(),
                    currentOrder.getState(),
                    currentOrder.getTax(),
                    currentOrder.getProdType(),
                    currentOrder.getArea(),
                    currentOrder.getCostPerSqft(),
                    currentOrder.getLaborCostPerSqft(),
                    currentOrder.getMaterialCost(),
                    currentOrder.getLaborCost(),
                    currentOrder.getTax(),
                    currentOrder.getTotal());
            io.print(OrderInfo);
        }
        io.readString("Please hit enter to continue");
    }

    public void displaySummary(Order order){
        io.print("****** Order Summary ******");
        io.print("Order Number: "+ order.getOrderNum());
        io.print("Customer Name: "+ order.getCustomerName());
        io.print("State: "+order.getState());
        io.print("Tax Rate: "+order.getTaxRate()+"%");
        io.print("Product Type: "+order.getProdType());
        io.print("Area: "+order.getArea());
        io.print("Cost/SqFt: $"+order.getCostPerSqft());
        io.print("Labor Cost/SqFt: $"+order.getLaborCostPerSqft());
        io.print("Material Cost: $"+order.getMaterialCost());
        io.print("Labor Cost: $"+order.getLaborCost());
        io.print("Tax: $"+order.getTax());
        io.print("Total: $"+order.getTotal());
        io.print("");
    }

    public void displayNewOrder(Order order){
        io.print("****** Updated Order Summary ******");
        io.print("Order Number: "+ order.getOrderNum());
        io.print("Customer Name: "+ order.getCustomerName());
        io.print("State: "+order.getState());
        io.print("Tax Rate: "+order.getTaxRate()+"%");
        io.print("Product Type: "+order.getProdType());
        io.print("Area: "+order.getArea());
        io.print("Cost/SqFt: $"+order.getCostPerSqft());
        io.print("Labor Cost/SqFt: $"+order.getLaborCostPerSqft());
        io.print("Material Cost: $"+order.getMaterialCost());
        io.print("Labor Cost: $"+order.getLaborCost());
        io.print("Tax: $"+order.getTax());
        io.print("Total: $"+order.getTotal());
        io.print("");
    }
    public void displayErrorMessage(String message) {
        io.print(message + '\n');
        io.readString("Please hit enter to continue.");
    }
    public boolean promptUserForAdd(){
        String response = io.readString("Do you want to place this order? (Y/N) ").toUpperCase();
        if (response.equals("Yes")||response.equals("Y")){
            return true;
        } else{
            return false;
        }
    }
    public boolean promptUserForEdit(){
        String response = io.readString("Do you want to edit this order? (Y/N) ").toUpperCase();
        if (response.equals("Yes")||response.equals("Y")){
            return true;
        } else{
            return false;
        }
    }

    public boolean promptUserForDelete(){
        String response = io.readString("Are you sure you want to delete? (Y/N) ").toUpperCase();
        if (response.equals("Yes")||response.equals("Y")){
            return true;
        } else{
            return false;
        }
    }

    //BANNERS
    public void displayDeleteSuccessBanner(){io.print("******* Order deleted successfully! *******");}
    public void displayUpdateSuccessBanner(){io.print("******* Order updated successfully! *******");}
    public void displaySuccessBanner(){io.print("******* Order added successfully! *******");}
    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }
    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayOrderBanner(){
        io.print("******* DISPLAY ORDER *******");
    }
    public void displayAddBanner(){io.print("******* ADD ORDER *******");}
    public void displayEditBanner(){io.print("******* EDIT ORDER *******");}
    public void displayDeleteBanner(){io.print("******* DELETE ORDER *******");}



}
