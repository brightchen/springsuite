
I am trying to build a override-able properties system.
Basically, define property PROPERTY_FILE_LIST, this property defines a list of property files
the system will load properties from the files in this list, and the property in later file can override the same one in previous file

- terms:
  - MODULE_NAME: the name of a module. By default, the third field of the package is the module name. 
    For example, the module name of my.bc.configure is "configure"
  - PROPERTY_FILE_NAMES: the name( not path ) of property files. the default value is ${MODULE_NAME}.properties
  - HOME_DIR: the directory of application home. default is "."
  - CONF_DIR: the directory of application configuration, default is ${HOME_DIR}/conf
  - PROPERTY_FILE_LIST: a list of property file path. if the file path is relative file path ( not start with "/" ), 
    then the file path is related to the application's CONF directory.
    the default value of this property is <buildin property files>:<home property files>:<environment properties>
    