shiro
=====

Single sign on implementation with two components using shiro and memcache.

pre requisites

jdk-1.6 and above
maven 2.0 or later
jboss as7
memcache

How to deploy the application.

configure your datasource name in the persistence.xml, which you have configured in the jboss standalone.xml

<jta-data-source>java:jboss/datasources/ESERVE_AMAZON</jta-data-source>

create corresponding table in your database. or add the following property in persistence.xml

<property name="hibernate.hbm2ddl.auto" value="create-drop" />

compile and package modules in the following order.

CommonAuth
Auth1
Auth2


Deploying Auth1, Auth2 in the server.

http://localhost:8080/Auth1/login.jsp

http://localhost:8080/SecurityTest.jsp(it will work without asking for authentication.)


