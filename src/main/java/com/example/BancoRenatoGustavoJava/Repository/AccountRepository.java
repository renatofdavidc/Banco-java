package com.example.BancoRenatoGustavoJava.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.BancoRenatoGustavoJava.Model.Account;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCpf(Long cpf);
};


    
