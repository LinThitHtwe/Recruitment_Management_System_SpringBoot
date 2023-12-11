package com.ace.job.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ace.job.recruitment.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, DataTablesRepository<User, Integer> {
	List<User> findAllByEmail(String email);

	List<User> findAllByDepartmentIdAndStatusIsFalse(int departmentId);

	User findByEmail(String email);

	User findByRole(String role);

	User findAllByEmailAndStatusIsFalse(String email);

	@Modifying
	@Query("UPDATE User u SET u.status = :status WHERE u.id = :userId")
	void updateUserStatus(@Param("userId") int userId, @Param("status") Boolean status);

}
