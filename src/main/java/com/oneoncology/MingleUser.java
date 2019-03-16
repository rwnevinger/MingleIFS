package com.oneoncology;

import java.util.*;
import java.text.*;

public class MingleUser extends Object {

  // authentication
  private String AuthDomainResult = null;
  private String Password = new String();

  // authorization resource

  private String ID = new String();
  private String RMID = new String();
  private String Name = new String();
  private String FirstName = new String();
  private String GivenName = new String();
  private String LastName = new String();
  private String SurName = new String();
  private String CheckLS = new String();
  private String Email = new String();
  private String AuthEmail = new String();
  private boolean updateEmail = false;
  private String Access = new String();
  private String Actor = new String();
  private String Group = new String();
  private String PortalAdmin = "N";
  private String Addins = "DENY";
  private String PortalRole = "essrole.xml";
  private String Role = new String();
  private String WFUser = "0";
  private String Company = "0";
  private String Employee = "0";
  private String action = new String();
  private String ResourceResult = new String();
  private String ResourceAdded = "N";
  private String ResourceUpdated = "N";
  private String ResourceDeleted = "N";
  private String ResourceUnable = "N";
  private String IdentityResult = new String();
  private String IdentityAdded = "N";
  private String Status = new String();
  private boolean updateName = false;
  private boolean updateStatus = false;
  private String TermDate = new String();
  private int TermGracePeriod = 0;
  private boolean LSFIdentity = false;

  // add auth ess properties 
  private String Department = new String();
  private boolean updateDepartment = false;
  private String JobCode = new String();
  private boolean updateJobCode = false;
  private String ProcessLevel = new String();
  private boolean updateProcessLevel = false;
  private String PositionCode = new String();
  private boolean updatePositionCode = false;
  private String LastPayDate = new String();
  private String BirthDate = new String();
  private String Fica = new String();
  private String FicaPlus4 = new String();
  private String Supervisor = new String();
  private boolean updateSupervisor = false;
  private String DateHired = new String();

  // default ESSLdap; 
  private String ESSLdap = new String();

  // active directory properties
  private String ldapAuthentication = null;
  private String whenCreated = null;
  private String lastLogon = null;
  private String logonCount = null;

  private String lastOsLogon = null;
  private ArrayList<String> assignedRoles = new ArrayList<String>();

  // new constructor
  public MingleUser() {
  }

  // rmid setter & getter
  public void setAction(String value) {
    this.action = value;
  }

  public String getAction() {
    return this.action;
  }

  public void setAuthDomainResult(String value) {
    this.AuthDomainResult = value;
  }

  public String getAuthDomainResult() {
    return this.AuthDomainResult;
  }


  // string returned from loadusers resource
  public void setResourceResult(String value) {
    this.ResourceResult = value;
  }

  public String getResourceResult() {
    return this.ResourceResult;
  }

  public void setResourceAdded(String value) {
    this.ResourceAdded = value;
  }

  public String getResourceAdded() {
    return this.ResourceAdded;
  }

  public void setResourceUpdated(String value) {
    this.ResourceUpdated = value;
  }

  public String getResourceUpdated() {
    return this.ResourceUpdated;
  }

  public void setResourceDeleted(String value) {
    this.ResourceDeleted = value;
  }

  public String getResourceDeleted() {
    return this.ResourceDeleted;
  }

  public void setResourceUnable(String value) {
    this.ResourceUnable = value;
  }

  public String getResourceUnable() {
    return this.ResourceUnable;
  }

  // string returned from loadusers identity
  public void setIdentityResult(String value) {
    this.IdentityResult = value;
  }

  public String getIdentityResult() {
    return this.IdentityResult;
  }

  public void setIdentityAdded(String value) {
    this.IdentityAdded = value;
  }

  public String getIdentityAdded() {
    return this.IdentityAdded;
  }

  // id aka ssopid setter & getter
  public void setID(String value) {
    this.ID = value;
  }

  public String getID() {
    return this.ID;
  }

  // rmid setter & getter
  public void setRMID(String value) {
    this.RMID = value;
  }

  public String getRMID() {
    return this.RMID;
  }

