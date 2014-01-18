package my.bc.user.jpa;

import org.springframework.stereotype.Component;

import my.bc.persistent.jpa.GenericDaoJpa;
import my.bc.user.dao.UserDao;
import my.bc.user.model.User;

@Component
public class UserDaoJpa extends GenericDaoJpa< User > implements UserDao
{

}
