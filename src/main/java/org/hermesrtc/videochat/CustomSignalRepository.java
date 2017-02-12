package org.hermesrtc.videochat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface CustomSignalRepository extends JpaRepository<CustomSignal, Long> {
	
	@Query("select c from CustomSignal c where c.appKey.value = ?1") 
	List<CustomSignal> findByAppKeyValue (String appKey);
	
	@Query("select c from CustomSignal c where c.appKey.value = ?1 and c.value = ?2") 
	CustomSignal findByAppKeyValueAndSignalValue (String appKey, String signal);
}
