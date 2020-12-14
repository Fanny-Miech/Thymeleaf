package com.microservice.thymeleaf.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.microservice.thymeleaf.form.PersonForm;
import com.microservice.thymeleaf.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@Controller
public class MainController {

    private static List<Person> persons = new ArrayList<Person>();

    static {
        persons.add(new Person("Bill"));
        persons.add(new Person("Steve"));
    }

    // Injecte (inject) via application.properties.
    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @Value("${BASE_URL}")
    private String BASE_URL;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = {"/personList/{id}"}, method = RequestMethod.GET)
    public String person(Model model, @PathVariable("id") Integer id) {
        ResponseEntity<Person> response = restTemplate.getForEntity(BASE_URL + id.toString(), Person.class);
        Person person = response.getBody();
        model.addAttribute("person", person);
        return "person";
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", message);
        return "index";
    }

    @RequestMapping(value = {"/personList"}, method = RequestMethod.GET)
    public String personList(Model model) {
        ResponseEntity<Person[]> response =
                restTemplate.getForEntity(
                        BASE_URL,
                        Person[].class);
        Person[] persons = response.getBody();
        model.addAttribute("persons", persons);
        return "personList";
    }

    @RequestMapping(value = {"/addPerson"}, method = RequestMethod.GET)
    public String showAddPersonPage(Model model) {
        PersonForm personForm = new PersonForm();
        model.addAttribute("personForm", personForm);
        return "addPerson";
    }

    @PostMapping(value = {"/addPerson"})
    public String savePerson(Model model, //
                             @ModelAttribute("personForm") PersonForm personForm) {
        String name = personForm.getName();

        if (name != null && name.length() > 0) {
            Person newPerson = new Person(name);
            ResponseEntity<Person> response = restTemplate.postForEntity(BASE_URL,
                    newPerson,
                    Person.class);
            //persons.add(newPerson);
            return "redirect:/personList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addPerson";
    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id) {
        restTemplate.delete(BASE_URL + id.toString());
        return "redirect:/personList";
    }

    @GetMapping(value = "/updateForm/{id}")
    public String GetById(Model model, @PathVariable("id") Integer id) {
        ResponseEntity<Person> response = restTemplate.getForEntity(BASE_URL + id.toString(), Person.class);
        Person personToUpdate = response.getBody();
        PersonForm personForm = new PersonForm();

        model.addAttribute("personForm", personForm);
        model.addAttribute("person", personToUpdate);
        return "updateForm";
    }

    @PostMapping(value = "/update/{id}")
    public String update(Model model, @ModelAttribute("personForm") PersonForm personForm, @PathVariable("id") Integer id) {
        String name = personForm.getName();

        if (name != null && name.length() > 0) {
            Person newPerson = new Person(name, id);
            HttpEntity<Person> request = new HttpEntity<>(newPerson);
            restTemplate.put(BASE_URL, request, Person.class);

            return "redirect:/personList";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "updateForm";
    }

}