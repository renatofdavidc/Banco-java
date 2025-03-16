package com.example.BancoRenatoGustavoJava.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.BancoRenatoGustavoJava.Model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
