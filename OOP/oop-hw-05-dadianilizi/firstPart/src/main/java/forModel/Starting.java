
package forModel;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Starting implements ServletContextListener{
    @Override
    public  void  contextInitialized(ServletContextEvent servletContextEvent){
        AccountManager myManager=new AccountManager();
        ServletContext servletContext=servletContextEvent.getServletContext();
        servletContext.setAttribute("AccountManager",myManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
