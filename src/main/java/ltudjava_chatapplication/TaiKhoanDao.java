/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltudjava_chatapplication;
import entities.Taikhoan;
import java.util.List;
import ltudjava_chatapplication.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
/**
 *
 * @author WIN-10
 */
public class TaiKhoanDao {
    
    private  SessionFactory sf = HibernateUtil.getSessionFactory();
    private Session session;
    
    public List<Taikhoan> getList()
    {
        try {
            session = sf.openSession();
            session.beginTransaction();
            return session.createCriteria(Taikhoan.class).list();
        } catch (HibernateException e) {
            System.out.println(e);
            return null;
            
        }finally{
            session.close();
        }
        

    }
    
    public Taikhoan find(String username)
    {
         try {
            session = sf.openSession();
            return (Taikhoan)session.get(Taikhoan.class, username);
        } catch (HibernateException e) {
            System.out.println(e);
            return null;
            
        }finally{
            session.close();
        }
        
    }
    
     public boolean save(Taikhoan tk)
    {
         try {
            session = sf.openSession();
            session.beginTransaction();
            session.save(tk);
            session.getTransaction().commit();
            return true;
            
        } catch (HibernateException e) {
            System.out.println(e);
            session.getTransaction().rollback();
            return false;
            
        }finally{
            session.close();
        }
        
    }
     
     public boolean update(Taikhoan tk)
    {
         try {
            session = sf.openSession();
            session.beginTransaction();
            session.update(tk);
            session.getTransaction().commit();
            return true;
            
        } catch (HibernateException e) {
            System.out.println(e);
            session.getTransaction().rollback();
            return false;
            
        }finally{
            session.close();
        }
        
    }
    
      public boolean delete(Taikhoan tk)
    {
         try {
            session = sf.openSession();
            session.beginTransaction();
            session.delete(tk);
            session.getTransaction().commit();
            return true;
            
        } catch (HibernateException e) {
            System.out.println(e);
            session.getTransaction().rollback();
            return false;
            
        }finally{
            session.close();
        }
        
    }
    
    
}
