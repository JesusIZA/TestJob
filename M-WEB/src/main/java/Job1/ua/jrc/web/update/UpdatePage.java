package Job1.ua.jrc.web.update;

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

public class UpdatePage extends WebPage {

    public UpdatePage(PageParameters parameters){

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        final H2DAO h2DAO = (H2DAO) context.getBean("H2DAO");

        final String loginer = parameters.get("userLoginer").toString();

        final User user = new User();

        add(new Label("header", "Enter your NEW LOGIN, NAME and PASSWORD"));

        Form form = new Form("form");

        final Label labelName = new Label("labelName", "");
        labelName.setOutputMarkupId(true);
        labelName.setOutputMarkupPlaceholderTag(true);

        final Label labelPassword = new Label("labelPassword", "");
        labelPassword.setOutputMarkupId(true);
        labelPassword.setOutputMarkupPlaceholderTag(true);

        final TextField login = new TextField("login", new PropertyModel(user, "login"));
        login.setOutputMarkupId(true);
        login.setDefaultModelObject(loginer);
        login.setEnabled(false);

        final TextField password = new TextField("password", new PropertyModel(user, "password"));
        password.setOutputMarkupId(true);
        password.setDefaultModelObject(h2DAO.getUserByLogin(loginer).getPassword());

        final TextField name = new TextField("name", new PropertyModel(user, "name"));
        name.setOutputMarkupId(true);
        name.setDefaultModelObject(h2DAO.getUserByLogin(loginer).getName());

        AjaxButton ajaxButton = new AjaxButton("submit"){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form){
                super.onSubmit();

                if(password.getInput() != ""  && password.getInput().length() <= 10){
                    if(name.getInput() != ""  && name.getInput().length() <= 20) {
                        if(password.getInput().length() < 4){
                            labelName.setDefaultModelObject("");
                            labelPassword.setDefaultModelObject("Password is too short!");
                        } else {

                            User user2 = new User(loginer, name.getInput(), password.getInput());

                            h2DAO.update(user2);

                            PageParameters parameters = new PageParameters();
                            parameters.add("userLoginer", loginer);

                            setResponsePage(WelcomePage.class, parameters);
                        }
                    } else {
                        labelPassword.setDefaultModelObject("");
                        labelName.setDefaultModelObject("Name is not entered or length more then 20 symbols!");
                    }
                } else {
                    labelName.setDefaultModelObject("");
                    labelPassword.setDefaultModelObject("Password is not entered or length more then 10 symbols!");
                }

                target.add(labelName);
                target.add(labelPassword);
            };
        };

        add(form);

        form.add(login);

        form.add(password);
        form.add(labelPassword);

        form.add(name);
        form.add(labelName);

        form.add(ajaxButton);
    }
}
