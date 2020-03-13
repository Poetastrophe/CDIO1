import dal.*;
import dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SQLTest{
    private String username;
    private String password;

    public SQLTest(String SQLusername, String SQLpassword){
        this.username = SQLusername;
        this.password = SQLpassword;
        testCreateUser();
        testDeleteUser();
        try {
            testGetUserList();
        } catch (Exception e){
            e.printStackTrace();
        }
        testGetUser();
        testUpdateUser();
    }

    @Test
    public void testCreateUser(){
        IUserDAO userDAO = new UserDAOSQL("localhost","3306","user_database",username,password);
        String message = "";

        // When creating a user with a valid ID, i get no exception.
        try{
            userDAO.deleteUser(20);
        } catch (IUserDAO.DALException ignored){}
        try{
            UserDTO user1 = new UserDTO(20, "Name", "Na", "123456-1234", "password", "1");
            userDAO.createUser(user1);
            message = "User Created.";
        } catch (Exception E) {
            message = E.getMessage();
        }
        assertEquals(message, "User Created.");

        // When creating a user with the same ID as another one, i get an exception.

        try{
            UserDTO user2 = new UserDTO(-1, "Name", "Na", "123456-1234", "password", "1");
            UserDTO user1 = new UserDTO(-1, "Name", "Na", "123456-1234", "password", "1");
            userDAO.createUser(user1);
            userDAO.createUser(user2);
            message = "User Created.";
        } catch (IUserDAO.DALException E) {
            message = E.getMessage();
        }
        assertEquals(message,"Cannot create new user. Check for unique ID");

    }

    @Test
    public void testGetUser(){
        //setup
        IUserDAO iUserDAO = new UserDAOSQL("localhost","3306","user_database",username,password);

        UserDTO user;
        String message = "";
        //When trying to get a user that exists, i get the user.

        try {
            user = iUserDAO.getUser(0);
            assertNotNull(user);
        } catch (IUserDAO.DALException E){
            message = E.getMessage();
        }

    }

    @Test
    public void testGetUserList() throws IUserDAO.DALException {
        IUserDAO iUserDAO = new UserDAOSQL("localhost","3306","user_database",username,password);
        try {
            UserDTO user1 = new UserDTO(20, "Name", "Na", "123456-1234", "password", "1");
            iUserDAO.createUser(user1);
        } catch (IUserDAO.DALException ignored){

        }

        // When getting the user list, i get a list with one user, that has ID = 20.

        List<UserDTO> users = iUserDAO.getUserList();

        //Needs to be manually changed to actual table size
        assertEquals(3,users.size());
        //Needs to be manually changed to actual user position in database
        assertEquals(20, users.get(2).getUserId());
    }

    @Test
    public void testUpdateUser(){
        IUserDAO UserDAO = new UserDAOSQL("localhost","3306","user_database",username,password);

        try {
            UserDTO  user1 = new UserDTO(20, "Test1", "TeTe", "123456-1234", "password", "1");
            UserDAO.createUser(user1);

            int ID = user1.getUserId();
            String newName = "new";
            String newInIn = "Ne";
            String newCPR = "242424-2424";
            String newPassword = "he3_A";
            String newRole = "2";
            List<String> roles = new ArrayList<>();
            roles.add(newRole);
            UserDTO changeTo = new UserDTO(ID,newName,newInIn,newCPR,newPassword,newRole);
            UserDAO.updateUser(changeTo);
            user1 = UserDAO.getUser(ID);
            assertEquals(newName, user1.getUserName());
            assertEquals(newInIn, user1.getIni());
            assertEquals(newCPR, user1.getCpr());
            assertEquals(newPassword, user1.getPassword());
           assertEquals(roles, user1.getRoles());

        } catch (IUserDAO.DALException ignored){

        }


    }

    @Test
    public void testDeleteUser(){
        IUserDAO userDAO = new UserDAOSQL("localhost","3306","user_database",username,password);
        String message = "User Created.";

        // When creating a user with a valid ID, i get no exception.
        try{
            userDAO.deleteUser(20);
        } catch (IUserDAO.DALException e){
            message = e.toString();
        }
        try{
            UserDTO user1 = new UserDTO(20, "Name", "Na", "123456-1234", "password", "1");
            userDAO.createUser(user1);
        } catch (Exception ignored) {
        }
        assertEquals(message, "User Created.");
    }
}
