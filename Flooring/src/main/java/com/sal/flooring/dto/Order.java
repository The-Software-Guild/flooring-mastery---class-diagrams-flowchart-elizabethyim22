package com.sal.flooring.dto;

import com.sal.flooring.service.FlooringPersistenceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author elizabethyim
 *
 * */
//used enum for ProdType
public class Order {
    private int orderNum;
//    private LocalDate date;
    private String customerName;
    private String state;
    private BigDecimal taxRate;
    private String prodType;
    private BigDecimal area;
    private BigDecimal costPerSqft;
    private BigDecimal laborCostPerSqft;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;
    private final String DELIMITER=",";

    public Order(int orderNum){
        this.orderNum=orderNum;
    }

    public Order(int orderNum, String customerName, String state, BigDecimal taxRate, String prodType,
                 BigDecimal area, BigDecimal costPerSqft, BigDecimal laborCostPerSqft, BigDecimal materialCost,
                 BigDecimal laborCost, BigDecimal tax, BigDecimal total) {
        this.orderNum=orderNum;
        this.customerName=customerName;
        this.state=state;
        this.taxRate=taxRate;
        this.prodType=prodType;
        this.area=area;
        this.costPerSqft=costPerSqft;
        this.laborCostPerSqft=laborCostPerSqft;
        this.materialCost=materialCost;
        this.laborCost=laborCost;
        this.tax=tax;
        this.total=total;
    }

    public Order(String orderString) throws FlooringPersistenceException {
        try{
            String[] fields = orderString.split(DELIMITER);
            this.orderNum=Integer.parseInt(fields[0]);
            this.customerName=fields[1];
            this.state=fields[2];
            this.taxRate = new BigDecimal(fields[3]);
            this.prodType=fields[4];
            this.area=new BigDecimal(fields[5]);
            this.costPerSqft=new BigDecimal(fields[6]);
            this.laborCostPerSqft=new BigDecimal(fields[7]);
            this.materialCost=new BigDecimal(fields[8]);
            this.laborCost=new BigDecimal(fields[9]);
            this.tax=new BigDecimal(fields[10]);
            this.total=new BigDecimal(fields[11]);

        }catch(PatternSyntaxException ex){
            throw new FlooringPersistenceException(ex.getMessage());
        }catch(NullPointerException | NumberFormatException ex){
//            throw new FlooringPersistenceException(ex.getMessage());
            throw new FlooringPersistenceException("Error: this doesnt work");
        }
    }
    public String marshalOrderAsText() {
        return this.getOrderNum() + DELIMITER + this.getCustomerName() + DELIMITER + this.getState() + DELIMITER + this.getTaxRate() + DELIMITER
                 + this.getProdType() + DELIMITER + this.getArea() + DELIMITER + this.getCostPerSqft() + DELIMITER + this.getLaborCostPerSqft()
                + DELIMITER + this.getMaterialCost() + DELIMITER + this.getLaborCost() + DELIMITER + this.getTax() + DELIMITER + this.getTotal();
    }

    //getter and setter for orderNum
    public int getOrderNum(){
        return orderNum;
    }
    public void setOrderNum(int orderNum){
        this.orderNum=orderNum;
    }

    //getter and setter for customerName
    public String getCustomerName(){
        return customerName;
    }
    public void setCustomerName(String customerName){
        this.customerName=customerName;
    }

    //getter and setter for state
    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state=state;
    }

    //getter and setter for taxRate
    public BigDecimal getTaxRate(){
        return taxRate;
    }
    public void setTaxRate(BigDecimal taxRate){
        this.taxRate=taxRate;
    }

    //getter and setter for prodType
    public String getProdType(){
        return prodType;
    }
    public void setProdType(String prodType){
        this.prodType=prodType;
    }

    //getter and setter for area
    public BigDecimal getArea(){
        return area;
    }
    public void setArea(BigDecimal area){
        this.area=area;
    }

    //getter and setter for costPerSqFt
    public BigDecimal getCostPerSqft(){
        return costPerSqft;
    }
    public void setCostPerSqft(BigDecimal costPerSqft){
        this.costPerSqft=costPerSqft;
    }

    //getter and setter for laborCostPerSqFt
    public BigDecimal getLaborCostPerSqft(){
        return laborCostPerSqft;
    }
    public void setLaborCostPerSqft(BigDecimal laborCostPerSqft){
        this.laborCostPerSqft=laborCostPerSqft;
    }

    //getter and setter for materialCost
    public BigDecimal getMaterialCost(){
        return materialCost;
    }
    public void setMaterialCost(BigDecimal materialCost){
        this.materialCost=materialCost;
    }

    //getter and setter for laborCost
    public BigDecimal getLaborCost(){
        return laborCost;
    }
    public void setLaborCost(BigDecimal laborCost){
        this.laborCost=laborCost;
    }

    //getter and setter for tax
    public BigDecimal getTax(){
        return tax;
    }
    public void setTax(BigDecimal tax){
        this.tax=tax;
    }

    //getter and setter for total
    public BigDecimal getTotal(){
        return total;
    }
    public void setTotal(BigDecimal total){
        this.total=total;
    }

    //hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getOrderNum() == order.getOrderNum() && getCustomerName().equals(order.getCustomerName()) && getState().equals(order.getState()) && getTaxRate().equals(order.getTaxRate()) && getProdType().equals(order.getProdType()) && getArea().equals(order.getArea()) && getCostPerSqft().equals(order.getCostPerSqft()) && getLaborCostPerSqft().equals(order.getLaborCostPerSqft()) && getMaterialCost().equals(order.getMaterialCost()) && getLaborCost().equals(order.getLaborCost()) && getTax().equals(order.getTax()) && getTotal().equals(order.getTotal()) && DELIMITER.equals(order.DELIMITER);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderNum(), getCustomerName(), getState(), getTaxRate(), getProdType(), getArea(), getCostPerSqft(), getLaborCostPerSqft(), getMaterialCost(), getLaborCost(), getTax(), getTotal(), DELIMITER);
    }

    //equals


    @Override
    public String toString() {
        return "Order{" +
                "orderNum=" + orderNum +
                ", customerName='" + customerName + '\'' +
                ", state='" + state + '\'' +
                ", taxRate=" + taxRate +
                ", prodType='" + prodType + '\'' +
                ", area=" + area +
                ", costPerSqft=" + costPerSqft +
                ", laborCostPerSqft=" + laborCostPerSqft +
                ", materialCost=" + materialCost +
                ", laborCost=" + laborCost +
                ", tax=" + tax +
                ", total=" + total +
                ", DELIMITER='" + DELIMITER + '\'' +
                '}';
    }
}
