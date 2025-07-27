package com.asb.backCompanyService.repository;

import com.asb.backCompanyService.model.EntityUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface IUserRepository extends JpaRepository<EntityUser, Long> {

    public Optional<EntityUser> findByEmail(String email);

    public Optional<EntityUser> findByEmailOrLogin(String email, String login);
    @Query("from EntityUser where (email=:email or login=:login) and id not in (:userIds)")
    public Page<EntityUser> findByEmailOrLoginAndIdNotIn(String email, String login, Collection<Long> userIds, Pageable pageable);

    public Optional<EntityUser> findByLogin(String login);

    @Override
    Page<EntityUser> findAll(Pageable pageable);


    Page<EntityUser> findByStatus(String status, Pageable pageable);

    List<EntityUser> findByStatus(String status);



    Page<EntityUser> findByIdOrNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrLoginContainingIgnoreCaseOrCompanyId_CompanyNameContainingIgnoreCaseOrPositionId_DescriptionContainingIgnoreCaseOrAreaId_DescriptionContainingIgnoreCaseOrAndStatus(
            Long id, String name, String email, String login, String companyName, String positionDescription, String areaDescription, String status, Pageable pageable);

}