  // company setter & getter
  public void setCompany(String value) {
    this.Company = value;
  }

  public String getCompany() {
    return this.Company;
  }

  // datehired setter & getter
  public void setDateHired(String value) {
    this.DateHired = value;
  }

  public String getDateHired() {
    return this.DateHired;
  }

  // department setter & getter
  public void setDepartment(String value) {
    this.Department = value;
  }

  public String getDepartment() {
    return this.Department;
  }

  public void updateDepartment(boolean value) {
    this.updateDepartment = value;
  }

  public boolean isUpdateDepartment() {
    return updateDepartment;
  }

  // employee setter & getter
  public void setEmployee(String value) {
    this.Employee = value;
  }

  public String getEmployee() {
    return this.Employee;
  }

  // employee setter & getter
  public void setESSLdap(String value) {
    this.ESSLdap = value;
  }

  public String getESSLdap() {
    return this.ESSLdap;
  }

  // Fica/SSN setter & getter
  public void setFica(String value) {
    this.Fica = value;
  }

  public String getFica() {
    return this.Fica;
  }

  // Fica/SSN setter & getter
  public void setFicaPlus4(String value) {
    this.FicaPlus4 = value;
  }

  public String getFicaPlus4() {
    return this.FicaPlus4;
  }

  // jobcoode setter & getter
  public void setJobCode(String value) {
    this.JobCode = value;
  }

  public String getJobCode() {
    return this.JobCode;
  }

  public void updateJobCode(boolean value) {
    this.updateJobCode = value;
  }

  public boolean isUpdateJobCode() {
    return updateJobCode;
  }

  // name setter & getter
  public void setName(String value) {
    this.Name = value;
  }

  public String getName() {
    return this.Name;
  }

  // firstname setter & getter
  public void setFirstName(String value) {
    this.FirstName = value;
  }

  public String getFirstName() {
    return this.FirstName;
  }

  // givennamee used by auth ess setter & getter
  public void setGivenName(String value) {
    this.GivenName = value;
  }

  public String getGivenName() {
    return this.GivenName;
  }

  // lastname setter & getter
  public void setLastName(String value) {
    this.LastName = value;
  }

  public String getLastName() {
    return this.LastName;
  }

  // surname used by auth ess setter & getter
  public void setSurName(String value) {
    this.SurName = value;
  }

  public String getSurName() {
    return this.SurName;
  }

  public void updateName(boolean value) {
    this.updateName = value;
  }

  public boolean isUpdateName() {
    return updateName;
  }


  // checkls setter & getter
  public void setCheckLS(String value) {
    this.CheckLS = value;
  }

  public String getCheckLS() {
    return this.CheckLS;
  }

  // email setter & getter
  public void setEmail(String value) {
    this.Email = value;
  }

  public String getEmail() {
    return this.Email;
  }

  // auth ess email setter & getter
  public void setAuthEmail(String value) {
    this.AuthEmail = value;
  }

  public String getAuthEmail() {
    return this.AuthEmail;
  }

  public void updateEmail(boolean value) {
    this.updateEmail = value;
  }

  public boolean isUpdateEmail() {
    return updateEmail;
  }

  // access setter & getter
  public void setAccess(String value) {
    this.Access = value;
  }

  public String getAccess() {
    return this.Access;
  }

  // access setter & getter
  public void setLandmarkActor(String value) {
    this.Actor = value;
  }

  public String getLandmarkActor() {
    return this.Actor;
  }

  // group setter & getter
  public void setGroup(String value) {
    this.Group = value;
  }

  public String getGroup () {
    return this.Group;
  }

  // portaladmin setter & getter
  public void setPortalAdmin(String value) {
    this.PortalAdmin = value;
  }

  public String getPortalAdmin() {
    return this.PortalAdmin;
  }

  // addins setter & getter
  public void setAddins(String value) {
    this.Addins = value;
  }

  public String getAddins() {
    return this.Addins;
  }

  // status setter & getter
  public void setStatus(String value) {
    this.Status = value;
  }

  public String getStatus() {
    return this.Status;
  }

  public void updateEmployeeStatus(boolean value) {
    this.updateStatus = value;
  }

