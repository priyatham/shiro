package com.connection.util;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dao.IUserDao;
import com.dao.UserDao;

/**
 * A store for entity managers. It is basically a ThreadLocal which stores the entity manager.
 * The {@link de.laliluna.transactions.TransactionInterceptor} is expected to register entity manager. The application code
 * can get the current entity manager either by injecting the store or the {@link EntityManagerDelegate}.
 *
 * @author Sebastian Hennebrueder
 */
public class EntityManagerStoreImpl /*implements EntityManagerStore*/ {

   final Logger logger = LoggerFactory.getLogger(EntityManagerStoreImpl.class);
   
   public static IUserDao user;
   
   @Produces @Manager
   @PersistenceContext(name = "configuration")
   protected EntityManager manager;

   @EJB
   protected IUserDao userDao;
   
   @Produces @Manager
   public IUserDao getUserDao(){
	   user = userDao;
	   return userDao;
   }
   
   public static IUserDao getUser(){
	   return user;
   }
   
   /*private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("configuration");;

   private ThreadLocal<Stack<EntityManager>> emStackThreadLocal = new ThreadLocal<Stack<EntityManager>>();

   public EntityManagerStoreImpl() {
	   
   }
   
   
  @PostConstruct
   public void init(){
	   emf = Persistence.createEntityManagerFactory("configuration");
	   logger.info("entity manager factory : {}", emf );
   }*/

   /*@Override
   public EntityManager get() {
      logger.debug("Getting the current entity manager");
      final Stack<EntityManager> entityManagerStack = emStackThreadLocal.get();
      if (entityManagerStack == null || entityManagerStack.isEmpty()) {
          if nothing is found, we return null to cause a NullPointer exception in the business code.
         This leeds to a nicer stack trace starting with client code.
          

         logger.warn("No entity manager was found. Did you forget to mark your method " +
               "as transactional?");

         return createAndRegister();
      } else
         return entityManagerStack.peek();
      
   }
   
   
   @Produces @Manager
   public EntityManager next(){
	   return get();
   }

   
   *//**
    * Creates an entity manager and stores it in a stack. The use of a stack allows to implement
    * transaction with a 'requires new' behaviour.
    *
    * @return the created entity manager
    *//*
   @Override
   public EntityManager createAndRegister() {
      logger.debug("Creating and registering an entity manager");
      Stack<EntityManager> entityManagerStack = emStackThreadLocal.get();
      if (entityManagerStack == null) {
         entityManagerStack = new Stack<EntityManager>();
         emStackThreadLocal.set(entityManagerStack);
      }

      final EntityManager entityManager = emf.createEntityManager();
      entityManagerStack.push(entityManager);
      return entityManager;
   }

   *//**
    * Removes an entity manager from the thread local stack. It needs to be created using the
    * {@link #createAndRegister()} method.
    *
    * @param entityManager - the entity manager to remove
    * @throws IllegalStateException in case the entity manager was not found on the stack
    *//*
   @Override
   public void unregister(EntityManager entityManager) {
      logger.debug("Unregistering an entity manager");
      final Stack<EntityManager> entityManagerStack = emStackThreadLocal.get();
      if (entityManagerStack == null || entityManagerStack.isEmpty())
         throw new IllegalStateException("Removing of entity manager failed. Your entity manager was not found.");

      if (entityManagerStack.peek() != entityManager)
         throw new IllegalStateException("Removing of entity manager failed. Your entity manager was not found.");
      entityManagerStack.pop();
   }*/
}