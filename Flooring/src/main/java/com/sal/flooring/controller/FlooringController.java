package com.sal.flooring.controller;

import com.sal.flooring.dao.FlooringDaoException;
import com.sal.flooring.dto.Order;
import com.sal.flooring.service.FlooringDataValidationException;
import com.sal.flooring.service.FlooringPersistenceException;
import com.sal.flooring.service.FlooringService;
import com.sal.flooring.ui.FlooringView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

@Component
public class FlooringController {
    private FlooringView view;
    private FlooringService service;

    @Autowired
    public FlooringController(FlooringView view, FlooringService service){
        this.view=view;
        this.service=service;
    }
    public void run() throws FlooringDataValidationException, FlooringPersistenceException, FlooringDaoException, IOException {
        boolean endSwitch = true;
        int menuSelection = 0;
        try{
            while (endSwitch){
                service.loadFiles();
                    menuSelection = getMenuSelection();
                switch(menuSelection){
                    case 1:
                        LocalDate date;
                        view.displayOrderBanner();
                        try{
                            date = view.getDate();
                            //this isnt doing anything
                            service.checkDateExists(date);
                            displayOrder(date);
                        } catch (FlooringDataValidationException | FlooringPersistenceException e){
                            displayErrorMessage(e.getMessage());
                        }
                        break;

                    case 2:
                        view.displayAddBanner();
                        addOrder();
                        break;
                    case 3:
                        view.displayEditBanner();
                        try{
                            date = view.getDate();
                            service.checkDateExists(date);
                            editOrder(date);
                        } catch (FlooringDataValidationException | FlooringPersistenceException e){
                            displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 4:
                        view.displayDeleteBanner();
                        try{
                            date = view.getDate();
                            service.checkDateExists(date);
                            deleteOrder(date);
                        } catch (FlooringDataValidationException | FlooringPersistenceException e){
                            displayErrorMessage(e.getMessage());
                        }
                        break;
                    case 5:
                        endSwitch=false;
                        break;
                    default:
                        unknownCommand();
                }
            }
            exitMessage();
        } catch(FlooringDaoException e){
            throw new FlooringDaoException("Error: invalid input. Please try again.");
        }
    }

    void displayErrorMessage(String message){
        view.displayErrorMessage(message);
    }
    private void unknownCommand(){
        view.displayUnknownCommandBanner();
    }

    public int getMenuSelection(){
        return view.displayMainMenuAndGetSelection();
    }

    private void exitMessage(){
        view.displayExitBanner();
    }

    private void displayOrder(LocalDate date) throws FlooringPersistenceException, FlooringDataValidationException, IOException {
        Map<Integer,Order> orderMap = service.loadOrderFile(date);
        List<Order> orderList = service.getOrderList(date,orderMap);
//        List<Order> orderList = service.getOrderList(date);
        view.displayOrderList(orderList);
    }

    private void addOrder() throws FlooringDataValidationException, FlooringPersistenceException, FlooringDaoException, IOException {
        LocalDate date = getOrderDate();
        int orderID=service.getNewId(date);
        String name = view.getCustomerName();
        String state=getOrderState();
        String prodType=view.getProdType();
        BigDecimal area=getOrderArea();
        Order newOrder = service.createOrder(orderID, name, state, prodType, area);
        view.displaySummary(newOrder);
        if(view.promptUserForAdd()){
//            service.loadOrderFile(date);
            Map<Integer,Order> orders = service.saveOrderToFile(orderID, newOrder, date);
            service.writeOrdersToFile(date, orders);
            view.displaySuccessBanner();
        }
    }

    private LocalDate getOrderDate() throws FlooringDataValidationException{
        boolean isValid=false;
        LocalDate date= LocalDate.now();
        while(!isValid){
            date = view.getDate();
            if (service.checkDateAfter(date)){
                isValid=true;
            } else {
                view.displayErrorMessage("Error: Date is invalid. Please try again");
            }
        }
        return date;
    }

    private String getOrderState() throws FlooringDataValidationException, FlooringPersistenceException {
        boolean isValid=false;
        String state="";
        while(!isValid){
            state=view.getState().toUpperCase();
            if(service.isTaxable(state)) {
                isValid=true;
            }else{
                view.displayErrorMessage("Error: This state is invalid. Please try again. ");
            }
        }
        return state;
    }
    private String getNewOrderState() throws FlooringDataValidationException, FlooringPersistenceException {
        boolean isValid=false;
        String state="";
        while(!isValid){
            state=view.getNewState().toUpperCase();
            if(service.isTaxable(state)|| state.equals("")) {
                isValid=true;
            }else{
                view.displayErrorMessage("Error: This state is invalid. Please try again. ");
            }
        }
        return state;
    }

    private BigDecimal getOrderArea() throws FlooringDataValidationException{
        boolean isValid=false;
        BigDecimal area=new BigDecimal(0);
        while(!isValid){
            area=view.getArea();
            if(service.validArea(area)) {
                isValid = true;
            }else {
                view.displayErrorMessage("Error: This area is invalid. Please try again. ");
            }
        }
        return area;
    }
    private BigDecimal getNewOrderArea() throws FlooringDataValidationException{
        boolean isValid=false;
        BigDecimal newArea=new BigDecimal(0);
            while (!isValid) {
                String area= view.getNewArea();
                if (area.equals("")){
                    return BigDecimal.valueOf(0);
                } else{
                    newArea = new BigDecimal(area);
                    if (service.validArea(newArea)) {
                        isValid = true;
                    } else {
                        view.displayErrorMessage("Error: This area is invalid. Please try again. ");
                    }
                }
            }
            return newArea;

    }

    private void editOrder(LocalDate date) throws FlooringDataValidationException, IOException, FlooringPersistenceException {
        int orderNum = view.getOrderNum();
        Order currentOrder = service.getCurrOrder(orderNum, date);
        String name = view.getNewCustomerName(currentOrder.getCustomerName());
        String state = getNewOrderState();
        String prodType = view.getNewProdType();
        BigDecimal area = getNewOrderArea();
        Order newOrder = service.updateOrder(currentOrder, orderNum, date, name, state, prodType, area);
        view.displayNewOrder(newOrder);
        if (view.promptUserForEdit()){
            service.saveUpdate(newOrder, date);
            view.displayUpdateSuccessBanner();
        }
    }
    private void deleteOrder(LocalDate date) throws FlooringDataValidationException, IOException, FlooringPersistenceException {
        int orderNum = view.getOrderNum();
        Order currentOrder = service.getCurrOrder(orderNum, date);
        if(!service.checkIdExists(orderNum, date)){
            throw new FlooringPersistenceException("Error: This ID does not exist. Please try again. ");
        }
        view.displaySummary(currentOrder);
        if (view.promptUserForDelete()){
            service.deleteOrder(date, orderNum);
            view.displayDeleteSuccessBanner();
        }
    }
}
