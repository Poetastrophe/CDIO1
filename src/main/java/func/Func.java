package func;

import dal.IUserDAO;
import dto.UserDTO;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Func implements IFunc {
    IUserDAO dao;

    public Func(IUserDAO dao){this.dao = dao;}

    @Override
    public UserDTO createUser(int userID, String userName, String cpr, List<String> roles) throws IUserDAO.UserFormatException, IUserDAO.DALException {
        UserDTO user = new UserDTO(userID,userName, generateInitials(userName), cpr, generateRandomPassword(), roles);
        dao.createUser(user);
        return user;
    }

    @Override
    public List<UserDTO> getUserList() throws IUserDAO.DALException {
        return new ArrayList<>(dao.getUserList());
    }

    @Override
    public UserDTO getUser(int userID) throws IUserDAO.DALException {
            return dao.getUser(userID);
    }

    @Override
    public UserDTO deleteUser(int userID) throws IUserDAO.DALException {
        UserDTO user = getUser(userID);
            dao.deleteUser(userID);
            return user;
    }
    @Override
    public String generateInitials(String username){
        return "NOT IMPLMENTED GENERATEINITIALS IN FUNC";
    }
    @Override
    public String generateRandomPassword(){
        int min = 6;
        int max = 50;
        int len =(int) (Math.random()*(max - min)+ min);
        ArrayList<Character> pass = new ArrayList<Character>();

        char[] special = {'.', '-', '_', '+', '!', '?', '='};
        int x=0;
        for(int i = 0; i < len; i++){
            if(x>2)
                x=0;
            if(x == 0)
                pass.add((char) (Math.random()*(90 - 65)+65));
            else if(x == 1)
                pass.add((char) (Math.random()*(122 - 97) + 97));
            else if(x == 2)
                pass.add(special[(int)(Math.random()*(special.length))]);
            x++;
        }
        Collections.shuffle(pass);
        StringBuilder res = new StringBuilder();
        for(int i =0; i<len; i++ ){
            res.append(pass.get(i));
        }
        return res.toString();
    }
}
