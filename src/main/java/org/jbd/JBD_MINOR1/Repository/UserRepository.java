package org.jbd.JBD_MINOR1.Repository;

import org.jbd.JBD_MINOR1.Model.User;
import org.jbd.JBD_MINOR1.Model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    //passing the query which i have created
    @Query(value = "select * from user where :query", nativeQuery = true)
    List<User> findUsersByNativeQuery(@Param("query")String q);

    User findByPhoneNoAndUserType(String phoneNo, UserType type);
    User findByEmail(String email);
}
