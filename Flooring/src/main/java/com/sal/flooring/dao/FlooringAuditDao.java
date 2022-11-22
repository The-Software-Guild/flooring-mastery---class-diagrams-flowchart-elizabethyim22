package com.sal.flooring.dao;

import com.sal.flooring.service.FlooringPersistenceException;

public interface FlooringAuditDao {
    void writeAuditEntry(String entry) throws FlooringPersistenceException;
}
