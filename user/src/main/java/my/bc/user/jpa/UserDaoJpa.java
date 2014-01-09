package my.bc.user.jpa;

import my.bc.persistent.jpa.GenericDaoJpa;
import my.bc.user.dao.UserDao;
import my.bc.user.model.User;

public class UserDaoJpa extends GenericDaoJpa< User > implements UserDao
{

}
