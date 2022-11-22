package com.sal.flooring.ui;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

/**
 *
 * @author elizabethyim
 *
 * */
@Component
public class UserIOImpl implements UserIO{
    Scanner scanner;
//    @Override
    public UserIOImpl(){
        scanner=new Scanner(System.in);
    }

//    @Override
    public void print(String message){
        System.out.println(message);
    }

//    @Override
    public String readString(String prompt){
        System.out.println(prompt);
        return scanner.nextLine();
    }

//    @Override
    public int readInt(String prompt){
        System.out.println(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

//    @Override
    public int readInt(String prompt, int min, int max){
        int num;
        do {
            System.out.println(prompt);
            num = Integer.parseInt(scanner.nextLine());
        }
        while(num < min || num > max);
        return num;
    }

//    @Override
    public BigDecimal readBigDecimal(String prompt){
        System.out.println(prompt);
        return new BigDecimal(scanner.nextLine());
    }

//    @Override
    public LocalDate readDate(String prompt){
        System.out.println(prompt);
        //how the date is read in
        //figure out how to throw error if not in this form
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String date = scanner.nextLine();
        LocalDate localDate = LocalDate.parse(date, formatter);
        //I can do this somewhere else when I need to change pattern
        String formatDate = localDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        return localDate;
    }

}
