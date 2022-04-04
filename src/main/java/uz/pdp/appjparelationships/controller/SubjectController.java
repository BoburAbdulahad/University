package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {

    @Autowired
    SubjectRepository subjectRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Subject> getSubjects(){
       return subjectRepository.findAll();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Subject getSubjectById(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isPresent()) {
            return optionalSubject.get();
        }
        return new Subject();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addSubject(@RequestBody Subject subject){
        boolean existsByName = subjectRepository.existsByName(subject.getName());
        if(existsByName)
            return "This subject already exist";
        subjectRepository.save(subject);
        return "Subject added";
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public String deleteSubject(@PathVariable Integer id){
        subjectRepository.deleteById(id);
        return "Deleted subject";
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public String editSubject(@PathVariable Integer id,@RequestBody Subject subject){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isPresent()){
            Subject subject1 = optionalSubject.get();
            subject1.setName(subject.getName());
            subjectRepository.save(subject1);
            return "Subject successfully edited";
        }
        return "Subject not founded";
    }

}
