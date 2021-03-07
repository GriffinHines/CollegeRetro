package com.retro.collegeretro.Repository;

import com.retro.collegeretro.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
