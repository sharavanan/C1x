/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrl.c1.dao;

//import com.lrl.c1.buyside.entity.Categorylist;
//import com.lrl.c1.buyside.entity.Demography;
import com.lrl.c1.entity.Account;
import com.lrl.c1.entity.Listofvalues;
import java.util.List;

import com.lrl.c1.entity.Mediaplan;
import com.lrl.c1.entity.Publisher;
import com.lrl.c1.entity.Publishercategory;
import com.lrl.c1.entity.Publishercontact;
import com.lrl.c1.entity.Userprofile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author logic
 */
@Repository("commonDao")
public class CommonDAOImpl implements CommonDAO {

    @Autowired
    private SessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(CommonDAOImpl.class);

    //@Autowired
    //private SessionFactory sessionFactory;
    
    /*
     *  Get CategoryName from Categorylist table
     */
   /* public List<Categorylist> GetAllCategory() {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Categorylist.class);
            return criteria.list();
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info("get all category " + e.getMessage());
            return null;
        }

    }

    
    public List<Placementtypelist> GetAllPlacements() {

        try {

            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Placementtypelist.class);//.setProjection( Projections.alias(Projections.groupProperty("Id"), "colr" ) );  
            return criteria.list();
        } catch (Exception e) {
            // e.printStackTrace();
            logger.info("get all Placementts " + e.getMessage());
            return null;
        }

    }

    public List<Demography> GetAgeRange() {
          try{  
         Session session = sessionFactory.getCurrentSession();                       
         Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Placementtypelist.class);//.setProjection( Projections.alias(Projections.groupProperty("Id"), "colr" ) );  
         return criteria.list();               
         }catch(Exception e){
         e.printStackTrace();
         return null;
         }
        return null;
    }
    */

