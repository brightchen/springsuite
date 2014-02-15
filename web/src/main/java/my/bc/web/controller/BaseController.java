package my.bc.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.bc.model.exception.ValidationException;

import org.springframework.web.context.request.WebRequest;

public class BaseController
{
  public ValidationException handleValidationException(ValidationException ex, HttpServletRequest request, HttpServletResponse response)
  {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return ex;
  }

  public String handleUncaughtException(Exception ex, WebRequest request, HttpServletResponse response)
  {
    response.setHeader("Content-Type", "application/json");
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return "Unknown error occurred: " + ex.getMessage();
  }
}
