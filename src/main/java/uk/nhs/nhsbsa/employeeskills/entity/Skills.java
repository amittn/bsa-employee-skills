package uk.nhs.nhsbsa.employeeskills.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Skills {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "skill_id", nullable = false)
  Long skillId;

  @Column(name = "skill", nullable = false)
  private String skill;

  @Column(name = "level", nullable = false)
  private String level;

  @JsonIgnore
  @ManyToMany(mappedBy = "empSkillsSet")
  private Set<Employee> empSet = new HashSet<>();
}
