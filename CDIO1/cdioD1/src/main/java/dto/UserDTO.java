package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable{

  private static final long serialVersionUID = 4545864587995944260L;
  private int	userId;
  private String userName;
  private String ini;
  private List<String> roles;
  private String cpr;
  private String password;


  public UserDTO() {
    this.roles = new ArrayList<>(); }

  public void setUserCpr(String Cpr){ this.cpr = Cpr;}
  public String getUserCpr(){return cpr;}

  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public void setPassword(String pass){this.password = pass;}
  public String getPassword(){return password;}

  public String getIni() {
    return ini;
  }
  public void setIni(String ini) {
    this.ini = ini;
  }

  public List<String> getRoles() {
    return roles;
  }
  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public void addRole(String role){
    this.roles.add(role);
  }
  /**
   *
   * @param role
   * @return true if role existed, false if not
   */
  public boolean removeRole(String role){
    return this.roles.remove(role);
  }

  @Override
  public String toString() {
    return "UserDTO [userId=" + userId + ", userName=" + userName + ", ini=" + ini + ", roles=" + roles + "]";
  }



}
