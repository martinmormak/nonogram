package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.core.*;

import java.io.FileNotFoundException;
import java.sql.*;

public interface FieldGenerator {

    Field generate() throws FileNotFoundException;

}
