package com.sal.flooring.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author elizabethyim
 *
 * */
public interface UserIO {
    void print(String message);
    String readString(String prompt);
    int readInt(String prompt);
    int readInt(String prompt, int min, int max);
    BigDecimal readBigDecimal(String prompt);
    LocalDate readDate(String prompt);
}
