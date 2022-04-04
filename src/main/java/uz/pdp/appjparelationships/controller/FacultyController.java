package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.University;
import uz.pdp.appjparelationships.payload.FacultyDto;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/faculty")
public class FacultyController {
    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    UniversityRepository universityRepository;
    //Universitet mas'ul xodimi uchun
    @GetMapping("/byUniversityId/{universityId}")
    public List<Faculty> getFacultiesByUniversityId(@PathVariable Integer universityId){
        List<Faculty> allByUniversityId = facultyRepository.findAllByUniversityId(universityId);
        return allByUniversityId;
    }


    @PostMapping
    public String add(@RequestBody FacultyDto facultyDto){
        boolean exists = facultyRepository.existsByNameAndUniversityId(facultyDto.getName(), facultyDto.getUniversityId());
        if (exists)
            return "This university such faculty exist";
        Faculty faculty=new Faculty();
        faculty.setName(facultyDto.getName());
        Optional<University> optionalUniversity = universityRepository.findById(facultyDto.getUniversityId());
        if (!optionalUniversity.isPresent())
            return "University not founded";
        faculty.setUniversity(optionalUniversity.get());
        facultyRepository.save(faculty);
        return "Faculty added";
    }
    @GetMapping//Vazirlik uchun
    public List<Faculty>getFaculty(){
        List<Faculty> facultyList = facultyRepository.findAll();
        return facultyList;
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Faculty getFacultyById(@PathVariable Integer id){
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        if (!optionalFaculty.isPresent())
            return new Faculty();
        return optionalFaculty.get();
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id){
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        if (optionalFaculty.isPresent()){
            facultyRepository.deleteById(optionalFaculty.get().getId());
            return "Faculty deleted";
        }
        return "Faculty not founded";
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public String edit(@PathVariable Integer id,@RequestBody FacultyDto facultyDto){
//        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
//        if (optionalFaculty.isPresent()){
//            boolean existsById = universityRepository.existsById(facultyDto.getUniversityId());
//            if (!existsById)
//                return "University not founded or university id fail!";
//            boolean exists = facultyRepository.existsByNameAndUniversityId(facultyDto.getName(), facultyDto.getUniversityId());
//            if (exists)
//                return "This faculty and this university id already exist";
//            Faculty faculty = optionalFaculty.get();
//            faculty.setName(facultyDto.getName());
//
//            facultyRepository.save(faculty);
//            return "Faculty edited";
//        }
//        return "Faculty not founded";
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        if (!optionalFaculty.isPresent()) {
            return "Faculty not founded";
        }
            Faculty faculty = optionalFaculty.get();
            faculty.setName(facultyDto.getName());
            Optional<University> optionalUniversity = universityRepository.findById(facultyDto.getUniversityId());
            if (!optionalUniversity.isPresent()) {
                return "University not founded";
            }
            faculty.setUniversity(optionalUniversity.get());
            facultyRepository.save(faculty);
            return "Faculty edited";
    }
}
