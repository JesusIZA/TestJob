package Job1.ua.jrc.web.registration;

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

public class RegistrationPage extends WebPage{

    public RegistrationPage(){

        User user = new User();

        add(new Label("header", "Enter your Login, Name and Password"));

        Form form = new Form("form");

        final Label labelLogin = new Label("labelLogin", "This Login already does exist!");
        labelLogin.setVisible(false);
        labelLogin.setOutputMarkupId(true);
        labelLogin.setOutputMarkupPlaceholderTag(true);

        final Label labelLogin2 = new Label("labelLogin2", "Login is not entered!");
        labelLogin2.setVisible(false);
        labelLogin2.setOutputMarkupId(true);
        labelLogin2.setOutputMarkupPlaceholderTag(true);

        final Label labelPassword = new Label("labelPassword", "Password is too short!");
        labelPassword.setVisible(false);
        labelPassword.setOutputMarkupId(true);
        labelPassword.setOutputMarkupPlaceholderTag(true);

        final Label labelPassword2 = new Label("labelPassword2", "Password is not entered!");
        labelPassword2.setVisible(false);
        labelPassword2.setOutputMarkupId(true);
        labelPassword2.setOutputMarkupPlaceholderTag(true);

        final Label labelName = new Label("labelName", "Name is not entered!");
        labelName.setVisible(false);
        labelName.setOutputMarkupId(true);
        labelName.setOutputMarkupPlaceholderTag(true);

        final TextField login = new TextField("login", new PropertyModel(user, "login"));
        login.setOutputMarkupId(true);

        final TextField password = new TextField("password", new PropertyModel(user, "password"));
        password.setOutputMarkupId(true);

        final TextField name = new TextField("name", new PropertyModel(user, "password"));
        name.setOutputMarkupId(true);

        AjaxButton ajaxButton = new AjaxButton("submit"){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form){
                super.onSubmit();

                if(login.getInput() != ""){
                    if(name.getInput() != "") {
                        if (password.getInput() != "") {

                            ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
                            H2DAO h2DAO = (H2DAO) context.getBean("H2DAO");

                            boolean flag = false;

                            for (User user : h2DAO.getAllUserList()) {
                                if (user.getLogin().hashCode() == login.getInput().hashCode())
                                    flag = true;
                            }

                            if (flag == true) {
                                //Login does exist
                                labelName.setVisible(false);
                                labelPassword2.setVisible(false);
                                labelPassword.setVisible(false);
                                labelLogin2.setVisible(false);
                                labelLogin.setVisible(true);

                            } else {
                                if (password.getInput().length() >= 4) {
                                    PageParameters parameters = new PageParameters();
                                    parameters.add("userLoginer", login.getInput());

                                    User tempUser = new User();
                                    tempUser.setLogin(login.getInput());
                                    tempUser.setName(name.getInput());
                                    tempUser.setPassword(password.getInput());

                                    h2DAO.insert(tempUser);

                                    setResponsePage(WelcomePage.class, parameters);
                                } else {
                                    labelName.setVisible(false);
                                    labelLogin2.setVisible(false);
                                    labelLogin.setVisible(false);
                                    labelPassword2.setVisible(false);
                                    labelPassword.setVisible(true);
                                }
                            }
                        } else {
                            //Password is not entered
                            labelName.setVisible(false);
                            labelLogin2.setVisible(false);
                            labelLogin.setVisible(false);
                            labelPassword2.setVisible(true);
                            labelPassword.setVisible(false);
                        }
                    } else {
                        //Name is not entered
                        labelPassword2.setVisible(false);
                        labelPassword.setVisible(false);
                        labelLogin.setVisible(false);
                        labelLogin2.setVisible(false);
                        labelName.setVisible(true);
                    }
                } else {
                    //Login is not entered
                    labelName.setVisible(false);
                    labelPassword2.setVisible(false);
                    labelPassword.setVisible(false);
                    labelLogin.setVisible(false);
                    labelLogin2.setVisible(true);
                }

                target.add(labelLogin);
                target.add(labelLogin2);
                target.add(labelPassword);
                target.add(labelPassword2);
                target.add(labelName);

            }
        };

        add(form);

        form.add(login);
        form.add(labelLogin);
        form.add(labelLogin2);

        form.add(name);
        form.add(labelName);

        form.add(password);
        form.add(labelPassword);
        form.add(labelPassword2);

        form.add(ajaxButton);

    }
}