  public boolean isUpdateEmployeeStatus() {
    return updateStatus;
  }

  // supervisor setter & getter
  public void setSupervisor(String value) {
    this.Supervisor = value;
  }

  public String getSupervisor() {
    return this.Supervisor;
  }

  public void updateSupervisor(boolean value) {
    this.updateSupervisor = value;
  }

  public boolean isUpdateSupervisor() {
    return updateSupervisor;
  }

  // LastPayDate setter & getter
  public void setLastPayDate(String value) {
    this.LastPayDate = value;
  }

  public String getLastPayDate() {
    return this.LastPayDate;
  }

  // TermDate setter & getter
  public void setTermDate(String value) {
    this.TermDate = value;
  }

  public String getTermDate() {
    return this.TermDate;
  }

  // TermGracePeriod setter & getter
  public void setTermGracePeriod(int value) {
    this.TermGracePeriod  = value;
  }

  public int getTermGracePeriod() {
    return this.TermGracePeriod;
  }

  // BirthDate setter & getter
  public void setBirthDate(String value) {
    this.BirthDate = value;
  }

  public String getBirthDate() {
    return this.BirthDate;
  }

  // password setter & getter
  public void setPassword(String value) {
    this.Password = value;
  }

  public String getPassword() {
    return this.Password;
  }

  // positioncode setter & getter
  public void setPositionCode(String value) {
    this.PositionCode = value;
  }

  public String getPositionCode() {
    return this.PositionCode;
  }

  public void updatePositionCode(boolean value) {
    this.updatePositionCode = value;
  }

  public boolean isUpdatePositionCode() {
    return updatePositionCode;
  }

  // processlevel setter & getter
  public void setProcessLevel(String value) {
    this.ProcessLevel = value;
  }

  public String getProcessLevel() {
    return this.ProcessLevel;
  }

  public void updateProcessLevel(boolean value) {
    this.updateProcessLevel = value;
  }

  public boolean isUpdateProcessLevel() {
    return updateProcessLevel;
  }

  // portalrole setter & getter
  public void setPortalRole(String value) {
    this.PortalRole = value;
  }

  public String getPortalRole() {
    return this.PortalRole;
  }

  // role setter & getter
  public void setRole(String value) {
    this.Role = value;
  }

  public String getRole() {
    return this.Role;
  }

  // wfuser setter & getter
  public void setWFUser(String value) {
    this.WFUser = value;
  }

  public String getWFUser() {
    return this.WFUser;
  }

  // 2.2017 add LSFIdentity 
  public void setLSFIdentity(boolean value) {
    this.LSFIdentity = value;
  }

  public boolean isLSFIdentity() {
    return LSFIdentity;
  }

  // lastOsLogon setter & getter
  public void setLastOsLogon(String value) {

    // value is elapsed seconds since epoch - 01011970
    // you can use perl localtime to validate value
    // perl -we 'print(my $time=localtime 1301603167, "\n")'

    // calendar instance
    Calendar cLastLogon = Calendar.getInstance();
    String sLastOsLogon = "-";

    if(!value.equals("null")) {

      // convert epoch seconds to long then multiply by 1000
      Long lLastLogon = Long.parseLong(value) * 1000;

      // set the time
      cLastLogon.setTime(new Date(lLastLogon));

      Integer month = cLastLogon.get(Calendar.MONTH);
      // increment month so it looks normal
      month++;
      sLastOsLogon = Integer.toString(month);
      sLastOsLogon += "/";
      sLastOsLogon += cLastLogon.get(Calendar.DATE);
      sLastOsLogon += "/";
      sLastOsLogon += cLastLogon.get(Calendar.YEAR);

      //System.out.println("LawsonUser:" + lLastLogon + " " + lastOsLogon);

    }

    this.lastOsLogon = sLastOsLogon;

  }

  public String getLastOsLogon() {
    return lastOsLogon;
  }

  // wfuser setter & getter
  public void setAssignedRoles(String value) {
    this.assignedRoles.add(value);
  }

  public ArrayList getAssignedRoles() {
    //return Arrays.copyOf(this.assignedRoleIndex,this.assignedRoleIndex.length);
    return this.assignedRoles;
  }

}

