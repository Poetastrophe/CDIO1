package func;

import dto.UserDTO;
import java.util.List;

public interface IFunc{
    // This method will generate the initials and password based on the given data. Returns a deep-copy of the resulting user.
    UserDTO createUser(int userID, String userName, String cpr, String password, List<String> roles);
    // Returns a deep-copy of the list of users, as to not being able to modify it.
    List<UserDTO> getUserList();
    // Returns a deep-copy of the user with the specific id.
    UserDTO getUser(int userID);
    // Deletes the user with specified id. Returns a deep-copy of the user.
    UserDTO deleteUser(int userID);
}