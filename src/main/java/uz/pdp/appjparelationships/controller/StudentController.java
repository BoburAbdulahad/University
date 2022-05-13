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

import java.util.ArrayList;
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
//    3.for Decanat
    //4.for Group

//  1.Vazirlik uchun
    @GetMapping("/forMinistry")
    public List<Student> getStudentsForMinistry(/*@RequestParam int page*/){
        //select * from student limit 10 offset 0
//        Pageable pageable= PageRequest.of(page,10);
//        Page<Student> studentPage = studentRepository.findAll(pageable);
//        return studentPage;
        return studentRepository.findAll();
    }
//  2.Universitet uchun
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student>getStudentsForUniversity(@PathVariable Integer universityId,@RequestParam int page){
        Pageable pageable=PageRequest.of(page,10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }
    @PostMapping
    public String add(@RequestBody StudentDto studentDto){//checking in the right added
        Student student=new Student();
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent())
            return "Group not founded";
        student.setGroup(optionalGroup.get());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        Address address=new Address();
        address.setCity(studentDto.getCity());
        address.setDistrict(studentDto.getDistrict());
        address.setStreet(studentDto.getStreet());
        student.setAddress(address);
        addressRepository.save(address);
        List<Subject> subjectList=new ArrayList<>();
        studentDto.getSubjectsId().forEach(integer -> subjectRepository.findById(integer).ifPresent(subjectList::add));
        if (subjectList.isEmpty()) {
            return "Subjects not founded";
        }
//        dtoSubjectsId.forEach(integer -> {
//            Optional<Subject> optionalSubject = subjectRepository.findById(integer);
//            if (optionalSubject.isPresent()){
//                subjectList.add(optionalSubject.get());
//            }else {
//                return "Subject not found";
//            }
//            subjectList.add(optionalSubject.get());
//        });

        student.setSubjects(subjectList);
        studentRepository.save(student);
        return "New student successfully added !!!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id){
        try {
            studentRepository.deleteById(id);
            return "Student deleted";
        }catch (Exception e){
            return "Error in deleting";
        }
    }
    @PutMapping("/{id}")
    public String edit(@PathVariable Integer id,@RequestBody StudentDto studentDto){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()) {
            return "Sorry, student not founded";
        }
        Student student = optionalStudent.get();
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()) {
            return "Group not founded for the student";
        }
        student.setGroup(optionalGroup.get());
        student.getAddress().setCity(studentDto.getCity());
        student.getAddress().setDistrict(studentDto.getDistrict());
        student.getAddress().setStreet(studentDto.getStreet());
        addressRepository.save(student.getAddress());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        List<Subject> subjectList = student.getSubjects();
        subjectList.clear();
        List<Subject> newSubjectsList=new ArrayList();
        studentDto.getSubjectsId().forEach(integer -> subjectRepository.findById(integer).ifPresent(newSubjectsList::add));
        if (newSubjectsList.isEmpty())
            return "Subjects not founded";
        student.setSubjects(newSubjectsList);
        studentRepository.save(student);
        return "Student succesfully edited";

    }
}