    public List<Publisher> getPublisherOverviewByName(String publisher) {
        try {

            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Publisher.class).add(Restrictions.like("name", publisher));
            System.out.println(" size in name = " + criteria.list().size());
            return criteria.list();
        } catch (Exception e) {
            //  e.printStackTrace();
            logger.info("getPublisherOverviewByName " + e.getMessage());
            return null;
        }
    }

    public List<Publisher> getPublisherOverviewById(int id) {
        try {

            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Publisher.class).add(Restrictions.like("id", id));
            System.out.println(" size in id = " + criteria.list().size());

            return criteria.list();
        } catch (Exception e) {
            // e.printStackTrace();
            logger.info("getPublisherOverviewById " + e.getMessage());
            return null;
        }
    }

    public List<Publishercontact> getPublisherContact(int publisher_id) {
        try {
            //Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Publishercontact.class).add( Restrictions.eq("publisherId", publisher_id) );                  
            List categoryl = sessionFactory.getCurrentSession().createQuery("FROM Publishercontact as pc  WHERE pc.publisherId =:pid  ").setInteger("pid", publisher_id).list();

            return categoryl;
            //return criteria.list();
        } catch (Exception e) {
            //  e.printStackTrace();
            logger.info("getPublisherContact " + e.getMessage());
            return null;
        }
    }

    public List<Listofvalues> getGender(String gen) {
        try {
            //String gen = "Gen%";
            // Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Listofvalues.class).add( Restrictions.like("name", gen.toString()) );
            List<Listofvalues> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Listofvalues listofvalues WHERE name like 'gender'").list();
            //System.out.println(" size in id = "+criteria.list().size());
            // System.out.println(" list size="+criteriaq.size());
            return criteriaq;
            //return criteria.list();
        } catch (Exception e) {
            // e.printStackTrace();
            logger.info("logging started");
            logger.info("----------------");
            logger.info("getGender " + e);
            logger.info("---------------------------");
            return null;
        }
    }

    public List<Listofvalues> getAgeRange() {
        try {
            //Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Listofvalues.class).add( Restrictions.like("name", "age") );                  
            List<Listofvalues> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Listofvalues listofvalues WHERE name like 'age'").list();
            //return criteria.list();
            return criteriaq;
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info("getAgeRange " + e.getMessage());
            return null;
        }
    }

    public List<Listofvalues> getPlacementTypes() {
        try {
            //Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Listofvalues.class).add( Restrictions.like("name", "PlacementType") );         
            List<Listofvalues> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Listofvalues listofvalues WHERE name like 'PlacementType'").list();
            //return criteria.list();
            return criteriaq;
        } catch (Exception e) {
            // e.printStackTrace();
            logger.info("getPlacementTypes " + e.getMessage());
            return null;
        }
    }

    public List<Listofvalues> getCategoryList() {
        try {
            //Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Listofvalues.class).add( Restrictions.like("name", "CategoryList") );                  
            List<Listofvalues> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Listofvalues listofvalues WHERE name like 'CategoryList'").list();
            //return criteria.list();
            return criteriaq;
        } catch (Exception e) {
            // e.printStackTrace();
            logger.info("getCategoryList " + e.getMessage());
            return null;
        }
    }

    public List<Listofvalues> getCountries() {
        try {
            // Criteria criteriaq = sessionFactory.getCurrentSession().createCriteria(Listofvalues.class).add( Restrictions.like("name", "Country") );         
            List<Listofvalues> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Listofvalues listofvalues WHERE name like 'Country'").list();
            //return criteria.list();
            return criteriaq;
        } catch (Exception e) {
            // e.printStackTrace();
            logger.info("getCountries " + e.getMessage());
            return null;
        }
    }

    public int getCountryId(String country) {
        int countryid = 0;
        try {

            // Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Listofvalues.class).add( Restrictions.like("value", country) );                          
            List<Listofvalues> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Listofvalues listofvalues WHERE value like '" + country + "'").list();
            ListIterator recordings = criteriaq.listIterator();
            while (recordings.hasNext()) {
                Listofvalues r = (Listofvalues) recordings.next();
                countryid = r.getId();
            }

        } catch (Exception e) {
            //e.printStackTrace();          
            logger.info("getCountryId " + e.getMessage());
        }
        return countryid;
    }

    public int getStateId(String state) {
        int countryid = 0;
        try {

            // Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Listofvalues.class).add( Restrictions.like("value", state) );         
            List<Listofvalues> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Listofvalues listofvalues WHERE value = '" + state + "'").list();
            // return criteria.list();
            ListIterator recordings = criteriaq.listIterator();

            while (recordings.hasNext()) {
                Listofvalues r = (Listofvalues) recordings.next();
                countryid = r.getId();
            }

        } catch (Exception e) {
            // e.printStackTrace();          
            logger.info("getStateId " + e.getMessage());
        }
        //System.out.println(" country id "+countryid);
        return countryid;
    }

    public List<Listofvalues> getStates(String country) {
        try {
            int countryid = this.getCountryId(country);
            //Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Listofvalues.class).add(Restrictions.like("parentid", countryid) );         
            List<Listofvalues> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Listofvalues listofvalues WHERE parentid like " + countryid).list();
            //return criteria.list();
            return criteriaq;
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info("getStates " + e.getMessage());
            return null;
        }
    }

    public List<Listofvalues> getCities(String state) {
        try {
            int stateid = this.getStateId(state);
            //Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Listofvalues.class).add(Restrictions.like("parentid", stateid) );         
            List<Listofvalues> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Listofvalues listofvalues WHERE parentid like " + stateid).list();
            // return criteria.list();
            return criteriaq;
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info("getCities " + e.getMessage());
            return null;
        }
    }

    public List<Publishercategory> getPublishersId(String categorylist) {
        try {
            //Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Publishercategory.class).add( Expression.in("categoryid", categorylist) );
            //System.out.println(" size in name = "+criteria.list().size());
            List<Publishercategory> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Publishercategory publishercategory WHERE categoryid  IN ( " + categorylist + ") group by publishercategory.publisherid  ").list();

            List<Publisher> pub = sessionFactory.getCurrentSession().createQuery("SELECT publishercategory.publisherid FROM Publishercategory publishercategory WHERE publishercategory.categoryid  IN ( " + categorylist + ") group by publishercategory.publisherid  ").list();
            for (Publisher p : pub) {
                System.out.println(" pub.name " + p.getName());
            }


            return criteriaq;
            //return criteria.list();
        } catch (Exception e) {
           // e.printStackTrace();
            logger.info("getPublisherOverviewByName " + e.getMessage());
            return null;
        }

    }

    public List<Publisher> getSearchPublishers(String categorylist) {
        try {

            List<Publisher> pub = sessionFactory.getCurrentSession().createQuery("SELECT publishercategory.publisherid FROM Publishercategory publishercategory WHERE publishercategory.categoryid  IN ( " + categorylist + ") group by publishercategory.publisherid  ").list();
//         
            //System.out.println("publisher list == "+pub.size());
            
            
            
            return pub;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("getPublisherOverviewByName " + e.getMessage());
            return null;
        }

    }
    
    public List<Userprofile> getUser(String username){
         try {
              List<Userprofile> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Userprofile userprofile WHERE userprofile.userName like '"+username+"' ").setMaxResults(1).list();                       
             //List<Userprofile> criteriaq = sessionFactory.getCurrentSession().createQuery("FROM Userprofile userprofile WHERE userprofile.userName like '"+username+"' ").list();                       
            return criteriaq;
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info("getCountries " + e.getMessage());
            return null;
        }
        
    }
    
    
     public Account createNewAccount(Account account) {

        try {
            sessionFactory.getCurrentSession().saveOrUpdate("Account", account);
            //sessionFactory.getCurrentSession().flush();
            //sessionFactory.getCurrentSession().refresh(account);          
            return account;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
     public Userprofile createNewUserProfile(Userprofile userprofile){
         try {
            sessionFactory.getCurrentSession().saveOrUpdate("Userprofile", userprofile);
            //sessionFactory.getCurrentSession().flush();
            //sessionFactory.getCurrentSession().refresh(userprofile);          
            return userprofile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
     }
    
    
    
}
