package uz.pdp.appjparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appjparelationships.entity.Subject;
@Repository
public interface SubjectRepository extends JpaRepository<Subject,Integer> {
    boolean existsByName(String name);
}
