package com.sal.flooring;

import com.sal.flooring.controller.FlooringController;
import com.sal.flooring.dao.*;
import com.sal.flooring.service.FlooringDataValidationException;
import com.sal.flooring.service.FlooringPersistenceException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws FlooringDataValidationException, FlooringPersistenceException, FlooringDaoException, IOException {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.sal.flooring");
        appContext.refresh();
        FlooringController controller = appContext.getBean("flooringController", FlooringController.class);
        controller.run();
    }
}