package Job1.ua.jrc.web.registration;

import Job1.ua.jrc.web.login.LoginPage;
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

        final Label labelLogin = new Label("labelLogin", "");
        labelLogin.setOutputMarkupId(true);
        labelLogin.setOutputMarkupPlaceholderTag(true);

        final Label labelPassword = new Label("labelPassword", "");
        labelPassword.setOutputMarkupId(true);
        labelPassword.setOutputMarkupPlaceholderTag(true);

        final Label labelName = new Label("labelName", "");
        labelName.setOutputMarkupId(true);
        labelName.setOutputMarkupPlaceholderTag(true);

        final TextField login = new TextField("login", new PropertyModel(user, "login"));
        login.setOutputMarkupId(true);

        final TextField password = new TextField("password", new PropertyModel(user, "password"));
        password.setOutputMarkupId(true);

        final TextField name = new TextField("name", new PropertyModel(user, "name"));
        name.setOutputMarkupId(true);

        AjaxButton ajaxButton = new AjaxButton("submit"){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form){
                super.onSubmit();

                if(login.getInput() != "" && login.getInput().length() <= 10){
                    if(name.getInput() != "" && login.getInput().length() <= 20) {
                        if (password.getInput() != "" && login.getInput().length() <= 10) {

                            ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
                            H2DAO h2DAO = (H2DAO) context.getBean("H2DAO");

                            boolean flag = false;

                            for (User user : h2DAO.getAllUserList()) {
                                if (user.getLogin().hashCode() == login.getInput().hashCode())
                                    flag = true;
                            }

                            if (flag == true) {
                                //Login does exist
                                labelName.setDefaultModelObject("");
                                labelPassword.setDefaultModelObject("");
                                labelLogin.setDefaultModelObject("Login does exist!");

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
                                    labelName.setDefaultModelObject("");
                                    labelLogin.setDefaultModelObject("");
                                    labelPassword.setDefaultModelObject("Password is too short!");
                                }
                            }
                        } else {
                            //Password is not entered
                            labelName.setDefaultModelObject("");
                            labelLogin.setDefaultModelObject("");
                            labelPassword.setDefaultModelObject("Password is not entered or length more then 10 symbols");
                        }
                    } else {
                        //Name is not entered
                        labelPassword.setDefaultModelObject("");
                        labelLogin.setDefaultModelObject("");
                        labelName.setDefaultModelObject("Name is not entered or length more then 20 symbols");
                    }
                } else {
                    //Login is not entered
                    labelName.setDefaultModelObject("");
                    labelPassword.setDefaultModelObject("");
                    labelLogin.setDefaultModelObject("Login is not entered or length more then 10 symbols");
                }

                target.add(labelLogin);
                target.add(labelPassword);
                target.add(labelName);

            }
        };

        Form sign = new Form("form2");

        Button signBtn = new Button("sign"){
            @Override
            public void onSubmit(){
                super.onSubmit();


                setResponsePage(LoginPage.class);
            }
        };
        sign.add(signBtn);
        add(sign);

        add(form);

        form.add(login);
        form.add(labelLogin);

        form.add(name);
        form.add(labelName);

        form.add(password);
        form.add(labelPassword);

        form.add(ajaxButton);

    }
}
