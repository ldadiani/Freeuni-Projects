package forController;

import forModel.AccountManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class AccountCreation extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AccountManager myAccounts= (AccountManager) getServletContext().getAttribute("AccountManager");
        String name= (String) request.getParameter("Username");
        String password=(String) request.getParameter("Password");

        if(!myAccounts.checkAccount(name)){
            myAccounts.createNewAccount(name,password);
            request.setAttribute("name",name);
            request.getRequestDispatcher("/WEB-INF/Correct.jsp").forward(request,response);
        } else {
            request.setAttribute("name",name);
            request.getRequestDispatcher("/WEB-INF/alreadyInUse.jsp").forward(request,response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/createNewAccount.jsp").forward(request,response);
    }
}
