package com.example.demo2.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo2.entity.User;
import com.example.demo2.enums.UserRole;
import java.math.BigDecimal;


public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    List<User> findByUnitNumber(String address);

    @Query("SELECT DISTINCT u.unitNumber FROM User u")
    List<String> findDistinctUnitNumbers();

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    Optional<User> findFirstByUnitNumberAndSquareFootageIsNotNull(String unitNumber);
    @Query("SELECT u FROM User u WHERE u.is_active = :isActive " +
            "And u.role = :role")
    List<User> findByIsActiveAndRole(@Param("isActive") boolean isActive, @Param("role") UserRole role);

    @Query(value = """
            SELECT
                u.unit_number as unitNumber,
                MAX(u.square_footage) as squareFootage,
                MAX(u.car_parking_space) as totalCarSpaces,
                MAX(u.motor_parking_space) as totalMotoSpaces
            FROM users u
            WHERE u.is_active = true
            AND u.role = 'RESIDENT'
            GROUP BY u.unit_number
            """, nativeQuery = true)
    List<Map<String, Object>> findUnitAssetSummary();

    List<User> findBySquareFootageAndRole(BigDecimal squareFootage, UserRole role);
}
