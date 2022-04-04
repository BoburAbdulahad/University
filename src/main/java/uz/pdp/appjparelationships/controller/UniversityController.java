package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.University;
import uz.pdp.appjparelationships.payload.UniversityDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class UniversityController {

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    AddressRepository addressRepository;

    @RequestMapping(value = "/university",method = RequestMethod.GET)
    public List<University> getUniversities(){
        List<University> universityList = universityRepository.findAll();
        return  universityList;
    }

    @RequestMapping(value = "/university",method = RequestMethod.POST)
    public String addUniversity(@RequestBody UniversityDto universityDto){
        Address address=new Address();
        address.setCity(universityDto.getCity());
        address.setDistrict(universityDto.getDistrict());
        address.setStreet(universityDto.getStreet());
        Address savedAddress = addressRepository.save(address);

        University university=new University();
        university.setName(universityDto.getName());
        university.setAddress(savedAddress);
        universityRepository.save(university);
        return "University added";

    }
    @RequestMapping(value = "/university/{id}",method = RequestMethod.GET)
    public University getUniversityById(@PathVariable Integer id){
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isPresent()) {
            University university = optionalUniversity.get();
            return university;
        }
        return new University();
    }
    //delete
    @RequestMapping(value = "/university/{id}",method = RequestMethod.DELETE)
    public String deleteUniversity(@PathVariable Integer id){
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isPresent()) {
            University university = optionalUniversity.get();
            universityRepository.deleteById(university.getId());
            return "University deleted";
        }
        return "University not founded";
    }
    //update
    @RequestMapping(value = "/university/{id}",method = RequestMethod.PUT)
    public String editUniversity(@PathVariable Integer id,@RequestBody UniversityDto universityDto){
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isPresent()) {
            University university = optionalUniversity.get();
            university.setName(universityDto.getName());
            university.getAddress().setCity(universityDto.getCity());
            university.getAddress().setDistrict(universityDto.getDistrict());
            university.getAddress().setStreet(universityDto.getStreet());
            universityRepository.save(university);
            return "University edited";
        }
        return "University not founded";
    }

}
