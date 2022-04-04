package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubjectRepository subjectRepository;
//  1.Vazirlik uchun
    @GetMapping("/forMinistry")
    public Page<Student> getStudentsForMinistry(@RequestParam int page){
        //select * from student limit 10 offset 0
        Pageable pageable= PageRequest.of(page,10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }
//  2.Universitet uchun
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student>getStudentsForUniversity(@PathVariable Integer universityId,@RequestParam int page){
        Pageable pageable=PageRequest.of(page,10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }
    @PostMapping
    public String add(@RequestBody StudentDto studentDto){
        Student student=new Student();
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()) {
            return "Sorry your group not founded";
        }
        student.setGroup(optionalGroup.get());
        List<Subject> savedSubjects = null;
        for (Integer integer : studentDto.getSubjectsId()) {
           Optional<Subject> optionalSubject = subjectRepository.findById(integer);
            if (!optionalSubject.isPresent())
                return "Subject not founded";
            if (integer==optionalSubject.get().getId())
                savedSubjects.add(optionalSubject.get());
        }

        student.setSubjects(savedSubjects);
        Address address=new Address();
        address.setCity(studentDto.getCity());
        address.setDistrict(studentDto.getDistrict());
        address.setStreet(studentDto.getStreet());
        Address savedAddress = addressRepository.save(address);
        student.setAddress(savedAddress);
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        studentRepository.save(student);
        return "Student added";

    }

}
