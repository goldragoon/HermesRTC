package org.hermesrtc.videochat;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface AppKeyRepository extends JpaRepository<AppKey, Long> {
	
	@Query("select k from AppKey k where k.developer.developer_id = ?1") 
	List<AppKey> findByDeveloper(Long developer_id);
	
	@Query("select k from AppKey k where k.developer.id = ?1") 
	List<AppKey> findByDeveloperUsername(String username);
	
	@Query("select k from AppKey k where k.value = ?1") 
	AppKey findByAppKeyValue(String value);
}
