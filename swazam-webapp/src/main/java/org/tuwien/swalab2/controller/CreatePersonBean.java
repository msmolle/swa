/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.controller;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.tuwien.swalab2.ejb.PersonFacade;
import org.tuwien.swalab2.swazam.util.model.entities.Person;

/**
 *
 * @author gh
 */
@Named("createPerson")
@RequestScoped
public class CreatePersonBean {

    @Inject
    private PersonFacade personFacade;
    
    private Person person = new Person();
    
    public CreatePersonBean() {
    
    }    
    
    public String createAndPersistPerson(){
        
        personFacade.create(person);
        
        return "";
    }

    public String getPenis(){
        return "B===>";
    }
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
    
    

}
