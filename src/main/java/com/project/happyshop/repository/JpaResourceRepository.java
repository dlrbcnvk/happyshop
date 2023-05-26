package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaResourceRepository extends JpaRepository<Resource, Long> {

    Resource findByResourceNameAndHttpMethod(String resourceName, String httpMethod);
}
