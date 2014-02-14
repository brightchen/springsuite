package my.bc.web.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.bc.model.data.ErrorDatas;
import my.bc.model.exception.ValidationException;
import my.bc.service.EntityService;
import my.bc.user.data.UserDetailData;
import my.bc.user.data.UserSimpleData;
import my.bc.user.model.User;
import my.bc.user.service.UserService;
import my.bc.web.util.ClientDataUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController extends BaseController
{
  @Autowired
  private UserService userService;

  @Autowired
  private EntityService entityService;


  @RequestMapping( value = "/users", method = RequestMethod.GET)  
  @ResponseBody
  public Collection<UserSimpleData> getAdministrators(Model model) 
  {  
    return ClientDataUtil.toClientDatas( UserSimpleData.class, null, userService.getAllUsers() );
  }

  @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
  @ResponseBody
  public UserDetailData getAdministrator( @PathVariable("id") Long id  ) 
  {  
    UserDetailData detail = ClientDataUtil.toClientData(UserDetailData.class, entityService.getEntityById( User.class, id ) );
    return detail;
  } 

  
  @RequestMapping(value = "/users", method = RequestMethod.POST)
  @ResponseBody  
  public Object addAdministrator(@RequestBody UserDetailData userAddData, HttpServletRequest request, HttpServletResponse response ) 
  {  
    try
    {
      //validate the input first.
      User user = new User();
      userAddData.toBean(user);
  
      user = editUser( user );
      userAddData.setId( user.getId() );
      
      return userAddData;
    }
    catch( ValidationException ve )
    {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return new ErrorDatas( ve.getErrors() );
    }
  } 

  @RequestMapping(value = "/users", method = RequestMethod.PUT)
  @ResponseBody  
  public Object modifyAdministrator(@RequestBody UserDetailData userEditData, HttpServletRequest request, HttpServletResponse response ) 
  { 
    Long userId = userEditData.getId();
    String userName = userEditData.getName();
    if( userId == null && userName.isEmpty() )
    {
      //invalid input, 
      return null;
    }
    
    User user = null;

    if( userId != null )
    {
      user = userService.getUserById(userId);
    }

    if( user == null  )
    {
      if( userName.isEmpty() )
      {
        //invalid
        return null;
      }
      
      user = userService.getUserByName(userName);
    }
    
    if( user == null )
    {
      //invalid;
      return null;
    }

    try
    {
      userEditData.toBean(user);
      
      editUser( user );
      
      return userEditData;
    }
    catch( ValidationException ve )
    {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return new ErrorDatas( ve.getErrors() );
    }
  } 

  protected User editUser( User user )
  {
    if( user == null )
    {
      //invalid;
      return null;
    }
    
    entityService.loadDirectlyRelatedEntities( user );
    
    userService.validateUser(user);

    return userService.saveUser( user);
  }
  
  @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAdministrator( @PathVariable("id") Long id  ) 
  {  
    userService.removeUser(id);
  } 

  @RequestMapping(value = "/users/enable/{id}", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void enableAdministrator( @PathVariable("id") Long id  ) 
  {  
    userService.enableUser(id);
  } 
  
  @RequestMapping(value = "/users/disable/{id}", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void disableAdministrator( @PathVariable("id") Long id  ) 
  {  
    userService.disableUser(id);
  } 
  

  @ExceptionHandler
  public @ResponseBody
  ValidationException handleValidationException(ValidationException ex, HttpServletRequest request, HttpServletResponse response)
  {
    return super.handleValidationException(ex, request, response);
    // response.setHeader("Content-Type", "application/json");
    // response.sendError(503, "" + ex.getLocalizedMessage());
  }
//
//  @ExceptionHandler(Exception.class)
//  public @ResponseBody
//  String handleUncaughtException(Exception ex, WebRequest request, HttpServletResponse response)
//  {
//    // if (AjaxUtils.isAjaxRequest(request)) {
//    response.setHeader("Content-Type", "application/json");
//    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//    return "Unknown error occurred: " + ex.getMessage();
//    // } else {
//    // response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//    // ex.getMessage());
//    // return null;
//    // }
//  }


}