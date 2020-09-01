# employee-management


Project setup requirements:
* JDK 1.8 (java development kit as project is build using java, to run we need JRE)
* Maven latest version: (for dependency management and build and run)
* Postgres DB for storage (I have used version: 'psql (PostgreSQL) 12.3')
* Redis for cache ( I have used version: 5.0.4)

The project can be run as follows
* sh employee_management.sh
* Application will run on port 8081, we can see the Swagger Dashboard by browsing "http://localhost:8081/swagger-ui.html#/employee-controller"
* On startup application will load employee records from dir/employee.csv and save it to the Postgres db


sample Urls for testing Api's:
* PUT http://localhost:8081/employee/place/Delhi/salary/20 [salary increment for given place (Delhi) and percentage (20)]
* GET http://localhost:8081/employee/place/Delhi [get employees for given place (Delhi)]
* GET http://localhost:8081/employee/supervisee/1 [get all supervisee for given employee id (1)]
* GET http://localhost:8081/employee/salary?property=bu&propertyValue=RND [get total salary for given business unit (RND)]
* GET http://localhost:8081/employee/salary?property=place&propertyValue=Lko [get total salary for given place (Lko)]
* GET http://localhost:8081/employee/salary?property=supervisor&propertyValue=1 [get total salary for given supervisor id (1)]
* GET http://localhost:8081/employee/title/SDE2/salary-range [get salary range for give title (SDE2)]