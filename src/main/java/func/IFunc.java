package func;

import dal.IUserDAO;
import dto.UserDTO;
import java.util.List;

public interface IFunc{
    // This method will generate the initials and password based on the given data. Returns a deep-copy of the resulting user.
    UserDTO createUser(int userID, String userName, String cpr, List<String> roles) throws IUserDAO.DALException, IUserDAO.UserFormatException;
    // Returns a deep-copy of the list of users, as to not being able to modify it.
    List<UserDTO> getUserList() throws IUserDAO.DALException;
    // Returns a deep-copy of the user with the specific id.
    UserDTO getUser(int userID) throws IUserDAO.DALException;
    // Deletes the user with specified id. Returns a deep-copy of the user.
    UserDTO deleteUser(int userID) throws IUserDAO.DALException;
    // This is a random generated password for the user, when it has not yet been set manually
    String generateRandomPassword();
    // This generates an initial for a user based on a username.
    String generateInitials(String username);

}
