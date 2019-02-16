package org.robertux.spring.demos.data.roles;

import java.util.List;

import org.robertux.spring.demos.data.roles.Rol;
import org.springframework.data.repository.Repository;

public interface RolRepository extends Repository<Rol, Long> {
	public List<Rol> findAll();
}
