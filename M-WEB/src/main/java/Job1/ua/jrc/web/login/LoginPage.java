package Job1.ua.jrc.web.login;

import Job1.ua.jrc.web.welcomepage.WelcomePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.jrc.bd.entity.User;
import ua.jrc.bd.impldao.H2DAO;


public class LoginPage extends WebPage {

    public LoginPage(){

        final User user = new User();

        add(new Label("header", "Enter your Login and Password"));

        Form form = new Form("form");

        final Label labelLogin = new Label("labelLogin", "");
        labelLogin.setOutputMarkupId(true);
        labelLogin.setOutputMarkupPlaceholderTag(true);

        final Label labelPassword = new Label("labelPassword", "");
        labelPassword.setOutputMarkupId(true);
        labelPassword.setOutputMarkupPlaceholderTag(true);

        final TextField login = new TextField("login", new PropertyModel(user, "login"));
        login.setOutputMarkupId(true);

        final TextField password = new TextField("password", new PropertyModel(user, "password"));
        password.setOutputMarkupId(true);

        AjaxButton ajaxButton = new AjaxButton("submit"){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form){
                super.onSubmit();

                if(login.getInput() != ""){
                    if(password.getInput() != ""){

                        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
                        H2DAO h2DAO = (H2DAO) context.getBean("H2DAO");

                        String flag = "";

                        for(User user : h2DAO.getAllUserList()){
                            if(user.getLogin().hashCode() == login.getInput().hashCode())
                                flag = user.getLogin();
                        }

                        if(flag == ""){
                            labelPassword.setDefaultModelObject("");
                            labelLogin.setDefaultModelObject("Login does not exist!");
                        } else {
                            if (password.getInput().hashCode() == h2DAO.getUserByLogin(login.getInput()).getPassword().hashCode()) {
                                PageParameters parameters = new PageParameters();
                                parameters.add("userLoginer", login.getInput());

                                setResponsePage(WelcomePage.class, parameters);
                            } else {
                                labelLogin.setDefaultModelObject("");
                                labelPassword.setDefaultModelObject("Password is failed!");
                            }
                        }
                    } else {
                        labelLogin.setDefaultModelObject("");
                        labelPassword.setDefaultModelObject("Password is not entered!");
                    }
                } else {
                    labelPassword.setDefaultModelObject("");
                    labelLogin.setDefaultModelObject("Login is not entered!");
                }

                target.add(labelLogin);
                target.add(labelPassword);

            }
        };

        add(form);

        form.add(login);
        form.add(labelLogin);

        form.add(password);
        form.add(labelPassword);

        form.add(ajaxButton);
    }
}
