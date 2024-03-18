package org.example.test.onpier.repository;

import org.example.test.onpier.model.User;
import org.example.test.onpier.model.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, UserId> {
    @Query("select distinct u from User u where exists (select 1 from Borrowed b where b.user = u)")
    List<User> findUsersWithNonEmptyBorrowingHistory();

    @Query("select distinct u from User u join fetch u.borrowingHistory b where b.borrowedFrom = ?1")
    List<User> findUsersBorrowedBookedOnCertainDate(Date date);
}
