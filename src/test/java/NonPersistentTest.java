import dal.UserDAONonPersistent;
import dto.UserDTO;
import org.junit.jupiter.api.Test;

import dal.IUserDAO.*;

import java.util.List;

import static org.junit.Assert.*;

public class NonPersistentTest {

    public NonPersistentTest(){
        testCreateUser();
        try {
            testDeleteUser();
        } catch (DALException e) {
            e.printStackTrace();
        }
        try {
            testGetUserList();
        } catch (Exception ignored){}
        testGetUser();
        try {
            testUpdateUser();
        } catch (DALException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateUser(){
        // When creating a user with an invalid ID, i get an exception.
        UserDAONonPersistent NonPersistent = new UserDAONonPersistent();
        String message = "";
        try{
            UserDTO user1 = new UserDTO(-1, "Name", "Na", "123456-1234", "password", "Admin");
            NonPersistent.createUser(user1);
            message = "User Created.";
        } catch (DALException E) {
            message = E.getMessage();
        }
        assertEquals(message, "BrugerID skal være mellem 11 og 99 (inklusivt)");

        // When creating a user with a valid ID, i get no exception.

        try{
            UserDTO user1 = new UserDTO(20, "Name", "Na", "123456-1234", "password", "Admin");
            NonPersistent.createUser(user1);
            message = "User Created.";
        } catch (Exception E) {
            message = E.getMessage();
        }
        assertEquals(message, "User Created.");

        // When creating a user with the same ID as another one, i get an exception.

        try{
            UserDTO user1 = new UserDTO(20, "Name", "Na", "123456-1234", "password", "Admin");
            NonPersistent.createUser(user1);
            message = "User Created.";
        } catch (DALException E) {
            message = E.getMessage();
        }
        assertEquals(message, "Der findes allerede en bruger med ID 20");

    }

    @Test
    public void testGetUser(){
        //setup
        UserDAONonPersistent NonPersistent = new UserDAONonPersistent();
        try {
            UserDTO user1 = new UserDTO(20, "Name", "Na", "123456-1234", "password", "Admin");
            NonPersistent.createUser(user1);
        } catch (DALException ignored){

        }

        UserDTO user;
        String message = "";
        //When trying to get a user that exists, i get the user.

        try {
            user = NonPersistent.getUser(20);
            assertNotNull(user);
        } catch (DALException E){
            message = E.getMessage();
        }

        //When trying to get a user that doesn't exist, i get an exception.
        try {
            user = NonPersistent.getUser(-1);
        } catch (DALException E){
            message = E.getMessage();
        }
        assertEquals(message, "Der findes ingen bruger med ID -1");

    }

    @Test
    public void testGetUserList() throws DALException {
        //setup (When creating an object of UserDAONonPersistent, 3 dummy users are initialized automatically)
        UserDAONonPersistent NonPersistent = new UserDAONonPersistent();
        try {
            UserDTO user1 = new UserDTO(20, "Name", "Na", "123456-1234", "password", "Admin");
            NonPersistent.createUser(user1);
        } catch (DALException E){

        }

        // When getting the user list, i get a list with one user, that has ID = 20.

        List<UserDTO> users = NonPersistent.getUserList();

        assertEquals(4,users.size());
        assertEquals(20, users.get(3).getUserId());

    }

    @Test
    public void testUpdateUser() throws DALException {
        //setup
        UserDAONonPersistent NonPersistent = new UserDAONonPersistent();
        try {
            UserDTO user1 = new UserDTO(20, "Name", "Na", "123456-1234", "password", "Admin");
            NonPersistent.createUser(user1);
        } catch (DALException E){

        }

        String message = "";

        // When trying to update a user that doesn't exist, i get an exception

        try {
            UserDTO user2 = new UserDTO(-1, "Name", "Na", "123456-1234", "password", "Admin");
            NonPersistent.updateUser(user2);
        } catch (DALException E){
            message = E.getMessage();
        }
        assertEquals(message, "Brugeren du prøvede at opdatere fandtes ikke");

        // When trying to update a user that exists, it gets updated
        message = "";
        try {
            UserDTO user2 = new UserDTO(20, "UpdatedUser", "Na", "123456-1234", "password", "Admin");
            NonPersistent.updateUser(user2);
        } catch (DALException E){
            message = E.getMessage();
        }
        assertEquals(message, "");
        assertEquals(NonPersistent.getUser(20).getUserName(), "UpdatedUser");

    }

    @Test
    public void testDeleteUser() throws DALException {
        //setup
        UserDAONonPersistent NonPersistent = new UserDAONonPersistent();
        try {
            UserDTO user1 = new UserDTO(20, "Name", "Na", "123456-1234", "password", "Admin");
            NonPersistent.createUser(user1);
        } catch (DALException E){

        }

        String message = "";

        //When deleting a user, it is removed from the user-list.
        try {
            NonPersistent.deleteUser(20);
        } catch (DALException E){
            message = E.getMessage();
        }

        assertEquals(message,"");
        assertEquals(3, NonPersistent.getUserList().size());

        //When trying to delete a user that doesn't exist, i get an exception.

        try {
            NonPersistent.deleteUser(-1);
        } catch (DALException E){
            message = E.getMessage();

        }
        assertEquals(message,"Der fandtes ingen bruger med ID -1");


    }
}
