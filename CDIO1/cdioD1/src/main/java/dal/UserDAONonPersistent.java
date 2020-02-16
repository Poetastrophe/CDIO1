package dal;
import java.util.ArrayList;
import java.util.List;

import dto.UserDTO;

public class UserDAONonPersistent implements IUserDAO
{
    public List<UserDTO> users;

    public UserDAONonPersistent()
    {
        users = new ArrayList<UserDTO>();

        initializeDummyData();
    }

    public void initializeDummyData()
    {
        List<String> roles = new ArrayList<String>();
        roles.add("Admin");

        UserDTO dummy1 = new UserDTO();
        dummy1.setUserId(11);
        dummy1.setUserName("Mette Vons");
        dummy1.setIni("MeVo");
        dummy1.setRoles(roles);

        UserDTO dummy2 = new UserDTO();
        dummy1.setUserId(12);
        dummy1.setUserName("Steen Sikkert");
        dummy1.setIni("StSi");
        dummy1.setRoles(roles);

        UserDTO dummy3 = new UserDTO();
        dummy1.setUserId(13);
        dummy1.setUserName("Dan Mark");
        dummy1.setIni("DaMa");
        dummy1.setRoles(roles);

        try{
            createUser(dummy1);
            createUser(dummy2);
            createUser(dummy3);
        }
        catch (DALException e){
            System.out.println(e.getMessage());
        }


    }

    public UserDTO getUser(int userId) throws DALException{
        for (UserDTO user : users)
        {
            if (user.getUserId() == userId)
            {
                return user;
            }
        }
        throw new DALException("Der findes ingen bruger med ID " + userId);
    }

    public List<UserDTO> getUserList() throws DALException
    {
        return users;
    }

    public void createUser(UserDTO newUser) throws DALException
    {
        if (newUser.getUserId() < 11 || newUser.getUserId() > 99)
        {
            throw new DALException("BrugerID skal være mellem 11 og 99 (inklusivt)");
        }
        for (UserDTO user : users)
        {
            if (user.getUserId() == newUser.getUserId())
            {
                throw new DALException("Der findes allerede en bruger med ID " + user.getUserId());
            }
        }
        users.add(newUser);
    }

    public void updateUser(UserDTO newUser) throws DALException
    {
        for (UserDTO user : users)
        {
            if (user.getUserId() == newUser.getUserId())
            {
                users.remove(user);
                users.add(newUser);
                return;
            }
        }
        throw new DALException("Brugeren du prøvede at opdatere fandtes ikke");
    }

    public void deleteUser(int userId) throws DALException
    {
        for (UserDTO user : users)
        {
            if (user.getUserId() == userId)
            {
                users.remove(user);
                return;
            }
        }
        throw new DALException("Der fandtes ingen bruger med ID " + userId);
    }

}
