package org.hermesrtc.videochat;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
	@Query("select d from Developer d where d.id = ?1") 
	Developer findById(@Param("id") String id);
}