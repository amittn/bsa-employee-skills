package uk.nhs.nhsbsa.employeeskills.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.nhs.nhsbsa.employeeskills.entity.Skills;

@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {}
