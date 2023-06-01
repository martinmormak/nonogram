package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.core.*;
import sk.tuke.gamestudio.entity.Data;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.sql.*;
import java.util.Date;

public class FieldGeneratorJPA implements FieldGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataService dataService;

    Integer pickRandomFileFromDatabase (){
        try {
            int ident = (int) entityManager.createNamedQuery("Data.getRandomFile")
                    .setParameter("game", "nonogram")
                    .setMaxResults(1)
                    .getSingleResult();
            System.out.println(ident);
            return ident;
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }

    Field readDataFromdDatabaseAndCreateField(Integer fieldID){
        Field field;
        try {

            Data data=dataService.getSavedGameData("nonogram",fieldID,"savedtemplate");

            byte[] objectData = data.getData();

            ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(objectData));
            field = (Field) objIn.readObject();
            objIn.close();
        }
        catch (Exception e) {
            throw new RuntimeException("I have problem with load game");
        }
        return field;
    }

    public Field generate() {
        return this.readDataFromdDatabaseAndCreateField(this.pickRandomFileFromDatabase());
    }

    public Field generate(Integer fieldID){
        if(fieldID==null||fieldID==0){return null;}
        try {
            return this.readDataFromdDatabaseAndCreateField(fieldID);

        }catch (Exception e){
            System.out.println("Zadal si zly subor.");
            System.exit(0);
        }
        return null;
    }
}
