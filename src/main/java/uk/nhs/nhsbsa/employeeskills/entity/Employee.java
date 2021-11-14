package uk.nhs.nhsbsa.employeeskills.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "emp_id", updatable = false)
  private Long empId;

  @ManyToMany
  @JoinTable(
      name = "emp_skills",
      joinColumns = @JoinColumn(name = "employee_id"),
      inverseJoinColumns = @JoinColumn(name = "skills_id"))
  private Set<Skills> empSkillsSet = new HashSet<>();

  @Column(name = "given_name", nullable = false)
  private String givenName;

  @Column(name = "family_name", nullable = false)
  private String familyName;

  @Column(name = "date_of_birth", nullable = false)
  private String dateOfBirth;

  @Column(name = "check_sum", nullable = true)
  private String checkSum;
}
