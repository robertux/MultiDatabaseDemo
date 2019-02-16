package org.robertux.spring.demos.data.users;

import java.util.List;

import org.robertux.spring.demos.data.users.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {
	public List<User> findAll();
}
