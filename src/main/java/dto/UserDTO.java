package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserDTO implements Serializable{

  private static final long serialVersionUID = 4545864587995944260L;
  private int	userId;
  private String userName;
  private String ini;
  private List<String> roles;
  private String cpr;
  private String password;

// Use this when constructing/changing a variable, we guarantee no mutability
  public UserDTO(int userId, String userName, String ini, String CPR, String password, List<String> roles){
    this.roles = roles;
    this.userId = userId;
    this.userName = userName;
    this.ini = ini;
    this.cpr = CPR;
    this.password = password;
  }

  public String getCpr() {
    return cpr;
  }

  public String getPassword() {
    return password;
  }

  public String getUserCpr(){return cpr;}

  public int getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public String getIni() {
    return ini;
  }

  public List<String> getRoles() {
    return roles;
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
