package com.marekhudyma.style.persistence;

import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, AccountId> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Account> findById(AccountId id);
}
