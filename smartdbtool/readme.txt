The task of this project is to build a smart database tool, which can query and update the database by criteria.
the detail requirements:
  - the database table schema and relationship
    - the database table schema and relationship is provided by build-in java code which under packages ( phase 1 )
    - the database table schema and relationship can be provided by hibernate entities java classes. 
      these entities java classes can be zipped as jar file and uploaded to the application ( phase 2 )
    - The application can parse the underneath database metadata and generate hibernate entities ( phase 3 )
  - the query/update criteria
    - the criteria can be simple criteria or composed by relationship "OR", "AND", and use "(" and ")" to define the priority
	- a simple criteria is in the format <field> <operator> <value>. such as "name = bright"
    - the user don't need to specify the table relationship such as "join ... on ...". The application can tell the relationship
     