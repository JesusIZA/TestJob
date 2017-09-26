package Job1.ua.jrc.web.delete;

import Job1.ua.jrc.web.login.LoginPage;
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

public class DeletePage extends WebPage {

    public DeletePage(PageParameters parameters){

        final String loginer = parameters.get("userLoginer").toString();

        final User user = new User();

        add(new Label("header", "Enter your PASSWORD for DELETE profile"));

        Form form = new Form("form");

        final Label labelPassword = new Label("labelPassword", "");
        labelPassword.setOutputMarkupId(true);
        labelPassword.setOutputMarkupPlaceholderTag(true);

        final TextField login = new TextField("login", new PropertyModel(user, "login"));
        login.setOutputMarkupId(true);
        login.setDefaultModelObject(loginer);
        login.setEnabled(false);

        final TextField password = new TextField("password", new PropertyModel(user, "password"));
        password.setOutputMarkupId(true);

        AjaxButton ajaxButton = new AjaxButton("submit"){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form){
                super.onSubmit();

                    if(password.getInput() != ""){

                        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
                        H2DAO h2DAO = (H2DAO) context.getBean("H2DAO");

                            if (password.getInput().hashCode() == h2DAO.getUserByLogin(login.getInput()).getPassword().hashCode()) {
                                h2DAO.delete(login.getInput());
                                System.out.println("delete");
                                setResponsePage(LoginPage.class);
                            } else {
                                labelPassword.setDefaultModelObject("Password is failed!");
                            }
                    } else {
                        labelPassword.setDefaultModelObject("Password is not entered!");
                    }

                target.add(labelPassword);
            }
        };

        add(form);

        form.add(login);
        form.add(password);
        form.add(labelPassword);

        form.add(ajaxButton);
    }
}
