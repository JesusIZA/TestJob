package Job1.ua.jrc.web.login;

import Job1.ua.jrc.web.registration.RegistrationPage;
import Job1.ua.jrc.web.welcomepage.WelcomePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
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

        final Label labelLogin = new Label("labelLogin", "Login does not exist!");
        labelLogin.setVisible(false);
        labelLogin.setOutputMarkupId(true);
        labelLogin.setOutputMarkupPlaceholderTag(true);

        final Label labelLogin2 = new Label("labelLogin2", "Login does not entered!");
        labelLogin2.setVisible(false);
        labelLogin2.setOutputMarkupId(true);
        labelLogin2.setOutputMarkupPlaceholderTag(true);

        final Label labelPassword = new Label("labelPassword", "Password failed!");
        labelPassword.setVisible(false);
        labelPassword.setOutputMarkupId(true);
        labelPassword.setOutputMarkupPlaceholderTag(true);

        final Label labelPassword2 = new Label("labelPassword2", "Password does not entered!");
        labelPassword2.setVisible(false);
        labelPassword2.setOutputMarkupId(true);
        labelPassword2.setOutputMarkupPlaceholderTag(true);

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
                            labelPassword2.setVisible(false);
                            labelPassword.setVisible(false);
                            labelLogin2.setVisible(false);
                            labelLogin.setVisible(true);

                        } else {
                            if (password.getInput().hashCode() == h2DAO.getUserByLogin(login.getInput()).getPassword().hashCode()) {
                                PageParameters parameters = new PageParameters();
                                parameters.add("userLoginer", login.getInput());

                                setResponsePage(WelcomePage.class, parameters);
                            } else {
                                labelLogin2.setVisible(false);
                                labelLogin.setVisible(false);
                                labelPassword2.setVisible(false);
                                labelPassword.setVisible(true);
                            }
                        }
                    } else {
                        labelLogin2.setVisible(false);
                        labelLogin.setVisible(false);
                        labelPassword2.setVisible(true);
                        labelPassword.setVisible(false);
                    }
                } else {
                    labelPassword2.setVisible(false);
                    labelPassword.setVisible(false);
                    labelLogin.setVisible(false);
                    labelLogin2.setVisible(true);
                }

                target.add(labelLogin);
                target.add(labelLogin2);
                target.add(labelPassword);
                target.add(labelPassword2);

            }
        };

        add(form);

        form.add(login);
        form.add(labelLogin);
        form.add(labelLogin2);

        form.add(password);
        form.add(labelPassword);
        form.add(labelPassword2);

        form.add(ajaxButton);


    }
}
